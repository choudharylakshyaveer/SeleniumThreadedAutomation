package threadedAutomation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;

public class ConnectionPoolCustom {
	{
		try {
			setUpPool();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// JDBC Driver Name & Database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String JDBC_DB_URL = "jdbc:mysql://localhost:3306/test?useSSL=true";

	// JDBC Database Credentials
	static final String JDBC_USER = "root";
	static final String JDBC_PASS = "mysql";

	private static GenericObjectPool gPool = null;
	private static DataSource dataSource=null;

	@SuppressWarnings("unused")
	public DataSource setUpPool() throws Exception {
		Class.forName(JDBC_DRIVER);

		// Creates an Instance of GenericObjectPool That Holds Our Pool of Connections
		// Object!
		gPool = new GenericObjectPool();
		gPool.setMaxActive(20);

		// Creates a ConnectionFactory Object Which Will Be Use by the Pool to Create
		// the Connection Object!
		ConnectionFactory cf = new DriverManagerConnectionFactory(JDBC_DB_URL, JDBC_USER, JDBC_PASS);

		// Creates a PoolableConnectionFactory That Will Wraps the Connection Object
		// Created by the ConnectionFactory to Add Object Pooling Functionality!
		PoolableConnectionFactory pcf = new PoolableConnectionFactory(cf, gPool, null, null, false, true);
		dataSource = new PoolingDataSource(gPool);
		return dataSource;
	}

	public GenericObjectPool getConnectionPool() {
		return gPool;
	}

	public static Connection getConnectionFromPool() throws SQLException {
		return dataSource.getConnection();
	}
	
	// This Method Is Used To Print The Connection Pool Status
	void printDbStatus() {
		System.out.println("Max.: " + getConnectionPool().getMaxActive() + "; Active: "
				+ getConnectionPool().getNumActive() + "; Idle: " + getConnectionPool().getNumIdle());
	}
	
	public void closeResources(ResultSet rs, Connection con, PreparedStatement pstmt) {
		if(rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(con !=null) {
			
			try {
				if(con.getAutoCommit()==false)
				{
					con.setAutoCommit(true);
				}
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
