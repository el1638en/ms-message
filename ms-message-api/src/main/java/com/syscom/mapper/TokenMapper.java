package com.syscom.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.syscom.beans.Token;
import com.syscom.dto.TokenDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR, uses = { FonctionMapper.class })
public interface TokenMapper {

	@Mapping(target = "userName", source = "user.name")
	@Mapping(target = "userFirstName", source = "user.firstName")
	@Mapping(target = "roleCode", source = "user.role.code")
	@Mapping(target = "roleLibelle", source = "user.role.libelle")
	@Mapping(target = "fonctions", source = "user.role.fonctions")
	TokenDTO beanToDto(Token token);

	@InheritInverseConfiguration
	Token dtoToBean(TokenDTO tokenDTO);

}
