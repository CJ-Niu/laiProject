package rpc;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

public class RpcHelper {
	// Writes a JSONArray to http response.
	public static void writeJsonArray(HttpServletResponse response, JSONArray array) throws IOException {
		response.setContentType("application/json");
		response.getWriter().print(array);
	}

	// Writes a JSONObject to http response.
	public static void writeJsonObject(HttpServletResponse response, JSONObject obj) throws IOException {
		response.setContentType("application/json");
		response.getWriter().print(obj);
	}
	
	// Parses a JSONObject from http request.
	// New function to parse HTTP request body. 
	// Imagine the input HTTP request looks like:
	// {
    //	'user_id':'1111',
    //	'favorite' : [
    // 	   'item_id1',
    //	    'item_id2'
    //	]
	// }
	public static JSONObject readJSONObject(HttpServletRequest request) {
  	   StringBuilder sBuilder = new StringBuilder();
  	   try (BufferedReader reader = request.getReader()) {
  		 String line = null;
  		 while((line = reader.readLine()) != null) {
  			 sBuilder.append(line);
  		 }
  		 return new JSONObject(sBuilder.toString());
  		
  	   } catch (Exception e) {
  		 e.printStackTrace();
  	   }
  	
  	  return new JSONObject();
            }

}

