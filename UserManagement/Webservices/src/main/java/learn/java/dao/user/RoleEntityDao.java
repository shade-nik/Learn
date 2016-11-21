package learn.java.dao.user;

import java.util.List;
import java.util.Optional;

import learn.java.enity.user.RoleEntity;

public interface RoleEntityDao {
	
	Optional<List<RoleEntity>> getAll();
	
}
