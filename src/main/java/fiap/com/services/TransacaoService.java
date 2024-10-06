package fiap.com.services;

import fiap.com.exception.UnauthorizedException;
import fiap.com.model.Conta;
import fiap.com.model.Transacao;
import fiap.com.repository.TransacaoDAO;

import java.util.List;

public class TransacaoService {
    private static TransacaoService instance = null;
    private final TransacaoDAO transacaoDAO;

    private TransacaoService() {
        transacaoDAO = TransacaoDAO.getInstance();
    }

    public static TransacaoService getInstance() {
        if (instance == null) {
            instance = new TransacaoService();
        }
        return instance;
    }

    public List<Transacao> buscarHistoricoPorConta(Conta conta) {
        return transacaoDAO.pegarHistorico(conta);
    }

    public static void main(String[] args) throws UnauthorizedException {
        String cpf = "12345678910";
        TransacaoService service = new TransacaoService();
        ContaService contaService = ContaService.getInstance();
        Conta conta = contaService.autenticar("user", "pass");
        var historico = service.buscarHistoricoPorConta(conta);
        for (int i = 0; i < historico.size(); i++) {
            System.out.println(String.valueOf(i + 1) + " - " + historico.get(i));
        }
    }
}
