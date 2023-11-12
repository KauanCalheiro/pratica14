package com.univates.components;

public class KComboOption<T>
{
    private T   value;
    private int key;
    
    public KComboOption( T value, int key )
    {
        this.value = value;
        this.key   = key;
    }
    
    public T getValue()
    {
        return this.value;
    }
    
    public int getKey()
    {
        return this.key;
    }    
}
