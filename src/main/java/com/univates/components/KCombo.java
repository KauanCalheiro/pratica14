package com.univates.components;

import java.util.ArrayList;

import javax.swing.JComboBox;

public class KCombo<T> extends JComboBox<Object>
{
    private ArrayList<KComboOption<T>> options_list = new ArrayList<KComboOption<T>>();
    
    private boolean start_empty;
    
    public KCombo( ArrayList<KComboOption<T>> options, boolean start_empty )
    {
        this.start_empty = start_empty;
        this.setOptions( options );
        this.setPosition( 10, 10 );   
        this.setSize( super.getPreferredSize() );
    }
    
    public KCombo( boolean start_empty )
    {
        this.start_empty = start_empty;
        this.setPosition( 10, 10 );   
        this.setSize( super.getPreferredSize() );
    }   
    
    public void setOptions( ArrayList<KComboOption<T>> options )
    {
        if (start_empty) 
        {
            super.addItem( "" );
        }
        
        for (KComboOption<T> object : options) 
        {
            super.addItem( object.getValue() );
            this.options_list.add( object );
        }
    }
    
    public void setPosition(int x, int y) 
    {
        super.setBounds( x, y, super.getWidth(), super.getHeight());
    }
    
    public T getValue()
    {
        isValidSelected();
        return (T)this.options_list.get( this.getSelectedIndex() ).getValue();
    }
    
    public int getKey()
    {
        isValidSelected();
        return this.options_list.get( this.getSelectedIndex() ).getKey();
    }
    
    public KComboOption<T> getOption()
    {
        isValidSelected();
        return this.options_list.get( this.getSelectedIndex() );
    }
    
    public void setSelectedByValue( T value )
    {
        for (KComboOption<T> option : this.options_list) 
        {
            if ( option.getValue() == value ) 
            {
                super.setSelectedItem( option.getValue() );
            }
        }
    }
    
    public void setSelectedByKey( int key )
    {
        for (KComboOption<T> option : this.options_list) 
        {
            if ( option.getKey() == key ) 
            {
                super.setSelectedItem( option.getValue() );
            }
        }
    }
    
    private void isValidSelected()
    {
        if (start_empty && super.getSelectedIndex() == 0) 
        {
            throw new NullPointerException("Não é possível selecionar o um item vazio.");
        }
    }
    
    @Override
    public int getSelectedIndex()
    {
        if (start_empty) 
        {
            return super.getSelectedIndex() - 1;    
        }
        
        return super.getSelectedIndex();
    }
}
