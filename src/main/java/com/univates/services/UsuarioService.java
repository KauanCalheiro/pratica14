package com.univates.services;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import com.univates.models.Usuario;

public class UsuarioService 
{
    
    public static String hashSenha( String senha )
    {
        try 
        {
            MessageDigest md        = MessageDigest.getInstance( "SHA-256" );
            byte[]        hash_bytes = md.digest( senha.getBytes() );
            
            return Base64.getEncoder().encodeToString(hash_bytes);
        } 
        catch (NoSuchAlgorithmException e) 
        {
            e.printStackTrace();
            return null;
        }
    }
    
    public static boolean validarSenha( String hash_senha, String entrada_senha )
    {
        return hashSenha(entrada_senha).equals(hash_senha);
    }
    
    public static boolean isSenhaAlredyHashed(String senha)
    {
        return senha.length() == 44;
    }
    
    public static void validaUsuario( Usuario usuario )
    {
        if( usuario.getNome().trim().length() < 3 )
        {
            throw new IllegalArgumentException("Nome deve ter no mínimo 3 caracteres");
        }
        
        UsuarioService.validarCPF(usuario.getCpf());
        
        if( usuario.getSenha().length() < 8 )
        {
            throw new IllegalArgumentException("Senha deve ter no mínimo 8 caracteres");
        }
        
        if ( usuario.getSalario() < 0 )
        {
            throw new IllegalArgumentException("Salário deve ser maior que 0");
        }
    }
    
    public static String getMensagem( boolean is_positivo, double valor )
    {
        if( is_positivo )
        {
            return "R$ " + valor + " foram adicionados!";       
        }
        else
        {
            return "R$ " + (valor < 0 ? valor : -valor) + " foram retirados!";
        }
    }
    
    public static boolean login( String cpf, String senha )
    {
        Usuario usuario = Usuario.getUsuarioByCpf(cpf);
        if ( usuario != null ) 
        {
            return UsuarioService.validarSenha(usuario.getSenha(), senha);
        }
        else
        {
            throw new IllegalArgumentException("Usuário não encontrado");
        }
    }
    
    private static void validarCPF(String cpf) 
    {
        cpf = cpf.replaceAll("[^0-9]", "");

        if (cpf.length() != 11) 
        {
            throw new IllegalArgumentException("CPF inválido. CPF deve ter 11 caracteres");
        }

        if (cpf.matches("(\\d)\\1{10}")) 
        {
            throw new IllegalArgumentException("CPF inválido. Todos os dígitos são iguais");
        }

        int digito1 = calcularDigito(cpf, 10, 0);

        if (digito1 != Character.getNumericValue(cpf.charAt(9))) 
        {
            throw new IllegalArgumentException("CPF inválido. Dígito verificador 1 não confere");
        }

        int digito2 = calcularDigito(cpf, 11, digito1);

        if(digito2 == Character.getNumericValue(cpf.charAt(10)))
        {
            throw new IllegalArgumentException("CPF inválido. Dígito verificador 2 não confere");
        }
    }

    private static int calcularDigito(String cpf, int pesoInicial, int digitoAnterior) 
    {
        int soma = 0;
        
        for (int i = 0; i < 9; i++) 
        {
            soma += Character.getNumericValue(cpf.charAt(i)) * (pesoInicial - i);
        }

        int resto  = soma % 11;
        int digito = 11 - resto;

        if (resto < 2) 
        {
            digito = 0;
        }

        return digito;
    }

}
