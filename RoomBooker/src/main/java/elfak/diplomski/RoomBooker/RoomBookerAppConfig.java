package elfak.diplomski.RoomBooker;

import com.vaadin.flow.spring.annotation.EnableVaadin;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
@ComponentScan(basePackages = {"elfak.diplomski.RoomBooker", "elfak.diplomski.RoomBooker.query.impl"})
@EnableVaadin(value = "elfak.diplomski.RoomBooker")
public class RoomBookerAppConfig {
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;

    public DSLContext dslContext;

    @Bean
    public DSLContext createDSLContex() {

        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            dslContext = DSL.using(conn, SQLDialect.MYSQL);
            return dslContext;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
