# Student Management System Demo

A demo Student Management System Spring application.

## Database Setup

Use the docker-compose.yml for database initialization.

```bash
docker compose up
```
After the database container is up and running, you need to create a database called "schooldb".
```bash
docker exec -it {container_name} bash
psql -U {postgres_user}
CREATE DATABASE schooldb;
```
You can change the environment variables as you wish in docker-compose.yml and application.properties files.
Default the env variables are as follows:
#### container_name = postgres
#### postgres_user = alptekin