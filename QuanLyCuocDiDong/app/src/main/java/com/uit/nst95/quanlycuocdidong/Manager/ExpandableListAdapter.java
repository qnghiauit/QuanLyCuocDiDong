package com.uit.nst95.quanlycuocdidong.Manager;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.uit.nst95.quanlycuocdidong.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by QNghia on 17/10/2016.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader;
    private HashMap<String, List<Contact>> _listDataChild;

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<Contact>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final Contact childText = (Contact) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.custom_item_expandablelist_useful_number, null);
        }

        TextView Name = (TextView) convertView.findViewById(R.id.textViewNameContact);
        TextView PhoneNumber = (TextView) convertView.findViewById(R.id.textViewPhoneNumber);
        Name.setText(childText.get_name());
        PhoneNumber.setText(childText.get_phoneNumber());
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.custom_header_expandablelist_useful_number, null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageViewHeader);
        lblListHeader.setText(headerTitle);
        if(groupPosition == 0)
            imageView.setImageResource(R.drawable.cuuho);
        else if(groupPosition == 1)
            imageView.setImageResource(R.drawable.help);
        else if (groupPosition == 2)
            imageView.setImageResource(R.drawable.taxi);
        else if(groupPosition == 3)
            imageView.setImageResource(R.drawable.bank);
        else if (groupPosition == 4)
            imageView.setImageResource(R.drawable.provider);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}