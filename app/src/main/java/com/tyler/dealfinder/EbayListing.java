package com.tyler.dealfinder;

public class EbayListing {
	private String name;
	private double price;
	private String url;

	public EbayListing(String n, double p, String u) {
		name = n;
		price = p;
		url = u;
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
}