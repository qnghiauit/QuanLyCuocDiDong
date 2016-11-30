package com.uit.nst95.quanlycuocdidong.Activity;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.uit.nst95.quanlycuocdidong.DB.DAO_UsefulNumber;
import com.uit.nst95.quanlycuocdidong.FirebaseDB.CuuHo;
import com.uit.nst95.quanlycuocdidong.FirebaseDB.NganHang;
import com.uit.nst95.quanlycuocdidong.FirebaseDB.NhaMang;
import com.uit.nst95.quanlycuocdidong.FirebaseDB.Taxi;
import com.uit.nst95.quanlycuocdidong.FirebaseDB.TuVan;
import com.uit.nst95.quanlycuocdidong.Manager.Contact;
import com.uit.nst95.quanlycuocdidong.Manager.ExpandableListAdapter;
import com.uit.nst95.quanlycuocdidong.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.R.attr.data;
import static com.uit.nst95.quanlycuocdidong.Activity.MainActivity.cuuHos;
import static com.uit.nst95.quanlycuocdidong.Activity.MainActivity.nganHangs;
import static com.uit.nst95.quanlycuocdidong.Activity.MainActivity.nhaMangs;
import static com.uit.nst95.quanlycuocdidong.Activity.MainActivity.taxis;
import static com.uit.nst95.quanlycuocdidong.Activity.MainActivity.tuVans;

/**
 * A simple {@link Fragment} subclass.
 */
public class UsefulPhoneNumbersFragment extends Fragment {

    private ExpandableListAdapter _expandableListAdapter;
    private ExpandableListView _expandeableListView;
    private List<Contact> _listContactTaxiHN, _listContactTaxiDN, _listContactTaxiHCM;
    private List<Contact> _listContactTuVan, _listContactCuuHo, _listContactNganHang, _listContactNhaMang;
    private List<String> _listDataHeader;
    private HashMap<String, List<Contact>> _listDataChild;
    String arr[]={
            "Hà Nội",
            "Đà Nẵng",
            "Hồ Chí Minh"};

    public UsefulPhoneNumbersFragment() {
        // Required empty public constructor
    }

    public static UsefulPhoneNumbersFragment newInstance() {
        UsefulPhoneNumbersFragment f = new UsefulPhoneNumbersFragment();
        return (f);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_useful_phone_numbers, container, false);

        _expandeableListView = (ExpandableListView) view.findViewById(R.id.expandableListView);
        prepareListData();
        _expandableListAdapter = new ExpandableListAdapter(getContext(), _listDataHeader, _listDataChild);
        _expandeableListView.setAdapter(_expandableListAdapter);

        Spinner spin = (Spinner) view.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.custom_item_spinner_useful_phonenumber, arr);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spin.setAdapter(adapter);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                _listDataChild.remove(_listDataHeader.get(2));
                switch (position) {
                    case 0: {
                        _listDataChild.put(_listDataHeader.get(2), _listContactTaxiHN);
                        break;
                    }
                    case 1: {
                        _listDataChild.put(_listDataHeader.get(2), _listContactTaxiDN);
                        break;
                    }
                    case 2: {
                        _listDataChild.put(_listDataHeader.get(2), _listContactTaxiHCM);
                        break;
                    }
                }
                _expandableListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // Listview on child click listener
        _expandeableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" +
                        _listDataChild.get(_listDataHeader.get(groupPosition)).get(childPosition).get_phoneNumber()));
                startActivity(intent);
                return false;
            }
        });
        // Inflate the layout for this fragment
        return view;
    }
    private void prepareListData() {
        DAO_UsefulNumber dao_usefulNumber = new DAO_UsefulNumber(getContext());
        dao_usefulNumber.Open();
        _listContactCuuHo = new ArrayList<>();
        _listContactNhaMang = new ArrayList<>();
        _listContactNganHang = new ArrayList<>();
        _listContactTuVan = new ArrayList<>();
        _listContactTaxiHN = new ArrayList<>();
        _listContactTaxiDN = new ArrayList<>();
        _listContactTaxiHCM = new ArrayList<>();

        if(cuuHos.size() > 0 && nhaMangs.size() > 0 && nganHangs.size() > 0 && tuVans.size() > 0 && taxis.size() > 0){
            System.out.println("Get data from Firebase DB");
            for (CuuHo cuuho : cuuHos) {
                _listContactCuuHo.add(new Contact(cuuho.getNAME(), cuuho.getNUMBER()));
            }
            for (NganHang nganhang : nganHangs) {
                _listContactNganHang.add(new Contact(nganhang.getNAME(), nganhang.getNUMBER()));
            }
            for (NhaMang nhamang : nhaMangs) {
                _listContactNhaMang.add(new Contact(nhamang.getNAME(), nhamang.getNUMBER()));
            }
            for (Taxi taxi : taxis) {
                if (taxi.getREGION() == 0) {
                    _listContactTaxiHN.add(new Contact(taxi.getNAME(), taxi.getNUMBER()));
                }
                if (taxi.getREGION() == 1) {
                    _listContactTaxiDN.add(new Contact(taxi.getNAME(), taxi.getNUMBER()));
                }
                if (taxi.getREGION() == 2) {
                    _listContactTaxiHCM.add(new Contact(taxi.getNAME(), taxi.getNUMBER()));
                }
            }
            for (TuVan tuvan : tuVans) {
                _listContactTuVan.add(new Contact(tuvan.getNAME(), tuvan.getNUMBER()));
            }
        } else {
            System.out.println("Get data from Sqlite DB");
            _listContactTuVan = dao_usefulNumber.GetAllTuVan();
            _listContactCuuHo = dao_usefulNumber.GetAllCuuHo();
            _listContactNganHang = dao_usefulNumber.GetAllNganHang();
            _listContactNhaMang = dao_usefulNumber.GetAllNhaMang();
            _listContactTaxiHN = dao_usefulNumber.GetAllTaxi(0);
            _listContactTaxiDN = dao_usefulNumber.GetAllTaxi(1);
            _listContactTaxiHCM = dao_usefulNumber.GetAllTaxi(2);
        }



        _listDataHeader = new ArrayList<String>();
        _listDataChild = new HashMap<String, List<Contact>>();

        // Adding child data
        _listDataHeader.add("Cứu hộ");
        _listDataHeader.add("Tư vấn");
        _listDataHeader.add("Taxi");
        _listDataHeader.add("Ngân hàng");
        _listDataHeader.add("Nhà mạng");

        _listDataChild.put(_listDataHeader.get(0), _listContactCuuHo); // Header, Child data
        _listDataChild.put(_listDataHeader.get(1), _listContactTuVan);
        _listDataChild.put(_listDataHeader.get(2), _listContactTaxiHCM);
        _listDataChild.put(_listDataHeader.get(3), _listContactNganHang);
        _listDataChild.put(_listDataHeader.get(4), _listContactNhaMang);
        dao_usefulNumber.Close();
    }
}
