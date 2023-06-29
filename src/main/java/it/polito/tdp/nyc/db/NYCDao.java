package it.polito.tdp.nyc.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import it.polito.tdp.nyc.model.Hotspot;

public class NYCDao {
	
	public List<Hotspot> getAllHotspot(){
		String sql = "SELECT * FROM nyc_wifi_hotspot_locations";
		List<Hotspot> result = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Hotspot(res.getInt("OBJECTID"), res.getString("Borough"),
						res.getString("Type"), res.getString("Provider"), res.getString("Name"),
						res.getString("Location"),res.getDouble("Latitude"),res.getDouble("Longitude"),
						res.getString("Location_T"),res.getString("City"),res.getString("SSID"),
						res.getString("SourceID"),res.getInt("BoroCode"),res.getString("BoroName"),
						res.getString("NTACode"), res.getString("NTAName"), res.getInt("Postcode")));
			}
			
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}

		return result;
	}
	
	
	
	
	public List<String> getAllBorhi(){
		
		String sql = " select distinct hl.Borough "
				+ "from  nyc_wifi_hotspot_locations hl "
				+ "order by hl.`Borough` asc ";
		
		
		List<String> result = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(res.getString("hl.Borough"));
			}
			
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}

		return result;
	}
	
	
	// per vertici 
public List<String> getVertici(String n){
		
		String sql = " select distinct hl.`NTAName` "
				+ "from  nyc_wifi_hotspot_locations hl "
				+ "where hl.`Borough`= ?  "; 
		
		
		List<String> result = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, n);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(res.getString("hl.NTAName"));
			}
			
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}

		return result;
	}

public List<String> getAllSSDI(String ntaName) {
	
//	String sql = "select distinct hl.`SSID` "
//			+ "from  nyc_wifi_hotspot_locations hl "
//			+ "where hl.`NTAName`= ? "; 

	String sql ="select l.SSID "
			+ "from nyc_wifi_hotspot_locations l  "
			+ "where l.`NTAName`=? "
			+ "group  by l.SSID " ;
	
	List<String> result = new ArrayList<>();
	try {
		Connection conn = DBConnect.getConnection();
		PreparedStatement st = conn.prepareStatement(sql);
		st.setString(1,ntaName);
		
		ResultSet res = st.executeQuery();

		while (res.next()) {
			result.add(res.getString("l.SSID"));
		}
		
		conn.close();
	} catch (SQLException e) {
		e.printStackTrace();
		throw new RuntimeException("SQL Error");
	}

	return result;
}
}
