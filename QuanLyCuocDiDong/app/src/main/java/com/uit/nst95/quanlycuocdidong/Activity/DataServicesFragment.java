package com.uit.nst95.quanlycuocdidong.Activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.uit.nst95.quanlycuocdidong.DB.DAO_UsefulNumber;
import com.uit.nst95.quanlycuocdidong.Manager.Data3GArrayAdapter;
import com.uit.nst95.quanlycuocdidong.Manager.Data3GPackage;
import com.uit.nst95.quanlycuocdidong.Manager.PackageNetwork;
import com.uit.nst95.quanlycuocdidong.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class DataServicesFragment extends Fragment {

    private ArrayList<PackageNetwork> arrayListPackage = new ArrayList<PackageNetwork>();
    private Data3GArrayAdapter adapter = null;
    private ListView lstView3GPackage;
    public DataServicesFragment() {
        // Required empty public constructor
    }

    public static DataServicesFragment newInstance() {
        DataServicesFragment f = new DataServicesFragment();
        return (f);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data_services, container, false);

        lstView3GPackage = (ListView) view.findViewById(R.id.listView3GData);
        lstView3GPackage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                /*Bundle myBundle = new Bundle();
                PackageNetwork myPN = (PackageNetwork) parent.getItemAtPosition(position);
                myBundle.putSerializable(DefinedConstant.KEY_PACKAGE, myPN);
                Intent myIntent = new Intent(ChoosePackageActivity.this, MainActivity.class);
                myIntent.putExtra(DefinedConstant.BUNDLE_NAME, myBundle);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(myIntent);*/
            }
        });

        DAO_UsefulNumber dao_usefulNumber = new DAO_UsefulNumber(getContext());
        dao_usefulNumber.Open();
        ArrayList<Data3GPackage> myArrayData3G = dao_usefulNumber.GetAll3GPackage(1);
        dao_usefulNumber.Close();

        adapter = new Data3GArrayAdapter(getActivity(),R.layout.custom_item_listview_data3g, myArrayData3G);
        lstView3GPackage.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        return view;
    }

}
