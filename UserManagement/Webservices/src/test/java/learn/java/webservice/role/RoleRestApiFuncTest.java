package learn.java.webservice.role;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import learn.java.dao.user.RoleEntityDao;
import learn.java.dao.user.UserEntityDao;
import learn.java.dto.user.LearnRole;
import learn.java.dto.user.LearnUser;
import learn.java.entity.user.RoleEntity;
import learn.java.entity.user.UserEntity;
import learn.java.webservice.WebservicesApplication;
import learn.java.webservice.exception.UserNotFoundException;
import learn.java.webservice.user.UserService;
import learn.java.webservice.user.UserServiceTest;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { WebservicesApplication.class}, 
		webEnvironment = WebEnvironment.RANDOM_PORT)
public class RoleRestApiFuncTest {

	private static final String ROLES_TEMPLATE = "http://localhost:%s/roles%s%s";
	private static final String USERS_TEMPLATE = "http://localhost:%s/users%s%s/roles";

	private static final String[] TEST_NAMES = {"test1", "test2", "test3"};
	private static final String TEST_USER = "TestUser";
	private static final String TEST_ROLE = "TEST_ROLE";
	private static final String TEST_NEW_ROLE = "TEST_NEW_ROLE";
	
	private Set<RoleEntity> roles = UserServiceTest.buildRoles(TEST_NAMES);
	private UserEntity user;
	
	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Autowired
	private RoleEntityDao roleDao;
	@Autowired
	private UserEntityDao userDao;
	
	@Autowired 
	private RoleService roleService;
	@Autowired 
	private UserService userService;
	
	@Before
	public void before() {
		for(RoleEntity role : roles) {
			if(!roleDao.findByName(role.getRoleName()).isPresent()) {
				roleDao.save(role);
			}
		}
		if(!roleDao.findByName(TEST_ROLE).isPresent()) {
			roleDao.save(UserServiceTest.buildRoleEntity(TEST_ROLE));
		}
		
		Optional<UserEntity> testUserEntity = userDao.findByUserName(TEST_USER);
		if(!testUserEntity.isPresent()) {
			user = UserServiceTest.buildUserEntity(TEST_USER);
			user.setRoles(null);
			userService.createUser(LearnUser.toDto(user));
			userService.updateUserRoles(TEST_USER, roles.stream().map(LearnRole::toDto).collect(Collectors.toList()));
		}
	}
	
	@After
	public void after() {
		userDao.findByUserName(TEST_USER).ifPresent((user) -> {
			userDao.delete(user);
		});
		for(RoleEntity role : roles) {
			roleDao.findByName(role.getRoleName()).ifPresent((existed) -> {
				roleDao.delete(existed);
			});
		}
		roleDao.findByName(TEST_ROLE).ifPresent(role -> {
			roleDao.delete(role);
		});
		roleDao.findByName(TEST_NEW_ROLE).ifPresent(role -> {
			roleDao.delete(role);
		});
	}

	@Test
	public void shouldFetchAllRoles() {
		ResponseEntity<LearnRole[]> response = testRestTemplate.getForEntity(buildUrl(ROLES_TEMPLATE), LearnRole[].class);

		assertThat(response.hasBody()).isTrue();
		LearnRole[] responseRoles = response.getBody();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseRoles).extracting("roleName").contains(TEST_NAMES);
	}

	@Test
	public void shouldFetchAllRolesStrolglyTyped() {
		ResponseEntity<List<LearnRole>> response = testRestTemplate.exchange(buildUrl(ROLES_TEMPLATE), HttpMethod.GET, null, new ParameterizedTypeReference<List<LearnRole>> () {
		});
		assertThat(response.hasBody()).isTrue();
		List<LearnRole> responseRoles = response.getBody();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseRoles).extracting("roleName").contains(TEST_NAMES);
	}

	@Test
	public void shouldFetchRoleByName() {
		ResponseEntity<LearnRole> response = testRestTemplate.getForEntity(buildUrl(ROLES_TEMPLATE, TEST_NAMES[0]), LearnRole.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.hasBody()).isTrue();
		assertThat(response.getBody().getRoleName()).isEqualTo(TEST_NAMES[0]);
	}
	
	@Test
	@Rollback
	public void shouldCreateNewRole() {
		LearnRole testRole = new LearnRole(TEST_NEW_ROLE,  "");

		ResponseEntity<LearnRole> response = testRestTemplate.postForEntity(buildUrl(ROLES_TEMPLATE), testRole, LearnRole.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.hasBody()).isTrue();
		assertThat(response.getBody().getRoleName()).isEqualTo(TEST_NEW_ROLE);
		Optional<RoleEntity> saved = roleDao.findByName(TEST_NEW_ROLE);
		assertThat(saved.isPresent()).isTrue();
	}

	@Test
	@Rollback
	public void shouldUpdateUserRoles() {
		List<LearnRole> rolesToAdd = new ArrayList<>();
		rolesToAdd.add(new LearnRole(TEST_ROLE,  ""));
		UserEntity entity = userDao.findByUserName(TEST_USER).orElseThrow(AssertionError::new);
		assertThat(entity.getRoles()).hasSize(3);
		
		ResponseEntity<LearnUser> response = testRestTemplate.exchange(buildUrl(USERS_TEMPLATE, TEST_USER), HttpMethod.PUT, new HttpEntity<List<LearnRole>>(rolesToAdd), LearnUser.class);
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.hasBody()).isTrue();
		assertThat(response.getBody().getRoles()).hasSize(4);
		assertThat(response.getBody().getRoles()).extracting("roleName").contains(TEST_NAMES).contains(TEST_ROLE);
	}

	@Test
	@Rollback
	public void shouldDeleteUserRole() {
		UserEntity entity = userDao.findByUserName(TEST_USER).orElseThrow(AssertionError::new);
		assertThat(entity.getRoles()).hasSize(3);
		assertThat(entity.getRoles()).extracting("roleName").contains(TEST_NAMES[0]);
		
		List<LearnRole> rolesToDelete = new ArrayList<>();
		rolesToDelete.add(new LearnRole(TEST_NAMES[0], ""));
		
		ResponseEntity<LearnUser> response = testRestTemplate.exchange(buildUrl(USERS_TEMPLATE, TEST_USER), HttpMethod.DELETE, new HttpEntity<List<LearnRole>>(rolesToDelete), LearnUser.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.hasBody()).isTrue();
		assertThat(response.getBody().getRoles()).hasSize(2);
		assertThat(response.getBody().getRoles()).extracting("roleName").doesNotContain(TEST_NAMES[0]);

	}
	
	private String buildUrl(String template) {
		return buildUrl(template, "");
	}
	
	private String build() {
		String res = null;
		return res;
	}
	
	private String buildUrl(String template, String name) {
		return String.format(template, port, name.isEmpty() ? "":"/", name);
	}
}
