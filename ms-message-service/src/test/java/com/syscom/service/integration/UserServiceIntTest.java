package com.syscom.service.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.apache.commons.collections4.IteratorUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.syscom.AbstractTest;
import com.syscom.beans.User;
import com.syscom.exceptions.BusinessException;
import com.syscom.repository.UserRepository;
import com.syscom.service.UserService;

public class UserServiceIntTest extends AbstractTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	@Test(expected = IllegalArgumentException.class)
	public void testCreateNullUser() throws Exception {
		// GIVEN
		User user = null;

		// WHEN
		userService.create(user);

		// THEN
	}

	@Test(expected = BusinessException.class)
	public void testCreateEmptyUser() throws Exception {
		// GIVEN
		User user = new User();

		// WHEN
		userService.create(user);

		// THEN
	}

	@Test(expected = BusinessException.class)
	public void testCreateUserWithExistMail() throws Exception {
		// GIVEN
		String mail = "test@gmail.com";
		User user_1 = new User();
		user_1.setBirthDay(LocalDate.now());
		user_1.setName("name_1");
		user_1.setFirstName("firstName_1");
		user_1.setMail(mail);
		user_1.setPassword("password_1");
		userRepository.save(user_1);

		User user_2 = new User();
		user_2.setBirthDay(LocalDate.now());
		user_2.setName("name_2");
		user_2.setFirstName("firstName_2");
		user_2.setMail(mail);
		user_2.setPassword("password_2");

		// WHEN
		userService.create(user_2);

		// THEN
	}

	@Test
	public void testCreateUser() throws Exception {
		// GIVEN
		User user = new User();
		user.setBirthDay(LocalDate.now().minusDays(1));
		user.setName("name");
		user.setFirstName("firstName");
		user.setMail("test@gmail.com");
		user.setPassword("password");

		// WHEN
		userService.create(user);

		// THEN
		List<User> users = IteratorUtils.toList(userRepository.findAll().iterator());
		assertThat(users).hasSize(1);
		User userResult = users.get(0);
		assertThat(userResult.getMail()).isEqualTo(user.getMail());
		assertThat(userResult.getName()).isEqualTo(user.getName());
		assertThat(userResult.getFirstName()).isEqualTo(user.getFirstName());
		assertThat(userResult.getPassword()).isEqualTo(user.getPassword());
		assertThat(userResult.getBirthDay()).isEqualTo(user.getBirthDay());

	}

}