package learn.java.entity.user;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.core.style.ToStringCreator;

@Entity
@Table(name = "user_role")
public class RoleEntity {
	private Long id;
	private String roleName;
	private String description;
//	private Set<UserEntity> users;

	public RoleEntity() {
	}
	
	@Id
	@GeneratedValue/*(strategy=GenerationType.SEQUENCE)*/
	@Column(name = "user_role_id")
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name="role_name", unique = true, nullable = false)
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

//	@ManyToMany(mappedBy = "roles")
//	public Set<UserEntity> getUsers() {
//		return users;
//	}
//	
//	public void setUsers(Set<UserEntity> users) {
//		this.users = users;
//	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj != null && obj instanceof RoleEntity) {
			return getRoleName().equals(((RoleEntity) obj).getRoleName());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return getRoleName().hashCode();
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
