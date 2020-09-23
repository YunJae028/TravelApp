package com.example.customlistview;


public class ListViewItem {

    private String firstimage;
    private String title;
    private String address;
    private String tel;
    private String contentid;
    private String readcount;
    double mapx;
    double mapy;

    public String getFirstimage() {
        return firstimage;
    }

    public void setFirstimage(String firstimage) {
        this.firstimage = firstimage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public double getMapx() {
        return mapx;
    }

    public void setMapx(double mapx) {
        this.mapx = mapx;
    }

    public double getMapy() {
        return mapy;
    }

    public void setMapy(double mapy) {
        this.mapy = mapy;
    }

    public void setContentId(String contentid){this.contentid = contentid;}

    public String getContentId(){return contentid;}

    public void setReadcount(String readcount){this.readcount = readcount;}

    public String getReadcount(){return readcount;}


}


