# MySQL Master-Slave Setup Documentation

## Table of Contents

1. [Overview](#overview)
2. [Architecture](#architecture)
3. [Directory Structure](#directory-structure)
4. [Environment Configuration](#environment-configuration)
5. [Docker Compose Setup](#docker-compose-setup)
6. [Database Schema](#database-schema)
7. [Scripts Documentation](#scripts-documentation)
8. [ProxySQL Configuration](#proxysql-configuration)
9. [Replication Setup Process](#replication-setup-process)
10. [Monitoring and Maintenance](#monitoring-and-maintenance)
11. [Troubleshooting](#troubleshooting)
12. [Best Practices](#best-practices)

## Overview

This is a MySQL Database Replication System, a containerized database solution designed
for high availability and read scalability.
It implements a master-slave replication architecture with ProxySQL for
intelligent query routing and load-balancing, specifically optimized for user management operations.

### Key Features

- **Master-Slave Replication**: 1 primary (master) + 3 replicas (slaves)
- **GTID-based Replication**: Global Transaction Identifiers for consistent replication
- **ProxySQL Load Balancing**: Automatic read/write query routing
- **Automated Setup**: One-click deployment and configuration
- **Health Monitoring**: Built-in replication status checking
- **Docker Containerization**: Consistent deployment across environments

### Use Cases

- Database management systems requiring high read throughput.
- Applications needing database high availability.
- Systems requiring separation of read and write operations.
- Development environments mimicking production setups.

## Architecture

```
┌─────────────────┐    ┌──────────────────┐
│   Application   │───>│    ProxySQL      │
└─────────────────┘    └──────────────────┘
                              │
                    ┌─────────┼───────────────────┐
                    │         │         │         │
              ┌─────▼───┐ ┌───▼───┐ ┌───▼───┐ ┌───▼───┐
              │Primary  │ │Replica│ │Replica│ │Replica│
              │(Master) │ │   1   │ │   2   │ │   3   │
              │Port:1000│ │:1001  │ │:1002  │ │:1003  │
              └─────────┘ └───────┘ └───────┘ └───────┘
                    │         ▲         ▲         ▲
                    └─────────┴─────────┴─────────┘
                           GTID Replication
```

### Component Roles

- **Primary (Master)**: Handles all write operations, source of truth
- **Replicas (Slaves)**: Handle read operations, maintain synchronized copies
- **ProxySQL**: Routes queries based on type:-
    - INSERT/UPDATE/DELETE → primary
    - SELECT → replicas

## Directory Structure

```
D:\KYOKU\KYOKU-DOCKER/
├── .env.example                  # Template for environment setup
├── .gitignore                    # Git ignore rules
├── docker-compose.yml            # Docker services definition
├── run.bat  ──────────────────>  # Main setup and execution script
├── data/
│   └── mysql/                    # Persistent data storage
└── mysql/
    └── user/
        ├── proxy/
        │   └── proxysql.conf.template       # ProxySQL template
        ├── scripts/
        │   ├── check-replication.bat        # Status monitoring
        │   ├── generate-configs.bat         # Config file generation
        │   ├── setup-replication.bat        # Replication setup
        │   ├── start-replication.bat        # Replication starter
        │   └── test-replication.bat         # Functionality testing
        └── sql/
            ├── 01_user_db.sql               # Database schema
            ├── insert_user_db.sql           # Initial data
            └── setup-replication-master.sql.template  # Master setup template
```

## Environment Configuration

### Required Environment Variables

Remove `.example` from file `.env.example` in the root directory.Then add the following variables:

```dotenv
# Root MySQL Password
MYSQL_ROOT_USER_PASSWORD=your_strong_root_password

# Replication User Credentials
MYSQL_USER_REPLICATION_USER=repl_user
MYSQL_USER_REPLICATION_PASSWORD=repl_password

# Application User Credentials
MYSQL_USER_USER=app_user
MYSQL_USER_PASSWORD=app_password

# ProxySQL Admin Credentials
MYSQL_USER_PROXY_ADMIN=proxy_admin
MYSQL_USER_PROXY_PASSWORD=proxy_password

# Monitor User Credentials (for ProxySQL monitoring)
MYSQL_USER_MONITOR_USER=monitor_user
MYSQL_USER_MONITOR_PASSWORD=monitor_password
```

### Security Considerations

- Use strong passwords for all accounts
- Consider using Docker secrets in production
- Rotate passwords regularly
- Limit network access to necessary ports only

## Docker Compose Setup

### Service Definitions

#### Primary MySQL Server (user-primary)

```yaml
user-primary:
  image: mysql:8.4.0
  container_name: user-primary
  ports:
    - "1000:3306"
  environment:
    MYSQL_ROOT_PASSWORD: ${MYSQL_ROOT_USER_PASSWORD}
    MYSQL_REPLICATION_USER: ${MYSQL_USER_REPLICATION_USER}
    MYSQL_REPLICATION_PASSWORD: ${MYSQL_USER_REPLICATION_PASSWORD}
  command: >
    --server-id=1
    --log-bin=mysql-bin
    --binlog-do-db=USER
    --gtid-mode=ON
    --enforce-gtid-consistency=ON
    --log-replica-updates=ON
    --binlog-expire-logs-seconds=604800
```

**Configuration Explained:**

- `server-id=1`: Unique identifier for the primary server
- `log-bin=mysql-bin`: Enables binary logging for replication
- `binlog-do-db=USER`: Only replicate the USER database
- `gtid-mode=ON`: Enables Global Transaction Identifiers
- `enforce-gtid-consistency=ON`: Ensures GTID safety
- `log-replica-updates=ON`: Logs updates from replicas
- `binlog-expire-logs-seconds=604800`: Keeps binary logs for 7 days

#### Replica MySQL Servers (user-replica1)

```yaml
user-replica1:
  image: mysql:8.4.0
  container_name: user-replica1
  ports:
    - "1001:3306"
  environment:
    MYSQL_ROOT_PASSWORD: ${MYSQL_ROOT_USER_PASSWORD}
  command: >
    --server-id=2
    --relay-log=relay-bin
    --read-only=1
    --gtid-mode=ON
    --enforce-gtid-consistency=ON
    --log-replica-updates=ON
    --skip-replica-start=1
```

**Configuration Explained:**

- `server-id=2/3/4`: Unique identifiers for each replica
- `relay-log=relay-bin`: Enables relay logging
- `read-only=1`: Prevents writes on replicas
- `skip-replica-start=1`: Prevents automatic replication start

### Volume Mapping

- SQL initialization files are mounted to `/docker-entrypoint-initdb.d/`
- Data directories are persisted to `./data/mysql/data/[service-name]/data`

## Database Schema

### USER Database Structure

#### UserType Table

```mysql
CREATE TABLE UserType
(
    id     INT PRIMARY KEY AUTO_INCREMENT,
    `type` VARCHAR(8) NOT NULL UNIQUE
);
```

**Purpose**: Stores user authentication types (GOOGLE, EMAIL)
<br>
<br>
**Key Features**:

- Short VARCHAR to optimize storage
- Unique constraint prevents duplicates

#### Country Table

```mysql
CREATE TABLE Country
(
    id      INT PRIMARY KEY AUTO_INCREMENT,
    country VARCHAR(40) NOT NULL,
    code    VARCHAR(4)  NOT NULL,
    UNIQUE KEY uq_country (country),
    UNIQUE KEY uq_code (code)
);
```

**Purpose**: Reference table for country data.
<br>
<br>
**Key Features**:

- Dual unique constraints on country name and code
- Normalized design reduces data redundancy

#### User Table

```mysql
CREATE TABLE `User`
(
    id            BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_type_id  INT          NOT NULL,
    email         VARCHAR(320) NOT NULL,
    username      VARCHAR(320) NOT NULL,
    display_name  VARCHAR(320) NOT NULL,
    password_hash VARCHAR(700) NOT NULL,
    profile_pic   VARCHAR(700) DEFAULT NULL,
    birth_date    DATE         DEFAULT NULL,
    country_id    INT          NOT NULL,
    created_at    TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    last_updated  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    -- Constraints and Indexes
    UNIQUE KEY uq_user_type_email (user_type_id, email),
    INDEX idx_email (email),
    INDEX idx_user_type_id (user_type_id),
    CONSTRAINT fk_user_user_type FOREIGN KEY (user_type_id) REFERENCES UserType (id),
    CONSTRAINT fk_user_country FOREIGN KEY (country_id) REFERENCES Country (id)
);
```

**Design Decisions Explained**:

- `BIGINT` for ID: Accommodates large user bases
- `VARCHAR(320)` for email: Supports maximum RFC-compliant email length
- `VARCHAR(700)` for password_hash: Accommodates various hashing algorithms
- Composite unique key: Prevents duplicate emails per user type
- Foreign key constraints: Ensures referential integrity
- Timestamps: Automatic tracking of creation and modification

#### UserJWTToken Table

```mysql
CREATE TABLE UserJWTToken
(
    user_id       BIGINT PRIMARY KEY,
    refresh_token VARCHAR(1000) NOT NULL,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_token FOREIGN KEY (user_id) REFERENCES `User` (id) ON DELETE CASCADE
);
```

**Purpose**: Stores JWT refresh tokens.
<br>
<br>
**Key Features**:

- One-to-one relationship with User
- Cascade delete ensures cleanup
- Large VARCHAR accommodates JWT tokens

## Scripts Documentation

### run.bat - Main Execution Script

**Purpose**: Primary script that orchestrates the entire setup process

**Execution Flow**:

1. **Docker Validation**: Verifies Docker is running
2. **Configuration Generation**: Calls `generate-configs.bat`
3. **Service Startup**: Executes `docker-compose up -d`
4. **Initialization Wait**: 30-second delay for service initialization
5. **Replication Setup**: Calls `start-replication.bat`

**Error Handling**:

- Exits on Docker unavailability
- Validates each step before proceeding
- Provides clear error messages

**Usage**:

```cmd
run.bat
```

### generate-configs.bat - Configuration File Generator

**Purpose**: Generates configuration files from templates using environment variables

**Process**:

1. Loads environment variables from `.env`
2. Validates template file existence
3. Uses PowerShell string replacement to generate:
    - `proxysql.conf` from `proxysql.conf.template`
    - `setup-replication-master.sql` from `setup-replication-master.sql.template`

**Template Placeholders**:

- `{{MYSQL_USER_PROXY_ADMIN}}` → ProxySQL admin username
- `{{MYSQL_USER_PROXY_PASSWORD}}` → ProxySQL admin password
- `{{MYSQL_USER_USER}}` → Application user
- `{{MYSQL_USER_PASSWORD}}` → Application password
- `{{MYSQL_USER_REPLICATION_USER}}` → Replication user
- `{{MYSQL_USER_REPLICATION_PASSWORD}}` → Replication password
- `{{MYSQL_MONITOR_USER}}` → Monitor user
- `{{MYSQL_MONITOR_PASSWORD}}` → Monitor password

### start-replication.bat — Replication Configuration Script

**Purpose**: Configures master-slave replication across all MySQL instances

**Detailed Process**:

1. **Environment Loading**: Reads credentials from `.env`
2. **Connection Testing**: Verifies primary database accessibility
3. **User Verification**: Ensures replication user exists
4. **GTID Verification**: Confirms GTID mode is enabled
5. **Replica Configuration**: For each replica:
    - Tests connectivity
    - Stops existing replication
    - Resets replication configuration
    - Configures replication source using MySQL 8.x syntax
    - Starts replication
6. **Status Verification**: Checks replication status

**MySQL 8.x Compatibility**:

- Uses `CHANGE REPLICATION SOURCE TO` instead of deprecated `CHANGE MASTER TO`
- Uses `SHOW REPLICA STATUS` instead of `SHOW SLAVE STATUS`
- Uses `SHOW BINARY LOG STATUS` instead of `SHOW MASTER STATUS`

**Error Handling**:

- Retries connection attempts (up to 10 times) *
- Displays specific error messages
- Shows replication errors for troubleshooting

### check-replication.bat — Status Monitoring Script

**Purpose**: Provides comprehensive replication health monitoring

**Monitoring Areas**:

1. **Master Status**: Shows binary log position and file
2. **Replica Status**: For each replica, displays:
    - `Replica_IO_Running`: I/O thread status
    - `Replica_SQL_Running`: SQL thread status
    - `Seconds_Behind_Source`: Replication lag
    - `Last_Error`: Any replication errors
3. **ProxySQL Stats**: Shows server status in ProxySQL

**Output Format**:

```
Master Status
----------------------------------------
File: mysql-bin.000001, Position: 1234

Replica 1 Status
----------------------------------------
Replica_IO_Running: Yes
Replica_SQL_Running: Yes
Seconds_Behind_Source: 0
Last_Error: (empty if no errors)
```

### test-replication.bat - Functionality Testing Script

**Purpose**: Validates replication functionality through data insertion and verification

**Test Process**:

1. **Test Data Generation**: Creates unique test value with timestamp
2. **Primary Insertion**: Inserts test data into the UserType table on primary
3. **Replication Wait**: 3-second delay for replication propagation
4. **Replica Verification**: Checks each replica for test data presence
5. **Cleanup**: Removes test data from primary (propagates to replicas)

**Test Data Structure**:

```sql
INSERT INTO UserType (type)
VALUES ('test');
```

**Success Indicators**:

- Test data appears on all replicas
- No replication errors reported
- Cleanup successful

## ProxySQL Configuration

### Configuration Template Structure

#### Admin Variables

```hocon
admin_variables = {
  admin_credentials = "{{MYSQL_USER_PROXY_ADMIN}}:{{MYSQL_USER_PROXY_PASSWORD}}"
  mysql_ifaces = "0.0.0.0:6032"
}
```

**Purpose**: Administrative access configuration
**Access**: Port 6032 for ProxySQL administration

#### MySQL Variables

```hocon
mysql_variables = {
  threads = 4
  max_connections = 2048
  default_query_delay = 0
  default_query_timeout = 36000000
  interfaces = "0.0.0.0:6033"
  default_schema = "information_schema"
  monitor_username = "{{MYSQL_USER_MONITOR_USER}}"
  monitor_password = "{{MYSQL_USER_MONITOR_PASSWORD}}"
  monitor_history = 600000
  monitor_connect_interval = 60000
  monitor_ping_interval = 10000
}
```

**Key Settings Explained**:

- `threads=4`: Number of worker threads
- `max_connections=2048`: Maximum concurrent connections
- `default_query_timeout=36000000`: 10-hour query timeout
- `interfaces="0.0.0.0:6033"`: MySQL protocol interface
- `monitor_*`: Health monitoring configuration

#### Server Definitions

```lombok.config
mysql_servers = (
    {
        address="user-primary"
        port=3306
        hostgroup=0
        weight=1000
        comment="Primary Server"
    },
    {
        address="user-replica1"
        port=3306
        hostgroup=1
        weight=900
        comment="Read Replica 1"
    }
    # ... additional replicas
)
```

**Hostgroup Strategy**:

- **Hostgroup 0**: Write operations (primary only)
- **Hostgroup 1**: Read operations (all replicas)
- **Weight 900**: Load balancing priority (all replicas have the same priority)

#### Query Routing Rules

```lombok.config
mysql_query_rules
= (
    {
        rule_id=1
        active=1
        match_pattern="^SELECT.*"
        destination_hostgroup=1
        apply=1
        comment="Route SELECT to read replicas"
    },
    {
        rule_id=2
        active=1
        match_pattern="^INSERT|UPDATE|DELETE.*"
        destination_hostgroup=0
        apply=1
        comment="Route writes to primary"
    }
)
```

**Routing Logic**:

- **SELECT queries**: Automatically routed to read replicas (hostgroup 1)
- **INSERT/UPDATE/DELETE**: Routed to primary (hostgroup 0)
- **Pattern matching**: Uses regex for query type detection

## Replication Setup Process

### Phase 1: Environment Preparation

1. Environment variable validation
2. Docker service availability check
3. Configuration file generation from templates
4. Docker Compose service startup

### Phase 2: Primary Configuration

1. Wait for primary database initialization
2. Verify replication user creation
3. Confirm GTID mode activation
4. Validate binary logging configuration

### Phase 3: Replica Configuration

For each replica server:

1. **Connection Testing**: Verify replica accessibility
2. **GTID Validation**: Ensure GTID mode compatibility
3. **Replication Reset**: Clear any existing replication configuration
4. **Source Configuration**: Set primary as replication source
5. **Authentication Setup**: Configure replication user credentials
6. **Auto-positioning**: Enable GTID-based positioning
7. **SSL Configuration**: Set up secure connections
8. **Replication Start**: Initiate replication process

### Phase 4: Validation and Monitoring

1. **Status Verification**: Check replication thread status
2. **Lag Monitoring**: Verify replication lag is minimal
3. **Error Detection**: Identify and report any issues
4. **Functional Testing**: Validate data replication through test insertions

## Monitoring and Maintenance

### Key Performance Indicators (KPIs)

#### Replication Health

- **Replica_IO_Running**: Should always be "Yes"
- **Replica_SQL_Running**: Should always be "Yes"
- **Seconds_Behind_Source**: Should be < 5 seconds under normal load
- **Last_Error**: Should be empty

#### ProxySQL Metrics

- **Connection Count**: Monitor concurrent connections
- **Query Distribution**: Verify read/write query routing
- **Server Status**: Ensure all servers are "ONLINE"

### Automated Monitoring

```
# Check replication status
mysql\user\scripts\check-replication.bat

# Test replication functionality
mysql\user\scripts\test-replication.bat
```

### Manual Health Checks

#### Primary Server Health

```mysql
SHOW BINARY LOG STATUS;

SHOW VARIABLES LIKE 'gtid%';

SELECT COUNT(*)
FROM information_schema.processlist
WHERE command = 'Binlog Dump GTID';
```

#### Replica Health

```mysql
SHOW
    REPLICA STATUS
    \G
SELECT @@read_only;
SHOW
    VARIABLES LIKE 'gtid%';
```

#### ProxySQL Health

```mysql
-- Connect to ProxySQL admin interface (port 6032)
SELECT hostgroup_id, hostname, port, status, weight
FROM mysql_servers;
SELECT rule_id, match_pattern, destination_hostgroup
FROM mysql_query_rules
WHERE active = 1;
```

### Maintenance Tasks

#### Daily Tasks

- Check replication lag
- Monitor error logs
- Verify backup completion
- Review query performance

#### Weekly Tasks

- Rotate binary logs
- Update ProxySQL statistics
- Review capacity metrics
- Test failover procedures

#### Monthly Tasks

- Security audit (password rotation)
- Performance optimization
- Disaster recovery testing
- Documentation updates

## Troubleshooting

### Common Issues and Solutions

#### Issue: Replica Not Starting

**Symptoms**:

- Replica_IO_Running: No
- Last_IO_Error: Connection refused

**Solutions**:

1. **Check Network Connectivity**:
   ```cmd
   docker exec user-replica1 ping user-primary
   ```

2. **Verify Credentials**:
   ```mysql
   -- On primary
   SELECT User, Host FROM mysql.user WHERE User = 'repl_user';
   ```

3. **Reset Replication**:
   ```cmd
   mysql\user\scripts\start-replication.bat
   ```

#### Issue: High Replication Lag

**Symptoms**:

- Seconds_Behind_Source > 30
- Slow query performance on replicas

**Solutions**:

1. **Check Primary Load**:
   ```mysql
   SHOW PROCESSLIST;
   SHOW ENGINE INNODB STATUS;
   ```

2. **Optimize Replica Performance**:
   ```mysql
   -- Increase parallel workers
   SET GLOBAL replica_parallel_workers = 4;
   ```

3. **Review Binary Log Size**:
   ```mysql
   SHOW VARIABLES LIKE 'max_binlog_size';
   ```

#### Issue: ProxySQL Connection Errors

**Symptoms**:

- Application unable to connect
- "Host 'X' is not allowed to connect" errors

**Solutions**:

1. **Check User Privileges**:
   ```mysql
   -- On primary
   SHOW GRANTS FOR 'app_user'@'%';
   ```

2. **Verify ProxySQL Configuration**:
   ```mysql
   -- Connect to ProxySQL admin
   SELECT * FROM mysql_users;
   SELECT * FROM mysql_servers;
   ```

3. **Restart ProxySQL**:
   ```cmd
   docker-compose restart user-proxysql
   ```

#### Issue: GTID Consistency Errors

**Symptoms**:

- "Statement violates GTID consistency"
- Replication stops with GTID errors

**Solutions**:

1. **Check GTID Mode**:
   ```mysql
   SHOW VARIABLES LIKE 'gtid_mode';
   SHOW VARIABLES LIKE 'enforce_gtid_consistency';
   ```

2. **Skip Problematic Transaction** (Use with caution):
   ```mysql
   STOP REPLICA;
   SET GTID_NEXT='UUID:NUMBER';
   BEGIN; COMMIT;
   SET GTID_NEXT='AUTOMATIC';
   START REPLICA;
   ```

### Error Log Analysis

#### Primary Error Log Locations

```shell
# Container logs
docker logs user-primary

# MySQL error logs (inside container)
/var/log/mysql/error.log

# Binary log information
SHOW BINARY LOGS;
```

#### Replica Error Log Analysis

```shell
-- Check specific error details
SHOW
    REPLICA STATUS
    \G

-- Look for common issues
grep -i "error\|warn\|fail" /var/log/mysql/error.log
```

### Performance Optimization

#### Binary Log Optimization

```mysql
-- Reduce binary log retention
SET
    GLOBAL binlog_expire_logs_seconds = 259200;
-- 3 days

-- Optimize binary log format
SET
    GLOBAL binlog_format = 'ROW';
```

#### Replication Optimization

```mysql
-- Parallel replication
SET
    GLOBAL replica_parallel_type = 'LOGICAL_CLOCK';
SET
    GLOBAL replica_parallel_workers = 4;

-- Skip slave errors (development only)
SET
    GLOBAL sql_replica_skip_counter = 1;
```

## Best Practices

### Security Best Practices

#### Password Management

- Use strong, unique passwords for each service
- Rotate passwords regularly (quarterly recommended)
- Store passwords securely (consider Docker secrets)
- Limit password exposure in logs and configuration files

#### Network Security

```yaml
# Example secure network configuration
networks:
  db-network:
    driver: bridge
    internal: true  # Prevents external access
```

#### User Privilege Management

```mysql
-- Principle of least privilege
CREATE
    USER 'app_read_only'@'%' IDENTIFIED BY 'password';
GRANT
    SELECT
        ON USER.* TO 'app_read_only'@'%';

-- Regular privilege audits
SELECT User, Host, authentication_string
FROM mysql.user;
```

### Performance Best Practices

#### MySQL Configuration Tuning

```mysql
-- InnoDB optimization
SET
    GLOBAL innodb_buffer_pool_size = '1G';
SET
    GLOBAL innodb_log_file_size = '256M';
SET
    GLOBAL innodb_flush_log_at_trx_commit = 2;

-- Query cache (if applicable)
SET
    GLOBAL query_cache_size = 67108864;
SET
    GLOBAL query_cache_type = ON;
```

#### ProxySQL Optimization

```hocon
// Connection pooling
mysql_variables = {
  max_connections = 2048
  default_max_latency_ms = 1000
  threshold_query_length = 524288
  threshold_resultset_size = 4194304
}
```

#### Monitoring and Alerting

```shell
# Set up monitoring scripts
#!/bin/bash
# Check replication lag every minute
REPLICATION_LAG=$(mysql -h replica1 -e "SHOW REPLICA STATUS\G" | grep "Seconds_Behind_Source" | awk '{print $2}')

if [ "$REPLICATION_LAG" -gt 30 ]; then
    echo "ALERT: High replication lag: $REPLICATION_LAG seconds" | mail -s "MySQL Alert" admin@company.com
fi
```

### Backup and Recovery

#### Backup Strategy

```shell
# Full backup script
#!/bin/bash
BACKUP_DIR="/backup/mysql"
DATE=$(date +%Y%m%d_%H%M%S)

# Primary backup
docker exec user-primary mysqldump --single-transaction --routines --triggers --all-databases > "$BACKUP_DIR/primary_$DATE.sql"

# Binary log backup
docker exec user-primary mysql -e "FLUSH LOGS;"
docker cp user-primary:/var/lib/mysql/mysql-bin.* "$BACKUP_DIR/"
```

#### Recovery Procedures

```shell
# Point-in-time recovery
mysql < full_backup.sql
mysqlbinlog --start-datetime="2024-01-01 12:00:00" --stop-datetime="2024-01-01 12:30:00" mysql-bin.000001 | mysql
```

### Development vs Production

#### Development Configuration

```yaml
# Reduced resource allocation
environment:
  - MYSQL_ROOT_PASSWORD=dev_password
command: >
  --server-id=1
  --log-bin=mysql-bin
  --binlog-expire-logs-seconds=86400  # 1 day
  --innodb-buffer-pool-size=128M      # Smaller buffer
```

#### Production Configuration

```yaml
# Production optimizations
environment:
  - MYSQL_ROOT_PASSWORD=${SECURE_ROOT_PASSWORD}
command: >
  --server-id=1
  --log-bin=mysql-bin
  --binlog-expire-logs-seconds=604800  # 7 days
  --innodb-buffer-pool-size=2G         # Larger buffer
  --innodb-log-file-size=512M
  --max-connections=1000
```

### Capacity Planning

#### Scaling Considerations

- **Read Scaling**: Add more replicas as read load increases
- **Write Scaling**: Consider sharding or write scaling solutions
- **Storage Growth**: Monitor and plan for data growth
- **Connection Scaling**: Tune ProxySQL connection pooling

#### Resource Monitoring

```mysql
-- Monitor key metrics
SELECT VARIABLE_NAME,
       VARIABLE_VALUE
FROM performance_schema.global_status
WHERE VARIABLE_NAME IN (
                        'Connections',
                        'Queries',
                        'Slow_queries',
                        'Bytes_sent',
                        'Bytes_received'
    );
```

This documentation provides a comprehensive guide to understanding, deploying, and maintaining the KYOKU MySQL
Replication System. Regular updates to this documentation should be made as the system evolves and new requirements
emerge.