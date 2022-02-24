package service;

import java.util.HashMap;
import java.util.Iterator;

/**
 * 服务端连接线程池
 */
public class ServerConnectionPool {
    /**
     * HashMap 模拟线程池
     */
    private static HashMap<String,ServerConnection> serverConnections = new HashMap<>();

    /**
     * 返回所有在线线程
     * @return 线程池
     */
    public static HashMap<String, ServerConnection> getServerConnections() {
        return serverConnections;
    }

    /**
     * Server线程加入线程池
     * @param userID 用户id
     * @param serverConnection 对应的服务器端连接
     */
    public static void addServerConnection(String userID, ServerConnection serverConnection){
        serverConnections.put(userID,serverConnection);
    }

    /**
     * 根据用户id获取连接
     * @param userID 用户id
     * @return 对应的Server连接线程
     */
    public static ServerConnection getServerConnection(String userID){
        return serverConnections.get(userID);
    }

    /**
     * 根据用户id注销连接
     * @param uid 用户id
     */
    public static void delServerConnection(String uid){
        serverConnections.remove(uid);
    }

    /**
     * 返回在线用户列表
     * @return 在线用户列表
     */
    public static String getOnlineList(){
        Iterator<String> iterator = serverConnections.keySet().iterator();
        String list = "";
        while(iterator.hasNext())
            list += iterator.next().toString() + " ";
        return list;
    }
}
