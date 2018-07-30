package com.dhy.phel;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

/**
 * 参考 https://github.com/singwhatiwanna/PinnedHeaderExpandableListView
 */
public class PinnedExpandableListView extends ExpandableListView implements OnScrollListener {
    private View pinnedGroupView;
    private int pinnedViewHeight;
    private OnScrollListener onScrollListener;
    private int pinnedGroupViewId;

    public PinnedExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    public PinnedExpandableListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(attrs);
    }

    private void initView(@NonNull AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.PinnedExpandableListView);
        pinnedGroupViewId = typedArray.getResourceId(R.styleable.PinnedExpandableListView_pinnedGroupViewId, View.NO_ID);
        typedArray.recycle();
        setVerticalFadingEdgeEnabled(false);
        setOnScrollListener(this);
    }

    @Override
    public void setOnScrollListener(OnScrollListener onScrollListener) {
        if (onScrollListener != this) {
            this.onScrollListener = onScrollListener;
        } else {
            this.onScrollListener = null;
        }
        super.setOnScrollListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (pinnedGroupView != null) pinnedViewHeight = pinnedGroupView.getMeasuredHeight();
        else initPinnedGroupView();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initPinnedGroupView();
    }

    private void initPinnedGroupView() {
        ViewParent parent = getParent();
        if (parent instanceof View) {
            pinnedGroupView = ((View) parent).findViewById(pinnedGroupViewId);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (pinnedGroupView != null) onMoving(pinnedGroupView.getTop());
    }

    protected void refreshHeader() {
        if (pinnedGroupView == null) return;
        int firstVisiblePos = getFirstVisiblePosition();
        int firstVisibleGroupPos = getPackedPositionGroup(getExpandableListPosition(firstVisiblePos));
        View view = findViewWithTag(firstVisibleGroupPos + 1);//next group
        if (view != null) {
            int top = view.getTop();
            if (top < pinnedViewHeight) {
                onMoving(top - pinnedViewHeight);
            } else {
                onMoving(0);
            }
        } else {
            onMoving(0);
        }
        updatePinnedView(pinnedGroupView, firstVisibleGroupPos);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (onScrollListener != null) onScrollListener.onScrollStateChanged(view, scrollState);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (totalItemCount > 0) refreshHeader();
        if (onScrollListener != null) {
            onScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    private void updatePinnedView(View headerView, int position) {
        ExpandableListAdapter expandableListAdapter = getExpandableListAdapter();
        if (expandableListAdapter != null) {
            expandableListAdapter.getGroupView(position, isGroupExpanded(position), headerView, this);
        }
    }

    private void onMoving(int top) {
        pinnedGroupView.setY(top);
    }
}