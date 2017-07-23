package net.lemonsoft.lwc;

import javafx.application.Application;
import javafx.stage.Stage;
import net.lemonsoft.lwc.core.MainManager;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
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
