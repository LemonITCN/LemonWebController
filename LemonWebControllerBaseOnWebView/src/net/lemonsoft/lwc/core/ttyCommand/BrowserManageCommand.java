package net.lemonsoft.lwc.core.ttyCommand;

import javafx.application.Platform;
import net.lemonsoft.lwc.core.SubController;
import net.lemonsoft.lwc.core.Tty;
import net.lemonsoft.lwc.core.viewController.BrowserViewController;

/**
 * 浏览器管理命令
 * window.browser
 * Created by lemonsoft on 2016/8/19.
 */
public class BrowserManageCommand {

    private SubController belongController;

    private Tty belongTty;

    public BrowserManageCommand(SubController belongController, Tty tty) {
        super();
        this.belongController = belongController;
        this.belongTty = tty;
    }

    /**
     * 创建一个浏览器
     *
     * @return 创建一个浏览器
     */
    public String create() {
        return belongController.createBrowser();
    }

    /**
     * 显示指定的浏览器
     *
     * @param browserId 要显示的浏览器的ID
     */
    public void show(String browserId) {
        belongController.getBrowserById(browserId).show();
    }

    /**
     * 隐藏指定的浏览器
     *
     * @param browserId 要隐藏的浏览器的ID
     */
    public void hide(String browserId) {
        belongController.getBrowserById(browserId).hide();
    }

    /**
     * 关闭这个浏览器
     *
     * @param browserId 要关闭的浏览器的id
     */
    public void close(String browserId) {
        belongController.closeBrowserById(browserId);
    }

    /**
     * 设置浏览器的窗口尺寸
     *
     * @param browserId 要设置的浏览器的id
     * @param width     浏览器的宽度
     * @param height    浏览器的高度
     */
    public void setSize(String browserId, double width, double height) {
        belongController.getBrowserById(browserId).setWidth(width);
        belongController.getBrowserById(browserId).setHeight(height);
    }

    /**
     * 设置浏览器的位置信息
     *
     * @param browserId 要设置位置的浏览器id
     * @param x         要设置成的x坐标
     * @param y         要设置成的y坐标
     */
    public void setPosition(String browserId, double x, double y) {
        BrowserViewController browser = belongController.getBrowserById(browserId);
        browser.setX(x);
        browser.setY(y);
    }

    /**
     * 让指定的浏览器执行指定的js代码
     *
     * @param browserId 要执行命令的浏览器的id
     * @param jsCode    要执行的js代码
     */
    public Object executeJavaScript(String browserId, String jsCode) {
        return belongController.getBrowserById(browserId).executeJavaScript(jsCode);
    }

    /**
     * 让浏览器加载指定的URL
     *
     * @param browserId       要加载URL的id
     * @param url             要加载的URL
     * @param successCallback 加载成功的回调函数
     * @param failedCallback  加载失败的回调函数
     */
    public void loadURL(String browserId, String url, Object successCallback, Object failedCallback) {
        BrowserViewController browser = belongController.getBrowserById(browserId);
        browser.loadUrl(url, new BrowserViewController.LoadURLHandler() {
            @Override
            public void loadSuccess() {
                System.out.println("SUCCESS");
//                System.out.println(String.format("var _lk_success_c = %s();_lk_success_c=null;", successCallback));
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Object result = belongTty.executeJavaScript(String.format("var _lk_success_c = %s();_lk_success_c=null;", successCallback));
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });

            }

            @Override
            public void loadFailed() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        belongTty.executeJavaScript(String.format("var _lk_failed_c = %s();_lk_failed_c=null;", failedCallback));
                    }
                });
            }
        });
    }

}
