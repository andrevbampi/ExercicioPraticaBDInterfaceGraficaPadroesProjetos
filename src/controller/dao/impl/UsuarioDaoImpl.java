package controller.dao.impl;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import controller.dao.UsuarioDao;
import controller.dao.impl.util.ConnectionMariaDB;
import controller.exception.DaoException;
import model.Usuario;

public class UsuarioDaoImpl implements UsuarioDao {
	
	public void criarEstruturaSeNaoExistir() throws DaoException {
		try {
			//Verificar se a tabela existe. Caso não existir, criar e inserir um usuário padrão.
			if (!tabelaExiste()) {
			
				//Iniciando a conexão com o banco de dados
				Connection connection = ConnectionMariaDB.conectar();
				
				//Comando de criar a tabela
				String comandoSql = "CREATE TABLE IF NOT EXISTS exercicioBD.usuario ("
										+ "login VARCHAR(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,"
										+ "senha VARCHAR(50) NOT NULL,"
										+ "nome VARCHAR(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,"
									    + "genero CHAR NOT NULL,"
										+ "administrador BOOLEAN NOT NULL,"
									    + "PRIMARY KEY (login))";
				
				//Preparando e executando o comando SQL
				Statement statement = connection.createStatement();
				statement.execute(comandoSql);
	
				//Fechando a conexão com o banco de dados. Nunca esquecer de fazer isso após terminar de utilizar a conexão.
				connection.close();
				
				//Inserindo um usuário padrão para poder entrar no sistema
				Usuario usuario = new Usuario();
				usuario.setLogin("root");
				usuario.setSenha("123");
				usuario.setNome("Root");
				usuario.setAdministrador(true);
				usuario.setGenero('O');
				
				inserir(usuario);
			}
		} catch (SQLException e) {
			throw new DaoException("Erro ao criar tabela: " + e.getMessage());
		}

	}
	
	private static boolean tabelaExiste() throws SQLException {
		boolean achou = false;
		
		//Iniciando a conexão com o banco de dados
		Connection connection = ConnectionMariaDB.conectar();
		
		String comandoSql = "SELECT table_name"
							+ " FROM information_schema.tables"
							+ " WHERE table_schema = 'exercicioBD'"
							+ " AND table_name = 'usuario'";
		
		//Preparando o comando SQL
		PreparedStatement ps = connection.prepareStatement(comandoSql);
		
		//Executando o comando SQL
		ResultSet rs = ps.executeQuery();
		
		achou = rs.next();

		//Fechando a conexão com o banco de dados. Nunca esquecer de fazer isso após terminar de utilizar a conexão.
		connection.close();
			
		return achou;
	}
	
	public void inserir(Usuario usuario) throws DaoException {
		try {
			//Iniciando a conexão com o banco de dados
			Connection connection = ConnectionMariaDB.conectar();
			
			String comandoSql = "insert into exercicioBD.usuario"
					+ " (login, senha, nome, genero, administrador)"
					+ " values (?, ?, ?, ?, ?)";
			
			//Preparando o comando SQL
			PreparedStatement ps = connection.prepareStatement(comandoSql);
			
			//Substituindo os "?" por valores válidos para serem utilizados no comando SQL
			ps.setString(1, usuario.getLogin());
			ps.setString(2, usuario.getSenha());
			ps.setString(3, usuario.getNome());
			ps.setString(4, String.valueOf(usuario.getGenero()));
			ps.setBoolean(5, usuario.isAdministrador());
			
			//Executando o comando SQL
			ps.execute();
			
			//Fechando a conexão com o banco de dados. Nunca esquecer de fazer isso após terminar de utilizar a conexão.
			connection.close();
			
		} catch (SQLException e) {
			throw new DaoException("Erro ao inserir usuário na tabela: " + e.getMessage());
		}
	}
	
	public List<Usuario> listarTodos() throws DaoException {
		List<Usuario> listaUsuarios = new ArrayList<Usuario>();
		try {
			Connection connection = ConnectionMariaDB.conectar();
			String comandoSql = "select login, senha, nome, genero, administrador"
					+ " from exercicioBD.usuario order by login";
			
			PreparedStatement ps = connection.prepareStatement(comandoSql);
			
			//ResultSet é utilizado para comandos SQL que tenham resultado, como selects.
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				Usuario usuario = new Usuario();
				usuario.setLogin(rs.getString("login"));
				usuario.setSenha(rs.getString("senha"));
				usuario.setNome(rs.getString("nome"));
				usuario.setGenero(rs.getString("genero").charAt(0));
				usuario.setAdministrador(rs.getBoolean("administrador"));
				listaUsuarios.add(usuario);
			}
			
			connection.close();
		} catch (SQLException e) {
			throw new DaoException("Erro ao listar todos os usuários da tabela: " + e.getMessage());
		}
		return listaUsuarios;
	}
	
	public boolean excluir(String login) throws DaoException {
		boolean excluiu;
		try {
			Connection connection = ConnectionMariaDB.conectar();
			
			String comandoSql = "delete from exercicioBD.usuario"
					+ " where login = ?";
			
			PreparedStatement ps = connection.prepareStatement(comandoSql);
			ps.setString(1, login);
			
			excluiu = ps.executeUpdate() > 0;
			
			connection.close();
		} catch (SQLException e) {
			throw new DaoException("Erro ao excluir usuário da tabela: " + e.getMessage());
		}
		return excluiu;
	}
	
	public Usuario buscarPorLogin(String login) throws DaoException {
		Usuario usuario = null;
		try {
			Connection connection = ConnectionMariaDB.conectar();
			
			String comandoSql = "select login, senha, nome, genero, administrador"
					+ " from exercicioBD.usuario where login = ?";
			
			PreparedStatement ps = connection.prepareStatement(comandoSql);
			
			ps.setString(1, login);
			
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				usuario = new Usuario();
				usuario.setLogin(rs.getString("login"));
				usuario.setSenha(rs.getString("senha"));
				usuario.setNome(rs.getString("nome"));
				usuario.setGenero(rs.getString("genero").charAt(0));
				usuario.setAdministrador(rs.getBoolean("administrador"));
			}
			connection.close();
		} catch (SQLException e) {
			throw new DaoException("Erro ao buscar usuário da tabela por login: " + e.getMessage());
		}
		return usuario;
	}

	public void editar(Usuario usuario) throws DaoException {
		try {
			Connection connection = ConnectionMariaDB.conectar();
			String comandoSql = "update exercicioBD.usuario"
					+ " set senha = ?, nome = ?, genero = ?, administrador = ?"
					+ " where login = ?";
			
			PreparedStatement ps = connection.prepareStatement(comandoSql);
			
			ps.setString(1, usuario.getSenha());
			ps.setString(2, usuario.getNome());
			ps.setString(3, String.valueOf(usuario.getGenero()));
			ps.setBoolean(4, usuario.isAdministrador());
			ps.setString(5, usuario.getLogin());
			
			ps.execute();
			
			connection.close();
		} catch (SQLException e) {
			throw new DaoException("Erro ao editar usuário: " + e.getMessage());
		}
	}
}
