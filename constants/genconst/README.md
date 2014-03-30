genconst.py
===========
2014 - Dan Spencer

Generate constants for Java, JavaScript and MySQL

Scenario
--------
A username has a maximum of 25 characters. A user tries to log in with a
30-character username.

There should be length constraints in three places:
    * The database (ie. MySQL)
    * The server's login controller (ie. Java EE servlet, Spring controller)
    * The JavaScript source handling the AJAX request to the controller
    * The HTML input element(s)

How does the programmer guarantee that the constraint is the same in these
domains? If the constant is changed, it should change _everywhere_!

Naïve approach
--------------
Hardcode the constants everywhere

Spring controller (Java):
    public class AccountPOJO {
        @Size(max=25)
        private String username;
        ...
    }

MySQL:
    CREATE TABLE WEB_ACCOUNT(
        username VARCHAR(25) NOT NULL UNIQUE,
        ...
    );

HTML:
    <input name="username" size="25">


Desired approach
----------------
Use a shared constant value declared elsewhere

### Constants declaration file ###
    WebAccount
        USERNAME_MAXLENGTH: 25
    Map
        MAPNAME_MINLENGTH: 3
        MAPNAME_MAXLENGTH: 50

### Spring controller (Java) ###
    public class AccountPOJO {
        @Size(max=Constants.WebAccount.USERNAME_MAXLENGTH)
        private String username;
        ...
    }

### MySQL ###
    CREATE TABLE WEB_ACCOUNT(
        username VARCHAR(@Constants.WebAccount.USERNAME_MAXLENGTH) NOT NULL UNIQUE,
        ...
    );

### HTML ###
    <input name="username" size="${Constants.WebAccount.USERNAME_MAXLENGTH}">



Generating constants
--------------------

    ./genconst.py Constants.txt Constants java Constants.java
    ./genconst.py Constants.txt Constants mysql Constants.sql
    ./genconst.py Constants.txt Constants js Constants.js

### Java ###
    // The "Constants" namespace depends on the arguments provided to the generator
    public final class Constants {
        public static final class WebAccount {
            public static final int USERNAME_MAXLENGTH = 25;
        }
        public static final class Map {
            public static final int MAPNAME_MINLENGTH = 5;
            public static final int MAPNAME_MAXLENGTH = 50;
        }
    }

### MySQL ###
    SET @Constants.WebAccount.USERNAME_MAXLENGTH = 25;
    SET @Constants.Map.MAPNAME_MINLENGTH = 5;
    SET @Constants.Map.MAPNAME_MAXLENGTH = 50;

### JavaScript ###
    var Constants = {
        "WebAccount": {
            "USERNAME_MAXLENGTH": 25
        },
        "Map": {
            "MAPNAME_MINLENGTH": 5,
            "MAPNAME_MAXLENGTH": 50
        }
    }

