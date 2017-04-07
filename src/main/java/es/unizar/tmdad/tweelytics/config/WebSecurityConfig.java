package es.unizar.tmdad.tweelytics.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.social.security.SpringSocialConfigurer;

import es.unizar.tmdad.tweelytics.service.SimpleSocialUsersDetailService;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                	.antMatchers("/").permitAll()
                	.antMatchers("/search").permitAll()
                	.antMatchers("/connect/*").permitAll()
                	.antMatchers("/connect").permitAll()
                	.antMatchers("/config/*").authenticated()
            .and()
            	.formLogin()
                	.loginPage("/").permitAll()
            .and()
                .logout().permitAll()
            .and()
            	.apply(new SpringSocialConfigurer())
            .and()
                .csrf().disable();
    }
    
	@Bean
	public SocialUserDetailsService socialUsersDetailService() {
		return new SimpleSocialUsersDetailService(userDetailsService());
	}
}