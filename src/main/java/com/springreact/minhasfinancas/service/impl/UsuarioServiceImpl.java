package com.springreact.minhasfinancas.service.impl;

import org.springframework.stereotype.Service;

import com.springreact.minhasfinancas.exception.RegraNegocioException;
import com.springreact.minhasfinancas.model.entity.Usuario;
import com.springreact.minhasfinancas.model.repository.UsuarioRepository;
import com.springreact.minhasfinancas.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {
	
	private UsuarioRepository repository;
	
	public UsuarioServiceImpl(UsuarioRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public Usuario autenticar(String email, String senha) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Usuario salvarUsuario(Usuario usuario) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validarEmail(String email) {
		
		boolean existe = repository.existsByEmail(email);
		if(existe) {
			throw new RegraNegocioException("Já existe um usuário cadastrado para este email. "); 
		}
		
	}
	

}
