package com.univates.screens;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.table.DefaultTableModel;

import com.univates.components.KCombo;
import com.univates.components.KMessage;
import com.univates.models.Transacao;
import com.univates.models.Usuario;
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

    private DefaultTableModel modelo_tabela_ultimas_transacoes = new DefaultTableModel(new Object[][] {}, new String[] {"Id", "Data", "Valor"}) 
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
    private JLabel resumo    = new JLabel("Resumo dos meses");
    
    private JTextField textoValor = new JTextField();
    
    private ButtonGroup grupo1  = new ButtonGroup();
    private JRadioButton radio1 = new JRadioButton("Inserir");
    private JRadioButton radio2 = new JRadioButton("Retirar");
    
    private JCheckBox checkbox_data_manual = new JCheckBox("Colocar data manualmente");

    private KCombo<Integer> input_ano  = new KCombo<>( TransacaoService.getAnosParaCombo(), false );
    private KCombo<String>  input_mes  = new KCombo<>( TransacaoService.getMesesParaCombo(), false );
    private KCombo<Integer> input_dia  = new KCombo<>( true );
    
    private JButton botaoConf = new JButton("Confirma");

    private Font fonte1 = new Font("Optima", Font.PLAIN, 18);
    private Font fonte2 = new Font("Optima", Font.BOLD, 23);
    private Font fonte3 = new Font("Optima", Font.BOLD, 18);

    Usuario usuario;

    public TelaPrincipal()
    {
        setSize(800, 610); 
        setTitle("Tela"); 
        setDefaultCloseOperation(EXIT_ON_CLOSE); 
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(null);
        
        this.colocaComponentesNaTela();
        this.setPosicaoComponentes();
        this.setFonteComponentes();
        this.propriedadesDaTabela();

        botaoConf.addActionListener(this::acaoRealizaTransacao);
        checkbox_data_manual.addActionListener(this::acaoCheckboxDataManual);
        input_ano.addActionListener(this::atualizaComboDias);
        input_mes.addActionListener(this::atualizaComboDias);
        
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
        add(tabela_ultimas_transacoes);
        add(tabela_transacoes_por_mes);
        add(radio1);
        add(radio2);
        add(checkbox_data_manual);
        add(input_ano);
        add(input_mes);
        add(input_dia);
    }
    
    private void setPosicaoComponentes() 
    {
        nome                     .setBounds(10, 10, 200, 20); 
        saldo                    .setBounds(10, nome.getY()+40, 200, 20); 
        valor                    .setBounds(10, saldo.getY()+30, 200, 20);
        textoValor               .setBounds(10, valor.getY()+30, 200, 20);
        historico                .setBounds(10, textoValor.getY()+40, 200, 20);
        tabela_ultimas_transacoes.setBounds(10, historico.getY()+25, 760, 165); 
        resumo                   .setBounds(10, tabela_ultimas_transacoes.getY()+170, 200, 20); 
        tabela_transacoes_por_mes.setBounds(10, resumo.getY()+25, 760, 195);
        botaoConf                .setBounds(315, textoValor.getY(), 200, 20);
        radio1                   .setBounds(215, 90, 100, 20);
        radio2                   .setBounds(215, 110, 100, 20);
        checkbox_data_manual     .setBounds(215, 130, 200, 20);
        input_ano                .setBounds(215, 150, 100, 20);
        input_mes                .setBounds(315, 150, 100, 20);
        input_dia                .setBounds(415, 150, 100, 20);
    }

    private void setFonteComponentes() 
    {
        nome      .setFont(fonte2);
        saldo     .setFont(fonte3);
        textoValor.setFont(fonte1);
        valor     .setFont(fonte1);
        historico .setFont(fonte1);
        resumo    .setFont(fonte1);
        botaoConf .setFont(fonte1);
        radio1    .setFont(fonte1);
        radio2    .setFont(fonte1);
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
            
            Timestamp data      = checkbox_data_manual.isSelected() 
            ? Timestamp.valueOf(LocalDateTime.of( input_ano.getValue(), input_mes.getKey(), input_dia.getValue(), 0, 0 ))
            : Timestamp.valueOf( LocalDateTime.now());
            
            Transacao transacao = new Transacao( valor , data , this.usuario );
            
            transacao.store();
            
            KMessage.infoMessage( UsuarioService.getMensagem(is_positivo, valor ) );
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
        
        this.valor.setText( "R$ " + usuario.getSaldo() );
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
            
            this.modelo_tabela_ultimas_transacoes.addRow(new Object[]{ id, dataFormatada , valor });
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

        tabela_ultimas_transacoes.getColumnModel().getColumn(0).setPreferredWidth(50);
        tabela_ultimas_transacoes.getColumnModel().getColumn(1).setPreferredWidth(120);
        tabela_ultimas_transacoes.getColumnModel().getColumn(2).setPreferredWidth(280);
    }
    
    private void bloquearReordenacaoDeColunas() 
    {
        tabela_ultimas_transacoes.getTableHeader().setReorderingAllowed(false);
        tabela_transacoes_por_mes.getTableHeader().setReorderingAllowed(false);
    }

    private void addScrollTabela() 
    {
        JScrollPane scrollPane = new JScrollPane(tabela_transacoes_por_mes);
        scrollPane.setBounds(10, resumo.getY()+25, 760, 195);
        add(scrollPane);
        
        JScrollPane scrollPane2 = new JScrollPane(tabela_ultimas_transacoes);
        scrollPane2.setBounds(10, historico.getY()+25, 760, 165);
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
