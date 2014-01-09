package uk.hux;

import java.util.*;
import uk.hux.api_wrapper.*;
import java.io.*;
import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.util.*;

public class LocalLocationLog extends LocationLog implements Iterable<Location>
{
  private static final String LOCTABLE = "locations";
  private static final String UIDCOL = "userid";
  private static final String TIMECOL = "timestamp";
  private static final String LATCOL = "latitude";
  private static final String LONCOL = "longitude";
  private static final String ACCCOL = "accuracy";
  
  private class DatabaseManager extends SQLiteOpenHelper
  {
    public DatabaseManager(Context context)
    {
      super(context, "Huxley", null, 1);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) //only called if it doesn't exist
    {
      db.execSQL("CREATE TABLE "+LOCTABLE+" ("+UIDCOL+" INTEGER, "+TIMECOL+" INTEGER, "+LATCOL+" REAL, "+LONCOL+" REAL, "+ACCCOL+" REAL);");
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int from, int to)
    {
    }
  }
  
  private SQLiteDatabase logDB; //persistent copy
  private Vector<Location> log; //in-memory log
  
  public LocalLocationLog()
  {
    log = new Vector<Location>();
    logDB = new DatabaseManager(HuxleyCore.core.getApplicationContext()).getWritableDatabase();
    loadFromDB();
  }
  
  public void loadFromDB()
  {
    Cursor res = logDB.query(LOCTABLE, null, null, null, null, null, null); //i.e. select *
    if (res == null)
      Log.e("Huxley", "CURSOR NULL");
    while (res.moveToNext())
    {
      int uid = res.getInt(res.getColumnIndex(UIDCOL));
      long time = res.getLong(res.getColumnIndex(TIMECOL));
      double lat = res.getDouble(res.getColumnIndex(LATCOL));
      double lon = res.getDouble(res.getColumnIndex(LONCOL));
      double acc = res.getDouble(res.getColumnIndex(ACCCOL));
      log.add(new Location(uid, time, lat, lon, new Accuracy(acc)));
    }
  }
  
  public void loadFromFile(String filename) throws IOException
  {
    BufferedReader reader = new BufferedReader(new FileReader(new File(filename)));
    String line;
    while ((line = reader.readLine()) != null)
    {
      StringTokenizer tokeniser = new StringTokenizer(line, "\t");
      long time = Long.parseLong(tokeniser.nextToken());
      
      try
      {
        double lat = Double.parseDouble(tokeniser.nextToken());
        double lon = Double.parseDouble(tokeniser.nextToken());
        tokeniser.nextToken(); //altitude
        double accuracy = Double.parseDouble(tokeniser.nextToken());
        tokeniser.nextToken(); //speed
        tokeniser.nextToken(); //satellites
        String name = (tokeniser.hasMoreTokens() ? tokeniser.nextToken() : "");
        addLocation(new Location(0, time, lat, lon, new Accuracy(accuracy))); //user ID set to 0
      }
      catch (NumberFormatException e)
      {
        //skip the line
      }
    }
    reader.close();
  }
  
  public void addLocation(Location loc)
  {
    log.add(loc);
    
    ContentValues v = new ContentValues();
    v.put(UIDCOL, HuxleyCore.core.userID);
    v.put(TIMECOL, loc.timestamp);
    v.put(LATCOL, loc.latitude);
    v.put(LONCOL, loc.longitude);
    v.put(ACCCOL, loc.accuracy.getMetres());
    long retVal = logDB.insert(LOCTABLE, null, v);
    if (retVal == -1)
      Log.e("Huxley", "INSERT FAILED");
  }
  
  public Iterator<Location> iterator()
  {
    return log.iterator();
  }
  
  public int size()
  {
    return log.size();
  }
  
  public List<Place> whereWereYou(long timestamp)
  {
    if (log.size() == 0)
      return null;
    
    //simple binary chop
    int top = 0;
    int bottom = log.size()-1;
    while (top + 1 < bottom)
    {
      int i = (top+bottom)/2;
      long time_i = log.get(i).timestamp;
      if (time_i < timestamp) //then we want to look after i in log
        top = i+1;
      else //then we want to look before i in log
        bottom = i-1;
    }
    
    if (top <= bottom)
    {
      Location loc = log.get(top);
      GooglePlacesAPI lookup = new GooglePlacesAPI(GooglePlacesAPI.DEFAULT_URL, "AIzaSyDfFLxnubvYNeA-TgnrLwdBSdTCFLiu5yY");
      return lookup.search(loc);
    }
    return null;
  }
  
  public static void main(String args[]) throws Exception //test
  {
    /*LocationLog log = new LocationLog();
    log.loadFromFile("f:/domenico/huxley/huxley/tracklog-iceland.txt");
    for (Location ll : log)
      System.out.println(ll);*/
  }
}