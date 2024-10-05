## Instalando as dependências
É importante que você instale as dependências especificadas no arquivo [pom.xml](pom.xml) utilizando o maven.

Você pode utilizar o comando:
```bash
mvn clean install
```

# Rodando o projeto

Esse projeto está utilizando um banco de dados em arquivo h2. Para criá-lo em sua máquina, basta rodar o método main 
da classe [JdbcHelper](src/main/java/fiap/com/repository/JdbcHelper.java).

O banco deve ser criado dentro da pasta db na raiz do projeto