package com.amdocs.owa.csv.entities;

import java.util.List;
import java.util.ArrayList;

public class MassRequest {
	
	private MassDetails massDetails = new MassDetails();
	private MassHeader massHeader = new MassHeader();
	private List<MassRequestLine> massLines = new ArrayList<MassRequestLine>();
	private String headerAttributesString;
	private String detailsAttributesString;
	private String linesAttributesString;
	private String requestType;
	public MassDetails getMassDetails() {
		return massDetails;
	}
	public void setMassDetails(MassDetails massDetails) {
		this.massDetails = massDetails;
	}
	public MassHeader getMassHeader() {
		return massHeader;
	}
	public void setMassHeader(MassHeader massHeader) {
		this.massHeader = massHeader;
	}
	public List<MassRequestLine> getMassLines() {
		return massLines;
	}
	public void setMassLines(List<MassRequestLine> massLines) {
		this.massLines = massLines;
	}
	public String getHeaderAttributesString() {
		return headerAttributesString;
	}
	public void setHeaderAttributesString(String headerAttributesString) {
		this.headerAttributesString = headerAttributesString;
	}
	public String getDetailsAttributesString() {
		return detailsAttributesString;
	}
	public void setDetailsAttributesString(String detailsAttributesString) {
		this.detailsAttributesString = detailsAttributesString;
	}
	public String getLinesAttributesString() {
		return linesAttributesString;
	}
	public void setLinesAttributesString(String linesAttributesString) {
		this.linesAttributesString = linesAttributesString;
	}
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

}
