package com.syscom.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.syscom.AbstractTest;
import com.syscom.beans.Fonction;
import com.syscom.beans.Role;
import com.syscom.beans.User;

public class UserRepositoryTest extends AbstractTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private FonctionRepository fonctionRepository;

	// Donnees de test pour les utilisateurs
	private static final String MAIL = "mail_user@gmail.com";
	private static final String NAME = "NAME";
	private static final String FIRST_NAME = "FIRST_NAME";
	private static final String PASSWORD = "PASSWORD";
	private static final LocalDate BIRTH_DAY = LocalDate.now().minusDays(1);

	private static User getDefaultUser() {
		return User.builder().name(NAME).firstName(FIRST_NAME).mail(MAIL).password(PASSWORD).birthDay(BIRTH_DAY)
				.build();

	}

	@Test
	public void testFindOptionalByExistMail() throws Exception {
		// GIVEN
		userRepository.save(getDefaultUser());

		// WHEN
		Optional<User> optionalUser = userRepository.findOptionalByMail(MAIL);

		// THEN
		assertThat(optionalUser.isPresent()).isTrue();
		User user = optionalUser.get();
		assertThat(user.getMail()).isEqualTo(MAIL);
		assertThat(user.getFirstName()).isEqualTo(FIRST_NAME);
		assertThat(user.getName()).isEqualTo(NAME);
		assertThat(user.getPassword()).isEqualTo(PASSWORD);
		assertThat(user.getBirthDay()).isEqualTo(BIRTH_DAY);
		assertThat(user.getId()).isNotNull();
	}

	@Test
	public void testFindOptionalByUnknownMail() throws Exception {
		// GIVEN
		userRepository.save(getDefaultUser());

		// WHEN
		Optional<User> optionalUser = userRepository.findOptionalByMail("UNKNOWN_MAIL");

		// THEN
		assertThat(optionalUser.isPresent()).isFalse();
	}

	@Test
	public void testFindOWithRoleAndFonctions() throws Exception {
		// GIVEN
		Fonction fonction = fonctionRepository.save(Fonction.builder().code("FO_CODE").libelle("FO_LIBELLE").build());

		Role role = roleRepository
				.save(Role.builder().code("RO_CODE").libelle("RO_LIBELLE").fonctions(Arrays.asList(fonction)).build());

		User user = getDefaultUser();
		user.setRole(role);
		user = userRepository.save(user);

		// WHEN
		User userResult = userRepository.findWithRoleAndFonctions(MAIL);

		// THEN
		assertThat(userResult).isNotNull();
		assertThat(userResult.getRole()).isNotNull();
		assertThat(userResult.getRole().getCode()).isEqualTo(role.getCode());
		assertThat(userResult.getRole().getLibelle()).isEqualTo(role.getLibelle());
		assertThat(userResult.getRole().getFonctions()).isNotNull();
		assertThat(userResult.getRole().getFonctions().size()).isEqualTo(1);
	}

}
