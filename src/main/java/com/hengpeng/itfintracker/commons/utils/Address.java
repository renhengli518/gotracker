package com.hengpeng.itfintracker.commons.utils;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Address implements Serializable {
	/**
	 * @author renhengli
	 * @date 2016年9月14日
	 * @version 1.0
	 */
	private static final long serialVersionUID = 4237259530635335803L;
	private String country;
	private String area;
	private String region;
	private String city;

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

}
