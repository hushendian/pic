package com.syswin.popwindow;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * Created by hushendian on 2018/5/16.
 * Job number: 600842
 * Email: hushendian@syswin.com
 * Leader: guohaichun
 */

public class MyPopWindow extends PopupWindow implements View.OnClickListener {
    private View mMenuView;
    private View btn_take_photo;
    private View btn_pick_photo;
    private View btn_cancel;

    public MyPopWindow(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.popwindow_camerapic, null, false);
        this.btn_take_photo = this.mMenuView.findViewById(R.id.btn_takepic);
        this.btn_pick_photo = this.mMenuView.findViewById(R.id.btn_picalbum);
        this.btn_cancel = this.mMenuView.findViewById(R.id.btn_cancelpic);
        this.mMenuView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_UP||event.getY()<btn_take_photo.getTop())
               MyPopWindow.this.dismiss();
                return true;
            }
        });
        this.btn_pick_photo.setOnClickListener(this);
        this.btn_take_photo.setOnClickListener(this);
        this.setFocusable(true);
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        this.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        this.setContentView(mMenuView);
        this.setBackgroundDrawable(new ColorDrawable(1342177280));
        this.setAnimationStyle(R.style.PopupAnimation);
    }

    @Override
    public void onClick(View v) {

    }
}
