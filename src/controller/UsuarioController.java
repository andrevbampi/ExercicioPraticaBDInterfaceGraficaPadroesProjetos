package controller;

import java.util.List;

import javax.swing.JOptionPane;

import controller.dao.UsuarioDao;
import controller.dao.impl.UsuarioDaoImpl;
import controller.exception.BusinessRuleException;
import controller.exception.DaoException;
import model.Usuario;
import view.JFrameUsuario;

public class UsuarioController {

	UsuarioDao usuarioDao;
	
	public UsuarioController() {
		usuarioDao = new UsuarioDaoImpl();
	}
	
	private void validarUsuario(Usuario usuario) throws BusinessRuleException {
		if (usuario.getLogin().trim().equals("")) {
			throw new BusinessRuleException("Login não informado.");
		}
		if (usuario.getSenha().trim().equals("")) {
			throw new BusinessRuleException("Senha não informada.");
		}
		if (usuario.getNome().trim().equals("")) {
			throw new BusinessRuleException("Nome não informado.");
		}
		if (usuario.getGenero() != 'M' &&
				usuario.getGenero() != 'F' &&
				usuario.getGenero() != 'O') {
			throw new BusinessRuleException("Gênero inválido.");
		}
	}
	
	public void inserir(Usuario usuario) throws BusinessRuleException {
		validarUsuario(usuario);
		try {
			usuarioDao.inserir(usuario);
		} catch (DaoException e) {
			throw new BusinessRuleException("Falha técnica ao inserir usuário: " + e.getMessage());
		}
	}
	
	public void editar(Usuario usuario) throws BusinessRuleException {
		validarUsuario(usuario);
		try {
			usuarioDao.editar(usuario);
		} catch (DaoException e) {
			throw new BusinessRuleException("Falha técnica ao editar usuário: " + e.getMessage());
		}
	}
	
	public Usuario buscarPorLogin(String login) throws BusinessRuleException {
		try {
			return usuarioDao.buscarPorLogin(login);
		} catch (DaoException e) {
			throw new BusinessRuleException("Falha ténica ao buscar usuário por login: " + e.getMessage());
		}
	}
	
	public List<Usuario> listarTodos() throws BusinessRuleException {
		try {
			return usuarioDao.listarTodos();
		} catch (DaoException e) {
			throw new BusinessRuleException("Falha ténica ao buscar todos os usuários: " + e.getMessage());
		}
	}
	
	public boolean excluir(String login) throws BusinessRuleException {
		try {
			return usuarioDao.excluir(login);
		} catch (DaoException e) {
			throw new BusinessRuleException("Falha ténica ao excluir usuário: " + e.getMessage());
		}
	}
	
	public void criarEstruturaSeNaoExistir() throws BusinessRuleException {
		try {
			usuarioDao.criarEstruturaSeNaoExistir();
		} catch (DaoException e) {
			throw new BusinessRuleException("Falha ténica ao criar a estrutura de dados: " + e.getMessage());
		}
	}
	
	public void efetuarLogin(String login, String senha) throws BusinessRuleException {
		Usuario usuario = buscarPorLogin(login.trim());
		if (usuario == null) {
			throw new BusinessRuleException("Usuário inválido.");
		} else {
			if (!usuario.getSenha().trim().equals(senha.trim())) {
				throw new BusinessRuleException("Senha inválida.");
			}
		}
	}
}
