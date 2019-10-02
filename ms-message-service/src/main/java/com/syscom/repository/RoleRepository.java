package com.syscom.repository;

import org.springframework.data.repository.CrudRepository;

import com.syscom.beans.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {

	Role findByCode(String code);

}
