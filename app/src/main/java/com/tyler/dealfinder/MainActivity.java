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

        //Toast t = Toast.makeText(this, "Derp", Toast.LENGTH_LONG);
        //t.show();

        TextView listing = (TextView) findViewById(R.id.listing);
        AsyncTaskRunner task = new AsyncTaskRunner();
        try {
            listing.setText(task.execute("").get().get(0).getName());
        } catch (Exception e) {
            e.printStackTrace();
        };

        //new DownloadImageTask((ImageView) findViewById(R.id.treeRiver)).execute("http://vignette1.wikia.nocookie.net/kirby/images/8/83/King_Dedede_for_SSB4.png/revision/latest?cb=20140110131154&path-prefix=en");
        //new EbaySearch((TextView) findViewById(R.id.output)).execute("df");
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

