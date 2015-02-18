package com.tyler.dealfinder;

public class EbayListing {
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