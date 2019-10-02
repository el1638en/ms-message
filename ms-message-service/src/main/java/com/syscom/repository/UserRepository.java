package com.syscom.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.syscom.beans.User;

/**
 * 
 * Repository pour effectuer les CRUD des utilisateurs {@link User}
 *
 */

public interface UserRepository extends CrudRepository<User, Long> {

	Optional<User> findOptionalByMail(String mail);

	/**
	 * TODO Rechercher un utilisateur et charger son rôle et ses droits (fonctions).
	 * 
	 * @param mail mail de l'utilisateur
	 * @return un utilisateur {@link User}
	 */
	@Query("select user from User user left join fetch user.role role left join fetch role.fonctions where user.mail =:mail")
	User findWithRoleAndFonctions(@Param("mail") String mail);

}
