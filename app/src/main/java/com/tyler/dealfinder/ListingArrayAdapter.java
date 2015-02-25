package com.tyler.dealfinder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;

public class ListingArrayAdapter extends ArrayAdapter<EbayListing> {

	private final Context context;

	private final ArrayList<EbayListing> listingArrayList;

	public ListingArrayAdapter(Context context, ArrayList<EbayListing> listingArrayList) {
		super(context, R.layout.list_item, listingArrayList);
		this.context = context;
		this.listingArrayList = listingArrayList;
	}

	public ArrayList<EbayListing> getValues() {
		return this.listingArrayList;
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
        ImageView image = (ImageView) rowView.findViewById(R.id.picture);

		priceView.setText("$" + listingArrayList.get(position).getPrice());
		nameView.setText(listingArrayList.get(position).getName());
		image.setContentDescription(listingArrayList.get(position).getName());
        new DownloadImageTask(image).execute(listingArrayList.get(position).getPicUrl());

		return rowView;
	}

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }
        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                //Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
