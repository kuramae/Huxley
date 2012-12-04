package uk.hux;

import android.app.*;
import android.content.*;
import android.os.*;
import android.net.*;
import android.widget.Toast;
import android.location.*;
import java.net.*;
import java.util.List;
import java.util.Vector;
import java.io.*;

/*
Logs location data
*/

public class HuxleyService extends Service implements LocationListener
{
  public static boolean running = false;
  private final int ERROR_MSG = 7777;
  private final int DANYEY_MSG = 8888;
  private final int GPS_MSG = 9999;
  private final float accuracyThreshold = 512;
  private LocationManager locMan;
  private ConnectivityManager connMan;
  private long lastGPSLock = 0;
  
  @Override
  public void onCreate()
  {
  }
  
  @Override
  public void onStart(Intent intent, int startId)
  {
    locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    connMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    int networkTime = 180000;
    if (locMan.isProviderEnabled(locMan.GPS_PROVIDER))
    {
      Toast.makeText(TrackingService.this, "GPS enabled", Toast.LENGTH_SHORT).show();
      locMan.requestLocationUpdates(locMan.GPS_PROVIDER, 60000, 50.0f, this);
    }
    else
    {
      Toast.makeText(TrackingService.this, "GPS disabled", Toast.LENGTH_LONG).show();
      Toast.makeText(TrackingService.this, "Using network localisation", Toast.LENGTH_SHORT).show();
      networkTime = 30000;
    }
    final Handler handler = new Handler();
    final int finalNetworkTime = networkTime; //finalise the value; over-strict java type system
    handler.postDelayed(new Runnable()
      {
        public void run()
        {
          if (TrackingService.running)
          {
            if (System.currentTimeMillis() - lastGPSLock > finalNetworkTime) //only if a long gap in GPS
              processLocation(locMan.getLastKnownLocation(locMan.NETWORK_PROVIDER), false);
            handler.postDelayed(this, finalNetworkTime);
          }
        }
      }, finalNetworkTime);
    showMessage("Huxley service started", GPS_MSG);
    running = true;
  }
  
  @Override
  public void onDestroy()
  {
    running = false;
    NotificationManager notifier = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    notifier.cancelAll();
    locMan.removeUpdates(this);
  }
  
  @Override
  public IBinder onBind(Intent intent)
  {
    return null;
  }
  
  public void onLocationChanged(Location location)
  {
    lastGPSLock = System.currentTimeMillis();
    processLocation(location, true);
  }
  
  private void processLocation(Location location, boolean fromGPS)
  {
    try
    {
      int satellites = 0;
      if (location.getExtras() != null && location.getExtras().get("satellites") != null)
        satellites = (Integer) location.getExtras().get("satellites");
      if (location.getAccuracy() > accuracyThreshold) 
        showMessage("GPS accuracy low ("+location.getAccuracy()+"m, "+satellites+" satellites)", GPS_MSG);
      
      if (fromGPS)
      {
        Location networkLocation = locMan.getLastKnownLocation(locMan.NETWORK_PROVIDER); //try the network also
        if (networkLocation != null && networkLocation.hasAccuracy() && location.getAccuracy() > networkLocation.getAccuracy())
        {
          showMessage("Using network location ("+networkLocation.getAccuracy()+"m versus "+location.getAccuracy()+"m)", ERROR_MSG);
          location = networkLocation;
        }
      }
      
      if (location.getAccuracy() <= accuracyThreshold || !fromGPS)
      {
        String locDescription = "";
        showMessage("GPS: "+threeDP(location.getLatitude())+", "+threeDP(location.getLongitude())+(locDescription.equals("") ? "" : " ("+locDescription+")"), GPS_MSG);
        //log it
      }
    }
    catch (Exception e)
    {
      String line = " ";
      if (e.getStackTrace() != null)
        line += "(line "+e.getStackTrace()[0].getLineNumber()+")";
      Toast.makeText(TrackingService.this, e.getClass().getName()+": "+e.getMessage()+line, Toast.LENGTH_SHORT).show();
    }
  }
  
  public void showMessage(String message, int type)
  {
    try
    {
      NotificationManager notifier = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
      notifier.cancel(type);
      Notification msg = new Notification(R.drawable.notify, "Huxley", System.currentTimeMillis());
      PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, Tracking.class), 0);
      msg.setLatestEventInfo(this, "Huxley", message, contentIntent);
      notifier.notify(type, msg);
    }
    catch (Exception e)
    {
      Toast.makeText(TrackingService.this, "Huxley exception: "+e.getMessage(), Toast.LENGTH_LONG).show();
    }
  }
  
  public void onProviderDisabled(String provider)
  {
    showMessage("GPS disabled", ERROR_MSG);
  }
  
  public void onProviderEnabled(String provider) {}
  public void onStatusChanged(String provider, int status, Bundle extras) {}
}