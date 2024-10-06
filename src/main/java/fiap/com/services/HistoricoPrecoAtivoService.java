package fiap.com.services;

import fiap.com.model.Ativo;
import fiap.com.model.HistoricoPrecoAtivo;
import fiap.com.model.Transacao;
import fiap.com.repository.HistoricoPrecoAtivoDAO;
import fiap.com.repository.TransacaoDAO;

import java.util.List;

public class HistoricoPrecoAtivoService {
    private static HistoricoPrecoAtivoService instance = null;
    private final HistoricoPrecoAtivoDAO historicoPrecoAtivoDAO;

    private HistoricoPrecoAtivoService() {
        historicoPrecoAtivoDAO = HistoricoPrecoAtivoDAO.getInstance();
    }

    public static HistoricoPrecoAtivoService getInstance() {
        if (instance == null) {
            instance = new HistoricoPrecoAtivoService();
        }

        return instance;
    }

    public List<HistoricoPrecoAtivo> buscarHistoricoPorAtivo(Ativo ativo) {
        return historicoPrecoAtivoDAO.pegarHistorico(ativo);
    }

    public List<HistoricoPrecoAtivo> buscarHistoricoCompleto() {
        return historicoPrecoAtivoDAO.pegarHistoricoCompleto();

    }

    public static void main(String[] args) {
        String cpf = "12345678910";
        HistoricoPrecoAtivoService service = new HistoricoPrecoAtivoService();
        AtivoService ativoService = AtivoService.getInstance();
        Ativo ativo = ativoService.getAtivo("BTC").orElseThrow();
        var historico = service.buscarHistoricoPorAtivo(ativo);
        for (int i = 0; i < historico.size(); i++) {
            System.out.println(String.valueOf(i + 1) + " - " + historico.get(i));
        }
    }
}
