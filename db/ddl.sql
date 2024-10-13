-- Tabela Ativo
CREATE TABLE Ativo (
                       codigo_ativo VARCHAR2(10) PRIMARY KEY,
                       nome_ativo VARCHAR2(255) NOT NULL,
                       valor_ativo NUMBER(18, 5) NOT NULL
);

-- Tabela Conta
CREATE TABLE Conta (
                       cpf VARCHAR2(11) PRIMARY KEY,
                       nome VARCHAR2(255) NOT NULL,
                       email VARCHAR2(255) NOT NULL,
                       data_nascimento DATE NOT NULL,
                       login VARCHAR2(50) NOT NULL,
                       senha VARCHAR2(255) NOT NULL,
                       saldo NUMBER(18, 2) DEFAULT 0 CHECK (saldo >= 0)
);

-- Constraint UNIQUE na tabela Conta para o login
ALTER TABLE Conta ADD CONSTRAINT unique_login UNIQUE (login);

-- Tabela Carteira (associação Many-to-Many entre Conta e Ativo)
CREATE TABLE Carteira (
                          cpf VARCHAR2(11),
                          codigo_ativo VARCHAR2(10),
                          quantidade NUMBER(18, 5) NOT NULL,
                          PRIMARY KEY (cpf, codigo_ativo)
);

-- Constraint Foreign Key para associar Carteira com Conta (cpf)
ALTER TABLE Carteira ADD CONSTRAINT fk_carteira_conta FOREIGN KEY (cpf) REFERENCES Conta(cpf) ON DELETE CASCADE;

-- Constraint Foreign Key para associar Carteira com Ativo (codigo_ativo)
ALTER TABLE Carteira ADD CONSTRAINT fk_carteira_ativo FOREIGN KEY (codigo_ativo) REFERENCES Ativo(codigo_ativo) ON DELETE CASCADE;

-- Tabela HistoricoPrecoAtivo (associação Many-to-One com Ativo)
CREATE TABLE HistoricoPrecoAtivo (
                                     codigo_ativo VARCHAR2(10),
                                     data_preco TIMESTAMP,
                                     valor_ativo NUMBER(18, 5) NOT NULL,
                                     PRIMARY KEY (codigo_ativo, data_preco)
);

-- Constraint Foreign Key para associar HistoricoPrecoAtivo com Ativo (codigo_ativo)
ALTER TABLE HistoricoPrecoAtivo ADD CONSTRAINT fk_historico_ativo FOREIGN KEY (codigo_ativo) REFERENCES Ativo(codigo_ativo) ON DELETE CASCADE;

-- Tabela Transacao
CREATE TABLE Transacao (
                           tipo VARCHAR2(6) CHECK (tipo IN ('COMPRA', 'VENDA')),
                           valor NUMBER(18, 2) NOT NULL,
                           data TIMESTAMP,
                           cpf VARCHAR2(11),
                           codigo_ativo VARCHAR2(10),
                           PRIMARY KEY (cpf, codigo_ativo, data)
);

-- Constraint Foreign Key para associar Transacao com Conta (cpf)
ALTER TABLE Transacao ADD CONSTRAINT fk_transacao_conta FOREIGN KEY (cpf) REFERENCES Conta(cpf) ON DELETE CASCADE;

-- Constraint Foreign Key para associar Transacao com Ativo (codigo_ativo)
ALTER TABLE Transacao ADD CONSTRAINT fk_transacao_ativo FOREIGN KEY (codigo_ativo) REFERENCES Ativo(codigo_ativo) ON DELETE CASCADE;
