package com.springreact.minhasfinancas.service;


import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.springreact.minhasfinancas.exception.RegraNegocioException;
import com.springreact.minhasfinancas.model.entity.Usuario;
import com.springreact.minhasfinancas.model.repository.UsuarioRepository;
import com.springreact.minhasfinancas.service.impl.UsuarioServiceImpl;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {
	
	@Autowired
	UsuarioService service;
	
	@Autowired
	UsuarioRepository repository;
	
	@Autowired
	UsuarioServiceImpl usuarioimpl;
	
	//@Test(expected = Test.None.class) // Eu espero que ele não lance nenhuma exceção com este comando
	@Test
	public void deveValidarEmail() {
		//cenário
		repository.deleteAll();
		
		//acao
		service.validarEmail("email@email.com");
				
	}
	
	//@Test(expected = RegraNegocioException.class) // Aqui eu espero que ele lance a exceção RegraNegocioExcpetion
	@Test
	public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado() {
		
		//cenario
		Usuario usuario = Usuario.builder().nome("usuario").email("email@email.com").build();
		repository.save(usuario);
		
		//acao
		//service.validarEmail("email@email.com");
		
		//Não funcionou com a cláusula expected da anotation Test conforme o vídeo, mas funcionou da forma abaixo
		assertThrows(RegraNegocioException.class, () -> {			
			service.validarEmail("email@email.com");
		});
		
	}

}
