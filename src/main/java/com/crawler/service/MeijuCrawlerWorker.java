package com.crawler.service;

import com.crawler.meta.DownLinkInfo;
import com.crawler.meta.MeiJu;
import com.crawler.meta.MeiJuSimple;
import com.crawler.meta.Movie;
import com.crawler.util.HttpClientUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.qiniu.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by guoyanlei
 * Date：2017/10/22
 * Description：
 */
public class MeijuCrawlerWorker {


    public final static String MEIJU_URL = "http://www.msj1.com/c/qhkh/page/";

    public final static String MEIJU_DETAIL_URL = "http://www.msj1.com/archives/5461.html";

    public static void main(String[] args) {

        MeijuCrawlerWorker crawlerWorker = new MeijuCrawlerWorker();

        String content = crawlerWorker.getResponseContent(MEIJU_URL + "11");
        List<MeiJuSimple> meiJuSimples = crawlerWorker.parseMeijuList(content);
        meiJuSimples.forEach(System.out::println);

//        String meiju = crawlerWorker.getResponseContent(MEIJU_DETAIL_URL);
//        crawlerWorker.parseMeiju(meiju);
    }

    public String getResponseContent(String url) {

        return HttpClientUtil.getInstance().sendHttpGet(url);
    }

    public List<MeiJuSimple> parseMeijuList(String content) {

        List<MeiJuSimple> meiJuSimples = Lists.newArrayList();

        Pattern pattern = Pattern.compile("<div class=\"art_img_box clearfix\" style=\"padding:15px;\">(.+?)<div class=\"art_info_bottom\" style=\"width:685px\">(.+?)</div>", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(content);

        while (matcher.find()) {
            MeiJuSimple meiJuSimple = new MeiJuSimple();
            String content1 = matcher.group(1);
            String content2 = matcher.group(2);
            String turl = "http://www.msj1.com/archives/{%tid}.html";
            String regx = "com/archives/([1-9]\\d{1,19})\\.html\" title=";
            Pattern p = Pattern.compile(regx, Pattern.DOTALL);
            Matcher m = p.matcher(content1);
            if(m.find()){
                String tid = m.group(1);
                meiJuSimple.setTid(Integer.parseInt(tid));
                //System.out.println(turl.replace("{%tid}", tid));
                meiJuSimple.setUrl(turl.replace("{%tid}", tid));
            }
            p = Pattern.compile("<em>\\[(.+?)\\]</em>", Pattern.DOTALL);
            m = p.matcher(content1);
            if(m.find()){
                //System.out.println(m.group(1).trim());
                meiJuSimple.setUpdateStatus(m.group(1).trim());
                meiJuSimple.setEnd(m.group(1).trim().contains("完结"));
            }

            Pattern p2 = Pattern.compile("栏目：<a href=\"http://www.msj1.com/c/(.+?)\" rel=\"category tag\">(.+?)</a></span>", Pattern.DOTALL);
            Matcher m2 = p2.matcher(content2);
            if(m2.find()){
                //System.out.println(m2.group(1));
                //System.out.println(m2.group(2));
                meiJuSimple.setCategoryEN(m2.group(1));
                meiJuSimple.setCategoryCH(m2.group(2));
            }

            Pattern p3 = Pattern.compile("<span>标签(.+?)</span>", Pattern.DOTALL);
            Matcher m3 = p3.matcher(content2);
            if(m3.find()){
                String tags =  m3.group(1);
                Pattern p3_1 = Pattern.compile("<a href=\"(.+?)\" rel=\"tag\">(.+?)</a>", Pattern.DOTALL);
                Matcher m3_1 = p3_1.matcher(tags);
                String tagEN = "";
                String tagCH = "";
                while (m3_1.find()) {
                    String t = m3_1.group(1);
                    tagEN += t.substring(t.lastIndexOf("/")+1, t.length()) + ",";
                    tagCH += m3_1.group(2) + ",";
                }
                //System.out.println(tagCH);
                //System.out.println(tagEN);
                meiJuSimple.setTagEN(tagEN.substring(0, tagEN.length()-1));
                meiJuSimple.setTagCH(tagCH.substring(0, tagCH.length()-1));
            }

            meiJuSimples.add(meiJuSimple);
        }

        return meiJuSimples;
    }

    public MeiJu parseMeiju(String content, MeiJuSimple meiJuSimple) {

        MeiJu meiJu = new MeiJu(meiJuSimple);
        //解析title
        String regx = "<h1 itemprop=\"name\">(.+?)</h1>";
        Pattern pattern = Pattern.compile(regx, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(content);
        while(matcher.find()){
            //System.out.println((matcher.group(1).trim()));
            meiJu.setTitle(matcher.group(1).trim());
        }

        //解析date
        regx = "<small>时间:</small>(.+?)\t\t\t\t<small>";
        pattern = Pattern.compile(regx, Pattern.DOTALL);
        matcher = pattern.matcher(content);
        while(matcher.find()){
            //System.out.println((matcher.group(1).trim()));
            meiJu.setDate(matcher.group(1).trim());
        }

        //解析image
        regx = "<p><img class=\"alignleft size-full(.+?)width=\"225\" height=\"300\" /></p>";
        pattern = Pattern.compile(regx, Pattern.DOTALL);
        matcher = pattern.matcher(content);
        while(matcher.find()){
            String img = matcher.group(1).trim();
            Pattern p1 = Pattern.compile("src=\"(.+?)\"", Pattern.DOTALL);
            Matcher m1 = p1.matcher(img);
            if (m1.find()) {
                meiJu.setImage(m1.group(1).trim());
                //System.out.println(m1.group(1).trim());
            }
        }

        //解析summery
        regx = "<div class=\"dbinfo\">(.+?)<h2 id=";
        pattern = Pattern.compile(regx, Pattern.DOTALL);
        matcher = pattern.matcher(content);
        while(matcher.find()){
            //System.out.println((matcher.group(1).replaceAll("<div>|</div>|<span class=\"pl\">|</span>","").trim()));
            meiJu.setSummary(matcher.group(1).replaceAll("<div>|</div>|<span class=\"pl\">|</span>","").trim());
        }

        //解析down_url
        List<List<DownLinkInfo>> lists = Lists.newArrayList();
        pattern = Pattern.compile("<h2 id=\"download\">(.+?)</table>", Pattern.DOTALL);
        matcher = pattern.matcher(content);
        while (matcher.find()) {
            String down = matcher.group();
            lists.add(this.parseDownLink(down));
        }
        meiJu.setDownLinks(lists);
        return meiJu;
    }

    public List<DownLinkInfo> parseDownLink(String content) {

        List<DownLinkInfo> linkInfos = Lists.newArrayList();
        Pattern pattern = Pattern.compile("<tr>(.+?)</tr>", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            String tr = matcher.group(1).trim();
            DownLinkInfo downLink = new DownLinkInfo();

            Pattern p = Pattern.compile("<a href=\"ed2k:(.+?)\" target=\"_blank\"", Pattern.DOTALL);
            Matcher m = p.matcher(tr);
            while (m.find()) {
                //System.out.println("ed2k:" + (m.group(1).trim()));
                downLink.setEd2k("ed2k:" + m.group(1).trim());
            }
            if (StringUtils.isNullOrEmpty(downLink.getEd2k())) {
                p = Pattern.compile("<td>(.+?) | 磁力</td>", Pattern.DOTALL);
                m = p.matcher(tr);
                if (m.find()) {
                    //System.out.println((m.group(1).trim()));
                    downLink.setTitle(m.group(1).trim());
                }
            }

            p = Pattern.compile("target=\"_blank\">(.+?)</a> \\| <a ", Pattern.DOTALL);
            m = p.matcher(tr);
            while (m.find()) {
                //System.out.println(m.group(1).trim());
                downLink.setTitle(m.group(1).trim());
            }
            if (StringUtils.isNullOrEmpty(downLink.getTitle())) {
                p = Pattern.compile("target=\"_blank\">(.+?)</a></td>", Pattern.DOTALL);
                m = p.matcher(tr);
                while (m.find()) {
                    //System.out.println(m.group(1).trim());
                    downLink.setTitle(m.group(1).trim());
                }
            }
            if (StringUtils.isNullOrEmpty(downLink.getTitle())) {
                p = Pattern.compile(" target=\"_blank\" rel=\"noopener noreferrer\">(.+?)</a> \\| <a ", Pattern.DOTALL);
                m = p.matcher(tr);
                while (m.find()) {
                    //System.out.println(m.group(1).trim());
                    downLink.setTitle(m.group(1).trim());
                }
            }

            p = Pattern.compile("<a href=\"magnet:(.+?)\">磁力</a>", Pattern.DOTALL);
            m = p.matcher(tr);
            while (m.find()) {
                //System.out.println("magnet:" + m.group(1).trim().replaceAll(" target=\"_blank", ""));
                downLink.setMagnet("magnet:" + m.group(1).trim().replaceAll(" target=\"_blank", ""));
            }

            p = Pattern.compile("<a href=\"http(.+?)\" target=\"_blank\" rel=\"noopener noreferrer\">网盘</a>", Pattern.DOTALL);
            m = p.matcher(tr);
            while (m.find()) {
                //System.out.println("http" + m.group(1).trim());
                downLink.setWangpan("http" + m.group(1).trim());
            }

            p = Pattern.compile("<td class=\"right\">\\((.+?)\\)</td>", Pattern.DOTALL);
            m = p.matcher(tr);
            while (m.find()) {
                //System.out.println(m.group(1).trim());
                downLink.setSize(m.group(1).trim());
            }

            linkInfos.add(downLink);
        }
        return linkInfos;
    }

}
