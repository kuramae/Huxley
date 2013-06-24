package uk.hux.gui;

import uk.hux.service.HuxleyService;
import android.app.Activity;
import android.os.Bundle;
import android.location.*;
import android.widget.*;
import android.content.*;
import android.view.*;

public class HuxleyGUI extends Activity
{
  private TextView text;
  private Button startButton;
  private Button stopButton;
  private Button logButton;
  
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    
    // Setting up screen
    text = new TextView(this);
    startButton = new Button(this);
    startButton.setText("Start Huxley");
    startButton.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View v)
        {
          text.append("Trying to start service\n");
          startService(new Intent(HuxleyGUI.this, HuxleyService.class));
          text.append("Started\n");
          startButton.setEnabled(false);
          stopButton.setEnabled(true);
        }
      });
    stopButton = new Button(this);
    stopButton.setText("Stop Huxley");
    stopButton.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View v)
        {
          text.append("Trying to stop service\n");
          stopService(new Intent(HuxleyGUI.this, HuxleyService.class));
          text.append("Stopped\n");
          startButton.setEnabled(true);
          stopButton.setEnabled(false);
        }
      });
    
    logButton = new Button(this);
    logButton.setText("Log status");
    logButton.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View v)
        {
          if (HuxleyService.log == null)
            text.append("Log empty\n");
          else
            text.append(HuxleyService.log.size()+" log entries\n");
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
    buttonRow.addView(logButton);
    layout.addView(nameRow);
    layout.addView(buttonRow);
    layout.addView(text);
    
    setContentView(layout);
    
    checkStatus();
  }
  
  public void checkStatus()
  {
    // Checking if GPS and/or network localisations are enabled and display them
    // This is run on startup
    if (HuxleyService.running)
    {
      startButton.setEnabled(false);
      stopButton.setEnabled(true);
      text.setText("Service already running\n");
    }
    else
    {
      startButton.setEnabled(true);
      stopButton.setEnabled(false);
      text.setText("Service not running\n");
    }
    
    LocationManager locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    if (locMan.isProviderEnabled(locMan.GPS_PROVIDER))
      text.append("GPS enabled\n");
    else
      text.append("GPS disabled\n");
    if (locMan.isProviderEnabled(locMan.NETWORK_PROVIDER))
      text.append("Network localisation enabled\n");
    else
      text.append("Network localisation disabled\n");
  }
}
