package com.example.ApkPKL.ui.aboutus;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AboutUsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AboutUsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Aplikasi ini merupakan produk Pengabdian Kepada Masyarakat DIPA Fakultas Ilmu Komputer Tahun 2020");
    }

    public LiveData<String> getText() {
        return mText;
    }
}