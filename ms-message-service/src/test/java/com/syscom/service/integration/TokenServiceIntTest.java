package com.syscom.service.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import com.syscom.AbstractTest;
import com.syscom.beans.Token;
import com.syscom.beans.User;
import com.syscom.exceptions.BusinessException;
import com.syscom.repository.RoleRepository;
import com.syscom.service.TokenService;
import com.syscom.service.UserService;

public class TokenServiceIntTest extends AbstractTest {

	@Rule
	public ExpectedException exceptionRule = ExpectedException.none();

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private UserService userService;

	@Test
	public void whenCreateTokenWithNullLoginThenThrowException() throws Exception {
		// GIVEN
		exceptionRule.expect(IllegalArgumentException.class);

		// WHEN
		tokenService.createToken(null);

		// THEN
	}

	@Test
	public void whenCreateTokenForUnknownUserThenThrowException() throws Exception {
		// GIVEN
		exceptionRule.expect(BusinessException.class);

		// WHEN
		tokenService.createToken("user@gmail.com");

		// THEN
	}

	@Test
	public void createToken() throws Exception {
		// GIVEN
		String mail = "test@gmail.com";
		User user = new User();
		user.setBirthDay(LocalDate.now().minusDays(1));
		user.setName("name");
		user.setFirstName("firstName");
		user.setMail(mail);
		user.setPassword("password");
		user.setRole(roleRepository.findByCode("USERS"));
		userService.create(user);

		// WHEN
		Token token = tokenService.createToken(user.getMail());

		// THEN
		assertThat(token).isNotNull();
		assertThat(token.getValue()).isNotNull();
		assertThat(token.getUser()).isEqualTo(user);
		assertThat(token.getDateExpiration()).isAfter(LocalDateTime.now());
	}

}
