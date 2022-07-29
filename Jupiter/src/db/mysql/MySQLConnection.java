package db.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import java.sql.Connection;
import java.util.List;
import java.util.Set;

import db.DBConnection;
import entity.Item;
import entity.Item.ItemBuilder;
import external.TicketMasterClient;

public class MySQLConnection implements DBConnection {

	private Connection conn;

	public MySQLConnection() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").getConstructor().newInstance();
			conn = DriverManager.getConnection(MySQLDBUtil.URL);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		if (conn != null) {
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void setFavoriteItems(String userId, List<String> itemIds) {
		if (conn == null) {
			System.err.println("DB connection failed");
			return;
		}

		try {
			String sql = "INSERT IGNORE INTO history(user_id, item_id) VALUES (?, ?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, userId);			// 1 userId can point to multiple itemId
			for (String itemId : itemIds) {		// for loop for each itemId
				ps.setString(2, itemId);
				ps.execute();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void unsetFavoriteItems(String userId, List<String> itemIds) {
		if (conn == null) {
			System.err.println("DB connection failed");
			return;
		}

		try {		   // DELETE-删除		  WHERE-从哪删除 (sql keyword)
			String sql = "DELETE FROM history WHERE user_id = ? AND item_id = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, userId);
			for (String itemId : itemIds) {
				ps.setString(2, itemId);
				ps.execute();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Set<String> getFavoriteItemIds(String userId) {
		if (conn == null) {
			return new HashSet<>();
		}

		Set<String> favoriteItems = new HashSet<>();

		try {
			String sql = "SELECT  item_id FROM history WHERE user_id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, userId);

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				String itemId = rs.getString("item_id");
				favoriteItems.add(itemId);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return favoriteItems;

	}

	@Override
	public Set<Item> getFavoriteItems(String userId) {
		if (conn == null) {
			return new HashSet<>();
		}

		Set<Item> favoriteItems = new HashSet<>();
		Set<String> itemIds = getFavoriteItemIds(userId);

		try {			// SELECT *-(所有的)
			String sql = "SELECT * FROM items WHERE item_id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			
			for (String itemId : itemIds) {
				stmt.setString(1, itemId);

				ResultSet rs = stmt.executeQuery();

				ItemBuilder builder = new ItemBuilder();

				while (rs.next()) {
					builder.setItemId(rs.getString("item_id"));
					builder.setName(rs.getString("name"));
					builder.setAddress(rs.getString("address"));
					builder.setImageUrl(rs.getString("image_url"));
					builder.setUrl(rs.getString("url"));
					builder.setCategories(getCategories(itemId));
					builder.setDistance(rs.getDouble("distance"));
					builder.setRating(rs.getDouble("rating"));

					favoriteItems.add(builder.build());
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return favoriteItems;

	}


	@Override
	public Set<String> getCategories(String itemId) {
		if (conn == null) {
			return null;
		}
		Set<String> categories = new HashSet<>();
		try {
			String sql = "SELECT category from categories WHERE item_id = ? ";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, itemId);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				String category = rs.getString("category");
				categories.add(category);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return categories;

	}

	@Override
	public List<Item> searchItems(double lat, double lon, String term) {
		TicketMasterClient ticketMasterClient = new TicketMasterClient();
		List<Item> items = ticketMasterClient.search(lat, lon, term);

		for (Item item : items) { // iterative item, then stores the item into DB
			saveItem(item);
		}

		return items; // 存在DB里的同时，也把数据返回给前端
	}

	@Override
	public void saveItem(Item item) {
		// TODO Auto-generated method stub
		if (conn == null) {
			System.err.println("DB connection failed");
			return;
		}
		try {
			String sql = "INSERT IGNORE INTO items VALUES (?, ?, ?, ?, ?, ?, ?)"; // IGNORE -
																					// 当insert多行时如果其中一行失败或有错误，就忽略它
			PreparedStatement ps = conn.prepareStatement(sql); // then 继续将剩下的insert指令做完，不会因为中间的一行错误而停止
			ps.setString(1, item.getItemId()); // insert重复value也算失败/错误
			ps.setString(2, item.getName());
			ps.setDouble(3, item.getRating());
			ps.setString(4, item.getAddress());
			ps.setString(5, item.getImageUrl());
			ps.setString(6, item.getUrl());
			ps.setDouble(7, item.getDistance());
			ps.execute();

			sql = "INSERT IGNORE INTO categories VALUES(?, ?)";
			ps = conn.prepareStatement(sql);
			// itemid 123
			ps.setString(1, item.getItemId());
			// pop, music
			for (String category : item.getCategories()) { // for loop两次，分别将pop和music加进category
				ps.setString(2, category);
				ps.execute();
			}
			// after for loop:
			// itemid, category
			// 123 , pop
			// 123 , music

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getFullname(String userId) {	// get user name
		if (conn == null) {
			return "";
		}		
		String name = "";
		try {
			String sql = "SELECT first_name, last_name FROM users WHERE user_id = ? ";
			PreparedStatement statement = conn.prepareStatement(sql);
			// set ? to userId， 1 means first "?"
			statement.setString(1, userId);
			ResultSet rs = statement.executeQuery();
			// check if there is any data, rs.next() = false if there is no record
			while (rs.next()) {
				name = rs.getString("first_name") + " " + rs.getString("last_name");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return name;
	}


	@Override
	public boolean verifyLogin(String userId, String password) {	// check for userId and password
		if (conn == null) {
			return false;
		}
		try {
			// 提供模板， 下面setString是替换
			String sql = "SELECT user_id FROM users WHERE user_id = ? AND password = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			// set first ? to userId
			stmt.setString(1, userId);
			// set second ? to password
			stmt.setString(2, password);
			ResultSet rs = stmt.executeQuery();
			// if exist a record with same userId and password, return true
			if (rs.next()) {		// 本来光标是第一行之前，next就是去找下一行
				return true;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return false;
	}
	
	@Override
	public boolean registerUser(String userId, String password, String firstname, String lastname) {
		if (conn == null) {
			System.err.println("DB connection failed");
			return false;
		}

		try {
			String sql = "INSERT IGNORE INTO users VALUES (?, ?, ?, ?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, userId);		// first ? --> userId
			ps.setString(2, password);		// second ? --> password
			ps.setString(3, firstname);		// third ? --> firstname
			ps.setString(4, lastname);		// last ? --> lastname
			
			return ps.executeUpdate() == 1;		// insert时检查有没有插入数据
		} catch (Exception e) {					// 如果是1，表示执行成功
			e.printStackTrace();				// 如果是0，表示插入没成功，user已经存在
		}
		return false;	
	}

}
