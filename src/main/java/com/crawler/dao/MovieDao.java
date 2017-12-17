package com.crawler.dao;

import com.alibaba.fastjson.JSON;
import com.crawler.meta.Movie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MovieDao {

	public int insert(Movie movie) {
		Connection conn = null;
		PreparedStatement pst = null;
		JDBCBean jdbc = new JDBCBean();
		conn = jdbc.getConn();
		try {
			pst = conn.prepareStatement("insert into `movie` value(" +
					"NULL,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

			pst.setString(1,movie.getTitle());
			pst.setString(2,movie.getPoster());
			pst.setString(3,movie.getName());
			pst.setString(4,movie.getTransName());
			pst.setInt(5,movie.getYear() == null ? 0 : movie.getYear());
			pst.setString(6,movie.getLocation());
			pst.setString(7,movie.getType());
			pst.setString(8,movie.getLanguage());
			pst.setString(9,movie.getReleaseTime());
			pst.setInt(10,movie.getLengthMins() == null ? 0 : movie.getLengthMins());
			pst.setString(11,movie.getSummary());
			pst.setString(12, JSON.toJSONString(movie.getScreenshot()));
			pst.setString(13,JSON.toJSONString(movie.getEd2kDownLink()));
			pst.setString(14,movie.getBaiduLink());
			pst.setString(15,movie.getBaiduLinkPwd());
			pst.setLong(16,movie.getTid());
			pst.setInt(17,0);
			pst.setInt(18,0);
			pst.setLong(19,System.currentTimeMillis());
			pst.setLong(20,System.currentTimeMillis());

			int i = pst.executeUpdate();
			return i;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jdbc.closeAll(conn, pst, null);
		}
		return 0;
	}

	public int getByTid(int tid) {

		int id = 0;
		Connection conn = null;
		PreparedStatement pst = null;
		JDBCBean jdbc = new JDBCBean();
		conn = jdbc.getConn();
		ResultSet rs = null;
		try {
			pst = conn
					.prepareStatement("SELECT id FROM `movie` where tid = ?");
			pst.setInt(1,tid);
			rs = pst.executeQuery();
			if (rs.next()) {
				id = rs.getInt("id");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jdbc.closeAll(conn, pst, null);
		}
		return id;
	}
}
