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
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtService {
	
	@Value("${security.jwt.expiracao}")
	private String expiracao;
	
	@Value("${security.jwt.chave-assinatura}")
	private String chaveAssinatura;

	public String gerarToken( Usuario usuario ) {
		Map<String, Object> claims = new HashMap<String, Object>();
		claims.put("sub", usuario.getEmail());
		
		LocalDateTime agora = LocalDateTime.now();
		long expString = Long.valueOf(expiracao);
		LocalDateTime expiracao = agora.plusMinutes( expString );
		Date data = Date.from( expiracao.atZone(ZoneId.systemDefault() ).toInstant() );
	
		return Jwts
				.builder()
				.setSubject(usuario.getEmail())
				.setExpiration(data)
				.signWith( SignatureAlgorithm.HS512, "asdfa1sdfa1sd32f1a3d1f3")
				.compact();
	}
	
	public String obterUsuario( String token ) {
		return (String) obterClaims(token).get("sub");
	}
	
	public Claims obterClaims( String token ) {
		return Jwts.parser().setSigningKey("asdfa1sdfa1sd32f1a3d1f3").parseClaimsJws(token).getBody();
	}
	
	public boolean tokenValido( String token ) {
		Date expiration = (Date) obterClaims(token).getExpiration();
		LocalDateTime data = expiration.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		return !LocalDateTime.now().isAfter(data);
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
