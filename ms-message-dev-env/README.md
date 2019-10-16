## [Docker-compose](https://docs.docker.com/compose/)

Docker Compose est un outil qui permet de décrire dans un fichier YAML (docker-compose.yml) un ensemble
de conteneurs Docker. Docker-compose propose des commandes pour gérer les conteneurs comme un ensemble de services.
Docker-compose est utile dans un environnement de développement Java/J2EE pour
démarrer rapidement une base de données, Elastic Search, etc...
Dans cet exemple, nous allons utiliser `docker-compose` pour configurer les bdd à utiliser.

1. Création de l'image personnalisée de Postgres

Pour cette application, nous avons besoin d'une bdd de :

  - dev local que le développeur utilisera pour démarrer le µS `ms_message`.
  - test dédiée aux tests Junit (`ms_message_tests`).

À partir de l'image oficielle de [Postgres](https://hub.docker.com/_/postgres), nous allons créer une image personnalisée `ms-postgres` qui contient les 2 bases de données.

Dans le répertoire `postgresql`, nous avons un :

- le fichier `initDb.sql` : il contient les commandes pour créer les 2 bdd.
- le fichier `Dockerfile` : il contient les instructions pour copier le fichier `initDb.sql` dans le container et l'exécuter.

Pour créer l'image personnalisée, procéder comme suit :

```
root@pl-debian:~# cd postgresql
root@pl-debian:~# docker image build -t ms-postgres .
Sending build context to Docker daemon  3.072kB
Step 1/3 : FROM postgres:latest
latest: Pulling from library/postgres
76802d77aa81: Pull complete
d84ab156d91d: Pull complete
3e7bd63c53e6: Pull complete
66992dbe6d8f: Pull complete
cb2fe0741e74: Pull complete
Digest: sha256:1c2e0ae6fa018d8547413423200f4e518ddcb1514e8ce67701993dc7f4dd98bf
Status: Downloaded newer image for postgres:latest
 ---> f9b3d2f9a593
Step 2/3 : LABEL description="Postgres SGBD"       maintainer="Eric LEGBA - eric.legba@gmail.com"
 ---> Running in 68d17265ce1f
Removing intermediate container 68d17265ce1f
 ---> 6e84e6562c79
Step 3/3 : COPY initDb.sql /docker-entrypoint-initdb.d/
 ---> 17e551d124e0
Successfully built 17e551d124e0
Successfully tagged ms-postgres:latest
```

2. Fichier docker-compose.yml

Le fichier de configuration `docker-compose.yml` contient la description du service postgres.

  ```YAML
    version: '3.6'

    services:
      ms-postgres:
        image: ms-postgres
        container_name: ms-postgres
        ports:
          - "5432:5432"
        healthcheck:
	       test: ["CMD-SHELL", "pg_isready -U postgres"]
	       interval: 10s
	       timeout: 5s
	       retries: 5
  ```

`docker-compose` va créer le service `ms-postgres`. Ce service va lancer le container `ms-postgres` en utilisant l'image `ms-postgres` que nous avons créée précédemment.
(documentation [Healthcheck](https://docs.docker.com/engine/reference/builder/#healthcheck))

Pour :
  - lancer le service :

    ```
    root@pl-debian:~# cd ms-message-dev-env
    root@pl-debian:~# docker-compose up -d
    Creating ms-postgres ... done
    ```

  - consulter les logs :

    ```
    root@pl-debian:~# docker-compose logs
    ms-postgres    | ****************************************************
    ms-postgres    | waiting for server to start....2019-10-06 21:08:20.975 UTC [44] LOG:  starting PostgreSQL 12.0 (Debian 12.0-1.pgdg100+1) on x86_64-pc-linux-gnu, compiled by gcc (Debian 8.3.0-6) 8.3.0, 64-bit
    ms-postgres    | 2019-10-06 21:08:21.008 UTC [44] LOG:  listening on Unix socket "/var/run/postgresql/.s.PGSQL.5432"
    ms-postgres    | 2019-10-06 21:08:21.119 UTC [45] LOG:  database system was shut down at 2019-10-06 21:08:20 UTC
    ms-postgres    | 2019-10-06 21:08:21.147 UTC [44] LOG:  database system is ready to accept connections
    ms-postgres    |  done
    ms-postgres    | server started
    ms-postgres    |
    ms-postgres    | /usr/local/bin/docker-entrypoint.sh: running /docker-entrypoint-initdb.d/initDb.sql
    ms-postgres    | CREATE ROLE
    ms-postgres    | CREATE DATABASE
    ms-postgres    | CREATE ROLE
    ms-postgres    | CREATE DATABASE
    ```  

  - arrêter le service :

    ```
    root@pl-debian:~# docker-compose stop
    ```   
