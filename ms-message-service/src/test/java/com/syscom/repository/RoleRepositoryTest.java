package com.syscom.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.syscom.AbstractTest;
import com.syscom.beans.Role;

public class RoleRepositoryTest extends AbstractTest {

	private static final String ROLE_CODE = "RO_CODE";
	private static final String ROLE_LIBELLE = "RO_LIBELLE";

	@Autowired
	private RoleRepository roleRepository;

	@Before
	public void setUp() {
		roleRepository.save(Role.builder().code(ROLE_CODE).libelle(ROLE_LIBELLE).build());
	}

	@Test
	public void testFindByCodeOK() throws Exception {
		// GIVEN

		// WHEN
		Role role = roleRepository.findByCode(ROLE_CODE);

		// THEN
		assertThat(role).isNotNull();
		assertThat(role.getCode()).isEqualTo(ROLE_CODE);
		assertThat(role.getLibelle()).isEqualTo(ROLE_LIBELLE);
	}

	@Test
	public void testFindAllByRoleKO() throws Exception {
		// GIVEN

		// WHEN
		Role role = roleRepository.findByCode("UNKNOWN_ROLE");

		// THEN
		assertThat(role).isNull();
	}

}
