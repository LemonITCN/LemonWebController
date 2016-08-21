package net.lemonsoft.lwc.core;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

import java.util.UUID;

/**
 * 浏览器对象
 * Created by lemonsoft on 2016/8/14.
 */
public class Browser extends Stage {

    /**
     * WebView控件
     */
    private WebView webView;
    private Scene rootScene;
    private String id;
    private SubController belongSubController;// 归属于的子控制器

    private BrowserConsoleCommand browserConsoleCommand;

    private LoadURLHandler loadURLHandler;

    private Browser() {
        super();
    }

    protected Browser(SubController belongSubController) {
        super();
        setTitle("New Blank Browser");
        this.belongSubController = belongSubController;
        this.id = UUID.randomUUID().toString();
        this.webView = new WebView();
        this.rootScene = new Scene(webView);
        this.setScene(rootScene);
        browserConsoleCommand = new BrowserConsoleCommand();
        this.webView.getEngine().getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
            @Override
            public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
                if (newValue == Worker.State.SUCCEEDED) {
                    if (loadURLHandler != null)
                        loadURLHandler.loadSuccess();
                }
                if (newValue == Worker.State.FAILED) {
                    if (loadURLHandler != null)
                        loadURLHandler.loadFailed();
                }
                try {
                    belongSubController.refreshGUI();
                    setTitle(String.format("[%s] - %s", id, (String) webView.getEngine().executeScript("document.title")));
                    JSObject window = (JSObject) webView.getEngine().executeScript("window");
                    window.setMember("console", browserConsoleCommand);// 设置控制台命令
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.showingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                belongSubController.refreshGUI();
            }
        });
        final boolean[] shift_press = {false};
        final boolean[] s_press = {false};
        this.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                KeyCode code = event.getCode();
                switch (code) {
                    case SHIFT:
                        shift_press[0] = true;
                        break;
                    case S:
                        s_press[0] = true;
                        break;
                    default:
                }
                if (shift_press[0] && s_press[0])
                    belongSubController.getGUIStage().show();// 触发shift-s执行代码
            }
        });
        this.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                KeyCode code = event.getCode();
                switch (code) {
                    case SHIFT:
                        shift_press[0] = false;
                        break;
                    case S:
                        s_press[0] = false;
                        break;
                    default:
                }
            }
        });
    }

    /**
     * 获取当前浏览器的标识符id
     *
     * @return 当前浏览器的标识符id
     */
    public String getId() {
        return this.id;
    }

    /**
     * 获取浏览器加载的URL
     *
     * @return 当前浏览器加载的url
     */
    public String getCurrentUrl() {
        String result = executeJavaScript("location.href").toString();
        return result;
    }

    /**
     * 获取当前浏览器的标题
     *
     * @return 当前浏览器应显示的标题
     */
    public String getCurrentTitle() {
        String result = executeJavaScript("document.title").toString();
        return result;
    }

    /**
     * 在浏览器中执行js代码
     *
     * @param jsCode 要在浏览器中执行的js代码
     * @return 代码执行的返回值
     */
    public Object executeJavaScript(String jsCode) {
        Object result = webView.getEngine().executeScript(jsCode);
        if (result != null)
            browserConsoleCommand.result(result.toString());
        return result;
    }

    /**
     * 加载URL
     *
     * @param url            要加载的指定的url
     * @param loadURLHandler 加载的回调机制
     */
    public void loadUrl(String url, LoadURLHandler loadURLHandler) {
        this.loadURLHandler = loadURLHandler;
        this.webView.getEngine().load(url);
    }

    /**
     * 设置当前浏览器窗口是否显示
     *
     * @param isShow 设置浏览器窗口是否显示的布尔值
     */
    public void setShowOrHide(boolean isShow) {
        if (isShow)
            this.show();
        else
            this.hide();
    }

    /**
     * 在浏览器中执行命令对象
     *
     * @param command 要执行的浏览器命令对象
     * @return 命令执行的返回值
     */
    public Object executeBrowserCommand(BrowserCommand command) {
        return executeJavaScript(command.getJavaScriptCode());
    }

    public class BrowserConsoleCommand {
        public void log(String info) {
            String out = String.format("[Browser Log] %s", info);
            System.out.println(out);
            if (belongSubController.viewController != null) {
                belongSubController.consoleOut(getId(), out);
            }
        }

        public void result(String info) {
            String out = String.format("[Browser executeResult] %s", info);
            System.out.println(out);
            if (belongSubController.viewController != null) {
                belongSubController.consoleOut(getId(), out);
            }
        }

        public void error(String info) {
            String out = String.format("[Browser Err] %s", info);
            System.err.println(out);
            if (belongSubController.viewController != null) {
                belongSubController.consoleOut(getId(), out);
            }
        }
    }

    /**
     * 加载URL的机制
     */
    public interface LoadURLHandler {

        /**
         * 加载成功
         */
        void loadSuccess();

        /**
         * 加载失败
         */
        void loadFailed();
    }

}
