DROP TABLE Users;
DROP TABLE Albums;
DROP TABLE Media;
DROP TABLE UserInfo;
DROP TABLE AlbumRatings;
DROP TABLE AlbumAccess;
DROP TABLE AlbumTags;
DROP TABLE TagCategory;
DROP TABLE UserSubscriptions;

CREATE TABLE Users (
uid INTEGER PRIMARY KEY,
username VARCHAR(127) NOT NULL,
passkey VARCHAR(127),
role INTEGER,
points INTEGER,
status VARCHAR(10),
lastAccessed TIMESTAMP
);

CREATE TABLE UserInfo (
id INTEGER PRIMARY KEY,
uid INTEGER,
name VARCHAR(255),
gender BOOLEAN,
email VARCHAR(255),
address VARCHAR(255),
description VARCHAR(1024),
FOREIGN KEY(uid) REFERENCES Users(uid)
);

CREATE TABLE UserSubscriptions (
id INTEGER PRIMARY KEY,
uid INTEGER,
albumid INTEGER,
lastChecked TIMESTAMP,
FOREIGN KEY(uid) REFERENCES Users(uid),
FOREIGN KEY(albumid) REFERENCES Albums(id)
);

CREATE TABLE Albums (
id INTEGER PRIMARY KEY,
name VARCHAR(255) NOT NULL,
subtitle VARCHAR(255),
owner INTEGER,
coverid INTEGER,
parent INTEGER,
description VARCHAR(1023),
permission VARCHAR(25),
mediaType INTEGER,
lastModifiedDate TIMESTAMP,
FOREIGN KEY(owner) REFERENCES Users(uid),
FOREIGN KEY(coverid) REFERENCES Media(id)
);

CREATE TABLE Media (
id INTEGER PRIMARY KEY,
name VARCHAR(255) NOT NULL,
ext VARCHAR(31) NOT NULL,
albumid INTEGER,
owner INTEGER,
type INTEGER,
lastModifiedDate TIMESTAMP,
FOREIGN KEY(albumid) REFERENCES Albums(id),
FOREIGN KEY(owner) REFERENCES Users(uid)
);

CREATE TABLE AlbumAccess (
id INTEGER PRIMARY KEY,
albumid INTEGER,
owner INTEGER,
visitor INTEGER,
FOREIGN KEY(albumid) REFERENCES Albums(id),
FOREIGN KEY(owner) REFERENCES Users(uid),
FOREIGN KEY (visitor) REFERENCES Users(uid)
);

CREATE TABLE AlbumTags (
id INTEGER PRIMARY KEY,
name VARCHAR(255),
albumid INTEGER,
cateid INTEGER,
relevance INTEGER,
FOREIGN KEY(albumid) REFERENCES Albums(id),
FOREIGN KEY(cateid) REFERENCES Category(id),
UNIQUE (albumid, name) ON CONFLICT IGNORE
);

CREATE TABLE AlbumRatings (
id INTEGER PRIMARY KEY,
rating INTEGER,
albumid INTEGER,
userid INTEGER,
FOREIGN KEY(albumid) REFERENCES Albums(id),
FOREIGN KEY(userid) REFERENCES Users(uid)
);

CREATE TABLE TagCategory (
id INTEGER PRIMARY KEY,
name VARCHAR(255) NOT NULL UNIQUE,
visibility VARCHAR(25)
);

CREATE INDEX AlbumOwner ON Albums(owner);
CREATE INDEX AlbumPermission ON Albums(permission);
CREATE INDEX AlbumAccessOwner ON AlbumAccess(visitor);
CREATE INDEX MediaAlbumID ON Media(albumid);
CREATE INDEX MediaOwnerID ON Media(owner);
CREATE INDEX AlbumTagAlbum ON AlbumTags(albumid);