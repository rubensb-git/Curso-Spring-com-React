package com.springreact.minhasfinancas.api.resource;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springreact.minhasfinancas.api.dto.AtualizaStatusDTO;
import com.springreact.minhasfinancas.api.dto.LancamentoDTO;
import com.springreact.minhasfinancas.exception.RegraNegocioException;
import com.springreact.minhasfinancas.model.entity.Lancamento;
import com.springreact.minhasfinancas.model.entity.Usuario;
import com.springreact.minhasfinancas.model.enums.StatusLancamento;
import com.springreact.minhasfinancas.model.enums.TipoLancamento;
import com.springreact.minhasfinancas.service.LancamentoService;
import com.springreact.minhasfinancas.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/lancamentos")
@RequiredArgsConstructor
public class LancamentoResource {
	
	private final LancamentoService service;
	private final UsuarioService usuarioService;	
	
	@GetMapping
	public ResponseEntity buscar(@RequestParam(value="descricao", required = false) String descricao, 
								 @RequestParam(value="mes", required = false) Integer mes, 
								 @RequestParam(value="ano", required=false)Integer ano, 
								 @RequestParam(value="usuario")Long idUsuario) {
		
		Lancamento lancamentoFiltro = new Lancamento();
		lancamentoFiltro.setDescricao(descricao);
		lancamentoFiltro.setMes(mes);
		lancamentoFiltro.setAno(ano);
		
		Optional<Usuario> usuario = usuarioService.obterporId(idUsuario);
		
		if(!usuario.isPresent()) {
			return ResponseEntity.badRequest().body("Não foi possível realizar a consulta. Usuário não encontrado para os parâmetros informados. ");
		}
		else {
			lancamentoFiltro.setUsuario(usuario.get());
		}
		
		List<Lancamento> lancamentos = service.buscar(lancamentoFiltro);
		
		return ResponseEntity.ok(lancamentos);
		
	}
	
	@PostMapping
	public ResponseEntity salvar(@RequestBody LancamentoDTO dto) {
		
		try {
			Lancamento entidade = converter(dto);
			entidade = service.salvar(entidade);
			return new ResponseEntity(entidade, HttpStatus.CREATED);
			
		} catch (RegraNegocioException e) 
		{
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}	
	

	@PutMapping("{id}")
	public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody LancamentoDTO dto) {				
						
		 return service.obterPorId(id).map(entity -> {	
			try {
				Lancamento lancamento = converter(dto);
				lancamento.setId(entity.getId());
				service.atualizar(lancamento);
				return ResponseEntity.ok(entity);							
			} catch(RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet( () -> new ResponseEntity("Lançamento não encontrado na base de Dados", HttpStatus.BAD_REQUEST));
		
	}	
	
	@PutMapping("{id}/atualiza-status")
	public ResponseEntity atualizarStatus(@PathVariable("id") Long id, @RequestBody AtualizaStatusDTO dto) {				
				
		return service.obterPorId(id).map( entity -> {
			StatusLancamento statusSelecionado = StatusLancamento.valueOf(dto.getStatus());
			entity.getId();
			if(statusSelecionado == null) {
				return ResponseEntity.badRequest().body("Não foi possível alterar o status. ");
			}
			
			try {
				
				entity.setStatus(statusSelecionado);
				service.atualizar(entity);
				return ResponseEntity.ok(entity);
				
			} catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() ->
		       new ResponseEntity("Lançamento não encontrado na base de dados", HttpStatus.BAD_REQUEST));
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity deletar(@PathVariable("id") Long id) {
		return service.obterPorId(id).map(entity -> {
			service.deletar(entity);
			return new ResponseEntity(HttpStatus.NO_CONTENT); 
		}).orElseGet( () -> new ResponseEntity("Lançamento não encontrado na base de Dados", HttpStatus.BAD_REQUEST) );
	}
	
	private Lancamento converter(LancamentoDTO dto) {
		Lancamento lancamento = new Lancamento();
		lancamento.setId(dto.getId());
		lancamento.setDescricao(dto.getDescricao());
		lancamento.setAno(dto.getAno());
		lancamento.setMes(dto.getMes());
		lancamento.setValor(dto.getValor());
		
		Usuario usuario = usuarioService.obterporId(dto.getUsuario()).orElseThrow(() -> new RegraNegocioException("Usuário não encontrado para o Id informado. "));
		
		lancamento.setUsuario(usuario);
		
		if(dto.getTipo() != null) {
			lancamento.setTipo(TipoLancamento.valueOf(dto.getTipo()));	
		}
		
		if(dto.getStatus() != null) {
			lancamento.setStatus(StatusLancamento.valueOf(dto.getStatus()));	
		}		
		
		return lancamento;
	}
	
	

}