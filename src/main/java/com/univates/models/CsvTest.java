package com.univates.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class CsvTest {
    
    public static void main(String[] args) 
    {
        CsvTest test = new CsvTest();
        test.testEscreveLinha();
        test.testFormataListaItens();
    }

    public void testEscreveLinha() {
        Csv csv = new Csv("test.csv");

        ArrayList<String> listaItens = new ArrayList<>();
        listaItens.add("item1");
        listaItens.add("item2");
        listaItens.add("item3");

        try {
            boolean result = csv.escreveLinha(listaItens);
            Assertions.assertTrue(result);
        } catch (Exception e) {
            Assertions.fail("Exception should not be thrown");
        }
    }

    public void testFormataListaItens() {
        Csv csv = new Csv("test.csv");

        ArrayList<String> listaItens = new ArrayList<>();
        listaItens.add("item1");
        listaItens.add("item2");
        listaItens.add("item3");

        ArrayList<String> expected = new ArrayList<>();
        expected.add("item1,item2,item3");

        ArrayList<String> result = csv.formataListaItens(listaItens);
        Assertions.assertEquals(expected, result);
    }
}

