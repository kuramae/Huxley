package uk.hux;

public class Accuracy {
	public final double metres;

	public Accuracy(double metres)
	{
		this.metres = metres;
	}

	public double getMetres() 
	{
		return metres;
	}
  
  public String toString()
  {
    return metres+"m";
  }
}
