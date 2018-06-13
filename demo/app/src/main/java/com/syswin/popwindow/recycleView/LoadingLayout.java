package com.syswin.popwindow.recycleView;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;


/**
 * Created by hushendian on 2018/6/9.
 */

abstract class LoadingLayout extends FrameLayout implements ILoadingLayout {
    private View mContainer;
    private State mCurState;
    private State mPreState;

    public LoadingLayout(Context context) {
        this(context, (AttributeSet) null);
    }

    public LoadingLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mCurState = State.NONE;
        this.mPreState = State.NONE;
        this.init(context, attrs);
    }

    protected void init(Context context, AttributeSet attrs) {
        this.mContainer = this.createLoadingView(context, attrs);
        if (null == this.mContainer) {
            throw new NullPointerException("Loading view can not be null.");
        } else {
            LayoutParams params = new LayoutParams(-1, -2);
            this.addView(this.mContainer, params);
        }
    }

    protected abstract View createLoadingView(Context var1, AttributeSet var2);

    public void show(boolean show) {
        if (show != (View.VISIBLE == this.getVisibility())) {
            android.view.ViewGroup.LayoutParams params = this.mContainer.getLayoutParams();
            if (null != params) {
                if (show) {
                    params.height = -2;
                } else {
                    params.height = 0;
                }

                this.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
            }

        }
    }

    public void setLastUpdatedLabel(CharSequence label) {
    }

    public void setLoadingDrawable(Drawable drawable) {
    }

    public void setPullLabel(CharSequence pullLabel) {
    }

    public void setRefreshingLabel(CharSequence refreshingLabel) {
    }

    public void setReleaseLabel(CharSequence releaseLabel) {
    }

    protected State getPreState() {
        return this.mPreState;
    }

    public void setState(State state) {
        if (this.mCurState != state) {
            this.mPreState = this.mCurState;
            this.mCurState = state;
            this.onStateChanged(state, this.mPreState);
        }

    }


    public State getState() {
        return this.mCurState;
    }

    public void onPull(float scale) {
    }


    protected void onStateChanged(State curState, State oldState) {
        switch (curState.ordinal()) {
            case 1:
                this.onReset();
                break;
            case 2:
                this.onReleaseToRefresh();
                break;
            case 3:
                this.onPullToRefresh();
                break;
            case 4:
                this.onRefreshing();
                break;
            case 5:
                this.onNoMoreData();
        }

    }

    protected void onReset() {
    }

    protected void onPullToRefresh() {
    }

    protected void onReleaseToRefresh() {
    }

    protected void onRefreshing() {
    }

    protected void onNoMoreData() {
    }

    public abstract int getContentSize();


}
