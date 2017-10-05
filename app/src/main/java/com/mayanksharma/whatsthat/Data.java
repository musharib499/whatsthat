package com.mayanksharma.whatsthat;

/**
 * Created by nxp50640 on 27-09-2017.
 */

public class Data {

    private String Course;
    private String Year;
    private String Sem;
    private String Id;
    private String Url;

    public Data ()
    {

    }

    public Data(String course, String year, String sem, String id, String url) {
        this.Course = course;
        this.Year = year;
        this.Sem = sem;
        this.Id = id;
        this.Url= url;
    }

    public String getCourse() {
        return Course;
    }

    public String getYear() {
        return Year;
    }

    public String getSem() {
        return Sem;
    }

    public String getId() {
        return Id;
    }

    public String getUrl() {
        return Url;
    }

    public void setCourse(String course) {
        Course = course;
    }

    public void setYear(String year) {
        Year = year;
    }

    public void setSem(String sem) {
        Sem = sem;
    }

    public void setId(String id) {
        Id = id;
    }

    public void setUrl(String url) {
        Url = url;
    }
}
