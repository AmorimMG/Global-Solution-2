package fiap.com.services;

import fiap.com.model.Ativo;
import fiap.com.model.HistoricoPrecoAtivo;
import fiap.com.repository.AtivoDAO;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AtivoService {
    private static AtivoService instance = null;
    private final AtivoDAO ativoDAO;

    private final Map<String, Ativo> cache = new HashMap<>();

    public static AtivoService getInstance() {
        if (instance == null) {
            instance = new AtivoService();
        }
        return instance;
    }

    private AtivoService() {
        ativoDAO = AtivoDAO.getInstance();
    }

    public Ativo criar(String codigo, String nome, BigDecimal preco) throws Exception {
        Ativo ativo = new Ativo(codigo, nome, preco);
        boolean success = ativoDAO.criar(ativo);

        if (!success) {
            throw new Exception("Erro ao criar ativo...");
        }

        HistoricoPrecoAtivo h = HistoricoPrecoAtivo.fromAtivo(ativo);
        h.salvar();

        return ativo;
    }

    public List<Ativo> listarAtivos() {
        return ativoDAO.listarAtivos();
    }

    private Optional<Ativo> acharPorCodigo(String codigo) {
        return ativoDAO.acharPorCodigo(codigo);
    }

    public Optional<Ativo> getAtivo(String codigo) {
        Ativo cached = cache.get(codigo);
        if (cached != null) {
            return Optional.of(cached);
        }

        Optional<Ativo> db = acharPorCodigo(codigo);
        db.ifPresentOrElse(
                (ativo -> cache.put(ativo.getCodigoAtivo(), ativo)),
                () -> System.out.println("Ativo com código [" + codigo + "] não encontrado")
        );

        return db;
    }


    public static void main(String[] args) {
        AtivoService a = AtivoService.getInstance();

        try {
            System.out.println("Criando ativo BitCoin");
            a.criar("BTC", "BitCoin", new BigDecimal("1000"));

            System.out.println("Criando ativo Ethereum");
            a.criar("ETH", "Ethereum", new BigDecimal("500"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println("Procurando BTC");
        Optional<Ativo> res = a.getAtivo("BTC");
        System.out.println("Resultado: " + res);

        System.out.println("Procurando ETH");
        res = a.getAtivo("ETH");
        System.out.println("Resultado:" + res);

        System.out.println("Procurando por ativo invalido");
        res = a.getAtivo("ABC");
        System.out.println("Resultado:" + res);

        System.out.println("Procurando TODOS");
        List<?> ativos = a.listarAtivos();
        System.out.println("Resultado:" + ativos);
    }

    public void deletarAtivo(String codigo) {
        boolean success = ativoDAO.deletar(codigo);
        if (!success) {
            System.out.println("Erro ao deletar ativo");
        }
    }
}
