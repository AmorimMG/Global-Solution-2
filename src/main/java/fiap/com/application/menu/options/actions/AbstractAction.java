package fiap.com.application.menu.options.actions;

import fiap.com.exception.UnauthorizedException;

public abstract class AbstractAction {
    protected abstract void action() throws UnauthorizedException;

    public void execute() throws UnauthorizedException {
        action();
    }
}
