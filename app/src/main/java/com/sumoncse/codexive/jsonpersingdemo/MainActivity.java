package com.sumoncse.codexive.jsonpersingdemo;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView show;

    //ByteCode convert into CharecterCode;
    BufferedReader charecterCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        show = findViewById(R.id.t1);
        JsonTask jsonTask = new JsonTask();
        jsonTask.execute();
    }

    public class JsonTask extends AsyncTask<String,String,String>{


        @Override
        protected String doInBackground(String... strings) {

            HttpURLConnection httpURLConnection = null;
            String name;
            String address;
            String id;


            try{
                URL url = new URL("https://api.myjson.com/bins/dbx4g");
                httpURLConnection = (HttpURLConnection) url.openConnection();

                //Read ByteCode;
                InputStream byteCode = httpURLConnection.getInputStream();
                //ByteCode convert into CharecterCode;
                charecterCode = new BufferedReader(new InputStreamReader(byteCode));
                //StringBuffer is used line to line Charecter print;
                StringBuffer stringBuffer = new StringBuffer();
                String line = " ";
                StringBuffer lastBuffer = new StringBuffer();

                while((line = charecterCode.readLine()) != null){

                   stringBuffer.append(line);

                }

                //Line Convert Not Json;
                String file = stringBuffer.toString();
                JSONObject fileObject = new JSONObject(file);
                JSONArray jsonArray = fileObject.getJSONArray("profile");

                for (int i=0;i<jsonArray.length();i++) {
                    JSONObject arrayObject = jsonArray.getJSONObject(i);
                    name = arrayObject.getString("name");
                    address = arrayObject.getString("address");
                    id = arrayObject.getString("id");
                    lastBuffer.append(name+"\n"+ address+"\n" + id+"\n\n");
                }
                return lastBuffer.toString();

            }catch (MalformedURLException e){

                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                httpURLConnection.disconnect();

                try {
                    charecterCode.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            show.setText(s);
        }
    }
}
