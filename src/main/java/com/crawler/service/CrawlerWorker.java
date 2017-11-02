package com.crawler.service;

import com.crawler.dao.MovieDao;
import com.crawler.meta.Movie;
import com.crawler.util.HttpClientUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by guoyanlei
 * Date：2017/10/22
 * Description：
 */
public class CrawlerWorker {


//    public final static String MOVIE_URL = "http://www.mxroom.com/thread-132542-1-1.html";
//    public final static String MOVIE_URL = "http://www.mxroom.com/thread-131994-1-1.html";
    public final static String MOVIE_URL = "http://www.mxroom.com/thread-132691-1-1.html";

    public String getResponseContent(String url) {

        return HttpClientUtil.getInstance().sendHttpGet(url);
    }

    public Map<Integer, String> parseMovieList(String content) {

        String turl = "http://www.mxroom.com/thread-{%tid}-1-1.html";

        String regx = "<tbody id=\"normalthread_([1-9]\\d{4,19})\">";

        Pattern pattern = Pattern.compile(regx, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(content);

        Map<Integer, String> maps = Maps.newHashMap();
        while(matcher.find()){
            String tid = matcher.group(1);
            maps.put(Integer.parseInt(tid), turl.replace("{%tid}", tid));
        }
        return maps;
    }

    public Movie parseMovie(String content) {

        Movie movie = new Movie();

        //解析title
        String regx = "<span id=\"thread_subject\">(.+?)</span>";
        Pattern pattern = Pattern.compile(regx, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(content);
        while(matcher.find()){
            movie.setTitle(matcher.group(1).trim());
        }

        //解析海报和截图
        pattern = Pattern.compile("<img id=(.+?)border=\"0\"", Pattern.DOTALL);
        matcher = pattern.matcher(content);
        int count = 1;
        List<String> screenShot = Lists.newArrayList();
        while (matcher.find()) {
            String tmp = matcher.group(1);
            if (count == 1) {
                movie.setPoster(parseSrcUrl(tmp).trim());
            } else {
                screenShot.add(parseSrcUrl(tmp));
            }
            count++;
        }
        movie.setScreenshot(screenShot);

        //解析下载链接
        pattern = Pattern.compile("<a id=\"ed2k_(.+?)<script", Pattern.DOTALL);
        matcher = pattern.matcher(content);
        Map<String, String> ed2kLinks = Maps.newHashMap();
        while (matcher.find()) {
            String tmp = matcher.group(1);
            Matcher m = Pattern.compile("target=\"_blank\">(.+?)</a>",Pattern.DOTALL).matcher(tmp);
            if (m.find()){
                ed2kLinks.put(parseHttpUrl(tmp), m.group(1).trim());
            }
        }
        movie.setEd2kDownLink(ed2kLinks);
        if (movie.getEd2kDownLink().size() <= 0) {
            matcher = Pattern.compile("复制链接到迅雷下载：<br />\n" +
                    "<br />\n" +
                    "(.+?)<br />", Pattern.DOTALL).matcher(content);
            if (matcher.find()) {
                ed2kLinks.put(matcher.group(1), movie.getTitle());
            }
        }
        movie.setEd2kDownLink(ed2kLinks);
        if (movie.getEd2kDownLink().size() <= 0) {
            matcher = Pattern.compile("thunder://(.+?)<br />", Pattern.DOTALL).matcher(content);
            if (matcher.find()) {
                ed2kLinks.put("thunder://" + matcher.group(1), movie.getTitle());
            }
        }

        //解析百度云下载
        pattern = Pattern.compile("<a href=\"http://pan.baidu.com(.+?)\" target=\"_blank\">", Pattern.DOTALL);
        matcher = pattern.matcher(content);
        while (matcher.find()) {
            String tmp = matcher.group(1);
            movie.setBaiduLink("http://pan.baidu.com" + tmp.trim());
        }
        if (movie.getBaiduLink() == null) {
            pattern = Pattern.compile("<a href=\"https://pan.baidu.com(.+?)\" target=\"_blank\">", Pattern.DOTALL);
            matcher = pattern.matcher(content);
            while (matcher.find()) {
                String tmp = matcher.group(1);
                movie.setBaiduLink("http://pan.baidu.com" + tmp.trim());
            }
        }
        if (movie.getBaiduLink() == null) {
            matcher = Pattern.compile("网盘链接：<a href=\"(.+?)\" target=\"_blank\">", Pattern.DOTALL).matcher(content);
            if (matcher.find()) {
                movie.setBaiduLink(matcher.group(1).trim());
            }
        }

        pattern = Pattern.compile("密码: (.+?)<br />", Pattern.DOTALL);
        matcher = pattern.matcher(content);
        while (matcher.find()) {
            String tmp = matcher.group(1);
            movie.setBaiduLinkPwd(tmp.trim().substring(0,4));
        }
        if (movie.getBaiduLinkPwd() == null) {
            matcher = Pattern.compile("密码：(.+?)<br />", Pattern.DOTALL).matcher(content);
            if (matcher.find()) {
                movie.setBaiduLinkPwd(matcher.group(1).replaceAll("</div>","").trim().substring(0,4));
            }
        }

        //解析译名
        matcher = Pattern.compile("译　　名　(.+?)<br />", Pattern.DOTALL).matcher(content);
        if (matcher.find()) {
            String tmp = matcher.group(1);
            movie.setTransName(tmp.replaceAll("</font>","").trim());
        }

        //解析片名
        matcher = Pattern.compile("片　　名　(.+?)<br />", Pattern.DOTALL).matcher(content);
        if (matcher.find()) {
            String tmp = matcher.group(1);
            movie.setName(tmp.replaceAll("</font>","").trim());
        }
        if (movie.getName() == null) {
            matcher = Pattern.compile("《(.+?)》", Pattern.DOTALL).matcher(movie.getTitle());
            if (matcher.find()) {
                movie.setName(matcher.group(1).trim().trim());
            }
        }
        if (movie.getTransName() == null && movie.getName() != null) {
            movie.setTransName(movie.getName().trim());
        }

        //解析年代
        matcher = Pattern.compile("年　　代　(.+?)<br />", Pattern.DOTALL).matcher(content);
        if (matcher.find()) {
            String tmp = matcher.group(1);
            movie.setYear(Integer.parseInt(tmp.replaceAll("</font>","").trim().substring(0, 4)));
        }

        //解析产地
        matcher = Pattern.compile("产　　地　(.+?)<br />", Pattern.DOTALL).matcher(content);
        if (matcher.find()) {
            String tmp = matcher.group(1);
            movie.setLocation(tmp.replaceAll("</font>","").trim());
        }
        if (movie.getLocation() == null) {
            matcher = Pattern.compile("制片国家/地区: (.+?)<br />", Pattern.DOTALL).matcher(content);
            if (matcher.find()) {
                movie.setLocation(matcher.group(1).trim());
            }
        }
        if (movie.getLocation() == null) {
            matcher = Pattern.compile("地　　区　(.+?) <br /", Pattern.DOTALL).matcher(content);
            if (matcher.find()) {
                movie.setLocation(matcher.group(1).trim());
            }
        }

        //解析类别
        matcher = Pattern.compile("类　　别　(.+?)<br />", Pattern.DOTALL).matcher(content);
        if (matcher.find()) {
            String tmp = matcher.group(1);
            movie.setType(tmp.replaceAll("</font>","").trim());
        }
        if (movie.getType() == null) {
            matcher = Pattern.compile("类型: (.+?)<br />", Pattern.DOTALL).matcher(content);
            if (matcher.find()) {
                movie.setType(matcher.group(1).trim());
            }
        }
        if (movie.getType() == null) {
            matcher = Pattern.compile("类　　型　(.+?) <br />", Pattern.DOTALL).matcher(content);
            if (matcher.find()) {
                movie.setType(matcher.group(1).trim());
            }
        }

        //解析语言
        matcher = Pattern.compile("语　　言　(.+?)<br />", Pattern.DOTALL).matcher(content);
        if (matcher.find()) {
            String tmp = matcher.group(1);
            movie.setLanguage(tmp.replaceAll("</font>","").trim());
        }
        if (movie.getLanguage()==null) {
            matcher = Pattern.compile("语言: (.+?)<br />", Pattern.DOTALL).matcher(content);
            if (matcher.find()) {
                movie.setLanguage(matcher.group(1).trim());
            }
        }

        //解析上映日期
        matcher = Pattern.compile("上映日期　(.+?)<br />", Pattern.DOTALL).matcher(content);
        if (matcher.find()) {
            String tmp = matcher.group(1);
            movie.setReleaseTime(tmp.replaceAll("</font>","").trim());
        }
        if (movie.getReleaseTime() == null) {
            matcher = Pattern.compile("上映日期: (.+?)<br />", Pattern.DOTALL).matcher(content);
            if (matcher.find()) {
                movie.setReleaseTime(matcher.group(1).trim());
            }
        }
        if (movie.getYear() == null && movie.getReleaseTime() != null) {
            if (movie.getReleaseTime().length() > 4) {
                movie.setYear(Integer.parseInt(movie.getReleaseTime().substring(0,4)));
            }
        }

        //解析片长
        matcher = Pattern.compile("片　　长　(.+?)分钟<br />", Pattern.DOTALL).matcher(content);
        if (matcher.find()) {
            String tmp = matcher.group(1);
            if (tmp!= null) {
                movie.setLengthMins(Integer.parseInt(tmp.replaceAll("</font>","").trim()));
            }
        }
        if (movie.getLengthMins() == null) {
            matcher = Pattern.compile("片长: (.+?)分钟<br />", Pattern.DOTALL).matcher(content);
            if (matcher.find()) {
                if (matcher.group(1)!= null) {
                    String tmp = matcher.group(1).trim();
                    if (tmp.length() > 3) {
                        movie.setLengthMins(Integer.parseInt(tmp.substring(0,3)));
                    } else {
                        movie.setLengthMins(Integer.parseInt(tmp));
                    }
                }
            }
        }
        if (movie.getLengthMins() == null) {
            matcher = Pattern.compile("片　　长　(.+?)分钟 <br />", Pattern.DOTALL).matcher(content);
            if (matcher.find()) {
                if (matcher.group(1)!= null) {
                    String tmp = matcher.group(1).trim();
                    if (tmp.length() > 3) {
                        movie.setLengthMins(Integer.parseInt(tmp.substring(0,3)));
                    } else {
                        movie.setLengthMins(Integer.parseInt(tmp));
                    }
                }
            }
        }

        //解析简介
        matcher = Pattern.compile("<table cellspacing=\"0\" class=\"t_table\" >(.+?)</td></tr></table></td></tr></table>", Pattern.DOTALL).matcher(content);
        if (matcher.find()) {
            String tmp = matcher.group(1).replaceAll("<br />", "\n");
            Pattern p = Pattern.compile("<([^>]*)>");
            Matcher m = p.matcher(tmp);
            StringBuffer sb = new StringBuffer();
            while (m.find()) {
                m.appendReplacement(sb, "");
            }
            m.appendTail(sb);
            movie.setSummary(sb.toString().replaceAll("&nbsp;", " ").trim());
        }

        return movie;

    }

    public String parseHttpUrl(String content) {

        String patternString = "href=\"(.+?)\" "; //href

        Pattern pattern = Pattern.compile(patternString,Pattern.DOTALL);
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

    public String parseSrcUrl(String content) {

        String patternString = "src=\"(.+?)\" "; //href

        Pattern pattern = Pattern.compile(patternString,Pattern.DOTALL);
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }
}
