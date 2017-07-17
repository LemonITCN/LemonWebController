package net.lemonsoft.lwc.core.viewController;

import com.teamdev.jxbrowser.chromium.*;
import com.teamdev.jxbrowser.chromium.events.*;
import com.teamdev.jxbrowser.chromium.javafx.BrowserView;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.*;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import net.lemonsoft.lwc.core.BrowserCommand;
import net.lemonsoft.lwc.core.SubController;
import net.lemonsoft.lwc.core.Tty;
import netscape.javascript.JSObject;

import java.util.HashMap;
import java.util.UUID;

/**
 * 浏览器对象
 * Created by lemonsoft on 2016/8/14.
 */
public class BrowserViewController extends Stage {

    /**
     * WebView控件
     */
    private BrowserView browserView;
    private Browser browser;
    private Scene rootScene;
    private String id;
    private SubController belongSubController;// 归属于的子控制器

    private BrowserConsoleCommand browserConsoleCommand;
    private BrowserCallback browserCallback;

    private LoadURLHandler loadURLHandler;

    // JS回调函数 - 浏览器关闭
    private CallBackBundle onClose;
    // JS回调函数 - 浏览器加载URL成功
    private CallBackBundle onLoadSuccess;
    // JS回调函数 - 浏览器加载URL失败
    private CallBackBundle onLoadFailed;
    // JS回调函数 - 自定义回调池
    private HashMap<String, CallBackBundle> customCallbackPool = new HashMap<>();

    private BrowserViewController() {
        super();
    }

    public BrowserCallback getBrowserCallback() {
        if (browserCallback == null)
            browserCallback = new BrowserCallback();
        return browserCallback;
    }

    public BrowserViewController(SubController belongSubController) {
        setTitle("New Blank BrowserViewController");
        this.setWidth(640);
        this.setHeight(520);
        this.belongSubController = belongSubController;
        this.id = UUID.randomUUID().toString();
        this.browserView = new BrowserView();
        this.browser = this.browserView.getBrowser();
        this.browser.setPopupHandler(new PopupHandler() {
            @Override
            public PopupContainer handlePopup(PopupParams popupParams) {
                browser.loadURL(popupParams.getURL());
                return null;
            }
        });
        this.rootScene = new Scene(browserView);
        this.setScene(rootScene);
        browserConsoleCommand = new BrowserConsoleCommand();
        this.browser.addScriptContextListener(new ScriptContextAdapter() {
            @Override
            public void onScriptContextCreated(ScriptContextEvent event) {
                super.onScriptContextCreated(event);
                loadCompleteDeal();
            }
        });
        this.browser.addLoadListener(new LoadAdapter() {
            @Override
            public void onFinishLoadingFrame(FinishLoadingEvent event) {
                super.onFinishLoadingFrame(event);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if (loadURLHandler != null)
                            loadURLHandler.loadSuccess();
                        if (onLoadSuccess != null)
                            onLoadSuccess.getTty().executeJavaScript(String.format("(%s)('%s')", onLoadSuccess.getCallback(), browser.getURL()));
                    }
                });
            }

            @Override
            public void onFailLoadingFrame(FailLoadingEvent event) {
                super.onFailLoadingFrame(event);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if (loadURLHandler != null)
                            loadURLHandler.loadFailed();
                        if (onLoadFailed != null)
                            onLoadFailed.getTty().executeJavaScript(String.format("(%s)('%s')", onLoadFailed.getCallback(), browser.getURL()));
                    }
                });
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
        final boolean[] m_press = {false};
        final boolean[] d_press = {false};
        final boolean[] c_press = {false};
        // 监听键盘的快捷键组合
        // shift+s shift+m shift+m shift+d shift+c
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
                    case M:
                        m_press[0] = true;
                        break;
                    case D:
                        d_press[0] = true;
                        break;
                    case C:
                        c_press[0] = true;
                        break;
                    default:
                }
                if (shift_press[0] && s_press[0])
                    belongSubController.getGUIStage().show();// 触发shift-s执行代码
                if (shift_press[0] && m_press[0])
                    belongSubController.getBelongMainManager().getGUIStage().show();// 触发shift-d执行代码
                if (shift_press[0] && d_press[0])
                    belongSubController.showDataCollectionGUI();
                if (shift_press[0] && c_press[0])
                    belongSubController.getConsole().show();
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
                    case M:
                        m_press[0] = false;
                        break;
                    case D:
                        d_press[0] = false;
                        break;
                    case C:
                        c_press[0] = false;
                        break;
                    default:
                }
            }
        });
    }

    public void loadCompleteDeal() {
        try {
            loadURLHandler = null;
            belongSubController.refreshGUI();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    setTitle(String.format("[%s] - %s", id, (String) browser.getTitle()));
                }
            });
            JSValue value = browser.executeJavaScriptAndReturnValue("window");
            value.asObject().setProperty("console", browserConsoleCommand);
            // 监听浏览器激活回调函数
            value.asObject().setProperty("callback", getBrowserCallback());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        return executeJavaScript("location.href").toString();
    }

    /**
     * 获取当前浏览器的标题
     *
     * @return 当前浏览器应显示的标题
     */
    public String getCurrentTitle() {
        return executeJavaScript("document.title").toString();
    }

    /**
     * 在浏览器中执行js代码
     *
     * @param jsCode 要在浏览器中执行的js代码
     * @return 代码执行的返回值
     */
    public Object executeJavaScript(String jsCode) {
        JSValue result = browser.executeJavaScriptAndReturnValue(jsCode);
        if (result instanceof JSString)
            return result.getStringValue();
        return browser.executeJavaScriptAndReturnValue(jsCode);
    }

    /**
     * 加载URL
     *
     * @param url            要加载的指定的url
     * @param loadURLHandler 加载的回调机制
     */
    public void loadUrl(String url, LoadURLHandler loadURLHandler) {
        this.loadURLHandler = loadURLHandler;
        this.browser.stop();
        this.browser.loadURL(url);
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

    @Override
    public void close() {
        super.close();
        if (onClose != null)
            onClose.getTty().executeJavaScript(String.format("(%s)()", onClose.getCallback()));
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
            String out = String.format("[BrowserViewController Log] %s", info);
            System.out.println(out);
            if (belongSubController.viewController != null) {
                belongSubController.consoleOut(getId(), out);
            }
        }

        public void result(String info) {
            String out = String.format("[BrowserViewController executeResult] %s", info);
            System.out.println(out);
            if (belongSubController.viewController != null) {
                belongSubController.consoleOut(getId(), out);
            }
        }

        public void error(String info) {
            String out = String.format("[BrowserViewController Err] %s", info);
            System.err.println(out);
            if (belongSubController.viewController != null) {
                belongSubController.consoleOut(getId(), out);
            }
        }
    }

    /**
     * 回调函数原生JS调用接口
     */
    public class BrowserCallback {
        /**
         * 激活指定的回调函数
         *
         * @param key
         * @param params
         */
        public void invoke(String key, String params) {
            System.out.println("一个回调被激活：" + key + " + pararms = " + params);
            invokeCustomCallback(key, params);
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

    public void setOnClose(CallBackBundle onClose) {
        this.onClose = onClose;
    }

    public void setOnLoadSuccess(CallBackBundle onLoadSuccess) {
        this.onLoadSuccess = onLoadSuccess;
    }

    public void setOnLoadFailed(CallBackBundle onLoadFailed) {
        this.onLoadFailed = onLoadFailed;
    }

    public void putCustomCallback(String key, CallBackBundle callBackBundle) {
        customCallbackPool.put(key, callBackBundle);
    }

    public void invokeCustomCallback(String key, String params) {
        if (customCallbackPool.containsKey(key))
            customCallbackPool.get(key).getTty().executeJavaScript(String.format("(%s)(" + params + ")", customCallbackPool.get(key).getCallback()));
    }

}

