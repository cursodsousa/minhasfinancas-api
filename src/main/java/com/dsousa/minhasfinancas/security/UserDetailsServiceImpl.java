package com.dsousa.minhasfinancas.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.dsousa.minhasfinancas.exception.ErroAutenticacao;
import com.dsousa.minhasfinancas.model.entity.Usuario;
import com.dsousa.minhasfinancas.service.UsuarioService;

public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private UsuarioService usuarioService;

	@Override
	public UserDetails loadUserByUsername( String login ) throws UsernameNotFoundException {
		Usuario usuario = usuarioService.obterPorEmail(login);
		
		if(usuario == null) {
			throw new ErroAutenticacao("Usuário não encontrado para o email informado.");
		}
		
		return  User
			.builder()
			.username(usuario.getEmail())
			.password(usuario.getSenha())
			.authorities( new SimpleGrantedAuthority("USER") )
			.build();
		
	}

}
