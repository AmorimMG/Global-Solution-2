-- Tabela Ativo
CREATE TABLE Ativo (
                       codigo_ativo VARCHAR(10) PRIMARY KEY,  -- Código do ativo como chave primária
                       nome_ativo VARCHAR(255) NOT NULL,      -- Nome do ativo, campo não nulo
                       valor_ativo DECIMAL(18, 5) NOT NULL      -- Valor do ativo, não pode ser negativo
);

-- Tabela Conta
CREATE TABLE Conta (
                       cpf VARCHAR(11) PRIMARY KEY,         -- CPF como chave primária
                       nome VARCHAR(255) NOT NULL,          -- Nome do usuário, campo não nulo
                       email VARCHAR(255) NOT NULL,         -- Email não nulo
                       data_nascimento DATE NOT NULL,       -- Data de nascimento, campo não nulo
                       login VARCHAR(50) NOT NULL,          -- Login do usuário, campo não nulo
                       senha VARCHAR(255) NOT NULL,         -- Senha do usuário, campo não nulo
                       saldo DECIMAL(18, 2) DEFAULT 0,      -- Saldo da conta
                       CHECK (saldo >= 0)                   -- Saldo não pode ser negativo
);

-- Tabela Carteira (associação Many-to-Many entre Conta e Ativo)
CREATE TABLE Carteira (
                          cpf VARCHAR(11),                      -- Chave estrangeira para Conta
                          codigo_ativo VARCHAR(10),             -- Chave estrangeira para Ativo
                          quantidade DECIMAL(18, 5) NOT NULL,   -- Quantidade de ativos na carteira
                          PRIMARY KEY (cpf, codigo_ativo)       -- Chave primária composta
);

-- Constraints da tabela Carteira
ALTER TABLE Carteira
    ADD CONSTRAINT fk_carteira_conta
        FOREIGN KEY (cpf) REFERENCES Conta(cpf) ON DELETE CASCADE;

ALTER TABLE Carteira
    ADD CONSTRAINT fk_carteira_ativo
        FOREIGN KEY (codigo_ativo) REFERENCES Ativo(codigo_ativo) ON DELETE CASCADE;

-- Tabela HistoricoPrecoAtivo (associação Many-to-One com Ativo)
CREATE TABLE HistoricoPrecoAtivo (
                                     codigo_ativo VARCHAR(10),                  -- Chave estrangeira para Ativo
                                     data_preco TIMESTAMP,                      -- Data do preço (parte da chave primária)
                                     valor_ativo DECIMAL(18, 5) NOT NULL,       -- Valor do ativo na data
                                     PRIMARY KEY (codigo_ativo, data_preco)     -- Chave primária composta
);

-- Constraints da tabela HistoricoPrecoAtivo
ALTER TABLE HistoricoPrecoAtivo
    ADD CONSTRAINT fk_historico_ativo
        FOREIGN KEY (codigo_ativo) REFERENCES Ativo(codigo_ativo) ON DELETE CASCADE;

-- Tabela Transacao
CREATE TABLE Transacao (
                           tipo VARCHAR(6) CHECK (tipo IN ('COMPRA', 'VENDA')),  -- Tipo da transação
                           valor DECIMAL(18, 2) NOT NULL,                       -- Valor da transação
                           data TIMESTAMP,                                      -- Data da transação (parte da chave primária)
                           cpf VARCHAR(11),                                     -- Chave estrangeira para Conta (parte da chave primária)
                           codigo_ativo VARCHAR(10),                            -- Chave estrangeira para Ativo (parte da chave primária)
                           PRIMARY KEY (cpf, codigo_ativo, data)               -- Chave primária composta
);

-- Constraints da tabela Transacao
ALTER TABLE Transacao
    ADD CONSTRAINT fk_transacao_conta
        FOREIGN KEY (cpf) REFERENCES Conta(cpf) ON DELETE CASCADE;

ALTER TABLE Transacao
    ADD CONSTRAINT fk_transacao_ativo
        FOREIGN KEY (codigo_ativo) REFERENCES Ativo(codigo_ativo) ON DELETE CASCADE;
