package learn.java.webservice.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import learn.java.dao.user.RoleEntityDao;
import learn.java.dao.user.UserEntityDao;
import learn.java.dto.user.LearnRole;
import learn.java.dto.user.LearnUser;
import learn.java.dto.user.LearnUsers;
import learn.java.entity.user.RoleEntity;
import learn.java.entity.user.UserEntity;
import learn.java.webservice.exception.UserAlreadyExistsException;
import learn.java.webservice.exception.UserNotFoundException;
import learn.java.webservice.role.RoleService;

@Component
public class UserService {

	private UserEntityDao userDao;
	private RoleEntityDao roleDao;
	
	public LearnUsers getAllUsers() {
		List<UserEntity> users = userDao.findAll();
		LearnUsers result = new LearnUsers();
		result.setUsers(users.stream().map(user -> {
			return new LearnUser(user);
		}).collect(Collectors.toList()));

		return result;
	}

	public LearnUser getByUsername(String username) {
		UserEntity entity = getByName(username);
		return new LearnUser(entity);
	}

	public LearnUser createUser(LearnUser user) {
		Optional<UserEntity> entity = userDao.findByUserName(user.getUsername());
		if (entity.isPresent()) {
			return LearnUser.toDto(entity.get());
//			throw new UserAlreadyExistsException("Username: " + user.getUsername() + " already exists.");
		}
		
//		List<String> roleNames = user.getRoles().stream().map((role)-> { return role.getRoleName();}).collect(Collectors.toList());
//		Set<RoleEntity> roles = roleDao.findRoles(roleNames);
//		UserEntity u = LearnUser.toEntity(user);
//		u.setRoles(roles);

		UserEntity saved = userDao.save(LearnUser.toEntity(user));
		return new LearnUser(saved);
	}

	public LearnUser updateUser(LearnUser update) {
		UserEntity existedEntity = getByName(update.getUsername());
		mergeEntities(existedEntity, LearnUser.toEntity(update));
		UserEntity updated = userDao.save(mergeEntities(existedEntity, LearnUser.toEntity(update)));
		return new LearnUser(updated);
	}

	public void deleteUser(String username) {
		UserEntity entity = getByName(username);
		userDao.delete(entity);
	}

	public List<LearnRole> getUserRoles(String username) {
		List<LearnRole> res = new ArrayList<>(getByUsername(username).getRoles());
		return res;
	}

	public LearnUser updateUserRoles(String username, List<LearnRole> roles) {
		UserEntity entity = getByName(username);
		Set<RoleEntity> existingRoles = getExistingRoles(roles);
		entity.getRoles().addAll(existingRoles);
		UserEntity updated = userDao.save(entity);
		return LearnUser.toDto(updated);
	}


	public LearnUser deleteUserRoles(String username, List<LearnRole> roles) {
		UserEntity user = getByName(username);
		user.getRoles().removeAll(getExistingRoles(roles));
		return LearnUser.toDto(userDao.save(user));
	}
	
	@Autowired
	public void setUserDao(UserEntityDao userDao) {
		this.userDao = userDao;
	}
	
	@Autowired
	public void setRoleDao(RoleEntityDao roleDao) {
		this.roleDao = roleDao;
	}

	private boolean isExists(String username) {
		if (username != null) {
			return userDao.findByUserName(username).isPresent();
		}
		return false;
	}
	
	private UserEntity getByName(String username) {
		return userDao.findByUserName(username).orElseThrow(() -> {
			return new UserNotFoundException("Not found user : " + username);
		});
	}

	private Set<RoleEntity> getExistingRoles(List<LearnRole> roles) {
		Set<String> roleNames = roles.stream().map(role-> {return role.getRoleName(); }).collect(Collectors.toSet());
		Set<RoleEntity> entities = roleDao.findExisting(roleNames);
		return entities;
	}

	private UserEntity mergeEntities(UserEntity existedEntity, UserEntity entity) {
		if(!existedEntity.getEmail().equals(entity.getEmail())) {
			existedEntity.setEmail(entity.getEmail());
		}
		if(!existedEntity.getUsername().equals(entity.getUsername())) {
			existedEntity.setUsername(entity.getUsername());
		}
		existedEntity.getRoles().addAll(entity.getRoles()) ;
		return existedEntity;	
	}


}
