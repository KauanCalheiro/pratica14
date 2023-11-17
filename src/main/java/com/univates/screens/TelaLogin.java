package com.univates.screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import com.univates.components.KMessage;
import com.univates.models.Usuario;
import com.univates.services.UsuarioService;

public class TelaLogin extends JFrame
{
    private int tentativas = 0;
    
    TelaCadastro  tela_cadastro  = new TelaCadastro();
    TelaPrincipal tela_principal = new TelaPrincipal();

    private JLabel login = new JLabel("Login");
    private JLabel cpf   = new JLabel("CPF: ");
    private JLabel senha = new JLabel("Senha: ");

    private JTextField     textoCpf   = new JTextField();
    private JPasswordField textoSenha = new JPasswordField();

    private JButton botaoLog = new JButton("Login");
    private JButton botaoVoltar = new JButton("Cadastrar");

    private Font fonte1 = new Font("Serif", Font.PLAIN, 18);
    private Font fonte2 = new Font("Serif", Font.BOLD, 27);

    private Color cor1 = new Color(255, 245, 232); //bege claro
    private Color cor2 = new Color(109, 73, 37); //marrom
    private Color cor3 = new Color(135, 102, 69); //bege escuro

    public TelaLogin()
    {
        setSize(800, 500); 
        getContentPane().setBackground(cor1);
        setTitle("Tela de Login"); 
        setDefaultCloseOperation(EXIT_ON_CLOSE); 
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(null);

        this.colocaComponentesNaTela();
        this.setPosicaoComponentes();
        this.setFonteComponentes();
        this.setTamanhoComponentes();
        this.setCorComponentes();
        
        textoCpf.setText(tela_cadastro.getTextoCpf().getText());
        botaoLog.addActionListener(this::acaoLogin);
        botaoVoltar.addActionListener(this::acaoVoltar);
        
        textoCpf.setText("08019505113");
        textoSenha.setText("Kauan123");
    }
    
    private void colocaComponentesNaTela()
    {
        add(login);
        add(cpf);
        add(senha);
        add(textoCpf);
        add(textoSenha);
        add(botaoLog);
        add(botaoVoltar);
    }
    
    private void setPosicaoComponentes()
    {
        login      .setBounds(315, 40, 200, 20); 
        cpf        .setBounds(250, login.getY()+40, 300, 20); 
        senha      .setBounds(cpf.getX(), cpf.getY()+25, 300, 20); 
        textoCpf   .setBounds(315, login.getY()+45, 200, 20);
        textoSenha .setBounds(textoCpf.getX(), cpf.getY()+30, 200, 20);
        botaoLog   .setBounds(textoSenha.getX(), cpf.getY()+60, 200, 20);
        botaoVoltar.setBounds(555, 300, 200, 20);
    }
    
    private void setFonteComponentes()
    {
        login      .setFont(fonte2);
        cpf        .setFont(fonte1);
        senha      .setFont(fonte1);
        textoCpf   .setFont(fonte1);
        textoSenha .setFont(fonte1);
        botaoLog   .setFont(fonte1);
        botaoVoltar.setFont(fonte1);
    }

    private void setTamanhoComponentes() 
    {
        login       .setSize( login.getPreferredSize() );
        cpf         .setSize( cpf.getPreferredSize() );
        senha       .setSize( senha.getPreferredSize() );
        textoCpf    .setSize( 200, 20 );
        textoSenha  .setSize( 200, 20 );
        botaoLog    .setSize( botaoLog.getPreferredSize() );
        botaoVoltar .setSize( botaoVoltar.getPreferredSize() );
    }

    private void setCorComponentes() 
    {
        login       .setForeground(cor2);
        cpf         .setForeground(cor3);
        senha       .setForeground(cor3);
        textoCpf    .setForeground(cor2);
        textoCpf    .setBackground(cor1);
        textoCpf    .setBorder(new LineBorder(cor3, 2));
        textoSenha  .setForeground(cor2);
        textoSenha  .setBackground(cor1);
        textoSenha  .setBorder(new LineBorder(cor3, 2));
        botaoLog    .setForeground(cor1);
        botaoLog    .setBackground(cor2);
        botaoLog    .setBorder(new LineBorder(cor2, 2));
        botaoVoltar .setForeground(cor1);
        botaoVoltar .setBackground(cor2);
        botaoVoltar .setBorder(new LineBorder(cor2, 2));
    }


    private void acaoLogin (ActionEvent actionEvent) 
    {
        this.encerraAplicacaoSeTentativasExcedidas();
        
        String cpf   = textoCpf.getText();
        String senha = String.valueOf(textoSenha.getPassword());
        
        try 
        {
            if( UsuarioService.login( cpf, senha ) )
            {
                this.dispose();
                tela_principal.setUsuario( Usuario.getUsuarioByCpf( cpf ) );
                tela_principal.setVisible(true);
            } 
            else 
            {
                this.tentativas++;
                throw new Exception ("Usuario não encontrado ou senha inválida") ;
            }
        } 
        catch (Exception e) 
        {
            KMessage.errorMessage( e.getMessage() );
        }
    }

    private void acaoVoltar (ActionEvent actionEvent) 
    {
        TelaCadastro tc = new TelaCadastro();
        
        this.dispose();
        
        tc.setVisible(true);
    }
    
    private void encerraAplicacaoSeTentativasExcedidas() 
    {
        if (this.tentativas >= 3) 
        {
            KMessage.errorMessage( "Numero de tentativas excedidas" );
            System.exit(0);
        }
    }
}
