package net.lemonsoft.lwc.core;

import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.util.UUID;

/**
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

    private Browser(){
        super();
    };

    protected Browser(SubController belongSubController){
        super();
        setTitle("New Blank Browser");
        this.belongSubController = belongSubController;
        this.id = UUID.randomUUID().toString();
        this.webView = new WebView();
        this.rootScene = new Scene(webView);
        this.setScene(rootScene);
    }

    public String getId(){
        return this.id;
    }

    /**
     * 在浏览器中执行js代码
     * @param jsCode 要在浏览器中执行的js代码
     * @return 代码执行的返回值
     */
    public Object executeJavaScript(String jsCode){
        return webView.getEngine().executeScript(jsCode);
    }

    /**
     * 在浏览器中执行命令对象
     * @param command 要执行的浏览器命令对象
     * @return 命令执行的返回值
     */
    public Object executeBrowserCommand(BrowserCommand command){
        return executeJavaScript(command.getJavaScriptCode());
    }

}
