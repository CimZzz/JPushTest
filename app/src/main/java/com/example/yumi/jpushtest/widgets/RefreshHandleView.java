package com.example.yumi.jpushtest.widgets;

/**
 * Created by CimZzz(王彦雄) on 2017/11/30.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */

import android.view.View;
import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by CimZzz(王彦雄) on 16/12/2.<br>
 * Since : StormLive_v0.0.1 <br>
 * Company : Virtual-Lightning.com<br>
 * Description : <br>
 * 描述
 */
public class RefreshHandleView extends FrameLayout {
    private static final byte STATE_CONTENT = 0;
    private static final byte STATE_LOADING = 1;
    private static final byte STATE_LOADFAILED = 2;
    private static final byte STATE_EMPTY = 3;
    private static final byte STATE_INIT = 4;

    private static final String TAG_CONTENT = "Content";
    private static final String TAG_LOADING = "Loading";
    private static final String TAG_LOADFAILED = "LoadFailed";
    private static final String TAG_EMPTY = "Empty";

    private View contentView;
    private View loadingView;
    private View loadFailedView;
    private View emptyView;

    private byte state;

    private IRefreshHandleCallBack refreshHandleCallBack;

    public RefreshHandleView(Context context, AttributeSet attrs) {
        super(context, attrs);

        state = STATE_INIT;
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        switch ((String)child.getTag()) {
            case TAG_CONTENT:
                if(contentView != null)
                    return;

                contentView = child;
                child.setVisibility(this.state == STATE_CONTENT ? VISIBLE : INVISIBLE);
                break;
            case TAG_LOADING:
                if(loadingView != null)
                    return;

                loadingView = child;
                child.setVisibility(this.state == STATE_LOADING || this.state == STATE_INIT ? VISIBLE : INVISIBLE);
                break;
            case TAG_LOADFAILED:
                if(loadFailedView != null)
                    return;

                loadFailedView = child;
                child.setVisibility(this.state == STATE_LOADFAILED ? VISIBLE : INVISIBLE);
                break;
            case TAG_EMPTY:
                if(emptyView != null)
                    return;

                emptyView = child;
                child.setVisibility(this.state == STATE_EMPTY ? VISIBLE : INVISIBLE);
                break;
            default:return;
        }

        super.addView(child, index, params);
    }

    private void handleDisplay(boolean isAppear) {
        switch (state) {
            case STATE_CONTENT:
                if(contentView != null) {
                    if(this.refreshHandleCallBack != null) {
                        if(isAppear)
                            this.refreshHandleCallBack.onContentViewAppear(contentView);
                        else this.refreshHandleCallBack.onContentViewDisappear(contentView);
                    }

                    contentView.setVisibility(isAppear ? VISIBLE : INVISIBLE);
                }
                break;
            case STATE_LOADING:
                if(loadingView != null) {
                    if(this.refreshHandleCallBack != null)
                        if(isAppear)
                            this.refreshHandleCallBack.onLoadingViewAppear(loadingView);
                        else this.refreshHandleCallBack.onLoadingViewDisappear(loadingView);

                    loadingView.setVisibility(isAppear ? VISIBLE : INVISIBLE);
                }
                break;
            case STATE_LOADFAILED:
                if(loadFailedView != null) {
                    if(this.refreshHandleCallBack != null)
                        if(isAppear)
                            this.refreshHandleCallBack.onLoadFailedViewAppear(loadFailedView);
                        else this.refreshHandleCallBack.onLoadFailedViewDisappear(loadFailedView);

                    loadFailedView.setVisibility(isAppear ? VISIBLE : INVISIBLE);
                }
                break;
            case STATE_EMPTY:
                if(emptyView != null) {
                    if(this.refreshHandleCallBack != null)
                        if(isAppear)
                            this.refreshHandleCallBack.onEmptyViewAppear(emptyView);
                        else this.refreshHandleCallBack.onEmptyViewDisappear(emptyView);


                    emptyView.setVisibility(isAppear ? VISIBLE : INVISIBLE);
                }
                break;
        }
    }

    private synchronized void stateChange(byte curState) {
        if(state == curState)
            return;

        handleDisplay(false);

        state = curState;

        handleDisplay(true);
    }

    public void refreshComplete() {
        stateChange(STATE_CONTENT);
    }

    public void refreshing() {
        stateChange(STATE_LOADING);
    }

    public void refreshFailed() {
        stateChange(STATE_LOADFAILED);
    }

    public void empty() {
        stateChange(STATE_EMPTY);
    }

    public boolean isInit() {return state == STATE_INIT;}

    public void setRefreshHandleCallBack(IRefreshHandleCallBack refreshHandleCallBack) {
        this.refreshHandleCallBack = refreshHandleCallBack;

        handleDisplay(true);
    }


    public static abstract class IRefreshHandleCallBack {
        protected void onContentViewAppear(View contentView) {}
        protected void onContentViewDisappear(View contentView) {}

        protected void onLoadingViewAppear(View loadingView) {}
        protected void onLoadingViewDisappear(View loadingView) {}

        protected void onLoadFailedViewAppear(View loadFailedView) {}
        protected void onLoadFailedViewDisappear(View loadFailedView) {}

        protected void onEmptyViewAppear(View emptyView) {}
        protected void onEmptyViewDisappear(View emptyView) {}
    }
}
