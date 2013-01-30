package uk.hux.test;

import uk.hux.Accuracy;
import uk.hux.Location;
import uk.hux.Place;
import uk.hux.api_wrapper.GooglePlacesAPI;
import junit.framework.*;
import java.util.*;
import android.util.Log;

public class APIWrapperTest extends TestCase {

	
	public void testPlaces() 
	{
		GooglePlacesAPI gpa = new GooglePlacesAPI("https://maps.googleapis.com/maps/api/place/nearbysearch/json", "AIzaSyDfFLxnubvYNeA-TgnrLwdBSdTCFLiu5yY");
		Date today = new Date();
		Location loc = new Location(0L, today.getTime(), 51.500194,-0.131836, new Accuracy(50));
		List<Place> lp = gpa.search(loc);
		// Not really testing much here, we could have no place around
		assertTrue("No place returned", lp.size() > 0);
		for(Place l : lp)
		{
			// Name
			Log.d("Huxley", "Name: " + l.name);
			// Not really testing much here, the name can be empty
			Assert.assertTrue(l.name.length() >= 0);
			// Reference field (unique identifier of the place)
			Assert.assertTrue(l.reference.length() > 0);
		}
	}
	
	
}
