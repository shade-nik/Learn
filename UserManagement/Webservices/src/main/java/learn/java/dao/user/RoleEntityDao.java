package learn.java.dao.user;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import learn.java.entity.user.RoleEntity;

@Repository("roleDao")
public interface RoleEntityDao extends JpaRepository<RoleEntity, Long> {

	@Query("select r from RoleEntity r where r.roleName = (:rolename)")
	Optional<RoleEntity> findByName(@Param("rolename") String rolename);
	
	@Query("select r from RoleEntity r where r.roleName in (:roleNames) ")
	Set<RoleEntity> findExisting(@Param("roleNames") Collection<String> roleNames);

//	Optional<List<RoleEntity>> findAll();
	
}
