package com.crawler.dao;

import java.sql.*;

public class JDBCBean {

	private String driver = "com.mysql.jdbc.Driver";
	private String conn = "jdbc:MySQL://localhost:3306/movie";
	private String DBname ="root";
	private String DBpwd = "root";
	private Connection connection = null;
	private Statement stmt = null;
	private ResultSet rs = null;
	
	/*---------------------第一种方式-----------------*/
	//加载驱动程序
	public JDBCBean() {
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	//建立于数据库的连接
	public Connection getConnection() {
		try {
			connection = DriverManager.getConnection(conn,DBname,DBpwd);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}
	//创建语句对象
	public Statement createStatement() {
		try {
			stmt = getConnection().createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return stmt;
	}
	//执行查询操作
	public ResultSet executeQuery(String sql) {
		try {
			rs = createStatement().executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	//执行更新操作
	public int executeUpdate(String sql) {
		int result = 0;
		try {
			result = createStatement().executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	//执行关闭操作
	public void close() {
		try{
			if(rs!=null)
				rs.close();
			if(stmt!=null)
				stmt.close();
			if(connection!=null)
				connection.close();
		}catch(SQLException e)
		{
			e.printStackTrace();
		}	
	}
	/*---------------------第二种方式-----------------*/
	/*获取数据库连接的方法*/
	public Connection getConn(){
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Connection con=null;
		try {
			con = DriverManager.getConnection(conn,DBname,DBpwd);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;
	}
	/*关闭数据库资源的方法（关闭的顺序不能改变）*/
	public void closeAll(Connection conn,PreparedStatement pstmt,ResultSet rs){
		if (rs!=null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (pstmt!=null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (conn!=null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}


