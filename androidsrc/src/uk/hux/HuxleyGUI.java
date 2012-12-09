package uk.hux;

import android.app.Activity;
import android.os.Bundle;
import android.location.*;
import android.app.*;
import android.widget.*;
import android.content.*;
import android.content.res.Resources;
import android.view.*;
import java.net.*;

public class HuxleyGUI extends Activity
{
  private TextView text;
  private Button startButton;
  private Button stopButton;
  
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    
    text = new TextView(this);
    startButton = new Button(this);
    startButton.setText("Start Huxley service");
    startButton.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View v)
        {
          text.append("Trying to start service\n");
          startService(new Intent(HuxleyGUI.this, HuxleyService.class));
          text.append("Started\n");
        }
      });
    stopButton = new Button(this);
    stopButton.setText("Stop Huxley service");
    stopButton.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View v)
        {
          text.append("Trying to stop service\n");
          stopService(new Intent(HuxleyGUI.this, HuxleyService.class));
          text.append("Stopped\n");
        }
      });
    
    TextView name = new TextView(this);
    name.setText("Huxley");
    name.setTextSize(40);
    
    TableLayout layout = new TableLayout(this);
    layout.setColumnStretchable(0, true);
    layout.setColumnStretchable(1, true);
    TableRow buttonRow = new TableRow(this);
    TableRow nameRow = new TableRow(this);
    nameRow.addView(name);
    buttonRow.addView(startButton);
    buttonRow.addView(stopButton);
    layout.addView(nameRow);
    layout.addView(buttonRow);
    layout.addView(text);
    
    setContentView(R.layout.main);
    
    LocationManager locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    if (locMan.isProviderEnabled(locMan.GPS_PROVIDER))
      text.setText("GPS enabled\n");
    else
      text.setText("GPS disabled\n");
    if (locMan.isProviderEnabled(locMan.NETWORK_PROVIDER))
      text.append("Network localisation enabled\n");
    else
      text.append("Network localisation disabled\n");
  }
}
