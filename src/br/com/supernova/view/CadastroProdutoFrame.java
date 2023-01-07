package br.com.supernova.view;

import br.com.supernova.controller.CategoriaController;
import br.com.supernova.controller.ProdutoController;
import br.com.supernova.model.Categoria;
import br.com.supernova.model.Produto;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CadastroProdutoFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private JLabel labelName, labelDescription, labelCategory;
    private JTextField textName, textDescription;
    private JComboBox<Categoria> comboCategory;
    private JButton buttonSave, buttonEdit, buttonClear, buttonDelete;
    private JTable table;
    private DefaultTableModel model;
    private ProdutoController produtoController;
    private CategoriaController categoriaController;

    public CadastroProdutoFrame(){
        super("Produtos");
        Container container = getContentPane();
        setLayout(null);

        this.categoriaController = new CategoriaController();
        this.produtoController = new ProdutoController();

        labelName = new JLabel("Nome do Produto");
        labelDescription = new JLabel("Descrição do Produto");
        labelCategory = new JLabel("Categoria do Produto");

        labelName.setBounds(10,10,240, 15);
        labelDescription.setBounds(10, 50, 240, 15);
        labelCategory.setBounds(10,90, 240, 15);

        labelName.setForeground(Color.BLACK);
        labelDescription.setForeground(Color.BLACK);
        labelCategory.setForeground(Color.BLACK);

        container.add(labelName);
        container.add(labelDescription);
        container.add(labelCategory);

        textName = new JTextField();
        textDescription = new JTextField();
        comboCategory = new JComboBox<Categoria>();

        comboCategory.addItem(new Categoria(0, "Selecione"));
        List<Categoria> categorias = this.listarCategoria();
        for (Categoria categoria : categorias){
            comboCategory.addItem(categoria);
        }

        textName.setBounds(10, 25, 265,20);
        textDescription.setBounds(10, 65, 265,20);
        comboCategory.setBounds(10, 105,265,20);

        container.add(textName);
        container.add(textDescription);
        container.add(comboCategory);

        buttonSave = new JButton("Salvar");
        buttonClear = new JButton("Limpar");

        buttonSave.setBounds(10,145,80, 20);
        buttonClear.setBounds(100, 145,90,20);

        container.add(buttonSave);
        container.add(buttonClear);

        table = new JTable();
        model = (DefaultTableModel) table.getModel();

        model.addColumn("ID do Produto");
        model.addColumn("Nome do Produto");
        model.addColumn("Descrição do Produto");

        preencherTabela();

        table.setBounds(10,185, 760,300);
        container.add(table);

        buttonDelete = new JButton("Excluir");
        buttonEdit = new JButton("Alterar");

        buttonDelete.setBounds(10,500,80,20);
        buttonEdit.setBounds(100, 500,90,20);

        container.add(buttonDelete);
        container.add(buttonEdit);

        setSize(800, 600);
        setVisible(Boolean.TRUE);
        setLocationRelativeTo(null);

        buttonSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvar();
                limparTabela();
                preencherTabela();
            }
        });

        buttonClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpar();
            }
        });

        buttonDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deletar();
                limparTabela();
                preencherTabela();
            }
        });

        buttonEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                alterar();
                limparTabela();
                preencherTabela();
            }
        });
    }

    private void limparTabela() {
        model.getDataVector().clear();
    }

    private void alterar(){
        Object objetOfLine= (Object) model.getValueAt(table.getSelectedRow(), table.getSelectedColumn());
        if(objetOfLine instanceof Integer){
            Integer id = (Integer) objetOfLine;
            String nome = (String) model.getValueAt(table.getSelectedRow(), 1);
            String descricao = (String) model.getValueAt(table.getSelectedRow(), 2);
            this.produtoController.alterar(nome, descricao, id);
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, selecione o ID");
        }
    }

    private void deletar() {
        Object objectOfLine = (Object) model.getValueAt(table.getSelectedRow(), table.getSelectedColumn());
        if(objectOfLine instanceof Integer){
            Integer id = (Integer) objectOfLine;
            this.produtoController.deletar(id);
            model.removeRow(table.getSelectedRow());
            JOptionPane.showMessageDialog(this, "Item excluídocom sucesso!");
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, selecionar o ID");
        }
    }

    private void limpar() {
        this.textName.setText("");
        this.textDescription.setText("");
        this.comboCategory.setSelectedIndex(0);
    }
    private void preencherTabela() {
        List<Produto> produtos = listarProduto();
        try {
            for(Produto produto : produtos){
                model.addRow(new Object[]{ produto.getId(), produto.getNome(), produto.getDescricao() });
            }
        } catch (Exception ex){
            throw ex;
        }
    }

    private void salvar() {
        if (!textName.getText().equals("") && !textDescription.getText().equals("")) {
            Produto produto = new Produto(textName.getText(), textDescription.getText());
            Categoria categoria = (Categoria) comboCategory.getSelectedItem();
            produto.setCategoriaId(categoria.getId());
            this.produtoController.salvar(produto);
            JOptionPane.showMessageDialog(this, "Salvo com sucesso!");
            this.limpar();
        } else {
            JOptionPane.showMessageDialog(this, "Nome e Descrição devem ser informados.");
        }
    }

    private java.util.List<Produto> listarProduto() {
        return this.produtoController.listar();
    }

    private java.util.List<Categoria> listarCategoria() {
        return this.categoriaController.listar();
    }
}
