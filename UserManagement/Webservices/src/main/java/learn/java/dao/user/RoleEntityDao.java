package learn.java.dao.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import learn.java.entity.user.RoleEntity;

@Repository("roleDao")
public interface RoleEntityDao extends JpaRepository<RoleEntity, Long> {
	
	
//	Optional<List<RoleEntity>> findAll();
	
}
