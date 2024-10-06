package fiap.com.services;

import fiap.com.exception.UnauthorizedException;
import fiap.com.model.Ativo;
import fiap.com.model.Carteira;
import fiap.com.model.Conta;
import fiap.com.repository.CarteiraDAO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

public class CarteiraService {
    private static CarteiraService instance = null;
    private final CarteiraDAO carteiraDAO;

    public static CarteiraService getInstance() {
        if (instance == null) {
            instance = new CarteiraService();
        }
        return instance;
    }

    private CarteiraService(){
        carteiraDAO = CarteiraDAO.getInstance();
    }

    public Map<String, BigDecimal> buscarCarteira(Conta conta) {
        return carteiraDAO.buscarCarteira(conta);
    }

    public void exibirCarteira(Conta conta) {
        Map<String, BigDecimal> carteira = buscarCarteira(conta);
        carteira.forEach((k, v) -> {
            System.out.println(k + " - " + v);
        });
    }

    public static void main(String[] args) throws UnauthorizedException {
        ContaService contaService = ContaService.getInstance();
        AtivoService ativoService = AtivoService.getInstance();
        CarteiraService carteiraService = CarteiraService.getInstance();

        Conta conta = contaService.autenticar("user", "pass");

        Carteira carteira = new Carteira(conta);

        List<Ativo> ativos = ativoService.listarAtivos();

        conta.depositar(new BigDecimal(ativos.size() * 1000));

        System.out.println("Saldo inicial na conta: " + conta.getSaldo());

        ativos.forEach((ativo -> {
            System.out.println("Comprando ativo [" + ativo.getCodigoAtivo() + "] para o usuário [" + conta.getLogin() + "]");
            carteira.comprar(ativo, new BigDecimal("1000"));
            System.out.println("Novo saldo: " + conta.getSaldo());
        }));

        System.out.println("Saldo na conta após compras: " + conta.getSaldo());

        System.out.println("\n-------------------------\n");

        carteiraService.exibirCarteira(conta);

        System.out.println("\n-------------------------\n");

        // Vamos dobras o valor de todos os ativos para simulação
        ativos.forEach(ativo -> {
            ativo.aumentarValor(ativo.getValorAtivo());
        });

        System.out.println("Liquidando todos os ativos");
        ativos.forEach(ativo -> {
            System.out.println("Liquidando ativo [" + ativo.getCodigoAtivo() + "] para o usuário + [" + conta.getLogin() + "]");
            BigDecimal venda = carteira.liquidar(ativo);
            System.out.println("Valor da venda: " + venda);
            System.out.println("Novo saldo: " + conta.getSaldo());

        });

        System.out.println("Saldo na conta após vendas: " + conta.getSaldo());

        // Vamos resetar o valor dos ativos após a simulação
        ativos.forEach(ativo -> {
            ativo.diminuirValor(ativo.getValorAtivo().divide(new BigDecimal("2"), 5, RoundingMode.HALF_UP));
        });

        System.out.println("\n-------------------------\n");

        carteiraService.exibirCarteira(conta);
    }
}
