package com.mayanksharma.whatsthat;

/**
 * Created by nxp50640 on 05-10-2017.
 */

public class Qrdata {
    private String Qurl;

    public Qrdata ()
    {

    }

    public Qrdata(String qurl) {
        this.Qurl = qurl;
    }

    public String getQurl() {
        return Qurl;
    }

    public void setQurl(String qurl) {    
        Qurl = qurl;
    }
}
