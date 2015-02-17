package com.tyler.dealfinder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        TextView listing = (TextView) findViewById(R.id.listing);
        AsyncTaskRunner task = new AsyncTaskRunner();
        try {
            listing.setText(task.execute("").get().get(0).getName());
        } catch (Exception e) {
            e.printStackTrace();
        };

        new AsyncTaskRunner().execute("");
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


    private class AsyncTaskRunner extends AsyncTask<String, Void, ArrayList<EbayListing>> {
        private ArrayList<EbayListing> resp;

        @Override
        protected ArrayList<EbayListing> doInBackground(String... params) {
            EbaySearch search = new EbaySearch();
            try {
                resp = search.run("Iphone 5g");
            } catch(Exception e) {
                e.printStackTrace();
            }
            return resp;
        }
    }
}

