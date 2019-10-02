package com.syscom.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.syscom.dto.TokenDTO;
import com.syscom.exceptions.BusinessException;
import com.syscom.mapper.TokenMapper;
import com.syscom.service.TokenService;

@RestController
@RequestMapping(LoginController.PATH)
public class LoginController {

	public static final String PATH = "/api/login";
	private final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private TokenService tokenService;

	@Autowired
	private TokenMapper tokenMapper;

	/**
	 * API d'authentification d'un utilisateur.
	 *
	 * @param authotization
	 * @return token
	 * @throws BusinessException
	 * @throws AccessDeniedException
	 */
	@GetMapping
	public TokenDTO login() throws BusinessException, AccessDeniedException {
		logger.info("Login to API with credentials");
		if (SecurityContextHolder.getContext().getAuthentication() == null) {
			throw new AccessDeniedException("Acces refuse");
		}
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		logger.info("Create token for user {}", user);
		return tokenMapper.beanToDto(tokenService.createToken(user.getUsername()));
	}

}
