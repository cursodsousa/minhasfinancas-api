package com.dsousa.minhasfinancas.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import com.dsousa.minhasfinancas.security.JwtAuthFilter;
import com.dsousa.minhasfinancas.security.JwtService;
import com.dsousa.minhasfinancas.service.UsuarioService;

@EnableWebSecurity
public class SecurityConfiguration  extends WebSecurityConfigurerAdapter {
	
	@Autowired 
	private JwtService jwtService;
	@Autowired 
	private UsuarioService usuarioService;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new PasswordEncoder() {
			
			@Override
			public boolean matches( CharSequence rawPassword, String encodedPassword ) {
				return rawPassword.equals(encodedPassword.substring(0, encodedPassword.length() - 1));
			}
			
			@Override
			public String encode(CharSequence rawPassword) {
				return rawPassword + "1";
			}
		};
	}
	
	@Bean
	public UserDetailsService userDetailsService() {
		return new UserDetailsService() {
			@Override
			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
				return User
						.builder()
						.username(username)
						.password(passwordEncoder().encode("321"))
						.roles("USER")
						.build();
			}
		};
	}
	
	@Override
	protected void configure( AuthenticationManagerBuilder auth ) throws Exception {
		auth
			.userDetailsService(userDetailsService())
			.passwordEncoder(passwordEncoder());
	}
	
	@Bean
	public OncePerRequestFilter jwtAuthFilter() {
		return new JwtAuthFilter(jwtService, usuarioService);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.authorizeRequests()
			.antMatchers("**/api/lancamentos/**").hasRole("USER")
			.antMatchers("/api/usuarios/autenticar").permitAll()
			.anyRequest().authenticated()
		.and().httpBasic()
		.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and().addFilterBefore( jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class);
	}
}
