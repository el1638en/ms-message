package com.syscom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe Principale de lancement de l'application (exclude = {
 * SecurityAutoConfiguration.class })
 */
@SpringBootApplication
@EnableAutoConfiguration
public class MessageApp {

	public static void main(String[] args) {
		SpringApplication.run(MessageApp.class, args);
	}

}
