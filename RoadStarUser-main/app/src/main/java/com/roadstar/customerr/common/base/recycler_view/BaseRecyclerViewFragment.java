package com.roadstar.customerr.common.base.recycler_view;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.roadstar.customerr.R;
import com.roadstar.customerr.app.network.HttpResponseItem;
import com.roadstar.customerr.common.base.BaseFragment;
import com.roadstar.customerr.common.utils.Logger;

import java.util.ArrayList;
import java.util.List;

import static com.roadstar.customerr.common.base.BaseActivity.MULTIPLE_PERMISSIONS;

/**
 * Base fragment for whole application to manage basic functions
 */
abstract public class BaseRecyclerViewFragment extends BaseFragment implements OnRecyclerViewItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    // region VARIABLES
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    protected static final int LINEAR_LAYOUT_MANAGER = 0;
    protected static final int GRID_LAYOUT_MANAGER = 1;
    protected static final int STAGGERED_GRID_LAYOUT_MANAGER = 2;
    String[] permissions = new String[]{
            // Manifest.permission.WRITE_EXTERNAL_STORAGE,
            //  Manifest.permission.READ_EXTERNAL_STORAGE,

            //  Manifest.permission.CAMERA,
            //Manifest.permission.RECORD_AUDIO,
            android.Manifest.permission.CALL_PHONE,
    };// endregion

    // region LIFE_CYCLE
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.getRootView().findViewById(R.id.recycler_view);
        refreshLayout = view.findViewById(R.id.swipe_refresh);
        if (refreshLayout != null)
            refreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (recyclerView != null) {
            BaseRecyclerViewAdapter adapter = (BaseRecyclerViewAdapter) recyclerView.getAdapter();
            if (adapter != null)
                adapter.clearResources();
            recyclerView.setAdapter(null);
        }
    }
    // endregion

    // region SCROLL
    protected void addOnScrollListener() {
        if (recyclerView != null)
            recyclerView.addOnScrollListener(new OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                }
            });
    }
    // endregion

    public void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    // region LAYOUT_MANAGER_INFO
    protected CurrentLayoutManagerInfo getLayoutManagerInfo(RecyclerView recyclerView) {
        if (recyclerView == null)
            return null;
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        CurrentLayoutManagerInfo currentLayoutManagerInfo = new CurrentLayoutManagerInfo();
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

    public RatingBar findRatingBarByID(int resourceID) {

        return getActivity().findViewById(resourceID);
    }

    public boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(getActivity(), p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


    @Override
    public void onRefresh() {
        stopRefresh();
    }

    @Override
    public String getTitle() {
        return null;
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

    /**
     * @param holder view holder on clicked item
     */
    @Override
    public void onRecyclerViewItemClick(BaseRecyclerViewHolder holder) {
        Logger.info(getRecyclerViewFragmentTag(), "onRecyclerViewItemClick adapter position " +
                holder.getAdapterPosition() + " layout position " + holder.getLayoutPosition());
    }

    /**
     * @param holder     view holder on clicked item
     * @param resourceId resource id of clicked item
     */
    @Override
    public void onRecyclerViewChildItemClick(BaseRecyclerViewHolder holder, int resourceId) {
        Logger.info(getRecyclerViewFragmentTag(), "onRecyclerViewChildItemClick adapter position " +
                holder.getAdapterPosition() + " layout position " + holder.getLayoutPosition() +
                ", resourceId " + resourceId);
    }
    // endregion

    public void stopRefresh() {
        if (refreshLayout == null)
            return;
        refreshLayout.setRefreshing(false);
    }

    public void startRefresh() {
        if (refreshLayout == null)
            return;
        refreshLayout.setRefreshing(true);
    }

    @Override
    public void onNetworkResponse(HttpResponseItem response) {
        super.onNetworkResponse(response);
        if (refreshLayout != null)
            refreshLayout.setRefreshing(false);
    }

}