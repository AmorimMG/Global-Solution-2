package fiap.com;

import fiap.com.application.menu.Menu;
import fiap.com.model.*;
import fiap.com.repository.JdbcHelper;
import fiap.com.services.*;
import fiap.com.util.DateUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;


public class Main {
    public static void main(String[] args) throws Exception {
        /*
          Des-comente a linha abaixo para rodar a simulação end to end da aplicação. Saiba que ela vai apagar
          o banco de dados ao início e ao fim.
          */
//        endToEnd();

        Menu menu = new Menu();
        menu.execute();
    }

    /**
     * Teste End-To-End.
     * <p>
     * Aqui, apagamos completamente o banco de dados, criamos as tabelas e constraints do zero, e fazemos um fluxo
     * completo da aplicação
     * <p>
     * Ao fim, reiniciamos novamente o banco de dados
     */
    private static void endToEnd() throws Exception {
        JdbcHelper jdbcHelper = JdbcHelper.getInstance();
        jdbcHelper.resetDb();

        // ------- Instanciação das classes de serviço ------ //


        ContaService contaService = ContaService.getInstance();
        CarteiraService carteiraService = CarteiraService.getInstance();
        AtivoService ativoService = AtivoService.getInstance();
        TransacaoService transacaoService = TransacaoService.getInstance();
        HistoricoPrecoAtivoService historicoPrecoAtivoService = HistoricoPrecoAtivoService.getInstance();


        // ------- Instanciação da conta ------ //
        System.out.println("\n=================\n");

        contaService.cadastrar(
                "12345678910",
                "Global Solution",
                "gloabal@solution.com.br",
                DateUtil.dateFromString("25/12/1947"),
                "user",
                "pass"
        );

        Conta conta = contaService.autenticar("user", "pass");
        System.out.println("Conta criada: " + conta);

        // ------- Instanciação dos ativos ------ //
        System.out.println("\n=================\n");

        try {
            ativoService.criar("BTC", "Bitcon", new BigDecimal("1000"));
            ativoService.criar("ETH", "Ethereum", new BigDecimal("400"));
            ativoService.criar("BNB", "Binance coin", new BigDecimal("600"));
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar ativos");
            return;
        }

        List<Ativo> ativos = ativoService.listarAtivos();
        System.out.println("Ativos no banco de dados: ");
        ativos.forEach(System.out::println);

        // ------- Busca de ativos ------ //
        System.out.println("\n=================\n");

        System.out.println("Buscando pelo ativo com código BTC");
        Optional<Ativo> btcOpt = ativoService.getAtivo("BTC");
        System.out.println("Resultado da busca: " + btcOpt);

        System.out.println("\nBuscando pelo ativo inválido com código XYZ");
        Optional<Ativo> xyzOpt = ativoService.getAtivo("XYZ");
        System.out.println("Resultado da busca: " + xyzOpt);

        Optional<Ativo> ethOpt = ativoService.getAtivo("ETH");
        Optional<Ativo> bnbOpt = ativoService.getAtivo("BNB");


        // ------- Depósito de fundos na conta ------ //
        System.out.println("\n=================\n");
        System.out.println("Saldo da conta antes do depósito: " + conta.getSaldo());

        conta.depositar(new BigDecimal("10000"));

        System.out.println("Saldo da conta após depósito: " + conta.getSaldo());

        Optional<Conta> contaFromDB = contaService.buscarPorCpf(conta.getCpf());
        contaFromDB.ifPresent((c) -> System.out.println("Dados da conta no banco de dados: " + c));

        // ------- Compra de ativos ------ //
        System.out.println("\n=================\n");

        Carteira carteira = new Carteira(conta);

        Ativo btc = btcOpt.orElseThrow();
        Ativo eth = ethOpt.orElseThrow();
        Ativo bnb = bnbOpt.orElseThrow();

        carteira.comprar(btc, new BigDecimal("1000"));
        carteira.comprar(eth, new BigDecimal("500"));
        carteira.comprar(bnb, new BigDecimal("1500"));

        carteiraService.exibirCarteira(conta);

        System.out.println("Saldo da conta após compras: " + conta.getSaldo());

        // ------- Alteração de preço ativos ------ //
        btc.aumentarValor(new BigDecimal("250"));
        eth.aumentarValor(new BigDecimal("125"));
        bnb.diminuirValor(new BigDecimal("300"));

        // ------- Venda de ativos ------ //
        System.out.println("\n=================\n");

        // Vamos vender metade do bitcoin e liquidar o resto
        Optional<BigDecimal> quantidadeDeBtcNaCarteira = carteira.getAtivo(btc);
        if (quantidadeDeBtcNaCarteira.isPresent()) {
            BigDecimal qtd = quantidadeDeBtcNaCarteira.get();
            BigDecimal res = carteira.vender(btc, qtd.divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP));
            System.out.println("Lucro com a venda do BTC: " + res);
        }

        BigDecimal res = carteira.liquidar(eth);
        System.out.println("Lucro com a venda do ETH: " + res);

        res = carteira.liquidar(bnb);
        System.out.println("Lucro com a venda do BNB: " + res);

        System.out.println("Saldo da conta após vendas: " + conta.getSaldo());
        System.out.println("\nCarteira do usuário após vendas: ");
        carteiraService.exibirCarteira(conta);

        // ------- Históricos ------ //
        System.out.println("\n=================\n");

        List<Transacao> transacoes = transacaoService.buscarHistoricoPorConta(conta);
        System.out.println("Histórico de transações da conta: ");
        for (int i = 0; i < transacoes.size(); i++) {
            System.out.println(i + 1 + " - " + transacoes.get(i));
        }

        System.out.println("\nHistórico de alterações de preço: ");
        List<HistoricoPrecoAtivo> historicoPrecoAtivo = historicoPrecoAtivoService.buscarHistoricoCompleto();
        for (int i = 0; i < historicoPrecoAtivo.size(); i++) {
            System.out.println(i + 1 + " - " + historicoPrecoAtivo.get(i));
        }

        // ------- Reset do banco ------ //
        System.out.println("\n=================\n");
        jdbcHelper.resetDb();
    }


}