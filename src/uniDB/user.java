package uniDB;

import java.util.UUID;
import java.util.Queue;


/**
 * 
 * Abstract User class meant to be extended by Student and Faculty
 *
 */
public abstract class user {
	public String username;
	protected String password;
	public String fullname;

	protected UUID ID;
	protected Queue<String> inbox;

	/**
	 * 
	 * @param usrnme - Your username
	 * @param psswrd - Your Password This is used to verify credentials matching
	 *               account
	 * @return Boolean - Verification of login
	 */
	public boolean login(String usrnme, String psswrd) {
		return (psswrd.equals(this.password) && usrnme.equals(this.username));
	}

	/**
	 * 
	 * @param psswrd - Your new password Sets password
	 */
	public void setPassword(String psswrd) {
		this.password = psswrd;
	}

	/**
	 * Gets username
	 * @return String
	 */
	public String getUsername() {
		return this.username;
	}

	/**
	 * 
	 * @param username
	 * Sets and gets username
	 * @return String
	 */
	public String setUsername(String username) {
		return this.username = username;
	}
	
	/**
	 * Fetches fullname
	 * @return String
	 */
	public String getFullname()
	{
		return this.fullname;
	}

	/**
	 * 
	 * @param fullname
	 * Sets and gets fullname
	 * @return String
	 */
	public String setFullname(String fullname) {
		return this.fullname = fullname;
	}

	/**
	 * Gets ID
	 * @return String
	 */
	public String getId() {
		return ID.toString();
	}

	/**
	 * Generates unique random ID string
	 */
	public void generateID() {
		this.ID = ID.randomUUID();
	}

	/**
	 * Pulls all messages out of user's inbox and returns them as a string
	 * @return String
	 */
	public String[] flushInbox()
	{
		String[] arr = new String[inbox.size()];
		if(inbox.size() == 0)
		{
			arr[0] = "Inbox is empty";
			return arr;
		}
		for(int i = 0; i < inbox.size(); i++)
		{
			arr[i] = inbox.poll();
		}
		return arr;
	}

	/**
	 * Adds message to inbox
	 * @param message
	 */
	public void addMessage(String message)
	{
		inbox.add(message);
	}
}
