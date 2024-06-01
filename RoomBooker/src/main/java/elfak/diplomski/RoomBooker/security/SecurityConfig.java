package elfak.diplomski.RoomBooker.security;

import com.vaadin.flow.spring.security.VaadinWebSecurity;
import elfak.diplomski.RoomBooker.gui.login.LoginView;
import elfak.diplomski.RoomBooker.query.IUserQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.ArrayList;
import java.util.List;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends VaadinWebSecurity {

    @Autowired
    IUserQuery userQuery;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth ->
                        auth.requestMatchers(AntPathRequestMatcher
                                .antMatcher(HttpMethod.GET, "/images/*.png")).permitAll())
                .formLogin(form -> form.defaultSuccessUrl("/", true));
        super.configure(http);
        setLoginView(http, LoginView.class);
    }

    @Bean
    public UserDetailsService users() {
        List<database.tables.pojos.User> users = userQuery.getUsers();
        List<UserDetails> usersDetails = new ArrayList<>();
        users.forEach(user -> {
            usersDetails.add(User.builder().username(user.getName()).password("{noop}" + user.getPassword()).roles("USER", "ADMIN").build());
        });
//        UserDetails user = User.builder().username("Djura").password("{noop}123").roles("USER").build();
        return new InMemoryUserDetailsManager(usersDetails);
    }


}
