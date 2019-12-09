package com.dsousa.minhasfinancas.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.dsousa.minhasfinancas.model.entity.Usuario;
import com.dsousa.minhasfinancas.service.UsuarioService;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class JwtAuthFilter extends OncePerRequestFilter{
	
	private static final String AUTH_HEADER = "Authorization";
	private final JwtService jwtService;
	private final UsuarioService usuarioService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String authorizationHeader = request.getHeader(AUTH_HEADER);
		
		if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			String token = authorizationHeader.split(" ")[1];
			boolean tokenValido = jwtService.tokenValido(token);
			if(tokenValido) {
				String email = jwtService.obterUsuario(token);
				Optional<Usuario> usuario = usuarioService.buscarPorEmail(email);
				UsernamePasswordAuthenticationToken user = new 
							UsernamePasswordAuthenticationToken(usuario.get(), null, Arrays.asList(new SimpleGrantedAuthority("USER")));
				user.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(user);
				
			}
		}
		
		filterChain.doFilter(request, response);
		
	}

}
