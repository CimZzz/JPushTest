package com.example.yumi.jpushtest.widgets;

/**
 * Created by CimZzz(王彦雄) on 2017/11/22.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */

import android.util.FloatMath;
import android.view.View;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.handmark.pulltorefresh.library.PullToRefreshBase;

/**
 * Created by CimZzz(王彦雄) on 16/12/1.<br>
 * Since : StormLive_v0.0.1 <br>
 * Company : Virtual-Lightning.com<br>
 * Description : <br>
 * 描述
 */
public class RecyclerRefreshView extends PullToRefreshBase<View> {
    public RecyclerView recyclerView;
    public RecyclerRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setScrollingWhileRefreshingEnabled(true);
        getLoadingLayoutProxy(true,false).setRefreshingLabel("正在获取数据中...");
        getLoadingLayoutProxy(true,false).setPullLabel("下拉获取更多历史记录...");
        getLoadingLayoutProxy(true,false).setReleaseLabel("松开以更新");
        recyclerView.setOverScrollMode(OVER_SCROLL_NEVER);
        recyclerView.setFadingEdgeLength(0);
        recyclerView.setVerticalFadingEdgeEnabled(false);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Boolean isLastItem = isListViewLastItemVisible();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(isLastItem)
            recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
    }
    @Override
    public Orientation getPullToRefreshScrollDirection() {
        return Orientation.VERTICAL;
    }
    @Override
    protected View createRefreshableView(Context context, AttributeSet attrs) {
        recyclerView = new RecyclerView(context);
        recyclerView.setLayoutManager(new GridLayoutManager(context,3));
        return recyclerView;
    }
    @Override
    protected boolean isReadyForPullEnd() {
        return isListViewLastItemVisible();
    }
    @Override
    protected boolean isReadyForPullStart() {
        return isListViewFirstItemVisible();
    }
    public boolean isListViewLastItemVisible() {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        int length;
        if (adapter == null || (length = adapter.getItemCount()) == 0)
            return false;
        View lastView = recyclerView.getChildAt(recyclerView.getChildCount() - 1);
        if (lastView == null)
            return false;
        int indexOfAdapter = recyclerView.getChildAdapterPosition(lastView);
        return indexOfAdapter == length - 1 && lastView.getBottom() <= recyclerView.getBottom();
    }
    private boolean isListViewFirstItemVisible() {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter == null || adapter.getItemCount() == 0)
            return false;
        View firstView = recyclerView.getChildAt(0);
        if (firstView == null)
            return false;
        int indexOfAdapter = recyclerView.getChildAdapterPosition(firstView);
        return indexOfAdapter == 0 && firstView.getTop() <= recyclerView.getTop();
    }
    public void setAdapter(RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
    }
    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {recyclerView.setLayoutManager(layoutManager);}
}