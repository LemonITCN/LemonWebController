package net.lemonsoft.lwc.core;

import com.teamdev.jxbrowser.chromium.JSObject;
import com.teamdev.jxbrowser.chromium.JSValue;
import com.teamdev.jxbrowser.chromium.events.FinishLoadingEvent;
import com.teamdev.jxbrowser.chromium.events.LoadAdapter;
import com.teamdev.jxbrowser.chromium.events.ScriptContextAdapter;
import com.teamdev.jxbrowser.chromium.events.ScriptContextEvent;
import com.teamdev.jxbrowser.chromium.javafx.BrowserView;
import net.lemonsoft.lwc.core.ttyCommand.BrowserManageCommand;
import net.lemonsoft.lwc.core.ttyCommand.CommunicationCommand;
import net.lemonsoft.lwc.core.ttyCommand.DataCollectionCommand;
import net.lemonsoft.lwc.core.ttyCommand.LogCommand;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;

/**
 * Tty - 控制台单位
 * Created by LiuRi on 16/8/20.
 */
public class Tty {

    private String id;
    private String name;
    private BrowserView container;
    private SubController belongSubController;

    private static StringBuilder systemCommandJSCode;

    public Tty(String name, SubController controller) {
        super();
        if (systemCommandJSCode == null) {
            systemCommandJSCode = new StringBuilder();
            InputStream inputStream = getClass().getResourceAsStream("systemCommand/SystemCommand.min.js");
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    systemCommandJSCode.append(line);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.belongSubController = controller;
        this.id = UUID.randomUUID().toString();
        this.name = name;
        container = new BrowserView();
        container.getStyleClass().add("ttyWebView");
        JSObject window = container.getBrowser().executeJavaScriptAndReturnValue("window").asObject();

        TtyExecute ttyExecute = new TtyExecute(container);
        BrowserManageCommand manageCommand = new BrowserManageCommand(belongSubController, this);
        DataCollectionCommand collectionCommand = new DataCollectionCommand(belongSubController);
        LogCommand logCommand = new LogCommand(belongSubController);
        CommunicationCommand communicationCommand = new CommunicationCommand(belongSubController);

        window.setProperty("execute", ttyExecute);
        window.setProperty("browser", manageCommand);
        window.setProperty("dataCollection", collectionCommand);
        window.setProperty("log", logCommand);
        window.setProperty("communication", communicationCommand);
        container.getBrowser().executeJavaScriptAndReturnValue(systemCommandJSCode.toString());
        container.getBrowser().addScriptContextListener(new ScriptContextAdapter() {
            @Override
            public void onScriptContextCreated(ScriptContextEvent event) {
                super.onScriptContextCreated(event);
                JSObject window = event.getBrowser().executeJavaScriptAndReturnValue("window").asObject();
                window.setProperty("execute", ttyExecute);
                window.setProperty("browser", manageCommand);
                window.setProperty("dataCollection", collectionCommand);
                window.setProperty("log", logCommand);
                window.setProperty("communication", communicationCommand);
                container.getBrowser().executeJavaScript(systemCommandJSCode.toString());
            }
        });
        container.getBrowser().loadURL(getClass().getResource("") + "layout/SubControllerConsoleTty.html");
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

    public BrowserView getContainer() {
        return container;
    }

    /**
     * 让当前TTY执行指定的js代码
     *
     * @param jsCode 要执行的js代码
     * @return js代码执行之后返回的结果数据
     */
    public JSValue executeJavaScript(String jsCode) {
        return this.container.getBrowser().executeJavaScriptAndReturnValue(jsCode);
    }

    public class TtyExecute {

        private BrowserView container;

        public TtyExecute(BrowserView container) {
            super();
            this.container = container;
        }

        public JSValue execute(String command) {
            JSValue result = container.getBrowser().executeJavaScriptAndReturnValue(command);
            return result;
        }
    }

    public interface TtyLoadHandler {
        void ttyLoadFinish(Tty tty);
    }
}
