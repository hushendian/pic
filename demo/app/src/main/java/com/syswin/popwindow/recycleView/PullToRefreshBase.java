package com.syswin.popwindow.recycleView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;


/**
 * Created by hushendian on 2018/6/9.
 */

public abstract class PullToRefreshBase<T extends View> extends LinearLayout implements IPullToRefresh<T> {
    private static final int SCROLL_DURATION = 150;
    private static final float OFFSET_RADIO = 2.5F;
    T mRefreshableView;
    private float mLastMotionY = -1.0F;
    private OnRefreshListener<T> mRefreshListener;
    private LoadingLayout mHeaderLayout;
    private LoadingLayout mFooterLayout;
    private int mHeaderHeight;
    private int mFooterHeight;
    private boolean mPullRefreshEnabled = true;
    private boolean mPullLoadEnabled = false;
    private boolean mScrollLoadEnabled = false;
    private boolean mInterceptEventEnable = true;
    private boolean mIsHandledTouchEvent = false;
    private int mTouchSlop;
    private ILoadingLayout.State mPullDownState;
    private ILoadingLayout.State mPullUpState;
    private SmoothScrollRunnable mSmoothScrollRunnable;
    private FrameLayout mRefreshableViewWrapper;

    public PullToRefreshBase(Context context) {
        super(context);
        this.mPullDownState = ILoadingLayout.State.NONE;
        this.mPullUpState = ILoadingLayout.State.NONE;
        this.init(context, (AttributeSet) null);
    }

    private void init(Context context, AttributeSet attrs) {
        this.setOrientation(LinearLayout.VERTICAL);
        this.mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        this.mHeaderLayout = this.createHeaderLoadingLayout(context, attrs);
        this.mFooterLayout = this.createFooterLoadingLayout(context, attrs);
        this.mRefreshableView = this.createRefreshableView(context, attrs);
        if (null == this.mRefreshableView) {
            throw new NullPointerException("Refreshable view can not be null.");
        } else {
            this.addRefreshableView(context, this.mRefreshableView);
            this.addHeaderAndFooter(context);
            this.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                public void onGlobalLayout() {
                    refreshLoadingViewsSize();
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            });
        }
    }

    public void setOrientation(int orientation) {
        if (1 != orientation) {
            throw new IllegalArgumentException("This class only supports VERTICAL orientation.");
        } else {
            super.setOrientation(orientation);
        }
    }

    protected LoadingLayout createHeaderLoadingLayout(Context context, AttributeSet attrs) {
        return new HeaderLoadingLayout(context);
    }

    protected LoadingLayout createFooterLoadingLayout(Context context, AttributeSet attrs) {
        return new FooterLoadingLayout(context);
    }

    protected abstract T createRefreshableView(Context var1, AttributeSet var2);

    protected void addRefreshableView(Context context, T refreshableView) {
        int width = -1;
        int height = -1;
        this.mRefreshableViewWrapper = new FrameLayout(context);
        this.mRefreshableViewWrapper.addView(refreshableView, width, height);
        height = 10;
        this.addView(this.mRefreshableViewWrapper, new LayoutParams(width, height));
    }

    protected void addHeaderAndFooter(Context context) {
        LayoutParams params = new LayoutParams(-1, -2);
        LoadingLayout headerLayout = this.mHeaderLayout;
        LoadingLayout footerLayout = this.mFooterLayout;
        if (null != headerLayout) {
            if (this == headerLayout.getParent()) {
                this.removeView(headerLayout);
            }

            this.addView(headerLayout, 0, params);
        }

        if (null != footerLayout) {
            if (this == footerLayout.getParent()) {
                this.removeView(footerLayout);
            }

            this.addView(footerLayout, -1, params);
        }

    }

    private void refreshLoadingViewsSize() {
        int headerHeight = null != this.mHeaderLayout ? this.mHeaderLayout.getContentSize() : 0;
        int footerHeight = null != this.mFooterLayout ? this.mFooterLayout.getContentSize() : 0;
        if (headerHeight < 0) {
            headerHeight = 0;
        }

        if (footerHeight < 0) {
            footerHeight = 0;
        }

        this.mHeaderHeight = headerHeight;
        this.mFooterHeight = footerHeight;
        headerHeight = null != this.mHeaderLayout ? this.mHeaderLayout.getMeasuredHeight() : 0;
        footerHeight = null != this.mFooterLayout ? this.mFooterLayout.getMeasuredHeight() : 0;
        if (0 == footerHeight) {
            footerHeight = this.mFooterHeight;
        }

        int pLeft = this.getPaddingLeft();
        int pTop = this.getPaddingTop();
        int pRight = this.getPaddingRight();
        int pBottom = this.getPaddingBottom();
        pTop = -headerHeight;
        pBottom = -footerHeight;
        this.setPadding(pLeft, pTop, pRight, pBottom);
    }

    public PullToRefreshBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mPullDownState = ILoadingLayout.State.NONE;
        this.mPullUpState = ILoadingLayout.State.NONE;
        this.init(context, attrs);
    }

    public PullToRefreshBase(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mPullDownState = ILoadingLayout.State.NONE;
        this.mPullUpState = ILoadingLayout.State.NONE;
        this.init(context, attrs);
    }

    public final boolean onInterceptTouchEvent(MotionEvent event) {
        if (!this.isInterceptTouchEventEnabled()) {
            return false;
        } else if (!this.isPullLoadEnabled() && !this.isPullRefreshEnabled()) {
            return false;
        } else {
            int action = event.getAction();
            if (action != 3 && action != 1) {
                if (action != 0 && this.mIsHandledTouchEvent) {
                    return true;
                } else {
                    switch (action) {
                        case 0:
                            this.mLastMotionY = event.getY();
                            this.mIsHandledTouchEvent = false;
                            break;
                        case 2:
                            float deltaY = event.getY() - this.mLastMotionY;
                            float absDiff = Math.abs(deltaY);
                            if (absDiff > (float) this.mTouchSlop || this.isPullRefreshing() || this.isPullLoading()) {
                                this.mLastMotionY = event.getY();
                                if (this.isPullRefreshEnabled() && this.isReadyForPullDown()) {
                                    this.mIsHandledTouchEvent = Math.abs(this.getScrollYValue()) > 0 || deltaY > 0.5F;
                                    if (this.mIsHandledTouchEvent) {
                                        this.mRefreshableView.onTouchEvent(event);
                                    }
                                } else if (this.isPullLoadEnabled() && this.isReadyForPullUp()) {
                                    this.mIsHandledTouchEvent = Math.abs(this.getScrollYValue()) > 0 || deltaY < -0.5F;
                                }
                            }
                    }

                    return this.mIsHandledTouchEvent;
                }
            } else {
                this.mIsHandledTouchEvent = false;
                return false;
            }
        }
    }

    private boolean isInterceptTouchEventEnabled() {
        return this.mInterceptEventEnable;
    }

    protected boolean isPullRefreshing() {
        return this.mPullDownState == ILoadingLayout.State.REFRESHING;
    }

    protected boolean isPullLoading() {
        return this.mPullUpState == ILoadingLayout.State.REFRESHING;
    }

    protected abstract boolean isReadyForPullDown();

    public int getScrollYValue() {
        return this.getScrollY();
    }

    protected abstract boolean isReadyForPullUp();

    private void setInterceptTouchEventEnabled(boolean enabled) {
        this.mInterceptEventEnable = enabled;
    }

    public final boolean onTouchEvent(MotionEvent ev) {
        boolean handled = false;
        switch (ev.getAction()) {
            case 0:
                this.mLastMotionY = ev.getY();
                this.mIsHandledTouchEvent = false;
                break;
            case 1:
            case 3:
                if (this.mIsHandledTouchEvent) {
                    this.mIsHandledTouchEvent = false;
                    if (this.isReadyForPullDown()) {
                        if (this.mPullRefreshEnabled && this.mPullDownState == ILoadingLayout.State.RELEASE_TO_REFRESH) {
                            this.startRefreshing();
                            handled = true;
                        } else {
                            this.resetHeaderLayout();
                        }
                    } else if (this.isReadyForPullUp()) {
                        if (this.isPullLoadEnabled() && this.mPullUpState == ILoadingLayout.State.RELEASE_TO_REFRESH) {
                            this.startLoading();
                            handled = true;
                        } else {
                            this.resetFooterLayout();
                        }
                    }
                }
                break;
            case 2:
                float deltaY = ev.getY() - this.mLastMotionY;
                this.mLastMotionY = ev.getY();
                if (this.isPullRefreshEnabled() && this.isReadyForPullDown()) {
                    this.pullHeaderLayout(deltaY / 2.5F);
                    handled = true;
                } else if (this.isPullLoadEnabled() && this.isReadyForPullUp()) {
                    this.pullFooterLayout(deltaY / 2.5F);
                    handled = true;
                } else {
                    this.mIsHandledTouchEvent = false;
                }
        }

        return handled;
    }

    protected final void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.refreshLoadingViewsSize();
        this.refreshRefreshableViewSize(w, h);
        this.post(new Runnable() {
            public void run() {
                requestLayout();
            }
        });
    }

    protected void refreshRefreshableViewSize(int width, int height) {
        if (null != this.mRefreshableViewWrapper) {
            LayoutParams lp = (LayoutParams) this.mRefreshableViewWrapper.getLayoutParams();
            if (lp.height != height) {
                lp.height = height;
                this.mRefreshableViewWrapper.requestLayout();
            }
        }

    }

    public void doPullRefreshing(final boolean smoothScroll, long delayMillis) {
        this.postDelayed(new Runnable() {
            public void run() {
                int newScrollValue = -mHeaderHeight - ScreenUtil.dp2px(20.0F);
                int duration = smoothScroll ? 150 : 0;
                startRefreshing();
                pullHeaderLayoutFor(10);
                smoothScrollTo(newScrollValue, (long) duration, 0L);
            }
        }, delayMillis);
    }

    protected void startRefreshing() {
        if (!this.isPullRefreshing()) {
            this.mPullDownState = ILoadingLayout.State.REFRESHING;
            this.onStateChanged(ILoadingLayout.State.REFRESHING, true);
            if (null != this.mHeaderLayout) {
                this.mHeaderLayout.setState(ILoadingLayout.State.REFRESHING);
            }

            if (null != this.mRefreshListener) {
                this.postDelayed(new Runnable() {
                    public void run() {
                        mRefreshListener.onPullDownToRefresh(PullToRefreshBase.this);
                    }
                }, this.getSmoothScrollDuration());
            }

        }
    }

    protected void pullHeaderLayoutFor(int delta) {
    }

    private void smoothScrollTo(int newScrollValue, long duration, long delayMillis) {
        if (null != this.mSmoothScrollRunnable) {
            this.mSmoothScrollRunnable.stop();
        }

        int oldScrollValue = this.getScrollYValue();
        boolean post = oldScrollValue != newScrollValue;
        if (post) {
            this.mSmoothScrollRunnable = new SmoothScrollRunnable(oldScrollValue, newScrollValue, duration);
        }

        if (post) {
            if (delayMillis > 0L) {
                this.postDelayed(this.mSmoothScrollRunnable, delayMillis);
            } else {
                this.post(this.mSmoothScrollRunnable);
            }
        }

    }

    protected void onStateChanged(ILoadingLayout.State state, boolean isPullDown) {
    }

    protected long getSmoothScrollDuration() {
        return 150L;
    }

    protected void pullHeaderLayout(float delta) {
        int oldScrollY = this.getScrollYValue();
        if (delta < 0.0F && (float) oldScrollY - delta >= 0.0F) {
            this.setScrollTo(0, 0);
        } else {
            this.setScrollBy(0, -((int) delta));
            if (null != this.mHeaderLayout && 0 != this.mHeaderHeight) {
                float scale = (float) Math.abs(this.getScrollYValue()) / (float) this.mHeaderHeight;
                this.mHeaderLayout.onPull(scale);
            }

            int scrollY = Math.abs(this.getScrollYValue());
            if (this.isPullRefreshEnabled() && !this.isPullRefreshing()) {
                if (scrollY > this.mHeaderHeight + ScreenUtil.dp2px(20.0F)) {
                    this.mPullDownState = ILoadingLayout.State.RELEASE_TO_REFRESH;
                } else {
                    this.mPullDownState = ILoadingLayout.State.PULL_TO_REFRESH;
                }

                this.mHeaderLayout.setState(mPullDownState);
                this.onStateChanged(this.mPullDownState, true);
            }

        }
    }

    protected void pullFooterLayout(float delta) {
        int oldScrollY = this.getScrollYValue();
        if (delta > 0.0F && (float) oldScrollY - delta <= 0.0F) {
            this.setScrollTo(0, 0);
        } else {
            this.setScrollBy(0, -((int) delta));
            if (null != this.mFooterLayout && 0 != this.mFooterHeight) {
                float scale = (float) Math.abs(this.getScrollYValue()) / (float) this.mFooterHeight;
                this.mFooterLayout.onPull(scale);
            }

            int scrollY = Math.abs(this.getScrollYValue());
            if (this.isPullLoadEnabled() && !this.isPullLoading()) {
                if (scrollY > this.mFooterHeight) {
                    this.mPullUpState = ILoadingLayout.State.RELEASE_TO_REFRESH;
                } else {
                    this.mPullUpState = ILoadingLayout.State.PULL_TO_REFRESH;
                }

                this.mFooterLayout.setState(this.mPullUpState);
                this.onStateChanged(this.mPullUpState, false);
            }

        }
    }

    protected void startLoading() {
        if (!this.isPullLoading()) {
            this.mPullUpState = ILoadingLayout.State.REFRESHING;
            this.onStateChanged(ILoadingLayout.State.REFRESHING, false);
            if (null != this.mFooterLayout) {
                this.mFooterLayout.setState(ILoadingLayout.State.REFRESHING);
            }

            if (null != this.mRefreshListener) {
                this.postDelayed(new Runnable() {
                    public void run() {
                        mRefreshListener.onPullUpToRefresh(PullToRefreshBase.this);
                    }
                }, this.getSmoothScrollDuration());
            }

        }
    }

    private void setScrollTo(int x, int y) {
        this.scrollTo(x, y);
    }

    private void setScrollBy(int x, int y) {
        this.scrollBy(x, y);
    }

    public interface OnRefreshListener<V extends View> {
        void onPullDownToRefresh(PullToRefreshBase<V> var1);

        void onPullUpToRefresh(PullToRefreshBase<V> var1);
    }

    final class SmoothScrollRunnable implements Runnable {
        private final Interpolator mInterpolator;
        private final int mScrollToY;
        private final int mScrollFromY;
        private final long mDuration;
        private boolean mContinueRunning = true;
        private long mStartTime = -1L;
        private int mCurrentY = -1;

        public SmoothScrollRunnable(int fromY, int toY, long duration) {
            this.mScrollFromY = fromY;
            this.mScrollToY = toY;
            this.mDuration = duration;
            this.mInterpolator = new DecelerateInterpolator();
        }

        public void stop() {
            this.mContinueRunning = false;
            removeCallbacks(this);
        }

        public void run() {
            if (this.mDuration <= 0L) {
                setScrollTo(0, this.mScrollToY);
            } else {
                if (this.mStartTime == -1L) {
                    this.mStartTime = System.currentTimeMillis();
                } else {
                    long oneSecond = 1000L;
                    long normalizedTime = 1000L * (System.currentTimeMillis() - this.mStartTime) / this.mDuration;
                    normalizedTime = Math.max(Math.min(normalizedTime, 1000L), 0L);
                    int deltaY = Math.round((float) (this.mScrollFromY - this.mScrollToY) * this.mInterpolator.getInterpolation((float) normalizedTime / 1000.0F));
                    this.mCurrentY = this.mScrollFromY - deltaY;
                    setScrollTo(0, this.mCurrentY);
                }

                if (this.mContinueRunning && this.mScrollToY != this.mCurrentY) {
                    postDelayed(this, 16L);
                }

            }
        }


    }

    public void setPullRefreshEnabled(boolean pullRefreshEnabled) {
        this.mPullRefreshEnabled = pullRefreshEnabled;
    }


    public void setPullLoadEnabled(boolean pullLoadEnabled) {
        this.mPullLoadEnabled = pullLoadEnabled;
    }


    public void setScrollLoadEnabled(boolean scrollLoadEnabled) {
        this.mScrollLoadEnabled = scrollLoadEnabled;
    }


    public boolean isPullRefreshEnabled() {
        return this.mPullRefreshEnabled && null != this.mHeaderLayout;
    }


    public void setOnRefreshListener(OnRefreshListener<T> refreshListener) {
        this.mRefreshListener = refreshListener;
    }


    public boolean isPullLoadEnabled() {
        return this.mPullLoadEnabled && null != this.mFooterLayout;
    }


    public boolean isScrollLoadEnabled() {
        return this.mScrollLoadEnabled;
    }


    public void onPullDownRefreshComplete() {
        if (this.isPullRefreshing()) {
            this.mPullDownState = ILoadingLayout.State.RESET;
            this.onStateChanged(ILoadingLayout.State.RESET, true);
            this.postDelayed(new Runnable() {
                public void run() {
                    setInterceptTouchEventEnabled(true);
                    mHeaderLayout.setState(ILoadingLayout.State.RESET);
                }
            }, this.getSmoothScrollDuration());
            this.resetHeaderLayout();
            this.setInterceptTouchEventEnabled(false);
        }

    }


    public void onPullUpRefreshComplete() {
        if (this.isPullLoading()) {
            this.mPullUpState = ILoadingLayout.State.RESET;
            this.onStateChanged(ILoadingLayout.State.RESET, false);
            this.postDelayed(new Runnable() {
                public void run() {
                    setInterceptTouchEventEnabled(true);
                    mFooterLayout.setState(ILoadingLayout.State.RESET);
                }
            }, this.getSmoothScrollDuration());
            this.resetFooterLayout();
            this.setInterceptTouchEventEnabled(false);
        }

    }


    public T getRefreshableView() {
        return this.mRefreshableView;
    }


    public LoadingLayout getHeaderLoadingLayout() {
        return this.mHeaderLayout;
    }


    public LoadingLayout getFooterLoadingLayout() {
        return this.mFooterLayout;
    }


    public void setLastUpdatedLabel(CharSequence label) {
        if (null != this.mHeaderLayout) {
            this.mHeaderLayout.setLastUpdatedLabel(label);
        }

        if (null != this.mFooterLayout) {
            this.mFooterLayout.setLastUpdatedLabel(label);
        }

    }


    protected void resetHeaderLayout() {
        int scrollY = Math.abs(this.getScrollYValue());
        boolean refreshing = this.isPullRefreshing();
        if (refreshing && scrollY <= this.mHeaderHeight) {
            this.smoothScrollTo(0);
        } else {
            if (refreshing) {
                this.smoothScrollTo(-this.mHeaderHeight);
            } else {
                this.smoothScrollTo(0);
            }

        }
    }

    protected void resetFooterLayout() {
        int scrollY = Math.abs(this.getScrollYValue());
        boolean isPullLoading = this.isPullLoading();
        if (isPullLoading && scrollY <= this.mFooterHeight) {
            this.smoothScrollTo(0);
        } else {
            if (isPullLoading) {
                this.smoothScrollTo(this.mFooterHeight);
            } else {
                this.smoothScrollTo(0);
            }

        }
    }


    private void smoothScrollTo(int newScrollValue) {
        this.smoothScrollTo(newScrollValue, this.getSmoothScrollDuration(), 0L);
    }


}
