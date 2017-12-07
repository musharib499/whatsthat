
package com.mayanksharma.whatsthat.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Course implements Parcelable
{

    private String course;
    private String id;
    private String sem;
    private String year;
    private String url;
    private String roomNo;
    public final static Creator<Course> CREATOR = new Creator<Course>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Course createFromParcel(Parcel in) {
            return new Course(in);
        }

        public Course[] newArray(int size) {
            return (new Course[size]);
        }

    }
            ;

    protected Course(Parcel in) {
        this.course = ((String) in.readValue((String.class.getClassLoader())));
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.sem = ((String) in.readValue((String.class.getClassLoader())));
        this.year = ((String) in.readValue((String.class.getClassLoader())));
        this.url = ((String) in.readValue((String.class.getClassLoader())));
        this.roomNo = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Course() {
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSem() {
        return sem;
    }

    public void setSem(String sem) {
        this.sem = sem;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getRoomNo()
    {
        return roomNo;
    }

    public void setRoomNo(String roomNo)
    {
        this.roomNo = roomNo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(course);
        dest.writeValue(id);
        dest.writeValue(sem);
        dest.writeValue(year);
        dest.writeValue(url);
        dest.writeValue(roomNo);
    }

    public int describeContents() {
        return  0;
    }


}
