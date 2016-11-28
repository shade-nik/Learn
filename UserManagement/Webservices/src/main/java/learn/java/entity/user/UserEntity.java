package learn.java.entity.user;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.core.style.ToStringCreator;

@Entity
@Table(name = "user", uniqueConstraints = { 
		@UniqueConstraint(columnNames = { "username" }, name = "uq_user_username"),
		@UniqueConstraint(columnNames = { "email" }, name = "uq_user_email") })
public class UserEntity {
	
	private Long id;
	private String username;
	private String email;

	private Set<RoleEntity> roles;

	public UserEntity() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "username", nullable = false)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "email", nullable = false)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@JoinTable(name = "user_role_xref", /*uniqueConstraints = {}, */
	           joinColumns = @JoinColumn(
	        		    name = "xref_user_id", 
	           			referencedColumnName = "user_id",
	           			foreignKey = @ForeignKey(name = "fk_user")),
	                    foreignKey = @ForeignKey(name = "fk_out_user"),
	           inverseJoinColumns = @JoinColumn(name = "xref_role_id",
	           			referencedColumnName = "user_role_id",
	           			foreignKey = @ForeignKey(name = "fk_security_role")),
	                    inverseForeignKey = @ForeignKey(name = "fk_out_security_role"))
	public Set<RoleEntity> getRoles() {
		return roles;
	}

	public void setRoles(Set<RoleEntity> roles) {
		this.roles = roles;
	}
	
	@Override
	public String toString() {
		return new ToStringCreator(this)
				.append("id", id)
				.append("email", email)
				.append("username", username)
				.append("roles", roles)
				.toString();
	}
	
}
