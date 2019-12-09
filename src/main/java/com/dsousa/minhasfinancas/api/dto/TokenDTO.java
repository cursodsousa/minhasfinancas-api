package com.dsousa.minhasfinancas.api.dto;

import com.dsousa.minhasfinancas.model.entity.Usuario;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenDTO {

	private String token;
	private Usuario usuario;
}
