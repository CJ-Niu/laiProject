package external;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import entity.Item;
import entity.Item.ItemBuilder;


public class TicketMasterClient {
	
	private static final String HOST = "https://app.ticketmaster.com";
	private static final String PATH = "/discovery/v2/events.json";
	private static final String DEFAULT_KEYWORD = "event";
	private static final int DEFAULT_RADIUS = 50;
	private static final String API_KEY = "7AcvGgjKNwpRftyYGDSxQWSu4vklwAZ4";
	
	public List<Item> search(double lat, double lon, String keyword) {
		// corner case
		if (keyword == null) {
			keyword = DEFAULT_KEYWORD;
		}
		
		try {
			keyword = URLEncoder.encode(keyword, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		String geoHash = GeoHash.encodeGeohash(lat, lon, 8);
		String query = String.format("apikey=%s&geoPoint=%s&keyword=%s&radius=%s", API_KEY, geoHash, keyword, DEFAULT_RADIUS);
		String url = HOST + PATH + "?" + query;
		// s把从BufferReader里面读取的数据一点点拼接起来
		StringBuilder responseBody = new StringBuilder();
		
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
			
			// send request
			connection.setRequestMethod("GET");		// Default is GET
			connection.connect();
			
			// receive response
			// get response's code
			int responseCode = connection.getResponseCode();
			if (responseCode != 200) {		// only when == 200 means success
				return new ArrayList<>();
			}
			// get response's body
			
			// InputStreamReader - 一个字一个字的读数据(正确性没问题，但是慢)
			// InputStreamReader reader = new InputStreamReader(connection.getInputStream());
			
			// BufferedReader - 一段一段的读数据(line - 默认8kb为一行)
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line = reader.readLine();		// String line; 
			while (line != null) {					// while ((line = reader.readLine()) != null) { 
				responseBody.append(line);			//		responseBody.append(line); 
				line = reader.readLine();			// }
			}
			reader.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// 把response body的内容，转换成了JSONObject - 此时应该返回三个key value pair (_links, _embedded, _page)
		// (key: _embedded, value: object)		因为ticket master的API就是这么设置的 （要看网站文档）
		try {
			JSONObject obj = new JSONObject(responseBody.toString());
			if (!obj.isNull("_embedded")) {
				JSONObject embedded = obj.getJSONObject("_embedded");
				return getItemList(embedded.getJSONArray("events"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return new ArrayList<>();
	}
	

	// Convert JSONArray to a list of item objects.
	private List<Item> getItemList(JSONArray events) throws JSONException {
		List<Item> itemList = new ArrayList<>();
		for (int i = 0; i < events.length(); ++i) {
			JSONObject event = events.getJSONObject(i);
			ItemBuilder builder = new ItemBuilder();
			
			if (!event.isNull("id")) {
				builder.setItemId(event.getString("id"));
			}
			if (!event.isNull("name")) {
				builder.setName(event.getString("name"));
			}
			if (!event.isNull("url")) {
				builder.setUrl(event.getString("url"));
			}
			if (!event.isNull("distance")) {
				builder.setDistance(event.getDouble("distance"));
			}

			builder.setAddress(getAddress(event));
			builder.setCategories(getCategories(event));
			builder.setImageUrl(getImageUrl(event));
			
			Item item = builder.build();
			itemList.add(item);
		}
		return itemList;
	}
	
	/**
	 * Helper methods
	 */
	private String getAddress(JSONObject event) throws JSONException {
		if (!event.isNull("_embedded")) {
			JSONObject embedded = event.getJSONObject("_embedded");
			if (!embedded.isNull("venues")) {
				JSONArray venues = embedded.getJSONArray("venues");
				for (int i = 0; i < venues.length(); ++i) {
					JSONObject venue = venues.getJSONObject(i);
					StringBuilder builder = new StringBuilder();
					if (!venue.isNull("address")) {
						JSONObject address = venue.getJSONObject("address");
						if (!address.isNull("line1")) {
							builder.append(address.getString("line1"));
						}
						
						if (!address.isNull("line2")) {
							builder.append(",");
							builder.append(address.getString("line2"));
						}
						
						if (!address.isNull("line3")) {
							builder.append(",");
							builder.append(address.getString("line3"));
						}
					}
					
					if (!venue.isNull("city")) {
						JSONObject city = venue.getJSONObject("city");
						builder.append(",");
						builder.append(city.getString("name"));
					}
					String result = builder.toString();
					if (!result.isEmpty()) {
						return result;
					}
				}
			}
		}
		
		return "";
	}
	
	private Set<String> getCategories(JSONObject event) throws JSONException {		
		Set<String> categories = new HashSet<>();
		if (!event.isNull("classifications")) {
			JSONArray classifications = event.getJSONArray("classifications");
			for (int i = 0; i < classifications.length(); ++i) {
				JSONObject classification = classifications.getJSONObject(i);
				if (!classification.isNull("segment")) {
					JSONObject segment = classification.getJSONObject("segment");
					if (!segment.isNull("name")) {
						categories.add(segment.getString("name"));
					}
				}
			}
		}
		return categories;
	}
	
	private String getImageUrl(JSONObject event) throws JSONException {
		if (!event.isNull("images")) {
			JSONArray array = event.getJSONArray("images");
			for (int i = 0; i < array.length(); i++) {
				JSONObject image = array.getJSONObject(i);
				if (!image.isNull("url")) {
					return image.getString("url");
				}
			}
		}
		return "";
	}

	/**
	 * Main entry to test TicketMasterClient.
	 */
	public static void main(String[] args) {
		TicketMasterClient client = new TicketMasterClient();
		List<Item> events = client.search(37.38, -122.08, null);

		for (Item event : events) {
			System.out.println(event.toJSONObject());
		}
	}

}
