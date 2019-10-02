package com.syscom.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.syscom.beans.Role;
import com.syscom.dto.RoleDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR, uses = { FonctionMapper.class })
public interface RoleMapper {

	@Mapping(target = "fonctions", source = "fonctions")
	RoleDTO beanToDto(Role role);

	@InheritInverseConfiguration
	@Mapping(target = "id", ignore = true)
	Role dtoToBean(RoleDTO roleDTO);
}
