package learn.java.dto.user;

import org.springframework.core.style.ToStringCreator;

import learn.java.entity.user.RoleEntity;

public class LearnRole {

	private String roleName;
	private String description;
	
	public LearnRole() {
	}

	public LearnRole(RoleEntity entity) {
		this.roleName = entity.getRoleName();
		this.description = entity.getDescription();
	}
	
	public LearnRole(String roleName, String description) {
		super();
		this.roleName = roleName;
		this.description = description;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		return new ToStringCreator(this)
				.append("role", roleName)
				.append("description", description)
				.toString();
	}
	
	public static RoleEntity toEntity(LearnRole role) {
		RoleEntity res = new RoleEntity();
		res.setDescription(role.getDescription());
		res.setRoleName(role.getRoleName());
		return res;
	}
	
	public static LearnRole toDto(RoleEntity entity) {
		return new LearnRole(entity);
	}
}
