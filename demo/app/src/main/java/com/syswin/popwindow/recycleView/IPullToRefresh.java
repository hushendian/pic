package com.syswin.popwindow.recycleView;

import android.view.View;


/**
 * Created by hushendian on 2018/6/9.
 */

public interface IPullToRefresh <T extends View> {
    void setPullRefreshEnabled(boolean var1);

    void setPullLoadEnabled(boolean var1);

    void setScrollLoadEnabled(boolean var1);

    boolean isPullRefreshEnabled();

    boolean isPullLoadEnabled();

    boolean isScrollLoadEnabled();

    void setOnRefreshListener(PullToRefreshBase.OnRefreshListener<T> var1);

    void onPullDownRefreshComplete();

    void onPullUpRefreshComplete();

    T getRefreshableView();

    LoadingLayout getHeaderLoadingLayout();

    LoadingLayout getFooterLoadingLayout();

    void setLastUpdatedLabel(CharSequence var1);
}
