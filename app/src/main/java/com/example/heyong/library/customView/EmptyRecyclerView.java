package com.example.heyong.library.customView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 *
 *
 */

public class EmptyRecyclerView extends RecyclerView {
    private static final String TAG = "EmptyRecyclerView";

    private View emptyView;
    final private AdapterDataObserver observer = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            checkIfEmpty();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            checkIfEmpty();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            checkIfEmpty();
        }
    };

    public EmptyRecyclerView(Context context) {
        super(context);
    }

    public EmptyRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EmptyRecyclerView(Context context, AttributeSet attrs,
                             int defStyle) {
        super(context, attrs, defStyle);
    }

    private void checkIfEmpty() {
        if (emptyView != null && getAdapter() != null) {
            final boolean emptyViewVisible =
                    getAdapter().getItemCount() == 0;
            emptyView.setVisibility(emptyViewVisible ? VISIBLE : GONE);
            setVisibility(emptyViewVisible ? GONE : VISIBLE);
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        final Adapter oldAdapter = getAdapter();
        if (oldAdapter != null) {
            oldAdapter.unregisterAdapterDataObserver(observer);
        }
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(observer);
        }
        checkIfEmpty();
    }


    public void setEmptyView(View emptyView) {
        if(emptyView == null)
            return;
        this.emptyView = emptyView;
        ViewGroup parent = (ViewGroup) this.getParent();
        parent.addView(emptyView,0);
        checkIfEmpty();
    }

    /** usage:
     *  .xml:
     *      ...
     *      <EmptyRecyclerView/>
     *      ...
     *      <EmptyView/>
     *  在xml中添加emptyView，位置随意
     * @param emptyView
     */
    public void setEmptyViewByXML(View emptyView){
        this.emptyView = emptyView;
        checkIfEmpty();
    }

}
