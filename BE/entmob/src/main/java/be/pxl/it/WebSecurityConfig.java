package be.pxl.it;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
@Configuration
@ComponentScan
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    @Autowired
    public WebSecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/authorize").permitAll()
                .antMatchers(HttpMethod.POST, "/events/{id}").hasAnyRole("TEACHER","STUDENT")
                .antMatchers(HttpMethod.GET, "/events/user/{cardNumber}").hasAnyRole("TEACHER","STUDENT")
                .antMatchers(HttpMethod.GET, "/events/required/{cardNumber}").hasAnyRole("TEACHER", "STUDENT")
                .antMatchers("/events/**").hasRole("TEACHER")
                .antMatchers("/parties/**").hasRole("TEACHER")
                .antMatchers("/authorize/users").hasRole("TEACHER")
                .anyRequest().authenticated().and().
                httpBasic().and().
                csrf().disable();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }
}
