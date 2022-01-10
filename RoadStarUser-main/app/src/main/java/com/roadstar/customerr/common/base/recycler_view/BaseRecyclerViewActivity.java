package com.roadstar.customerr.common.base.recycler_view;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.roadstar.customerr.R;
import com.roadstar.customerr.app.network.HttpResponseItem;
import com.roadstar.customerr.common.base.BaseActivity;
import com.roadstar.customerr.common.utils.Logger;

/**
 * Created by $Bilal on 7/19/2017.
 */

public class BaseRecyclerViewActivity extends BaseActivity implements
        OnRecyclerViewItemClickListener, SwipeRefreshLayout.OnRefreshListener{

    // region VARIABLES
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;

    // region VARIABLES
    protected static final int LINEAR_LAYOUT_MANAGER = 0;
    protected static final int GRID_LAYOUT_MANAGER = 1;
    protected static final int STAGGERED_GRID_LAYOUT_MANAGER = 2;
    // endregion

    // region LIFE_CYCLE

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        recyclerView = findViewById(R.id.recycler_view);
        refreshLayout = findViewById(R.id.swipe_refresh);
        if (refreshLayout != null)
            refreshLayout.setOnRefreshListener(this);
    }

    public boolean isRefreshVisible(){
        return refreshLayout != null && refreshLayout.isRefreshing();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (recyclerView != null) {
            BaseRecyclerViewAdapter adapter = (BaseRecyclerViewAdapter) recyclerView.getAdapter();
            if (adapter != null)
                adapter.clearResources();
            recyclerView.setAdapter(null);
        }
    }
    // endregion

    /**
     * Returns name of current fragment
     *
     * @return BaseRecyclerViewFragment name
     */
    public String getRecyclerViewFragmentTag() {
        return getClass().getSimpleName();
    }

    /**
     * Get reference of RecyclerView
     *
     * @return {@link RecyclerView}
     */
    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    /**
     * Get reference of SwipeRefreshLayout
     *
     * @return {@link SwipeRefreshLayout}
     */
    public SwipeRefreshLayout getSwipeLayout() {
        return refreshLayout;
    }

    /**
     * Set adapter
     *
     * @param adapter {@link BaseRecyclerViewAdapter}
     */
    protected void setAdapter(BaseRecyclerViewAdapter adapter) {
        if (recyclerView != null) {
            recyclerView.setAdapter(adapter);
        }
    }

    // region LAYOUT_MANAGER_INFO
    protected BaseRecyclerViewActivity.CurrentLayoutManagerInfo getLayoutManagerInfo(RecyclerView recyclerView) {
        if (recyclerView == null)
            return null;
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        BaseRecyclerViewActivity.CurrentLayoutManagerInfo currentLayoutManagerInfo =
                new BaseRecyclerViewActivity.CurrentLayoutManagerInfo();
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager manager = (GridLayoutManager) layoutManager;
            currentLayoutManagerInfo.setFirstVisiblePosition(manager.findFirstVisibleItemPosition());
            currentLayoutManagerInfo.setLastVisiblePosition(manager.findLastVisibleItemPosition());
            currentLayoutManagerInfo.setCurrentLayoutManager(GRID_LAYOUT_MANAGER);
        } else if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager manager = (LinearLayoutManager) layoutManager;
            currentLayoutManagerInfo.setFirstVisiblePosition(manager.findFirstVisibleItemPosition());
            currentLayoutManagerInfo.setLastVisiblePosition(manager.findLastVisibleItemPosition());
            currentLayoutManagerInfo.setCurrentLayoutManager(LINEAR_LAYOUT_MANAGER);
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager manager = (StaggeredGridLayoutManager) layoutManager;
            currentLayoutManagerInfo.setFirstVisiblePosition(manager.findFirstVisibleItemPositions(null)[0]);
            currentLayoutManagerInfo.setLastVisiblePosition(manager.findLastVisibleItemPositions(null)[0]);
            currentLayoutManagerInfo.setCurrentLayoutManager(STAGGERED_GRID_LAYOUT_MANAGER);
        }
        return currentLayoutManagerInfo;
    }

    @Override
    public void onRefresh() {
    }



    final protected class CurrentLayoutManagerInfo {

        private int firstVisiblePosition = 0, lastVisiblePosition = 0;
        private int currentLayoutManager = LINEAR_LAYOUT_MANAGER;

        public int getFirstVisiblePosition() {
            return firstVisiblePosition;
        }

        public void setFirstVisiblePosition(int firstVisiblePosition) {
            this.firstVisiblePosition = firstVisiblePosition;
        }

        public int getLastVisiblePosition() {
            return lastVisiblePosition;
        }

        public void setLastVisiblePosition(int lastVisiblePosition) {
            this.lastVisiblePosition = lastVisiblePosition;
        }

        public int getCurrentLayoutManager() {
            return currentLayoutManager;
        }

        public void setCurrentLayoutManager(int currentLayoutManager) {
            this.currentLayoutManager = currentLayoutManager;
        }
    }
    // endregion

    // region RECYCLER_VIEW

    /**
     * Returns name of current fragment
     *
     * @return BaseRecyclerViewFragment name
     */
    public String getRecyclerViewTag() {
        return getClass().getSimpleName();
    }


    /**
     * @param holder view holder on clicked item
     */
    @Override
    public void onRecyclerViewItemClick(BaseRecyclerViewHolder holder) {
        Logger.info(getRecyclerViewTag(), "onRecyclerViewItemClick adapter position " +
                holder.getAdapterPosition() + " layout position " + holder.getLayoutPosition());
    }

    /**
     * @param holder     view holder on clicked item
     * @param resourceId resource id of clicked item
     */
    @Override
    public void onRecyclerViewChildItemClick(BaseRecyclerViewHolder holder, int resourceId) {
        Logger.info(getRecyclerViewTag(), "onRecyclerViewChildItemClick adapter position " +
                holder.getAdapterPosition() + " layout position " + holder.getLayoutPosition() +
                ", resourceId " + resourceId);
    }
    // endregion

    @Override
    public void onNetworkResponse(HttpResponseItem response) {
        super.onNetworkResponse(response);
        if (refreshLayout != null)
            refreshLayout.setRefreshing(false);
    }

    public void stopRefresh() {
        if (refreshLayout == null)
            return;
        refreshLayout.setRefreshing(false);
    }
}
