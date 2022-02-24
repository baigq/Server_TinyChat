package common;

import java.io.Serializable;

/**
 * 用户类
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    private String userID;

    /**
     * 用户密码
     */
    private String password;

    public User(){}

    /**
     * 返回用户id
     * @return 用户id
     */
    public String getUserID() {
        return userID;
    }

    /**
     * 设置用户id
     * @param userID 用户id
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }

    /**
     * 设置用户密码
     * @param password 用户密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取用户密码
     * @return 用户密码
     */
    public String getPassword() {
        return password;
    }
}
