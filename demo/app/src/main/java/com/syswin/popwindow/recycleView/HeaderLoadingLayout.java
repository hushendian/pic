package com.syswin.popwindow.recycleView;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.syswin.popwindow.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by hushendian on 2018/6/9.
 */

public class HeaderLoadingLayout extends LoadingLayout {
    public static final float SCALE = 1.0F;
    private String TAG = "HeaderLoadingLayout";
    private LinearLayout mHeaderContainer;
    private ImageView ivLoading1;
    private ImageView ivLoading2;
    private ImageView ivLoading3;
    private List<View> mViews;
    private int[] delays = new int[]{120, 240, 360};
    private float[] scaleFloats = new float[]{1.0F, 1.0F, 1.0F};

    public HeaderLoadingLayout(Context context) {
        super(context);
        this.initView(context);
    }

    public void initView(Context context) {
        this.mHeaderContainer = (LinearLayout) this.findViewById(R.id.pull_to_refresh);
        this.ivLoading1 = (ImageView) this.findViewById(R.id.iv_1);
        this.ivLoading2 = (ImageView) this.findViewById(R.id.iv_2);
        this.ivLoading3 = (ImageView) this.findViewById(R.id.iv_3);
        this.ivLoading1.animate().alpha(0.0F);
        this.ivLoading2.animate().alpha(0.0F);
        this.ivLoading3.animate().alpha(0.0F);
        this.mViews = new ArrayList();
        this.mViews.add(this.ivLoading1);
        this.mViews.add(this.ivLoading2);
        this.mViews.add(this.ivLoading3);
    }

    public HeaderLoadingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(context);
    }

    public HeaderLoadingLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected View createLoadingView(Context context, AttributeSet attrs) {
        View container = LayoutInflater.from(context).inflate(R.layout.pull_header_loading_layout, (ViewGroup) null);
        return container;
    }

    public void onPull(float scale) {
        System.out.println("onPull");
    }

    protected void onReset() {
        this.resetRotation();
        System.out.println("onReset");
    }

    protected void onPullToRefresh() {
        System.out.println("onPullToRefresh");
    }

    protected void onReleaseToRefresh() {
        this.setTranslateAnimation();
        System.out.println("onReleaseToRefresh");
    }

    private void setTranslateAnimation() {
        for (int i = 0; i < this.mViews.size(); ++i) {
            ObjectAnimator moveIn = ObjectAnimator.ofFloat(this.mViews.get(i), "translationY", new float[]{-200.0F, 0.0F});
            moveIn.setRepeatCount(0);
            moveIn.setDuration(800L);
            ObjectAnimator rotate_x = ObjectAnimator.ofFloat(this.mViews.get(i), "scaleX", new float[]{0.0F, 1.0F});
            ObjectAnimator rotate_y = ObjectAnimator.ofFloat(this.mViews.get(i), "scaleY", new float[]{0.0F, 1.0F});
            rotate_x.setRepeatCount(0);
            rotate_x.setDuration(800L);
            rotate_y.setRepeatCount(0);
            rotate_y.setDuration(800L);
            ObjectAnimator fadeInOut = ObjectAnimator.ofFloat(this.mViews.get(i), "alpha", new float[]{0.0F, 1.0F});
            fadeInOut.setRepeatCount(0);
            fadeInOut.setDuration(800L);
            AnimatorSet animSet = new AnimatorSet();
            animSet.setStartDelay((long) this.delays[i]);
            animSet.play(moveIn).with(rotate_x).with(rotate_y).with(fadeInOut);
            animSet.addListener(new Animator.AnimatorListener() {
                public void onAnimationStart(Animator arg0) {
                }

                public void onAnimationEnd(Animator arg0) {
                    setVAnimator();
                }

                public void onAnimationCancel(Animator arg0) {
                }

                public void onAnimationRepeat(Animator arg0) {
                }
            });
            animSet.start();
            this.ivLoading1.setVisibility(View.VISIBLE);
            this.ivLoading2.setVisibility(View.VISIBLE);
            this.ivLoading3.setVisibility(View.VISIBLE);
        }

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
        }

    }

    private void setScaleAnimator() {
        for (int i = 0; i < this.mViews.size(); ++i) {
            ((View) this.mViews.get(i)).setScaleX(this.scaleFloats[i]);
            ((View) this.mViews.get(i)).setScaleY(this.scaleFloats[i]);
        }

    }

    protected void onRefreshing() {
        System.out.println("onRefreshing");
    }

    public int getContentSize() {
        return null != this.mHeaderContainer ? this.mHeaderContainer.getHeight() : (int) (this.getResources().getDisplayMetrics().density * 60.0F);
    }

    private void resetRotation() {
        Iterator var1 = this.mViews.iterator();

        while (var1.hasNext()) {
            View view = (View) var1.next();
            view.clearAnimation();
        }

    }
}

