package com.tyler.dealfinder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListingArrayAdapter extends ArrayAdapter<EbayListing> {

	private final Context context;

	private final ArrayList<EbayListing> listingArrayList;

	public ListingArrayAdapter(Context context, ArrayList<EbayListing> listingArrayList) {
		super(context, R.layout.list_item, listingArrayList);
		this.context = context;
		this.listingArrayList = listingArrayList;
	}

	@Override
	public EbayListing getItem(int position) {
		return listingArrayList.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater)context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(R.layout.list_item, parent, false);

		TextView priceView = (TextView) rowView.findViewById(R.id.listingPrice);
		TextView nameView = (TextView) rowView.findViewById(R.id.listingName);
//		TextView urlView = (TextView) rowView.findViewById(R.id.listingURL);

		priceView.setText("$" + listingArrayList.get(position).getPrice());
		nameView.setText(listingArrayList.get(position).getName());
//		urlView.setText(listingArrayList.get(position).getUrl());

		return rowView;
	}
}
