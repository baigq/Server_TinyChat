package service;

import common.Message;
import common.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Server广播服务类
 */
public class NoticeService implements Runnable{
    private Scanner scanner = new Scanner(System.in);

    /**
     * 广播线程运行方法
     */
    @Override
    public void run() {
        while(true) {
            System.out.println("INPUT NOTICE:(input exit to quit)");
            String notice = scanner.next();

            if(notice.equals("exit"))
                break;

            // 广播报文
            Message message = new Message();
            message.setMsgType(MessageType.MESSAGE_PUBLIC);
            message.setSender("Server");
            message.setContent(notice);
            message.setTime(new Date().toString());

            System.out.println("Server to ALL: " + notice);

            // 获取所有在线用户进行广播
            HashMap<String, ServerConnection> serverConnections =
                    ServerConnectionPool.getServerConnections();
            Iterator<String> iterator = serverConnections.keySet().iterator();

            while (iterator.hasNext()) {
                String uid = iterator.next().toString();
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(
                            serverConnections.get(uid).getClientSocket().getOutputStream());
                    oos.writeObject(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
