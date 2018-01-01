package com.example.felix_pc.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
/*import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;*/

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    public ArrayList<String> source=new ArrayList<String>();
    public String url="http://10.0.2.2/zf3_api/public/usuarios";
    public class user
    {
        public String nombre;
        public String apellido;
        public String email;
        public user(String nombre,String apellido,String email)
        {
            this.nombre=nombre;
            this.apellido=apellido;
            this.email=email;
        }
        @Override
        public String toString() {
            return this.nombre +" - "+this.apellido+" - "+this.email;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getData();
        ListView listView1 = (ListView) findViewById(R.id.listView1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, source);
        listView1.setAdapter(adapter);
    }


    public void getData()
    {
        new Thread(new Runnable() {
            public void run() {
                try
                {
                    StringBuilder sn=new StringBuilder();
                    URL rl=new URL(url);
                    HttpURLConnection httpUrl=(HttpURLConnection)rl.openConnection();
                    httpUrl.setRequestMethod("GET");
                    httpUrl.connect();
                    InputStream in = new BufferedInputStream(httpUrl.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                    String inputLine;
                    StringBuffer response=new StringBuffer();

                    String json;
                    while ((inputLine=reader.readLine())!=null)
                    {
                        response.append(inputLine);
                    }

                    JSONObject object = new JSONObject(response.toString());
                    JSONObject objResult = new JSONObject(object.get("result").toString());
                    JSONArray arrayArticles = new JSONArray(objResult.get("articles").toString());

                    for (int i = 0; i < arrayArticles.length(); i++)
                    {
                        //recorre cada registro y concatena el resultado
                        JSONObject row = arrayArticles.getJSONObject(i);
                        String nombre = row.getString("nombre");
                        String apellido = row.getString("apellido");
                        String email = row.getString("email");
                        source.add(new user(nombre,apellido,email).toString());
                    }
                }
                catch (Exception exp)
                {
                    Log.e("Error en test", exp.getMessage());
                }
            }
        }).start();


    }
}
