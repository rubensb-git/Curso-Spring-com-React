package com.springreact.minhasfinancas.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springreact.minhasfinancas.exception.ErroAutenticacao;
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
		
		Optional<Usuario> usuario = repository.findByEmail(email);			
		
		if(!usuario.isPresent()) {
			throw new ErroAutenticacao("Usuário não encontrado para o e-mail informado. ");
		}
		
		if(!usuario.get().getSenha().equals(senha)) {
			throw new ErroAutenticacao("Senha inválida. ");
		}
		
		return usuario.get();
	}

	@Override
	@Transactional
	public Usuario salvarUsuario(Usuario usuario) {
		validarEmail(usuario.getEmail());		
		return repository.save(usuario);
	}

	@Override
	public void validarEmail(String email) {		
		boolean existe = repository.existsByEmail(email);
		if(existe) {			
			throw new RegraNegocioException("Já existe um usuário cadastrado para este email. "); 			
		}
		
	}

	@Override
	public Optional<Usuario> obterporId(Long id) {
		
		return repository.findById(id);
	}
	

}
