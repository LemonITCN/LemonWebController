package net.lemonsoft.lwc.core.ttyCommand;

import net.lemonsoft.lwc.core.Browser;
import net.lemonsoft.lwc.core.SubController;

/**
 * 浏览器管理命令
 * window.browser
 * Created by lemonsoft on 2016/8/19.
 */
public class BrowserManageCommand {

    private SubController controller;

    public BrowserManageCommand(SubController controller){
        super();
        this.controller = controller;
    }

    /**
     * 创建一个浏览器
     * @return 创建一个浏览器
     */
    public String create(){
        return controller.createBrowser();
    }

    /**
     * 显示指定的浏览器
     * @param browserId 要显示的浏览器的ID
     */
    public void show(String browserId){
        controller.getBrowserById(browserId).show();
    }

    /**
     * 隐藏指定的浏览器
     * @param browserId 要隐藏的浏览器的ID
     */
    public void hide(String browserId){
        controller.getBrowserById(browserId).hide();
    }

    /**
     * 关闭这个浏览器
     * @param browserId 要关闭的浏览器的id
     */
    public void close(String browserId){
        controller.closeBrowserById(browserId);
    }

    /**
     * 设置浏览器的窗口尺寸
     * @param browserId 要设置的浏览器的id
     * @param width 浏览器的宽度
     * @param height 浏览器的高度
     */
    public void setSize(String browserId , double width , double height){
        controller.getBrowserById(browserId).setWidth(width);
        controller.getBrowserById(browserId).setHeight(height);
    }

    /**
     * 设置浏览器的位置信息
     * @param browserId 要设置位置的浏览器id
     * @param x 要设置成的x坐标
     * @param y 要设置成的y坐标
     */
    public void setPosition(String browserId , double x ,double y){
        Browser browser = controller.getBrowserById(browserId);
        browser.setX(x);
        browser.setY(y);
    }

    /**
     * 让指定的浏览器执行指定的js代码
     * @param browserId 要执行命令的浏览器的id
     * @param jsCode 要执行的js代码
     */
    public void executeJavaScript(String browserId , String jsCode){
        controller.getBrowserById(browserId).executeJavaScript(jsCode);
    }

}
