package learn.java.webservice;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.cfg.AvailableSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(	basePackages = {JpaRepositoriesConfig.DAO_PACKAGE_TO_SCAN, JpaRepositoriesConfig.DTO_PACKAGE_TO_SCAN},  	
					   	transactionManagerRef = JpaRepositoriesConfig.MANAGER_NAME)
@Import(LearnDataSourceConfig.class)
public class JpaRepositoriesConfig {

	public static final String DAO_PACKAGE_TO_SCAN = "learn.java.dao" ;
	public static final String DTO_PACKAGE_TO_SCAN = "learn.java.entity" ;
	public static final String MANAGER_NAME = "jpaTransactionManager";
	
	
	@Bean
	@Primary
	public EntityManagerFactory entityManagerFactory(@Qualifier("learningDataSource") DataSource ds) {
		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setDataSource(ds);
		factory.setJpaVendorAdapter(jpaVendorAdapter());
		factory.setPackagesToScan(DAO_PACKAGE_TO_SCAN, DTO_PACKAGE_TO_SCAN);
	    factory.afterPropertiesSet();
	    return factory.getObject();
	}
	
	@Bean
	public HibernateJpaVendorAdapter jpaVendorAdapter() {
		HibernateJpaVendorAdapter jpaAdapter = new HibernateJpaVendorAdapter();
		jpaAdapter.setShowSql(true);
		jpaAdapter.setGenerateDdl(true);
		jpaAdapter.setDatabase(Database.MYSQL);
		return jpaAdapter;
	}
	
	@Bean(name = JpaRepositoriesConfig.MANAGER_NAME)
	@Autowired
	public PlatformTransactionManager jpaTransactionManager(EntityManagerFactory entityManagerFactory) {
		JpaTransactionManager txManager = new JpaTransactionManager();
		txManager.setEntityManagerFactory(entityManagerFactory);
		return txManager;
	}
	
	
	@Bean
	@Autowired
	public LocalSessionFactoryBean sessionFactory(DataSource ds) {
		LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();
		factoryBean.setDataSource(ds);
		factoryBean.setPackagesToScan(new String[] {"local.halflight.learning.dto.hibernate"});

		Properties props = new Properties();
		props.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
		
		props.setProperty("hibernate.show_sql", "true");
		props.setProperty("hibernate.format_sql", "true");
		props.setProperty(AvailableSettings.HBM2DDL_AUTO, "update");
		//		props.setProperty("hibernate.hbm2ddl.auto", "create");
		props.setProperty("hibernate.connection.CharSet", "utf8");
		props.setProperty("hibernate.connection.characterEncoding", "utf8");
		props.setProperty("hibernate.connection.useUnicode", "true");
        
		factoryBean.setHibernateProperties(props);
		return factoryBean;
	}

	@Bean
	public BeanPostProcessor exceptionTranslator()
	{
		return new PersistenceExceptionTranslationPostProcessor();
	}
}
