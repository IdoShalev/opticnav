Why Docker?
-----------
Docker is a great new way to bundle and deploy your software stack.

The main motivation for creating Docker images is to simplify deployment and to
remove the need to manually install and configure all software components.
Docker offers other benefits as well, such as isolation and the ability
to quickly tear down containers and bring them back up.

Let's face it - configuring Java webapps can be a _pain_. Doing it in a portable
way, even moreso.

If you're not using Docker already, you should be.

Before building
---------------
Before building the Docker images, please run the docker.sh script at the
root of this project. It will populate the needed files used by the Dockerfiles.

Building the images
-------------------
The images are publicly available on the Docker Hub.
If you do not wish to use these images, you can build them yourself:

```bash
cd opticnav-daemon
docker build -t nukep/opticnav-daemon .
cd ..

cd opticnav-webapp
docker build -t nukep/opticnav-webapp .
```

Using the images
----------------
The web app depends on MySQL and the OpticNav device daemon (ARDd).

Create the MySQL and daemon containers first, followed by the web app:

```bash
# OpticNav device daemon
# A port must be opened for listening to devices: <public port>:4444
docker run -d --name=opticnav-daemon -p 4444:4444 nukep/opticnav-daemon

# MySQL
# This automatically creates a database with the proper uses and database. Change as you wish.
docker run -d --name=opticnav-db -e MYSQL_ROOT_PASSWORD=password -e MYSQL_USER=opticnav -e MYSQL_PASSWORD=password -e MYSQL_DATABASE=opticnav mysql

# OpticNav web app
docker run -d --name=opticnav-webapp --link opticnav-daemon:daemon --link opticnav-db:mysql -v /path/to/persist/images:/resources -p 12345:8080 nukep/opticnav-webapp
```
