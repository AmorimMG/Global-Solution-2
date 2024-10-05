package fiap.com.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

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
        BigDecimal valorNaCarteira = optional.orElse(BigDecimal.ZERO);

        BigDecimal reaisSacados = conta.sacar(reais);
        BigDecimal quantidadeDeMoedas = reaisSacados.divide(ativo.getValorAtivo(), 5, RoundingMode.HALF_UP);

        BigDecimal novoValor = valorNaCarteira.add(quantidadeDeMoedas);
        carteira.put(ativo.getCodigoAtivo(), novoValor);

        // TODO db

        Transacao t = Transacao.compra(reais, this.conta, ativo);

        // TODO db
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
        // TODO db

        Transacao t = Transacao.venda(valorDaVenda, this.conta, ativo);

        // TODO db

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

        BigDecimal valorNaCarteira = optional.orElse(BigDecimal.ZERO);
        return vender(ativo, valorNaCarteira);
    }

    public Optional<BigDecimal> getAtivo(Ativo ativo) {
        // TODO DB - Search hashMap, if present return if not search db
        return Optional.ofNullable(carteira.get(ativo.getCodigoAtivo()));
    }

    @Override
    public String toString() {
        return "Carteira{" +
                "conta=" + conta +
                ", carteira=" + carteira +
                '}';
    }
}
