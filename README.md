# Spring Boot - ms-message

Spring Boot est un framework Spring qui permet de simplifier le démarrage des applications Web
avec une meilleure gestion des configurations (plus besoin des nombreux fichiers XML de configuration, etc...).
Dans ce cas pratique, nous utilisons Spring Boot pour produire une API REST.

  1. Le façade Web

Le façade Web expose les données via des APIs REST. Elle est la partie accessible aux clients de l'API.
Toutes les APIs sont disponibles dans le module `ms-message-api.`
Pour construire une API, on utilise les annotations Spring telles que :

* `@RestController` : elle permet à Spring d'identifier la classe portant cette annotation comme étant un contrôleur REST et la liste des requêtes qu'elle est capable d'enregistrer.
* `@RequestMapping` : ajoutée sur une méthode, elle indique la méthode à appeler pour traiter une URI précise.
* `@RequestParam` : elle permet de gérer les paramètres présentes dans une requêtes HTTP. Les paramètres sont au format (field1=value1&field2=value2=field3=value3)

La documentation sur les API REST est disponible [ici](https://projects.spring.io/spring-restdocs/ "link to Spring REST").

Pour sécuriser APIs, nous utilisons :
* une authentification par Token : Il s'agit d'une authentification de type Basic avec un login:Mot de passe en Base64. Si les credentials sont connus, un token est fourni par l'API. Le client doit ajouter ce  token aux headers de ses requêtes.
* [Spring Security](https://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle/ "link to Spring Security") : En utilisant l'annotation `@Secured`, les APIs sécurisés ne sont accessibles qu'aux clients disposant de tokens valides et des droits les autorisant à les exploiter.

Pour tester les APIs, nous utilisons des tests d'intégration qui permettent de tester non seulement les APIs mais aussi les couches de service et les repository.

Pour démarrer un test d'intégration, les annotations ci-dessous sont utilisées :

```java
	 @Transactional
	 @RunWith(SpringRunner.class)
	 @SpringBootTest(classes = MessageApp.class)
	 @AutoConfigureMockMvc
	 @TestPropertySource(locations ="classpath:application-test.yml")
```

Quelques explications sur les annotations utilisées :

* `@SpringBootTest` : cette annotation a pour but de démarrer le serveur d'application à partir de la classe principale `MessageApp.class`,
* `@AutoConfigureMockMvc` : cette annotation permet de gérer la configuration automatique `MockMvc`.
* `@TestPropertySource(locations ="classpath:application-test.yml")` : cette annotation charge dans le fichier `application-test.yml` les informations
nécessaires au déroulement des tests d'intégration. Par exemple, ce fichier contient l'adresse de la bdd Postgres de test. Au lieu d'utiliser la bdd H2 embarquée, il est mieux d'utiliser une bdd réelle pour tester l'application dans des conditions réelles. Voici un aperçu de ce fichier de configuration :


  ```yml
  spring.flyway.schemas: test_ms_message_tests
  logging.level.com.syscom: INFO
  spring.jpa.database: POSTGRESQL
  spring.jpa.properties.hibernate.default_schema: test_ms_message_tests
  spring.datasource.driver-class-name: com.p6spy.engine.spy.P6SpyDriver
  spring.datasource.platform: postgres
  spring.datasource.type: com.zaxxer.hikari.HikariDataSource
  spring.datasource.url: jdbc:p6spy:postgresql://localhost:5432/ms_message_tests
  spring.datasource.username: ms_message_tests
  spring.datasource.password: ms_message_tests
  spring.datasource.maxpoolsize: 3
  logging.level.root: INFOe module contient la configuration des bdd locales pour démarrer le projet.
  logging.level.com.zaxxer: INFO
  token.jwt.duration: 600
  token.jwt.secret: 4pE8z3PBoHjnV1AhvGk+e8h2p+ShZpOnpr8cwHmMh1w=
  ```

  Voici les tests d'intégration pour les services CRUD de messages.

```java

        package com.syscom.serverside;

	    import static com.syscom.TestUtil.APPLICATION_JSON_UTF8;
	    import static com.syscom.TestUtil.convertObjectToJsonBytes;
	    import static com.syscom.enums.EnumRole.USERS;
	    import static org.assertj.core.api.Assertions.assertThat;
	    import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
	    import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

		import java.time.LocalDate;

	    import org.junit.Before;
	    import org.junit.Test;
	    import org.springframework.beans.factory.annotation.Autowired;
	    import org.springframework.http.HttpHeaders;
	    import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

        import com.syscom.beans.Message;
        import com.syscom.beans.Role;
        import com.syscom.beans.User;
        import com.syscom.dto.MessageDTO;
        import com.syscom.repository.RoleRepository;
        import com.syscom.rest.MessageController;
        import com.syscom.service.MessageService;
        import com.syscom.service.UserService;

        public class MessageControllerIntTest extends AbstractIntTest {

            private static final String TITLE = "TITLE";
            private static final String CONTENT = "CONTENT";
            private static final LocalDate BEGIN_DATE = LocalDate.now();
            private static final LocalDate END_DATE = LocalDate.now().plusDays(1);

            @Autowired
            private RoleRepository roleRepository;

            @Autowired
            private UserService userService;

            @Autowired
            private MessageService messageService;

            private User user;

            @Before
            public void setup() throws Exception {
                Role role = roleRepository.findByCode(USERS.name());
                user = User.builder().mail(MAIL).password(PASSWORD).name(NAME).firstName(FIRST_NAME).birthDay(BIRTH_DAY)
                .role(role).build();
                userService.create(user);
            }

            @Test
            public void testCreateMessageWithoutValidToken() throws Exception {
                // GIVEN
                MessageDTO messageDTO = MessageDTO.builder().title(TITLE).content(CONTENT).beginDate(BEGIN_DATE)
                .endDate(END_DATE).build();

                // WHEN

                // THEN
                mockMvc.perform(MockMvcRequestBuilders.post(MessageController.PATH).contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(messageDTO))).andExpect(status().isUnauthorized());
            }

            @Test
            public void testCreateMessageWithWrongToken() throws Exception {
                // GIVEN
                MessageDTO messageDTO = MessageDTO.builder().title(TITLE).content(CONTENT).beginDate(BEGIN_DATE)
                .endDate(END_DATE).build();

                // WHEN

                // THEN
                mockMvc.perform(MockMvcRequestBuilders.post(MessageController.PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer wrongTokendcqscsqcqsvsdvsdfv")
                .contentType(APPLICATION_JSON_UTF8).content(convertObjectToJsonBytes(messageDTO)))
                .andExpect(status().isUnauthorized());e module contient la configuration des bdd locales pour démarrer le projet.
            }

            @Test
            public void testCreateWrongMessage() throws Exception {
                // GIVEN
                MessageDTO messageDTO = new MessageDTO();

                // WHEN

                // THEN
                mockMvc.perform(MockMvcRequestBuilders.post(MessageController.PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken(MAIL, PASSWORD))
                .contentType(APPLICATION_JSON_UTF8).content(convertObjectToJsonBytes(messageDTO)))
                .andExpect(status().isBadRequest());
            }

            @Test
            public void messageServicetestCreateMessage() the module contient la configuration des bdd locales pour démarrer le projet. rows Exception {
                // GIVEN
                MessageDTO messageDTO = MessageDTO.builder().title(TITLE).content(CONTENT).beginDate(BEGIN_DATE)
                .endDate(END_DATE).build();

                // WHEN

                // THEN
                mockMvc.perform(MockMvcRequestBuilders.post(MessageController.PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken(MAIL, PASSWORD))
                .contentType(APPLICATION_JSON_UTF8).content(convertObjectToJsonBytes(messageDTO)))
                .andExpect(status().isOk());
            }

            @Test
            public void testFindAllMessages() throws Exception {
                // GIVEN
                messageService.create(new Message(null, TITLE, CONTENT, BEGIN_DATE, END_DATE));

                // WHEN

                // THEN
                mockMvc.perform(MockMvcRequestBuilders.get(MessageController.PATH).header(HttpHeaders.AUTHORIZATION,
                "Bearer " + getAccessToken(MAIL, PASSWORD))).andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1)).andExpect(jsonPath("$.[0].title").value(TITLE))
                .andExpect(jsonPath("$.[0].content").value(CONTENT));
            }

            @Test
            public void testFindMessageById() throws Exception {
                // GIVEN
                Message message = messageService.create(new Message(null, TITLE, CONTENT, BEGIN_DATE, END_DATE));

                // WHEN

                // THEN
                mockMvc.perform(MockMvcRequestBuilders.get(MessageController.PATH + "/" + message.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken(MAIL, PASSWORD)))
                .andExpect(status().isOk()).andExpect(jsonPath("$.title").value(TITLE))
                .andExpect(jsonPath("$.content").value(CONTENT));
            }


            @Test
            public void testDeleteMessage() throws Exception {
                // GIVEN
                Message message = messageService.create(new Message(null, TITLE, CONTENT, BEGIN_DATE, END_DATE));

                // WHEN

                // THEN
                mockMvc.perform(MockMvcRequestBuilders.delete(MessageController.PATH + "/" + message.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken(MAIL, PASSWORD))
                .contentType(APPLICATION_JSON_UTF8)).andExpect(status().isOk());

                assertThat(messageService.findAll().size()).isEqualTo(0);
            }

        }
    ```


  2. Le module `ms-message-service`

Les APIs REST utilisent le module `ms-message-service` qui s'occupe du traiment des données.  Il contient :

  - La couche métier (package `com.syscom.service` et `com.syscom.service.impl`) : contient les contrats d'interface et l'inplémentation des services métiers.
      Pour consulter/modifier des données en BDD, elle s'appuie sur les repositories de la couche DAO.

  - La couche DAO (package `com.syscom.repository`). Elle est composée des interfaces Spring Data qui permettent d'effectuer des requêtes sur la BDD.

  - La couche ORM(JPA/Hibernate, package `com.syscom.beans`). Cette couche réalise le mapping entre les objets Java et les tables de la BDD grâce aux annotations JPA (Java Persistence API)

  - Le versionning des scripts SQL : le versionning des scripts SQL est géré par le framework [flywaydb](https://flywaydb.org "link to flyway").


  3. `ms-message-dev-env`

Pour démarrer rapidement le projet localement, nous utilisons [Docker-compose](https://docs.docker.com/compose/ "link to Docker-compose").
Le fichier `docker-compose.yml` contient la configuration pour démarrer les bases de données.

Par ailleurs, ce module contient un fichier `postegresql/Dockerfile` qui permet de créer une image personnalisée de PostgreSQL. À partir  de l'image officielle de PostgreSQL, elle crée 2 bases de données (1 bdd pour le dev 
et 1 bdd pour les tests). Pour plus de détails, consulter le fichier README.md.


  4. Build de l'image Docker de l'API

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
