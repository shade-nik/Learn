package learn.java.dto.user;

import learn.java.enity.user.RoleEntity;

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

}
