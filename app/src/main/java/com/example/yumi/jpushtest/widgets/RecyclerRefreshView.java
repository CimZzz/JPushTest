package com.example.yumi.jpushtest.widgets;

/**
 * Created by CimZzz(王彦雄) on 2017/11/22.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */

import android.view.View;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.example.yumi.jpushtest.utils.LogUtilsKt;
import com.handmark.pulltorefresh.library.PullToRefreshBase;

public class RecyclerRefreshView extends PullToRefreshBase<View> {
    public RecyclerView recyclerView;
    public RecyclerRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setScrollingWhileRefreshingEnabled(true);
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
        RecyclerView.LayoutParams i = (RecyclerView.LayoutParams) firstView.getLayoutParams();
        return indexOfAdapter == 0 && ((firstView.getTop() - i.bottomMargin) == recyclerView.getTop());
    }
    public void setAdapter(RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
    }
    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {recyclerView.setLayoutManager(layoutManager);}
}