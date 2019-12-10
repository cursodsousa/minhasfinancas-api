package com.dsousa.minhasfinancas.security;

import org.springframework.security.crypto.password.PasswordEncoder;

public class CustomPasswordEncoder implements PasswordEncoder {
	
	private static final String SUFIXO = "S3nh4";
	
	@Override
	public String encode(CharSequence senhaDigitada) {
		return senhaDigitada + SUFIXO;
	}
	
	@Override
	public boolean matches( CharSequence senhaDigitada, String senhaCodificada ) {
		String senhaDescodificada = senhaCodificada.substring( 0, senhaCodificada.indexOf(SUFIXO) );
		return senhaDescodificada.equals(senhaDigitada);
	}
}
