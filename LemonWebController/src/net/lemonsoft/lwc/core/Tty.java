package net.lemonsoft.lwc.core;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.scene.web.WebView;
import net.lemonsoft.lwc.core.ttyCommand.BrowserManageCommand;
import net.lemonsoft.lwc.core.ttyCommand.DataCollectionCommand;
import netscape.javascript.JSObject;

import java.io.*;
import java.util.UUID;

/**
 * Tty - 控制台单位
 * Created by LiuRi on 16/8/20.
 */
public class Tty {

    private String id;
    private String name;
    private WebView container;
    private SubController belongSubController;

    private static StringBuilder systemCommandJSCode;

    public Tty(String name, SubController controller) {
        super();
        if (systemCommandJSCode == null) {
            systemCommandJSCode = new StringBuilder();
            File file = new File(getClass().getResource("").toString().substring(5) + "systemCommand/SystemCommand.min.js");
            if (file.isFile() && file.exists()) {
                try {
                    InputStreamReader reader = new InputStreamReader(new FileInputStream(file), "UTF-8");
                    BufferedReader bufferedReader = new BufferedReader(reader);
                    String line = null;
                    while ((line = bufferedReader.readLine()) != null) {
                        systemCommandJSCode.append(line);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        Tty self = this;
        this.belongSubController = controller;
        this.id = UUID.randomUUID().toString();
        this.name = name;
        container = new WebView();
        container.getStyleClass().add("ttyWebView");
        JSObject window = (JSObject) container.getEngine().executeScript("window");

        TtyExecute ttyExecute = new TtyExecute(container);
        BrowserManageCommand manageCommand = new BrowserManageCommand(belongSubController , this);
        DataCollectionCommand collectionCommand = new DataCollectionCommand(belongSubController);

        window.setMember("execute", ttyExecute);
        window.setMember("browser", manageCommand);
        window.setMember("dataCollection" , collectionCommand);
        container.getEngine().executeScript(systemCommandJSCode.toString());
        container.getEngine().getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
            @Override
            public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
                window.setMember("execute", ttyExecute);
                window.setMember("browser", manageCommand);
                window.setMember("dataCollection" , collectionCommand);
                container.getEngine().executeScript(systemCommandJSCode.toString());
            }
        });
        container.getEngine().load(getClass().getResource("") + "layout/SubControllerConsoleTty.html");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public WebView getContainer() {
        return container;
    }

    /**
     * 让当前TTY执行指定的js代码
     *
     * @param jsCode 要执行的js代码
     * @return js代码执行之后返回的结果数据
     */
    public Object executeJavaScript(String jsCode) {
        return this.container.getEngine().executeScript(jsCode);
    }

    public class TtyExecute {

        private WebView container;

        public TtyExecute(WebView container) {
            super();
            this.container = container;
        }

        public Object execute(String command) {
            Object result = container.getEngine().executeScript(command).toString();
            return result;
        }
    }

    public interface TtyLoadHandler {
        void ttyLoadFinish(Tty tty);
    }
}
