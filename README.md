# <h1  align='center'>Kyoku</h1>

## <h2  align='center'>Showcase</h2>

<a  href="https://github.com/POULASTAAdAS/Kyoku/blob/dev/ss/kyoku%20showcase.svg">

<img  src="https://github.com/POULASTAAdAS/Kyoku/blob/dev/ss/kyoku%20showcase.svg"  alt="ShowCase">

</a>

<br>

**Design Link -** [Figma](https://www.figma.com/design/9P5gHYSzu2LtQocpmQ0dBF/Kyoku?node-id=0-1&t=2KhBi5HUyz2zrDJk-1)

<br>

## <h2  align='center'>Description</h2>

> Kyoku is a music streaming application like Spotify or apple music
> It is using HLS protocol to stream music with a backed build using ktor and the application is build using jetpack compose library.
> With over 70 thousand+ songs to play

## <h2  align='center'>Preview</h2>

### Compact

<a href="https://github.com/POULASTAAdAS/Kyoku/blob/dev/ss/dark/compat/google%20auth.gif">
    <img src="https://github.com/POULASTAAdAS/Kyoku/blob/dev/ss/dark/compat/google%20auth.gif" width="200" height="400" alt="Auth Preview">
</a>
<a href="https://github.com/POULASTAAdAS/Kyoku/blob/dev/ss/dark/compat/import%20playlist.gif">
    <img src="https://github.com/POULASTAAdAS/Kyoku/blob/dev/ss/dark/compat/import%20playlist.gif" width="200" height="400" alt="Import Playlist">
</a>
<a href="https://github.com/POULASTAAdAS/Kyoku/blob/dev/ss/dark/compat/select%20genre.gif">
    <img src="https://github.com/POULASTAAdAS/Kyoku/blob/dev/ss/dark/compat/select%20genre.gif" width="200" height="400" alt="Select Genre">
</a>
<a href="https://github.com/POULASTAAdAS/Kyoku/blob/dev/ss/dark/compat/select%20artist.gif">
    <img src="https://github.com/POULASTAAdAS/Kyoku/blob/dev/ss/dark/compat/select%20artist.gif" width="200" height="400" alt="Select Artist">
</a>

### Expanded

<a href="https://github.com/POULASTAAdAS/Kyoku/blob/dev/ss/light/extended/email%20auth.gif">
    <img src="https://github.com/POULASTAAdAS/Kyoku/blob/dev/ss/light/extended/email%20auth.gif" width="400" height="230" alt="Auth Preview">
</a>
<a href="https://github.com/POULASTAAdAS/Kyoku/blob/dev/ss/light/extended/import%20playlist.gif">
    <img src="https://github.com/POULASTAAdAS/Kyoku/blob/dev/ss/light/extended/import%20playlist.gif" width="400" height="230" alt="Import Playlist">
</a>
<a href="https://github.com/POULASTAAdAS/Kyoku/blob/dev/ss/light/extended/select%20genre.gif">
    <img src="https://github.com/POULASTAAdAS/Kyoku/blob/dev/ss/light/extended/select%20genre.gif" width="400" height="230" alt="Select Genre">
</a>
<a href="https://github.com/POULASTAAdAS/Kyoku/blob/dev/ss/light/extended/select%20artist.gif">
    <img src="https://github.com/POULASTAAdAS/Kyoku/blob/dev/ss/light/extended/select%20artist.gif" width="400" height="230" alt="Select Artist">
</a>

<br>
<br>

## <h2  align='center'>Features</h2>

- Supports Every Screen size
- Authentication using google and email
- Auto Sync data with backend on each device

## <h2  align='center'>Technology</h2>

| **Client**      | **Server** |
| --------------- | ---------- |
| [Jetpack Compose](https://developer.android.com/jetpack?gad_source=1&gclid=CjwKCAiAzba9BhBhEiwA7glbanDtWkSrUjunIXb3dKY88xH_WZeZrqg99eS59XMENKk_SXU_buOVqRoClmEQAvD_BwE&gclsrc=aw.ds) |    [Ktor](https://ktor.io/)       |
| Splash Screen   | Exposed    |
| Credentials     | SQL        |
| WorkManager     | Session    |
| Dagger Hilt     | Redis      |
| WindowSizeClass |            |
| Media 3         |            |
| Navigation      |            |
| OkHttp          |            |
| Paging          |            |
| Room            |            |
| Coil            |            |
| Palette         |            |

## <h2  align='center'>Installation</h2>

### Clone Repo

```sh
git  clone  https://github.com/POULASTAAdAS/Movie-Browsing-App.git
```

### Docker

Remove .example from file [.env.example](https://github.com/POULASTAAdAS/Kyoku/blob/dev/docker/.env.example)

Add passwrords on file .env

```kotlin
MYSQL_USER_PASSWORD=your_password
MYSQL_PASSWORD=your_password
REDIS_PASSWORD=your_password
```

**Run** [docker-compose.yml](https://github.com/POULASTAAdAS/Kyoku/blob/dev/docker/docker-compose.yml)

```sh
docker-compose  start  -d
```

### Client

**Setup environment variables on local.properties file of the android app**

```kotlin
BASE_URL="your base url"
CLIENT_ID=abcd
```

**Add SHA1 certificate of the client app(Kyoku). Run this on gradle task**

```sh
signinReport
```

1. Create A Google Cloud Project on [GCP](https://console.cloud.google.com/projectcreate)

2. Create OAuth Creadeintials for android and web [here](https://console.cloud.google.com/apis/credentials)

3. Add the Client Id on local.properties file

### Server

**1. shard-manager**

Remove .example from file [res.json.example](https://github.com/POULASTAAdAS/Kyoku/blob/dev/shard-manager/src/main/resources/res.json.example). Add urls

```json
{
  "storage": {
    "driverClassName": "com.mysql.cj.jdbc.Driver",
    "userJdbcURL": "your jdbc url",
    "kyokuJdbcURL": "your jdbc url",
    "genreArtistShard": "your jdbc url",
    "shardPopularSongUrl": "your jdbc url"
  }
}
```

Run [shard-manager](https://github.com/POULASTAAdAS/Kyoku/tree/dev/shard-manager) backed server.

- This will create needed sharded database tables and populate with data

- shard-manager also updates needed data i.e. song , artist etc popularity add new data at 12 am every day. [Code file](https://github.com/POULASTAAdAS/Kyoku/blob/dev/shard-manager/src/main/kotlin/com/poulastaa/kyoku/shardmanager/app/plugins/ScheduleJobs.kt)

<br>
<br>

**2. kyoku-server**

Remove .example from file [application.conf.example](https://github.com/POULASTAAdAS/Kyoku/blob/dev/kyoku-server/app/src/main/resources/application.conf.example)

- Add all the fields

The databse urls should be same on both [application.conf.example](https://github.com/POULASTAAdAS/Kyoku/blob/dev/kyoku-server/app/src/main/resources/application.conf.example) and [res.json.example](https://github.com/POULASTAAdAS/Kyoku/blob/dev/shard-manager/src/main/resources/res.json.example)

```sh
storage  {
	driverClassName  =  "com.mysql.cj.jdbc.Driver"
	userJdbcURL  =  "your jdbc url"
	kyokuJdbcURL  =  your  jdbc  url"
	genreArtistShard = "your  jdbc  url"
	popularShard = "your  jdbc  url"
}
```

Add Properties as environment variables

```kotlin
BASE_URL=
EMAIL=
PASSWORD=
```

## <h2  align='center'>License</h2>

Designed and developed by Poulastaa Das.

```xml
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

```
