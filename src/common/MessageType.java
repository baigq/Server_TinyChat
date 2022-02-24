package common;

/**
 * 消息类型
 */
public interface MessageType {
    String MESSAGE_LOGIN_SUCCEED = "1";
    String MESSAGE_LOGIN_FAILED = "2";
    String MESSAGE_COMMON = "3";// 私聊
    String MESSAGE_GET_ONLINE_LIST = "4";
    String MESSAGE_RET_ONLINE_LIST = "5";
    String MESSAGE_CLIENT_EXIT = "6";
    String MESSAGE_PUBLIC = "7";// 群聊
    String MESSAGE_FILE = "8";
}
