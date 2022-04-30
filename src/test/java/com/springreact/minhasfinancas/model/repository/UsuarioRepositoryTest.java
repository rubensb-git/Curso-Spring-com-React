package com.springreact.minhasfinancas.model.repository;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.springreact.minhasfinancas.model.entity.Usuario;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class UsuarioRepositoryTest {
	
  @Autowired
  UsuarioRepository repository;
  
  @Test
  public void deveVerificarAExistenciaDeUmEmail() {
	  
	  //cenário 
	  Usuario usuario = new Usuario();
	  usuario.setNome("usuario");
	  usuario.setEmail("usuario@email.com");
	  repository.save(usuario);
	  
	  //ação / execução
	  boolean result = repository.existsByEmail("usuario@email.com");
	  
	  //verificacao
	  Assertions.assertThat(result).isTrue(); 
	  
  }

}
