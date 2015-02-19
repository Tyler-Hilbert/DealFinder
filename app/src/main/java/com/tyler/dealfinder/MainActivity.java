package com.tyler.dealfinder;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;


public class MainActivity extends Activity {
	private ListView listingsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

		listingsListView = (ListView) findViewById(R.id.listingsListView);
		AsyncTaskRunner asyncTaskRunner = new AsyncTaskRunner();
		asyncTaskRunner.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected class AsyncTaskRunner extends AsyncTask<String, Void, ArrayList<EbayListing>> {
        private ArrayList<EbayListing> resp;
		ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {
			progressDialog = ProgressDialog.show(MainActivity.this, "Loading products", "Loading products", true);
		}

		@Override
        protected ArrayList<EbayListing> doInBackground(String... params) {
            EbaySearch search = new EbaySearch();

            try {
                resp = search.run("iPhone");
            } catch(Exception e) {
                e.printStackTrace();
            }
            return resp;
        }

		@Override
		protected void onPostExecute(ArrayList<EbayListing> ebayListings) {
			final ListingArrayAdapter adapter = new ListingArrayAdapter(MainActivity.this, resp);
			listingsListView.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			progressDialog.dismiss();

			listingsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					EbayListing listing = adapter.getItem(position);
					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(listing.getUrl()));
					startActivity(browserIntent);
				}
			});
		}
	}
}

