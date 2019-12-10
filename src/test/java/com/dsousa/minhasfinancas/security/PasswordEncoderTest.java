package com.dsousa.minhasfinancas.security;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordEncoderTest {

	PasswordEncoder encoder = new CustomPasswordEncoder();
	
	@Test
	public void encodeTest() {
		String senha = "123@fulano";
		String prefixo = "S3nh4";
		
		String senhaCodificada = encoder.encode(senha);
		
		Assertions.assertThat(senhaCodificada).isEqualTo(senha + prefixo);
	}
	
	@Test
	public void matchesTest() {
		String senha = "123@fulano";
		String prefixo = "S3nh4";
		
		boolean batem = encoder.matches(senha, senha + prefixo);
		
		Assertions.assertThat(batem).isTrue();
	}
	
	@Test
	public void notMatchesTest() {
		String senha = "123@fulano";
		
		boolean batem = encoder.matches(senha, senha);
		
		Assertions.assertThat(batem).isFalse();
	}
}
