package com.syscom.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.syscom.beans.Fonction;

public interface FonctionRepository extends CrudRepository<Fonction, Long> {

	@Query("select f from Fonction f join fetch f.roles r where r.code=:role")
	List<Fonction> findAllByRole(@Param("role") String role);

}
