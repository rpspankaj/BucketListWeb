package com.walmart.ocr.model;

public class ParseRequest {
	private String id;
	private String FrontText;
	private String BackText;
	private String FrontTextFormatted;
	private String BackTextFormatted;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getFrontText() {
		return FrontText;
	}
	public void setFrontText(String frontText) {
		FrontText = frontText;
	}
	public String getBackText() {
		return BackText;
	}
	public void setBackText(String backText) {
		BackText = backText;
	}
	public String getFrontTextFormatted() {
		return FrontTextFormatted;
	}
	public void setFrontTextFormatted(String frontTextFormatted) {
		FrontTextFormatted = frontTextFormatted;
	}
	public String getBackTextFormatted() {
		return BackTextFormatted;
	}
	public void setBackTextFormatted(String backTextFormatted) {
		BackTextFormatted = backTextFormatted;
	}
	
	

}
