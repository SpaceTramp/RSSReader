package com.ensicaen.pierre.fluxrss.data.network;


import android.util.Log;

import com.ensicaen.pierre.fluxrss.data.db.model.Item;
import com.ensicaen.pierre.fluxrss.data.network.asyncTask.DownloadRssTask;
import com.ensicaen.pierre.fluxrss.utils.NetworkUtils;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MyRSSsaxHandler extends DefaultHandler {

    private static final String TAG = MyRSSsaxHandler.class.getSimpleName();

    private ArrayList<Item> listItems = new ArrayList<>();
    private Item item;
    private String url = null;// l'URL du flux RSS à parser

    // Ensemble de drapeaux permettant de savoir où en est le parseur dans le flux XML
    private boolean inTitle = false;
    private boolean inDescription = false;
    private boolean inItem = false;
    private boolean inDate = false;

    private StringBuffer buffer = new StringBuffer();
    private int numItemMax = -1; // Le nombre total d’items dans le flux RSS

    public void setUrl(String url) {
        this.url = url;
    }


    public void processFeed() {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            XMLReader reader = parser.getXMLReader();
            reader.setContentHandler(this);
            InputStream inputStream = NetworkUtils.openLink(url);
            reader.parse(new InputSource(inputStream));

        } catch (Exception e) {
            Log.e(TAG, "processFeed Exception" + e.getMessage());
        }
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if (qName.equals("item")) {
            item = new Item();
            numItemMax++;
            inItem = true;
        } else if (inItem) {
            switch (qName) {
                case "title":
                    inTitle = true;
                    break;
                case "pubDate":
                    inDate = true;
                    break;
                case "description":
                    inDescription = true;
                    break;
                case "enclosure":
                    item.setUrl(attributes.getValue(0));
                    break;
            }
        }
    }

    public void endElement(String uri, String localName, String qName) {
        if (qName.equals("item")) {
            listItems.add(item);
            item = null;
            inItem = false;
        } else if (inItem) {
            switch (qName) {
                case "title":
                    item.setTitle(buffer.toString());
                    buffer = new StringBuffer();
                    inTitle = false;
                    break;
                case "pubDate":
                    item.setPubDate(buffer.toString());
                    buffer = new StringBuffer();
                    inDate = false;
                    break;
                case "description":
                    item.setDescription(buffer.toString());
                    buffer = new StringBuffer();
                    inDescription = false;
                    break;
                case "enclosure":
                    break;
            }
        }
    }

    public void characters(char[] ch, int start, int length) {
        if (inDate || inDescription || inTitle) {
            String lecture = new String(ch, start, length);
            buffer.append(lecture);
        }
    }

    public Item getItemByNumber(int i) {
        return listItems.get(i);
    }

    public int getNumItemMax() {
        return numItemMax;
    }
}


