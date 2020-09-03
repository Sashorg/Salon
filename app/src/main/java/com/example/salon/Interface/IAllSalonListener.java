package com.example.salon.Interface;

import java.util.List;

public interface IAllSalonListener {
    void onAllSalonLoadSuccess(List<String> areaNameList);
    void onAllSalonLoadFailed(String message);

}
