package com.example.myapp.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapp.R;
import com.example.myapp.data.model.BusRouteStopDetail;
import com.example.myapp.data.model.BusRouteStopETAInfo;
import com.example.myapp.databinding.FragmentBusRouteInfoBinding;
import com.example.myapp.factory.BusRouteListViewModelFactory;
import com.example.myapp.ui.adapter.BusStopItemAdapter;
import com.example.myapp.util.LocationUtils;
import com.example.myapp.viewmodel.BusRouteViewModel;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class BusRouteInfoFragment extends Fragment{
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
    private LocationRequest locationRequest;

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
    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private List<BusRouteStopDetail> currentStopList;

    private void refreshTimer() {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                int currentPos = viewModel.getCurrentStopPos().getValue();
                if (currentPos >= 0) {
                    String stop_id = viewModel.getStopDetailList().getValue().get(currentPos).stop;
                    viewModel.searchBusRouteStopETAInfo(stop_id, route, service_type);
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
        binding = FragmentBusRouteInfoBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this, factory).get(BusRouteViewModel.class);
        binding.setBusRouteViewModel(viewModel);
        viewModel.searchRouteStopListByRoute(route, direction, service_type);

        adapter = new BusStopItemAdapter(tempList, new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                oldPos = viewModel.getCurrentStopPos().getValue();

                if (oldPos != -1) {
                    timer.cancel();
                }

                viewModel.setCurrentStopPos(position == oldPos ? -1 : position);
                adapter.updateCurrentPos(position == oldPos ? -1 : position);
                adapter.notifyItemChanged(oldPos);
                adapter.notifyItemChanged(position);
            }
        });

        binding.stopList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.stopList.setItemAnimator(null);
        binding.stopList.setAdapter(adapter);

        timer.schedule(timerTask, 1);


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        getLastLocation();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.getStopDetailList().observe(getViewLifecycleOwner(), new Observer<List<BusRouteStopDetail>>() {
            @Override
            public void onChanged(List<BusRouteStopDetail> stopDetailList) {
                adapter.swapItems((ArrayList<BusRouteStopDetail>) stopDetailList);
                currentStopList = stopDetailList;

                for(BusRouteStopDetail detail: currentStopList){
                    LatLng mapStop = new LatLng(detail.latitude,detail.longitude);
                    gMap.addMarker(new MarkerOptions().position(mapStop));
                }

//                BusRouteStopDetail closestStop = currentStopList.get(LocationUtils.findClosestBusStopIndex(currentLocation,currentStopList));
//                LatLng mapStop = new LatLng(closestStop.latitude,closestStop.longitude);
//                gMap.addMarker(new MarkerOptions().position(mapStop));
                Integer closestStopIdx = LocationUtils.findClosestBusStopIndex(currentLocation,currentStopList);
                viewModel.setCurrentStopPos(closestStopIdx);
                adapter.updateCurrentPos(closestStopIdx == oldPos ? -1 : closestStopIdx);
                adapter.notifyItemChanged(closestStopIdx);

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


    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) getContext(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
             com.google.android.gms.location.LocationRequest locationRequest = LocationRequest.create();
             locationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY);
             locationRequest.setInterval(1000);
            LocationCallback locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {
                    super.onLocationResult(locationResult);

                }
            };

            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

        }

        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;

                    SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragmentContainerView);
                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull GoogleMap googleMap) {
                            gMap = googleMap;
                            gMap.getUiSettings().setMyLocationButtonEnabled(true);
                            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            gMap.setMyLocationEnabled(true);
                            gMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                                @Override
                                public boolean onMyLocationButtonClick() {
                                    CheckGps();
                                    return false;
                                }
                            });
                            LatLng mapHongKong = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
//                            gMap.addMarker(new MarkerOptions().position(mapHongKong));
                            gMap.moveCamera(CameraUpdateFactory.newLatLng(mapHongKong));
                            gMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));


                        }
                    });

                }
            }
        });
    }

    private void CheckGps() {

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addAllLocationRequests(Collections.singleton(locationRequest))
                .setAlwaysShow(true);

        Task<LocationSettingsResponse> locationSettingsResponseTask = LocationServices.getSettingsClient(getContext())
                .checkLocationSettings(builder.build());

        locationSettingsResponseTask.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                }catch (ApiException e){
                    if(e.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED){
                        ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                        try {
                            resolvableApiException.startResolutionForResult((Activity) getContext(),101);
                        } catch (IntentSender.SendIntentException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });
    }


}