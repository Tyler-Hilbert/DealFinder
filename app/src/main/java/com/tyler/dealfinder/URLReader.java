package com.tyler.dealfinder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class URLReader {

    public static String read(String address) throws Exception{

        URL url = new URL(address);
        URLConnection connection = url.openConnection();
        connection.setRequestProperty("User-Agent","Mozilla/5.0 ( compatible ) ");
        connection.setRequestProperty("Accept","*/*");

        String line;
        String response;
        long totalBytes = 0;

        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        while ((line = reader.readLine()) != null) {
            builder.append(line);
            totalBytes += line.getBytes("UTF-8").length ;
            //System.out.println("Total bytes read ::  " + totalBytes);
        }

        response = builder.toString();

        return response;
	}

}