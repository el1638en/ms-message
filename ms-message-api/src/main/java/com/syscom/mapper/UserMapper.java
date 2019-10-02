package com.syscom.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.syscom.beans.User;
import com.syscom.dto.UserDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR, uses = { RoleMapper.class })
public interface UserMapper {

	@Mapping(target = "role", source = "role")
	UserDTO beanToDto(User user);

	@InheritInverseConfiguration
	@Mapping(target = "id", ignore = true)
	User dtoToBean(UserDTO userDTO);

}
