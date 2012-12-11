package uk.hux;

public class Place 
{
  public String name;
  public String reference;
  
  public Place(String name, String reference)  {
	  this.name = name;
	  this.reference = reference;
  }
  
  public String toString()
  {
	  return name;
  }
}