package de.jgsoftware.h2cluster.config;


import com.zaxxer.hikari.HikariConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;

/**
 *
 * @author hoscho
 */

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "de.jgsoftware.h2cluster.dao.interfaces",
        entityManagerFactoryRef = "mawiEntityManagerFactory",
        transactionManagerRef = "mawiTransactionManager")
public class MaWiDBConfig extends HikariConfig
{
    @Autowired
    @Qualifier(value = "mawiJdbcTemplate")
    JdbcTemplate jtm1;


    @Autowired
    DataSource dataSource1;



    public MaWiDBConfig()
    {

    }


    @Bean("ds2")
    @Qualifier("mawidb")
    @ConfigurationProperties(prefix="app.datasource2")
    public DataSource secondDS()
    {
        return DataSourceBuilder.create().build();
    }



    @Bean(name = "mawiEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean mawiEntityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                           @Qualifier("mawidb") DataSource dataSource1) {
        HashMap<String, Object> properties = new HashMap<>();

        properties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        return builder.dataSource(dataSource1).properties(properties)
                .packages("de.jgsoftware.webshop.model").persistenceUnit("Mawi").build();
    }

    @Bean(name = "mawiTransactionManager")
    public PlatformTransactionManager mawiTransactionManager(
            @Qualifier("mawiEntityManagerFactory") EntityManagerFactory mawiEntityManagerFactory) {
        return new JpaTransactionManager(mawiEntityManagerFactory);
    }

    @Bean(name = "mawiJdbcTemplate")
    public JdbcTemplate jdbcTemplate(@Qualifier("ds2") DataSource dataSource1)
    {

        return new JdbcTemplate(dataSource1);
    }



}
