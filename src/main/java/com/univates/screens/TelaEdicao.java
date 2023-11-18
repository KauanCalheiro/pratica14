package com.univates.screens;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import com.univates.components.KCombo;
import com.univates.components.KMessage;
import com.univates.models.Transacao;
import com.univates.services.TransacaoService;

public class TelaEdicao extends JDialog 
{
    private JButton botao_voltar   = new JButton("Fechar");
    private JButton botao_editar  = new JButton("Editar");
    private JButton botao_excluir = new JButton("Excluir");
    
    private JTextField input_valor = new JTextField();
    
    private KCombo<Integer> input_ano  = new KCombo<>( TransacaoService.getAnosParaCombo(), false );
    private KCombo<String>  input_mes  = new KCombo<>( TransacaoService.getMesesParaCombo(), false );
    private KCombo<Integer> input_dia  = new KCombo<>( true );
    
    private JLabel label_valor = new JLabel("Valor");
    private JLabel label_ano   = new JLabel("Ano");
    private JLabel label_mes   = new JLabel("Mês");
    private JLabel label_dia   = new JLabel("Dia");
    
    private ButtonGroup  grupo1 = new ButtonGroup();
    private JRadioButton radio1 = new JRadioButton("Inserir");
    private JRadioButton radio2 = new JRadioButton("Retirar");

    private Font fonte1 = new Font("Serif", Font.PLAIN, 18);
    private Font fonte2 = new Font("Serif", Font.BOLD, 18);

    private Color cor1 = new Color(255, 245, 232); //bege claro
    private Color cor2 = new Color(109, 73, 37); //marrom
    
    private Transacao transacao;
    
    public TelaEdicao( Transacao transacao )
    {
        System.out.println(transacao.toString());

        setSize(450, 300); 
        getContentPane().setBackground(cor1);
        setTitle("Tela de Edição"); 
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); 
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(null);
        setModal(true);
        
        this.transacao = transacao;
        
        grupo1.add(radio1);
        grupo1.add(radio2);
        
        this.colocaComponentesNaTela();
        this.setPosicaoComponentes();
        this.updateComponents();
        this.setTamanhoComponentes();
        this.setCorCoponentes();
        this.setFonteComponentes();
        
        botao_voltar.addActionListener(this::acaoVoltar);
        botao_editar.addActionListener(this::acaoEditar);
        botao_excluir.addActionListener(this::acaoExcluir);
        
        input_ano.addActionListener(this::atualizaComboDias);
        input_mes.addActionListener(this::atualizaComboDias);
    }
    
    private void setTamanhoComponentes() 
    {
        botao_voltar .setSize( botao_voltar.getPreferredSize() );
        botao_editar .setSize( botao_editar.getPreferredSize() );
        botao_excluir.setSize( botao_excluir.getPreferredSize() );
    }

    private void updateComponents() 
    {
        input_valor.setText( String.valueOf( transacao.getValor() ) );
        input_ano.setSelectedItem( transacao.getAno() );
        input_mes.setSelectedIndex( transacao.getMes()-1 );
        atualizaComboDias(null);
        input_dia.setSelectedItem( transacao.getDia() );

        if ( transacao.getValor() > 0 ) 
        {
            radio1.setSelected(true);
        } 
        else 
        {
            radio2.setSelected(true);
        }
    }

    private void colocaComponentesNaTela() 
    {
        add(label_ano);
        add(label_valor);
        add(label_mes);
        add(label_dia);
        add(input_ano);
        add(input_mes);
        add(input_dia);
        add(input_valor);
        add(radio1);
        add(radio2);
        add(botao_editar);
        add(botao_excluir);
        add(botao_voltar); 
    }
    
    private void setPosicaoComponentes() 
    {
        label_valor  .setBounds(10, 10, 200, 25);
        input_valor  .setBounds(10, 35, 200, 25);
        label_ano    .setBounds(10, 60, 200, 25);
        input_ano    .setBounds(10, 85, 200, 25);
        label_mes    .setBounds(10, 110, 200, 25);
        input_mes    .setBounds(10, 135, 200, 25);
        label_dia    .setBounds(10, 160, 200, 25);
        input_dia    .setBounds(10, 185, 200, 25);
        radio1       .setBounds(320, 35, 90, 25);
        radio2       .setBounds(230, 35, 90, 25);
        botao_editar .setBounds(320, 110, 200, 25);
        botao_excluir.setBounds(320, 145, 200, 25);
        botao_voltar .setBounds(320, 185, 200, 25);
    }

    private void setFonteComponentes() 
    {
        label_valor  .setFont(fonte2);
        input_valor  .setFont(fonte1);
        label_ano    .setFont(fonte2);
        input_ano    .setFont(fonte1);
        label_mes    .setFont(fonte2);
        input_mes    .setFont(fonte1);
        label_dia    .setFont(fonte2);
        input_dia    .setFont(fonte1);
        radio1       .setFont(fonte1);
        radio2       .setFont(fonte1);
        botao_editar .setFont(fonte1);
        botao_excluir.setFont(fonte1);
        botao_voltar .setFont(fonte1);
    }
    
    private void setCorCoponentes() 
    {
        label_valor  .setForeground(cor2);
        input_valor  .setForeground(cor2);
        input_valor  .setBackground(cor1);
        input_valor  .setBorder(new LineBorder(cor2, 2));
        label_ano    .setForeground(cor2);
        input_ano    .setForeground(cor2);
        input_ano    .setBackground(cor1);
        input_ano    .setBorder(new LineBorder(cor2, 2));
        label_mes    .setForeground(cor2);
        input_mes    .setForeground(cor2);
        input_mes    .setBackground(cor1);
        input_mes    .setBorder(new LineBorder(cor2, 2));
        label_dia    .setForeground(cor2);
        input_dia    .setForeground(cor2);
        input_dia    .setBackground(cor1);
        input_dia    .setBorder(new LineBorder(cor2, 2));
        radio1       .setForeground(cor2);
        radio1       .setBackground(cor1);
        radio2       .setForeground(cor2);
        radio2       .setBackground(cor1);
        botao_editar .setForeground(cor2);
        botao_editar .setBackground(cor1);
        botao_editar .setBorder(new LineBorder(cor2, 2));
        botao_excluir.setForeground(cor2);
        botao_excluir.setBackground(cor1);
        botao_excluir.setBorder(new LineBorder(cor2, 2));
        botao_voltar .setForeground(cor2);
        botao_voltar .setBackground(cor1);
        botao_voltar .setBorder(new LineBorder(cor2, 2));
    }
    
    private void acaoVoltar (ActionEvent actionEvent) 
    {
        this.dispose();
    }
    
    private void acaoEditar (ActionEvent actionEvent) 
    {
        try 
        {
            boolean is_positivo = radio1.isSelected();
            int     ano         = input_ano.getValue();
            int     mes         = input_mes.getKey();
            int     dia         = input_dia.getValue();
            double  valor       = TransacaoService.validaValorEntrada( input_valor.getText(), is_positivo );
            
            boolean is_edicao_confirmada = KMessage.questionMessage("Deseja realmente editar o registro dessa transação?");
        
            if (!is_edicao_confirmada) 
            {
                this.dispose();
            }
            
            Timestamp data = Timestamp.valueOf( LocalDateTime.of(ano, mes, dia, 0, 0) );
            
            transacao.setData(data);
            transacao.setValor(valor);
            transacao.store();
            this.dispose();
        } 
        catch (Exception e) 
        {
            KMessage.errorMessage(e.getMessage());
        }
        
    }
    
    private void acaoExcluir (ActionEvent actionEvent) 
    {
        boolean is_exclusao_confirmada = KMessage.questionMessage("Deseja realmente excluir o registro dessa transação?");
        
        if (is_exclusao_confirmada) 
        {
            transacao.delete(transacao.getId());
            this.dispose();
        }
    }
    
    private void atualizaComboDias (ActionEvent actionEvent) 
    {
        input_dia.removeAllItems();
        
        int mes = input_mes.getKey();
        int ano = input_ano.getValue();
        
        input_dia.setOptions( TransacaoService.getDiasParaCombo(mes, ano) );
    }
}
