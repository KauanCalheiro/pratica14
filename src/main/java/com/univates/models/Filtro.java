package com.univates.models;

public class Filtro 
{
    String coluna;
    String operador;
    String valor;
    
    public Filtro(String coluna, String operador, String valor)
    {
        this.coluna   = coluna;
        this.operador = operador;
        this.valor    = valor;
        
        this.formatQuery();
    }
    
    private void formatQuery() 
    {
        switch (this.operador.toLowerCase()) 
        {
            case "like":
            case "ilike":
                this.valor = "'%" + this.valor + "%'";
            return;
            
            case "between":
                String[] valores = this.valor.split(",");
                this.valor = "'" + valores[0] + "'" + " AND " + "'" + valores[1] + "'" ;
            return;
            
            case "in":
                valores = this.valor.split(",");
                this.valor = "(";
                for (int i = 0; i < valores.length; i++) 
                {
                    this.valor += "'" + valores[i] + "'";
                    if (i < valores.length - 1) 
                    {
                        this.valor += ",";
                    }
                }
                this.valor += ")";
            return;
            
            case "is null":
            case "is not null":
            case "is true":
            case "is not true":
            case "is false":
            case "is not false":
                this.valor = "";
            return;

            case "order by":
                this.coluna = "";
            return;
            
            default:
                this.valor = "'" + this.valor + "'";
            return;
        }
    }

    public String getQuery()
    {        
        return this.coluna + " " + this.operador + " " + this.valor;
    }
}
