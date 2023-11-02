package com.univates.models;

import java.util.ArrayList;

import com.univates.services.UsuarioService;

public class Usuario extends Model<Usuario>
{
    private int    id;
    private String nome;
    private String cpf;
    private String senha;
    private double salario;
    
    
    public Usuario(String nome, String cpf, String senha, double salario)
    {
        super();
        
        if ( Usuario.getUsuarioByCpf(cpf) != null) 
        {
            throw new IllegalArgumentException("CPF já cadastrado");
        }

        this.id      = super.getNextId();
        this.nome    = nome;
        this.cpf     = cpf;
        this.senha   = UsuarioService.isSenhaAlredyHashed(senha) ? senha : UsuarioService.hashSenha(senha);
        this.salario = salario;
    }
    
    public Usuario()
    {}
    
    public int getId()
    {
        return this.id;
    }
    
    public String getNome()
    {
        return this.nome;
    }
    
    public String getCpf()
    {
        return this.cpf;
    }
    
    public String getSenha()
    {
        return this.senha;
    }
    
    public double getSalario()
    {
        return this.salario;
    }
    
    public void setId( int id ) 
    {
        this.id = id;
    }
    
    public void setNome( String nome )
    {
        this.nome = nome;
    }
    
    public void setCpf( String cpf )
    {
        this.cpf = cpf;
    }
    
    public void setSenha( String senha )
    {
        this.senha = UsuarioService.isSenhaAlredyHashed(senha) ? senha : UsuarioService.hashSenha(senha);
    }
    
    public void setSalario( double salario )
    {
        this.salario = salario;
    }
       
    public static Usuario getUsuarioByCpf( String cpf )
    {
        ArrayList<Filtro> filtros = new ArrayList<Filtro>();
        
        filtros.add( new Filtro("cpf", " = ", cpf) );
        
        Usuario usuario = new Usuario().getObjecto( filtros );
        
        return usuario;
    }
    
    public double getSaldo()
    {
        Transacao transacao = new Transacao();
        ArrayList<Transacao> trasacoes = transacao.getTrancoesByRefUsuario( this.id );
        
        double saldo = 0;
        
        for( Transacao t : trasacoes )
        {
            saldo += t.getValor();
        }
        
        return saldo;
    }
    
    public void store()
    {
        UsuarioService.validaUsuario( this );
        
        super.store( this.id );
    }
    
    @Override
    public String toString() 
    {
        return "Usuario [id=" + id + ", nome=" + nome + ", cpf=" + cpf + ", senha=" + senha + ", salario=" + salario + "]";
    }
}
