package uk.hux.api_wrapper;

import android.util.Log;
import java.util.*;
import java.net.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;


public class HTTPQuery {
	
	public static JSONObject queryJSON(String url, Map<String,String> urlparameters)
	{
		String sb = "";
		for(String skey : urlparameters.keySet())
		{
			sb += "?" + skey + "=" + urlparameters.get(skey);
		}
		sb = url + sb;		
		return queryJSON(sb);
	}
	
	public static JSONObject queryJSON(String s_url)
	{
		String result = "";
		HttpURLConnection conn = null;
		try{
			URL url = new URL(s_url);
			conn = (HttpURLConnection) url.openConnection();
			InputStreamReader in = new InputStreamReader(conn.getInputStream());
			BufferedReader br = new BufferedReader(in);
			
			String line;
			while((line = br.readLine()) != null)
			{
				result += line;
			}
			return new JSONObject(result);
		}
		catch (MalformedURLException e) {
            Log.e("Huxley", "Error in url " +  s_url, e);
        } 
        catch (IOException e) {
            Log.e("Huxley", "Error connecting to " + s_url, e);
        } 
        catch (JSONException e) {
            Log.e("Huxley", "Error constructing JSON object", e);
        }
        finally {
            if (conn != null) {
                conn.disconnect();
            }
        }		
        return null;

	}
}