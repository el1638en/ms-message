package com.syscom.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.syscom.beans.User;
import com.syscom.dto.UserDTO;

@RunWith(SpringRunner.class)
@Import({ UserMapperImpl.class, RoleMapperImpl.class, FonctionMapperImpl.class })
public class UserMapperTest {

	@Autowired
	private UserMapper userMapper;

	@Test
	public void testBeanToDto() {
		// GIVEN
		User user = new User();
		user.setBirthDay(LocalDate.now());
		user.setName("name");
		user.setFirstName("firstName");
		user.setMail("mail");
		user.setPassword("password");

		// WHEN
		UserDTO userDTO = userMapper.beanToDto(user);

		// THEN
		assertThat(userDTO).isNotNull();
		assertThat(userDTO.getMail()).isEqualTo(user.getMail());
		assertThat(userDTO.getName()).isEqualTo(user.getName());
		assertThat(userDTO.getFirstName()).isEqualTo(user.getFirstName());
	}

	@Test
	public void testDtoToBean() {
		// GIVEN
		UserDTO userDTO = new UserDTO();
		userDTO.setBirthDay(LocalDate.now());
		userDTO.setName("name");
		userDTO.setFirstName("firstName");
		userDTO.setMail("mail");
		userDTO.setPassword("password");

		// WHEN
		User user = userMapper.dtoToBean(userDTO);

		// THEN
		assertThat(user).isNotNull();
		assertThat(user.getMail()).isEqualTo(userDTO.getMail());
		assertThat(user.getName()).isEqualTo(userDTO.getName());
		assertThat(user.getFirstName()).isEqualTo(userDTO.getFirstName());
		assertThat(user.getPassword()).isEqualTo(userDTO.getPassword());

	}
}
