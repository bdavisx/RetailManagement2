package ws.billdavis.retailmanagement.productmanagement.client.fx.application;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
@Profile("default")
public interface DataSourceConfig {
    DataSource dataSource();
}
