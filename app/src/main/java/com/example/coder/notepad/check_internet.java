package com.example.geek.movie;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class check_internet
{
    public static boolean Internet_Avilable(Context context)
    {
        ConnectivityManager connectivemanager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo=connectivemanager.getActiveNetworkInfo();
        if(networkinfo!=null)
            return true;
        return false;
    }
    public static HttpURLConnection openconnection(String link) throws IOException
    {
        URL url=new URL(link);
        HttpURLConnection http=(HttpURLConnection)url.openConnection();
        http.setRequestMethod("GET");
        http.setConnectTimeout(1000);
        http.setReadTimeout(10000);
        http.setDoInput(true);
        return http;
    }
    public  static String getresult(HttpURLConnection http) throws IOException
    {
        InputStream inputStream=http.getInputStream();
        StringBuilder stringBuilder=new StringBuilder();
        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while((line=bufferedReader.readLine())!=null)
        {
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }

}

