package com.syscom.service;

import com.syscom.beans.User;
import com.syscom.exceptions.BusinessException;

public interface UserService {

	/**
	 * Rechercher un utilisateur à partir d'une adresse mail.
	 * 
	 * @param mail
	 * @return
	 */
	User findWithRoleAndFonctions(String mail);

	/**
	 * Création d'un nouvel utilisateur.
	 * 
	 * @param userDTO
	 * @throws BusinessException
	 */
	void create(User user) throws BusinessException;
}
