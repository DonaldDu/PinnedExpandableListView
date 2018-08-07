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
    private int pinnedGroupViewId;
    private OnScrollListener onScrollListener;

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
            if (pinnedGroupView != null) pinnedGroupView.setOnClickListener(onClickListener);
        }
    }

    private final View.OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean expanded = isGroupExpanded(pinnedGroupViewPosition);
            if (expanded) collapseGroup(pinnedGroupViewPosition);
            else expandGroup(pinnedGroupViewPosition);
        }
    };

    public void updatePinnedView(boolean force) {
        if (pinnedGroupView == null) return;
        int firstVisiblePos = getFirstVisiblePosition();
        int firstVisibleGroupPos = getPackedPositionGroup(getExpandableListPosition(firstVisiblePos));
        View view = findViewWithTag(firstVisibleGroupPos + 1);//next group
        int y;
        if (view != null) {
            int top = view.getTop();
            if (top < pinnedViewHeight) {
                y = top - pinnedViewHeight;
            } else {
                y = 0;
            }
        } else {
            y = 0;
        }
        updatePinnedView(pinnedGroupView, y, firstVisibleGroupPos, force);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (onScrollListener != null) onScrollListener.onScrollStateChanged(view, scrollState);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (totalItemCount > 0 && pinnedEnable) updatePinnedView(false);
        if (onScrollListener != null) {
            onScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    @Override
    public void setAdapter(ExpandableListAdapter adapter) {
        super.setAdapter(adapter);
        setPinnedEnable(false);
    }

    public void setAdapter(PinnedExpandableListAdapter adapter) {
        super.setAdapter(adapter);
        initPinnedGroupView();
        if (pinnedGroupView != null && pinnedEnable) {
            if (adapter == null || adapter.isEmpty()) {
                pinnedGroupView.setVisibility(GONE);
            } else {
                pinnedGroupView.setVisibility(VISIBLE);
                updatePinnedView(pinnedGroupView, 0, 0, true);
            }
        }
    }

    private boolean pinnedEnable = true;

    public void setPinnedEnable(boolean enable) {
        pinnedEnable = enable;
        if (pinnedGroupView != null) pinnedGroupView.setVisibility(enable ? VISIBLE : GONE);
    }

    private int pinnedGroupViewPosition = -1;

    private void updatePinnedView(@NonNull View pinnedGroupView, int y, int position, boolean force) {
        if (force || pinnedGroupView.getY() != y || pinnedGroupViewPosition != position) {
            pinnedGroupView.setY(y);
            ExpandableListAdapter expandableListAdapter = getExpandableListAdapter();
            if (expandableListAdapter != null) {
                pinnedGroupViewPosition = position;
                expandableListAdapter.getGroupView(position, isGroupExpanded(position), pinnedGroupView, this);
            }
        }
    }
}