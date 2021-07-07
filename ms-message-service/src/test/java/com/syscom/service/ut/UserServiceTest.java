package com.syscom.service.ut;

import static com.syscom.enums.EnumRole.USERS;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.syscom.beans.User;
import com.syscom.exceptions.BusinessException;
import com.syscom.repository.RoleRepository;
import com.syscom.repository.UserRepository;
import com.syscom.service.ResourceBundleService;
import com.syscom.service.impl.UserServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private RoleRepository roleRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private ResourceBundleService resourceBundleService;

	@InjectMocks
	private UserServiceImpl userService;

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
		user.setId(1L);
		user.setBirthDay(LocalDate.now().minusDays(1));
		user.setName("name");
		user.setFirstName("firstName");
		user.setMail("test@gmail.com");
		user.setPassword("password");

		// WHEN
		userService.create(user);

		// THEN
		verify(userRepository, times(1)).save(user);
		verify(roleRepository, times(1)).findByCode(USERS.name());
	}

}
