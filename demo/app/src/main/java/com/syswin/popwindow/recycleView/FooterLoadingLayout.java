package com.syswin.popwindow.recycleView;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by hushendian on 2018/6/9.
 */

public class FooterLoadingLayout extends LoadingLayout {
    public static final float SCALE = 1.0F;
    private LinearLayout mHeaderContainer;
    private ImageView ivLoading1;
    private ImageView ivLoading2;
    private ImageView ivLoading3;
    private List<View> mViews;
    private int[] delays = new int[]{120, 240, 360};
    private float[] scaleFloats = new float[]{1.0F, 1.0F, 1.0F};
    private TextView tv_no_more;

    public FooterLoadingLayout(Context context) {
        super(context);
        this.init();
    }

    private void init() {
        this.mHeaderContainer = (LinearLayout) this.findViewById(com.systoon.view.R.id.footer_pull_to_refresh);
        this.ivLoading1 = (ImageView) this.findViewById(com.systoon.view.R.id.footer_iv_1);
        this.ivLoading2 = (ImageView) this.findViewById(com.systoon.view.R.id.footer_iv_2);
        this.ivLoading3 = (ImageView) this.findViewById(com.systoon.view.R.id.footer_iv_3);
        this.tv_no_more = (TextView) this.findViewById(com.systoon.view.R.id.tv_no_more);
        this.ivLoading1.animate().alpha(0.0F);
        this.ivLoading2.animate().alpha(0.0F);
        this.ivLoading3.animate().alpha(0.0F);
        this.mViews = new ArrayList();
        this.mViews.add(this.ivLoading1);
        this.mViews.add(this.ivLoading2);
        this.mViews.add(this.ivLoading3);
    }

    public FooterLoadingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public FooterLoadingLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.init();
    }

    protected View createLoadingView(Context context, AttributeSet attrs) {
        View container = LayoutInflater.from(context).inflate(com.systoon.view.R.layout.pull_footer_loading_layout, (ViewGroup) null);
        return container;
    }

    public void setLastUpdatedLabel(CharSequence label) {
    }

    protected void onStateChanged(State curState, State oldState) {
        super.onStateChanged(curState, oldState);
    }

    protected void onReset() {
        this.resetRotation();
    }

    protected void onPullToRefresh() {
        this.setVAnimator();
    }

    private void setVAnimator() {
        for (int i = 0; i < this.mViews.size(); ++i) {
            ValueAnimator scaleAnim = ValueAnimator.ofFloat(new float[]{1.0F, 0.3F, 1.0F});
            scaleAnim.setDuration(750L);
            scaleAnim.setRepeatCount(-1);
            scaleAnim.setRepeatMode(ValueAnimator.REVERSE);
            scaleAnim.setStartDelay((long) this.delays[i]);
            final int finalI = i;
            scaleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    scaleFloats[finalI] = ((Float) animation.getAnimatedValue()).floatValue();
                    setScaleAnimator();
                    postInvalidate();
                }
            });
            scaleAnim.start();
            this.ivLoading1.setVisibility(View.VISIBLE);
            this.ivLoading2.setVisibility(View.VISIBLE);
            this.ivLoading3.setVisibility(View.VISIBLE);
        }

    }

    private void setScaleAnimator() {
        for (int i = 0; i < this.mViews.size(); ++i) {
            ((View) this.mViews.get(i)).setScaleX(this.scaleFloats[i]);
            ((View) this.mViews.get(i)).setScaleY(this.scaleFloats[i]);
        }

    }

    protected void onReleaseToRefresh() {
    }

    protected void onRefreshing() {
    }

    protected void onNoMoreData() {
        this.setTv_no_more(true);
    }

    public int getContentSize() {
        return null != this.mHeaderContainer ? this.mHeaderContainer.getHeight() : (int) (this.getResources().getDisplayMetrics().density * 40.0F);
    }

    private void setTv_no_more(boolean show) {
        if (show) {
            this.tv_no_more.setVisibility(View.VISIBLE);
            this.ivLoading1.setVisibility(View.GONE);
            this.ivLoading2.setVisibility(View.GONE);
            this.ivLoading3.setVisibility(View.GONE);
        } else {
            this.tv_no_more.setVisibility(View.GONE);
            this.ivLoading1.setVisibility(View.VISIBLE);
            this.ivLoading2.setVisibility(View.VISIBLE);
            this.ivLoading3.setVisibility(View.VISIBLE);
        }

    }

    private void resetRotation() {
        Iterator var1 = this.mViews.iterator();

        while (var1.hasNext()) {
            View view = (View) var1.next();
            view.clearAnimation();
        }

    }
}

