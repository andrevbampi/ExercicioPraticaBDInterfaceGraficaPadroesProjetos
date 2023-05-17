package controller.dao;

import java.util.List;

import controller.exception.DaoException;
import model.Usuario;

public interface UsuarioDao {
	
	public void criarEstruturaSeNaoExistir() throws DaoException;
	public void inserir(Usuario usuario) throws DaoException;
	public List<Usuario> listarTodos() throws DaoException;
	public boolean excluir(String login) throws DaoException;
	public Usuario buscarPorLogin(String login) throws DaoException;
	public void editar(Usuario usuario) throws DaoException;
}
