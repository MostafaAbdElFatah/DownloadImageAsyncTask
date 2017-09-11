package com.mostafaabdel_fatah.downloadimageasynctask;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.health.PackageHealthStats;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {


    ImageView imageView;
    String imageURL = "https://jsonparsingdemo-cec5b.firebaseapp.com/jsonData/images/lotr.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.imageView);
        String path = "sdcard/photoalbum/DownloadImage8.jpg";
        imageView.setImageDrawable(Drawable.createFromPath(path));
    }
    int count = 0;
    public void DownloadImage_btnClicked(View view) {
        new DownloadImageTask().execute(imageURL);
    }
    public  class DownloadImageTask extends AsyncTask<String,Integer,String>{

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("Download Image...");
            progressDialog.setMax(100);
            progressDialog.setProgress(0);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... param) {
            String path = param[0];
            int fileLength = 0;
            try {
                URL url = new URL(path);
                URLConnection urlConnection = url.openConnection();
                urlConnection.connect();
                fileLength = urlConnection.getContentLength();
                File newFolder = new File("sdcard/photoalbum");
                if (!newFolder.exists()){
                    newFolder.mkdir();
                }
                File inputFile = new File(newFolder,"DownloadImage8.jpg");
                InputStream inputStream = new BufferedInputStream(url.openStream(),8192);
                byte[] data = new  byte[1024];
                int total = 0 ;
                int count = 0 ;
                OutputStream outputStream = new FileOutputStream(inputFile);
                while ( (count=inputStream.read(data)) != -1 ){
                    total += count;
                    outputStream.write(data,0,count);
                    int process =(int) (total * 100) / fileLength;
                    publishProgress(process);
                }
                inputStream.close();
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "Download Image Complete...";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.hide();
            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
            String path = "sdcard/photoalbum/DownloadImage8.jpg";
            imageView.setImageDrawable(Drawable.createFromPath(path));
        }
    }
}
