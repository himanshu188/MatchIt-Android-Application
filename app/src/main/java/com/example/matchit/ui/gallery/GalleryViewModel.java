package com.example.matchit.ui.gallery;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GalleryViewModel extends ViewModel {

    private DynamicListActivity mText;

    public GalleryViewModel() {
        mText = new DynamicListActivity();
    }

}