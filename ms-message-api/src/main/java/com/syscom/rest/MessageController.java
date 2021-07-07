package com.syscom.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.syscom.dto.MessageDTO;
import com.syscom.exceptions.BusinessException;
import com.syscom.mapper.MessageMapper;
import com.syscom.service.MessageService;
import com.syscom.utils.Fonctions;

@RestController
@RequestMapping(MessageController.PATH)
public class MessageController {

	public static final String PATH = "/api/secured/message";
	private final Logger logger = LoggerFactory.getLogger(MessageController.class);

	@Autowired
	private MessageService messageService;

	@Autowired
	private MessageMapper messageMapper;

	/**
	 * API pour ajouter un nouveau message
	 *
	 * @param messageDTO message à ajouter {@link MessageDTO}
	 * @throws BusinessException Exception fonctionnelle {@link BusinessException}
	 */
	@PostMapping
	@Secured(Fonctions.ROLE_AJOUTER_MESSAGE)
	public void create(@RequestBody MessageDTO messageDTO) throws BusinessException {
		logger.info("Create new message : {}", messageDTO);
		messageService.create(messageMapper.dtoToBean(messageDTO));
		logger.info("message : {} has been created successfully.", messageDTO);
	}

	/**
	 * API pour consulter la liste des messages
	 */
	@GetMapping
	@Secured(Fonctions.ROLE_CONSULTER_MESSAGE)
	public List<MessageDTO> findAll() {
		logger.info("Find all messages.");
		return messageMapper.beansToDtos(messageService.findAll());
	}

	/**
	 * API pour rechercher un message.
	 * 
	 * @param id Identifiant du message.
	 * @return MessageDTO message recherché {@link MessageDTO}
	 * @throws BusinessException Exception fonctionnelle {@link BusinessException}
	 */
	@GetMapping(value = "/{id}")
	@Secured(Fonctions.ROLE_CONSULTER_MESSAGE)
	public MessageDTO findById(@PathVariable("id") Long id) throws BusinessException {
		logger.info("Find message by id : {}.", id);
		return messageMapper.beanToDto(messageService.findById(id));
	}

	/**
	 * API pour modifier un message.
	 *
	 * @param id         identifiant du message
	 * @param patientDTO {@link MessageDTO}
	 * @throws BusinessException Exception fonctionnelle {@link BusinessException}
	 */
	@PutMapping(value = "/{id}")
	@Secured(Fonctions.ROLE_MODIFIER_MESSAGE)
	public MessageDTO update(@PathVariable("id") Long id, @RequestBody MessageDTO messageDTO) throws BusinessException {
		logger.info("Update message {} identified by id {}.", messageDTO, id);
		return messageMapper.beanToDto(messageService.update(id, messageMapper.dtoToBean(messageDTO)));
	}

	/**
	 * API pour supprimer un message.
	 *
	 * @param id identifiant du message à supprimer.
	 * @throws BusinessException Exception fonctionnelle {@link BusinessException}
	 */
	@DeleteMapping(value = "/{id}")
	@Secured(Fonctions.ROLE_SUPPRIMER_MESSAGE)
	public void delete(@PathVariable("id") Long id) throws BusinessException {
		logger.info("Delete message identified by id {}.", id);
		messageService.delete(id);
		logger.info("message identified by ID {} has been deleted successfully.", id);
	}
}
