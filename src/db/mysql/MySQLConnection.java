package db.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DBConnection;
import entity.AdItem;
import entity.AdItem.AdItemBuilder;

public class MySQLConnection implements DBConnection {

	private Connection conn;

	public MySQLConnection() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").getConstructor().newInstance();
			conn = DriverManager.getConnection(MySQLDBUtil.URL); // Attempts to establish a connection to DB
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub
		if (conn != null) {
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		}
	}

	@Override
	public List<AdItem> searchAdItems() {
		// TODO Auto-generated method stub
		if (conn == null) {
			System.err.println("DB connection failed!");
			return new ArrayList<>();
		}
		List<AdItem> adItems = new ArrayList<>();
		try {
			// Get all ads candidates
			String sql = "SELECT * FROM ad LEFT JOIN advertiser on ad.advertiser_id = advertiser.advertiser_id WHERE ad.bid > 0 AND advertiser.budget > 0";
			PreparedStatement stmt = conn.prepareStatement(sql);

			ResultSet rs = stmt.executeQuery();
			AdItemBuilder builder = new AdItemBuilder();

			while (rs.next()) {
				builder.setAd_id(rs.getInt("ad_id"));
				builder.setBid(rs.getDouble("bid"));
				builder.setImage_url(rs.getString("image_url"));
				builder.setAdvertiser_id(rs.getInt("advertiser_id"));
				builder.setAd_score(rs.getDouble("ad_score"));
				adItems.add(builder.build());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return adItems;
	}

	@Override
	public double getBudget(int advertiser_id) {
		// TODO Auto-generated method stub
		if (conn == null) {
			System.err.println("DB connection failed!");
		}
		double curBudget = -1;
		try {
			String sql = "SELECT * FROM advertiser WHERE advertiser_id = (?)";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, advertiser_id);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				curBudget = rs.getDouble("budget");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return curBudget;
	}

	@Override
	public double updateBudget(int advertiser_id, double budget) {
		// TODO Auto-generated method stub
		if (conn == null) {
			System.err.println("DB connection failed!");
		}
		try {
			String sql = "UPDATE advertiser SET budget=(?) WHERE advertiser_id=(?)";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setDouble(1, budget);
			stmt.setInt(2, advertiser_id);
			int affectedRows = stmt.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("Updating advertiser budget failed, no rows affected.");
			} else {
				return getBudget(advertiser_id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("update budget done");
		return -1;
	}

	@Override
	public double getBid(int ad_id) {
		// TODO Auto-generated method stub
		if (conn == null) {
			System.err.println("DB connection failed!");
		}
		double curBid = -1;
		try {
			String sql = "SELECT * FROM ad WHERE ad_id = (?)";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, ad_id);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				curBid = rs.getDouble("bid");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return curBid;
	}

	@Override
	public double updateBid(int ad_id, double bid) {
		// TODO Auto-generated method stub
		if (conn == null) {
			System.err.println("DB connection failed!");
		}
		try {
			String sql = "UPDATE ad SET bid=(?) WHERE ad_id=(?)";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setDouble(1, bid);
			stmt.setInt(2, ad_id);
			System.out.println(stmt.toString());
			int affectedRows = stmt.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("Updating ad's bid failed, no rows affected.");
			} else {
				System.out.println("affectedRows: " + affectedRows);
				return getBid(ad_id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("update bid done");
		return -1;
	}

	@Override
	/**
	 * Return advertiser_id if generated successfully
	 */
	public int createAdvertiser(String advertiser_name, double budget) {
		// TODO Auto-generated method stub
		if (conn == null) {
			System.err.println("DB connection failed!");
		}

		// INSERT INTO advertiser(name, budget) VALUES ('Fangyuan', 120);
		try {
			String sql = "INSERT INTO advertiser(name, budget) VALUES((?),(?))";
			PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, advertiser_name);
			stmt.setDouble(2, budget);
			System.out.println(stmt.toString());
			int affectedRows = stmt.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("Creating advertiser failed, no rows affected.");
			}
			try (ResultSet generatedKeys = stmt.getGeneratedKeys()) { // return the advertiser_id generated
				if (generatedKeys.next()) {
					return generatedKeys.getInt(1);
				} else {
					throw new SQLException("Creating advertiser failed, no ID obtained.");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Insert advertiser done");
		return -1;
	}

	@Override
	public long createAd(double bid, String image_url, int advertiser_id, double ad_score) {
		// TODO Auto-generated method stub
		if (conn == null) {
			System.err.println("DB connection failed!");
		}
		// INSERT INTO ad(bid, image_url, advertiser_id, ad_score) VALUES("2", "", 2,
		// 0.4);
		try {
			String sql = "INSERT INTO ad(bid, image_url, advertiser_id, ad_score) VALUES((?), (?), (?), (?));";
			PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setDouble(1, bid);
			stmt.setString(2, image_url);
			stmt.setInt(3, advertiser_id);
			stmt.setDouble(4, ad_score);
			System.out.println(stmt.toString());
			int affectedRows = stmt.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("Creating ad failed, no row is affected.");
			}
			try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					return generatedKeys.getLong(1);
				} else {
					throw new SQLException("Creating ad failed, no ID obtained.");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Insert ad done.");
		return -1;
	}

}
