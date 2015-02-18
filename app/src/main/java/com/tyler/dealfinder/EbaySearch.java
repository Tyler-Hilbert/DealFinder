package com.tyler.dealfinder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class EbaySearch {
    public final static String EBAY_APP_ID = "TylerHil-1764-448e-9aea-d0910ceebd1c";
    public final static String EBAY_FINDING_SERVICE_URI =
            "http://svcs.ebay.com/services/search/FindingService/v1?"
            + "OPERATION-NAME={operation}&SERVICE-VERSION={version}&"
            + "SECURITY-APPNAME={applicationId}&"
            + "GLOBAL-ID={globalId}&"
            + "keywords={keywords}&"
            + "paginationInput.entriesPerPage={maxresults}&"
            + "itemFilter(0).name=TopRatedSellerOnly&itemFilter(0).value=true&"
            + "itemFilter(1).name=ListingType&itemFilter(1).value=FixedPrice";
    public static final String SERVICE_VERSION = "1.0.0";
    public static final String OPERATION_NAME = "findItemsByKeywords";
    public static final String GLOBAL_ID = "EBAY-US";
    public final static int REQUEST_DELAY = 3000;
    public final static int MAX_RESULTS = 10;


    public ArrayList<EbayListing> run(String tag) throws Exception {
        String address = createAddress(tag);
        String response = URLReader.read(address);

        //Honor rate limits - wait between results
        Thread.sleep(REQUEST_DELAY);
        return processResponse(response);
    }

    private String createAddress(String tag) {
        tag = tag.replace(" ", "+");

        //substitute token
        String address = EbaySearch.EBAY_FINDING_SERVICE_URI;
        address = address.replace("{version}", EbaySearch.SERVICE_VERSION);
        address = address.replace("{operation}", EbaySearch.OPERATION_NAME);
        address = address.replace("{globalId}", EbaySearch.GLOBAL_ID);
        address = address.replace("{applicationId}", EbaySearch.EBAY_APP_ID);
        address = address.replace("{keywords}", tag);
        address = address.replace("{maxresults}", "" + MAX_RESULTS);

        return address;
    }

    private ArrayList<EbayListing> processResponse(String response) throws Exception {
        // Grab results
        XPath xpath = XPathFactory.newInstance().newXPath();
        InputStream is = new ByteArrayInputStream(response.getBytes("UTF-8"));
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = domFactory.newDocumentBuilder();

        Document doc = builder.parse(is);
        XPathExpression ackExpression = xpath.compile("//findItemsByKeywordsResponse/ack");
        XPathExpression itemExpression = xpath.compile("//findItemsByKeywordsResponse/searchResult/item");

        String ackToken = (String) ackExpression.evaluate(doc, XPathConstants.STRING);

        if (!ackToken.equals("Success")) {
            throw new Exception(" service returned an error");
        }

        NodeList nodes = (NodeList) itemExpression.evaluate(doc, XPathConstants.NODESET);
        ArrayList<EbayListing> listings = new ArrayList<EbayListing>();

        // Put results in ArrayList
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);

            String itemId = (String) xpath.evaluate("itemId", node, XPathConstants.STRING);
            String title = (String) xpath.evaluate("title", node, XPathConstants.STRING);
            String itemUrl = (String) xpath.evaluate("viewItemURL", node, XPathConstants.STRING);
            String galleryUrl = (String) xpath.evaluate("galleryURL", node, XPathConstants.STRING);
            String currentPrice = (String) xpath.evaluate("sellingStatus/currentPrice", node, XPathConstants.STRING);

            listings.add(new EbayListing(title, Double.parseDouble(currentPrice), itemUrl, galleryUrl));
        }


        // Sort Results
        Collections.sort(listings, new Comparator<EbayListing>() {
            @Override
            public int compare(EbayListing l1, EbayListing l2) {
                return (int)(l1.getPrice() - l2.getPrice());
            }
        });

        is.close();
        return listings;
    }
}