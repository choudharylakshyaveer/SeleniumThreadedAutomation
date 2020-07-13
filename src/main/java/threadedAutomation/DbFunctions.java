package threadedAutomation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DbFunctions {

	public Long insertListInDb(String sql, List<Object> list)
	{
		ConnectionPoolCustom jdbcObj = new ConnectionPoolCustom();
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		Long id=null;
		try
		{
			jdbcObj.printDbStatus();
			con = jdbcObj.getConnectionFromPool();
			pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			int count = 1;
			for(Object columnValue:list) {
				pstmt.setObject(count, columnValue);
				count++;
			}
			pstmt.executeUpdate();
			rs = pstmt.getGeneratedKeys();
			if(rs !=null && rs.next())
			{
				id=rs.getLong(1);
				System.out.println();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			jdbcObj.closeResources(rs, con, pstmt);
			jdbcObj.printDbStatus();
		}
		return id;
		
	}
	
	public List<String> getUrlListFromTbl(String columnName, String tableName) {
		List<String> urls = new ArrayList<String>();
		ConnectionPoolCustom jdbcObj = new ConnectionPoolCustom();
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement st = null;
		Long id=null;
		try {
			con = jdbcObj.getConnectionFromPool();
			String sql = "SELECT "+columnName+" FROM "+tableName+" WHERE scanned=0";
			st = con.prepareStatement(sql);
			rs = st.executeQuery(sql);
			while(rs.next())
			{
				urls.add(rs.getString(1));
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			jdbcObj.closeResources(rs, con, st);
		}
		return urls;
	}

	public void insertListInDbInRows(String sql, List<Object> urls, String masterLink) {

		ConnectionPoolCustom jdbcObj = new ConnectionPoolCustom();
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement st = null;
		Long id=null;
		for(Object url:urls)
		{
			try {
				System.out.println(sql);
				con = jdbcObj.getConnectionFromPool();
				st = con.prepareStatement(sql);
				st.setString(1, String.valueOf(url));
				st.setString(2, masterLink);
				System.out.println(st.toString());
				try {
				st.executeUpdate();
				}catch (Exception e) {
					e.printStackTrace();
				}
				
			}catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				jdbcObj.closeResources(rs, con, st);
			}
		}
	}
	
	
	
}
