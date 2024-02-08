package com.example.myapp.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapp.R;
import com.example.myapp.data.model.BusRouteInfo;
import com.example.myapp.data.model.BusRouteStopDetail;
import com.example.myapp.data.model.BusRouteStopETAInfo;
import com.example.myapp.data.model.BusRouteStopInfo;
import com.example.myapp.databinding.FragmentBusRouteInfoBinding;
import com.example.myapp.factory.BusRouteListViewModelFactory;
import com.example.myapp.ui.adapter.BusRouteListAdapter;
import com.example.myapp.ui.adapter.BusStopItemAdapter;
import com.example.myapp.viewmodel.BusRouteViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class BusRouteInfoFragment extends Fragment {

    public static final String TAG = "BusRouteInfo";

    private FragmentBusRouteInfoBinding binding;
    private BusRouteListViewModelFactory factory = new BusRouteListViewModelFactory();

    private BusRouteViewModel viewModel;

    private String route;
    private String direction;
    private String service_type;

    private ArrayList<BusRouteStopDetail> tempList = new ArrayList<>();
    private BusStopItemAdapter adapter;

    private GoogleMap gMap;



    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private Timer timer = new Timer();

    private Integer oldPos = -1;

    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
        }
    };

    private void refreshTimer(){
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                int currentPos = viewModel.getCurrentStopPos().getValue();
                if(currentPos >= 0){
                    String stop_id = viewModel.getStopDetailList().getValue().get(currentPos).stop;
                    viewModel.searchBusRouteStopETAInfo(stop_id,route,service_type);
                }
            }

        };
    }

    public static BusRouteInfoFragment newInstance(String route, String direction, String service_type) {
        BusRouteInfoFragment fragment = new BusRouteInfoFragment();
        Bundle args = new Bundle();
        args.putString("route", route);
        args.putString("direction", direction);
        args.putString("service_type", service_type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            route = getArguments().getString("route");
            direction = getArguments().getString("direction");
            service_type = getArguments().getString("service_type");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBusRouteInfoBinding.inflate(inflater,container,false);
        viewModel = new ViewModelProvider(this, factory).get(BusRouteViewModel.class);
        binding.setBusRouteViewModel(viewModel);
        viewModel.searchRouteStopListByRoute(route,direction,service_type);

        adapter = new BusStopItemAdapter(tempList, new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                oldPos = viewModel.getCurrentStopPos().getValue();

                if(oldPos != -1){
                    timer.cancel();
                }

                viewModel.setCurrentStopPos(position == oldPos? -1 : position);
                adapter.updateCurrentPos(position == oldPos? -1 : position);
                adapter.notifyItemChanged(oldPos);
                adapter.notifyItemChanged(position);
            }
        });

        binding.stopList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.stopList.setItemAnimator(null);
        binding.stopList.setAdapter(adapter);

        timer.schedule(timerTask,1);


//        AppCompatActivity activity = (AppCompatActivity) getContext();
//        SupportMapFragment mapFragment = (SupportMapFragment) activity.getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
//        mapFragment.getMapAsync(this);

        return binding.getRoot();
    }

//    @Override
//    public void onMapReady(@NonNull GoogleMap googleMap) {
//        this.gMap = googleMap;
//
//        LatLng mapIndia = new LatLng(20.5937,78.9629);
//        this.gMap.addMarker(new MarkerOptions().position(mapIndia));
//        this.gMap.moveCamera(CameraUpdateFactory.newLatLng(mapIndia));
//    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        viewModel.getStopDetailList().observe(getViewLifecycleOwner(), new Observer<List<BusRouteStopDetail>>() {
            @Override
            public void onChanged(List<BusRouteStopDetail> stopDetailList) {
                adapter.swapItems((ArrayList<BusRouteStopDetail>) stopDetailList);
            }
        });

        viewModel.getCurrentBusStop().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer pos) {
                if(pos == -1){
                    timer.cancel();
                }else{
                    refreshTimer();
                    timer.schedule(timerTask,0,10000);
                }
            }
        });

        viewModel.getCurrentBusRouteStopETAList().observe(getViewLifecycleOwner(), new Observer<List<BusRouteStopETAInfo>>() {
            @Override
            public void onChanged(List<BusRouteStopETAInfo> infos) {
                adapter.updateETAList(infos);
            }
        });

    }
}