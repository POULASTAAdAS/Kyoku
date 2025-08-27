-- Create replication user
CREATE USER IF NOT EXISTS 'user_replicator'@'%' IDENTIFIED WITH caching_sha2_password BY 'password';
GRANT REPLICATION SLAVE ON *.* TO 'user_replicator'@'%';

-- Create monitor user for ProxySQL
CREATE USER IF NOT EXISTS 'monitor'@'%' IDENTIFIED WITH caching_sha2_password BY 'monitor';
GRANT REPLICATION CLIENT ON *.* TO 'monitor'@'%';
GRANT PROCESS ON *.* TO 'monitor'@'%';
GRANT SELECT ON performance_schema.* TO 'monitor'@'%';

-- Ensure root user can connect from anywhere
CREATE USER IF NOT EXISTS 'root'@'%' IDENTIFIED WITH caching_sha2_password BY 'password';
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' WITH GRANT OPTION;

FLUSH PRIVILEGES;