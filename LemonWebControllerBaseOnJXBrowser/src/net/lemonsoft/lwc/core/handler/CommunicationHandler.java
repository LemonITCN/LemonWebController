package net.lemonsoft.lwc.core.handler;

/**
 * TTY和外部通信handler
 * Created by lemonsoft on 2016/8/23.
 */
public interface CommunicationHandler {

    /**
     * 消息方法
     * @param data 携带的数据
     */
    Object onMessage(Object data);

}
