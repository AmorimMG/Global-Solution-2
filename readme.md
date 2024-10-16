## Integrantes

- Andre Fernandes Chamis
- GABRIEL AMORIM MARTINS GONÇALVES
- Lucas Braun Mariano
- CAIO HENRIQUE ROCHA DA SILVA
- Kaio Lacerda Simões

## Instalando as dependências

É importante que você instale as dependências especificadas no arquivo [pom.xml](pom.xml) utilizando o maven.

Você pode utilizar o comando:

```bash
mvn clean install
```

# Rodando o projeto

Esse projeto está utilizando um banco de dados oracle Para inicializar o banco, basta rodar o método main
da classe [JdbcHelper](src/main/java/fiap/com/repository/JdbcHelper.java).

O arquivo de ddl do banco está na pasta db, na raiz do projeto.

Algumas classes de serviço, encontradas no pacote [services](src/main/java/fiap/com/services) possuem métodos `main()`
para popular o banco com sua respectiva entidade, como, por exemplo, o `UserService.java` e o `AtivoService.java`.

Também é possível de encontrar nas classes de serviço métodos para fazer uma "demo" de cada entidade.

Na classe de entrada do projeto, temos um teste end-to-end que reiniciará o banco e fará o fluxo do zero, cadastrando
entidades e fazendo um fluxo completo de compra de ativo