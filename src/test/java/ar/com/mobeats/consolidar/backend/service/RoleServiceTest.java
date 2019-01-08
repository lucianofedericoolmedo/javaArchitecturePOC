package ar.com.mobeats.consolidar.backend.service;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.jpa.domain.Specification;

import ar.com.mobeats.consolidar.backend.model.Permiso;
import ar.com.mobeats.consolidar.backend.model.Rol;
import ar.com.mobeats.consolidar.backend.repository.RoleRepository;
import ar.com.mobeats.consolidar.backend.service.exception.NotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class RoleServiceTest {

	private RoleService service;
	
	@Mock
    private RoleRepository roleRepository;
	
	@Before
	public void setUp() throws Exception {
		service = new RoleService();
		service.setRoleRepository(roleRepository);
	}

	@Test
	public void createSuccessfulCallsRepoAndGivesIdToRol() {
		Rol role = buildRole(1L);
		Long expectedId = 1L;
		Rol expectedRole = buildRole(expectedId);
		
		when(roleRepository.save(role)).thenReturn(expectedRole);
		
		Rol saved = service.create(role);
		
		verify(roleRepository).save(role);
		assertEquals(expectedId, saved.getId());
	}

	@Test
	public void updateSuccessfulCallsRepo() {
		Long expectedId = 1L;
		Rol expectedRole = buildRole(expectedId);
		
		when(roleRepository.save(expectedRole)).thenReturn(expectedRole);
		
		service.update(expectedRole);
		
		verify(roleRepository).save(expectedRole);
	}

	@Test
	public void getCallsRepoAndReturnsExpectedRole(){
		Long expectedId = 1L;
		Rol expectedRole = buildRole(expectedId);
		
		when(roleRepository.findOne(expectedId)).thenReturn(expectedRole);
		
		Rol role = service.get(expectedId);
		
		assertEquals(expectedRole, role);
		verify(roleRepository).findOne(expectedId);
	}
	
	@Test(expected = NotFoundException.class)
	public void getThrowsNotFoundExceptionIfNotFound(){
		Long expectedId = 1L;
		when(roleRepository.findOne(expectedId)).thenReturn(null);
		
		service.get(expectedId);
		
		verify(roleRepository).findOne(expectedId);
	}

	@Test
	public void testDelete() {
		Long expectedId = 1L;
		Rol expectedRole = buildRole(expectedId);
		
		doNothing().when(roleRepository).delete(expectedRole);
		
		service.delete(expectedRole);
		
		verify(roleRepository).delete(expectedRole);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testFind() {
		String nombre = "un nombre";
		List<Rol> expectedRoles = buildRoles();
		
		when(roleRepository.findAll(any(Specification.class))).thenReturn(expectedRoles);
		
		List<Rol> roles = service.find(nombre);
		
		assertArrayEquals(expectedRoles.toArray(), roles.toArray());
		verify(roleRepository).findAll(any(Specification.class));
	}

	private List<Rol> buildRoles() {
		List<Rol> roles = new ArrayList<Rol>();
		roles.add(buildRole(1L));
		roles.add(buildRole(2L));
		
		return roles;
	}
	
	private Rol buildRole(long l) {
		Rol role = new Rol();
		role.setId(l);
		role.setNombre("nuevo rol");
		role.setPermisos(new HashSet<Permiso>());
		
		return role;
	}
}
