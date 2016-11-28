package learn.java.dao.user;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import learn.java.entity.user.RoleEntity;
import learn.java.entity.user.UserEntity;

@Repository("userDao")
public interface UserEntityDao extends JpaRepository<UserEntity, Long> {

	@Query("select u from UserEntity u where u.username = (:username)")
	Optional<UserEntity> findByUserName(@Param("username") String username);
	
//	UserEntity save(UserEntity user);
	
	@Query("select u from UserEntity u where u.email = (:email)")
	Optional<UserEntity> findByEmail(@Param("email") String email);

	@Query("select u.roles from UserEntity u where u.username = (:username) ")
	Set<RoleEntity> findUserRoles(@Param("username") String username);
	
}
