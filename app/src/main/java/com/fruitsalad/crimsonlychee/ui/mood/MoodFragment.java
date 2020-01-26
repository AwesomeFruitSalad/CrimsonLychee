package com.fruitsalad.crimsonlychee.ui.mood;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.fruitsalad.crimsonlychee.R;

public class MoodFragment extends Fragment {

    // TODO: Put the mood stuff here

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_mood, container, false);
        return root;
    }
}