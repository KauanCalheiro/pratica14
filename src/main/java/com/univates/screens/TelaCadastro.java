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

public class TelaCadastro extends JFrame
{
    private JLabel cadastro = new JLabel("Cadastro");
    private JLabel aux      = new JLabel("Já possui cadastro? Faça Login!");
    private JLabel nome     = new JLabel("Nome: ");
    private JLabel cpf      = new JLabel("CPF: ");
    private JLabel salario  = new JLabel("Valor Inicial: ");
    private JLabel senha    = new JLabel("Senha: ");
    private JLabel senhaConf    = new JLabel("Confirme sua senha: ");

    private JTextField     textoNome    = new JTextField();
    private JTextField     textoCpf     = new JTextField();
    private JTextField     textoSalario = new JTextField();
    private JPasswordField textoSenha   = new JPasswordField();
    private JPasswordField textoSenhaConf   = new JPasswordField();

    private JButton botaoCad = new JButton("Cadastrar"); 
    private JButton botaoLog = new JButton("Login");

    private Font fonte1 = new Font("Arial", Font.PLAIN, 18);
    private Font fonte2 = new Font("Arial", Font.BOLD, 27);
    private Font fonte3 = new Font("Arial", Font.BOLD, 18);

    private Color cor1 = new Color(250, 250, 250); //branco
    private Color cor2 = new Color(110, 143,143); //verde 
    private Color cor3 = new Color(102, 102, 102); //cinza

    public TelaCadastro()
    {
        setSize(600, 450); 
        getContentPane().setBackground(cor1);
        setTitle("Tela de Cadastro"); 
        setDefaultCloseOperation(EXIT_ON_CLOSE); 
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(null);
        
        this.colocaComponentesNaTela();
        this.setPosicaoComponentes();
        this.setFonteComponentes();
        this.setTamanhoComponentes(); 
        this.setCorComponentes();       

        botaoCad.addActionListener(this::cadastraNovoUsuario);   
        botaoLog.addActionListener(this::chamaTelaLogin);  
    }
    
    private void colocaComponentesNaTela() 
    {
        add(cadastro);
        add(aux);
        add(nome);
        add(cpf);
        add(salario);
        add(senha);
        add(textoNome);
        add(textoCpf);
        add(textoSalario);
        add(textoSenha);
        add(botaoCad);
        add(botaoLog);
        add(senhaConf);
        add(textoSenhaConf);
    }
    
    private void setPosicaoComponentes() 
    {
        cadastro      .setBounds(215, 30, 200, 20); 
        nome          .setBounds(155, cadastro.getY()+30, 300, 20); 
        textoNome     .setBounds(215, cadastro.getY()+30, 200, 20);
        cpf           .setBounds(165, nome.getY()+30, 300, 20); 
        textoCpf      .setBounds(textoNome.getX(), textoNome.getY()+30, 200, 20);
        salario       .setBounds(110, textoCpf.getY()+30, 300, 20); 
        textoSalario  .setBounds(textoCpf.getX(), textoCpf.getY()+30, 200, 20);
        senha         .setBounds(150, textoSalario.getY()+30, 300, 20); 
        textoSenha    .setBounds(textoSalario.getX(), textoSalario.getY()+30, 200, 20);
        senhaConf     .setBounds(38, textoSenha.getY()+30, 200, 20);
        textoSenhaConf.setBounds(textoSenha.getX(), textoSenha.getY()+30, 200, 20);
        botaoCad      .setBounds(textoSenhaConf.getX(), textoSenhaConf.getY()+30, 200, 20);
        aux           .setBounds(150, botaoCad.getY()+50, 300, 20); 
        botaoLog      .setBounds(botaoCad.getX(), aux.getY()+30, 200, 20);
    }

    private void setFonteComponentes() 
    {
        cadastro      .setFont(fonte2);
        aux           .setFont(fonte3);
        nome          .setFont(fonte1);
        cpf           .setFont(fonte1);
        salario       .setFont(fonte1);
        senha         .setFont(fonte1);
        textoNome     .setFont(fonte1);
        textoCpf      .setFont(fonte1);
        textoSalario  .setFont(fonte1);
        textoSenha    .setFont(fonte1);
        botaoCad      .setFont(fonte1);
        botaoLog      .setFont(fonte1);
        senhaConf     .setFont(fonte1);
        textoSenhaConf.setFont(fonte1);
    }
    
    private void setTamanhoComponentes() 
    {
        cadastro    .setSize( cadastro.getPreferredSize() );
        aux         .setSize( aux.getPreferredSize() );
        nome        .setSize( nome.getPreferredSize() );
        cpf         .setSize( cpf.getPreferredSize() );
        salario     .setSize( salario.getPreferredSize() );
        senha       .setSize( senha.getPreferredSize() );
        textoNome   .setSize( 200, 20 );
        textoCpf    .setSize( 200, 20 );
        textoSalario.setSize( 200, 20 );
        textoSenha  .setSize( 200, 20 );
        botaoCad    .setSize( botaoCad.getPreferredSize() );
        botaoLog    .setSize( botaoLog.getPreferredSize() );
    }

    private void setCorComponentes() 
    {
        cadastro      .setForeground(cor2);
        aux           .setForeground(cor2);
        nome          .setForeground(cor3);
        cpf           .setForeground(cor3);
        salario       .setForeground(cor3);
        senha         .setForeground(cor3);
        senhaConf     .setForeground(cor3);
        textoNome     .setForeground(cor2);
        textoNome     .setBackground(cor1);
        textoNome     .setBorder(new LineBorder(cor3, 2));
        textoCpf      .setForeground(cor2);
        textoCpf      .setBackground(cor1);
        textoCpf      .setBorder(new LineBorder(cor3, 2));
        textoSalario  .setForeground(cor2);
        textoSalario  .setBackground(cor1);
        textoSalario  .setBorder(new LineBorder(cor3, 2));
        textoSenha    .setForeground(cor2);
        textoSenha    .setBackground(cor1);
        textoSenha    .setBorder(new LineBorder(cor3, 2));
        textoSenhaConf.setForeground(cor2);
        textoSenhaConf.setBackground(cor1);
        textoSenhaConf.setBorder(new LineBorder(cor3, 2));
        botaoCad      .setForeground(cor1);
        botaoCad      .setBackground(cor2);
        botaoCad      .setBorder(new LineBorder(cor2, 2));
        botaoLog      .setForeground(cor1);
        botaoLog      .setBackground(cor2);
        botaoLog      .setBorder(new LineBorder(cor2, 2));
    }

    public JTextField getTextoCpf() 
    {
        return textoCpf;
    }

    public void setTextoCpf( JTextField textoCpf ) 
    {
        this.textoCpf = textoCpf;
    }    

    private void cadastraNovoUsuario ( ActionEvent actionEvent ) 
    {
        try 
        {
            String nome = textoNome.getText();
            String cpf = textoCpf.getText().trim().replaceAll("[^0-9]", "");;
            String senha = String.valueOf(textoSenha.getPassword());
            String senhaConfirma = String.valueOf(textoSenhaConf.getPassword());
            String salarioText = textoSalario.getText();
    
            if (nome.isEmpty() || cpf.isEmpty() || senha.isEmpty() || salarioText.isEmpty() || senhaConfirma.isEmpty()) 
            {
                KMessage.errorMessage("Preencha todos os campos antes de cadastrar.");
                return;
            }

            if (!senha.equals(senhaConfirma)) {
            KMessage.errorMessage("Senhas diferentes. Tente novamente.");
            }
            
            Double  salario = Double.parseDouble(salarioText);
            
            Usuario usuario = new Usuario(nome, cpf, senha, salario);
            
            UsuarioService.validaUsuario(usuario);
            
            usuario.store();
            
            KMessage.infoMessage("Usuário cadastrado com sucesso!");
        } 
        catch (NumberFormatException e) 
        {
            KMessage.errorMessage("Valor do salário inválido. Por favor, insira um valor numérico.");
        } 
        catch (Exception e) 
        {
            KMessage.errorMessage(e.getMessage());
        }
    }

    

    private void chamaTelaLogin ( ActionEvent actionEvent ) 
    {
        TelaLogin tela_login = new TelaLogin();
        
        this.dispose();
        
        tela_login.setVisible(true);
    }
}
