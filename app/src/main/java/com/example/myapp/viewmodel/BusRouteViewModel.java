package com.example.myapp.viewmodel;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapp.data.BusRouteModel;
import com.example.myapp.data.model.BusRouteInfo;
import com.example.myapp.data.model.BusRouteStopDetail;
import com.example.myapp.data.model.BusRouteStopETAInfo;
import com.example.myapp.data.model.BusRouteStopInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class BusRouteViewModel extends ViewModel {

    private BusRouteModel busRouteModel = new BusRouteModel();
    public final ObservableBoolean isLoading = new ObservableBoolean(false);
    private final MutableLiveData<List<BusRouteInfo>> busRouteInfoList = new MutableLiveData<>();
    private ArrayList<BusRouteInfo> fullRouteList = new ArrayList<>();

    private final MutableLiveData<List<BusRouteStopInfo>> currentRouteStopList = new MutableLiveData<>();

    private final MutableLiveData<List<BusRouteStopDetail>> currentRouteStopDetailList = new MutableLiveData<>();

    private final MutableLiveData<Integer> currentBusStopPos = new MutableLiveData<>(-1);

    private final MutableLiveData<List<BusRouteStopETAInfo>> currentBusRouteStopETAList = new MutableLiveData<>();

    public BusRouteViewModel(BusRouteModel model) {
        super();
        this.busRouteModel = model;
    }

    public LiveData<List<BusRouteInfo>> getRouteList(){
        return  busRouteInfoList;
    }
    public LiveData<List<BusRouteStopInfo>> getCurrentRouteStopList(){ return currentRouteStopList; }
    public LiveData<List<BusRouteStopDetail>> getStopDetailList(){ return currentRouteStopDetailList; }
    public LiveData<List<BusRouteStopETAInfo>> getCurrentBusRouteStopETAList(){ return currentBusRouteStopETAList; }

    public void searchBusRoute(){
        isLoading.set(true);
        busRouteModel.searchBusRoute(new BusRouteModel.onDataReadyCallback(){
            @Override
            public void onDataReady(List<BusRouteInfo> data) {
                busRouteInfoList.setValue(data);
                fullRouteList = (ArrayList<BusRouteInfo>) data;
                isLoading.set(false);
            }
        });

    }

    public void searchRouteStopListByRoute(String route,String direction,String service_type){
        isLoading.set(true);
        busRouteModel.searchRouteStopListByRoute(route,direction,service_type,new BusRouteModel.onRouteStopDataReadyCallback(){
            @Override
            public void onDataReady(List<BusRouteStopInfo> data) {
                System.out.println("Searched All Stop ID");
                currentRouteStopList.setValue(data);
                searchBusRouteStopDetailByStop();
            }
        });
    }

    public void searchBusRouteStopDetailByStop(){
        AtomicInteger completionCounter = new AtomicInteger(0);
        int totalRequests = currentRouteStopList.getValue().size();
        ArrayList<BusRouteStopDetail> details = new ArrayList<>();

        for (BusRouteStopInfo info: this.getCurrentRouteStopList().getValue()) {
            details.add(null);
            System.out.println(info.seq);
            busRouteModel.searchBusRouteStopDetail(info.stop,new BusRouteModel.onRouteStopDetailDataReadyCallback(){
                @Override
                public void onDataReady(BusRouteStopDetail data) {
                    details.set(Integer.parseInt(info.seq)-1,data);
//                    System.out.println(info.seq + data.nameTc);
                    int count = completionCounter.incrementAndGet();
                    if (count == totalRequests) {
                        System.out.println("Get All the Stop");
                        currentRouteStopDetailList.setValue(details);
                        isLoading.set(false);
                    }
                }
            });
        }

    }

    public ArrayList<BusRouteInfo> getFullRouteList() {
        return fullRouteList;
    }

    public LiveData<Integer> getCurrentBusStop() {
        return currentBusStopPos;
    }

    public void setCurrentBusStop(int pos) {
        this.currentBusStopPos.setValue(pos);
    }


    public LiveData<Integer> getCurrentStopPos() {
        return currentBusStopPos;
    }

    public void setCurrentStopPos(Integer currentStopPos) {
        this.currentBusStopPos.setValue(currentStopPos);
    }


    public void searchBusRouteStopETAInfo(String stop_id, String route, String service_type){
        busRouteModel.searchBusRouteStopETAInfo(stop_id,route,service_type,new BusRouteModel.onRouteStopETAInfoDataReadyCallback(){
            @Override
            public void onDataReady(List<BusRouteStopETAInfo> data) {
                currentBusRouteStopETAList.setValue(data);
            }
        });
    }


}
