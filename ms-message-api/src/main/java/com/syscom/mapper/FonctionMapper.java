package com.syscom.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.syscom.beans.Fonction;
import com.syscom.dto.FonctionDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface FonctionMapper {

	FonctionDTO beanToDto(Fonction fonction);

	@InheritInverseConfiguration
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "roles", ignore = true)
	Fonction dtoToBean(FonctionDTO fonctionDTO);

}
