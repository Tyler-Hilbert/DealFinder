package com.tyler.dealfinder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.LruCache;
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
	private LruCache<String, Bitmap> mMemoryCache;
	private final ArrayList<EbayListing> listingArrayList;

	public ListingArrayAdapter(Context context, ArrayList<EbayListing> listingArrayList) {
		super(context, R.layout.list_item, listingArrayList);
		final int cacheSize = (int) (Runtime.getRuntime().maxMemory() / 1024) / 8;

		this.context = context;
		this.listingArrayList = listingArrayList;

		this.mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				return bitmap.getByteCount() / 1024;
			}
		};
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
		loadBitmap(image.getId() + position, image, listingArrayList.get(position).getPicUrl());


		return rowView;
	}

	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		if (getBitMapFromMemCache(key) == null) {
			mMemoryCache.put(key, bitmap);
		}
	}

	public Bitmap getBitMapFromMemCache(String key) {
		return mMemoryCache.get(key);
	}

	public void loadBitmap(int resId, ImageView imageView, String bitmapUrl) {
		final String imageKey = String.valueOf(resId);

		final Bitmap bitmap = getBitMapFromMemCache(imageKey);
		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
		} else {
			BitmapWorkerTask task = new BitmapWorkerTask(imageView, resId);
			task.execute(bitmapUrl);
		}
	}

    private class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
		int resId;

		public BitmapWorkerTask(ImageView bmImage, int resId) {
            this.bmImage = bmImage;
			this.resId = resId;
        }

        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap mIcon11 = null;

			try {
                InputStream in = new java.net.URL(url).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
				addBitmapToMemoryCache(Integer.toString(resId), mIcon11);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
