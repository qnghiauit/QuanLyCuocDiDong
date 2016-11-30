package com.uit.nst95.quanlycuocdidong.Activity;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.uit.nst95.quanlycuocdidong.DB.DAO_UsefulNumber;
import com.uit.nst95.quanlycuocdidong.Manager.Data3GArrayAdapter;
import com.uit.nst95.quanlycuocdidong.Manager.Data3GPackage;
import com.uit.nst95.quanlycuocdidong.Manager.DefinedConstant;
import com.uit.nst95.quanlycuocdidong.Manager.PackageNetwork;
import com.uit.nst95.quanlycuocdidong.R;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.uit.nst95.quanlycuocdidong.Activity.MainActivity.data3GPackages;

/**
 * A simple {@link Fragment} subclass.
 */
public class DataServicesFragment extends Fragment {

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

        SharedPreferences settings = getActivity().getSharedPreferences(DefinedConstant.PREFS_NAME, MODE_PRIVATE);
        String mangDiDong = settings.getString(DefinedConstant.KEY_PROVIDER, DefinedConstant.VALUE_DEFAULT);

        lstView3GPackage = (ListView) view.findViewById(R.id.listView3GData);

        DAO_UsefulNumber dao_usefulNumber = new DAO_UsefulNumber(getContext());
        dao_usefulNumber.Open();
        ArrayList<Data3GPackage> myArrayData3G = new ArrayList<>();

        if(data3GPackages.size() > 0){
            System.out.println("Get data from Firebase DB");
            switch (mangDiDong)
            {
                case DefinedConstant.MOBIFONE: {
                    for (com.uit.nst95.quanlycuocdidong.FirebaseDB.Data3GPackage data: data3GPackages) {
                        if(data.getId_provider() == 2){
                            myArrayData3G.add(new com.uit.nst95.quanlycuocdidong.Manager.Data3GPackage(data.getId_3gpackage(),data.getSyntax_reg(),data.getNumber_receive().toString(),data.getFee(),data.getData_highspeed(),data.getExpiry_date(),data.getCharges_arise()));
                        }
                    }
                    break;
                }
                case DefinedConstant.VINAPHONE: {
                    for (com.uit.nst95.quanlycuocdidong.FirebaseDB.Data3GPackage data: data3GPackages) {
                        if(data.getId_provider() == 1){
                            myArrayData3G.add(new com.uit.nst95.quanlycuocdidong.Manager.Data3GPackage(data.getId_3gpackage(),data.getSyntax_reg(),data.getNumber_receive().toString(),data.getFee(),data.getData_highspeed(),data.getExpiry_date(),data.getCharges_arise()));
                        }
                    }
                    break;
                }
                case DefinedConstant.VIETTEL: {
                    for (com.uit.nst95.quanlycuocdidong.FirebaseDB.Data3GPackage data: data3GPackages) {
                        if(data.getId_provider() == 3){
                            myArrayData3G.add(new com.uit.nst95.quanlycuocdidong.Manager.Data3GPackage(data.getId_3gpackage(),data.getSyntax_reg(),data.getNumber_receive().toString(),data.getFee(),data.getData_highspeed(),data.getExpiry_date(),data.getCharges_arise()));
                        }
                    }
                    break;
                }
                case DefinedConstant.GMOBILE: { //khong co dich vu nay
                    myArrayData3G = null;
                    break;
                }
                case DefinedConstant.VIETNAMOBILE: {
                    for (com.uit.nst95.quanlycuocdidong.FirebaseDB.Data3GPackage data: data3GPackages) {
                        if(data.getId_provider() == 4){
                            myArrayData3G.add(new com.uit.nst95.quanlycuocdidong.Manager.Data3GPackage(data.getId_3gpackage(),data.getSyntax_reg(),data.getNumber_receive().toString(),data.getFee(),data.getData_highspeed(),data.getExpiry_date(),data.getCharges_arise()));
                        }
                    }
                    break;
                }
            }
        } else {
            System.out.println("Get data from Sqlite DB");
            switch (mangDiDong) {
                case DefinedConstant.MOBIFONE: {
                    myArrayData3G = dao_usefulNumber.GetAll3GPackage(2);
                    break;
                }
                case DefinedConstant.VINAPHONE: {
                    myArrayData3G = dao_usefulNumber.GetAll3GPackage(1);
                    break;
                }
                case DefinedConstant.VIETTEL: {
                    myArrayData3G = dao_usefulNumber.GetAll3GPackage(3);
                    break;
                }
                case DefinedConstant.GMOBILE: { //khong co dich vu nay
                    myArrayData3G = null;
                    break;
                }
                case DefinedConstant.VIETNAMOBILE: {
                    myArrayData3G = dao_usefulNumber.GetAll3GPackage(4);
                    break;
                }
            }
        }
        if (myArrayData3G == null) {
            view = inflater.inflate(R.layout.fragment_not_found, container, false);
        }
        else {
            adapter = new Data3GArrayAdapter(getActivity(),R.layout.custom_item_listview_data3g, myArrayData3G);
            lstView3GPackage.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        dao_usefulNumber.Close();

        return view;
    }

}
