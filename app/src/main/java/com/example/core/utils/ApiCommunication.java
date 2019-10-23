package com.example.core.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


public class ApiCommunication {

    public String request(String _url, String _sentence){

        HttpURLConnection urlConn = null;

        try {

            URL url = new URL(_url);

            urlConn =  (HttpURLConnection)url.openConnection();

            urlConn.setRequestMethod("POST");
            urlConn.setRequestProperty("Content-Type", "application/json");

            String strParams = _sentence;
            OutputStream os = urlConn.getOutputStream();

            os.write(strParams.getBytes("UTF-8"));
            os.flush();
            os.close();

            if(urlConn.getResponseCode() != HttpURLConnection.HTTP_OK)
                return "";

            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "UTF-8"));

            String result = "";
            String tmp;

            while((tmp = reader.readLine()) != null)
                result += tmp;

            return result;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(urlConn != null)
                urlConn.disconnect();
        }

        return "";
    }
}
