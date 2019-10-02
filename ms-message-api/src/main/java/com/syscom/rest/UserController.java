package com.syscom.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.syscom.dto.UserDTO;
import com.syscom.exceptions.BusinessException;
import com.syscom.mapper.UserMapper;
import com.syscom.service.UserService;

/**
 * API utilisateurs
 *
 */
@RestController
@RequestMapping(UserController.PATH)
public class UserController {

	public static final String PATH = "/api/user";

	private final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private UserMapper userMapper;

	/**
	 * API pour creer un nouvel utilisateur
	 *
	 * @param userDTO {@link UserDTO}
	 * @throws BusinessException Exception fonctionnelle {@link BusinessException}
	 */
	@PostMapping
	public void createUser(@RequestBody UserDTO userDTO) throws BusinessException {
		logger.info("Create user : {}", userDTO);
		userService.create(userMapper.dtoToBean(userDTO));
		logger.info("user {} has been created successfully.", userDTO);
	}

}