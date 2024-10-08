package fiap.com.application;

import fiap.com.application.menu.Menu;
import fiap.com.model.Carteira;
import fiap.com.model.Conta;

import java.util.Optional;

public class ServiceContext {
    private static ServiceContext instance = null;
    private Conta conta;
    private Menu menu;
    private Carteira carteira;

    public static ServiceContext getInstance() {
        if (instance == null) {
            instance = new ServiceContext();
        }
        return instance;
    }

    private ServiceContext() {
    }

    public boolean isAuthenticated() {
        return conta != null;
    }

    public void login(Conta conta) {
        this.conta = conta;
        this.carteira = new Carteira(conta);
    }

    public void logout() {
        this.conta = null;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public Menu getMenu() {
        return menu;
    }

    public Optional<Carteira> getCarteira() {
        return Optional.ofNullable(carteira);
    }

    public Optional<Conta> getConta() {
        return Optional.ofNullable(conta);
    }
}
