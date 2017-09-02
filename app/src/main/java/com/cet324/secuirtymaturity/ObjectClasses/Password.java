package com.cet324.secuirtymaturity.ObjectClasses;

/**
 * Created by DanielWilkinson on 1/1/2017.
 */
public class Password {
    String name,date,company,email,frequencyOfChange;
    public String getFrequencyOfChange() {return frequencyOfChange;}
    public void setFrequencyOfChange(String frequencyOfChange) {this.frequencyOfChange = frequencyOfChange;}
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getCompany() {
        return company;
    }
    public void setCompany(String company) {
        this.company = company;
    }
    public Password(){}
    public Password( String name, String company, String date, String email, String frequencyOfChange) {
        this.name = name;
        this.company = company;
        this.date = date;
        this.email = email;
        this.frequencyOfChange = frequencyOfChange;
    }
}
