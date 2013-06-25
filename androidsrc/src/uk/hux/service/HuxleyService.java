package uk.hux.service;

import uk.hux.gui.*;
import uk.hux.*;
import android.app.*;
import android.content.*;
import android.os.*;
import android.net.*;
import android.widget.Toast;
import android.location.*;
import android.util.*;

/*
Logs location data
*/

public class HuxleyService extends Service implements LocationListener
{
  private static final String HUXSERVICE = "HuxleyService";
  private static final int ERROR_MSG = 7777;
  private static final int DANYEY_MSG = 8888;
  private static final int GPS_MSG = 9999;
  private static final float accuracyThreshold = 512;
  
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
    Log.v(HUXSERVICE, "Starting HuxleyService");
    locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    connMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    
    int networkTime = 180000;
    if (locMan.isProviderEnabled(locMan.GPS_PROVIDER))
    {
      Toast.makeText(HuxleyService.this, "GPS enabled", Toast.LENGTH_SHORT).show();
      locMan.requestLocationUpdates(locMan.GPS_PROVIDER, 60000, 50.0f, this);
    }
    else
    {
      Toast.makeText(HuxleyService.this, "GPS disabled", Toast.LENGTH_LONG).show();
      Toast.makeText(HuxleyService.this, "Using network localisation", Toast.LENGTH_SHORT).show();
      networkTime = 30000;
    }
    
    final Handler handler = new Handler();
    final int finalNetworkTime = networkTime; //finalise the value; over-strict java type system
    handler.postDelayed(new Runnable()
      {
        public void run()
        {
          if (HuxleyCore.core.serviceRunning)
          {
            Log.v(HUXSERVICE, "Higher than: " + finalNetworkTime + " < " + System.currentTimeMillis() + " - " + lastGPSLock);
            if (System.currentTimeMillis() - lastGPSLock > finalNetworkTime) //only if a long gap in GPS
              processLocation(locMan.getLastKnownLocation(locMan.NETWORK_PROVIDER), false);
            handler.postDelayed(this, finalNetworkTime);
          }
        }
      }, finalNetworkTime);
    showMessage("Huxley service started", GPS_MSG);
    HuxleyCore.core.serviceRunning = true;
  }
  
  @Override
  public void onDestroy()
  {
    HuxleyCore.core.serviceRunning = false;
    NotificationManager notifier = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    notifier.cancelAll();
    locMan.removeUpdates(this);
  }
  
  @Override
  public IBinder onBind(Intent intent)
  {
    return null;
  }
  
  public void onLocationChanged(android.location.Location location)
  {
    lastGPSLock = System.currentTimeMillis();
    processLocation(location, true);
  }
  
  private void processLocation(android.location.Location location, boolean fromGPS)
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
        android.location.Location networkLocation = locMan.getLastKnownLocation(locMan.NETWORK_PROVIDER); //try the network also
        if (networkLocation != null && networkLocation.hasAccuracy() && location.getAccuracy() > networkLocation.getAccuracy())
        {
          showMessage("Using network location ("+networkLocation.getAccuracy()+"m versus "+location.getAccuracy()+"m)", ERROR_MSG);
          location = networkLocation;
        }
      }
      
      if (location.getAccuracy() <= accuracyThreshold || !fromGPS)
      {
        String locDescription = "";
        showMessage("GPS: "+location.getLatitude()+", "+location.getLongitude()+(locDescription.equals("") ? "" : " ("+locDescription+")"), GPS_MSG);
        HuxleyCore.core.log.addLocation(new uk.hux.Location(HuxleyCore.core.userID, System.currentTimeMillis(), location.getLatitude(), location.getLongitude(), new Accuracy(location.getAccuracy())));
      }
    }
    catch (Exception e)
    {
      String line = " ";
      if (e.getStackTrace() != null)
        line += "(line "+e.getStackTrace()[0].getLineNumber()+")";
      Toast.makeText(HuxleyService.this, e.getClass().getName()+": "+e.getMessage()+line, Toast.LENGTH_SHORT).show();
    }
  }
  
  public void showMessage(String message, int type)
  {
    try
    {
      NotificationManager notifier = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
      notifier.cancel(type);
      Notification msg = new Notification(R.drawable.hux, "Huxley", System.currentTimeMillis());
      PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, HuxleyGUI.class), 0);
      msg.setLatestEventInfo(this, "Huxley", message, contentIntent);
      notifier.notify(type, msg);
    }
    catch (Exception e)
    {
      Toast.makeText(HuxleyService.this, "Huxley exception: "+e.getMessage(), Toast.LENGTH_LONG).show();
    }
  }
  
  public void onProviderDisabled(String provider)
  {
    showMessage("GPS disabled", ERROR_MSG);
  }
  
  public void onProviderEnabled(String provider) {}
  public void onStatusChanged(String provider, int status, Bundle extras) {}
}