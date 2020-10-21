package com.example.ApkPKL.ui.aboutus;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AboutUsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AboutUsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Aplikasi untuk driver ambulan yang berfungsi untuk mempermudah pasien dalam menggunakan ambulan /n ©panggilambulan");
    }

    public LiveData<String> getText() {
        return mText;
    }
}