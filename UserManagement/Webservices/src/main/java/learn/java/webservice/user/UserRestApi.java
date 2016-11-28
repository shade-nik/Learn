package learn.java.webservice.user;

import org.springframework.http.ResponseEntity;

import learn.java.dto.user.LearnUser;
import learn.java.dto.user.LearnUsers;

public interface UserRestApi {

	ResponseEntity<LearnUsers> getUsers();

	ResponseEntity<LearnUser> getUserByUserName(String usename);

	ResponseEntity<LearnUser> createUser(LearnUser user);

	ResponseEntity<?> deleteUserByName(String username);

	ResponseEntity<LearnUser> updateUser(String username, LearnUser user);

}
