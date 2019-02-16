package com.example.chintan.smartwardrobe;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Connection
{
    public static String localurl="http://192.168.1.4/SmartWardrobe_service/Service1.svc/";

    public static boolean checkNetworkAvailable(Context context)
    {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    public int register()
    {
        try {
            final String TAG_id = "Msg";
            StringBuilder result = new StringBuilder();

            HttpClient httpclient = new DefaultHttpClient();
            String url = String.format(localurl + "register");
            HttpPost httpPost = new HttpPost(url);
            String json = "";
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("name",Userdata.getName());
            jsonObject.accumulate("age",Userdata.getAge());
            jsonObject.accumulate("gender",Userdata.getGender());
            jsonObject.accumulate("emailid",Userdata.getEmail());
            jsonObject.accumulate("password", Userdata.getPassword());

            json = jsonObject.toString();
            StringEntity se = new StringEntity(json);
            httpPost.setEntity(se);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse response = httpclient.execute(httpPost);
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                InputStream in = new BufferedInputStream(response.getEntity().getContent());
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }
                JSONObject jobj = new JSONObject(result.toString());
                String msg = jobj.getString(TAG_id);

                if (msg.equals("Data inserted")) {
                    return 1;
                } else if (msg.equals("Email-id already exist")) {
                    return 2;
                } else {
                    return 3;
                }
            } else {
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (Exception e) {
            return 0;
        }
    }

    public int authUser(String emailid, String Pass)
    {
        try
        {
            StringBuilder result = new StringBuilder();
            String email = emailid;
            String pass = Pass;
            String url = String.format(localurl + "login/" + email + "/" + pass);
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(new HttpGet(url));
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {

                InputStream in = new BufferedInputStream(response.getEntity().getContent());
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }
                JSONObject jobj = new JSONObject(result.toString());
                String userid= jobj.getString("userid");
                String useremail = jobj.getString("emailid");
                String Password = jobj.getString("Password");
                String msg=jobj.getString("Msg");

                if (useremail!="null"&&Password!="null") {

                    Userdata.setUserid(userid);
                    Userdata.setEmail(useremail);
                    Userdata.setPassword(Password);
                    return 1;
                } else {
                    return 2;
                }
            } else {
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int addclothesdata(Clothesdata imdata)
    {
        try
        {
            String strData = "";
            byte[] data=new byte[imdata.getImage().length];
            data= imdata.getImage();
            int lent=data.length;
            String res= Base64.encodeToString(data, Base64.DEFAULT);
            String abc = strData;
            Log.d("STRDATA: ", strData);

            final String TAG_id = "Msg";
            StringBuilder result = new StringBuilder();

            HttpClient httpclient = new DefaultHttpClient();
            String url = String.format(localurl + "uploadData");
            HttpPost httpPost = new HttpPost(url);
            String json = "";
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("type",Clothesdata.get_clothtype());
            jsonObject.accumulate("weather",Clothesdata.get_weather());
            jsonObject.accumulate("occasion",Clothesdata.get_occasion());
            jsonObject.accumulate("color",Clothesdata.getColor());
            jsonObject.accumulate("rfid",Clothesdata.get_rfid());
            jsonObject.accumulate("picpath",Clothesdata.getPicpath());
            jsonObject.accumulate("image",res);
            json = jsonObject.toString();
            StringEntity se = new StringEntity(json);
            httpPost.setEntity(se);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse response = httpclient.execute(httpPost);
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                InputStream in = new BufferedInputStream(response.getEntity().getContent());
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }
                JSONObject jobj = new JSONObject(result.toString());
                String msg = jobj.getString(TAG_id);

                if (msg.equals("Data saved"))
                {
                    return 1;
                }
                else if (msg.equals("Image already exist"))
                {
                    return 2;
                }
                else
                {
                    return 3;
                }
            }
            else {
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        }
        catch (Exception e)
        {
            return 0;
        }
    }

    public boolean getusedlist(String email)
    {
        try
        {
            ArrayList<String> clothid, clothtype;
            StringBuilder result = new StringBuilder();
            String mail = email;

            /* String url = String.format(localurl + "doclist/"+Location_.getLatitude()+"/"+Location_.getLongitude());*/
            String url = String.format(localurl + "usedcloth/" + mail);

            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(new HttpGet(url));
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {

                InputStream in = new BufferedInputStream(response.getEntity().getContent());
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }
                br.close();
                JSONArray jarrayobj = new JSONArray(result.toString());
                int length = 0;
                if (jarrayobj.length() > 0) {
                    length = jarrayobj.length();
                }

                clothid = new ArrayList<String>(length);
                clothtype = new ArrayList<String>(length);

                for (int i = 0; i < jarrayobj.length(); i++)
                {
                    JSONObject job = jarrayobj.getJSONObject(i);
                    String cloth_id = job.optString("cloth_id");
                    String cloth_type = job.optString("type");

                    clothid.add(cloth_id);
                    clothtype.add(cloth_type);
                }

                UsedList.setClothid(clothid);
                UsedList.setClothtype(clothtype);
                return true;
            }
            else {
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean getunusedlist(String email)
    {
        try
        {
            ArrayList<String> clothid, clothtype;
            StringBuilder result = new StringBuilder();
            String mail = email;

            /* String url = String.format(localurl + "doclist/"+Location_.getLatitude()+"/"+Location_.getLongitude());*/
            String url = String.format(localurl + "unusedcloth/" + mail);

            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(new HttpGet(url));
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {

                InputStream in = new BufferedInputStream(response.getEntity().getContent());
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }
                br.close();
                JSONArray jarrayobj = new JSONArray(result.toString());
                int length = 0;
                if (jarrayobj.length() > 0) {
                    length = jarrayobj.length();
                }

                clothid = new ArrayList<String>(length);
                clothtype = new ArrayList<String>(length);

                for (int i = 0; i < jarrayobj.length(); i++)
                {
                    JSONObject job = jarrayobj.getJSONObject(i);
                    String cloth_id = job.optString("cloth_id");
                    String cloth_type = job.optString("type");

                    clothid.add(cloth_id);
                    clothtype.add(cloth_type);
                }

                UnusedList.setClothid(clothid);
                UnusedList.setClothtype(clothtype);
                return true;
            }
            else {
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
