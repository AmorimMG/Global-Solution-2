package fiap.com.model;

import fiap.com.repository.CarteiraDAO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Classe utilitária para facilitar a manipulação das linhas da tabela Carteira.
 * Representa uma relação many to many onde cada par cpd-codigo moeda forma uma linha
 * */
public class Carteira { // Many to Many com Conta e Ativo, usarei uma PK composta
    private final Conta conta;
    private final Map<String, BigDecimal> carteira = new HashMap<>();

    public Carteira(Conta conta) {
        this.conta = conta;
    }

    public Conta getConta() {
        return conta;
    }

    /**
     * Metodo para comprar um ativo
     * @param ativo para ser comprado
     * @param reais quantos reais devem ser depositados do ativo especificado
     *
     * @return a nova quantidade do ativo
     * */
    public BigDecimal comprar(Ativo ativo, BigDecimal reais) {
        Optional<BigDecimal> optional = getAtivo(ativo);
        boolean novo = optional.isEmpty();
        BigDecimal valorNaCarteira = optional.orElse(BigDecimal.ZERO);

        conta.sacar(reais);
        BigDecimal quantidadeDeMoedas = reais.divide(ativo.getValorAtivo(), 5, RoundingMode.HALF_UP);

        BigDecimal novoValor = valorNaCarteira.add(quantidadeDeMoedas);
        carteira.put(ativo.getCodigoAtivo(), novoValor);

        salvar(ativo, novo);

        Transacao t = Transacao.compra(reais, this.conta, ativo);
        t.salvar();

        return novoValor;
    }

    /**
     * Metodo para comprar um ativo
     * @param ativo para ser vendido
     * @param ativos quantos ativos devem ser vendidos
     *
     * @return o valor da venda em reais
     * */
    public BigDecimal vender(Ativo ativo, BigDecimal ativos) {
        Optional<BigDecimal> optional = getAtivo(ativo);
        if (optional.isEmpty()) {
            throw new IllegalArgumentException("Você não possui esse ativo!");
        }

        BigDecimal valorNaCarteira = optional.get();

        if (ativos.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Você não pode vender uma quantidade negativa de ativos");
        }

        if (ativos.compareTo(valorNaCarteira) > 0) {
            throw new IllegalArgumentException("Você está tentando vender mais ativos do que possui!");
        }

        carteira.put(ativo.getCodigoAtivo(), valorNaCarteira.subtract(ativos));

        BigDecimal valorDaVenda = ativos.multiply(ativo.getValorAtivo()).setScale(2, RoundingMode.HALF_UP);
        conta.depositar(valorDaVenda);

        salvar(ativo, false);

        Transacao t = Transacao.venda(valorDaVenda, this.conta, ativo);
        t.salvar();

        return valorDaVenda;
    }

    /**
     * Vende todos os ativos de um tipo
     * @param ativo - para ser vendido
     *
     * @return o valor da venda em reais
     * */
    public BigDecimal liquidar(Ativo ativo) {
        Optional<BigDecimal> optional = getAtivo(ativo);

        BigDecimal valorNaCarteira = optional.orElse(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);
        return vender(ativo, valorNaCarteira);
    }

    public Optional<BigDecimal> getAtivo(Ativo ativo) {
        BigDecimal quantidade = carteira.get(ativo.getCodigoAtivo());
        if (quantidade != null) {
            quantidade = quantidade.setScale(2, RoundingMode.HALF_UP);
            return Optional.of(quantidade);
        }

        CarteiraDAO carteiraDAO = CarteiraDAO.getInstance();
        Optional<BigDecimal> res = carteiraDAO.buscarPorCpfCodigo(this.conta.getCpf(), ativo.getCodigoAtivo());
        res.ifPresent((v) -> {
            carteira.put(ativo.getCodigoAtivo(), v.setScale(2, RoundingMode.HALF_UP));
        });

        return res;
    }

    private void salvar(Ativo ativo, boolean novo) {
        BigDecimal quantidade = carteira.get(ativo.getCodigoAtivo());
        if (quantidade == null) {
            quantidade = BigDecimal.ZERO;
            novo = true;
        }
        CarteiraDAO carteiraDAO = CarteiraDAO.getInstance();
        carteiraDAO.salvar(this.conta.getCpf(), ativo.getCodigoAtivo(), quantidade, novo);
    }

    @Override
    public String toString() {
        return "Carteira{" +
                "conta=" + conta +
                ", carteira=" + carteira +
                '}';
    }
}
