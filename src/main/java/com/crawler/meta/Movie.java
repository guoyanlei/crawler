package com.crawler.meta;

import java.util.List;
import java.util.Map;

/**
 * Created by guoyanlei
 * Date：2017/10/23
 * Description：
 */
public class Movie {

    private Long id;
    private String title;
    private String poster;      //海报
    private String name;
    private String transName;
    private Integer year;
    private String location;
    private String type;
    private String language;
    private String releaseTime;
    private Integer lengthMins;
    private String summary;
    private List<String> screenshot;
    private Map<String, String> ed2kDownLink;
    private String baiduLink;
    private String baiduLinkPwd;
    private Integer tid;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTransName() {
        return transName;
    }

    public void setTransName(String transName) {
        this.transName = transName;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }

    public Integer getLengthMins() {
        return lengthMins;
    }

    public void setLengthMins(Integer lengthMins) {
        this.lengthMins = lengthMins;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<String> getScreenshot() {
        return screenshot;
    }

    public void setScreenshot(List<String> screenshot) {
        this.screenshot = screenshot;
    }

    public Map<String, String> getEd2kDownLink() {
        return ed2kDownLink;
    }

    public void setEd2kDownLink(Map<String, String> ed2kDownLink) {
        this.ed2kDownLink = ed2kDownLink;
    }

    public String getBaiduLink() {
        return baiduLink;
    }

    public void setBaiduLink(String baiduLink) {
        this.baiduLink = baiduLink;
    }

    public String getBaiduLinkPwd() {
        return baiduLinkPwd;
    }

    public void setBaiduLinkPwd(String baiduLinkPwd) {
        this.baiduLinkPwd = baiduLinkPwd;
    }

    public Integer getTid() {
        return tid;
    }

    public void setTid(Integer tid) {
        this.tid = tid;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "  title='" + title + '\'' +
                ",\n poster='" + poster + '\'' +
                ",\n name='" + name + '\'' +
                ",\n transName='" + transName + '\'' +
                ",\n year=" + year +
                ",\n location='" + location + '\'' +
                ",\n type='" + type + '\'' +
                ",\n language='" + language + '\'' +
                ",\n releaseTime='" + releaseTime + '\'' +
                ",\n lengthMins=" + lengthMins +
                ",\n summary='" + summary + '\'' +
                ",\n screenshot=" + screenshot +
                ",\n ed2kDownLink='" + ed2kDownLink + '\'' +
                ",\n baiduLink='" + baiduLink + '\'' +
                ",\n baiduLinkPwd='" + baiduLinkPwd + '\'' +
                ",\n tid='" + tid + '\'' +
                '}';
    }
}
