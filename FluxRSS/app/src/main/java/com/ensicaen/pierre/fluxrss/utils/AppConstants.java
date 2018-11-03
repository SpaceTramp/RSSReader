package com.ensicaen.pierre.fluxrss.utils;

public final class AppConstants {

    public static final String PARISIEN_RSS = "http://www.leparisien.fr/actualites-a-la-une.rss.xml";
    public static final String NASA_RSS = "https://www.nasa.gov/rss/dyn/lg_image_of_the_day.rss";
    public static final String CURRENT_RSS = NASA_RSS;

    public static final String DATABASE_NAME = "FeedReader.db";
    public static final String TABLE_NAME = "entry";

    public static final String PING_GOOGLE = "/system/bin/ping -c 1 8.8.8.8";

    public static final int MOVED_PERMANENTLY = 301;
    public static final int MOVED_TEMPORARILY = 302;



}
