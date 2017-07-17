package net.lemonsoft.lwc.core.viewController;

import net.lemonsoft.lwc.core.Tty;

public class CallBackBundle {
    private Object callback;
    private Tty tty;

    public CallBackBundle(Object callback, Tty tty) {
        this.callback = callback;
        this.tty = tty;
    }

    public Object getCallback() {
        return callback;
    }

    public void setCallback(Object callback) {
        this.callback = callback;
    }

    public Tty getTty() {
        return tty;
    }

    public void setTty(Tty tty) {
        this.tty = tty;
    }
}
