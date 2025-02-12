
# <h1  align='center'>Kyoku</h1>

## <h2  align='center'>Showcase</h2>

<a  href="https://github.com/POULASTAAdAS/Kyoku/blob/dev/ss/kyoku%20showcase.svg">

<img  src="https://github.com/POULASTAAdAS/Kyoku/blob/dev/ss/kyoku%20showcase.svg"  alt="ShowCase">
</a>

## <h2  align='center'>Description</h2>

> Kyoku is a music streaming application like Spotify or apple music
> It is using HLS protocol to stream music.

## <h2  align='center'>Preview</h2>
### <h2  align='center'>Setup</h2>  

![Setup Compact Dark](https://github.com/POULASTAAdAS/Kyoku/blob/dev/ss/dark/compat/setup.mp4)

![Setup Extended Light](https://github.com/POULASTAAdAS/Kyoku/blob/dev/ss/light/extended/setup.mp4)

  

## <h2  align='center'>Design Link</h2>

  

[Figma](https://www.figma.com/design/9P5gHYSzu2LtQocpmQ0dBF/Kyoku?node-id=0-1&t=2KhBi5HUyz2zrDJk-1)

  

## <h2  align='center'>Installation</h2>

#### Clone Repo
```sh
git clone https://github.com/POULASTAAdAS/Movie-Browsing-App.git
```

#### Docker
 Remove .example from file [.env.example](https://github.com/POULASTAAdAS/Kyoku/blob/dev/docker/.env.example)

Add passwrords on file .env

```sh
MYSQL_USER_PASSWORD=your_password
MYSQL_PASSWORD=your_password
REDIS_PASSWORD=your_password
```
Run [docker-compose.yml](https://github.com/POULASTAAdAS/Kyoku/blob/dev/docker/docker-compose.yml)

```sh
docker-compose start -d
```

#### Client

Setup environment variables on local.properties file of the android app 
>BASE_URL="your base url"
>CLIENT_ID=abcd <p>

Add SHA1 certificate of the client app(Kyoku). Run this on gradle task
```sh
signinReport
```
1. Create A Google Cloud Project on [GCP](https://console.cloud.google.com/projectcreate) 
2. Create OAuth Creadeintials for android and web [here](https://console.cloud.google.com/apis/credentials)
 3. Add the Client Id on local.properties file

#### Server
Remove .example from file [res.json.example](https://github.com/POULASTAAdAS/Kyoku/blob/dev/shard-manager/src/main/resources/res.json.example). Add urls
```sh
{
  "storage": {
    "driverClassName": "com.mysql.cj.jdbc.Driver",
    "userJdbcURL": "your jdbc url",
    "kyokuJdbcURL": "your jdbc url",
    "genreArtistShard": "your jdbc url",
    "shardPopularSongUrl": "your jdbc url",
  }
}
```

Run [shard-manager](https://github.com/POULASTAAdAS/Kyoku/tree/dev/shard-manager) backed server.

 - This will create needed sharded database tables and populate with data
 - shard-manager also updates needed data i.e. song , artist etc popularity add new data at 12 am every day. [Code file](https://github.com/POULASTAAdAS/Kyoku/blob/dev/shard-manager/src/main/kotlin/com/poulastaa/kyoku/shardmanager/app/plugins/ScheduleJobs.kt)

Remove .example from file [application.conf.example](https://github.com/POULASTAAdAS/Kyoku/blob/dev/kyoku-server/app/src/main/resources/application.conf.example)
- Add all the fields

The databse urls should be same on both [application.conf.example](https://github.com/POULASTAAdAS/Kyoku/blob/dev/kyoku-server/app/src/main/resources/application.conf.example) and [res.json.example](https://github.com/POULASTAAdAS/Kyoku/blob/dev/shard-manager/src/main/resources/res.json.example)

```sh
storage {
  driverClassName = "com.mysql.cj.jdbc.Driver"

  userJdbcURL = "your jdbc url"
  kyokuJdbcURL = your jdbc url"
  genreArtistShard = "your jdbc url"
  popularShard = "your jdbc url"
}
```  

Add Properties as environment variables
```sh
BASE_URL=your base url
```
  
 
## <h2  align='center'>License</h2>
Designed and developed by Poulastaa Das

Licensed under [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0)