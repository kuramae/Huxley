package uk.hux;

import java.util.*;
import java.io.*;

public class LocationLog implements Iterable<Location>
{
  private Vector<Location> log = new Vector<Location>();
  
  public LocationLog()
  {
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
        addLocation(new Location(0, time, lat, lon, new Accuracy(accuracy)));
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
  }
  
  public Iterator<Location> iterator()
  {
    return log.iterator();
  }
  
  public static void main(String args[]) throws Exception //test
  {
    LocationLog log = new LocationLog();
    log.loadFromFile("f:/domenico/huxley/huxley/tracklog-iceland.txt");
    for (Location ll : log)
      System.out.println(ll);
  }
}