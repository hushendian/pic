package com.syswin.popwindow.recycleView;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.syswin.popwindow.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by hushendian on 2018/6/9.
 */

public class NewFooterLoadingLayout extends LoadingLayout {
    public static final float SCALE = 1.0F;
    List<ValueAnimator> animators = new ArrayList();
    private String TAG = "NewFooterLoadingLayout";
    private LinearLayout mHeaderContainer;
    private ImageView ivLoading1;
    private ImageView ivLoading2;
    private ImageView ivLoading3;
    private List<View> mViews;
    private int[] delays = new int[]{120, 240, 360};
    private float[] scaleFloats = new float[]{1.0F, 1.0F, 1.0F};
    private TextView tv_no_more;

    public NewFooterLoadingLayout(Context context) {
        super(context);
        this.initView(context);
    }

    public void initView(Context context) {
        this.mHeaderContainer = (LinearLayout) this.findViewById(R.id.footer_pull_to_refresh);
        this.ivLoading1 = (ImageView) this.findViewById(R.id.footer_iv_1);
        this.ivLoading2 = (ImageView) this.findViewById(R.id.footer_iv_2);
        this.ivLoading3 = (ImageView) this.findViewById(R.id.footer_iv_3);
        this.tv_no_more = (TextView) this.findViewById(R.id.tv_no_more);
        this.mViews = new ArrayList();
        this.mViews.add(this.ivLoading1);
        this.mViews.add(this.ivLoading2);
        this.mViews.add(this.ivLoading3);
        LayoutParams params1 = (LayoutParams) this.ivLoading2.getLayoutParams();
        params1.setMargins(ScreenUtil.dp2px(5.0F), 0, ScreenUtil.dp2px(5.0F), 0);
        this.ivLoading2.setLayoutParams(params1);
    }

    public NewFooterLoadingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(context);
    }

    public NewFooterLoadingLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected View createLoadingView(Context context, AttributeSet attrs) {
        View container = LayoutInflater.from(context).inflate(R.layout.pull_footer_loading_layout, (ViewGroup) null);
        return container;
    }

    public void onPull(float scale) {
        System.out.println("onPull");
    }

    protected void onReset() {
        if (!State.NO_MORE_DATA.equals(this.getState())) {
            this.resetRotation();
        }

        System.out.println("onReset");
    }

    protected void onPullToRefresh() {
        System.out.println("onPullToRefresh");
    }

    protected void onReleaseToRefresh() {
        System.out.println("onReleaseToRefresh");
    }

    protected void onRefreshing() {
        if (State.NO_MORE_DATA.equals(this.getState())) {
            this.tv_no_more.setVisibility(View.GONE);
            this.mHeaderContainer.setVisibility(View.VISIBLE);
        }

        System.out.println("onRefreshing");
    }

    protected void onNoMoreData() {
        super.onNoMoreData();
        this.tv_no_more.setVisibility(View.VISIBLE);
        this.mHeaderContainer.setVisibility(View.GONE);
    }

    public int getContentSize() {
        return null != this.mHeaderContainer ? this.mHeaderContainer.getHeight() : (int) (this.getResources().getDisplayMetrics().density * 60.0F);
    }

    private void resetRotation() {
        this.ivLoading1.setVisibility(View.INVISIBLE);
        this.ivLoading1.setScaleX(1.0F);
        this.ivLoading1.setScaleY(1.0F);
        this.ivLoading1.setTranslationY(0.0F);
        this.ivLoading1.setAnimation((Animation) null);
        this.ivLoading2.setVisibility(View.INVISIBLE);
        this.ivLoading2.setScaleX(1.0F);
        this.ivLoading2.setScaleY(1.0F);
        this.ivLoading2.setTranslationY(0.0F);
        this.ivLoading2.setAnimation((Animation) null);
        this.ivLoading3.setVisibility(View.INVISIBLE);
        this.ivLoading3.setScaleX(1.0F);
        this.ivLoading3.setScaleY(1.0F);
        this.ivLoading3.setTranslationY(0.0F);
        this.ivLoading3.setAnimation((Animation) null);
        Iterator var1 = this.animators.iterator();

        while (var1.hasNext()) {
            ValueAnimator animation = (ValueAnimator) var1.next();
            animation.cancel();
        }

    }

    public void startMove(int scrollY) {
        this.setMoveView1(scrollY);
        this.setMoveView2(scrollY);
        this.setMoveView3(scrollY);
    }

    public void setMoveView1(int top) {
        int ll_h = this.mHeaderContainer.getHeight();
        int start_y = 1;
        int end_y = ll_h / 2;
        if (top >= start_y && top <= end_y) {
            this.ivLoading1.setVisibility(View.VISIBLE);
            this.ivLoading1.setScaleX((float) top / (float) end_y);
            this.ivLoading1.setScaleY((float) top / (float) end_y);
            this.ivLoading1.setTranslationY((float) (start_y - top - 1));
            Log.i(this.TAG, "setMoveView1: " + top);
        } else {
            this.ivLoading1.setScaleX(1.0F);
            this.ivLoading1.setScaleY(1.0F);
            this.ivLoading1.setTranslationY((float) (-end_y));
        }

    }

    public void setMoveView2(int top) {
        int ll_h = this.mHeaderContainer.getHeight();
        int start_y = ll_h / 2;
        if (top >= start_y && top <= ll_h) {
            this.ivLoading2.setVisibility(View.VISIBLE);
            this.ivLoading2.setScaleX(2.0F * (float) (top - start_y) / (float) ll_h);
            this.ivLoading2.setScaleY(2.0F * (float) (top - start_y) / (float) ll_h);
            this.ivLoading2.setTranslationY((float) (start_y - top));
            Log.i(this.TAG, "setMoveView2: " + top);
        } else if (top > ll_h) {
            this.ivLoading2.setScaleX(1.0F);
            this.ivLoading2.setScaleY(1.0F);
            this.ivLoading2.setTranslationY((float) (-ll_h / 2));
        }

    }

    public void setMoveView3(int top) {
        int ll_h = this.mHeaderContainer.getHeight();
        int end_y = ll_h * 3 / 2;
        if (top >= ll_h && top <= end_y) {
            this.ivLoading3.setVisibility(View.VISIBLE);
            this.ivLoading3.setScaleX(3.0F * (float) (top - ll_h) / (float) end_y);
            this.ivLoading3.setScaleY(3.0F * (float) (top - ll_h) / (float) end_y);
            this.ivLoading3.setTranslationY((float) (ll_h - top));
            Log.i(this.TAG, "setMoveView3: " + top);
        } else if (top > end_y) {
            this.ivLoading3.setScaleX(1.0F);
            this.ivLoading3.setScaleY(1.0F);
            this.ivLoading3.setTranslationY((float) (-ll_h / 2));
        }

    }

    public void setMoveView4(boolean isLoading) {
        if (isLoading) {
            this.setVAnimator();
        } else {
            Iterator var2 = this.mViews.iterator();

            while (var2.hasNext()) {
                View view = (View) var2.next();
                view.clearAnimation();
            }
        }

    }

    private void setVAnimator() {
        if (this.animators != null) {
            Iterator var1 = this.animators.iterator();

            while (var1.hasNext()) {
                ValueAnimator animation = (ValueAnimator) var1.next();
                animation.cancel();
            }

            this.animators.clear();
        }

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
            this.animators.add(scaleAnim);
            scaleAnim.start();
        }

    }

    private void setScaleAnimator() {
        for (int i = 0; i < this.mViews.size(); ++i) {
            ((View) this.mViews.get(i)).setScaleX(this.scaleFloats[i]);
            ((View) this.mViews.get(i)).setScaleY(this.scaleFloats[i]);
        }

    }
}

