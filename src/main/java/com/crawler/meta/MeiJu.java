package com.crawler.meta;

import java.util.List;

/**
 * Created by guoyanlei
 * date：2017/12/16
 * time：22:02
 * description：
 */
public class MeiJu extends MeiJuSimple{

    private String title;
    private String date;
    private String image;
    private String summary;
    private List<List<DownLinkInfo>> downLinks;

    public MeiJu() {
    }

    public MeiJu(MeiJuSimple meiJuSimple){
        tid = meiJuSimple.getTid();
        url = meiJuSimple.getUrl();
        updateStatus = meiJuSimple.getUpdateStatus();
        tagCH = meiJuSimple.getTagCH();
        tagEN = meiJuSimple.getTagEN();
        categoryCH = meiJuSimple.getCategoryCH();
        categoryEN = meiJuSimple.getCategoryEN();
        isEnd = meiJuSimple.isEnd();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<List<DownLinkInfo>> getDownLinks() {
        return downLinks;
    }

    public void setDownLinks(List<List<DownLinkInfo>> downLinks) {
        this.downLinks = downLinks;
    }
}
