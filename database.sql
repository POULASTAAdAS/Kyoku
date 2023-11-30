create database musicServer;
use musicServer;

create table MovieSongs (
	songId bigInt primary key auto_increment,
	songUrl text
);

create table MovieSongsMetaData(
	songMetaDataId bigInt primary key auto_increment,
	title text,
    artist text,
    album text, 
    albumCoverUrl text,
    release_year int,
    genre text,
    duration_seconds int,
    songId bigInt references Songs.songId on delete cascade
);

insert into MovieSongs values
( 1, "F://songs//Aashiqui 2//Tum Hi Ho.mp3" ) ,
( 2, "F://songs//Aashiqui 2//Sunn Raha Hai.mp3" ) ,
( 3,"F://songs//Aashiqui 2//Chahun Main Ya Naa.mp3" ) ,
( 4, "F://songs//Aashiqui 2//Hum Mar Jayenge.mp3" ) ,
( 5, "F://songs//Aashiqui 2//Meri Aashiqui.mp3" ) ,
( 6, "F://songs//Aashiqui 2//Piya Aaye Na.mp3" ) ,
( 7, "F://songs//Aashiqui 2//Bhula Dena.mp3" ) ,
( 8, "F://songs//Aashiqui 2//Aasan Nahin Yahan.mp3" ) ,
( 9, "F://songs//Aashiqui 2//Sunn Raha Hai (Female).mp3" ) ,
( 10, "F://songs//Aashiqui 2//Milne Hai Mujhse Aayi.mp3" );


insert into MovieSongsMetaData values
(1 , "Tum Hi Ho" , "Arijit Singh" , "Aashiqui 2" , "F://songs//Aashiqui 2//cover//Aashiqui_2.jpeg" , 2013 , "pop" , 262 , 1),
(2 , "Sunn Raha Hai" , "Ankit Tiwari" , "Aashiqui 2" , "F://songs//Aashiqui 2//cover//Aashiqui_2.jpeg", 2013 ,  "pop" , 390 , 2),
(3 , "Chahun Main Ya Naa" , "Arijit Singh & Palak Muchhal"  , "Aashiqui 2" , "F://songs//Aashiqui 2//cover//Aashiqui_2.jpeg" , 2013 , "pop" , 304 , 3),
(4 , "Hum Mar Jayenge" , "Arijit Singh & Tulsi Kumar"  , "Aashiqui 2" , "F://songs//Aashiqui 2//cover//Aashiqui_2.jpeg" , 2013 , "pop" , 306 , 4),
(5 , "Meri Aashiqui" , "Arijit Singh & Palak Muchhal"  , "Aashiqui 2" , "F://songs//Aashiqui 2//cover//Aashiqui_2.jpeg", 2013 , "pop" , 266 , 5),
(6 , "Piya Aaye Na" , "KK & Tulsi Kumar"  , "Aashiqui 2" ,"F://songs//Aashiqui 2//cover//Aashiqui_2.jpeg", 2013 , "pop" , 286 , 6),
(7 , "Bhula Dena" , "Mustafa Zahid"  , "Aashiqui 2" , "F://songs//Aashiqui 2//cover//Aashiqui_2.jpeg" , 2013 , "pop" , 240 , 7),
(8 , "Aasan Nahin Yahan" , "Arijit Singh"  , "Aashiqui 2" ,"F://songs//Aashiqui 2//cover//Aashiqui_2.jpeg" , 2013 , "pop" , 214 , 8),
(9 , "Sunn Raha Hai (Female)" , "Shreya Ghoshal"  , "Aashiqui 2" , "F://songs//Aashiqui 2//cover//Aashiqui_2.jpeg" , 2013 , "pop" , 214 , 9),
(10 , "Milne Hai Mujhse Aayi" , "Arijit Singh"  , "Aashiqui 2" , "F://songs//Aashiqui 2//cover//Aashiqui_2.jpeg" , 2013 , "pop" , 214 , 10);


select * from MovieSongs;
select * from MovieSongsMetaData;

-- drop table MovieSongsMetaData;
-- drop table songs;

select songUrl from MovieSongs where songId = (select SongId from MovieSongsMetaData where songMetaDataId = 1);
