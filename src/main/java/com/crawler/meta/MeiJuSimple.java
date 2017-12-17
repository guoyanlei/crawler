package com.crawler.meta;

/**
 * Created by guoyanlei
 * date：2017/12/16
 * time：22:09
 * description：
 */
public class MeiJuSimple {

    protected Integer tid;
    protected String url;
    protected String updateStatus;
    protected String tagCH;
    protected String tagEN;
    protected String categoryCH;
    protected String categoryEN;
    protected boolean isEnd;

    public Integer getTid() {
        return tid;
    }

    public void setTid(Integer tid) {
        this.tid = tid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUpdateStatus() {
        return updateStatus;
    }

    public void setUpdateStatus(String updateStatus) {
        this.updateStatus = updateStatus;
    }

    public String getTagCH() {
        return tagCH;
    }

    public void setTagCH(String tagCH) {
        this.tagCH = tagCH;
    }

    public String getTagEN() {
        return tagEN;
    }

    public void setTagEN(String tagEN) {
        this.tagEN = tagEN;
    }

    public String getCategoryCH() {
        return categoryCH;
    }

    public void setCategoryCH(String categoryCH) {
        this.categoryCH = categoryCH;
    }

    public String getCategoryEN() {
        return categoryEN;
    }

    public void setCategoryEN(String categoryEN) {
        this.categoryEN = categoryEN;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public void setEnd(boolean end) {
        isEnd = end;
    }

    @Override
    public String toString() {
        return "MeiJuSimple{" +
                "tid=" + tid +
                ", url='" + url + '\'' +
                ", updateStatus='" + updateStatus + '\'' +
                ", tagCH='" + tagCH + '\'' +
                ", tagEN='" + tagEN + '\'' +
                ", categoryCH='" + categoryCH + '\'' +
                ", categoryEN='" + categoryEN + '\'' +
                ", isEnd=" + isEnd +
                '}';
    }
}
