package com.roadstar.customerr.app.module.ui.booking_history;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.roadstar.customerr.R;
import com.roadstar.customerr.app.business.BaseItem;
import com.roadstar.customerr.app.data.models.Booking;
import com.roadstar.customerr.app.data.models.CombineHistoryModel;
import com.roadstar.customerr.app.data.models.InternationalHistoryModel;
import com.roadstar.customerr.app.data.models.Progress;
import com.roadstar.customerr.app.network.AppNetworkTask;
import com.roadstar.customerr.app.network.HttpRequestItem;
import com.roadstar.customerr.app.network.HttpResponseItem;
import com.roadstar.customerr.common.base.recycler_view.BaseRecyclerViewActivity;
import com.roadstar.customerr.common.base.recycler_view.BaseRecyclerViewHolder;
import com.roadstar.customerr.common.utils.AppUtils;
import com.roadstar.customerr.common.utils.NetworkUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.roadstar.customerr.common.utils.ApiConstants.BOOKING_HISTORY;

public class BookingHistoryActivity extends BaseRecyclerViewActivity {
    private BookingHistoryAdapter adapter;
    RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_history);
        init();
    }

    private void init() {
        setActionBar(getString(R.string.booking_history));
        setupJobRecycleView();
        callGetBookingHistory();
        bindOnClicklistners();
    }

    private void bindOnClicklistners() {

    }

    private void setupJobRecycleView() {

        recyclerView = findViewById(R.id.recycler_view);
        //recyclerView = getRecyclerView();

        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setHasFixedSize(false);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

    }

    private void callGetBookingHistory() {
        addProgressItem();

        HttpRequestItem requestItem = new HttpRequestItem(BOOKING_HISTORY, "");
        requestItem.setHeaderParams(AppUtils.getHeaderParams());
        requestItem.setHttpRequestType(NetworkUtils.HTTP_GET);
        AppNetworkTask appNetworkTask = new AppNetworkTask(null, this);
        appNetworkTask.execute(requestItem);
    }

    @Override
    public void onNetworkSuccess(HttpResponseItem response) {
        super.onNetworkSuccess(response);
        removeProgressItem();
        animate(R.id.layout_main);
        removeProgressItem();

        try {
            JSONObject apiResponse = new JSONObject(response.getResponse());
            if (apiResponse.length() != 0) {



                CombineHistoryModel list = new Gson().fromJson(apiResponse.toString(), new TypeToken<CombineHistoryModel>() {}.getType());
                populateList(list);

            } else {
                checkLoadedItems();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onNetworkError(HttpResponseItem response) {
        super.onNetworkError(response);
        removeProgressItem();
        checkLoadedItems();
    }

    private void addProgressItem() {
        populateItem(new Progress());
    }

    private void populateItem(BaseItem item) {

        if (adapter == null) {
            adapter = new BookingHistoryAdapter(this, null, this);
            recyclerView.setAdapter(adapter);
        }
        adapter.add(item);
    }

    private void removeProgressItem() {
        if (adapter != null && adapter.getAdapterCount() > 0 &&
                adapter.getItemAt(adapter.getAdapterCount() - 1) instanceof Progress) {
            adapter.remove(adapter.getAdapterCount() - 1);
        }
    }

    private void populateList(CombineHistoryModel itemList) {

        if (itemList.getLocalJobs().size() > 0 || itemList.getInternationalJobs().size()>0) {

            ArrayList<Booking> itemListOne = itemList.getLocalJobs();
            ArrayList<InternationalHistoryModel> itemListInternational = itemList.getInternationalJobs();

            CombineHistoryModel allrequest = new CombineHistoryModel();
            allrequest.setLocalJobs(itemListOne);
            allrequest.setInternationalJobs(itemListInternational);

            List<BaseItem> combined = new ArrayList<>();
            combined.addAll(itemListOne);
            combined.addAll(itemListInternational);

            List<BaseItem> allRequestList = new ArrayList<>(combined);


            if (adapter == null) {
                adapter = new BookingHistoryAdapter(this, allRequestList, this);
                recyclerView.setAdapter(adapter);

            } else {
                adapter.addAll(allRequestList);
                adapter.notifyDataSetChanged();
            }
        } else
            checkLoadedItems();


    }

    private void checkLoadedItems() {
        if (getParentView() != null)
            findViewById(R.id.lay_info).setVisibility(isListEmpty() ? View.VISIBLE : View.GONE);
    }

    private boolean isListEmpty() {
        if (adapter == null)
            return true;
        if (adapter.getItemCount() == 0)
            return true;
        return false;
    }

    @Override
    public void onRecyclerViewItemClick(BaseRecyclerViewHolder holder) {
        super.onRecyclerViewItemClick(holder);
//        Jobs jobs = (Jobs) adapter.getItemAt(holder.getAdapterPosition());
//        Bundle bundle = new Bundle();
//        bundle.putParcelable(AppConstants.KEY_JOB, jobs);
//        JobDetailsFragment jobDetailsFragment = new JobDetailsFragment();
//        jobDetailsFragment.setArguments(bundle);
//        ((MainActivity) this).onReplaceFragment(jobDetailsFragment, true);
    }


    @Override
    public void onRefresh() {
        super.onRefresh();
        clearAdapter();
        callGetBookingHistory();
    }

    void clearAdapter() {
        adapter = null;
        recyclerView.setAdapter(null);
        //checkLoadedItems();
    }


}
