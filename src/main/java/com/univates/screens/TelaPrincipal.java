package com.univates.screens;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.Timestamp;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.*;

import com.univates.components.KCombo;
import com.univates.components.KFileChooser;
import com.univates.components.KMessage;
import com.univates.models.Transacao;
import com.univates.models.Usuario;
import com.univates.services.CsvService;
import com.univates.services.TransacaoService;
import com.univates.services.UsuarioService;

public class TelaPrincipal extends JFrame 
{
    private DefaultTableModel modelo_tabela_transacoes_por_mes = new DefaultTableModel(new Object[][] {}, new String[] {"Mês", "Valor"}) 
    {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private DefaultTableModel modelo_tabela_ultimas_transacoes = new DefaultTableModel(new Object[][] {}, new String[] {"Id", "Data", "Valor", "Descrição"}) 
    {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private JTable tabela_transacoes_por_mes  = new JTable(modelo_tabela_transacoes_por_mes);
    private JTable tabela_ultimas_transacoes  = new JTable(modelo_tabela_ultimas_transacoes);
      
    private JLabel nome      = new JLabel();
    private JLabel saldo     = new JLabel("Saldo disponível");
    private JLabel valor     = new JLabel();
    private JLabel historico = new JLabel("Histórico");
    private JLabel resumo    = new JLabel("Movimentação nos meses");
    private JLabel descricao = new JLabel("Descrição (opcional)");
    
    private JTextField textoValor     = new JTextField();
    private JTextField textoDescricao = new JTextField();
    
    private ButtonGroup grupo1  = new ButtonGroup();
    private JRadioButton radio1 = new JRadioButton("Inserir");
    private JRadioButton radio2 = new JRadioButton("Retirar");
    
    private JCheckBox checkbox_data_manual = new JCheckBox("Colocar data manualmente");

    private KCombo<Integer> input_ano  = new KCombo<>( TransacaoService.getAnosParaCombo(), false );
    private KCombo<String>  input_mes  = new KCombo<>( TransacaoService.getMesesParaCombo(), false );
    private KCombo<Integer> input_dia  = new KCombo<>( true );
    
    private JButton botaoConf          = new JButton("Confirma");
    private JButton botaoBaixarDados   = new JButton("Baixar dados");
    private JButton botaoBaixarModelos = new JButton("Baixar modelo");
    private JButton botaoImportDados   = new JButton("Importar dados");

    private Font fonte1 = new Font("Arial", Font.PLAIN, 18);
    private Font fonte2 = new Font("Arial", Font.BOLD, 25);
    private Font fonte3 = new Font("Arial", Font.BOLD, 20);
    private Font fonte4 = new Font("Arial", Font.PLAIN, 15);

    private Color cor1 = new Color(250, 250, 250); //branco
    private Color cor2 = new Color(110, 143,143); //verde 
    private Color cor3 = new Color(102, 102, 102); //cinza

    Usuario usuario;

    public TelaPrincipal()
    {
        setSize(720, 680); 
        getContentPane().setBackground(cor1);
        setTitle("Tela"); 
        setDefaultCloseOperation(EXIT_ON_CLOSE); 
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(null);
        
        this.colocaComponentesNaTela();
        this.setPosicaoComponentes();
        this.setFonteComponentes();
        this.propriedadesDaTabela();
        this.setCorComponentes();

        botaoConf.addActionListener(this::acaoRealizaTransacao);
        checkbox_data_manual.addActionListener(this::acaoCheckboxDataManual);
        input_ano.addActionListener(this::atualizaComboDias);
        input_mes.addActionListener(this::atualizaComboDias);
        
        botaoBaixarModelos.addActionListener(this::acaoBaixarModelos);
        botaoBaixarDados.addActionListener(this::acaoExportarDados);
        botaoImportDados.addActionListener(this::acaoImportarDados);
        
        acaoCheckboxDataManual(null);
        atualizaComboDias(null);

        radio1.setSelected(true);
        grupo1.add(radio1);
        grupo1.add(radio2);
    }
    
        
    private void colocaComponentesNaTela() 
    {
        add(nome);
        add(saldo);
        add(textoValor);
        add(valor);
        add(historico);
        add(resumo);
        add(botaoConf);
        add(botaoBaixarDados);
        add(botaoBaixarModelos);
        add(botaoImportDados);
        add(tabela_ultimas_transacoes);
        add(tabela_transacoes_por_mes);
        add(radio1);
        add(radio2);
        add(checkbox_data_manual);
        add(input_ano);
        add(input_mes);
        add(input_dia);
        add(descricao);
        add(textoDescricao);
    }
    
    private void setPosicaoComponentes() 
    {
        nome                     .setBounds(10, 10, 200, 20); 
        saldo                    .setBounds(10, nome.getY()+40, 200, 20); 
        valor                    .setBounds(10, saldo.getY()+25, 200, 20);
        textoValor               .setBounds(10, valor.getY()+30, 200, 20);
        radio1                   .setBounds(10, textoValor.getY()+25, 90, 20);
        radio2                   .setBounds(100, textoValor.getY()+25, 90, 20);
        botaoConf                .setBounds(10, radio1.getY()+25, 200, 25);
        historico                .setBounds(10, botaoConf.getY()+45, 200, 20);
        tabela_ultimas_transacoes.setBounds(10, historico.getY()+25, 680, 165); 
        resumo                   .setBounds(10, tabela_ultimas_transacoes.getY()+180, 300, 20); 
        tabela_transacoes_por_mes.setBounds(10, resumo.getY()+25, 480, 194);
        descricao                .setBounds(300,valor.getY(), 200, 20);
        textoDescricao           .setBounds(300, textoValor.getY(), 300, 20);
        checkbox_data_manual     .setBounds(300, 130, 300, 20);
        input_ano                .setBounds(300, 155, 100, 25);
        input_mes                .setBounds(400, input_ano.getY(), 100, 25);
        input_dia                .setBounds(500, input_mes.getY(), 100, 25);
        botaoBaixarDados         .setBounds(500, 500, 180, 25);
        botaoBaixarModelos       .setBounds(500, 450, 180, 25);
        botaoImportDados         .setBounds(500, 550, 180, 25);
    }

    private void setFonteComponentes() 
    {
        nome                     .setFont(fonte2);
        saldo                    .setFont(fonte1);
        textoValor               .setFont(fonte1);
        valor                    .setFont(fonte3);
        historico                .setFont(fonte3);
        resumo                   .setFont(fonte3);
        botaoConf                .setFont(fonte1);
        radio1                   .setFont(fonte1);
        radio2                   .setFont(fonte1);
        checkbox_data_manual     .setFont(fonte1);
        tabela_ultimas_transacoes.setFont(fonte4);
        tabela_transacoes_por_mes.setFont(fonte4);
        input_ano                .setFont(fonte1);
        input_mes                .setFont(fonte1);
        input_dia                .setFont(fonte1);
        botaoBaixarDados         .setFont(fonte1);
        botaoBaixarModelos       .setFont(fonte1);
        botaoImportDados         .setFont(fonte1);
        descricao                .setFont(fonte1);
        textoDescricao           .setFont(fonte1);
    }

    private void setCorComponentes() 
    {
        nome                     .setForeground(cor2);
        saldo                    .setForeground(cor3);
        textoValor               .setForeground(cor2);
        textoValor               .setBackground(cor1);
        textoValor               .setBorder(new LineBorder(cor2, 2));
        valor                    .setForeground(cor2);
        historico                .setForeground(cor2);
        resumo                   .setForeground(cor2);
        botaoConf                .setForeground(cor1);
        botaoConf                .setBackground(cor2);
        botaoConf                .setBorder(new LineBorder(cor2, 2));
        radio1                   .setForeground(cor3);
        radio1                   .setBackground(cor1);
        radio2                   .setForeground(cor3);
        radio2                   .setBackground(cor1);
        checkbox_data_manual     .setForeground(cor3);
        checkbox_data_manual     .setBackground(cor1);
        tabela_ultimas_transacoes.setForeground(cor3);
        tabela_ultimas_transacoes.setBackground(cor1);
        tabela_ultimas_transacoes.setSelectionBackground(cor2);
        tabela_ultimas_transacoes.setGridColor(cor3);
        tabela_ultimas_transacoes.setBorder(new LineBorder(cor2, 0));
        tabela_transacoes_por_mes.setForeground(cor3);
        tabela_transacoes_por_mes.setBackground(cor1);
        tabela_transacoes_por_mes.setSelectionBackground(cor2);
        tabela_transacoes_por_mes.setGridColor(cor3);
        tabela_transacoes_por_mes.setBorder(new LineBorder(cor2, 2));
        input_ano                .setForeground(cor2);
        input_ano                .setBackground(cor1);
        input_ano                .setBorder(new LineBorder(cor2, 2));
        input_mes                .setForeground(cor2);
        input_mes                .setBackground(cor1);
        input_mes                .setBorder(new LineBorder(cor2, 2));
        input_dia                .setForeground(cor2);
        input_dia                .setBackground(cor1);
        input_dia                .setBorder(new LineBorder(cor2, 2));
        botaoBaixarDados         .setForeground(cor1);
        botaoBaixarDados         .setBackground(cor2);
        botaoBaixarDados         .setBorder(new LineBorder(cor2, 2));
        botaoBaixarModelos       .setForeground(cor1);
        botaoBaixarModelos       .setBackground(cor2);
        botaoBaixarModelos       .setBorder(new LineBorder(cor2, 2));
        botaoImportDados         .setForeground(cor1);
        botaoImportDados         .setBackground(cor2);
        botaoImportDados         .setBorder(new LineBorder(cor2, 2));
        descricao                .setForeground(cor3);
        textoDescricao           .setForeground(cor2);
        textoDescricao           .setBackground(cor1);
        textoDescricao           .setBorder(new LineBorder(cor2, 2));
    }
    
    private void acaoBaixarModelos( ActionEvent actionEvent )
    {
        KFileChooser explorador_arquivos = new KFileChooser();
        
        explorador_arquivos.setTipoSelecao( JFileChooser.DIRECTORIES_ONLY );
        
        explorador_arquivos.show();        
        
        if ( explorador_arquivos.getTipoSelecao() == JFileChooser.APPROVE_OPTION ) 
        {
            File selectedFile = explorador_arquivos.getSelecao();
            
            try 
            {
                CsvService.modeloCsv( selectedFile.getAbsolutePath(), "Modelo" );
                
                KMessage.infoMessage("Modelo baixado com sucesso!");
            } 
            catch (Exception e) 
            {
                KMessage.errorMessage( "Erro ao tentar baixar arquivo modelo" );
            }
        }        
    }
    
    private void acaoExportarDados( ActionEvent actionEvent )
    {
        KFileChooser explorador_arquivos = new KFileChooser();
        
        explorador_arquivos.setTipoSelecao( JFileChooser.DIRECTORIES_ONLY );
        
        explorador_arquivos.show();        
        
        if ( explorador_arquivos.getTipoSelecao() == JFileChooser.APPROVE_OPTION ) 
        {
            File selectedFile = explorador_arquivos.getSelecao();
            
            try 
            {
                CsvService.exportDadosByUser( selectedFile.getAbsolutePath(), this.usuario );
                
                KMessage.infoMessage("Dados exportados com sucesso!");
            } 
            catch (Exception e) 
            {
                // KMessage.errorMessage( "Erro ao tentar baixar arquivo modelo" );
                KMessage.errorMessage( e.getMessage() );
            }
        }        
    }
    
    private void acaoImportarDados( ActionEvent actionEvent )
    {
        boolean reescrever_transacoes = KMessage.questionMessage("Deseja reescrever as transações existentes?");
        
        KFileChooser explorador_arquivos = new KFileChooser();
        
        explorador_arquivos.setTipoSelecao( JFileChooser.FILES_ONLY );
        explorador_arquivos.setFiltro( "Arquivo csv", "csv" );
        explorador_arquivos.show();        
        
        if ( explorador_arquivos.getTipoSelecao() == JFileChooser.APPROVE_OPTION ) 
        {
            File selectedFile = explorador_arquivos.getSelecao();
            
            try 
            {
                CsvService.importDados( selectedFile.getAbsolutePath(), reescrever_transacoes, this.usuario );
                
                KMessage.infoMessage("Dados importados com sucesso!");
                
                this.updateFields();
            } 
            catch (Exception e) 
            {
                // KMessage.errorMessage( "Erro ao tentar baixar arquivo modelo" );
                KMessage.errorMessage( e.getMessage() );
            }
        }        
    }
    
    private void atualizaComboDias (ActionEvent actionEvent) 
    {
        input_dia.removeAllItems();
        
        int mes = input_mes.getKey();
        int ano = input_ano.getValue();
        
        input_dia.setOptions( TransacaoService.getDiasParaCombo(mes, ano) );
    }
    
    private void acaoCheckboxDataManual ( ActionEvent actionEvent ) 
    {   
        if ( checkbox_data_manual.isSelected() ) 
        {
            input_ano.setEnabled(true);
            input_mes.setEnabled(true);
            input_dia.setEnabled(true);
        } 
        else 
        {
            input_ano.setEnabled(false);
            input_mes.setEnabled(false);
            input_dia.setEnabled(false);
        }
    }

    private void acaoRealizaTransacao ( ActionEvent actionEvent ) 
    {   
        try 
        {
            boolean is_positivo = radio1.isSelected();
            double  valor       = TransacaoService.validaValorEntrada( textoValor.getText(), is_positivo ) ;
            String  descricao   = textoDescricao.getText();
            
            Timestamp data      = checkbox_data_manual.isSelected() 
            ? Timestamp.valueOf(LocalDateTime.of( input_ano.getValue(), input_mes.getKey(), input_dia.getValue(), 0, 0 ))
            : Timestamp.valueOf( LocalDateTime.now());
            
            Transacao transacao = new Transacao( valor , data , this.usuario, descricao );
            
            transacao.store();
            
            KMessage.infoMessage( UsuarioService.getMensagem( is_positivo, valor ) );
            this.updateFields();
        } 
        catch (Exception e) 
        {
            KMessage.errorMessage( e.getMessage() );
        }
    }

    public void setUsuario(Usuario usuario)
    {
        this.usuario = usuario;
        
        this.updateFields();
    }
    
    private void updateFields()
    {
        this.nome.setText( usuario.getNome() );
        this.nome.setSize( this.nome.getPreferredSize() );
        
        this.valor.setText( "R$ " + String.format( "%.2f", usuario.getSaldo() ) );
        this.valor.setSize( this.valor.getPreferredSize() );
        
        this.textoValor.setText("");
        
        this.updateTable();
    }

    private void updateTable()
    {
        this.updateTabelaDeTransacoes();
        this.updateTabelaDeResumoPorMes();
    }
    
    private void updateTabelaDeTransacoes()
    {
        modelo_tabela_ultimas_transacoes.setRowCount(0);
        
        ArrayList<Transacao> transacoes = new Transacao().getTrancoesByRefUsuario( this.usuario.getId() );

        for( Transacao transacao : transacoes )
        {
            String dataFormatada = transacao.getDataFormatada( "dd/MM/yyyy" );
            double valor         = transacao.getValor();
            int    id            = transacao.getId();
            String descricao     = transacao.getComentario();
            
            this.modelo_tabela_ultimas_transacoes.addRow(new Object[]{ id, dataFormatada , valor, descricao });
        }
    }
    
    private void updateTabelaDeResumoPorMes()
    {
        this.modelo_tabela_transacoes_por_mes.setRowCount(0);
        
        ArrayList<Transacao> gastos_por_meses = new Transacao().getTransacoesByMesUsuario( this.usuario.getId() );

        for (Transacao gasto_no_mes : gastos_por_meses) 
        {
            String mes_ano = gasto_no_mes.getDataFormatada("MM/yyyy");
            double valor   = gasto_no_mes.getValor();
            
            this.modelo_tabela_transacoes_por_mes.addRow(new Object[]{ mes_ano , valor });
        }
    }
    
    private void propriedadesDaTabela() 
    {
        this.addScrollTabela();
        this.chamaTelaEdicaoOnDoubleClickCelula();
        this.bloquearReordenacaoDeColunas();

        tabela_ultimas_transacoes.getColumnModel().getColumn(0).setPreferredWidth(70);
        tabela_ultimas_transacoes.getColumnModel().getColumn(1).setPreferredWidth(120);
        tabela_ultimas_transacoes.getColumnModel().getColumn(2).setPreferredWidth(120);
        tabela_ultimas_transacoes.getColumnModel().getColumn(3).setPreferredWidth(200);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i = 0; i < tabela_ultimas_transacoes.getColumnCount(); i++) {
            tabela_ultimas_transacoes.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        for (int i = 0; i < tabela_transacoes_por_mes.getColumnCount(); i++) {
            tabela_transacoes_por_mes.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }
    
    private void bloquearReordenacaoDeColunas() 
    {
        tabela_ultimas_transacoes.getTableHeader().setReorderingAllowed(false);
        tabela_transacoes_por_mes.getTableHeader().setReorderingAllowed(false);
    }

    private void addScrollTabela() 
    {
        JTableHeader header = tabela_ultimas_transacoes.getTableHeader();
        header.setBackground(cor1);
        header.setForeground(cor2);
        header.setFont(fonte3);
        header.setBorder(new LineBorder(cor2, 0));

        JScrollPane scrollPane2 = new JScrollPane(tabela_ultimas_transacoes);
        scrollPane2.setBounds(10, historico.getY()+25, 680, 175);
        scrollPane2.setBackground(cor2);
        scrollPane2.setBorder(new LineBorder(cor2, 2));
        scrollPane2.getViewport().setBackground(cor1);
        add(scrollPane2);
    }
    
    private void chamaTelaEdicaoOnDoubleClickCelula()
    {
        tabela_ultimas_transacoes.addMouseListener(new MouseAdapter() 
        {
            @Override
            public void mouseClicked(MouseEvent e) 
            {
                int linha  = tabela_ultimas_transacoes.getSelectedRow();
                int coluna = 0; 
                
                if (e.getClickCount() == 2 && linha >= 0) 
                {
                    int        id_transacao = Integer.parseInt( modelo_tabela_ultimas_transacoes.getValueAt(linha, coluna).toString() );
                    Transacao  transacao    = new Transacao().getObjetoById( id_transacao );
                    TelaEdicao tela_edicao  = new TelaEdicao( transacao );
                    
                    tela_edicao.setVisible(true);
                    
                    tela_edicao.addWindowListener(new java.awt.event.WindowAdapter() 
                    {
                        @Override
                        public void windowClosed(java.awt.event.WindowEvent windowEvent) 
                        {
                            updateFields();
                        }
                    });
                }
            }
        });
    }
}
