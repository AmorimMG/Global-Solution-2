package fiap.com.model;

import fiap.com.repository.AtivoDAO;

import java.math.BigDecimal;

public class Ativo {
    private final String codigoAtivo; // Primary Key
    private final String nomeAtivo;
    private BigDecimal valorAtivo = BigDecimal.ZERO;

    public Ativo(String codigoAtivo, String nomeAtivo, BigDecimal valorAtivo) {
        this.codigoAtivo = codigoAtivo;
        this.nomeAtivo = nomeAtivo;
        this.valorAtivo = valorAtivo;
    }

    public String getCodigoAtivo() {
        return codigoAtivo;
    }

    public String getNomeAtivo() {
        return nomeAtivo;
    }

    public BigDecimal getValorAtivo() {
        return valorAtivo;
    }

    public HistoricoPrecoAtivo setValorAtivo(BigDecimal valorAtivo) {
        if (valorAtivo.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O valor de um ativo não pode ser negativo!");
        }

        this.valorAtivo = valorAtivo;
        this.salvar();

        HistoricoPrecoAtivo h = HistoricoPrecoAtivo.fromAtivo(this);
        h.salvar();
        return h;
    }

    public HistoricoPrecoAtivo aumentarValor(BigDecimal incremento) {
        if (incremento.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O valor do incremento não deve ser negativo!");
        }

        return setValorAtivo(this.valorAtivo.add(incremento));
    }

    public HistoricoPrecoAtivo diminuirValor(BigDecimal decremento) {
        return setValorAtivo(this.valorAtivo.subtract(decremento));
    }

    @Override
    public String toString() {
        return "Ativo{" +
                "codigoAtivo='" + codigoAtivo + '\'' +
                ", nomeAtivo='" + nomeAtivo + '\'' +
                ", valorAtivo=" + valorAtivo +
                '}';
    }

    private void salvar() {
        AtivoDAO dao = AtivoDAO.getInstance();
        dao.salvar(this);
    }
}
