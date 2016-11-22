package learn.java.dao.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import learn.java.entity.user.UserEntity;
import learn.java.webservice.JpaRepositoriesConfig;
import learn.java.webservice.LearnDataSourceConfig;
import learn.java.webservice.service.user.UserServiceTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { LearnDataSourceConfig.class, JpaRepositoriesConfig.class })
@Rollback
public class UserEntityDaoIntTest {

	private static final Logger LOG = LoggerFactory.getLogger(UserEntityDaoIntTest.class);

	private static final String TEST_USERNAME = "TestUser";
	private static final String TEST_USER_EMAIL = "testuser@test.mail";

	@Autowired
	private UserEntityDao userEntityDao;

	UserEntity entity;

	@Before
	public void before() {
		entity = UserServiceTest.buildUserEntity(TEST_USERNAME);
		Optional<UserEntity> cleanup = userEntityDao.findByUserName(entity.getUsername());
		if (cleanup.isPresent()) {
			userEntityDao.delete(cleanup.get().getId());
		}
	}

	@After
	public void after() {
	}

	@Test
	@Rollback
	public void testCrud() throws Exception {
		UserEntityDao dao = getDao();
		UserEntity dto = getDto();
		dto.setId(null);

		LOG.info("===Generated UserEntity: {}", dto);
		// save
		UserEntity saved = dao.save(dto);
		assertTrue("Saved dto should be not null", saved != null);
		assertTrue("Saved dto id should be not null", saved.getId() != null);
		LOG.info("===Save it: {}", saved);

		// read
		UserEntity founded = dao.findOne(saved.getId());
		assertThat(founded).isNotNull();
		assertThat(founded).isEqualToComparingOnlyGivenFields(saved, "id");

		// update
		modifyDto(founded);
		UserEntity updated = dao.save(founded);
		assertThat(updated).isNotNull();
		LOG.info("===Update it: {}", updated);

		// remove
		dao.delete(updated.getId());

		UserEntity notFound = dao.findOne(updated.getId());
		assertThat(notFound).isNull();

	}

	protected UserEntityDao getDao() {
		return userEntityDao;
	}

	protected UserEntity getDto() {
		return entity;
	}

	protected UserEntity modifyDto(final UserEntity dto) {
		dto.setUsername("UpdatedName");
		return dto;
	}

}
