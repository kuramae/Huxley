package uk.hux.gui;

import uk.hux.service.*;
import uk.hux.*;
import android.app.*;
import android.os.Bundle;
import android.location.*;
import android.widget.*;
import android.content.*;
import android.view.*;
import java.util.*;

public class TimeChooserGUI extends Activity
{
  private DatePicker date;
  private TimePicker time;
  private Button whereButton;
  
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    
    final TextView text = new TextView(this);
    
    date = new DatePicker(this);
    date.setMaxDate(System.currentTimeMillis());
    
    time = new TimePicker(this);
    
    whereButton = new Button(this);
    whereButton.setText("So where the fuck was I?");
    whereButton.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View v)
        {
          GregorianCalendar cal = new GregorianCalendar(date.getYear(), date.getMonth(), date.getDayOfMonth(), time.getCurrentHour(), time.getCurrentMinute());
          List<Place> places = HuxleyCore.core.log.whereWereYou(cal.getTimeInMillis());
          if (places != null)
          {
            for (Place p : places)
              text.append(p.name+" ?\n");
          }
          else
            text.append("No places returned\n");
        }
      });
      
    TableLayout layout = new TableLayout(this);
    layout.addView(date);
    layout.addView(time);
    layout.addView(whereButton);
    layout.addView(text);
    
    setContentView(layout);
  }
}
