package com.contactmanager.adapter;

/**
 * @className CS 6301-022 User Interface Design
 * @author Ankit Tolia (abt140130), Abhishek Bansal (axb146030)
 * @email-id abt140130@utdallas.edu,axb146030@utdallas.edu 
 * @version 1.1
 * @started date 1th Nov 2014
 * 
 * Adaptor Class for Listview that is notified for any changes in list and updates the listview data accordingly.
 * Purpose : Class Assignment
 * @Created By: Ankit Tolia
 */


import java.util.List;

import com.contactmanager.bean.ContactListBean;
import com.contactmanager.R;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListViewAdapter extends ArrayAdapter<ContactListBean> {

	Context context;

	public CustomListViewAdapter(Context context, int resourceId,
			List<ContactListBean> items) {
		super(context, resourceId, items);
		this.context = context;
	}

	/* private view holder class that displays the child items of listview*/
	private class ViewHolder {
		TextView txtName;
		TextView txtPhone;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		ContactListBean rowItem = getItem(position);
		// Set Child View Data from the Bean
		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.list_item, null);
			holder = new ViewHolder();
			holder.txtName = (TextView) convertView
					.findViewById(R.id.contactName);
			holder.txtPhone = (TextView) convertView
					.findViewById(R.id.contactPhone);
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();

		if (rowItem.getLastName() != null)
			holder.txtName.setText(rowItem.getFirstName() + " "
					+ rowItem.getLastName());
		else
			holder.txtName.setText(rowItem.getFirstName());
		if (rowItem.getPhone() != null)
			holder.txtPhone.setText(rowItem.getPhone());
		else
			holder.txtPhone.setText("");

		return convertView;
	}
}