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
        tty.executeJavaScript("var a = new Browser();a.operate.loadURL('http://localhost/MAMP')");
    }


    public static void main(String[] args) {
        launch(args);
    }
}
