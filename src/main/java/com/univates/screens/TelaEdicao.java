package com.univates.screens;
import java.awt.event.ActionEvent;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

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
    
    private Transacao transacao;
    
    public TelaEdicao( Transacao transacao )
    {
        System.out.println(transacao.toString());

        setSize(800, 500); 
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
        label_ano    .setSize( label_ano.getPreferredSize() );
        label_valor  .setSize( label_valor.getPreferredSize() );
        input_valor  .setSize( 500, 20 );
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
        label_valor  .setBounds(10, 10, 200, 20);
        input_valor  .setBounds(10, 30, 200, 20);
        label_ano    .setBounds(10, 60, 200, 20);
        input_ano    .setBounds(10, 80, 200, 20);
        label_mes    .setBounds(10, 110, 200, 20);
        input_mes    .setBounds(10, 130, 200, 20);
        label_dia    .setBounds(10, 160, 200, 20);
        input_dia    .setBounds(10, 180, 200, 20);
        radio1       .setBounds(10, 210, 100, 20);
        radio2       .setBounds(10, 230, 100, 20);
        botao_editar .setBounds(10, 260, 200, 20);
        botao_excluir.setBounds(10, 290, 200, 20);
        botao_voltar .setBounds(10, 320, 200, 20);
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
