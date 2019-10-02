-- Création du bdd pour l''environnement de dev local.
CREATE USER ms_message LOGIN password 'ms_message' SUPERUSER;
CREATE DATABASE ms_message OWNER=ms_message;

-- Création du bdd de tests pour les tests Junit.
CREATE USER ms_message_tests LOGIN password 'ms_message_tests' SUPERUSER;
CREATE DATABASE ms_message_tests OWNER=ms_message_tests;
