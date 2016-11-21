package learn.java.webservice.rest.user;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import learn.java.dto.user.LearnUser;
import learn.java.dto.user.LearnUsers;
import learn.java.webservice.service.user.UserService;

@RestController
@RequestMapping("/user")
public class UserRestApiImpl implements UserRestApi {
	
	private static final Logger LOG = Logger.getLogger(UserRestApiImpl.class.getSimpleName());
	
	@Autowired
	private UserService userService;
	
	@Override
	@RequestMapping(produces = { MediaType.APPLICATION_JSON_VALUE }, 
	 method = { RequestMethod.GET })
	public ResponseEntity<LearnUsers> getAllUsers() {
		LOG.log(Level.FINE, "Received getAll request");
		
		LearnUsers users = userService.getAllUsers();

		return ResponseEntity.ok(users);
	}

	@Override
	@RequestMapping(produces = { MediaType.APPLICATION_JSON_VALUE }, 
	value = "/{username}", method = { RequestMethod.GET })
	public ResponseEntity<LearnUser> getUserByUserName(@PathVariable String username) {
		LOG.log(Level.FINE, "Received get{0} request", username); 
		return ResponseEntity.ok(new LearnUser(username, "email"));
	}

	@Override
	@RequestMapping(produces = { MediaType.APPLICATION_JSON_VALUE },
			consumes = { MediaType.APPLICATION_JSON_VALUE },
		    method = { RequestMethod.POST })
	public ResponseEntity<LearnUser> createUser(@RequestBody LearnUser user) {
		LOG.log(Level.FINE, "Received post request"); 
		return ResponseEntity.ok(user);
	}

	@Override
	@RequestMapping(produces = { MediaType.APPLICATION_JSON_VALUE },
		consumes = { MediaType.APPLICATION_JSON_VALUE },
		value = "/{username}", method = { RequestMethod.PUT })
	public ResponseEntity<LearnUser> updateUser(@PathVariable String username,  @RequestBody LearnUser user) {
		LOG.log(Level.FINE, "Received put request"); 
		return ResponseEntity.ok(user);
	}

	@Override
	@RequestMapping(produces = { MediaType.APPLICATION_JSON_VALUE },
	value = "/user", method = { RequestMethod.DELETE })
	public ResponseEntity<Void> deleteUserByName(String username) {
		LOG.log(Level.FINE, "Received delete request"); 
		return null;
	}

}
