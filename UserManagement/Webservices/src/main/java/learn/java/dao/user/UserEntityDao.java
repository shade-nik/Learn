package learn.java.dao.user;

import java.util.List;
import java.util.Optional;

import learn.java.dto.user.LearnUser;
import learn.java.enity.user.UserEntity;

public interface UserEntityDao {

	Optional<List<UserEntity>> getAll();

	Optional<UserEntity> findByUserName(String username);

	Optional<UserEntity> createUser(LearnUser user);
	
	Optional<UserEntity> updateUser(LearnUser user);

}
