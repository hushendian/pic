package com.syswin.popwindow.recycleView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.syswin.popwindow.R;

/**
 * Created by hushendian on 2018/6/9.
 */

public class HasScrollBarRecyclerView extends PullToRefreshRecyclerView {
    public HasScrollBarRecyclerView(Context context) {
        super(context);
    }

    public HasScrollBarRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HasScrollBarRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected RecyclerView createRecyclerView(Context context) {
        return (RecyclerView) LayoutInflater.from(context).inflate(R.layout.layout_recyclerview, (ViewGroup)null);
    }

}
