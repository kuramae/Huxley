package com.huxley;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.json.*;

@Path("where")
public class WhereWereYou
{
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public String whereWereYou(@QueryParam("userid") String userID, @QueryParam("timestamp") long timestamp)
  {
    //look in DB for userID
    //interpolate for timestamp
    //ask google for list of potentials
    return (new JSONArray()).toString(); //empty
  }
}
