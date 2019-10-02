package com.syscom.mapper;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.syscom.beans.Message;
import com.syscom.dto.MessageDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR, uses = {})
public interface MessageMapper {

	MessageDTO beanToDto(Message message);

	@InheritInverseConfiguration
	Message dtoToBean(MessageDTO messageDTO);

	List<MessageDTO> beansToDtos(List<Message> messages);

}
