package uk.hux;

import java.util.*;
import uk.hux.api_wrapper.*;
import java.io.*;
import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.util.*;

public abstract class LocationLog
{
  public abstract void addLocation(Location loc);
  
  public abstract List<Place> whereWereYou(long timestamp);
}