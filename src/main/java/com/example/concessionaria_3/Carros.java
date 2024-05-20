package com.example.concessionaria_3;

import java.time.LocalDate;

public class Carros {
    private int idCarro;
    private String marca;
    private String modelo;
    private LocalDate dataRegistro;
    private double preco;
    private String status;
    public Carros(int idCarro, String marca, String modelo, LocalDate dataRegistro, double preco, String status) {
        this.idCarro = idCarro;
        this.marca = marca;
        this.modelo = modelo;
        this.dataRegistro = dataRegistro;
        this.preco = preco;
        this.status = status;
    }

    // Construtor sem id (para novos carros, onde o id é gerado automaticamente)
    public Carros(String marca, String modelo, LocalDate dataRegistro, double preco, String status) {
        this.marca = marca;
        this.modelo = modelo;
        this.dataRegistro = dataRegistro;
        this.preco = preco;
        this.status = status;
    }

    public Carros(String marca, String modelo, LocalDate dataRegistro, double preco) {
        this.marca = marca;
        this.modelo = modelo;
        this.dataRegistro = dataRegistro;
        this.preco = preco;
    }

    // Construtor vazio
    public Carros() {
    }

    // Getters e Setters
    public int getIdCarro() {
        return idCarro;
    }

    public void setIdCarro(int idCarro) {
        this.idCarro = idCarro;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public LocalDate getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(LocalDate dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}