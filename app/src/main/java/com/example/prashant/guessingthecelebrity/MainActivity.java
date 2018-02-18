package com.example.prashant.guessingthecelebrity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
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

    Button buttonPressed, first, second, third, fourth;
    int  celebNumber,answerLocationPlace;
    String storage;


    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        imageView = findViewById(R.id.imageView);
        first=findViewById(R.id.first);
        second=findViewById(R.id.second);
        third=findViewById(R.id.third);
        fourth=findViewById(R.id.fourth);


        Random random=new Random();
        celebNumber=random.nextInt(101);

        kickOff1();

    }

    public void kickOff1() {

        Bitmap myImage;


        String result = "";
        try {
            OutSource outSource = new OutSource();
            result = outSource.execute("http://www.posh24.se/kandisar").get();
            storage=result;
            imageLink(result);
            celebName(result);

            // imageView.setImageBitmap(myImage);


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void imageLink(String html) {

        Pattern p = Pattern.compile("src=\"(.*?)\" alt");
        Matcher m = p.matcher(html);
        ArrayList<String> imageLink = new ArrayList<String>();

        Random rand = new Random();
        celebNumber = rand.nextInt(100);

        while (m.find()) {

            imageLink.add(m.group(1));

        }

       imageGenerator(imageLink.get(celebNumber));
    }



    public void celebName(String html){

        Pattern p= Pattern.compile("alt=\"(.*?)\"/>");
        Matcher m=p.matcher(html);
        ArrayList<String> celebName= new ArrayList<String>();

        while(m.find()){
            celebName.add(m.group(1));
        }

        ArrayList<String> answers=new ArrayList<String>();

        Random rand = new Random();
          answerLocationPlace=rand.nextInt(4);
      //  answerNumber(answerLocationPlace);


        for (int i=0;i<4;i++) {
            if(answerLocationPlace==i){
                answers.add(celebName.get(celebNumber));
            }else{
                int a=rand.nextInt(101);
                while(celebNumber==a){
                    a=rand.nextInt(101);

                }
               answers.add(celebName.get(a));


            }


        }


        Log.i("Name",celebName.get(celebNumber));

        first.setText(answers.get(0));
        second.setText(answers.get(1));
        third.setText(answers.get(2));
        fourth.setText(answers.get(3));



    }

    public Bitmap imageGenerator(String urls){

        URL url;
        HttpURLConnection connection=null;

        try{
            url=new URL(urls);
            connection=(HttpURLConnection) url.openConnection();
            connection.connect();

            InputStream in=connection.getInputStream();
            Bitmap myImage;

            myImage=BitmapFactory.decodeStream(in);
            imageView.setImageBitmap(myImage);



        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    public void clicked(View view) {
        buttonPressed = (Button) view;

        int tagPressed = Integer.parseInt(buttonPressed.getTag().toString());

        Log.i("ButtonPressed",buttonPressed.getTag().toString());
        Log.i("Answer",Integer.toString(answerLocationPlace));
        if (answerLocationPlace == tagPressed) {
            Toast.makeText(this, "Correct", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "Incorrect", Toast.LENGTH_SHORT).show();
        }

        imageLink(storage);
        celebName(storage);





    }

    public class OutSource extends AsyncTask<String, Void, String> {

        Button buttonPressed, first, second, third, fourth;

        @Override
        protected String doInBackground(String... urls) {

            URL url;
            HttpURLConnection connection = null;
            String result = "";
            ArrayList<String> imageLink = new ArrayList<String>();
            ArrayList<String> celebName = new ArrayList<String>();
            try {
                url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream in = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);


                int data = reader.read();
                while (data != -1) {
                    char current = (char) data;
                    result += current;

                    data = reader.read();

                }

                return result;




            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }


        }


    }


}
