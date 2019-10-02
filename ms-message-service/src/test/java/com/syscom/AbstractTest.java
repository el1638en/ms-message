package com.syscom;

import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.syscom.config.TestConfiguration;

@ContextConfiguration(classes = { TestConfiguration.class })
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@DataJpaTest
//Désactiver l'utilisation d'une bdd embarqué (H2) pour les tests.
@AutoConfigureTestDatabase(replace = Replace.NONE)
public abstract class AbstractTest {

}