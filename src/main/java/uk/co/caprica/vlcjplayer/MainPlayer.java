/*
 * This file is part of VLCJ.
 *
 * VLCJ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * VLCJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with VLCJ.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2015 Caprica Software Limited.
 */

package uk.co.caprica.vlcjplayer;

import uk.co.caprica.vlcj.component.EmbeddedMediaListPlayerComponent;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.log.NativeLog;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import uk.co.caprica.vlcj.runtime.streams.NativeStreams;
import uk.co.caprica.vlcjplayer.event.ShutdownEvent;
import uk.co.caprica.vlcjplayer.remoteControl.RemoteThread;
import uk.co.caprica.vlcjplayer.view.debug.DebugFrame;
import uk.co.caprica.vlcjplayer.view.effects.EffectsFrame;
import uk.co.caprica.vlcjplayer.view.main.MainFrame;
import uk.co.caprica.vlcjplayer.view.messages.NativeLogFrame;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static uk.co.caprica.vlcjplayer.Application.application;

/**
 * 教程地址：
 * http://capricasoftware.co.uk/#/projects/vlcj/tutorial
 *
 * DEMO地址：
 * https://github.com/caprica/vlcj/tree/master/src/test/java/uk/co/caprica/vlcj/test
 *
 * Application entry-point.
 *
 * 命令行JAR包执行：
 * java -Djava.ext.dirs=./依赖目录 -cp vlcj-player.jar uk.co.caprica.vlcjplayer.MainPlayer
 */


public class MainPlayer {

    private static final NativeStreams nativeStreams = null;

    // Redirect the native output streams to files, useful since VLC can generate a lot of noisy native logs we don't care about
    // (on the other hand, if we don't look at the logs we might won't see errors)
    /*static {
        if (RuntimeUtil.isNix()) {
            nativeStreams = new NativeStreams("stdout.log", "stderr.log");
        }
        else {
            nativeStreams = null;
        }
    }*/

    private JFrame mainFrame;

    @SuppressWarnings("unused")
    private JFrame messagesFrame;

    @SuppressWarnings("unused")
    private JFrame effectsFrame;

    @SuppressWarnings("unused")
    private JFrame debugFrame;

    private NativeLog nativeLog;

    private static EmbeddedMediaPlayerComponent mediaPlayerComponent;

    private static EmbeddedMediaListPlayerComponent mediaListPlayerComponent;


    /**
     * 构造类: 播放相关内容
     */
    private MainPlayer() {
        mediaListPlayerComponent = new EmbeddedMediaListPlayerComponent();

        mediaPlayerComponent = application().mediaPlayerComponent();

        mainFrame = new MainFrame(mediaListPlayerComponent);
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mediaPlayerComponent.getMediaPlayer().stop();
                mediaPlayerComponent.release();
                if (nativeStreams != null) {
                    nativeStreams.release();
                }
                application().post(ShutdownEvent.INSTANCE);
            }

            @Override
            public void windowClosed(WindowEvent e) {
            }
        });
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        EmbeddedMediaPlayer embeddedMediaPlayer = mediaPlayerComponent.getMediaPlayer();
        embeddedMediaPlayer.setFullScreenStrategy(new VlcjPlayerFullScreenStrategy(mainFrame));

        nativeLog = mediaPlayerComponent.getMediaPlayerFactory().newLog();

        messagesFrame = new NativeLogFrame(nativeLog);
        effectsFrame = new EffectsFrame();
        debugFrame = new DebugFrame();
    }


    /**
     * 启动播放器
     */
    private void start() {
        mainFrame.setVisible(true);
        mediaPlayerComponent.getMediaPlayer().setFullScreen(true);
    }


    //----------------------------------------------------------

    /**
     * 入口
     * @param args : 无参数
     * @throws : InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        // This will locate LibVLC for the vast majority of cases
        new NativeDiscovery().discover();

        setLookAndFeel();

        mainPlayThread();

        mainListenThread();
    }

    /**
     * 这一步未明白
     */
    private static void setLookAndFeel() {
        String lookAndFeelClassName;
        if (RuntimeUtil.isNix()) {
            lookAndFeelClassName = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
        }
        else {
            lookAndFeelClassName = UIManager.getSystemLookAndFeelClassName();
        }

        try {
            UIManager.setLookAndFeel(lookAndFeelClassName);
        }
        catch(Exception e) {
            // Silently fail, it doesn't matter
        }
    }

    /**
     * 远程指令监控
     */
    private static void mainListenThread() {
        try {
            ServerSocket ssocket = new ServerSocket(1234);
            System.out.println("监听中...");
            while (true) {
                Socket server = ssocket.accept();
                new Thread(new RemoteThread(server,
                        mediaPlayerComponent,
                        mediaListPlayerComponent))
                        .start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 主播放器启动线程
     */
    /*private static void mainPlayThread() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainPlayer().start();
            }
        });
    }*/

    // lambda 替换 上面的函数面目全非呀？
    private static void mainPlayThread() {
        SwingUtilities.invokeLater(
                () -> new MainPlayer().start()
        );

        System.out.println("这里是被 Lambda 转换过的分支里面的数据");
        System.out.println("这里是被 Lambda 转换过的分支里面的数据");
        System.out.println("这里是被 Lambda 转换过的分支里面的数据");
        System.out.println("这里是被 Lambda 转换过的分支里面的数据");
        System.out.println("这里是被 Lambda 转换过的分支里面的数据");
        System.out.println("这里是被 Lambda 转换过的分支里面的数据");
        System.out.println("这里是被 测试 SSH 提交数据 ");



    }

}
