package com.dsousa.minhasfinancas.security;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.dsousa.minhasfinancas.model.entity.Usuario;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtService {
	
	@Value("${security.jwt.expiracao}")
	private String expiracao;
	
	@Value("${security.jwt.chave-assinatura}")
	private String chaveAssinatura;

	public String gerarToken( Usuario usuario ) {
		long expString = Long.valueOf(expiracao);
		LocalDateTime expiracao =  LocalDateTime.now().plusMinutes( expString );
		Date data = Date.from( expiracao.atZone(ZoneId.systemDefault() ).toInstant() );
	
		return Jwts
				.builder()
				.setSubject(usuario.getEmail())
				.setExpiration(data)
				.signWith( SignatureAlgorithm.HS512, chaveAssinatura)
				.compact();
	}
	
	public String obterUsuario( String token ) throws ExpiredJwtException{
		return (String) obterClaims(token).get("sub");
	}
	
	public Claims obterClaims( String token ) throws ExpiredJwtException {
		return Jwts.parser().setSigningKey(chaveAssinatura).parseClaimsJws(token).getBody();
	}
	
	public boolean tokenValido( String token ) {
		try {
			Date expiration = (Date) obterClaims(token).getExpiration();
			LocalDateTime data = expiration.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
			return !LocalDateTime.now().isAfter(data);
		}catch (Exception e) {
			return false;
		}
	}
	
	public static void main(String[] args) {
		JwtService s = new JwtService();
		String token = s.gerarToken(Usuario.builder().email("wilso@email.com").build());
		String userName = s.obterUsuario(token);
		Claims claims = s.obterClaims(token);
		boolean valido = s.tokenValido(token);
		System.out.println(userName);
	}
}
