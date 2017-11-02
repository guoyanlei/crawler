package com.crawler.service;

import com.crawler.dao.MovieDao;
import com.crawler.meta.Movie;
import com.crawler.util.QiniuUtil;
import com.google.common.collect.Lists;
import com.qiniu.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by guoyanlei
 * Date：2017/10/26
 * Description：
 */
public class MovieMain {

    public final static String MOVIE_LIST_PREFIX = "http://www.mxroom.com/forum-36-";
    public final static String MOVIE_LIST_END = ".html";
    public final static int LENGTH = 10;

    public static void main(String[] args) throws InterruptedException {

        CrawlerWorker crawler = new CrawlerWorker();
        MovieDao movieDao = new MovieDao();

        QiniuUtil qiniuUtil = QiniuUtil.getInstance();

        for (int i = 1; i <= LENGTH; i++) {
            System.out.println("page -----------: " + i);
            Map<Integer, String> maps = crawler.parseMovieList(crawler.getResponseContent(MOVIE_LIST_PREFIX + i + MOVIE_LIST_END));

            for (Integer key : maps.keySet()) {
                System.out.println(maps.get(key));
                if (movieDao.getByTid(key) == 0) {
                    Movie movie = crawler.parseMovie(crawler.getResponseContent(maps.get(key)));
                    movie.setTid(key);
                    if (!StringUtils.isNullOrEmpty(movie.getPoster())) {
                        String qiniuUrl = qiniuUtil.storeQiniuAndGetUrl("movie", movie.getPoster());
                        System.out.println(qiniuUrl);
                        movie.setPoster(qiniuUrl);
                    }
                    List<String> newScrenshot = Lists.newArrayList();
                    List<String> screenshot = movie.getScreenshot();
                    for (String s : screenshot) {
                        String qiniuUrl = qiniuUtil.storeQiniuAndGetUrl("movie/screenshot", s);
                        System.out.println(qiniuUrl);
                        newScrenshot.add(qiniuUrl);
                    }
                    movie.setScreenshot(newScrenshot);
                    movieDao.insert(movie);
                }
                Thread.sleep(500);
            }
        }

    }
}
