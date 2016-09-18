package com.hengpeng.itfintracker.commons.utils;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressAttr implements Serializable {
	/**
	 * @author renhengli
	 * @date 2016年9月14日
	 * @version 1.0
	 */
	private static final long serialVersionUID = -8744486679259503220L;
	private String code;
	private Object data;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	

}
