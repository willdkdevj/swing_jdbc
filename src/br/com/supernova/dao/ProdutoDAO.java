package br.com.supernova.dao;

import br.com.supernova.model.Categoria;
import br.com.supernova.model.Produto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    private Connection connection;

    public ProdutoDAO(Connection connection) {
        this.connection = connection;
    }

    public void salver(Produto produto) throws SQLException {
        String sql = "INSERT INTO PRODUTO (NAME, DESCRIPTION) VALUES (?, ?)";

        try(PreparedStatement prepare = connection.prepareStatement(sql)){
            prepare.setString(1, produto.getNome());
            prepare.setString(2, produto.getDescricao());
            prepare.execute();

            try(ResultSet result = prepare.getResultSet()){
                while (result.next()){
                    produto.setId(result.getInt(1));
                }
            }
        }
    }

    public void salvarComCategoria(Produto produto) throws SQLException {
        String sql = "INSERT INTO PRODUTO (NAME, DESCRIPTION, CATEGORIA_ID) VALUES (?, ?, ?)";

        try(PreparedStatement prepare = connection.prepareStatement(sql)){
            prepare.setString(1, produto.getNome());
            prepare.setString(2, produto.getDescricao());
            prepare.setInt(3, produto.getCategoriaId());
            prepare.execute();

            try(ResultSet result = prepare.getResultSet()){
                while(result.next()){
                    produto.setId(result.getInt(1));
                }
            }
        }
    }

    public List<Produto> listar() throws SQLException {
        String sql = "SELECT ID, NAME, DESCRIPTION FROM PRODUTO";

        List<Produto> produtos = null;
        try(PreparedStatement prepare = connection.prepareStatement(sql)){
            prepare.execute();

            verificarRetornoDeProdutos(prepare);
        }
        return produtos;
    }

    public List<Produto> buscar(Categoria categoria) throws SQLException {
        String sql = "SELECT ID, NAME, DESCRIPTION FROM PRODUTO WHERE CATEGORIA_ID = ?";

        List<Produto> produtos = null;
        try(PreparedStatement prepare = connection.prepareStatement(sql)) {
            prepare.setInt(1, categoria.getId());
            prepare.execute();

            verificarRetornoDeProdutos(prepare);
        }
        return produtos;
    }

    public void deletar(Integer id) throws SQLException {
        String sql = "DELETE FROM PRODUTO WHERE ID = ?";

        try(PreparedStatement prepare = connection.prepareStatement(sql)){
            prepare.setInt(1, id);
            prepare.execute();
        }
    }

    public void alterar(String nome, String descricao, Integer id) throws SQLException {
        String sql = "UPDATE PRODUTO P SET P.NAME = ?, P.DESCRIPTION = ? WHERE ID = ?";

        try(PreparedStatement prepare = connection.prepareStatement(sql)){
            prepare.setString(1, nome);
            prepare.setString(2, descricao);
            prepare.setInt(3, id);
            prepare.execute();
        }
    }

    private static void verificarRetornoDeProdutos(PreparedStatement prepare) throws SQLException {
        List<Produto> produtos;
        try(ResultSet result = prepare.getResultSet()){
            produtos = new ArrayList<>();
            while (result.next()){
                Produto produto = new Produto(result.getInt(1), result.getString(2), result.getString(3));
                produtos.add(produto);
            }
        }
    }
}
