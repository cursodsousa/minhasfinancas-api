package com.dsousa.minhasfinancas.api.dto;

import com.dsousa.minhasfinancas.model.entity.Usuario;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TokenDTO {

	private Usuario usuario;
	private String token;
	
}
