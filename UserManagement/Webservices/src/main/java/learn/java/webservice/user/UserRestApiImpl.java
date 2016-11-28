package learn.java.webservice.user;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import learn.java.dto.user.LearnUser;
import learn.java.dto.user.LearnUsers;

@RestController
@RequestMapping("/users")
public class UserRestApiImpl implements UserRestApi {

	private static final Logger LOG = Logger.getLogger(UserRestApiImpl.class.getSimpleName());

	private UserService userService;

	@Override
	@RequestMapping(produces = { MediaType.APPLICATION_JSON_VALUE }, method = { RequestMethod.GET })
	public ResponseEntity<LearnUsers> getUsers() {
		LOG.log(Level.FINE, "Received getAll request");
		
		LearnUsers users = userService.getAllUsers();
		
		return ResponseEntity.ok(users);
	}

	@Override
	@RequestMapping(
			value = "/{username}", 
			produces = { MediaType.APPLICATION_JSON_VALUE }, 
			method = { RequestMethod.GET })
	public ResponseEntity<LearnUser> getUserByUserName(@PathVariable String username) {
		LOG.log(Level.FINE, "Received get{0} request", username);
		
		return ResponseEntity.ok(userService.getByUsername(username));
	}

	@Override
	@RequestMapping(
			produces = { MediaType.APPLICATION_JSON_VALUE },
			consumes = { MediaType.APPLICATION_JSON_VALUE },
			method = { RequestMethod.POST })
	public ResponseEntity<LearnUser> createUser(@RequestBody LearnUser user) {
		LOG.log(Level.FINE, "Received post request");
		return ResponseEntity.ok(userService.createUser(user));
	}

	@Override
	@RequestMapping(value = "/{username}", 
			produces = { MediaType.APPLICATION_JSON_VALUE },
			consumes = { MediaType.APPLICATION_JSON_VALUE }, 
			method = { RequestMethod.PUT })
	public ResponseEntity<LearnUser> updateUser(@PathVariable String username, @RequestBody LearnUser user) {
		LOG.log(Level.FINE, "Received put request");
		return ResponseEntity.ok(userService.updateUser(user));
	}

	@Override
	@RequestMapping( value = "/user",
			produces = { MediaType.APPLICATION_JSON_VALUE },
			method = { RequestMethod.DELETE })
	public ResponseEntity<?> deleteUserByName(String username) {
		LOG.log(Level.FINE, "Received delete request");
		userService.deleteUser(username);
		return ResponseEntity.ok().build();
	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}
