//package com.walmart.ocr.rest;
//
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//
//import javax.ws.rs.Consumes;
//import javax.ws.rs.GET;
//import javax.ws.rs.POST;
//import javax.ws.rs.Path;
//import javax.ws.rs.Produces;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//
//import net.sourceforge.tess4j.ITesseract;
//import net.sourceforge.tess4j.Tesseract;
//import net.sourceforge.tess4j.TesseractException;
//import net.sourceforge.tess4j.util.LoadLibs;
//
//import org.apache.commons.io.IOUtils;
//import org.apache.log4j.Logger;
//
//import com.sun.jersey.core.header.FormDataContentDisposition;
//import com.sun.jersey.multipart.FormDataParam;
//import com.walmart.ocr.model.OCRResult;
//
//@Path("/ocr")
//public class OcrConverterResource {
//
//	private static final Logger logger = Logger.getLogger(OcrConverterResource.class);
//	
//	@POST
//	@Path("/convertImageToText")
//	@Consumes(MediaType.MULTIPART_FORM_DATA)
//	public Response uploadFile1(
//			@FormDataParam("file") InputStream uploadedInputStream,
//			@FormDataParam("file") FormDataContentDisposition fileDetail) {
//
//		String uploadedFileLocation = fileDetail.getFileName();
//		String result = null;
//		try {
//			logger(" ");
//			logger("******** New Conversion Started *******");
//			// save it
//			writeToFile(uploadedInputStream, uploadedFileLocation);
//			String output = "File uploaded to : " + uploadedFileLocation;
//			logger(output);
//			File imageFile = new File(uploadedFileLocation);
//			ITesseract instance = new Tesseract(); // JNA Interface Mapping
//			// ITesseract instance = new Tesseract1(); // JNA Direct Mapping
//			//In case you don't have your own tessdata, let it also be extracted for you
//			File tessDataFolder = LoadLibs.extractTessResources("tessdata");
//
//			//Set the tessdata path
//			instance.setDatapath(tessDataFolder.getAbsolutePath());
//			result = instance.doOCR(imageFile);
//			result = result.replace("\n", " ");
//			logger(result);
//			uploadedInputStream.close();
//			if(imageFile.delete())
//				logger(uploadedFileLocation + " Deleted");
//			else{
//				logger("Failed to delete File");
//			}
//		} catch (TesseractException e) {
//			logger(e.getMessage());
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		saveToTXT(result);
//		return Response.status(200).entity(result).build();
//
//	}
//
//	@GET
//	@Path("/get")
//	@Produces(MediaType.APPLICATION_JSON)
//	public OCRResult getOCRJSON() throws IOException {
//
//		OCRResult ocrResult = new OCRResult();
//		String everything = null;
//		FileInputStream inputStream = null;
//		try {
//			inputStream = new FileInputStream("Converted1.txt");
//
//			everything = IOUtils.toString(inputStream);
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} finally {
//			inputStream.close();
//		}
//		ocrResult.setResultString(everything);
//		return ocrResult;
//	}
//
//	// save uploaded file to new location
//	private void writeToFile(InputStream uploadedInputStream,
//			String uploadedFileLocation) {
//
//		try {
//			OutputStream out = new FileOutputStream(new File(
//					uploadedFileLocation));
//			int read = 0;
//			byte[] bytes = new byte[1024];
//			while ((read = uploadedInputStream.read(bytes)) != -1) {
//				out.write(bytes, 0, read);
//			}
//			out.flush();
//			out.close();
//		} catch (IOException e) {
//
//			e.printStackTrace();
//		}
//
//	}
//
//	public void saveToTXT(String content) {
//
//		try {
//			File file = new File("Converted1.txt");
//
//			// if file does not exists, then create it
//			if (!file.exists()) {
//				file.createNewFile();
//
//			}
//
//			FileWriter fw = new FileWriter(file.getAbsoluteFile());
//			BufferedWriter bw = new BufferedWriter(fw);
//			bw.write(content);
//			bw.close();
//
//			logger("Saved as TXT ");
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//	}
//	void logger(String log){
//		logger.debug(log);
//	}
//}