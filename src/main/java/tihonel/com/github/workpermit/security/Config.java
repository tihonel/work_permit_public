package tihonel.com.github.workpermit.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class Config {
    @Bean
    SecurityFilterChain configure(HttpSecurity http) throws Exception{
        http.formLogin(c -> c.defaultSuccessUrl("/work_permits"))
                .authorizeHttpRequests(c -> c.anyRequest().authenticated())
                .csrf(Customizer.withDefaults());
        return http.build();
    }
    @Bean
    UserDetailsService userDetailsService(DataSource dataSource){
        String userByUsername = "SELECT username, password, enable FROM users WHERE username = ?";
        String authorityByUsername = "SELECT username, authority FROM authority WHERE username = ?";
        var uds = new JdbcUserDetailsManager(dataSource);
        uds.setUsersByUsernameQuery(userByUsername);
        uds.setAuthoritiesByUsernameQuery(authorityByUsername);
        return uds;
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
