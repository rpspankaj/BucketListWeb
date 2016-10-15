package com.walmart.ocr.util;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.Map;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
/**
 * 
 * @author pasha2
 *
 */
public class JsonstringToMap {
	
	/**
	 * It takes Json String as input & convert to a map with json keys & values for each entries
	 * @param jsonString
	 * @return
	 * @throws JSONException
	 */
	private JsonstringToMap(){
		
	}
	public static Map<String, Object> jsonString2Map(String jsonString) throws JSONException
	{
		return jsonString2Map( jsonString, null);
	}
	/**
	 * 
	 * @param jsonString
	 * @param prependKey
	 * @return
	 * @throws JSONException
	 */
	private static Map<String, Object> jsonString2Map(String jsonString,String prependKey)
			throws JSONException {
		Map<String, Object> jsonMap = new LinkedHashMap<String, Object>();		
		JSONObject jsonObject = new JSONObject(jsonString);
		Iterator<?> keyset = jsonObject.keys();
		while (keyset.hasNext()) {
			String key = (String) keyset.next();
			String actualKey=key;
			if(null!= prependKey){
				actualKey=prependKey+"."+key;
			}
			Object value = jsonObject.get(key);
			if (value instanceof JSONObject) {				
				Map<String, Object> temp = jsonString2Map(value.toString(), actualKey);
				jsonMap.putAll(temp);
			}
			if(!value.toString().startsWith("{")){
				jsonMap.put(actualKey, value);
			}
		}
		return jsonMap;
	}

	
}
