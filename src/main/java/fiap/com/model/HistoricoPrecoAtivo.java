package fiap.com.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record HistoricoPrecoAtivo(
        String codigoAtivo, // PK conjunta
        LocalDateTime dataPreco, // PK conjunta
        BigDecimal valorAtivo
) {
    public static HistoricoPrecoAtivo fromAtivo(Ativo ativo) {
        return new HistoricoPrecoAtivo(ativo.getCodigoAtivo(), LocalDateTime.now(), ativo.getValorAtivo());
    }
}
