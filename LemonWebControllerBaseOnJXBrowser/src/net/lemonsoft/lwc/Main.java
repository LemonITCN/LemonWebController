package net.lemonsoft.lwc;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.BrowserCore;
import com.teamdev.jxbrowser.chromium.internal.Environment;
import com.teamdev.jxbrowser.chromium.javafx.BrowserView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import net.lemonsoft.lwc.core.MainManager;

public class Main extends Application {

    @Override
    public void init() throws Exception {
        // On Mac OS X Chromium engine must be initialized in non-UI thread.
        if (Environment.isMac()) {
            BrowserCore.initialize();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Platform.setImplicitExit(false);
        MainManager manager = MainManager.defaultManager();
        manager.getGUIStage().show();

//        SubController controller = manager.createSubController();
//        Tty tty = controller.getConsole().getDefaultTty();
//        tty.executeJavaScript("var a = new BrowserViewController();a.show();a.operate.loadURL('http://www.taobao.com' , function(){DataCollection.put('imgSrc',a.dataGet.getImgDomURL('body > div.cup.J_Cup > div.screen.J_Screen > div.sa-rect > div > div.core.J_Core > div.ca-extra > div.tbh-member.J_Module.tbh-loaded.member-bg-default > div > div.member-bd > a > img'));} , function(){console.log('failed');});");
//        System.out.println(controller.getDataCollectionPool());



    }

    public static void main(String[] args) {
        launch(args);
    }
}
