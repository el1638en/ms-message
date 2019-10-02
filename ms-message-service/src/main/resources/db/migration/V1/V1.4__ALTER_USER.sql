-- Modification de la table des utilisateurs : ajout d'une colonne pour le rôle de l'utilisateur
ALTER TABLE T_USER ADD COLUMN U_ROLE_ID BIGINT;
ALTER TABLE T_USER ADD CONSTRAINT FK_U_ROLE_ID FOREIGN KEY(U_ROLE_ID) REFERENCES T_ROLE(R_ID);
COMMENT ON COLUMN T_USER.U_ROLE_ID IS 'ID du rôle de d''un utilisateur';