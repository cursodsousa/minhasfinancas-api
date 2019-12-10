package com.dsousa.minhasfinancas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.dsousa.minhasfinancas.security.UserDetailsServiceImpl;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Bean
	public UserDetailsService userDetailsService() {
		return new UserDetailsServiceImpl();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure( AuthenticationManagerBuilder auth ) throws Exception {
		auth
			.userDetailsService(userDetailsService())
			.passwordEncoder(passwordEncoder());
	}
	
	@Override
	protected void configure( HttpSecurity http ) throws Exception {
		http
			.csrf().disable()
			.authorizeRequests()
				.antMatchers( HttpMethod.POST, "/api/usuarios", "/api/usuarios/autenticar" ).permitAll()
				.anyRequest().authenticated()
			.and()
				.sessionManagement().sessionCreationPolicy( SessionCreationPolicy.STATELESS )
			.and()
				.httpBasic()
			;
	}
}
