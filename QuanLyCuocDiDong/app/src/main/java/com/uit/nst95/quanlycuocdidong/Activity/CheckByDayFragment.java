package com.uit.nst95.quanlycuocdidong.Activity;


import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.calendardatepicker.MonthAdapter;
import com.uit.nst95.quanlycuocdidong.DB.CallLog;
import com.uit.nst95.quanlycuocdidong.DB.DAO_CallLog;
import com.uit.nst95.quanlycuocdidong.DB.DAO_MessageLog;
import com.uit.nst95.quanlycuocdidong.DB.MessageLog;
import com.uit.nst95.quanlycuocdidong.Manager.CalllogArrayAdapter;
import com.uit.nst95.quanlycuocdidong.Manager.DateTimeManager;
import com.uit.nst95.quanlycuocdidong.Manager.DayFee;
import com.uit.nst95.quanlycuocdidong.Manager.MessLogArrayAdapter;
import com.uit.nst95.quanlycuocdidong.R;

import org.joda.time.DateTime;

import java.io.Console;
import java.text.DecimalFormat;
import java.util.List;
import java.util.logging.Logger;

import static android.R.id.tabhost;

/**
 * A simple {@link Fragment} subclass.
 */
public class CheckByDayFragment extends Fragment implements CalendarDatePickerDialogFragment.OnDateSetListener{

    private static final String FRAG_TAG_DATE_PICKER = "fragment_date_picker_name";
    public static final String currency = " đ";
    private ImageButton imageButtonCalendar;
    private TextView textViewCalendarValue;
    private int day, month, year;
    private int oldestDay,oldestMonth,oldestYear;
    private TextView textViewTongTienGoi;
    private TextView textViewSoPhutGoiNoiMang;
    private TextView textViewTienGoiNoiMang;
    private TextView textViewSoPhutGoiNgoaiMang;
    private TextView textViewTienGoiNgoaiMang;
    private TextView textViewTongTienSmS;
    private TextView textViewSoSmsNoiMang;
    private TextView textViewTienSmSNoiMang;
    private TextView textViewSoSmsNgoaiMang;
    private TextView textViewTienSmsNgoaiMang;
    private TextView textViewTongTien;
    private List<CallLog> callLogList;
    private List<MessageLog> messLogList;
    private CalllogArrayAdapter calllogAdapter;
    private MessLogArrayAdapter messLogAdapter;
    private ListView listViewCallLog;
    private ListView listViewMessLog;
    private DAO_MessageLog dao_messageLog;
    private DAO_CallLog dao_callLog;
    public CheckByDayFragment() {
        // Required empty public constructor
    }

    public static CheckByDayFragment newInstance() {
        CheckByDayFragment f = new CheckByDayFragment();
        return (f);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DateTime now = DateTime.now();
        this.year = now.getYear();
        this.month = now.getMonthOfYear();
        this.day = now.getDayOfMonth();
        createConnect();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_check_by_day, container, false);
        getControl(view);
        getLog();
        addEvent();


        return view;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void getControl(View view)
    {
        imageButtonCalendar = (ImageButton) view.findViewById(R.id.imageButtonCalendar);
        textViewCalendarValue = (TextView) view.findViewById(R.id.textViewValueOfCalendar);
        textViewTongTienGoi = (TextView) view.findViewById(R.id.textViewTongTienGoi);
        textViewSoPhutGoiNoiMang = (TextView) view.findViewById(R.id.textViewSoPhutGoiNoiMang);
        textViewTienGoiNoiMang = (TextView) view.findViewById(R.id.textViewTienGoiNoiMang);
        textViewSoPhutGoiNgoaiMang = (TextView) view.findViewById(R.id.textViewSoPhutGoiNgoaiMang);
        textViewTienGoiNgoaiMang = (TextView) view.findViewById(R.id.textViewTienGoiNgoaiMang);
        textViewTongTienSmS = (TextView) view.findViewById(R.id.textViewTongTienSmS);
        textViewSoSmsNoiMang = (TextView) view.findViewById(R.id.textViewSoSmsNoiMang);
        textViewTienSmSNoiMang = (TextView) view.findViewById(R.id.textViewTienSmSNoiMang);
        textViewSoSmsNgoaiMang = (TextView) view.findViewById(R.id.textViewSoSmsNgoaiMang);
        textViewTienSmsNgoaiMang = (TextView) view.findViewById(R.id.textViewTienSmsNgoaiMang);
        textViewTongTien = (TextView) view.findViewById(R.id.textViewTongTien);

        listViewCallLog = (ListView) view.findViewById(R.id.listViewCallLog);
        listViewMessLog = (ListView) view.findViewById(R.id.listViewMessLog);

        final TabHost tab = (TabHost) view.findViewById(R.id.tabLog);
        tab.setup();
        //Tạo tab1
        TabHost.TabSpec spec1 = tab.newTabSpec("t1");
        spec1.setContent(R.id.tab1);
        spec1.setIndicator("Cuộc gọi");
        tab.addTab(spec1);
        //Tạo tab2
        TabHost.TabSpec spec2 = tab.newTabSpec("t2");
        spec2.setContent(R.id.tab2);
        spec2.setIndicator("Tin nhắn");
        tab.addTab(spec2);
        //Thiết lập tab mặc định được chọn ban đầu là tab 0
        tab.setCurrentTab(0);
        final int height = 140;
        tab.getTabWidget().getChildAt(0).getLayoutParams().height = height;
        tab.getTabWidget().getChildAt(1).getLayoutParams().height = height;
        for(int i=0;i<tab.getTabWidget().getChildCount();i++)
        {
            TextView tv = (TextView) tab.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(Color.parseColor("#ffffff"));
        }
//#303F9F
    }
    private void addEvent()
    {
        imageButtonCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                DateTime now = DateTime.now();
                CalendarDatePickerDialogFragment calendarDatePickerDialogFragment = CalendarDatePickerDialogFragment
                        .newInstance(CheckByDayFragment.this, year, month - 1,
                                day);

                calendarDatePickerDialogFragment.setDateRange(
                        new MonthAdapter.CalendarDay(oldestYear, oldestMonth - 1,  oldestDay),
                        new MonthAdapter.CalendarDay(now.getYear(), now.getMonthOfYear() - 1, now.getDayOfMonth()));

                calendarDatePickerDialogFragment.show(fm, FRAG_TAG_DATE_PICKER);
            }
        });;
    }
    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
        if (this.day != dayOfMonth || this.month != monthOfYear || this.year != year)
        {
            this.day = dayOfMonth;
            this.month = monthOfYear +1 ;
            this.year = year;
            getLog();
        }
    }
    private void createConnect()
    {
        dao_callLog = new DAO_CallLog(getContext());
        dao_callLog.Open();
        long callTime = dao_callLog.getOldestCallTime();

        dao_messageLog = new DAO_MessageLog(getContext());
        dao_messageLog.Open();
        long messTime = dao_messageLog.GetOldestMessageTime();

        long oldestTime;
        if(callTime < messTime)
            oldestTime = callTime;
        else
            oldestTime = messTime;
        oldestYear = DateTimeManager.get_instance().getYearFromMilisecs(oldestTime);
        oldestMonth = DateTimeManager.get_instance().getMonthFromMilisecs(oldestTime);
        oldestDay = DateTimeManager.get_instance().getDayhFromMilisecs(oldestTime);
    }
    private void getLog()
    {
        int numCallLog;
        int numMessLog;
        DateTime dateTime = new DateTime(this.year,this.month,this.day,0,0);
        long millis = dateTime.getMillis();
        callLogList = dao_callLog.GetCallLogInDay(millis);
        numCallLog = callLogList.size();
        messLogList = dao_messageLog.GetMessageLogInDay(millis);
        numMessLog = messLogList.size();

        CallLog tempCall;
        MessageLog tempMess;
        DayFee dayFee = new DayFee(this.day,this.month,this.year);
        int minutes_innerCall = 0;
        int minutes_outerCall = 0;
        int i;

        for (i=0;i< numCallLog;i++)
        {
            tempCall = callLogList.get(i);
            if (tempCall.get_callType() == 0) {
                minutes_innerCall += tempCall.get_callDuration();
                dayFee.addFee_innerCall(tempCall.get_callFee());

            } else
            {
                minutes_outerCall += tempCall.get_callDuration();
                dayFee.addFee_outerCall(tempCall.get_callFee());
            }

        }
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        dayFee.setMinutes_innerCall(DateTimeManager.get_instance().convertToMinutesAndSec(minutes_innerCall,false));
        dayFee.setMinutes_outerCall(DateTimeManager.get_instance().convertToMinutesAndSec(minutes_outerCall,false));
        textViewTongTienGoi.setText(formatter.format(dayFee.getFee_innerCall() + dayFee.getFee_outerCall()) + currency);
        textViewSoPhutGoiNoiMang.setText(dayFee.getMinutes_innerCall());
        textViewTienGoiNoiMang.setText(formatter.format(dayFee.getFee_innerCall()) + currency);
        textViewSoPhutGoiNgoaiMang.setText(dayFee.getMinutes_outerCall());
        textViewTienGoiNgoaiMang.setText(formatter.format(dayFee.getFee_outerCall()) + currency);


        for (i=0;i< numMessLog;i++)
        {
            tempMess = messLogList.get(i);
            if (tempMess.get_messageType() == 0)
            {
                dayFee.countUpNumber_innerMess();
                dayFee.addFee_innerMess(tempMess.get_messageFee());

            } else
            {
                dayFee.countUpNumber_outerMess();
                dayFee.addFee_outerMess(tempMess.get_messageFee());
            }
        }
        textViewTongTienSmS.setText(formatter.format(dayFee.getFee_innerMess() + dayFee.getFee_outerMess()) + currency);
        textViewSoSmsNoiMang.setText(String.valueOf(dayFee.getNumber_innerMess()) + " tin nhắn");
        textViewTienSmSNoiMang.setText(formatter.format(dayFee.getFee_innerMess()) + currency);
        textViewSoSmsNgoaiMang.setText(String.valueOf(dayFee.getNumber_outerMess()) + " tin nhắn");
        textViewTienSmsNgoaiMang.setText(formatter.format(dayFee.getFee_outerMess()) + currency);

        textViewTongTien.setText(String.valueOf(formatter.format(dayFee.getFee_innerCall() + dayFee.getFee_outerCall()
                + dayFee.getFee_innerMess() + dayFee.getFee_outerMess())) + currency);


        calllogAdapter = new CalllogArrayAdapter(getActivity(),R.layout.custom_item_tab_call_message_log,callLogList);
        listViewCallLog.setAdapter(calllogAdapter);
        calllogAdapter.notifyDataSetChanged();

        messLogAdapter = new MessLogArrayAdapter(getActivity(),R.layout.custom_item_tab_call_message_log,messLogList);
        listViewMessLog.setAdapter(messLogAdapter);
        messLogAdapter.notifyDataSetChanged();

        textViewCalendarValue.setText(String.valueOf(day) + "/" + String.valueOf(month) + "/" + String.valueOf(year));
    }
    public static String getContactName(Context context, String phoneNumber) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        Cursor cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
        if (cursor == null) {
            return null;
        }
        String contactName = null;
        if(cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
        }

        if(cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        if(contactName == null)
            return phoneNumber;
        return contactName;
    }
}
