package com.roadstar.customerr.common.base.recycler_view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.roadstar.customerr.R;
import com.roadstar.customerr.app.business.BaseItem;
import com.roadstar.customerr.app.data.models.Empty;
import com.roadstar.customerr.app.data.models.Header;

import java.util.ArrayList;
import java.util.List;

/**
 * Base view adapter for managing {@link RecyclerView.Adapter}
 */
abstract public class BaseRecyclerViewAdapter extends RecyclerView.Adapter<BaseRecyclerViewHolder> implements View.OnClickListener {

    /**
     * Contains all items for recycler view
     */
    private List<BaseItem> items = null;

    /**
     * RecyclerView single item view and/or (maybe) single element inside RecyclerView click listener
     */
    private com.roadstar.customerr.common.base.recycler_view.OnRecyclerViewItemClickListener itemClickListener = null;

    public BaseRecyclerViewAdapter(List<BaseItem> items, com.roadstar.customerr.common.base.recycler_view.OnRecyclerViewItemClickListener itemClickListener) {
        this.items = items;
        this.itemClickListener = itemClickListener;
    }

    /**
     * If an item view has large or expensive data bound to it such as large bitmaps,
     * this may be a good place to release those resources.
     *
     * @param holder The ViewHolder for the view being recycled
     */
    @Override
    public void onViewRecycled(BaseRecyclerViewHolder holder) {
        super.onViewRecycled(holder);
    }

    /**
     * Override this method if you want to implement click listener for your recycler view's resources
     * like if you want to add click listener for Button/Image inside your single recycler view item.
     *
     * @param v view
     */
    @Override
    public void onClick(View v) {
    }

    protected String getClassName() {
        return getClass().getSimpleName();
    }

    /**
     * @return get all adapter's items
     */
    public List<BaseItem> getAdapterItems() {
        return items;
    }

    public int getAdapterCount() {
        return items == null ? 0 : items.size();
    }

    /**
     * @param item add single adapter into list
     */
    public void add(BaseItem item) {
        if (items == null)
            items = new ArrayList<>();
        items.add(item);
        notifyItemInserted(items.size() - 1);
    }

    /**
     * @param newItems add list of items into adapter
     */
    public void addAll(List<BaseItem> newItems) {
        if (items == null)
            items = new ArrayList<>();

        for (BaseItem item : newItems) {
            items.add(item);
            notifyItemInserted(items.size() - 1);
        }
    }

    /**
     * @param position to update
     * @param item     update item at given position
     */
    public void update(int position, BaseItem item) {
        items.set(position, item);
    }


    public void clearItems() {
        if (items != null) {
            int size = items.size();
            if (size > 0) {
                for (int i = 0; i < size; i++)
                    items.remove(0);
                notifyItemRangeRemoved(0, size);
            }
        }
    }

    /**
     * Remove item from recycler view
     *
     * @param item {@link BaseItem}
     */
    public void remove(BaseItem item) {
        if (items == null)
            return;
        items.remove(item);
    }

    /**
     * Remove item from recycler view
     *
     * @param position {@link BaseItem position}
     */
    public void remove(int position) {
        if (items == null)
            return;
        items.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, items.size());
    }

    /**
     * Get {@link BaseItem} at given index
     *
     * @param position index
     * @return Adapter item
     */
    public BaseItem getItemAt(int position) {
        return items == null ? null : items.get(position);
    }

    public OnRecyclerViewItemClickListener getItemClickListener() {
        return itemClickListener;
    }

    public void clearResources() {
        clearItems();
        items = null;
        itemClickListener = null;
    }

    @NonNull
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        BaseRecyclerViewHolder holder = null;
        if (viewType == BaseItem.ITEM_EMPTY) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_empty, parent, false);
            holder = new EmptyViewHolder(view);
        } else if (viewType == BaseItem.ITEM_PROGRESS) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_progress, parent, false);
            holder = new ProgressViewHolder(view);
        } else if (viewType == BaseItem.ITEM_HEADER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_header, parent, false);
            holder = new HeaderViewHolder(view);
        } else {
            return createSpecificViewHolder(parent, viewType);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseRecyclerViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        if (holder instanceof EmptyViewHolder) {
            EmptyViewHolder emptyViewHolder = (EmptyViewHolder) holder;
            Empty empty = (Empty) getItemAt(position);
            emptyViewHolder.textViewEmpty.setText(empty.getMessage());
        } else if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            Header header = (Header) getItemAt(position);
            headerViewHolder.textHeader.setText(header.getTitle());
        } else if (holder instanceof ProgressViewHolder) {
        } else
            bindSpecificViewHolder(holder, position, payloads);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseRecyclerViewHolder holder, int position) {

    }

    public abstract BaseRecyclerViewHolder createSpecificViewHolder(@NonNull ViewGroup parent, int viewType);

    public abstract void bindSpecificViewHolder(@NonNull BaseRecyclerViewHolder holder, int position, @NonNull List<Object> payloads);

    @Override
    public int getItemViewType(int position) {
        return getItemAt(position).getItemType();
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    private class ProgressViewHolder extends BaseRecyclerViewHolder {
        private ProgressViewHolder(View view) {
            super(view);
        }

        @Override
        protected BaseRecyclerViewHolder populateView() {
            return this;
        }
    }


    private class EmptyViewHolder extends BaseRecyclerViewHolder {
        private TextView textViewEmpty;

        private EmptyViewHolder(View view) {
            super(view);
            textViewEmpty = view.findViewById(R.id.tv_message);
        }

        @Override
        protected BaseRecyclerViewHolder populateView() {
            return this;
        }
    }

    private class HeaderViewHolder extends BaseRecyclerViewHolder {
        private TextView textHeader;

        private HeaderViewHolder(View view) {
            super(view);
            textHeader = view.findViewById(R.id.tv_title);
        }

        @Override
        protected BaseRecyclerViewHolder populateView() {
            return this;
        }
    }

}
