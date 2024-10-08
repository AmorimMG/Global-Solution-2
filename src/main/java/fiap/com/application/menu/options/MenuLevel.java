package fiap.com.application.menu.options;

public enum MenuLevel {
    HOME(false), // Login, Cadastro de conta e logout
    ADMIN(true), // Gerenciamento de ativos
    BROKER(true), // Compra, venda e visualização de histórico (apenas user logado)
    BROKER_ACCOUNT(true),
    BROKER_ACTIVE(true);

    private final boolean authOnly;

    MenuLevel(boolean authOnly) {
        this.authOnly = authOnly;
    }

    public boolean isAuthOnly() {
        return authOnly;
    }
}
