package uk.co.caprica.vlcjplayer;

import static uk.co.caprica.vlcjplayer.Application.application;

import java.awt.Window;

import uk.co.caprica.vlcj.player.embedded.DefaultAdaptiveRuntimeFullScreenStrategy;
import uk.co.caprica.vlcjplayer.event.AfterExitFullScreenEvent;
import uk.co.caprica.vlcjplayer.event.BeforeEnterFullScreenEvent;

final class VlcjPlayerFullScreenStrategy extends DefaultAdaptiveRuntimeFullScreenStrategy {

    VlcjPlayerFullScreenStrategy(Window window) {
        super(window);
    }

    @Override
    protected void beforeEnterFullScreen() {
        application().post(BeforeEnterFullScreenEvent.INSTANCE);
    }

    @Override
    protected  void afterExitFullScreen() {
        application().post(AfterExitFullScreenEvent.INSTANCE);
    }

    /**
     * Created by ShellMount on 2017/6/13.
     */
    public static class TestGit {
        public static void main(String[] args) {
            System.out.println("在GIT的模式下，是否能够上传？");
        }
    }
}
