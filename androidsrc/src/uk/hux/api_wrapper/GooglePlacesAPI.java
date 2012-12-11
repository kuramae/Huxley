package uk.hux.api_wrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import java.io.IOException;
import uk.hux.*;
import java.util.*;
import java.net.*;

public class GooglePlacesAPI {
	private String base_url;
	private String key;
	
	
	public String getBaseUrl()
	{
		return base_url;
	}
	
	public String getKey()
	{
		return key;
	}
	
	
	
	public GooglePlacesAPI(
		String base_url,
		String key)
	{
		this.key = key;
		this.base_url = base_url;
	}
	
	public  List<Place> search(
		Location loc) 
	{
		return search(loc,null);	
	}
		
	public  List<Place> search(
		Location loc,
		String keyword) 
	{
		ArrayList<Place> resultList = null;
		Map<String,String> arguments = new HashMap<String, String>();
		arguments.put("sensor", "false");
		arguments.put("key", getKey());
		if (keyword != null)
			arguments.put("keyword", keyword);
		arguments.put("location", (loc.latitude) + "," + (loc.longitude));
		arguments.put("radius", Double.toString(loc.accuracy));
		
		try {
			JSONArray json_res = 
			HTTPQuery.queryJSON(getBaseUrl(), arguments).getJSONArray("results");
			
			for (int i = 0; i < json_res.length(); i++) {
				Place place = new Place(json_res.getJSONObject(i).getString("name"),
										json_res.getJSONObject(i).getString("reference"));
				resultList.add(place);
			}
			
		}
		catch (Exception e) {
            Log.e("Huxley", "Error connecting to Places API", e);
            return resultList;
        } 

		return resultList;
		
		
	}
 
}