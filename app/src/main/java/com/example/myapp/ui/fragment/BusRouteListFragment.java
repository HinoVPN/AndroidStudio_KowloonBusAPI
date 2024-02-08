package com.example.myapp.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapp.factory.BusRouteListViewModelFactory;
import com.example.myapp.data.model.BusRouteInfo;
import com.example.myapp.databinding.FragmentBusRouteListBinding;
import com.example.myapp.ui.adapter.BusRouteListAdapter;
import com.example.myapp.viewmodel.BusRouteViewModel;

import java.util.ArrayList;
import java.util.List;

public class BusRouteListFragment extends Fragment {

    public static final String TAG = "BusRouteList";

    private FragmentBusRouteListBinding binding;

    private BusRouteListViewModelFactory factory = new BusRouteListViewModelFactory();

    private BusRouteViewModel viewModel;

    private ArrayList<BusRouteInfo> tempList = new ArrayList<>();
    private BusRouteListAdapter adapter = new BusRouteListAdapter(tempList);


    public static BusRouteListFragment newInstance() {
        return  new BusRouteListFragment();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this, factory).get(BusRouteViewModel.class);

        binding.setBusRouteViewModel(viewModel);
        if(viewModel.getFullRouteList().size() == 0){
            viewModel.searchBusRoute();
        }

        viewModel.getRouteList().observe(getViewLifecycleOwner(), new Observer<List<BusRouteInfo>>() {

            @Override
            public void onChanged(@Nullable List<BusRouteInfo> infos) {
                if(viewModel.getFullRouteList().size() == 0)
                    adapter.swapItems((ArrayList<BusRouteInfo>) infos);
            }
        });

        binding.busNumberInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                doFilter();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBusRouteListBinding.inflate(inflater,container,false);

        binding.busRouteListView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.busRouteListView.setItemAnimator(new DefaultItemAnimator());
        binding.busRouteListView.setAdapter(adapter);

        return binding.getRoot();
    }


    private void doFilter(){
        adapter.clearItems();

        if(binding.busNumberInput.getText().length() == 0){
            adapter.swapItems(viewModel.getFullRouteList());
        }else{

            ArrayList<BusRouteInfo> newRouteList = new ArrayList<>();
            for (BusRouteInfo busRouteInfo : viewModel.getFullRouteList()) {
                if (busRouteInfo.route.contains(binding.busNumberInput.getText())) {
                    newRouteList.add(busRouteInfo);
                }
            }
            adapter.swapItems(newRouteList);
        }
    }

}