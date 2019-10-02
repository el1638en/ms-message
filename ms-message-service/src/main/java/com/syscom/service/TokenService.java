package com.syscom.service;

import com.syscom.beans.Token;
import com.syscom.exceptions.BusinessException;

/**
 * Contrat d'interface pour la gestion métier des tokens de connexion.
 * 
 */
public interface TokenService {

	/**
	 * Creation d'un token pour un utilisateur
	 * 
	 * @param mail Adresse mail de l'utilisateur
	 * @return token {@link Token}
	 * @throws une exception métier {@link BusinessException}
	 */
	Token createToken(String mail) throws BusinessException;

	/**
	 * Reconstituer un token à partir de sa valeur.
	 * 
	 * @param value valeur du token à reconstituer
	 * @return token {@link Token}
	 */
	Token retrieveToken(String value);
}
