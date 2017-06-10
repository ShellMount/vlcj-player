package uk.co.caprica.vlcjplayer.client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;


// SOCKET 客户端
public class RemoteClient {
    public static void main (String[] args) {
        String host = "localhost";
        int port = 1234;

        try {
            InetAddress addr;
            Socket client =new Socket(host, port);
            addr = client.getInetAddress();
            System.out.println("连接到 " + host + ":" + port + " ----> " + addr);

            // 发送信息
            String command = "next";
            OutputStream OS = client.getOutputStream();
            DataOutputStream sender = new DataOutputStream(OS);
            sender.writeUTF(command);

            // 收到回复
            InputStream IS = client.getInputStream();
            DataInputStream receiver = new DataInputStream(IS);
            String content = receiver.readUTF();
            System.out.println("收到回复 ： " +  content);

            client.close();
        } catch (IOException e) {
            System.out.println("无法连接到指定主机 ： " + host);
        }
    }
}