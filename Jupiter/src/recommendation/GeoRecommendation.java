package recommendation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import db.DBConnection;
import db.DBConnectionFactory;
import entity.Item;


public class GeoRecommendation {

	public List<Item> recommendItems(String userId, double lat, double lon) {
		List<Item> recommendedItems = new ArrayList<>();

		// Step 1, get all favorited itemids
		DBConnection connection = DBConnectionFactory.getConnection();
		// note: 找到喜欢过的商品集合，返回的是用户曾经喜欢过的item id
		Set<String> favoritedItemIds = connection.getFavoriteItemIds(userId);

		// Step 2, get all categories, sort by count
		// {"sports": 5, "music": 3, "art": 2}
		// <key = event type, value = number of appearance>
		Map<String, Integer> allCategories = new HashMap<>();
		// walk through itemId
		for (String itemId : favoritedItemIds) {
			// note: 找到item所对应的categories的集合，加入到map当中
			Set<String> categories = connection.getCategories(itemId);
			for (String category : categories) {
				// category找到就+1，找不到就是0
				allCategories.put(category, allCategories.getOrDefault(category, 0) + 1);
			}
		}
		// note: 将刚才map中的value排序
		List<Entry<String, Integer>> categoryList = new ArrayList<>(allCategories.entrySet());
		Collections.sort(categoryList, (Entry<String, Integer> e1, Entry<String, Integer> e2) -> {
			// note: lambda comparator
			return Integer.compare(e2.getValue(), e1.getValue());
		});

		// Step 3, search based on category, filter out favorite items
		// note: HashSet记录曾经喜欢过的event，避免推荐的是我已经喜欢过的event
		Set<String> visitedItemIds = new HashSet<>();
		for (Entry<String, Integer> category : categoryList) {
			List<Item> items = connection.searchItems(lat, lon, category.getKey());

			for (Item item : items) {
				// note: 既不在我已经喜欢过的item里，同时之前也没有推荐过，就加入推荐列表里
				if (!favoritedItemIds.contains(item.getItemId()) && !visitedItemIds.contains(item.getItemId())) {
					recommendedItems.add(item);
					visitedItemIds.add(item.getItemId());
				}
			}
		}
		connection.close();
		return recommendedItems;
	}

}
