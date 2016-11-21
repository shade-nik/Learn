package learn.java.dto.user;

import java.util.Set;
import java.util.stream.Collectors;

import learn.java.enity.user.UserEntity;

public class LearnUser {

	private String username;
	private String email;

	private Set<LearnRole> roles;

	public LearnUser() {
	}

	public LearnUser(UserEntity entity) {
		this.username = entity.getUsername();
		this.email = entity.getEmail();
		this.roles = entity.getRoles().stream().map(role -> {
			return new LearnRole(role);
		}).collect(Collectors.toSet());
	}
	
	public LearnUser(String username, String email) {
		super();
		this.username = username;
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<LearnRole> getRoles() {
		return roles;
	}

	public void setRoles(Set<LearnRole> roles) {
		this.roles = roles;
	}

}
