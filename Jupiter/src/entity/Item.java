package entity;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Item {
	private String itemId;
	private String name;
	private double rating;
	private String address;
	private Set<String> categories;
	private String imageUrl;
	private String url;
	private double distance;
	
	// constructor
	private Item(ItemBuilder builder) {
		this.itemId = builder.itemId;
		this.name = builder.name;
		this.rating = builder.rating;
		this.address = builder.address;
		this.categories = builder.categories;
		this.imageUrl = builder.imageUrl;
		this.url = builder.url;
		this.distance = builder.distance;
	}
	// getter - 读取
	// Note: 必要，不然private没法读取
	public String getItemId() {
		return itemId;
	}
	public String getName() {
		return name;
	}
	public double getRating() {
		return rating;
	}
	public String getAddress() {
		return address;
	}
	public Set<String> getCategories() {
		return categories;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public String getUrl() {
		return url;
	}
	public double getDistance() {
		return distance;
	}
	
	// Note: 把item对象转换成JSONObject给前端显示
	public JSONObject toJSONObject() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("item_id", itemId);
			obj.put("name", name);
			obj.put("rating", rating);
			obj.put("address", address);
			obj.put("categories", new JSONArray(categories));
			obj.put("image_url", imageUrl);
			obj.put("url", url);
			obj.put("distance", distance);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}

	public static class ItemBuilder {
		private String itemId;
		private String name;
		private double rating;
		private String address;
		private Set<String> categories;
		private String imageUrl;
		private String url;
		private double distance;
		
		// setter在ItemBuilder Class里是必要的，不然没法实例化Item's Object
		public void setItemId(String itemId) {
			this.itemId = itemId;
		}
		public void setName(String name) {
			this.name = name;
		}
		public void setRating(double rating) {
			this.rating = rating;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		public void setCategories(Set<String> categories) {
			this.categories = categories;
		}
		public void setImageUrl(String imageUrl) {
			this.imageUrl = imageUrl;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public void setDistance(double distance) {
			this.distance = distance;
		}
		
		public Item build() {
			Item item = new Item(this);
			return item;
			// return new Item(this);
			// this: 当前ItemBuilder所对应的对象
		}
	}
	
//	public static void main(String[] args) {
//		ItemBuilder builder = new ItemBuilder();
//		Item item = builder.build();
//	}
	
	// setter - 更新
	// Note: 不一定需要，取决于这个变量被创建(初始化)出来后还需不需要更改，
	// Note: 这里因为我们的数据都是从TicketMaster读取到的，我们不需要修改它，所以不需要setter
//	public void setItemId(String itemId) {
//		this.itemId = itemId;
//	}
//	public void setName(String name) {
//		this.name = name;
//	}
//	public void setRating(double rating) {
//		this.rating = rating;
//	}
//	public void setAddress(String address) {
//		this.address = address;
//	}
//	public void setCategories(Set<String> categories) {
//		this.categories = categories;
//	}
//	public void setImageUrl(String imageUrl) {
//		this.imageUrl = imageUrl;
//	}
//	public void setUrl(String url) {
//		this.url = url;
//	}
//	public void setDistance(double distance) {
//		this.distance = distance;
//	}
	
	
	
}
