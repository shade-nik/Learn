package learn.java.dao.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import learn.java.dto.user.LearnUser;
import learn.java.entity.user.UserEntity;

@Repository("userDao")
public interface UserEntityDao extends JpaRepository<UserEntity, Long> {

	@Query("select u from UserEntity u where u.username = (:username)")
	Optional<UserEntity> findByUserName(@Param("username") String username);
	
	Optional<UserEntity> save(LearnUser user);
	
	@Query("select u from UserEntity u where u.email = (:email)")
	Optional<UserEntity> findByEmail(@Param("email") String email);
}
