package fiap.com.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Transacao(
        TipoTransacao tipo,
        BigDecimal valor,
        LocalDateTime data, // PK conjunta
        String cpf, // PK conjunta
        String cdAtivo // PK conjunta
) {

    public static Transacao compra(BigDecimal valor, Conta Conta, Ativo ativo) {
        return new Transacao(TipoTransacao.COMPRA, valor, LocalDateTime.now(), Conta.getCpf(), ativo.getCodigoAtivo());
    }

    public static Transacao venda(BigDecimal valor, Conta Conta, Ativo ativo) {
        return new Transacao(TipoTransacao.VENDA, valor, LocalDateTime.now(), Conta.getCpf(), ativo.getCodigoAtivo());
    }

    public enum TipoTransacao {
        COMPRA,
        VENDA
    }
}
