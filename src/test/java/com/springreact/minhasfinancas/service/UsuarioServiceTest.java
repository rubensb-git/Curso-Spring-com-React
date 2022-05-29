package com.springreact.minhasfinancas.service;


import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.springreact.minhasfinancas.exception.ErroAutenticacao;
import com.springreact.minhasfinancas.exception.RegraNegocioException;
import com.springreact.minhasfinancas.model.entity.Usuario;
import com.springreact.minhasfinancas.model.repository.UsuarioRepository;
import com.springreact.minhasfinancas.service.impl.UsuarioServiceImpl;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {
	
	@SpyBean
	UsuarioServiceImpl service;		
	
	@MockBean
	UsuarioRepository repository;		
	
	//@Test(expected = Test.None.class)
	@Test
	public void deveSalvarUmUsuario() {
		//cenário
		Mockito.doNothing().when(service).validarEmail(Mockito.anyString());
		
		Usuario usuario = Usuario.builder().id(1l).nome("nome").email("email@email.com").senha("senha").build();
		
		Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(usuario);
		
		//ação
		Usuario usuarioSalvo = service.salvarUsuario(new Usuario()); 
				
		//verificação
		Assertions.assertThat(usuarioSalvo).isNotNull();
		Assertions.assertThat(usuarioSalvo.getId()).isEqualTo(1l);
		Assertions.assertThat(usuarioSalvo.getNome()).isEqualTo("nome");
		Assertions.assertThat(usuarioSalvo.getEmail()).isEqualTo("email@email.com");
		Assertions.assertThat(usuarioSalvo.getSenha()).isEqualTo("senha");
		
	}
	
	//@Test(expected = RegraNegocioException.class)
	@Test
	public void naoDeveSalvarUmUsuarioComEmailJaCadastrado() {
		//Cenário
		String email = "email@email.com";
		Usuario usuario = Usuario.builder().email(email).build();		
		Mockito.doThrow(RegraNegocioException.class).when(service).validarEmail(email);		
		
		//Ação
		service.salvarUsuario(new Usuario());
		
		//Verificação
		Mockito.verify(repository, Mockito.never()).save(usuario);		

	}
	
	//@Test(expected = Test.None.class)
	@Test
	public void deveAutenticarUmUsuarioComSucesso() {
		
		//cenário
		String email = "email@email.com";
		String senha = "senha";
		
		Usuario usuario = Usuario.builder().email(email).senha(senha).id(1L).build();
		Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(usuario));
		
		//acao
		Usuario result = service.autenticar(email, senha);
		
		//verificacao
		Assertions.assertThat(result).isNotNull();
		
	}
	
	@Test
	public void deveLancarErroQuandoNaoEncontrarUsuarioCadastradoComOEmailInformado() {
		//cenário
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
		
		//acao
		Throwable exception = Assertions.catchThrowable(() -> service.autenticar("email@email.com", "senha"));
		
		//Verificacao
		Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("Usuário não encontrado para o e-mail informado. ");		
		
	}
		
	@Test
	public void deveLancarErroQuandoSenhaNaoBater() {
		//cenario
		String senha = "senha"; 
		Usuario usuario = Usuario.builder().email("email@email.com").senha(senha).build();
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));
		
		//acao
		Throwable exception = Assertions.catchThrowable(() -> service.autenticar("email@email.com", "123"));
		
		Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("Senha inválida. ");
				
		
	}
	
	//@Test(expected = Test.None.class) // Eu espero que ele não lance nenhuma exceção com este comando
	@Test
	public void deveValidarEmail() {
		//cenário
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);
		
		//acao
		service.validarEmail("email@email.com");
				
	}
	
	//@Test(expected = RegraNegocioException.class) // Aqui eu espero que ele lance a exceção RegraNegocioExcpetion. 
	@Test
	public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado() {
		
		//cenario
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);
		
		//acao
		Throwable exception = Assertions.catchThrowable(() -> service.validarEmail("email@email.com"));
		
		Assertions.assertThat(exception).isInstanceOf(RegraNegocioException.class).hasMessage("Já existe um usuário cadastrado para este email. ");
							
	}

}
