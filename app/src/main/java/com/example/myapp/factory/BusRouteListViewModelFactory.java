package com.example.myapp.factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapp.data.BusRouteModel;
import com.example.myapp.viewmodel.BusRouteViewModel;

public class BusRouteListViewModelFactory implements ViewModelProvider.Factory {
    private BusRouteModel model;
    public BusRouteListViewModelFactory() {
        this.model = new BusRouteModel();
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(BusRouteViewModel.class)) {
            return (T) new BusRouteViewModel(model);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
