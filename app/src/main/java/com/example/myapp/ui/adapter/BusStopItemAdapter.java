package com.example.myapp.ui.adapter;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.data.model.BusRouteStopDetail;
import com.example.myapp.data.model.BusRouteStopETAInfo;
import com.example.myapp.databinding.StopListItemBinding;
import com.example.myapp.ui.fragment.BusRouteInfoFragment;
import com.example.myapp.viewmodel.BusRouteViewModel;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BusStopItemAdapter extends RecyclerView.Adapter<BusStopItemAdapter.BusStopItemViewHolder>{
    private BusRouteInfoFragment.OnItemClickListener clickListener;
    private final ArrayList<BusRouteStopDetail> stopList;
    private Integer currentPos;
    private ArrayList<BusRouteStopETAInfo> currentETAList = new ArrayList<>();

    public BusStopItemAdapter(ArrayList<BusRouteStopDetail> stopList, BusRouteInfoFragment.OnItemClickListener listener){
        super();
        this.stopList = stopList;
        this.currentPos = -1;
        this.clickListener = listener;
    }

    @NonNull
    @Override
    public BusStopItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        StopListItemBinding binding = StopListItemBinding.inflate(layoutInflater, parent, false);

        return new BusStopItemAdapter.BusStopItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BusStopItemViewHolder holder, int position) {
        holder.bind(this.stopList.get(position));
        holder.binding.listNo.setText(String.valueOf(position + 1) + ". ");
        holder.binding.etaBlock.setVisibility(position == this.currentPos ? View.VISIBLE : View.GONE);

        this.setETAs(holder.binding);

        holder.itemView.setOnClickListener(v -> {
            clickListener.onItemClick(position);
        });
    }

    @Override
    public int getItemCount() {
        return this.stopList.size();
    }

    public class BusStopItemViewHolder extends RecyclerView.ViewHolder{
        private StopListItemBinding binding;

        BusStopItemViewHolder(StopListItemBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(BusRouteStopDetail detail) {
            binding.setBusRouteStopDetail(detail);
            binding.executePendingBindings();
        }

    }
    public void clearItems() {
        int size = this.stopList.size();
        this.stopList.clear();
        this.notifyItemRangeRemoved(0, size);
    }

    public void swapItems(ArrayList<BusRouteStopDetail> newItems) {
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffCallback(this.stopList, newItems));
        this.stopList.clear();
        this.stopList.addAll(newItems);
        result.dispatchUpdatesTo(this);
    }

    public void updateCurrentPos(Integer pos){
        this.currentPos = pos;
    }

    public void updateETAList(List<BusRouteStopETAInfo> newItems) {
        this.currentETAList.clear();
        this.currentETAList.addAll(newItems);

        notifyItemChanged(this.currentPos);
    }

    private static long getMinutesFromNow(LocalDateTime targetDateTime) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime now = null;
            now = LocalDateTime.now();
            Duration duration =  Duration.between(now, targetDateTime);
            long minutes = duration.toMinutes();
            return minutes < 0? 0: minutes;
        }
        return 0;
    }

    private void setETAs(StopListItemBinding binding){
        String targetDateTimeString;
        LocalDateTime targetDateTime;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            if(this.currentPos >= 0 && this.currentETAList.size() == 3){
                targetDateTimeString = this.currentETAList.get(0).eta;
                targetDateTime = LocalDateTime.parse(targetDateTimeString, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                String eta1 = getMinutesFromNow(targetDateTime) == 0? "-": String.valueOf(getMinutesFromNow(targetDateTime));

                targetDateTimeString = this.currentETAList.get(1).eta;
                targetDateTime = LocalDateTime.parse(targetDateTimeString, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                String eta2 = getMinutesFromNow(targetDateTime) == 0? "-": String.valueOf(getMinutesFromNow(targetDateTime));

                targetDateTimeString = this.currentETAList.get(2).eta;
                targetDateTime = LocalDateTime.parse(targetDateTimeString, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                String eta3 = getMinutesFromNow(targetDateTime) == 0? "-": String.valueOf(getMinutesFromNow(targetDateTime));

                binding.setEta1(eta1);
                binding.setEta2(eta2);
                binding.setEta3(eta3);
            }
        }
    }

    private class DiffCallback extends DiffUtil.Callback {

        private List<BusRouteStopDetail> mOldList;
        private List<BusRouteStopDetail> mNewList;

        DiffCallback(List<BusRouteStopDetail> oldList, List<BusRouteStopDetail> newList) {
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
            BusRouteStopDetail oldId = mOldList.get(oldItemPosition);
            BusRouteStopDetail newId = mNewList.get(newItemPosition);
            return Objects.equals(oldId,newId);
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            BusRouteStopDetail oldRepo = mOldList.get(oldItemPosition);
            BusRouteStopDetail newRepo = mNewList.get(newItemPosition);
            return Objects.equals(oldRepo,newRepo);
        }
    }

}
