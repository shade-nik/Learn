package learn.java.webservice.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import learn.java.dao.user.UserEntityDao;
import learn.java.dto.user.LearnUser;
import learn.java.dto.user.LearnUsers;
import learn.java.entity.user.UserEntity;
import learn.java.webservice.WebservicesApplication;
import learn.java.webservice.exception.UserNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { WebservicesApplication.class}, 
		webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserRestApiIntTest {

	private static final String URL_STRING_TEMPLATE = "http://localhost:%s/users%s%s";

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Autowired
	private UserEntityDao userDao;
	
	@Autowired 
	private UserService userService;
	
	@Before
	public void before() {
		userDao.findByUserName("testName").ifPresent((user)-> {
			userDao.delete(user);
		});
		UserEntity userEntity = UserServiceTest.buildUserEntity("testName");
		userDao.save(userEntity);
	}
	
	@After
	public void after() {
		userDao.findByUserName("testName").ifPresent((user)-> {
			userDao.delete(user);
		});
	}
	
	@Test
	public void shouldFetchAllUsers() {
		ResponseEntity<LearnUsers> response = testRestTemplate.getForEntity(buildUrl(), LearnUsers.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.hasBody()).isTrue();
		assertThat(response.getBody());
	}

	@Test
	public void shouldFetchUserByName() {
		ResponseEntity<LearnUser> response = testRestTemplate.getForEntity(buildUrl("testName"), LearnUser.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.hasBody()).isTrue();
		assertThat(response.getBody().getUsername()).isEqualTo("testName");
	}
	
	@Test
	public void shouldCreateUser() {
		LearnUser testUser = new LearnUser("testName", "test@email");
		
		ResponseEntity<LearnUser> response = testRestTemplate.postForEntity(buildUrl(), testUser, LearnUser.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.hasBody()).isTrue();
		assertThat(response.getBody().getUsername()).isEqualTo("testName");
	}

	@Test
	@Rollback
	public void shouldUpdateUser() {
		LearnUser testUser = new LearnUser("testName", "changed@mail");
		
		ResponseEntity<LearnUser> response = testRestTemplate.exchange(buildUrl("testName"), HttpMethod.PUT, new HttpEntity<LearnUser>(testUser), LearnUser.class);

		UserEntity modified = userDao.findByUserName("testName").orElseThrow(AssertionError::new);
		assertThat(modified.getEmail()).isEqualTo(("changed@mail"));
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.hasBody()).isTrue();
		assertThat(response.getBody().getUsername()).isEqualTo("testName");
	}

	@Test(expected = UserNotFoundException.class)
	public void shouldDeleteUser() {

		testRestTemplate.delete(buildUrl("testUser"));

		userService.getByUsername("testUser");
	}
	
	private String buildUrl() {
		return buildUrl("");
	}
	
	private String buildUrl(String username) {
		return String.format(URL_STRING_TEMPLATE, port, username.isEmpty() ? "":"/", username);
	}
}
