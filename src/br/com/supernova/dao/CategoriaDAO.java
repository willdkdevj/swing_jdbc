package br.com.supernova.dao;

import br.com.supernova.model.Categoria;
import br.com.supernova.model.Produto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    private Connection connection;

    public CategoriaDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Categoria> listar() throws SQLException {
        String sql = "SELECT ID, NAME FROM CATEGORIA";

        List<Categoria> categorias = null;
        try(PreparedStatement prepare = connection.prepareStatement(sql)){
            prepare.execute();

            try(ResultSet result = prepare.getResultSet()){
                if (categorias == null) categorias = new ArrayList<>();

                while(result.next()){
                    Categoria categoria= new Categoria(result.getInt(1), result.getString(2));


                    categorias.add(categoria);
                }
            }
        }
        return categorias;
    }

    public List<Categoria> listarComProduto() throws SQLException {
        Categoria categoriaUnica = null;
        List<Categoria> categorias = null;

        String sql = "SELECT C.ID, C.NAME, P.ID, P.NAME, P.DESCRIPTION "
                + "FROM CATEGORIA C "
                + "INNER JOIN PRODUTO P ON C.ID= P.CATEGORIA_ID";

        try(PreparedStatement prepare = connection.prepareStatement(sql)){
            prepare.execute();

            try(ResultSet result = prepare.getResultSet()){
                if(categorias == null) categorias = new ArrayList<>();

                while (result.next()){
                    if(categoriaUnica == null || !categoriaUnica.getNome().equals(result.getString(2))){
                        Categoria categoria = new Categoria(result.getInt(1), result.getString(2));

                        categorias.add(categoria);
                        categoriaUnica = categoria;
                    }
                    Produto produto = new Produto(result.getInt(3), result.getString(4), result.getString(5));
                    categoriaUnica.adicionar(produto);
                }
            }
        }
        return categorias;
    }
}
