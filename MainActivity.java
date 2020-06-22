package com.example.top10songs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ListView listSongs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        listSongs = (ListView)findViewById(R.id.listViews);

        DownloadXML downloadXML = new DownloadXML();
        downloadXML.execute("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topsongs/limit=10/xml");
    }

    private class DownloadXML extends AsyncTask<String,Void,String>{

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "onPostExecute: "+s);

            XmlParser xmlParser = new XmlParser();
            xmlParser.parse(s);

            ArrayAdapter<FeedEntry> arrayAdapter = new ArrayAdapter<>(MainActivity.this,R.layout.list_view,xmlParser.getApplication());
            listSongs.setAdapter(arrayAdapter);

        }

        @Override
        protected String doInBackground(String... strings) {

            String rssFeed = downloadXML(strings[0]);
            if(rssFeed==null){
                Log.e(TAG, "doInBackground: error downloading" );
            }
            return rssFeed;
        }


        private String downloadXML(String urlAdd){
            StringBuilder xml = new StringBuilder();
            try {
                URL url = new URL(urlAdd);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                int count;
                char[] inputs = new char[1000];
                while (true){
                    count = bufferedReader.read(inputs);
                    if (count<0){
                        break;
                    }
                    if(count>0){
                        xml.append(String.copyValueOf(inputs,0,count));
                    }

                }
                bufferedReader.close();
                return xml.toString();
            }catch (MalformedURLException e){
                Log.e(TAG, "downloadXML: Invalid url" + e.getMessage());
            }catch (IOException e){
                Log.e(TAG, "downloadXML: IOException" +e.getMessage());
            }
            return null;
        }
    }
}
