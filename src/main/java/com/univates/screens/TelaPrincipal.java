package com.univates.screens;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

//import org.w3c.dom.events.MouseEvent;

import com.univates.components.KMessage;
import com.univates.models.Transacao;
import com.univates.models.Usuario;
import com.univates.services.TransacaoService;
import com.univates.services.UsuarioService;

public class TelaPrincipal extends JFrame 
{
    private DefaultTableModel modelo = new DefaultTableModel(new Object[][] {}, new String[] {"Mês", "Valor"}) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private DefaultTableModel modelo2 = new DefaultTableModel(new Object[][] {}, new String[] {"Data", "Valor"}) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private JTable tabela  = new JTable(modelo);
    private JTable tabela2 = new JTable(modelo2);
    
    DateTimeFormatter data_formatada = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    
    private JLabel nome      = new JLabel();
    private JLabel saldo     = new JLabel("Saldo disponível");
    private JLabel valor     = new JLabel();
    private JLabel historico = new JLabel("Histórico");
    private JLabel resumo    = new JLabel("Resumo dos meses");
    
    private JTextField textoValor = new JTextField();
    
    private ButtonGroup grupo1  = new ButtonGroup();
    private JRadioButton radio1 = new JRadioButton("Inserir");
    private JRadioButton radio2 = new JRadioButton("Retirar");
    
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

        botaoConf.addActionListener(this::acaoRealizaTransacao);

        radio2.setSelected(true);
        grupo1.add(radio1);
        grupo1.add(radio2);

        //scroll nas tabelas
        JScrollPane scrollPane2 = new JScrollPane(tabela2);
        scrollPane2.setBounds(10, historico.getY()+25, 760, 165);
        add(scrollPane2);

        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.setBounds(10, resumo.getY()+25, 760, 195);
        add(scrollPane);
        

        //abrir outra tela ao clicar no item
        tabela.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tabela.getSelectedRow();
                int column = tabela.getSelectedColumn();
        
                if (e.getClickCount() == 2 && row >= 0 && column == 0) {
                    TelaEdicao te = new TelaEdicao();
                    te.setVisible(true);
                }
            }
        });
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
        add(tabela2);
        add(tabela);
        add(radio1);
        add(radio2);
    }
    
    private void setPosicaoComponentes() 
    {
        nome      .setBounds(10, 10, 200, 20); 
        saldo     .setBounds(10, nome.getY()+40, 200, 20); 
        valor     .setBounds(10, saldo.getY()+30, 200, 20);
        textoValor.setBounds(10, valor.getY()+30, 200, 20);
        historico .setBounds(10, textoValor.getY()+40, 200, 20);
        tabela2   .setBounds(10, historico.getY()+25, 760, 165); 
        resumo    .setBounds(10, tabela2.getY()+170, 200, 20); 
        tabela    .setBounds(10, resumo.getY()+25, 760, 195);
        botaoConf .setBounds(315, textoValor.getY(), 200, 20);
        radio1    .setBounds(215, 90, 100, 20);
        radio2    .setBounds(215, 110, 100, 20);  
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

    private void acaoRealizaTransacao ( ActionEvent actionEvent ) 
    {   
        try 
        {
            boolean is_positivo = radio1.isSelected();
            double  valor       = TransacaoService.validaValorEntrada( textoValor.getText(), is_positivo ) ;
            
            Transacao transacao = new Transacao( valor , Timestamp.valueOf( LocalDateTime.now()) , this.usuario );
            
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
        modelo2.setRowCount(0);
        
        ArrayList<Transacao> transacoes     = new Transacao().getTrancoesByRefUsuario( this.usuario.getId() );

        for( Transacao transacao : transacoes )
        {
            String dataFormatada = transacao.getData().toLocalDateTime().format(this.data_formatada);
            double valor         = transacao.getValor();
            
            this.modelo2.addRow(new Object[]{ dataFormatada , valor });
        }
    }
    
    private void updateTabelaDeResumoPorMes()
    {
        this.modelo.setRowCount(0);
        
        System.out.println( this.usuario);
        
        ArrayList<Transacao> gastos_por_meses = new Transacao().getTransacoesByMesUsuario( this.usuario.getId() );

        for (Transacao gasto_no_mes : gastos_por_meses) 
        {
            // String mes_ano = gasto_no_mes.getData().getYear() + " - " + gasto_no_mes.getData().getMonth();
            String mes_ano = gasto_no_mes.getData().toLocalDateTime().format(DateTimeFormatter.ofPattern("MM/yyyy"));
            double valor   = gasto_no_mes.getValor();
            
            this.modelo.addRow(new Object[]{ mes_ano , valor });
        }
    }
}
