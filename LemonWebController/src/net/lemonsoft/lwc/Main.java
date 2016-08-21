package net.lemonsoft.lwc;

import javafx.application.Application;
import javafx.stage.Stage;
import net.lemonsoft.lwc.core.MainManager;
import net.lemonsoft.lwc.core.SubController;
import net.lemonsoft.lwc.core.Tty;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        MainManager manager = MainManager.defaultManager();
        SubController controller = manager.createSubController();
//        manager.getGUIStage().show();
        Tty tty = controller.getConsole().getDefaultTty();
        tty.executeJavaScript("var a = new Browser();a.show();a.operate.loadURL('http://www.taobao.com' , function(){console.log(a.dataGet.getImgDomURL('body > div.cup.J_Cup > div.screen.J_Screen > div.sa-rect > div > div.core.J_Core > div.ca-extra > div.tbh-member.J_Module.tbh-loaded.member-bg-default > div > div.member-bd > a > img'));} , function(){console.log('failed');});");
        System.out.println(controller.getDataCollectionPool());
    }


    public static void main(String[] args) {
        launch(args);
    }
}
