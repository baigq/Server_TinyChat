package service;

import common.Message;
import common.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import static service.Server.offlineDB;

/**
 * 服务器端连接类
 */
public class ServerConnection extends Thread{
    // 客户端socket
    private Socket clientSocket;
    // 客户端id
    private String userID;
    private boolean loop = true;

    /**
     * 服务器连接初始化
     * @param clientSocket 客户端连接socket
     * @param userID 客户端id
     */
    public ServerConnection(Socket clientSocket, String userID) {
        this.clientSocket = clientSocket;
        this.userID = userID;
    }

    /**
     * 返回本连接的客户端socket
     * @return 此连接对应的客户端socket
     */
    public Socket getClientSocket() {
        return clientSocket;
    }

    /**
     * 服务端监听方法
     * A THREAD RESPONDING TO A CLIENT
     */
    @Override
    public void run() {
        System.out.println(userID + " Connection established !");

        // offline Function
        if(offlineDB.containsKey(userID)){
            ArrayList<Message> offlineMSG = offlineDB.get(userID);
            ObjectOutputStream oos = null;

            try {
                for(int i = 0;i < offlineMSG.size();i++){
                    // 此处应循环新建输出流（序列化）
                    oos = new ObjectOutputStream(clientSocket.getOutputStream());
                    oos.writeObject(offlineMSG.get(i));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                offlineDB.remove(userID);
            }
        }

        // 服务器监听
        while(loop){
            System.out.println(userID + " Server waiting ...");

            try {
                // 等待读取客户端请求
                ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
                Message msg = (Message) ois.readObject();

                // 请求解析
                switch (msg.getMsgType()){
                    case MessageType.MESSAGE_GET_ONLINE_LIST:
                        System.out.println(userID+" Request Online List");
                        String onlineList = ServerConnectionPool.getOnlineList();

                        // 消息封装
                        Message msg_ret = new Message();
                        msg_ret.setMsgType(MessageType.MESSAGE_RET_ONLINE_LIST);
                        msg_ret.setContent(onlineList);
                        msg_ret.setReceiver(msg.getSender());

                        // 消息发送
                        ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
                        oos.writeObject(msg_ret);

                        break;
                    case MessageType.MESSAGE_CLIENT_EXIT:
                        System.out.println(userID+" is going to exit.");

                        // 线程池中删除对应服务器连接
                        ServerConnectionPool.delServerConnection(userID);
                        clientSocket.close();

                        // 结束该线程
                        loop = false;
                        break;
                    case MessageType.MESSAGE_COMMON: {
                        String rx = msg.getReceiver();

                        // 离线接收功能
                        if (!ServerConnectionPool.getServerConnections().containsKey(rx)) {
                            if (offlineDB.containsKey(rx)) {
                                offlineDB.get(rx).add(msg);
                            } else {
                                offlineDB.put(rx, new ArrayList<Message>(Arrays.asList(msg)));
                            }
                            System.out.println("offline msg: "+msg.getSender()+" ---> "+msg.getReceiver());
                        } else {
                            ServerConnection serverConnection =
                                    ServerConnectionPool.getServerConnection(msg.getReceiver());
                            ObjectOutputStream oos1 = new ObjectOutputStream(
                                    serverConnection.clientSocket.getOutputStream());
                            oos1.writeObject(msg);
                        }

                        break;
                    }
                    case MessageType.MESSAGE_PUBLIC:
                        HashMap<String, ServerConnection> serverConnections = ServerConnectionPool.getServerConnections();
                        Iterator<String> iterator = serverConnections.keySet().iterator();

                        while (iterator.hasNext()){
                            String uid = iterator.next().toString();

                            // 排除自己,发送消息
                            if(!uid.equals(msg.getSender())){
                                ObjectOutputStream oos2 = new ObjectOutputStream(
                                        serverConnections.get(uid).clientSocket.getOutputStream());
                                oos2.writeObject(msg);
                            }
                        }

                        break;
                    case MessageType.MESSAGE_FILE: {
                        String rx = msg.getReceiver();

                        // 离线接收功能
                        if (!ServerConnectionPool.getServerConnections().containsKey(rx)) {
                            if (offlineDB.containsKey(rx)) {
                                offlineDB.get(rx).add(msg);
                            } else {
                                offlineDB.put(rx, new ArrayList<Message>(Arrays.asList(msg)));
                            }
                            System.out.println("offline File: "+msg.getSender()+" ---> "+msg.getReceiver());
                        } else {
                            ObjectOutputStream oos3 = new ObjectOutputStream(
                                    ServerConnectionPool.getServerConnection(msg.getReceiver()).clientSocket.getOutputStream());
                            oos3.writeObject(msg);
                        }

                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println(userID + " Connection CLOSED!...");
    }

}
