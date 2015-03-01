package com.tyler.dealfinder;

import java.io.Serializable;

public class EbayListing implements Serializable {
	private String name;
	private double price;
	private String url;
    private String picUrl;

	public EbayListing(String n, double p, String u, String pu) {
		name = n;
		price = p;
		url = u;
        picUrl = pu;
	}

	public String getName() {
		return name;
	}

	public double getPrice() {
		return price;
	}

	public String getUrl() {
		return url;
	}

    public String getPicUrl() {
        return picUrl;
    }
}