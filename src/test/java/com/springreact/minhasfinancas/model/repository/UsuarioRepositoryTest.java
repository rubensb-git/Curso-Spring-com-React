package com.springreact.minhasfinancas.model.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
//import org.junit.Test;
//import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.springreact.minhasfinancas.model.entity.Usuario;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class UsuarioRepositoryTest {
	
  @Autowired
  UsuarioRepository repository;
  
  @Test
  public void deveVerificarAExistenciaDeUmEmail() {
	  
	  //cenário 
	  Usuario usuario = Usuario.builder().nome("usuario").email("usuario@email.com").build();
	  repository.save(usuario);
	  
	  //ação / execução
	  boolean result = repository.existsByEmail("usuario@email.com");
	  
	  //verificacao
	  Assertions.assertThat(result).isTrue(); 
	  
  }

}
