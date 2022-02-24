package service;

import common.Message;
import common.MessageType;
import common.User;

import java.sql.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务器端
 */
public class Server {
    // 服务器socket
    private ServerSocket serverSocket = null;
    // 离线数据缓存,用于保存用户登出后接收到的消息
    static ConcurrentHashMap<String, ArrayList<Message>> offlineDB = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        new Server();
    }

    /**
     * 检查用户登录信息
     * @param userID 用户id
     * @param password 用户密码
     * @return 用户登录是否有效
     */
    private boolean check(String userID,String password){
        boolean flag = false;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        // 连接数据库
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/myDatabase?characterEncoding=utf8","root","root");

            String sql = "select * from user where name = " + userID + " and password = " + password;

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            if(rs.next())
                flag = true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            return flag;
        }
    }

    /**
     * Server构造函数
     */
    public Server() {
        System.out.println("Server Listening ...");

        try {
            // Server开始监听端口
            this.serverSocket = new ServerSocket(9999);
            // 开启消息广播线程
            new Thread(new NoticeService()).start();

            while (true){
                // 阻塞,等待新连接建立
                Socket clientSocket = serverSocket.accept();

                // 监听输入流
                ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
                User u = (User)ois.readObject();

                // 建立输出流
                Message message = new Message();
                ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());

                // 如果有效，新建SOCKET线程并存入线程池；否则关闭SOCKET
                if(check(u.getUserID(),u.getPassword())){
                    System.out.println(u.getUserID()+" LOG IN SUCCEED!");

                    // LOGIN SUCCESS 响应
                    message.setMsgType(MessageType.MESSAGE_LOGIN_SUCCEED);
                    oos.writeObject(message);

                    // CREATE A NEW THREAD TO KEEP CONNECTION
                    ServerConnection serverConnection = new ServerConnection(clientSocket, u.getUserID());
                    serverConnection.start();

                    // Add to Thread Pool
                    ServerConnectionPool.addServerConnection(u.getUserID(),serverConnection);
                }else {
                    System.out.println(u.getUserID()+" LOGIN FAILED!");

                    // LOGIN FAILED 响应
                    message.setMsgType(MessageType.MESSAGE_LOGIN_FAILED);
                    oos.writeObject(message);

                    // 关闭连接
                    clientSocket.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                // 关闭服务器socket
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
