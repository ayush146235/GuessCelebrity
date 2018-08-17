package com.example.ayushverma.guesscelebrity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private Button button0;
    private Button button1;
    private Button button2;
    private Button button3;
    private int locationOfCorrectTag;
    private int r1;

    private ArrayList<String> celebImageUrl = new ArrayList<>();
    private ArrayList<String> celebName = new ArrayList<>();

    public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {

            URL url;
            HttpURLConnection httpURLConnection;
            try {

                url = new URL(strings[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();
                InputStream in = httpURLConnection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(in);
                return bitmap;

            } catch (MalformedURLException e) {

                e.printStackTrace();
                Log.i("ImageDownloader", "Failed");

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public class Download extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            String result = "";
            URL url;
            HttpURLConnection httpURLConnection;

            try {

                url = new URL(strings[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream in = httpURLConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data != -1) {
                    char c = (char) data;
                    result += c;
                    data = reader.read();
                }

                return result;

            } catch (MalformedURLException e) {

                e.printStackTrace();

            } catch (IOException e) {

                e.printStackTrace();

            }


            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        button0 = findViewById(R.id.button0);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);

        Download download = new Download();
        String result = null;
        try {

            result = download.execute("http://www.posh24.se/kandisar").get();
            String[] splitresult = result.split("<div class=\"col-xs-12 col-sm-6 col-md-4\">");

            Pattern p = Pattern.compile("img src=\"(.*?)\"");
            Matcher m = p.matcher(splitresult[0]);
            while (m.find()) {

                celebImageUrl.add(m.group(1));

            }

            p = Pattern.compile("alt=\"(.*?)\"");
            m = p.matcher(splitresult[0]);
            while (m.find()) {

                celebName.add(m.group(1));

            }

            update();

        } catch (InterruptedException e) {

            e.printStackTrace();

        } catch (ExecutionException e) {

            e.printStackTrace();

        }


    }

    public void update(){

        Random random = new Random();
        r1 = random.nextInt(100) + 1;
        showImage();
        nameButton();

    }

    public void tapped(View view) {

        int tag = Integer.parseInt(view.getTag().toString());
        if (tag == locationOfCorrectTag) {

            Toast.makeText(this, "Correct", Toast.LENGTH_SHORT).show();

        } else {

            Toast.makeText(this, "Sorry! Correct Answer is "+celebName.get(r1-1), Toast.LENGTH_SHORT).show();

        }

        update();


    }

    public void nameButton() {

        Random random = new Random();
        //locationOfCorrectTag= Position of correct Button
        locationOfCorrectTag = random.nextInt(4);

        //i= Position to fill button
        for (int i = 0; i < 4; i++) {

            if (i != locationOfCorrectTag) {

                int r3 = random.nextInt(100) + 1;
                while (r3 == r1) {
                    r3 = random.nextInt(100) + 1;
                }

                switch (i) {

                    case 0:
                        button0.setText(celebName.get(r3 - 1));
                        break;
                    case 1:
                        button1.setText(celebName.get(r3 - 1));
                        break;
                    case 2:
                        button2.setText(celebName.get(r3 - 1));
                        break;
                    case 3:
                        button3.setText(celebName.get(r3 - 1));
                        break;

                }

            } else {

                switch (i) {

                    case 0:
                        button0.setText(celebName.get(r1 - 1));
                        break;
                    case 1:
                        button1.setText(celebName.get(r1 - 1));
                        break;
                    case 2:
                        button2.setText(celebName.get(r1 - 1));
                        break;
                    case 3:
                        button3.setText(celebName.get(r1 - 1));
                        break;

                }

            }

        }

    }

    public void showImage() {

        ImageDownloader imageDownloader = new ImageDownloader();
        try {

            Bitmap bitmap = imageDownloader.execute(celebImageUrl.get(r1 - 1)).get();
            imageView.setImageBitmap(bitmap);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
}
