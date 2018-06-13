package com.syswin.popwindow.recycleView;

import android.view.View;

import com.syswin.popwindow.R;


/**
 * Created by hushendian on 2018/6/9.
 */

public class PullToRefreshCompact {
    public PullToRefreshCompact() {
    }

    public void operateMoreData(boolean hasMore, LoadingLayout loadingLayout) {
        if (loadingLayout != null) {
            try {
                if (hasMore) {
                    View mHeaderContainer = loadingLayout.findViewById(R.id.footer_pull_to_refresh);
                    View ivLoading1 = loadingLayout.findViewById(R.id.footer_iv_1);
                    View ivLoading2 = loadingLayout.findViewById(R.id.footer_iv_2);
                    View ivLoading3 = loadingLayout.findViewById(R.id.footer_iv_3);
                    View tv_no_more = loadingLayout.findViewById(R.id.tv_no_more);
                    mHeaderContainer.setVisibility(View.VISIBLE);
                    ivLoading1.setVisibility(View.VISIBLE);
                    ivLoading2.setVisibility(View.VISIBLE);
                    ivLoading3.setVisibility(View.VISIBLE);
                    tv_no_more.setVisibility(View.GONE);
                } else {
                    loadingLayout.setState(ILoadingLayout.State.NO_MORE_DATA);
                }
            } catch (Exception var8) {
                var8.printStackTrace();
            }

        }
    }
}
