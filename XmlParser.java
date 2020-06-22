package com.example.top10songs;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

public class XmlParser {
    private static final String TAG = "XmlParser";
    private ArrayList<FeedEntry> application;

    public XmlParser() {
        this.application = new ArrayList<>();
    }

    public ArrayList<FeedEntry> getApplication() {
        return application;
    }
    public boolean parse(String xmlData){
        Log.d(TAG, "parse: called");
        boolean status = true;
        FeedEntry currentObject = null;
        boolean inEntry = false;
        String textValue = "";
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(xmlData));
            int eventType = xpp.getEventType();
            while(eventType!=XmlPullParser.END_DOCUMENT){
                String tagName = xpp.getName();
                switch (eventType){
                    case XmlPullParser.START_TAG:
                        if("entry".equalsIgnoreCase(tagName)){
                            inEntry = true;
                            currentObject = new FeedEntry();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if (inEntry){
                            if("entry".equalsIgnoreCase(tagName)){
                                application.add(currentObject);
                            } else if ("title".equalsIgnoreCase(tagName)){

                            currentObject.setTitile(textValue);
                        }
                        }
                        break;

                    default:

                }
                eventType = xpp.next();

            }

            for(FeedEntry app:application){
                Log.d(TAG, "parse:---------------------------------- ");
                Log.d(TAG,  app.toString());
            }
        }catch (Exception e){
            status = false;
            e.printStackTrace();
        }
        return status;
    }
}
