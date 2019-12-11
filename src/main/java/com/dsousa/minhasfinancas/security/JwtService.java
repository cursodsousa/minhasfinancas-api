package com.dsousa.minhasfinancas.security;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;

import com.dsousa.minhasfinancas.model.entity.Usuario;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtService {
	
	@Value("${security.jwt.expiracao}")
	private String expiracao;
	
	@Value("${security.jwt.chave-assinatura}")
	private String chaveAssinatura;

	public String gerarToken( Usuario usuario ) {
		
		long expiracaoLong = Long.valueOf(expiracao);
		LocalDateTime dataHoraExpiracao = LocalDateTime.now().plusMinutes(expiracaoLong);
		Date exp = Date.from( dataHoraExpiracao.atZone( ZoneId.systemDefault() ).toInstant() );
		
		return Jwts
					.builder()
					.setSubject( usuario.getEmail() )
					.setExpiration( exp )
					.signWith( SignatureAlgorithm.HS512, chaveAssinatura )
					.compact();
	}
	
	public String obterUsuario(String token) {
		Claims claims = obterClaims(token);
		return (String) claims.get("sub");
	}
	
	public boolean tokenValido( String token ) {
		Claims claims = obterClaims(token);
		Date dataHoraExpiracao = claims.getExpiration();
		LocalDateTime expiracao = dataHoraExpiracao
										.toInstant()
										.atZone(ZoneId.systemDefault())
										.toLocalDateTime();
		
		return !LocalDateTime.now().isAfter(expiracao);
	}
	
	public Claims obterClaims(String token) {
		return Jwts
					.parser()
					.setSigningKey( chaveAssinatura )
					.parseClaimsJws( token )
					.getBody();
	}
}
