package learn.java.webservice.role;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import learn.java.dto.user.LearnRole;
import learn.java.webservice.user.UserService;

@RestController
@RequestMapping("/")
public class RolesRestApi {
	@Autowired
	private RoleService roleService;
	@Autowired
	private UserService userService;
	
	
	@RequestMapping(
			value = "/roles",
			produces = { MediaType.APPLICATION_JSON_VALUE },
			method = { RequestMethod.GET })
	public ResponseEntity<?> getRoles() {
		return ResponseEntity.ok(roleService.findAll()); 
	}
	
	@RequestMapping(
			value = "/roles/{rolename}", 
			produces = { MediaType.APPLICATION_JSON_VALUE }, 
			method = { RequestMethod.GET })
	public ResponseEntity<LearnRole> getRole(@PathVariable("rolename") String rolename) {
		return ResponseEntity.ok(roleService.getRole(rolename));
	}

	@RequestMapping(
			value = "/roles", 
			produces = { MediaType.APPLICATION_JSON_VALUE }, 
			method = { RequestMethod.POST })
	public ResponseEntity<LearnRole> addRole(@RequestBody LearnRole role) {
		return ResponseEntity.ok(roleService.createRole(role));
	}

	
	@RequestMapping(
			value = "/users/{username}/roles", 
			produces = { MediaType.APPLICATION_JSON_VALUE }, 
			method = { RequestMethod.GET })
	public ResponseEntity<?> getUserRoles(@PathVariable("username") String username) {

		return ResponseEntity.ok(userService.getUserRoles(username));
	}
	
	@RequestMapping(
			value = "/users/{username}/roles", 
			produces = { MediaType.APPLICATION_JSON_VALUE }, 
			method = { RequestMethod.PUT })
	public ResponseEntity<?> updateUserRoles(@PathVariable("username") String username, @RequestBody List<LearnRole> roles) {
		return ResponseEntity.ok(userService.updateUserRoles(username, roles));
	}
	
	@RequestMapping(
			value = "/users/{username}/roles", 
			produces = { MediaType.APPLICATION_JSON_VALUE }, 
			method = { RequestMethod.DELETE })
	public ResponseEntity<?> deleteUserRoles(@PathVariable("username") String username,  @RequestBody List<LearnRole> roles) {
		return ResponseEntity.ok(userService.deleteUserRoles(username, roles));
	}
	
}
