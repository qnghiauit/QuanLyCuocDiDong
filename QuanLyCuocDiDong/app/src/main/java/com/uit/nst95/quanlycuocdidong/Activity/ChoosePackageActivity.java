package com.uit.nst95.quanlycuocdidong.Activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.uit.nst95.quanlycuocdidong.Manager.DefinedConstant;
import com.uit.nst95.quanlycuocdidong.Manager.PackageNetwork;
import com.uit.nst95.quanlycuocdidong.Manager.PackageNetworkArrayAdapter;
import com.uit.nst95.quanlycuocdidong.R;

import java.util.ArrayList;

import static com.uit.nst95.quanlycuocdidong.Manager.DefinedConstant.BIGKOOL;
import static com.uit.nst95.quanlycuocdidong.Manager.DefinedConstant.BIGSAVE;
import static com.uit.nst95.quanlycuocdidong.Manager.DefinedConstant.BUONLANG;
import static com.uit.nst95.quanlycuocdidong.Manager.DefinedConstant.ECONOMY;
import static com.uit.nst95.quanlycuocdidong.Manager.DefinedConstant.HISCHOOL;
import static com.uit.nst95.quanlycuocdidong.Manager.DefinedConstant.MOBICARD;
import static com.uit.nst95.quanlycuocdidong.Manager.DefinedConstant.MOBIGOLD;
import static com.uit.nst95.quanlycuocdidong.Manager.DefinedConstant.MOBIQ;
import static com.uit.nst95.quanlycuocdidong.Manager.DefinedConstant.QKIDS;
import static com.uit.nst95.quanlycuocdidong.Manager.DefinedConstant.QSTUDENT;
import static com.uit.nst95.quanlycuocdidong.Manager.DefinedConstant.QTEEN;
import static com.uit.nst95.quanlycuocdidong.Manager.DefinedConstant.SEA;
import static com.uit.nst95.quanlycuocdidong.Manager.DefinedConstant.SEVENCOLOR;
import static com.uit.nst95.quanlycuocdidong.Manager.DefinedConstant.STUDENT;
import static com.uit.nst95.quanlycuocdidong.Manager.DefinedConstant.SV2014;
import static com.uit.nst95.quanlycuocdidong.Manager.DefinedConstant.TALKSTUDENT;
import static com.uit.nst95.quanlycuocdidong.Manager.DefinedConstant.TALKTEEN;
import static com.uit.nst95.quanlycuocdidong.Manager.DefinedConstant.TIPHU2;
import static com.uit.nst95.quanlycuocdidong.Manager.DefinedConstant.TIPHU3;
import static com.uit.nst95.quanlycuocdidong.Manager.DefinedConstant.TIPHU5;
import static com.uit.nst95.quanlycuocdidong.Manager.DefinedConstant.TOMATO;
import static com.uit.nst95.quanlycuocdidong.Manager.DefinedConstant.VINACARD;
import static com.uit.nst95.quanlycuocdidong.Manager.DefinedConstant.VINAXTRA;
import static com.uit.nst95.quanlycuocdidong.Manager.DefinedConstant.VMAX;
import static com.uit.nst95.quanlycuocdidong.Manager.DefinedConstant.VMONE;

public class ChoosePackageActivity extends Activity {

    private String provider;
    private ListView lstViewPackage;
    private TextView textViewTut;
    private ArrayList<PackageNetwork> arrayListPackage = new ArrayList<PackageNetwork>();
    private PackageNetworkArrayAdapter adapter = null;
    private String stringTut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_package);

        getControl();
        getMobileNetwork();
        editActionBar();
        addItemToListView();
        addEvents();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void getControl()
    {
        lstViewPackage = (ListView) findViewById(R.id.listViewGoiCuoc);
        textViewTut = (TextView) findViewById(R.id.textViewTut);
    }
    private void addEvents()
    {
        lstViewPackage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Bundle myBundle = new Bundle();
                PackageNetwork myPN = (PackageNetwork) parent.getItemAtPosition(position);
                myBundle.putSerializable(DefinedConstant.KEY_PACKAGE, myPN);
                Intent myIntent = new Intent(ChoosePackageActivity.this, MainActivity.class);
                myIntent.putExtra(DefinedConstant.BUNDLE_NAME, myBundle);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(myIntent);
            }
        });
    }
    private void getMobileNetwork()
    {
        Intent callerIntent = getIntent();
        Bundle packageFromCaller = callerIntent.getBundleExtra(DefinedConstant.BUNDLE_NAME);
        provider = packageFromCaller.getString(DefinedConstant.KEY_PROVIDER);
        if(!provider.isEmpty()) {
            switch (provider) {
                case DefinedConstant.MOBIFONE: {
                    stringTut = "Nhấn *101#";
                    arrayListPackage.add(new PackageNetwork(provider, MOBICARD, R.drawable.mobicard));
                    arrayListPackage.add(new PackageNetwork(provider, MOBIGOLD, R.drawable.mobigold));
                    arrayListPackage.add(new PackageNetwork(provider, MOBIQ, R.drawable.mobiq));
                    arrayListPackage.add(new PackageNetwork(provider, QSTUDENT, R.drawable.qstudent));
                    arrayListPackage.add(new PackageNetwork(provider, QTEEN, R.drawable.qteen));
                    arrayListPackage.add(new PackageNetwork(provider, QKIDS, R.drawable.qkids));
                    textViewTut.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DefinedConstant.callUSSD(ChoosePackageActivity.this,"*101#");
                        }
                    });
                    break;
                }
                case DefinedConstant.VINAPHONE: {
                    stringTut = "Nhấn *110#";
                    arrayListPackage.add(new PackageNetwork(provider, VINACARD, R.drawable.vinacard));
                    arrayListPackage.add(new PackageNetwork(provider, VINAXTRA, R.drawable.vinaxtra));
                    arrayListPackage.add(new PackageNetwork(provider, TALKTEEN, R.drawable.talkez));
                    arrayListPackage.add(new PackageNetwork(provider, TALKSTUDENT, R.drawable.talkez));
                    textViewTut.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DefinedConstant.callUSSD(ChoosePackageActivity.this,"*110#");
                        }
                    });
                    break;
                }
                case DefinedConstant.VIETTEL: {
                    stringTut = "Gửi sms GC tới 195";

                    arrayListPackage.add(new PackageNetwork(provider, ECONOMY, R.drawable.vietel));
                    arrayListPackage.add(new PackageNetwork(provider, TOMATO, R.drawable.vietel));
                    arrayListPackage.add(new PackageNetwork(provider, STUDENT, R.drawable.vietel));
                    arrayListPackage.add(new PackageNetwork(provider, SEA, R.drawable.vietel));
                    arrayListPackage.add(new PackageNetwork(provider, HISCHOOL, R.drawable.vietel));
                    arrayListPackage.add(new PackageNetwork(provider, SEVENCOLOR, R.drawable.vietel));
                    arrayListPackage.add(new PackageNetwork(provider, BUONLANG, R.drawable.vietel));
                    textViewTut.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DefinedConstant.sendSmS(ChoosePackageActivity.this,"195", "GC");
                        }
                    });
                    break;
                }
                case DefinedConstant.GMOBILE: {
                    stringTut = "Nhấn *110#";
                    arrayListPackage.add(new PackageNetwork(provider, BIGSAVE, R.drawable.gmobile));
                    arrayListPackage.add(new PackageNetwork(provider, BIGKOOL, R.drawable.gmobile));
                    arrayListPackage.add(new PackageNetwork(provider, TIPHU2, R.drawable.gmobile));
                    arrayListPackage.add(new PackageNetwork(provider, TIPHU3, R.drawable.tiphu3));
                    arrayListPackage.add(new PackageNetwork(provider, TIPHU5, R.drawable.tiphu5));
                    textViewTut.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DefinedConstant.callUSSD(ChoosePackageActivity.this,"*110#");
                        }
                    });
                    break;
                }
                case DefinedConstant.VIETNAMOBILE: {
                    stringTut = "Nhấn *101#";
                    arrayListPackage.add(new PackageNetwork(provider, VMONE, R.drawable.vietnamobile));
                    arrayListPackage.add(new PackageNetwork(provider, VMAX, R.drawable.vietnamobile));
                    arrayListPackage.add(new PackageNetwork(provider, SV2014, R.drawable.vietnamobile));
                    textViewTut.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DefinedConstant.callUSSD(ChoosePackageActivity.this,"*101#");
                        }
                    });
                    break;
                }
            }
            textViewTut.setText(stringTut + " để kiểm tra gói cước đang sử dụng");
        }
    }
    private void addItemToListView()
    {
        adapter = new PackageNetworkArrayAdapter(this,R.layout.custom_listview_choose_package, arrayListPackage);
        lstViewPackage.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    private void editActionBar()
    {
        ActionBar bar = getActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setDisplayShowHomeEnabled(false);
        bar.setDisplayShowCustomEnabled(false);
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2196F3")));
        bar.setTitle(Html.fromHtml("<font color='#FFFFFF'>" + "Nhà mạng: "
                + provider + "</font>"));
    }
}
