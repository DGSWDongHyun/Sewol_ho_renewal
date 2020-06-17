package com.solo_dev.remember_renewal.Write_Activity.data_item;

public class Data_Write {

    String Title;
    String contents;
    String Date;
    String getkeys;
    String  img_contents;
    int liked = 0;
    int reported = 0;
    boolean viewing;
    String Display_name;
    String users;

    public Data_Write(){ }

    public Data_Write(String Title, String contents, String Date, String img_contents, int liked, int reported, String getkeys, boolean viewing, String users, String Display_Name){
        this.Title = Title;
        this.contents = contents;
        this.Date = Date;
        this.img_contents = img_contents;
        this.liked = liked;
        this.reported = reported;
        this.getkeys = getkeys;
        this.viewing = viewing;
        this.users = users;
        this.Display_name = Display_Name;
    }

    public String getDisplay_name() {
        return Display_name;
    }

    public void setDisplay_name(String display_name) {
        Display_name = display_name;
    }

    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }

    public boolean isViewing() {
        return viewing;
    }

    public void setViewing(boolean viewing) {
        this.viewing = viewing;
    }

    public String getGetkeys() {
        return getkeys;
    }

    public void setGetkeys(String getkeys) {
        this.getkeys = getkeys;
    }

    public String  getImg_contents() {
        return img_contents;
    }

    public void setImg_contents(String img_contents) {
        this.img_contents = img_contents;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public int getLiked() {
        return liked;
    }

    public void setLiked(int liked) {
        this.liked = liked;
    }

    public int getReported() {
        return reported;
    }

    public void setReported(int reported) {
        this.reported = reported;
    }
}

