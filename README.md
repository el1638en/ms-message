# Spring Boot - ms-message

Spring Boot est un framework Spring qui permet de simplifier le démarrage des applications Web
avec une meilleure gestion des configurations (plus besoin des nombreux fichiers XML de configuration, etc...).
Dans ce cas pratique, nous utilisons Spring Boot pour produire une API Backend Spring Rest.

  1. Le façade Web (module maven ms-message-api)

  Le façade Web est la partie accessible aux clients de l'API. Elle expose les APIs REST.
  Elle utilise les beans service  de la couche métier.Les API REST sont construits avec le framework
[Spring REST](https://projects.spring.io/spring-restdocs/ "link to Spring REST").
  On retrouve l'utilisation des annotations `@RestController`, `@RequestMapping`, `@RequestParam` pour construire les façades REST

  Pour gérer la sécurité des APIs, nous utilisons :
* une authentification par Token : Il s'agit d'une authentification de type Basic avec un login:Mot de passe en Base64.
Si les credentials sont connus, un token de connexion est fournis par l'API. Ce token sera rajouté aux headers des requêtes vers les APIs.
* [Spring Security](https://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle/ "link to Spring Security") : En utilisant l'annotation `@Secured`, les APIs sécurisés
ne sont accessibles qu'aux clients disposant de tokens valides et des droits les autorisant à les exploiter.

2. Les services et les accès à la bdd (module maven ms-message-service)

    - La couche métier (package `com.syscom.service` et `com.syscom.service.impl`) : contient les contrats d'interface et l'inplémentation des services métiers. Elle est disponible dans le module ms-message-service. Pour consulter/modifier des données en BDD, elle s'appuie sur les repositories de la couche DAO.

    - La couche DAO (package `com.syscom.repository`). Elle est composée des interfaces Spring Data qui permettent d'effectuer des requêtes sur la BDD.

    - La couche ORM(JPA/Hibernate, package `com.syscom.beans`). Cette couche réalise le mapping entre les objets Java et les tables de la BDD grâce aux annotations JPA (Java Persistence API)

    - Le versionning des scripts SQL : le versionning des scripts SQL est géré par le framework [flywaydb](https://flywaydb.org "link to flyway").


3. Build de l'image Docker de l'API

Pour générer l'mage Docker de l'API, nous utilisons le plugin [Google JIB (Build Java
Docker Images Better)](https://cloudplatform.googleblog.com/2018/07/introducing-jib-build-java-docker-images-better.html). Il existe d'autres outils de build comme [Fabric8](https://maven.fabric8.io/) ou [Spotify](https://github.com/spotify/docker-maven-plugin).
L'intérêt de Google JIB est sa simplicité d'utilisation (pas besoin de Dockerfile). La création de l'image est gérée full Java par le plugin. Voici la configuration
ajoutée dans le fichier pom.xml :

```xml
<plugin>
  <groupId>com.google.cloud.tools</groupId>
  <artifactId>jib-maven-plugin</artifactId>
  <version>${jib-maven-plugin.version}</version>
  <configuration>
     <from>
        <image>openjdk:8-jdk-alpine</image>
     </from>
     <to>
        <image>${repo.nexus.address}</image>
        <tags>
           <tag>${project.artifactId}-${project.version}</tag>
        </tags>
     </to>
     <container>
        <args>
           <arg>-Djava.security.egd=file:/dev/./urandom</arg>
           <arg>-jar</arg>
        </args>
        <ports>
           <port>8080</port>
        </ports>
     </container>
  </configuration>
</plugin>
```

Pour générer une image et le pusher sur un repository ( par exemple Nexus) :

```
legeric@pl-debian:~/ms-message-api$ mvn compile jib:build
```

Pour générer une image dans le docker local :

```
legeric@pl-debian:~/ms-message-api$ mvn compile jib:dockerBuild
```
