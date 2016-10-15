package com.walmart.ocr.util;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.http.auth.AUTH;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.storage.StorageScopes;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.AnnotateImageResponse;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.google.common.collect.ImmutableList;

public class GVision {
	private static final String APPLICATION_NAME = "ust-smart-ocr";
	private static final int MAX_RESULTS = 6;
	private static final Logger logger = Logger
			.getLogger(GVision.class);
	private Vision vision;

	public GVision() {
		vision = authenticateGoogleAPI();
	}

	/**
	 * Connects to the Vision API using Application Default Credentials.
	 */
	private Vision authenticateGoogleAPI() {
		try {
			InputStream resourceAsStream = AUTH.class.getClassLoader().getResourceAsStream("USTSmartOCR-bc067713a664.json");

			GoogleCredential credential = GoogleCredential.fromStream(resourceAsStream);
			if (credential.createScopedRequired()) {
			      Collection<String> scopes = StorageScopes.all();
			      credential = credential.createScoped(scopes);
			    }
			//GoogleCredential credential = GoogleCredential.getApplicationDefault().createScoped(VisionScopes.all());
			JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
			return new Vision.Builder(GoogleNetHttpTransport.newTrustedTransport(), jsonFactory, credential)
					.setApplicationName(APPLICATION_NAME)
					.build();
		} catch (IOException e) {
			logger.error("Unable to access Google Vision API", e);
		} catch (GeneralSecurityException e) {
			logger.error("Unable to authenticate with Google Vision API", e);
		}
		return vision;
	}

	/**
	 * Gets up to {@code maxResults} text for an image stored at
	 * {@code uri}.
	 */
	public AnnotateImageResponse doOCR(File file) throws Exception {

		if (vision == null)
			authenticateGoogleAPI();
		FileInputStream fileInputStream=null;
        byte[] bFile = new byte[(int) file.length()];
	    fileInputStream = new FileInputStream(file);
	    fileInputStream.read(bFile);
	    fileInputStream.close();
	    
		AnnotateImageRequest request = new AnnotateImageRequest()
			.setImage(new Image().encodeContent(bFile))
			.setFeatures(ImmutableList.of(new Feature().setType("TEXT_DETECTION").setMaxResults(MAX_RESULTS),
					new Feature().setType("LOGO_DETECTION").setMaxResults(MAX_RESULTS),
					new Feature().setType("LABEL_DETECTION").setMaxResults(MAX_RESULTS),
					new Feature().setType("IMAGE_PROPERTIES").setMaxResults(MAX_RESULTS)));
		Vision.Images.Annotate annotate;
		try {
			
			annotate = vision.images()
					.annotate(new BatchAnnotateImagesRequest().setRequests(ImmutableList.of(request)));
			BatchAnnotateImagesResponse batchResponse = annotate.execute();
			assert batchResponse.getResponses().size() == 1;
			AnnotateImageResponse response = batchResponse.getResponses().get(0);
			if (response.getError() != null) {
				logger.error("Failed to process document ["+file.getName()+"]");
				logger.error(response.getError().getMessage());
				throw new Exception(response.getError().getMessage());
			} else {
				
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.writeValue(
					    new FileOutputStream("output.json"), response);
				return response;				
			}
		} catch (IOException e) {
			logger.error("Failed to process document ["+file.getName()+"]",e);
			throw e;
		}
	}
	public BatchAnnotateImagesResponse doOCR(List<File> imageFiles) throws Exception {
		if (vision == null)
			authenticateGoogleAPI();

		List<byte[]> byteArrList = new ArrayList<byte[]>();
		for (File file : imageFiles) {
			FileInputStream fileInputStream = null;
			byte[] bFile = new byte[(int) file.length()];
			fileInputStream = new FileInputStream(file);
			fileInputStream.read(bFile);
			byteArrList.add(bFile);
			fileInputStream.close();
		}
		List<AnnotateImageRequest> annotateImageRequests = new ArrayList<AnnotateImageRequest>();
		
		
		
		for (byte[] bFile : byteArrList) {
			AnnotateImageRequest request = new AnnotateImageRequest();		
			request.setFeatures(ImmutableList.of(new Feature().setType("TEXT_DETECTION").setMaxResults(MAX_RESULTS),
					new Feature().setType("LOGO_DETECTION").setMaxResults(MAX_RESULTS),
					new Feature().setType("LABEL_DETECTION").setMaxResults(MAX_RESULTS),
					new Feature().setType("IMAGE_PROPERTIES").setMaxResults(MAX_RESULTS)));
			request.setImage(new Image().encodeContent(bFile));
			annotateImageRequests.add(request);
		}
		Vision.Images.Annotate annotate;
		try {
			annotate = vision.images()
					.annotate(new BatchAnnotateImagesRequest().setRequests(annotateImageRequests));
			BatchAnnotateImagesResponse batchResponse = annotate.execute();
			//assert batchResponse.getResponses().size() == 1;
			

				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.writeValue(new FileOutputStream("output.json"), batchResponse.getResponses());
				return batchResponse;
		
		} catch (IOException e) {
			logger.error("Failed to process Images", e);
			throw e;
		}
	}
	public static void main (String args[]){
		GVision gvision = new GVision();
		try {
			File myFile  = new File("149664-lego-racers-nintendo-64-front-cover.jpg");
			gvision.doOCR(myFile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
