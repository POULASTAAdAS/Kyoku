# Migration: Local Single DB And Single Redis Setup

Date: 2026-06-21

Purpose: simplify the local Kyoku setup to use one primary database per MySQL database and one Redis instance, with local config-server/discovery/gateway wiring.

This file documents exactly what changed and how to revert it.

## Files Changed

- `/Users/poulastaad/personal/Kyoku/kyoku-docker/docker-compose.yml`
- `/Users/poulastaad/personal/Kyoku/config-server/src/main/resources/application.yml`
- `/Users/poulastaad/personal/Kyoku/discovery/src/main/resources/application.yml`
- `/Users/poulastaad/personal/Kyoku/gateway/src/main/resources/application.yml`
- `/Users/poulastaad/personal/Kyoku/validator/src/main/resources/application.yml`
- `/Users/poulastaad/personal/Kyoku/auth/.gitignore`
- `/Users/poulastaad/personal/Kyoku/auth/src/main/resources/application.yml`
- `/Users/poulastaad/personal/secrates/application-dev.yml`

## Runtime Started During Verification

Docker Compose stack was started from:

```bash
cd /Users/poulastaad/personal/Kyoku/kyoku-docker
docker compose up -d
```

Spring services were started manually with `sh ./gradlew bootRun` because the Gradle wrapper is not executable:

```bash
cd /Users/poulastaad/personal/Kyoku/config-server
CONFIG_REPO_PATH="/Users/poulastaad/personal/secrates" nohup sh ./gradlew bootRun > "/var/folders/98/9_l8k16d3s36vpt0mlp1cvyw0000gp/T/opencode/kyoku-config-server.log" 2>&1 &

cd /Users/poulastaad/personal/Kyoku/discovery
nohup sh ./gradlew bootRun > "/var/folders/98/9_l8k16d3s36vpt0mlp1cvyw0000gp/T/opencode/kyoku-discovery.log" 2>&1 &

cd /Users/poulastaad/personal/Kyoku/gateway
nohup sh ./gradlew bootRun > "/var/folders/98/9_l8k16d3s36vpt0mlp1cvyw0000gp/T/opencode/kyoku-gateway.log" 2>&1 &
```

To stop these manually started Spring services:

```bash
pkill -f "config-server.*gradlew bootRun"
pkill -f "discovery.*gradlew bootRun"
pkill -f "gateway.*gradlew bootRun"
```

If any of ports `8888`, `8001`, or `8080` are still busy after the commands above, identify and stop the remaining Java child process by PID:

```bash
lsof -nP -iTCP:8888 -sTCP:LISTEN
lsof -nP -iTCP:8001 -sTCP:LISTEN
lsof -nP -iTCP:8080 -sTCP:LISTEN

kill <PID_FROM_LSOF>
```

During verification cleanup, these leftover Kyoku processes were stopped:

```text
config-server: 73241 and Gradle wrapper 73219
discovery: 73384 and Gradle wrapper 73329
gateway: 75785 and Gradle wrapper 75763
```

To stop the Docker stack:

```bash
cd /Users/poulastaad/personal/Kyoku/kyoku-docker
docker compose down
```

## docker-compose.yml Changes

File: `/Users/poulastaad/personal/Kyoku/kyoku-docker/docker-compose.yml`

### MySQL Services

The setup now keeps only one primary MySQL container active for each database.

Active MySQL services after migration:

- `user-primary` on host port `1000`
- `playlist-primary` on host port `1010`
- `activity-primary` on host port `1020`
- `content-primary` on host port `1030`

The following MySQL replica/proxy services were commented out:

- `user-replica1`
- `user-proxysql`
- `playlist-replica1`
- `playlist-proxysql`
- `content-replica1`
- `content-replica2`
- `content-proxysql`

`activity-primary` was uncommented/enabled because `/Users/poulastaad/personal/secrates/application-dev.yml` was changed to point `sql.activity` to `localhost:1020`.

### MySQL Replication Init Mounts

These replication init SQL mounts were commented out because replicas/proxies are disabled and the paths were not valid runnable file mounts in the local setup:

- `./mysql/user/sql/setup-replication-master.sql:/docker-entrypoint-initdb.d/setup-replication-master.sql`
- `./mysql/playlist/sql/setup-replication-master.sql:/docker-entrypoint-initdb.d/setup-replication-master.sql`
- `./mysql/activity/sql/setup-replication-master.sql:/docker-entrypoint-initdb.d/setup-replication-master.sql`

To revert, uncomment those mount lines again if replication is being restored.

### Redis Services

The setup now keeps only one Redis container active.

Active Redis service after migration:

- `gateway-redis` on host port `1040`

The following Redis services were commented out:

- `auth-redis` on old host port `1041`
- `playlist-redis` on old host port `1044`
- `content-redis` on old host port `1045`
- `activity-redis` on old host port `1046`

`gateway-redis` password was changed to match the local application config:

Old value:

```yaml
REDIS_PASSWROD: ${REDIS_CONTENT_PASSWORD}
command:
  - redis-server
  - --requirepass
  - ${REDIS_CONTENT_PASSWORD}
```

New value:

```yaml
REDIS_PASSWROD: contentRedis
command:
  - redis-server
  - --requirepass
  - contentRedis
```

To revert, restore the previous Redis env-variable based password and uncomment whichever per-service Redis containers are needed.

### MongoDB Credentials

`activity-nosql` was changed to use literal local credentials so `docker compose up` works without supplying missing environment variables at command time.

Old values:

```yaml
MONGO_INITDB_ROOT_USERNAME: ${MONGO_ACTIVITY_USER}
MONGO_INITDB_ROOT_PASSWORD: ${MONGO_ACTIVITY_PASSWORD}
```

New values:

```yaml
MONGO_INITDB_ROOT_USERNAME: root
MONGO_INITDB_ROOT_PASSWORD: password
```

To revert, restore the old env-variable values.

### RabbitMQ Credentials

`notification-rabbitmq` was changed to use literal local credentials so `docker compose up` works without supplying missing environment variables at command time.

Old values:

```yaml
RABBITMQ_DEFAULT_USER: ${NOTIFICATION_RABBITMQ_USER}
RABBITMQ_DEFAULT_PASS: ${NOTIFICATION_RABBITMQ_PASSWORD}
```

New values:

```yaml
RABBITMQ_DEFAULT_USER: root
RABBITMQ_DEFAULT_PASS: rabbitNotification
```

To revert, restore the old env-variable values.

### Local Docker Volume Paths

Windows-only bind mounts were commented out and replaced with local relative paths so Docker on macOS can start the stack.

MongoDB old path:

```yaml
G:/kyoku/mongo:/data/db
```

MongoDB new path:

```yaml
./data/mongo/activity:/data/db
```

File MinIO old path:

```yaml
G:/minio/file/upload-storage:/data
```

File MinIO new path:

```yaml
./data/minio/file/upload-storage:/data
```

Song MinIO old path:

```yaml
G:/minio/song/upload-storage:/data
```

Song MinIO new path:

```yaml
./data/minio/song/upload-storage:/data
```

To revert for a Windows host, restore the `G:/...` paths and remove/comment the `./data/...` paths.

## config-server application.yml Changes

File: `/Users/poulastaad/personal/Kyoku/config-server/src/main/resources/application.yml`

Config server moved from port `1200` to `8888` because Elasticsearch already uses host port `1200` in the local Docker setup.

Old server port:

```yaml
server:
  port: ${config-server.port:1200}
```

New server port:

```yaml
server:
  port: ${config-server.port:8888}
```

Old metadata/config port:

```yaml
config.port: ${config-server.port:1200}
```

New metadata/config port:

```yaml
config.port: ${config-server.port:8888}
```

Eureka default zone changed from remote to local.

Old value:

```yaml
defaultZone: ${eureka.url:http://kyoku.poulastaa.shop:8001/eureka/}
```

New value:

```yaml
defaultZone: ${eureka.url:http://localhost:8001/eureka/}
```

To revert, switch port/config metadata back to `1200` and restore the remote Eureka URL if needed.

## discovery application.yml Changes

File: `/Users/poulastaad/personal/Kyoku/discovery/src/main/resources/application.yml`

Config server import changed from remote/old port to local config-server on `8888`.

Old value:

```yaml
spring.config.import: optional:configserver:http://kyoku.poulastaa.shop:1200
```

New value:

```yaml
spring.config.import: optional:configserver:http://localhost:8888
```

Config client URI changed from remote/old port to local config-server on `8888`.

Old value:

```yaml
spring.cloud.config.uri: http://kyoku.poulastaa.shop:1200
```

New value:

```yaml
spring.cloud.config.uri: http://localhost:8888
```

Eureka default zone changed from remote to local.

Old value:

```yaml
defaultZone: ${eureka.url:http://kyoku.poulastaa.shop:8001/eureka/}
```

New value:

```yaml
defaultZone: ${eureka.url:http://localhost:8001/eureka/}
```

To revert, restore the remote config-server URL and remote Eureka default zone.

## gateway application.yml Changes

File: `/Users/poulastaad/personal/Kyoku/gateway/src/main/resources/application.yml`

Config-server URL changed from `localhost:1200` to `localhost:8888`.

Old value:

```yaml
config-server.url: http://localhost:1200
```

New value:

```yaml
config-server.url: http://localhost:8888
```

Default gateway Redis password changed from `gatewayRedis` to `contentRedis` to match the single active Redis container.

Old value:

```yaml
password: ${gateway.redis.password:gatewayRedis}
```

New value:

```yaml
password: ${gateway.redis.password:contentRedis}
```

To revert, change config-server URL back to `http://localhost:1200` and Redis password default back to `gatewayRedis`.

## validator application.yml Changes

File: `/Users/poulastaad/personal/Kyoku/validator/src/main/resources/application.yml`

Local config-server and discovery-server variables were added, matching the gateway style:

```yaml
config-server:
  url: http://localhost:8888

discovery-server:
  url: http://localhost:8001/eureka/
```

Config server import changed from remote/old port to local config-server on `8888`.

Old value:

```yaml
spring.config.import: optional:configserver:http://kyoku.poulastaa.shop:1200
```

New value:

```yaml
spring.config.import: optional:configserver:${config-server.url}
```

Config client URI changed from remote/old port to local config-server on `8888`.

Old value:

```yaml
spring.cloud.config.uri: http://kyoku.poulastaa.shop:1200
```

New value:

```yaml
spring.cloud.config.uri: ${config-server.url}
```

Eureka default zone changed from remote to local.

Old value:

```yaml
defaultZone: http://kyoku.poulastaa.shop:8001/eureka/
```

New value:

```yaml
defaultZone: ${eureka.url:${discovery-server.url}}
```

To revert, restore the remote config-server import/URI and remote Eureka default zone, then remove the added local `config-server` and `discovery-server` blocks if no longer needed.

## auth application.yml Changes

File: `/Users/poulastaad/personal/Kyoku/auth/src/main/resources/application.yml`

This file did not exist before this migration. It was added so the auth service follows the same local config-server/discovery pattern as gateway, discovery, config-server, and validator.

`/Users/poulastaad/personal/Kyoku/auth/.gitignore` was also changed to unignore this exact file because the auth module previously ignored `/src/main/resources/application.yml`. Without this exception, the new auth YAML would not appear in normal `git status` or `git add` flows.

Important local defaults added:

```yaml
config-server.url: http://localhost:8888
discovery-server.url: http://localhost:8001/eureka/
server.port: ${auth.port:8082}
spring.config.import: optional:configserver:${config-server.url}
spring.cloud.config.uri: ${config-server.url}
eureka.client.service-url.defaultZone: ${eureka.url:${discovery-server.url}}
```

The auth YAML also maps existing central config keys from `/Users/poulastaad/personal/secrates/application-dev.yml` into the property names currently used by auth code:

```yaml
spring.datasource.url: ${sql.user}
spring.data.redis.host: ${auth.redis.host:localhost}
spring.data.redis.port: ${auth.redis.port:1040}
spring.data.redis.password: ${auth.redis.password:contentRedis}
spring.rabbitmq.host: ${rabbit.notification.host:localhost}
spring.rabbitmq.port: ${rabbit.notification.port:1100}
spring.rabbitmq.username: ${rabbit.notification.username:root}
spring.rabbitmq.password: ${rabbit.notification.password:rabbitNotification}
grpc.client.user.address: ${user-service.grpc.url:discovery:///user}
```

Auth JWT mappings were added because auth code reads keys like `jwt.mail.verify.*`, while the central config stores the mail verification values under `auth.jwt.verify-mail.*`.

To revert, delete `/Users/poulastaad/personal/Kyoku/auth/src/main/resources/application.yml` if the auth service should return to having no local application YAML, and remove the `!/src/main/resources/application.yml` exception from `/Users/poulastaad/personal/Kyoku/auth/.gitignore`.

## External application-dev.yml Changes

File: `/Users/poulastaad/personal/secrates/application-dev.yml`

This file is outside the Kyoku repository.

### Eureka URL

Old value:

```yaml
eureka.url: http://kyoku.poulastaa.shop:8001/eureka/
```

New value:

```yaml
eureka.url: http://localhost:8001/eureka/
```

### SQL URLs

The app was changed to bypass ProxySQL and connect directly to each primary DB.

Old values:

```yaml
sql.user: jdbc:mysql://localhost:1004/USER?user=root&password=userPassword
sql.playlist: jdbc:mysql://localhost:1015/PLAYLIST?user=root&password=playlistPassword
sql.content: jdbc:mysql://localhost:1036/CONTENT?user=root&password=contentPassword
sql.activity: jdbc:mysql://localhost:1024/ACTIVITY?user=root&password=activityPassword
```

New values:

```yaml
sql.user: jdbc:mysql://localhost:1000/USER?user=root&password=userPassword
sql.playlist: jdbc:mysql://localhost:1010/PLAYLIST?user=root&password=playlistPassword
sql.content: jdbc:mysql://localhost:1030/CONTENT?user=root&password=contentPassword
sql.activity: jdbc:mysql://localhost:1020/ACTIVITY?user=root&password=activityPassword
```

To revert, switch the SQL URLs back to the old ProxySQL ports.

### Redis URLs

All app Redis configs were changed to use the single active Redis instance.

New shared Redis values:

```yaml
host: localhost
port: 1040
password: contentRedis
```

Old per-service Redis ports/passwords:

```yaml
auth redis port: 1041
auth redis password: authRedis

user redis port: 1043
user redis password: userRedis

playlist redis port: 1044
playlist redis password: playlistRedis

content redis port: 1045
content redis password: contentRedis

activity redis port: 1046
activity redis password: activityRedis
```

To revert, restore each service-specific Redis port/password and uncomment the matching Redis containers in `docker-compose.yml`.

## Verification Performed

These checks passed after the migration:

```bash
cd /Users/poulastaad/personal/Kyoku/kyoku-docker
docker compose config --quiet
docker compose ps
```

Verified Docker services were running:

- `user-primary`
- `playlist-primary`
- `activity-primary`
- `content-primary`
- `gateway-redis`
- `activity-nosql`
- `notification-rabbitmq`
- `elasticsearch`
- `kibana`
- `kyoku-file-minio`
- `kyoku-song-minio`

Verified MySQL primary pings:

```bash
docker exec user-primary mysqladmin ping -uroot -p"$MYSQL_ROOT_PASSWORD" --silent
docker exec playlist-primary mysqladmin ping -uroot -p"$MYSQL_ROOT_PASSWORD" --silent
docker exec activity-primary mysqladmin ping -uroot -p"$MYSQL_ROOT_PASSWORD" --silent
docker exec content-primary mysqladmin ping -uroot -p"$MYSQL_ROOT_PASSWORD" --silent
```

Verified Redis:

```bash
docker exec gateway-redis redis-cli -a contentRedis ping
```

Expected response:

```text
PONG
```

Verified MongoDB:

```bash
docker exec activity-nosql mongosh --quiet -u root -p password --authenticationDatabase admin --eval 'db.adminCommand({ ping: 1 }).ok'
```

Expected response:

```text
1
```

Verified RabbitMQ:

```bash
docker exec notification-rabbitmq rabbitmq-diagnostics -q ping
```

Expected response:

```text
Ping succeeded
```

Verified Spring services:

```bash
curl -fsS -u 'admin:configServer101$@' http://localhost:8888/actuator/health
curl -fsS http://localhost:8001/actuator/health
curl -fsS http://localhost:8080/actuator/health
curl -fsS -o /dev/null -w 'eureka-gateway %{http_code}\n' http://localhost:8001/eureka/apps/GATEWAY
```

Expected results:

- `config-server` is UP on `localhost:8888`
- `discovery` is UP on `localhost:8001`
- `gateway` is UP on `localhost:8080`
- Gateway is registered in Eureka with HTTP `200`

## Full Revert Procedure

1. Stop manually started Spring services:

```bash
pkill -f "config-server.*gradlew bootRun"
pkill -f "discovery.*gradlew bootRun"
pkill -f "gateway.*gradlew bootRun"
```

2. Stop Docker Compose:

```bash
cd /Users/poulastaad/personal/Kyoku/kyoku-docker
docker compose down
```

3. Revert repository files if you want to discard all repo-local migration edits:

```bash
cd /Users/poulastaad/personal/Kyoku
git checkout -- kyoku-docker/docker-compose.yml
git checkout -- config-server/src/main/resources/application.yml
git checkout -- discovery/src/main/resources/application.yml
git checkout -- gateway/src/main/resources/application.yml
git checkout -- validator/src/main/resources/application.yml
git checkout -- auth/.gitignore
rm auth/src/main/resources/application.yml
```

4. Manually revert `/Users/poulastaad/personal/secrates/application-dev.yml` because it is outside this repository:

- Restore remote `eureka.url` if needed.
- Restore SQL URLs to ProxySQL ports `1004`, `1015`, `1036`, and `1024`.
- Restore per-service Redis ports/passwords.

5. If restoring replicas/proxies, uncomment the replica/proxy services in `docker-compose.yml` and re-enable their related Redis services and replication init SQL mounts as needed.
