package com.syscom.repository;

import org.springframework.data.repository.CrudRepository;

import com.syscom.beans.Message;

/**
 * 
 * Repository pour effectuer les CRUD des messages {@link Message}
 *
 */
public interface MessageRepository extends CrudRepository<Message, Long> {

}
