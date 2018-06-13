package com.syswin.popwindow;

import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static void printErrStackTrace(String tag, Throwable tr, final String format, final Object... obj) {

        String log = obj == null ? format : String.format(format, obj);
        if (log == null) {
            log = "";
        }
        log += "  " + android.util.Log.getStackTraceString(tr);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final LinearLayout root = (LinearLayout) findViewById(R.id.root);
        findViewById(R.id.showPopwindow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyPopWindow myPopWindow = new MyPopWindow(MainActivity.this);
                myPopWindow.showAtLocation(root, Gravity.BOTTOM, 0, 0);
            }
        });
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.showdialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final SelfDialog selfDialog = new SelfDialog(MainActivity.this);
                selfDialog.setTitle("提示");
                selfDialog.setMessage("确定退出应用?");
                selfDialog.setYesOnclickListener("确定", new SelfDialog.onYesOnclickListener() {
                    @Override
                    public void onYesClick() {
                        Toast.makeText(MainActivity.this, "点击了--确定--按钮", Toast.LENGTH_LONG).show();
                        selfDialog.dismiss();
                    }
                });
                selfDialog.setNoOnclickListener("取消", new SelfDialog.onNoOnclickListener() {
                    @Override
                    public void onNoClick() {
                        Toast.makeText(MainActivity.this, "点击了--取消--按钮", Toast.LENGTH_LONG).show();
                        selfDialog.dismiss();
                    }
                });
                selfDialog.show();
            }
        });
        findViewById(R.id.btn_crash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printErrStackTrace("hsd", null, "buildDigestBean is failed ,digest :"+"n%%10.3f%10.3f%103" , new Object[0]);
            }
        });

    }
}
