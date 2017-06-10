package uk.co.caprica.vlcjplayer.remoteControl;

import uk.co.caprica.vlcj.component.EmbeddedMediaListPlayerComponent;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.list.MediaListPlayer;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by 428900 on 2017/6/6.
 */
public class RemoteThread implements Runnable {
    // 远程监听
    static Socket server;

    static EmbeddedMediaPlayerComponent mediaPlayerComponent;

    static EmbeddedMediaListPlayerComponent mediaListPlayerComponent;

    /**
     * 构造类: 监控线程中使用
     * @param serverSocket
     */
    public RemoteThread(Socket serverSocket,
                        EmbeddedMediaPlayerComponent inputMediaPlayerComponent,
                        EmbeddedMediaListPlayerComponent inputMediaListPlayerComponent) {
        server = serverSocket;
        mediaPlayerComponent = inputMediaPlayerComponent;
        mediaListPlayerComponent = inputMediaListPlayerComponent;
    }


    /**
     * 远程命令处理线程
     */
    public void run () {
        try {
            Receiver receiver = new Receiver(server);
            String command = receiver.unpackRequest();

            EmbeddedMediaPlayer player = mediaPlayerComponent.getMediaPlayer();
            MediaListPlayer playerList = mediaListPlayerComponent.getMediaListPlayer();

            Action action = new Action(command, player, playerList);
            action.execCommand();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
