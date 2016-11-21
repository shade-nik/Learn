package learn.java.webservice.service.user;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import learn.java.dao.user.RoleEntityDao;
import learn.java.dao.user.UserEntityDao;
import learn.java.dto.user.LearnUser;
import learn.java.dto.user.LearnUsers;
import learn.java.enity.user.UserEntity;
import learn.java.webservice.exception.UserAlreadyExistsException;
import learn.java.webservice.exception.UserNotFoundException;

@Component
public class UserService {

	private UserEntityDao userDao;
	private RoleEntityDao roleDao;

	public LearnUsers getAllUsers() {
		List<UserEntity> users = userDao.getAll().orElseThrow(() -> {
			return new UserNotFoundException("Not found any users.");
		});
		LearnUsers result = new LearnUsers();
		result.setUsers(users.stream().map(user -> {
			return new LearnUser(user);
		}).collect(Collectors.toList()));

		return result;
	}

	public LearnUser getUserByUsername(String username) {
		UserEntity entity = userDao.findByUserName(username).orElseThrow(() -> {
			return new UserNotFoundException("Not found user : " + username);
		});
		return new LearnUser(entity);
	}

	public LearnUser createUser(LearnUser user) {
		if (isExists(user.getUsername())) {
			throw new UserAlreadyExistsException("Username: " + user.getUsername() + " already exists.");
		}
		UserEntity entity = userDao.createUser(user).orElse(new UserEntity());
		return new LearnUser(entity);
	}

	public LearnUser updateUser(LearnUser user) {
		if (!isExists(user.getUsername())) {
			throw new UserNotFoundException("Username: " + user.getUsername() + " not found.");
		}
		UserEntity entity = userDao.updateUser(user).orElse(new UserEntity());
		return new LearnUser(entity);
	}

	public void setRoleDao(RoleEntityDao roleDao) {
		this.roleDao = roleDao;
	}

	public void setUserDao(UserEntityDao userDao) {
		this.userDao = userDao;
	}

	private boolean isExists(String username) {
		if (username != null) {
			return userDao.findByUserName(username).isPresent();
		}
		return false;
	}

}
