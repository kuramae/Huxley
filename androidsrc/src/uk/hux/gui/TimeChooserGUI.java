package uk.hux.gui;

import uk.hux.service.*;
import uk.hux.*;
import android.app.*;
import android.os.*;
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
  private TextView text;
  
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.time);
    
    date = (DatePicker) findViewById(R.id.date);
    time = (TimePicker) findViewById(R.id.time);
    whereButton = (Button) findViewById(R.id.where);
    text = (TextView) findViewById(R.id.text);
    
    date.setMaxDate(System.currentTimeMillis());

    whereButton.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View v)
        {
          whereButton.setEnabled(false);
          Handler handler = new Handler();
          handler.post(new Runnable()
            {
              public void run()
              {
                GregorianCalendar cal = new GregorianCalendar(date.getYear(), date.getMonth(), date.getDayOfMonth(), time.getCurrentHour(), time.getCurrentMinute());
                List<Place> places = HuxleyCore.core.log.whereWereYou(cal.getTimeInMillis());
                text.setText("");
                if (places != null)
                {
                  for (Place p : places)
                    text.append(p.name+" ?\n");
                }
                else
                  text.append("No places returned\n");
                whereButton.setEnabled(true);
              }
            });
        }
      });
  }
}
