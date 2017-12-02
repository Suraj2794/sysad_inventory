package com.example.suraj.inventory_app.data;

/**
 * Created by Suraj Singh on 10-03-2017.
 */
public class HistoryData {

    String system_name,tag,issue_date,processor,location,return_date,ram_size;
    public HistoryData(String system_name,String tag,String issue_date,String return_date,String processor,String ram_size,String location)
    {
        this.system_name=system_name;
        this.tag=tag;
        this.issue_date=issue_date;
        this.return_date=return_date;
        this.processor=processor;
        this.ram_size=ram_size;
        this.location=location;
    }

    public String getSystem_name()
    {
        return system_name;
    }

    public String get_tag()
    {
        return tag;
    }

    public String getIssue_date()
    {
        return issue_date;
    }

    public String getReturn_date()
    {
        return return_date;
    }

    public  String getProcessor(){return processor;}
    public String getLocation(){return location;}
    public String getRam_size(){return ram_size;}
}
