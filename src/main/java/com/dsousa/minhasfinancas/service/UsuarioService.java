package com.dsousa.minhasfinancas.service;

import java.util.Optional;

import com.dsousa.minhasfinancas.model.entity.Usuario;

public interface UsuarioService {

	Usuario autenticar(String email, String senha);
	
	Optional<Usuario> buscarPorEmail(String email);
	
	Usuario salvarUsuario(Usuario usuario);
	
	void validarEmail(String email);
	
	Optional<Usuario> obterPorId(Long id);
	
}
