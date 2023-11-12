package com.univates.services;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;

import com.univates.components.KComboOption;

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
    
    public static ArrayList<KComboOption<String>> getMesesParaCombo() 
    {
        ArrayList<KComboOption<String>> meses = new ArrayList<KComboOption<String>>();
        
        meses.add( new KComboOption<>("Janeiro", 1) );
        meses.add( new KComboOption<>("Fevereiro", 2) );
        meses.add( new KComboOption<>("Março", 3) );
        meses.add( new KComboOption<>("Abril", 4) );
        meses.add( new KComboOption<>("Maio", 5) );
        meses.add( new KComboOption<>("Junho", 6) );
        meses.add( new KComboOption<>("Julho", 7) );
        meses.add( new KComboOption<>("Agosto", 8) );
        meses.add( new KComboOption<>("Setembro", 9) );
        meses.add( new KComboOption<>("Outubro", 10) );
        meses.add( new KComboOption<>("Novembro", 11) );
        meses.add( new KComboOption<>("Dezembro", 12) );
        
        return meses;
    }
    
    public static ArrayList<KComboOption<Integer>> getAnosParaCombo() 
    {
        ArrayList<KComboOption<Integer>> anos = new ArrayList<KComboOption<Integer>>();
        
        int ano_atual = LocalDateTime.now().getYear();
        
        for (int i = ano_atual; i >= ano_atual - 10; i--) 
        {
            anos.add( new KComboOption<>( i, i ) );
        }
        
        return anos;
    }
    
    public static ArrayList<KComboOption<Integer>> getDiasParaCombo( int mes, int ano ) 
    {
        ArrayList<KComboOption<Integer>> dias = new ArrayList<KComboOption<Integer>>();
        
        int ultimo_dia_do_mes = YearMonth.of(ano, mes).lengthOfMonth();
        
        for (int i = 1; i <= ultimo_dia_do_mes; i++) 
        {
            dias.add( new KComboOption<>( i, i ) );
        }
        
        return dias;
    }
    

}
