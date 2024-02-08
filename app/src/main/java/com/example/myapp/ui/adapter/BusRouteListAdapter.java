package com.example.myapp.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.compose.ui.text.input.InputMethodManager;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.R;
import com.example.myapp.data.model.BusRouteInfo;
import com.example.myapp.databinding.RouteListItemBinding;
import com.example.myapp.ui.fragment.BusRouteInfoFragment;
import com.example.myapp.ui.fragment.BusRouteListFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BusRouteListAdapter extends RecyclerView.Adapter<BusRouteListAdapter.BusRouteViewHolder> {

    private ArrayList<BusRouteInfo> routeList;

    public BusRouteListAdapter(ArrayList<BusRouteInfo> routeList){
        this.routeList = routeList;
    }

    public class BusRouteViewHolder extends RecyclerView.ViewHolder{
        private RouteListItemBinding binding;

        BusRouteViewHolder(RouteListItemBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }

    }

    @NonNull
    @Override
    public BusRouteListAdapter.BusRouteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RouteListItemBinding binding = RouteListItemBinding.inflate(layoutInflater, parent, false);
        return new BusRouteViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BusRouteViewHolder holder, int position) {
        BusRouteInfo route = routeList.get(position);
        holder.binding.routeNo.setText(route.route);
        holder.binding.desc.setText(route.destinationTraditionalChinese);

        holder.binding.busRouteBlock.setOnClickListener(v -> {
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            String tag = BusRouteInfoFragment.TAG;

            if (activity.getSupportFragmentManager().findFragmentByTag(tag) == null) {
                BusRouteInfoFragment fragment = BusRouteInfoFragment.newInstance(
                        route.route,
                        route.bound.equals("I") ? "inbound" : "outbound",
                        route.serviceType
                );
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment, tag)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return routeList.size();
    }

    public void clearItems() {
        int size = this.routeList.size();
        this.routeList.clear();
        this.notifyItemRangeRemoved(0, size);
    }

    public void swapItems(ArrayList<BusRouteInfo> newItems) {
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffCallback(this.routeList, newItems));
        this.routeList.clear();
        this.routeList.addAll(newItems);
        result.dispatchUpdatesTo(this);
    }

    private class DiffCallback extends DiffUtil.Callback {

        private List<BusRouteInfo> mOldList;
        private List<BusRouteInfo> mNewList;

        DiffCallback(List<BusRouteInfo> oldList, List<BusRouteInfo> newList) {
            this.mOldList = oldList;
            this.mNewList = newList;
        }

        @Override
        public int getOldListSize() {
            return mOldList != null ? mOldList.size() : 0;
        }

        @Override
        public int getNewListSize() {
            return mNewList != null ? mNewList.size() : 0;
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            BusRouteInfo oldId = mOldList.get(oldItemPosition);
            BusRouteInfo newId = mNewList.get(newItemPosition);
            return Objects.equals(oldId,newId);
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            BusRouteInfo oldRepo = mOldList.get(oldItemPosition);
            BusRouteInfo newRepo = mNewList.get(newItemPosition);
            return Objects.equals(oldRepo,newRepo);
        }
    }
}
