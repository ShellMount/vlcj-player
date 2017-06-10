package uk.co.caprica.vlcjplayer.remoteControl;

import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.list.MediaListPlayer;


/**
 * Created by 428900 on 2017/6/6.
 */
public class Action {
    private String command;
    private EmbeddedMediaPlayer player;
    private MediaListPlayer playerList;

    Action (String inputCommand,
            EmbeddedMediaPlayer inputPlayer,
            MediaListPlayer inputPlayerList) {
        command = inputCommand;
        player = inputPlayer;
        playerList = inputPlayerList;
    }

    public void execCommand() {
        // 在 MainFrame 中自动设置置顶

        if (command.equalsIgnoreCase("STOP")) {
            player.stop();
        }

        if (command.equalsIgnoreCase("PAUSE")) {
            player.pause();
        }

        if (command.equalsIgnoreCase("FULLSCREEN")) {
            player.toggleFullScreen();
        }

        if (command.equalsIgnoreCase("PREVIOUS")) {
            playerList.playPrevious();
        }

        if (command.equalsIgnoreCase("NEXT")) {
            playerList.playNext();
        }

        if (command.equalsIgnoreCase("")) {
            player.subItems();
        }

        if (command.equalsIgnoreCase("")) {
            player.toggleFullScreen();
        }

        if (command.equalsIgnoreCase("")) {
            player.toggleFullScreen();
        }

        // 音量控制
        player.setVolume(8);

        System.out.println("player.subItems() : " + player.subItems());


    }
}
