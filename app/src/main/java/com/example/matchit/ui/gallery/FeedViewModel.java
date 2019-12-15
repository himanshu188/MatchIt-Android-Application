package com.example.matchit.ui.gallery;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FeedViewModel extends ViewModel {

    private DynamicListActivity mText;

    public FeedViewModel() {
        mText = new DynamicListActivity();
    }

}