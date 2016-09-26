package com.uit.nst95.quanlycuocdidong.Activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uit.nst95.quanlycuocdidong.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CheckByDayFragment extends Fragment {


    public CheckByDayFragment() {
        // Required empty public constructor
    }

    public static CheckByDayFragment newInstance() {
        CheckByDayFragment f = new CheckByDayFragment();

        /*Bundle args = new Bundle();

        args.putString(KEY_TITLE, title);
        f.setArguments(args);*/

        return (f);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_check_by_day, container, false);
    }

}
