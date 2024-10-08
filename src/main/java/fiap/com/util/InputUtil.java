package fiap.com.util;

import fiap.com.model.Ativo;
import fiap.com.services.AtivoService;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class InputUtil {
    private static final Scanner scanner = new Scanner(System.in);

    public static String getString(String message) {
        System.out.print(message);

        try {
            return scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Insira um valor textual válido!");
            return getString(message);
        }
    }

    public static Date getDate(String message, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);

        try {
            System.out.print(message);
            return df.parse(scanner.nextLine());
        } catch (Exception e) {
            System.out.println("Insira uma data válida!");
            return getDate(message, pattern);
        }
    }

    public static int getInteger(String message) {
        try {
            System.out.print(message);
            int valor = scanner.nextInt();
            scanner.nextLine();
            return valor;
        } catch (Exception e) {
            System.out.println("Insira um número inteiro válido!");
            scanner.nextLine();
            return getInteger(message);
        }
    }

    public static BigDecimal getBigDecimal(String message) {
        System.out.print(message);

        try {
            BigDecimal valor = scanner.nextBigDecimal();
            scanner.nextLine();
            return valor;
        } catch (Exception e) {
            System.out.println("Insira um número decimal válido! Use (,) como separador decimal");
            scanner.nextLine();
            return getBigDecimal(message);
        }
    }

    public static Ativo getAtivo() {
        return getAtivo("Digite o código do ativo: ");
    }

    public static Ativo getAtivo(String message) {
        AtivoService ativoService = AtivoService.getInstance();

        List<Ativo> ativos = ativoService.listarAtivos();

        System.out.println("Os ativos cadastrados nessa corretora são:");

        ativos.forEach(ativo -> System.out.printf(" - %s (%s): R$ %,.2f\n", ativo.getCodigoAtivo(), ativo.getNomeAtivo(), ativo.getValorAtivo()));

        Optional<Ativo> opt = Optional.empty();
        while (opt.isEmpty()) {
            String codigo = getString(message);

            opt = ativoService.getAtivo(codigo);
        }

        return opt.get();
    }
}
