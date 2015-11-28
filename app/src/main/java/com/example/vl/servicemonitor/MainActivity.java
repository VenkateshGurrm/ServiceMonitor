package com.example.vl.servicemonitor;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setTitle("  Service Monitor");


        final ListView listView = (ListView)findViewById(R.id.list);

        ArrayList<Row> list = new ArrayList<>();
        Resources res = getResources();
        TypedArray icons = res.obtainTypedArray(R.array.icons);
        String[] text = res.getStringArray(R.array.list_names);
        String[] urls = res.getStringArray(R.array.list_urls);

        for(int i=0;i<5;i++){
            Row row = null;
            if(i<4){
                row = new Row(text[i], icons.getDrawable(i),urls[i]);
            }else{
                row = new Row(text[i], icons.getDrawable(i),urls[i], false);
            }
            list.add(row);
        }
        listView.setAdapter(new CustomAdapter(list));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                Row item = (Row) listView.getItemAtPosition(position);

                if(item.browserType){
                    openWebPage(item.url);
                }else{
                    Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                    startActivity(intent);
                   // Toast.makeText(MainActivity.this, "You selected : " + item.getUrl(), Toast.LENGTH_SHORT).show();
                }


            }
        });

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    public void openWebPage(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
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

    private class CustomAdapter extends ArrayAdapter<Row> {

        public CustomAdapter(ArrayList<Row> rows) {
            super(MainActivity.this, 0, rows);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // If we weren't given a view, inflate one
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item, null);
            }

            Row c = getItem(position);
            TextView titleTextView = (TextView)convertView.findViewById(R.id.title);
            titleTextView.setText(c.getText());

            ImageView thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);
            thumbnail.setImageDrawable(c.getLogo());
            return convertView;
        }

        /*@Override
        public int getCount() {
            return list.size();
        }*/
    }

    class Row{
        private String text;
        boolean browserType;

        public Row(String text, Drawable logo, String url) {
            this.text = text;
            this.logo = logo;
            this.url = url;
            this.browserType = true;
        }

        public Row(String text, Drawable logo, String url, boolean browserType) {
            this.text = text;
            this.logo = logo;
            this.url = url;
            this.browserType = browserType;
        }

        public Drawable getLogo() {
            return logo;
        }

        public void setLogo(Drawable logo) {
            this.logo = logo;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        private Drawable logo;

        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
