# Kyoku Cloudflare Tunnel

Exposes the local Kyoku **gateway** (`localhost:8080`) to the internet through a named Cloudflare tunnel.

## Summary

| Item              | Value                                                            |
|-------------------|------------------------------------------------------------------|
| Tunnel name       | `kyoku`                                                          |
| Tunnel ID         | `ba9c2d5f-9969-4708-914a-b4882c5330ec`                           |
| Connected domain  | `kyoku-gateway.poulastaa.dev`                                    |
| Public URL        | `https://kyoku-gateway.poulastaa.dev`                            |
| Local target      | `http://localhost:8080` (gateway service)                        |
| Config file       | `~/.cloudflared/config.yml` (copy at `/etc/cloudflared/config.yml`) |
| Credentials file  | `~/.cloudflared/ba9c2d5f-9969-4708-914a-b4882c5330ec.json`       |
| Origin cert       | `~/.cloudflared/cert.pem`                                        |
| Binary            | `/opt/homebrew/bin/cloudflared`                                  |

## Tunnel config

`~/.cloudflared/config.yml`:

```yaml
tunnel: ba9c2d5f-9969-4708-914a-b4882c5330ec
credentials-file: /Users/poulastaad/.cloudflared/ba9c2d5f-9969-4708-914a-b4882c5330ec.json
ingress:
  - hostname: kyoku-gateway.poulastaa.dev
    service: http://localhost:8080
  - service: http_status:404
```

Only `kyoku-gateway.poulastaa.dev` is routed; any other hostname returns `404`.

## How to start

### Option 1: with the local infra script (recommended)

Starts Docker dependencies and opens the tunnel in a separate Terminal window:

```bash
cd kyoku-docker
./start-local-infra.sh
```

Use `--no-tunnel` to skip the tunnel, or override the config with `CLOUDFLARED_CONFIG=/path/to/config.yml`.

### Option 2: tunnel only

```bash
cloudflared tunnel --config ~/.cloudflared/config.yml run kyoku
```

Leave the process running. Stop it with `Ctrl+C`.

### Backend services

The tunnel only forwards traffic. Start the backend manually, in this order:

| Service       | Port   |
|---------------|--------|
| config-server | `8888` |
| discovery     | `8001` |
| gateway       | `8080` |

Then start the remaining services (auth, user, playlist, content, activity, file, validator).

## What to check

### Is the tunnel connected?

```bash
cloudflared tunnel info kyoku
```

`CONNECTIONS` should list active edge connections. `does not have any active connection` means the tunnel is not running.

### Is the gateway up locally?

```bash
curl http://localhost:8080/actuator/health
```

### Is the gateway reachable through the tunnel?

```bash
curl https://kyoku-gateway.poulastaa.dev/actuator/health
```

Expected: `{"status":"UP"}`. A Cloudflare `502`/`1033` error means the tunnel is down or the gateway on `8080` is not running.

### Public API routes (via gateway)

| Path prefix              | Service   |
|--------------------------|-----------|
| `/api/v1/auth/**`        | auth      |
| `/api/v1/user/**`        | user      |
| `/api/v1/playlist/**`    | playlist  |
| `/api/v1/content/**`     | content   |
| `/api/v1/activity/**`    | activity  |
| `/api/v1/file/**`        | file      |
| `/api/v1/validator/**`   | validator |
| `/discovery/**`          | discovery |

Example: `https://kyoku-gateway.poulastaa.dev/api/v1/auth/...`

## Known issues

- **launchd service is broken.** `com.cloudflare.cloudflared` is installed in both `~/Library/LaunchAgents/` and `/Library/LaunchDaemons/`, but it runs `cloudflared` with no arguments. It exits immediately with `use 'cloudflared tunnel run' to start tunnel ...`. Start the tunnel manually as shown above until the service is fixed (add `tunnel` and `run` to `ProgramArguments`, or reinstall with `sudo cloudflared service install`).
- **cloudflared is outdated** (`2026.6.1`). Upgrade with `brew upgrade cloudflared`.
- The gateway's Zipkin endpoint defaults to `http://kyoku-gateway.poulastaa.dev:8010`. The tunnel does not route port `8010`, so override `zipkin.url` to `http://localhost:8010/api/v2/spans` when running locally.

## Other tunnel on this machine

`stampede-management` (`43eb5150-35f2-42cf-84ef-5b725120cf85`) is not part of Kyoku. Its config is in `~/.cloudflared/stampede-management.yml` and it routes `stamped.poulastaa.dev` to `localhost:9990` and `localhost:9991`.
