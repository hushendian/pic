package com.syswin.popwindow.recycleView;

/**
 * Created by hushendian on 2018/6/9.
 */

public interface ILoadingLayout {
    void setState(ILoadingLayout.State var1);

    ILoadingLayout.State getState();

    int getContentSize();

    void onPull(float var1);

    public static enum State {
        NONE,
        RESET,
        PULL_TO_REFRESH,
        RELEASE_TO_REFRESH,
        REFRESHING,
        NO_MORE_DATA,
        s;

        private State() {
        }
    }


}
