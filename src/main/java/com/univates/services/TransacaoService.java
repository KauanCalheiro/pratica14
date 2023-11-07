package com.univates.services;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.YearMonth;

public class TransacaoService 
{
    public static int getUltimoDiaDoMes( Timestamp data ) 
    {
        LocalDateTime data_local        = data.toLocalDateTime();
        YearMonth     ano_mes           = YearMonth.of(data_local.getYear(), data_local.getMonth());
        int           ultimo_dia_do_mes = ano_mes.lengthOfMonth();
        
        return ultimo_dia_do_mes;
    }

    public static double validaValorEntrada(String valor_entrada, boolean is_positivo) 
    {
        if ( valor_entrada.trim().isEmpty() ) 
        {
            throw new IllegalArgumentException("Valor não pode ser vazio");    
        }
        
        double valor;
        
        try 
        {
            valor = Double.parseDouble( valor_entrada );
        } 
        catch (Exception e) 
        {
            throw new IllegalArgumentException("A entrada deve ser numérica");
        }
        
        if ( is_positivo && valor < 0) 
        {
            throw new IllegalArgumentException("Valor não pode ser negativo");
        }
        
        valor = !is_positivo && valor > 0 ? valor * -1 : valor;
        
        return valor;
    }
}
