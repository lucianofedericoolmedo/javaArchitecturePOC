package ar.com.mobeats.consolidar.backend.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ar.com.mobeats.consolidar.backend.model.Permiso;
import ar.com.mobeats.consolidar.backend.repository.PermissionRepository;
import ar.com.mobeats.consolidar.backend.service.PermissionService;

@RunWith(MockitoJUnitRunner.class)
public class PermissionServiceTest {

	private PermissionService service;
	
	@Mock
    private PermissionRepository permissionRepository;
	
	@Before
	public void setUp() throws Exception {
		service = new PermissionService();
		service.setPermissionRepository(permissionRepository);
	}

	@Test
	public void findAll() {
		List<Permiso> expectedPermisos = buildPermisos();

		when(permissionRepository.findAll()).thenReturn(expectedPermisos );
		
		List<Permiso> permisos = service.findAll();
		
		assertArrayEquals(expectedPermisos.toArray(), permisos.toArray());
		verify(permissionRepository).findAll();
	}

	private List<Permiso> buildPermisos() {
		List<Permiso> permisos = new ArrayList<Permiso>();
		permisos.add(buildPermiso(1L));
		permisos.add(buildPermiso(2L));
		return permisos;
	}

	private Permiso buildPermiso(Long id) {
		Permiso permiso = new Permiso();
		permiso.setId(id);
		permiso.setNombre("un nombre");
		return permiso;
	}

}
