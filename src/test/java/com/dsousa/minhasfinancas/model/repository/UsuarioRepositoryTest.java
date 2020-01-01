package com.dsousa.minhasfinancas.model.repository;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.dsousa.minhasfinancas.model.entity.Usuario;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UsuarioRepositoryTest {
	
	@Autowired
	UsuarioRepository repository;
	
	@Autowired
	TestEntityManager entityManager;
	
	@Test
	public void deveVerificarAExistenciaDeUmEmail() {
		//cenário
		Usuario usuario = criarUsuario();
		entityManager.persist(usuario);
		
		//ação/ execução
		boolean result = repository.existsByEmail("usuario@email.com");
		
		//verificacao
		Assertions.assertThat(result).isTrue();
		
	}
	
	@Test
	public void deveRetornarFalsoQuandoNaoHouverUsuarioCadastradoComOEmail() {
		//cenário
		
		//acao
		boolean result = repository.existsByEmail("usuario@email.com");
		
		//verificacao
		Assertions.assertThat(result).isFalse();
	}
	
	@Test
	public void devePersistirUmUsuarioNaBaseDeDados() {
		//cenário
		Usuario usuario =criarUsuario();
		
		//acao
		Usuario usuarioSalvo = repository.save(usuario);
		
		// verificacao
		Assertions.assertThat(usuarioSalvo.getId()).isNotNull();
	}
	
	@Test
	public void deveBuscarUmUsuarioPorEmail() {
		//cenario
		Usuario usuario = criarUsuario();
		entityManager.persist(usuario);
		
		//verificacao
		Optional<Usuario> result = repository.findByEmail("usuario@email.com");
		
		Assertions.assertThat( result.isPresent() ).isTrue();
		
	}
	
	@Test
	public void deveRetornarVazioAoBuscarUsuarioPorEmailQuandoNaoExisteNaBase() {
		
		//verificacao
		Optional<Usuario> result = repository.findByEmail("usuario@email.com");
		
		Assertions.assertThat( result.isPresent() ).isFalse();
		
	}
	
	public static Usuario criarUsuario() {
		return Usuario
				.builder()
				.nome("usuario")
				.email("usuario@email.com")
				.senha("senha")
				.build();
	}

}
