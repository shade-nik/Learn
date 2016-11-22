package learn.java.webservice.service.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easymock.EasyMock.expect;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Before;
import org.junit.Test;

import learn.java.dao.user.RoleEntityDao;
import learn.java.dao.user.UserEntityDao;
import learn.java.dto.user.LearnUser;
import learn.java.dto.user.LearnUsers;
import learn.java.enity.user.RoleEntity;
import learn.java.enity.user.UserEntity;
import learn.java.webservice.exception.UserAlreadyExistsException;
import learn.java.webservice.exception.UserNotFoundException;

public class UserServiceTest {

	UserService userService;

	private final IMocksControl control = EasyMock.createControl();
	private final UserEntityDao userDao = control.createMock(UserEntityDao.class);
	private final RoleEntityDao roleDao = control.createMock(RoleEntityDao.class);

	@Before
	public void before() {
		userService = new UserService();
		userService.setRoleDao(roleDao);
		userService.setUserDao(userDao);
	}

	@Test
	public void shouldGetAllUsers() {
		UserEntity userOne = buildUserEntity("userOne");
		userOne.setRoles(buildRoles("Role1", "Role2"));
		UserEntity userTwo = buildUserEntity("userTwo");
		userTwo.setRoles(buildRoles("Role3", "Role4"));

		List<UserEntity> users = Arrays.asList(userOne, userTwo);
		expect(userDao.getAll()).andReturn(Optional.of(users));

		control.replay();
		LearnUsers response = userService.getAllUsers();
		control.verify();

		assertThat(response.getUsers()).isNotEmpty().hasSize(2);
		assertThat(response.getUsers()).extracting("username").contains("userOne", "userTwo");
		assertThat(response.getUsers().stream().flatMap(user -> user.getRoles().stream()).collect(Collectors.toList()))
				// .isNotEmpty().hasSize(4);
				.extracting("roleName").contains("Role1", "Role2", "Role3", "Role4");
	}

	@Test
	public void shouldGetUserByUserName() {
		final String username = "User";
		expect(userDao.findByUserName(username)).andReturn(Optional.of(buildUserEntity(username)));

		control.replay();
		LearnUser response = userService.getUserByUsername(username);
		control.verify();

		assertThat(response).isNotNull();
		assertThat(response.getEmail()).isEqualTo(username + "@mail");
		assertThat(response.getUsername()).isEqualTo(username);
		assertThat(response.getRoles()).isNotEmpty();
	}

	@Test(expected = UserNotFoundException.class)
	public void shouldThrowIfUserNameNotFound() {
		final String username = "User";
		expect(userDao.findByUserName(username)).andReturn(Optional.empty());

		control.replay();
		LearnUser response = userService.getUserByUsername(username);
		control.verify();
	}

	@Test
	public void shouldCreateUser() {
		final String username = "User";
		LearnUser user = new LearnUser(buildUserEntity(username));
		expect(userDao.findByUserName(user.getUsername())).andReturn(Optional.empty());
		expect(userDao.createUser(user)).andReturn(Optional.of(buildUserEntity(username)));

		control.replay();
		LearnUser response = userService.createUser(user);
		control.verify();
	}

	@Test(expected = UserAlreadyExistsException.class)
	public void shouldThrowIfUserAlreadyExists() {
		final String username = "User";
		LearnUser user = new LearnUser(buildUserEntity(username));
		expect(userDao.findByUserName(user.getUsername())).andReturn(Optional.of(buildUserEntity(username)));

		control.replay();
		LearnUser response = userService.createUser(user);
		control.verify();
	}

	@Test
	public void shouldUpdateUser() {
		final String username = "User";
		LearnUser user = new LearnUser(buildUserEntity(username));
		expect(userDao.findByUserName(user.getUsername())).andReturn(Optional.of(buildUserEntity(username)));
		expect(userDao.updateUser(user)).andReturn(Optional.of(buildUserEntity(username)));

		control.replay();
		LearnUser response = userService.updateUser(user);
		control.verify();
	}

	@Test(expected = UserNotFoundException.class)
	public void shouldThrowIfUpdatedUserNotExist() {
		final String username = "User";
		LearnUser user = new LearnUser(buildUserEntity(username));
		expect(userDao.findByUserName(user.getUsername())).andThrow(new UserNotFoundException());

		control.replay();
		LearnUser response = userService.updateUser(user);
		control.verify();
	}

	private UserEntity buildUserEntity(String username) {
		UserEntity res = new UserEntity();
		res.setEmail(username + "@mail");
		res.setUsername(username);
		res.setRoles(buildRoles(username + "Role"));
		return res;
	}

	private Set<RoleEntity> buildRoles(String... roles) {
		if (roles != null && roles.length > 0) {
			Set<RoleEntity> res = new HashSet<>();
			for (String role : roles) {
				res.add(buildRoleEntity(role));
			}
			return res;
		}
		return Collections.EMPTY_SET;
	}

	private RoleEntity buildRoleEntity(String roleName) {
		RoleEntity res = new RoleEntity();
		res.setRoleName(roleName);
		res.setDescription(roleName + ": description");
		return res;
	}
}
