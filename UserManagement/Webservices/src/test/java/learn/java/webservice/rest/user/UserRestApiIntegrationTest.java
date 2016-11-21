package learn.java.webservice.rest.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import learn.java.dto.user.LearnUsers;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserRestApiIntegrationTest {

	@Autowired
	private UserRestApi userRestApi;
		
	
	@Test
	public void shouldReturnAllUsers() {
		ResponseEntity<LearnUsers> rp = userRestApi.getAllUsers();
	}
	
	
	
}
