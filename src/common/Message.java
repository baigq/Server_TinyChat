package common;

import java.io.Serializable;

/**
 * 消息封装类
 */
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 发送者标识
     */
    private String sender;

    /**
     * 接收者标识
     */
    private String receiver;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 发送时间
     */
    private String time;

    /**
     * 消息类型
     */
    private String msgType;

    /**
     * 文件大小
     */
    private byte[] fileBytes;

    /**
     * 文件源地址
     */
    private String src;

    /**
     * 文件目的地址
     */
    private String destination;

    /**
     * 返回文件大小
     * @return 字节数
     */
    public byte[] getFileBytes() {
        return fileBytes;
    }

    /**
     * 设置文件大小
     * @param fileBytes 文件字节数
     */
    public void setFileBytes(byte[] fileBytes) {
        this.fileBytes = fileBytes;
    }

    /**
     * 设置文件源地址
     * @param src 源地址
     */
    public void setSrc(String src) {
        this.src = src;
    }

    /**
     * 返回文件发送目的地址
     * @return 发送目的地址
     */
    public String getDestination() {
        return destination;
    }

    /**
     * 设置文件发送目的地址
     * @param destination 发送目的地址
     */
    public void setDestination(String destination) {
        this.destination = destination;
    }

    /**
     * 设置消息类型
     * @param msgType 消息类型
     */
    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    /**
     * 返回消息类型
     * @return 消息类型
     */
    public String getMsgType() {
        return msgType;
    }

    /**
     * 返回发送者
     * @return 发送者标识
     */
    public String getSender() {
        return sender;
    }

    /**
     * 设置发送者
     * @param sender 发送者标识
     */
    public void setSender(String sender) {
        this.sender = sender;
    }

    /**
     * 返回接收者
     * @return 接收者标识
     */
    public String getReceiver() {
        return receiver;
    }

    /**
     * 设置接收者
     * @param receiver 接收者标识
     */
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    /**
     * 获取消息内容
     * @return 消息内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置消息内容
     * @param content 消息内容
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 设置发送时间
     * @param time 发送时间
     */
    public void setTime(String time) {
        this.time = time;
    }
}
