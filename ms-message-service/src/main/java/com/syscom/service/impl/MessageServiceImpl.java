package com.syscom.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.syscom.beans.Message;
import com.syscom.beans.User;
import com.syscom.exceptions.BusinessException;
import com.syscom.repository.MessageRepository;
import com.syscom.service.MessageService;
import com.syscom.service.ResourceBundleService;

/**
 * Implementation du contrat d'interface des services métiers des messages
 *
 */
@Service
@Transactional
public class MessageServiceImpl implements MessageService {

	@Autowired
	private MessageRepository messageRepository;

	@Autowired
	private ResourceBundleService resourceBundleService;

	@Override
	public Message create(Message message) throws BusinessException {
		Assert.notNull(message, resourceBundleService.getMessage("message.not.null"));
		List<String> errors = validateMessage(message);
		if (!errors.isEmpty()) {
			throw new BusinessException(StringUtils.join(errors, ". "));
		}
		return messageRepository.save(message);
	}

	@Override
	public Message findById(Long id) {
		Assert.notNull(id, resourceBundleService.getMessage("message.id.not.null"));
		Optional<Message> optionalMessage = messageRepository.findById(id);
		return optionalMessage.isPresent() ? optionalMessage.get() : null;
	}

	@Override
	public List<Message> findAll() {
		return StreamSupport.stream(messageRepository.findAll().spliterator(), false).collect(Collectors.toList());
	}

	@Override
	public Message update(Long id, Message message) throws BusinessException {
		Assert.notNull(id, resourceBundleService.getMessage("message.id.not.null"));
		List<String> errors = validateMessage(message);
		if (!errors.isEmpty()) {
			throw new BusinessException(StringUtils.join(errors, ". "));
		}
		Message findMessage = findById(id);
		if (findMessage == null) {
			throw new BusinessException("Unknown message");
		}

		findMessage.setTitle(message.getTitle());
		findMessage.setContent(message.getContent());
		findMessage.setBeginDate(message.getBeginDate());
		findMessage.setEndDate(message.getEndDate());
		return messageRepository.save(findMessage);
	}

	@Override
	public void delete(Long id) throws BusinessException {
		Assert.notNull(id, resourceBundleService.getMessage("message.id.not.null"));
		if (!messageRepository.existsById(id)) {
			throw new BusinessException(resourceBundleService.getMessage("message.unknown.error"));
		}
		messageRepository.deleteById(id);
	}

	/**
	 * Vérifier les données obligatoires de l'utilisateur
	 *
	 * @param user Données de l'utilisateur {@link User}
	 * @return Liste de message d'erreurs
	 */
	private List<String> validateMessage(Message message) {
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = validatorFactory.getValidator();
		Set<ConstraintViolation<Message>> constraintViolations = validator.validate(message);
		if (CollectionUtils.isNotEmpty(constraintViolations)) {
			return constraintViolations.stream()
					.map(violation -> violation.getPropertyPath() + StringUtils.SPACE + violation.getMessage())
					.collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

}
