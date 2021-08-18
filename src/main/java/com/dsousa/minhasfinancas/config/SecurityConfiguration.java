package com.dsousa.minhasfinancas.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.dsousa.minhasfinancas.api.JwtTokenFilter;
import com.dsousa.minhasfinancas.service.JwtService;
import com.dsousa.minhasfinancas.service.impl.SecurityUserDetailsService;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private SecurityUserDetailsService userDetailsService;
	@Autowired
	private JwtService jwtService;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}
	
	@Bean
	public JwtTokenFilter jwtTokenFilter() {
		return new JwtTokenFilter(jwtService, userDetailsService);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.userDetailsService(userDetailsService)
			.passwordEncoder(passwordEncoder());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.authorizeRequests()
				.antMatchers(HttpMethod.POST, "/api/usuarios/autenticar").permitAll()
				.antMatchers(HttpMethod.POST, "/api/usuarios").permitAll()
				.anyRequest().authenticated()	
		.and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
			.addFilterBefore( jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class )
			;

	}
}
