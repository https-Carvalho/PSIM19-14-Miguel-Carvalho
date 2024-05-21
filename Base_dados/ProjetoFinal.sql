create database concessionaria;

use concessionaria;

CREATE TABLE clientes (
    id_cliente smallint PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(50),
    contato VARCHAR(50)
);

CREATE TABLE carros (
    id_carr smallint PRIMARY KEY AUTO_INCREMENT,
    marca VARCHAR(50),
    modelo VARCHAR(50),
    data_registo DATE,
    preco DECIMAL(10,2),
    status VARCHAR(50) DEFAULT 'disponível'
);

CREATE TABLE vendas (
    id_venda smallint AUTO_INCREMENT PRIMARY KEY,
    data_venda DATE,
    valor DECIMAL(10, 2),
    id_carro smallint,
    FOREIGN KEY (id_carro) REFERENCES carros(id_carro)
);

CREATE TABLE Utilizadores (
    id_utilizador smallint PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50),
    password VARCHAR(50)
);

INSERT INTO clientes (nome, contato) VALUES
('João Silva', '99999-9999'),
('Maria Souza', '88888-8888'),
('Pedro Oliveira', '77777-7777'),
('Ana Costa', '66666-6666'),
('Lucas Lima', '55555-5555');


INSERT INTO carros (marca, modelo, data_registo, preco, status) VALUES
('Toyota', 'Corolla', '2022-01-15', 20000.00, 'disponível'),
('Honda', 'Civic', '2021-05-20', 22000.00, 'disponível'),
('Ford', 'Focus', '2023-03-10', 25000.00, 'disponível'),
('Chevrolet', 'Malibu', '2020-07-22', 18000.00, 'disponível'),
('Nissan', 'Sentra', '2019-11-30', 17000.00, 'disponível');


INSERT INTO vendas (data_venda, valor, id_carro) VALUES
('2023-04-01', 19500.00, 1),
('2023-04-02', 21000.00, 2),
('2023-04-03', 24500.00, 3),
('2023-04-04', 17500.00, 4),
('2023-04-05', 16500.00, 5);


INSERT INTO Utilizadores (username, password) VALUES
('eu', 'root');
