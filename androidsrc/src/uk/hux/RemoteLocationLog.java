package uk.hux;

import org.json.*;
import java.net.*;
import java.util.*;
import android.net.*;
import android.content.*;
import java.io.*;

/*
If internet available, sends calls to server, else acts locally.

Assumes DB/JSON schema:
LOCATION (hashed user ID, timestamp, latitude, longitude, accuracy)

*/

public class RemoteLocationLog extends LocalLocationLog
{
  private static final String SERVER = "http://wherever:8080/";
  
  private boolean networkAvailable()
  {
    ConnectivityManager connMan = (ConnectivityManager) HuxleyCore.core.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo ni = connMan.getActiveNetworkInfo();
    return (ni != null && ni.isConnected());
  }
  
  /*
  Assumes DB/JSON schema for POST:
  LOCATION (hashed user ID, timestamp, latitude, longitude, accuracy)
  */
  public void addLocation(Location loc)
  {
    if (networkAvailable())
    {
      try
      {
        URL url = new URL(SERVER+"loc");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        
        JSONObject jLoc = new JSONObject();
        jLoc.put("userid", HuxleyCore.core.getUserIDHash());
        jLoc.put("timestamp", loc.timestamp);
        jLoc.put("lat", loc.latitude);
        jLoc.put("lon", loc.longitude);
        jLoc.put("acc", loc.accuracy);
        
        PrintWriter out = new PrintWriter(conn.getOutputStream());
        out.print(jLoc.toString());
        out.close();
      }
      catch (Exception xx)
      {
        super.addLocation(loc);
      }
    }
    else
      super.addLocation(loc);
  }
  
  /*
  Assumes JSON schema for GET response:
  [ {name:..., ref:...},... ]
  */
  public List<Place> whereWereYou(long timestamp)
  {
    if (networkAvailable())
    {
      try
      {
        URL url = new URL(SERVER+"where?uid="+HuxleyCore.core.getUserIDHash()+"&timestamp="+timestamp);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        try
        {
          String res = conn.getInputStream().toString();
          
          JSONArray response = new JSONArray(res);
          List<Place> results = new ArrayList<Place>();
          for (int i = 0; i < response.length(); i++)
          {
            JSONObject place = response.getJSONObject(i);
            results.add(new Place(place.getString("name"), place.getString("ref")));
          }
          return results;
        }
        finally
        {
          conn.disconnect();
        }
      }
      catch (Exception xx)
      {
        return super.whereWereYou(timestamp);
      }
    }
    else
      return super.whereWereYou(timestamp);
  }
}