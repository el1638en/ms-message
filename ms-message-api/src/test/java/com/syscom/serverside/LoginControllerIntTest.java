package com.syscom.serverside;

import static com.syscom.enums.EnumRole.USERS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.Base64Utils;

import com.syscom.beans.Role;
import com.syscom.beans.User;
import com.syscom.repository.RoleRepository;
import com.syscom.rest.LoginController;
import com.syscom.service.UserService;

public class LoginControllerIntTest extends AbstractIntTest {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserService userService;

	private User user;

	@Before
	public void setup() {
		Role role = roleRepository.findByCode(USERS.name());
		user = User.builder().mail(MAIL).password(PASSWORD).name(NAME).firstName(FIRST_NAME).birthDay(BIRTH_DAY)
				.role(role).build();
	}

	@Test
	public void testAuthenticationWithWrongCredentials() throws Exception {
		// GIVEN
		userService.create(user);
		String badCredentials = StringUtils.join("BAD_LOGIN", ":", "BAD_PASSWORD");

		// WHEN

		// THEN
		mockMvc.perform(MockMvcRequestBuilders.get(LoginController.PATH).header(HttpHeaders.AUTHORIZATION,
				"Basic " + Base64Utils.encodeToString(badCredentials.getBytes()))).andExpect(status().isUnauthorized());
	}

	@Test
	public void testAuthentication() throws Exception {
		// GIVEN
		userService.create(user);

		// WHEN

		// THEN
		mockMvc.perform(MockMvcRequestBuilders.get(LoginController.PATH).header(HttpHeaders.AUTHORIZATION,
				"Basic " + Base64Utils.encodeToString(StringUtils.join(MAIL, ":", PASSWORD).getBytes())))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$").isNotEmpty()).andExpect(jsonPath("$.value").isNotEmpty())
				.andExpect(jsonPath("$.userName").value(NAME)).andExpect(jsonPath("$.userFirstName").value(FIRST_NAME));
	}

	@Test
	public void testAuth() throws Exception {
		// GIVEN
		userService.create(user);

		// WHEN
		String token = getAccessToken(MAIL, PASSWORD);

		// THEN

		assertThat(token).isNotNull();
	}

}
