package uk.hux;

public abstract class UserStatus
{
	public final long userID;
	public final long timestamp; //unix

	protected UserStatus(long userID, long timestamp)
	{
		this.userID = userID;
		this.timestamp = timestamp;
	}
}