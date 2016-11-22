package learn.java.entity.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.core.style.ToStringCreator;

@Entity
@Table(name = "user_role")
public class RoleEntity {
	private Long id;
	private String roleName;
	private String description;
	
	public RoleEntity() {
	}
	
	@Id
	@GeneratedValue
	@Column(name = "user_role_id")
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@Column
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
		return new ToStringCreator(this)
				.append("id", id)
				.append("role", roleName)
				.append("description", description)
				.toString();
	}

}
