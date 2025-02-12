# mFlix (Movie-Browsing-App)

> mFlix is a movie browsing navite android application build using jetpack compose and custom backend build using ktor (used only for Authentication) uses [The Movie Database](https://developer.themoviedb.org/reference/intro/getting-started) as data source.

# Features

- Supports Every Screen size
- Authentication using google and email
- A custom library to save your favourite movies and tv shows
- Auto Sync data with custom backend server
- An enhanced search functionality
- upto date and accurate information

| **Client**      | **Server** |
| --------------- | ---------- |
| Jetpack Compose | Ktor       |
| Navigation      | Exposed    |
| OkHttp          | SQL        |
| Paging          | Session    |
| Room            |            |
| Coil            |            |
| Palette         |            |
| WindowSizeClass |            |

# Preview

### Compact

<a href="https://github.com/POULASTAAdAS/Movie-Browsing-App/blob/main/ss/mobile/auth_mFlix.gif">
    <img src="https://github.com/POULASTAAdAS/Movie-Browsing-App/blob/main/ss/mobile/auth_mFlix.gif" width="200" height="400" alt="Auth Preview">
</a>
<a href="https://github.com/POULASTAAdAS/Movie-Browsing-App/blob/main/ss/mobile/compact_home.gif">
    <img src="https://github.com/POULASTAAdAS/Movie-Browsing-App/blob/main/ss/mobile/compact_home.gif" width="200" height="400" alt="Compact Home">
</a>
<a href="https://github.com/POULASTAAdAS/Movie-Browsing-App/blob/main/ss/mobile/compact_search.gif">
    <img src="https://github.com/POULASTAAdAS/Movie-Browsing-App/blob/main/ss/mobile/compact_search.gif" width="200" height="400" alt="Compact Search">
</a>
<a href="https://github.com/POULASTAAdAS/Movie-Browsing-App/blob/main/ss/mobile/compact_profile.gif">
    <img src="https://github.com/POULASTAAdAS/Movie-Browsing-App/blob/main/ss/mobile/compact_profile.gif" width="200" height="400" alt="Compact Profile">
</a>

### Expanded

<a href="https://github.com/POULASTAAdAS/Movie-Browsing-App/blob/main/ss/tab/tab_home.gif">
    <img src="https://github.com/POULASTAAdAS/Movie-Browsing-App/blob/main/ss/tab/tab_home.gif"  alt="Tab Home">
</a>
<a href="https://github.com/POULASTAAdAS/Movie-Browsing-App/blob/main/ss/tab/tab_search.gif">
    <img src="https://github.com/POULASTAAdAS/Movie-Browsing-App/blob/main/ss/tab/tab_search.gif"  alt="Tab Search">
</a>

## Technology

> Client

- [Kotlin](https://kotlinlang.org/) Programing Language
- [Jetpack Compose](https://developer.android.com/jetpack?gad_source=1&gclid=Cj0KCQiA88a5BhDPARIsAFj595jSVle89CMGPqnq6A0C-V8KNDyNR8K_vGQZzUDgCO00VtoKs555fUsaAtXQEALw_wcB&gclsrc=aw.ds) - Ui Library
- [OkHttp](https://square.github.io/okhttp/) - Http Client to make api Request
- [Paging3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview) - A Jetpack compose library to load data as needed
- [Download Manager](https://developer.android.com/reference/kotlin/android/app/DownloadManager) - Efficiantly Download Report as pdf format
- [Room](https://developer.android.com/training/data-storage/room) - SqlLight Database for android
- [Dagger Hilt](https://developer.android.com/training/dependency-injection/hilt-android) - Dependency Management Tool for Android
  > Server
- [Ktor](https://ktor.io/) - evented I/O for the Server
- [Exposed](https://ktor.io/docs/server-integrate-database.html) - Server side Sql Database
- [koin](https://insert-koin.io/) - Dependency management tool for server
- [gradle.kt](https://docs.gradle.org/current/userguide/kotlin_dsl.html) - the streaming build system

## Installation

1. Clone the repo:

```sh
   git clone https://github.com/POULASTAAdAS/Movie-Browsing-App.git
```

2. Create A Google Cloud Project on [GCP](https://console.cloud.google.com/projectcreate)
   2.1 Add SHA1 certificate of the app. Run this on gradle task

```sh
   signinReport
```

2.2 Create OAuth Creadeintials for android and web [here](https://console.cloud.google.com/apis/credentials)
2.3 Setup environment variables on local.properties file of the android app

Create an account on [The Movie Database Api](https://www.themoviedb.org/u/)
Access the Bearer Api Token from [here](https://www.themoviedb.org/settings/api)

> CLIENT_ID=
> BASE_URL=
> API_BASE_URL=https://api.themoviedb.org/3
> API_TOKEN=[YOUR_API_TOKEN](https://www.themoviedb.org/settings/api)
> API_IMAGE_URL=https://image.tmdb.org/t/p/w500

3. Setup enveroinment variables for the server project:

   > clientId=
   > ISSUER=https://accounts.google.com
   > sessionEncryptionKey=
   > sessionSecretKey=

4. Run the docker-compose.yml file on [path](https://github.com/POULASTAAdAS/Movie-Browsing-App/blob/main/mFlexAuth/docker-compose.yml) this will create databsae mFlix and nessery tables for more deatils view [the sql file](https://github.com/POULASTAAdAS/Movie-Browsing-App/blob/main/mFlexAuth/docker/mysql/init.sql)
   3.1 Change the emails on [the sql file](https://github.com/POULASTAAdAS/Leave-Management-System/blob/main/LMSServer/mysql/init.sql) before runing docker compose
   3.2 See [application.conf](https://github.com/POULASTAAdAS/Leave-Management-System/blob/main/LMSServer/src/main/resources/application.conf) file for setting up the urls and port

```sh
    docker compose up -d
```

> By default the server port is exposed on 8083 and database port on 3310
> and you are good to go

# License

```xml
Designed and developed by Poulastaa Das

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
