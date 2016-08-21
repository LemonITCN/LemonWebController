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
        Tty tty = controller.getConsole().createATty("myTTY");
        tty.executeJavaScript("var a = new Browser();a.operate.loadURL('http://localhost/MAMP');console.log(a.dataGet.getImgDomURL('#myCarousel > div > div.item.carousel-back-green.active > div > div > table > tbody > tr > td:nth-child(1) > img'));");
    }


    public static void main(String[] args) {
        launch(args);
    }
}
