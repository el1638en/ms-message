package com.syscom.serverside;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.syscom.TestUtil;
import com.syscom.beans.User;
import com.syscom.dto.UserDTO;
import com.syscom.rest.UserController;
import com.syscom.service.UserService;

public class UserControllerIntTest extends AbstractIntTest {

	@Autowired
	private UserService userService;

	@Test
	public void testCreateUserWithoutMandatoryData() throws Exception {
		// GIVEN
		UserDTO userDTO = new UserDTO();

		// WHEN

		// THEN
		mockMvc.perform(MockMvcRequestBuilders.post(UserController.PATH).contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(userDTO))).andExpect(status().isBadRequest());
	}

	@Test
	public void testCreateExistUser() throws Exception {
		// GIVEN
		User user = User.builder().mail(MAIL).password(PASSWORD).name(NAME).firstName(FIRST_NAME).birthDay(BIRTH_DAY)
				.build();
		userService.create(user);

		UserDTO userDTO = UserDTO.builder().mail(MAIL).password(PASSWORD).name(NAME).firstName(FIRST_NAME)
				.birthDay(BIRTH_DAY).build();

		// WHEN

		// THEN
		mockMvc.perform(MockMvcRequestBuilders.post(UserController.PATH).contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(userDTO))).andExpect(status().isBadRequest());
	}

	@Test
	public void testCreateExistUser1() throws Exception {
		// GIVEN
		User user = User.builder().mail(MAIL).password(PASSWORD).name("NAME").firstName("FIRST_NAME")
				.birthDay(BIRTH_DAY).build();
		userService.create(user);
		UserDTO userDTO = UserDTO.builder().mail(MAIL).password(PASSWORD).name(NAME).firstName(FIRST_NAME)
				.birthDay(BIRTH_DAY).build();

		// WHEN

		// THEN
		mockMvc.perform(MockMvcRequestBuilders.post(UserController.PATH).contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(userDTO))).andExpect(status().isBadRequest());
	}

	@Test
	public void testCreateUser() throws Exception {
		// GIVEN
		UserDTO userDTO = UserDTO.builder().name(NAME).firstName(FIRST_NAME).mail(MAIL).password(PASSWORD)
				.birthDay(BIRTH_DAY).build();

		// WHEN

		// THEN
		mockMvc.perform(MockMvcRequestBuilders.post(UserController.PATH).contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(userDTO))).andExpect(status().isOk());
	}

}
