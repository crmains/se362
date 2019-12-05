package uniDB;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class Fair {

    private String _building;
    private int _capacity;
    ArrayList<String> companies;
    private Date _date;

    public Fair(String building, int capacity, Date date){
        _building = building;
        _capacity = capacity;
        _date = date;
        companies = new ArrayList<>();
    }

    public int get_capacity(){
        return _capacity;
    }

    public int getCount(){
        return companies.size();
    }

    public void registerCompany(String company){
        if(!companies.contains(company)) {
            companies.add(company);
        }
    }

    public Date get_date(){
        return _date;
    }

    public String get_building(){
        return _building;
    }

    public void removeCompany(String company){
        if(companies.contains(company)){
            companies.remove(company);
        }
    }

    @Override
    public String toString(){
        String temp = "";
   temp = "Building:" + get_building() + "\n" +
    "Date:" + get_date().toString() + "\n" +
    "Companies:" + "\n";
   for(String x : companies){
       temp += x + "\n";
   }
   return temp;
    }

}