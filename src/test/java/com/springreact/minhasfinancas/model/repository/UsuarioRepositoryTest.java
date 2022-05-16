package com.springreact.minhasfinancas.model.repository;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
//import org.junit.Test;
//import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.springreact.minhasfinancas.model.entity.Usuario;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UsuarioRepositoryTest {
	
  @Autowired
  UsuarioRepository repository;
  
  @Autowired
  TestEntityManager entityManager; 
  
  @Test
  public void deveVerificarAExistenciaDeUmEmail() {
	  
	  //cenário 
	  Usuario usuario = Usuario.builder().nome("usuario").email("usuario@email.com").build();
	  //repository.save(usuario);
	  entityManager.persist(usuario);
	  
	  //ação / execução
	  boolean result = repository.existsByEmail("usuario@email.com");
	  
	  //verificacao
	  Assertions.assertThat(result).isTrue(); 
	  
  }
  
  @Test
  public void deveRetornarFalsoQuandoNaoHouverUsusarioCadastradoComOEmail() {
	  //cenário
	  //repository.deleteAll();
	  
	  //ação / execução
	  boolean result = repository.existsByEmail("usuario@email.com");
	  
	  //verificacao
	  Assertions.assertThat(result).isFalse();
  }
  
  @Test
  public void devePersistirUmUsusarioNaBaseDeDados() {
	  
	  //cenário
	  Usuario usuario = criarUsuario();
	  
	  //ação
	  Usuario usuarioSalvo = repository.save(usuario);
	  
	  //verificação
	  Assertions.assertThat(usuarioSalvo.getId()).isNotNull();
	  
  }
  
  @Test
  public void deveBuscarUmUsuarioPorEmail() {
	  
	  //cenário
	  Usuario usuario = criarUsuario();
	  entityManager.persist(usuario);
	  
	  //ação
	  Optional<Usuario> result = repository.findByEmail("usuario@email.com");
	  
	  //verificação
	  Assertions.assertThat((result).isPresent()).isTrue();
  }
  
  @Test
  public void deveRetornarVazioAoBuscarUsuarioPorEmailQuandoNaoExisteNaBase() {
	  
	  //cenário
	  //Neste cenário o usuário não foi criado na base, portanto, deverá retornar vazio ao buscar o usuári por e-mail e assim satisfazer o teste. 
	  
	  //ação
	  Optional<Usuario> result = repository.findByEmail("usuario@email.com");
	  
	  //verificação
	  Assertions.assertThat((result).isPresent()).isFalse();	  
	  
  }
  
  public static Usuario criarUsuario() {
	  return Usuario.builder().nome("usuario").email("usuario@email.com").senha("senha").build();
  }

}
