package com.garbagebin.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import garbagebin.com.garbagebin.R;

/**
 * Created by sharanjeet on 1/10/15.
 */
public class Cart_Fragment extends Fragment {

    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cart_layout,container,false);
        context = getActivity();
        return view;    }
}
