package com.syswin.popwindow.recycleView;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;


/**
 * Created by hushendian on 2018/6/9.
 */

public class PullToRefreshRecyclerView extends PullToRefreshBase<RecyclerView> {
    private RecyclerView mRecyclerView;
    private LoadingLayout mLoadMoreFooterLayout;
    private RecyclerView.OnScrollListener mScrollListener;
    private NewFooterLoadingLayout footView;
    private NewHeaderLoadingLayout headerView;

    public PullToRefreshRecyclerView(Context context) {
        this(context, (AttributeSet) null);
    }

    public PullToRefreshRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullToRefreshRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setPullLoadEnabled(false);
    }

    public void setHasMoreData(boolean hasMoreData) {
        if (!hasMoreData) {
            if (null != this.mLoadMoreFooterLayout) {
                this.mLoadMoreFooterLayout.setState(ILoadingLayout.State.NO_MORE_DATA);
            }

            LoadingLayout footerLoadingLayout = this.getFooterLoadingLayout();
            if (null != footerLoadingLayout) {
                footerLoadingLayout.setState(ILoadingLayout.State.NO_MORE_DATA);
            }
        }

    }

    public void setOnScrollListener(RecyclerView.OnScrollListener l) {
        this.mScrollListener = l;
    }

    protected LoadingLayout createHeaderLoadingLayout(Context context, AttributeSet attrs) {
        this.headerView = new NewHeaderLoadingLayout(context);
        return this.headerView;
    }

    protected LoadingLayout createFooterLoadingLayout(Context context, AttributeSet attrs) {
        this.footView = new NewFooterLoadingLayout(context);
        return this.footView;
    }

    protected RecyclerView createRefreshableView(Context context, AttributeSet attrs) {
        RecyclerView listView = this.createRecyclerView(context);
        this.mRecyclerView = listView;
        listView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {
                if (isScrollLoadEnabled() && hasMoreData() && (scrollState == 0 || scrollState == 2) && isReadyForPullUp()) {
                    startLoading();
                }

                if (null != mScrollListener) {
                    mScrollListener.onScrollStateChanged(recyclerView, scrollState);
                }

            }

            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (null != mScrollListener) {
                    mScrollListener.onScrolled(recyclerView, dx, dy);
                }

            }
        });
        return listView;
    }

    protected RecyclerView createRecyclerView(Context context) {
        return new RecyclerView(context);
    }

    protected boolean isReadyForPullDown() {
        return this.isFirstItemVisible();
    }

    protected boolean isReadyForPullUp() {
        return this.isLastItemVisible();
    }

    protected void startRefreshing() {
        super.startRefreshing();
        this.headerView.setMoveView4(true);
    }

    protected void pullHeaderLayoutFor(int delta) {
        super.pullHeaderLayoutFor(delta);
        if (this.headerView != null) {
            this.headerView.setMoveView(true);
        }

    }

    protected void pullHeaderLayout(float delta) {
        super.pullHeaderLayout(delta);
        int scrollY = Math.abs(this.getScrollYValue());
        if (this.headerView != null) {
            this.headerView.startMove(scrollY);
        }

    }

    protected void pullFooterLayout(float delta) {
        super.pullFooterLayout(delta);
        int scrollY = Math.abs(this.getScrollYValue());
        if (this.footView != null) {
            this.footView.startMove(scrollY);
        }

    }

    protected void startLoading() {
        super.startLoading();
        if (null != this.mLoadMoreFooterLayout) {
            this.mLoadMoreFooterLayout.setState(ILoadingLayout.State.REFRESHING);
        }

        this.footView.setMoveView4(true);
    }

    public void setScrollLoadEnabled(boolean scrollLoadEnabled) {
        super.setScrollLoadEnabled(scrollLoadEnabled);
        if (scrollLoadEnabled) {
            if (null == this.mLoadMoreFooterLayout) {
                this.mLoadMoreFooterLayout = new FooterLoadingLayout(this.getContext());
            }

            this.mLoadMoreFooterLayout.show(true);
        } else if (null != this.mLoadMoreFooterLayout) {
            this.mLoadMoreFooterLayout.show(false);
        }

    }

    public void onPullUpRefreshComplete() {
        super.onPullUpRefreshComplete();
        if (null != this.mLoadMoreFooterLayout) {
            this.mLoadMoreFooterLayout.setState(ILoadingLayout.State.RESET);
        }

    }

    public LoadingLayout getFooterLoadingLayout() {
        return this.isScrollLoadEnabled() ? this.mLoadMoreFooterLayout : super.getFooterLoadingLayout();
    }

    protected void resetHeaderLayout() {
        super.resetHeaderLayout();
    }

    protected void resetFooterLayout() {
        super.resetFooterLayout();
    }

    private boolean isLastItemVisible() {
        RecyclerView.Adapter adapter = this.mRecyclerView.getAdapter();
        if (null == adapter) {
            return true;
        } else {
            if (this.mRecyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                LinearLayoutManager manager = (LinearLayoutManager) this.mRecyclerView.getLayoutManager();
                int lastItemPosition = manager.getItemCount() - 1;
                int lastVisiblePosition = manager.findLastVisibleItemPosition();
                if (lastVisiblePosition >= lastItemPosition) {
                    int childIndex = lastVisiblePosition - manager.findFirstVisibleItemPosition();
                    int childCount = this.mRecyclerView.getChildCount();
                    int index = Math.min(childIndex, childCount - 1);
                    View lastVisibleChild = this.mRecyclerView.getChildAt(index);
                    if (lastVisibleChild != null) {
                        return lastVisibleChild.getBottom() <= this.mRecyclerView.getBottom();
                    }
                }
            }

            return false;
        }
    }

    private boolean isFirstItemVisible() {
        RecyclerView.Adapter adapter = this.mRecyclerView.getAdapter();
        if (null == adapter) {
            return true;
        } else {
            int mostTop = this.mRecyclerView.getChildCount() > 0 ? this.mRecyclerView.getChildAt(0).getTop() : 0;
            return mostTop >= 0;
        }
    }

    private boolean hasMoreData() {
        return null == this.mLoadMoreFooterLayout || this.mLoadMoreFooterLayout.getState() != ILoadingLayout.State.NO_MORE_DATA;
    }
}

