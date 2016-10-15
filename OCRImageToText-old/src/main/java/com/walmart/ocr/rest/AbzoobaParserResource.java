package com.walmart.ocr.rest;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONException;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.walmart.ocr.model.ParseRequest;
import com.walmart.ocr.util.JsonstringToMap;


@Path("/abzoobaParse")
public class AbzoobaParserResource {

	private static final Logger logger = Logger
			.getLogger(AbzoobaParserResource.class);

	@POST
	@Path("/parseText")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> uploadFile1(ParseRequest parseInput) {
		String output = null;
		//Only Fake Response.
		boolean fake=false;
		Map<String, Object> myMap=null;
		try {
			logger("Parsing With  Abzooba ....");
			if(!fake){
			Client client = Client.create();

			//WebResource webResource = client.resource("http://52.23.170.75:5000/model2");
			WebResource webResource = client.resource("http://52.23.170.75:5000/beauty");
			ObjectMapper mapper = new ObjectMapper();
			String jsonInString = mapper.writeValueAsString(parseInput);
			jsonInString=jsonInString.replace("frontText", "FrontText");
			jsonInString=jsonInString.replace("backText", "BackText");
			ClientResponse response = webResource.type("application/json").post(ClientResponse.class, jsonInString);

			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}

			output = response.getEntity(String.class);
			myMap= new LinkedHashMap<String, Object>();
			Map<String, Object> myMap1=JsonstringToMap.jsonString2Map(output);
			myMap1.remove("id");
			myMap1.remove("Raw_Data");			
			myMap.put("UPC Number", parseInput.getId());
			String longDesc = (String) myMap1.get("Product_Long_Description");
			longDesc= longDesc.replaceAll("\n", "<br/>");
			myMap1.put("Product_Long_Description",longDesc);
			myMap.putAll(myMap1);
			
			}
			else{
			myMap= new LinkedHashMap<String, Object>();
			//myMap.put("id", parseInput.getId());
			//myMap.put("Raw_Data", parseInput.getText());
			myMap.put("Brand", "Lego");
			myMap.put("Age", "7-14");
			myMap.put("Warning", "choking hazard");
			myMap.put("Pieces", "248");
//			Client client = Client.create();
//
//			WebResource webResource = client.resource("http://ocrsmartreader.herokuapp.com/rest/abzoobaParse/parseText");
//			ObjectMapper mapper = new ObjectMapper();
//			String jsonInString = mapper.writeValueAsString(parseInput);
//			ClientResponse response = webResource.type("application/json").post(ClientResponse.class, jsonInString);
//
//			if (response.getStatus() != 200) {
//				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
//			}
//
//			output = response.getEntity(String.class);
			//myMap= new HashMap<String, Object>();
			//myMap=JsonstringToMap.jsonString2Map(output);
			//myMap.put("UPC Number", parseInput.getImageFileName());
			}

		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return myMap;
	}
	void logger(String log) {
		logger.debug(log);
	}
}
