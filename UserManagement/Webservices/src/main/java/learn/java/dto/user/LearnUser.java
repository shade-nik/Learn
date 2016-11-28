package learn.java.dto.user;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import learn.java.entity.user.UserEntity;

@JsonInclude(Include.NON_NULL)
public class LearnUser {

	private String username;
	private String email;

	private Set<LearnRole> roles = new HashSet<>();

	public LearnUser() {
	}

	public LearnUser(UserEntity entity) {
		this.username = entity.getUsername();
		this.email = entity.getEmail();
		if (entity.getRoles() != null) {
			this.roles = entity.getRoles().stream().map(LearnRole::toDto).collect(Collectors.toSet());
		}
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

	public static UserEntity toEntity(LearnUser user) {
		UserEntity res = new UserEntity();
		res.setEmail(user.getEmail());
		res.setUsername(user.getUsername());
		res.setRoles(user.getRoles().stream().map(LearnRole::toEntity).collect(Collectors.toSet()));
		return res;
	}

	public static LearnUser toDto(UserEntity entity) {
		return new LearnUser(entity);
	}
}
