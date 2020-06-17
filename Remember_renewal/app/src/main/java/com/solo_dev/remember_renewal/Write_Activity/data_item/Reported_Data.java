package com.solo_dev.remember_renewal.Write_Activity.data_item;

public class Reported_Data {
    String reporter;
    String reported_contents;

    public Reported_Data(String reporter, String reported_contents ){
        this.reporter = reporter;
        this.reported_contents = reported_contents;
    }

    public String getReported_contents() {
        return reported_contents;
    }

    public void setReported_contents(String reported_contents) {
        this.reported_contents = reported_contents;
    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }
}
