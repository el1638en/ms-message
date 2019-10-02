package com.syscom.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.assertj.core.groups.Tuple;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.syscom.AbstractTest;
import com.syscom.beans.Fonction;
import com.syscom.beans.Role;

public class FonctionRepositoryTest extends AbstractTest {

	private static final String ROLE_CODE = "RO_CODE";
	private static final String ROLE_LIBELLE = "RO_LIBELLE";

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private FonctionRepository fonctionRepository;

	@Before
	public void setUp() {
		Role role = roleRepository.save(Role.builder().code(ROLE_CODE).libelle(ROLE_LIBELLE).build());
		List<Role> roles = Arrays.asList(role);
		fonctionRepository.save(Fonction.builder().code("FONC_1").libelle("FONC_1_LIBELLE").roles(roles).build());
		fonctionRepository.save(Fonction.builder().code("FONC_2").libelle("FONC_2_LIBELLE").roles(roles).build());
	}

	@Test
	public void testFindAllByRoleOK() throws Exception {
		// GIVEN

		// WHEN
		List<Fonction> fonctions = fonctionRepository.findAllByRole(ROLE_CODE);

		// THEN
		assertThat(fonctions).isNotEmpty();
		assertThat(fonctions).hasSize(2);
		assertThat(fonctions).extracting("code", "libelle").containsExactly(Tuple.tuple("FONC_1", "FONC_1_LIBELLE"),
				Tuple.tuple("FONC_2", "FONC_2_LIBELLE"));
	}

	@Test
	public void testFindAllByRoleKO() throws Exception {
		// GIVEN

		// WHEN
		List<Fonction> fonctions = fonctionRepository.findAllByRole("UNKNOWN_ROLE");

		// THEN
		assertThat(fonctions).isEmpty();
	}

}
