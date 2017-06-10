package uk.co.caprica.vlcjplayer.remoteControl;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

/**
 * Created by 428900 on 2017/6/6.
 */
public class Receiver {
    Socket server;
    EmbeddedMediaPlayerComponent mediaPlayerComponent;

    public Receiver(Socket serverSocket)  {
        server = serverSocket;
    }

    public String unpackRequest () throws Exception{
        // 接收到的数据整理
        System.out.println("接收到新的连接 : " + server);
        DataInputStream receiver = new DataInputStream(server.getInputStream());
        String command = receiver.readUTF();
        System.out.println("收到客户端指令 ：" + command);

        // 回复客户端
        DataOutputStream replayer = new DataOutputStream(server.getOutputStream());
        replayer.writeUTF("指令处理成功 : successs");

        return command;
    }
}
