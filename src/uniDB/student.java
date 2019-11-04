package uniDB;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class student extends user {
	private double GPA;
	private String classification;
	private String major;
	private String id;
	private Map<String, String> att;

	public student() {}
	public student(String username, String password, String fullname)
	{
		this.setUsername(username);
		this.setPassword(password);
		this.setFullname(fullname);
		this.generateID();
		att = new HashMap<>();
	}

	public double getGPA() {
		return this.GPA;
	}

	public void setGPA(double GPA) {
		this.GPA = GPA;
	}

	public String getClassification() {
		return this.classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}
	
	public String getMajor() {
		return this.major;
	}

	public void setMajor(String major) {
		this.major = major;
	}
	
	public void addAttribute(String attribut, String value)
	{
		att.put(attribut, value);
	}

	public void editAttribute(String attribut, String value) {
		att.put(attribut,value);
	}

	public void removeAttribute(String attribut)
	{
		att.remove(attribut);
	}

	@Override
	public String toString(){
		String toReturn = "--------------------\n";
		toReturn += getFullname() + "\n" + getId() + "\n" + getUsername() + "\n" + getClassification() + "\n" + getMajor() + "\n";
		for(String key: att.keySet()){
			toReturn += key.toString() + ":" + att.get(key) + "\n";
		}
		toReturn += "--------------------\n";
		return toReturn;
	}
}

