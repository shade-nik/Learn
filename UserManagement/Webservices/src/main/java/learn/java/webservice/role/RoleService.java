package learn.java.webservice.role;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import learn.java.dao.user.RoleEntityDao;
import learn.java.dto.user.LearnRole;
import learn.java.dto.user.LearnUser;
import learn.java.entity.user.RoleEntity;
import learn.java.entity.user.UserEntity;
import learn.java.webservice.exception.UserAlreadyExistsException;

@Component
public class RoleService {

	private RoleEntityDao roleDao;

	public List<LearnRole> findAll() {
		List<RoleEntity> roles = roleDao.findAll();
		return roles.stream().map(LearnRole::toDto).collect(Collectors.toList());
	}

	public LearnRole getRole(String rolename) {
		return LearnRole.toDto(roleDao.findByName(rolename).get());
	}

	
	public LearnRole createRole(LearnRole role) {
		Optional<RoleEntity> entity = roleDao.findByName(role.getRoleName());
		if(entity.isPresent()) {
			return LearnRole.toDto(entity.get());	
		} else {
			return LearnRole.toDto(roleDao.save(LearnRole.toEntity(role)));
		}
	}
	
	private boolean isExists(String rolename) {
		if (rolename != null) {
			return roleDao.findByName(rolename).isPresent();
		}
		return false;
	}
	
	@Autowired
	public void setRoleDao(RoleEntityDao roleDao) {
		this.roleDao = roleDao;
	}

	
}
