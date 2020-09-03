package com.example.salon.Interface;

import com.example.salon.Models.Salon;

import java.util.List;

public interface ITypeLoadListener {
    void onTypeLoadSuccess(List<Salon> salonListList);
    void onTypeLoadFailed(String message);
}
