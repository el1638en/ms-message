package com.syscom.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.syscom.beans.User;
import com.syscom.exceptions.BusinessException;
import com.syscom.repository.RoleRepository;
import com.syscom.repository.UserRepository;
import com.syscom.service.ResourceBundleService;
import com.syscom.service.UserService;

/**
 * Implémentation du contrat d'interface des services métiers des utilisateurs
 *
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

	private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

	@Autowired
	private ResourceBundleService resourceBundleService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public void create(User user) throws BusinessException {
		logger.info("Create new user {}", user);
		Assert.notNull(user, resourceBundleService.getMessage("user.not.null"));
		List<String> errors = validateUser(user);
		if (!errors.isEmpty()) {
			throw new BusinessException(StringUtils.join(errors, ". "));
		}

		if (userRepository.findOptionalByMail(user.getMail()).isPresent()) {
			throw new BusinessException(resourceBundleService.getMessage("user.mail.already.used"));
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRole(roleRepository.findByCode("USERS"));
		user = userRepository.save(user);
	}

	/**
	 * Vérifier les données obligatoires de l'utilisateur
	 *
	 * @param user Données de l'utilisateur {@link User}
	 * @return Liste de message d'erreurs
	 */
	private List<String> validateUser(User user) {
//		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
//		Validator validator = validatorFactory.getValidator();
		Set<ConstraintViolation<User>> constraintViolations = validatorFactory.getValidator().validate(user);
		if (CollectionUtils.isNotEmpty(constraintViolations)) {
			return constraintViolations.stream()
					.map(violation -> violation.getPropertyPath() + StringUtils.SPACE + violation.getMessage())
					.collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	@Override
	public User findWithRoleAndFonctions(String mail) {
		return userRepository.findWithRoleAndFonctions(mail);
	}

}
