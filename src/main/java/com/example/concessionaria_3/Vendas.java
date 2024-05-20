package com.example.concessionaria_3;

import java.time.LocalDate;

public class Vendas {
    private int idVenda;
    private LocalDate dataVenda;
    private double valor;
    private int idCarro; // Adiciona o campo idCarro

    // Construtor completo
    public Vendas(int idVenda, LocalDate dataVenda, double valor, int idCarro) {
        this.idVenda = idVenda;
        this.dataVenda = dataVenda;
        this.valor = valor;
        this.idCarro = idCarro;
    }

    // Construtor sem idVenda (para inserção de novas vendas)
    public Vendas(LocalDate dataVenda, double valor, int idCarro) {
        this.dataVenda = dataVenda;
        this.valor = valor;
        this.idCarro = idCarro;
    }

    // Construtor vazio
    public Vendas() {
    }

    // Getters e setters
    public int getIdVenda() {
        return idVenda;
    }

    public void setIdVenda(int idVenda) {
        this.idVenda = idVenda;
    }

    public LocalDate getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(LocalDate dataVenda) {
        this.dataVenda = dataVenda;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public int getIdCarro() {
        return idCarro;
    }

    public void setIdCarro(int idCarro) {
        this.idCarro = idCarro;
    }
}


