package com.univates.models;

import java.time.LocalDateTime;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.univates.services.TransacaoService;

public class Transacao extends Model<Transacao>
{
    private int       id;
    private double    valor;
    private Timestamp data;
    private int       ref_usuario;
    
    
    public Transacao(double valor, Timestamp data, Usuario usuario)
    {
        super();
        this.id    = super.getNextId();
        this.valor = valor;
        this.data  = data;
        this.ref_usuario = usuario.getId();
    }
    
    public Transacao(double valor, Timestamp data, int ref_usuario)
    {
        super();
        this.id    = super.getNextId();
        this.valor = valor;
        this.data  = data;
        this.ref_usuario = ref_usuario;
    }
    
    public Transacao()
    {}
    
    public ArrayList<Transacao> getTrancoesByRefUsuario( int ref_usuario )
    {
        ArrayList<Filtro> filtros = new ArrayList<Filtro>();
        
        filtros.add( new Filtro("ref_usuario", "=", String.valueOf(ref_usuario)) );
        
        return this.getObjetos(filtros, "data DESC");
    }
    
    public ArrayList<Transacao> getTransacoesByMesUsuario( int ref_usuario ) 
    {
        Timestamp            data_referencia      = Timestamp.valueOf(LocalDateTime.now().withDayOfMonth(1));
        ArrayList<Transacao> lista_gastos_por_mes = new ArrayList<Transacao>();
        
        for (int i = 1; i <= 12; i++) 
        {
            ArrayList<Filtro> filtros = new ArrayList<Filtro>();
            
            String data_inicial = data_referencia.toLocalDateTime().toLocalDate().toString();
            String data_final   = Timestamp.valueOf(data_referencia.toLocalDateTime().withDayOfMonth(TransacaoService.getUltimoDiaDoMes(data_referencia))).toLocalDateTime().toLocalDate().toString();

            
            filtros.add( new Filtro("ref_usuario", "=", String.valueOf(ref_usuario)) );
            filtros.add( new Filtro( "data", ">=", data_inicial ) );
            filtros.add( new Filtro( "data", "<=", data_final ) );
            
            ArrayList<Transacao> transacoes_no_mes = this.getObjetos(filtros);
            
            double gasto_no_mes = 0;
            
            for (Transacao transacao : transacoes_no_mes) 
            {
                gasto_no_mes += transacao.getValor();    
            }
            
            lista_gastos_por_mes.add( new Transacao(gasto_no_mes, data_referencia, ref_usuario) );
            
            data_referencia = Timestamp.valueOf(data_referencia.toLocalDateTime().minusMonths(1));
        }
        
        return lista_gastos_por_mes;
    }
    
    public int getId() 
    {
        return id;
    }
    
    public double getValor()
    {
        return this.valor;
    }
    
    public Timestamp getData()
    {
        return this.data;
    }
    
    public int getRef_usuario()
    {
        return this.ref_usuario;
    }
    
    public void setId( int id )
    {
        this.id = id;
    }
    
    public void setValor( double valor )
    {
        this.valor = valor;
    }
    
    public void setData( Timestamp data )
    {
        this.data = data;
    }
    
    //TODO: ajustar para ajustar o nome do metodo
    public void setRef_usuario( int ref_usuario )
    {
        this.ref_usuario = ref_usuario;
    }
       
    public void store()
    {
        this.store( this.id );
    }
}