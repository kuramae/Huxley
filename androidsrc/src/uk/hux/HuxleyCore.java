package uk.hux;

import android.app.*;
import java.io.*;

public class HuxleyCore extends Application
{
  public static HuxleyCore core; //singleton
  
  public int userID = 0;
  public LocationLog log;
  public boolean serviceRunning = false;
  
  public HuxleyCore()
  {
    core = this;
    
    Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler()
      {
        @Override
        public void uncaughtException(Thread t, Throwable e)
        {
          try
          {
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream("/sdcard/huxleydump.txt")));
            e.printStackTrace(pw);
            pw.flush();
            pw.close();
          }
          catch (FileNotFoundException ff) {}
        }
      });
  }
  
  public void onCreate()
  {
    //load globals at app startup
    userID = 0;
    log = new LocationLog();
  }
}