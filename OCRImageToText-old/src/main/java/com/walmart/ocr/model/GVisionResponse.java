package com.walmart.ocr.model;

import java.util.List;

public class GVisionResponse {
	private List<String> logoDetails;
	private List<String> labelDetails;
	private List<String> textDeatils;
	private List<String> textDeatilsFormatted;
	private List<String> colorDeatils;
	public List<String> getLogoDetails() {
		return logoDetails;
	}
	public void setLogoDetails(List<String> logoDetails) {
		this.logoDetails = logoDetails;
	}
	public List<String> getLabelDetails() {
		return labelDetails;
	}
	public void setLabelDetails(List<String> labelDetails) {
		this.labelDetails = labelDetails;
	}
	public List<String> getTextDeatils() {
		return textDeatils;
	}
	public void setTextDeatils(List<String> textDeatils) {
		this.textDeatils = textDeatils;
	}
	public List<String> getColorDeatils() {
		return colorDeatils;
	}
	public void setColorDeatils(List<String> colorDeatils) {
		this.colorDeatils = colorDeatils;
	}
	public List<String> getTextDeatilsFormatted() {
		return textDeatilsFormatted;
	}
	public void setTextDeatilsFormatted(List<String> textDeatilsFormatted) {
		this.textDeatilsFormatted = textDeatilsFormatted;
	}
	

}
