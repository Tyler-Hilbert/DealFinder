package com.tyler.dealfinder;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends Activity {
	private ListView listingsListView;
	private SearchView searchView;
	private TextView statusView;
	private ResultsRetrievalTaskRunner resultsRetriever;
	private String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.main);

		listingsListView = (ListView) findViewById(R.id.listingsListView);
		statusView = (TextView) findViewById(R.id.status_text);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.searchview_in_menu, menu);

		MenuItem searchItem = menu.findItem(R.id.action_search);
		searchView = (SearchView) searchItem.getActionView();
		searchView.setIconifiedByDefault(true);
		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String s) {
				query = s;
				resultsRetriever = new ResultsRetrievalTaskRunner();
				resultsRetriever.execute();
				searchView.clearFocus();
				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				return false;
			}
		});

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

	protected class ResultsRetrievalTaskRunner extends AsyncTask<String, Void, ArrayList<EbayListing>> {
        private ArrayList<EbayListing> resp;
		ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {
			progressDialog = ProgressDialog.show(MainActivity.this, "Importing great deals", "Loading...", true);
		}

		@Override
        protected ArrayList<EbayListing> doInBackground(String... params) {
            EbaySearch search = new EbaySearch();

            try {
                resp = search.run(query);
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

