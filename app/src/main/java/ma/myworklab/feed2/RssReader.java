package ma.myworklab.feed2;

import android.util.JsonWriter;

import java.io.FileWriter;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;


class RssReader {

    private URL url;
    private String source;
    private static final long DAY = 2 * 60 * 60 * 1000;

    private URL getUrl() {
        return url;
    }

    void setUrl(URL url) {
        this.url = url;
        setSource(getUrl().getHost());
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    private static InputStream read(URL url) throws Exception{
        HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
        httpcon.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)" +
                " AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36");
        httpcon.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;" +
                "q=0.9,image/webp,*/*;q=0.8");
        httpcon.addRequestProperty("Accept-Language", "fr,ar;q=0.8,en;q=0.6,en-GB;q=0.4");
        return httpcon.getInputStream();
    }

    List<RssItem> getItems() throws Exception {
        RssItem currentItem;
        List<RssItem> rssItemList = new ArrayList<>();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(read(getUrl()));
        NodeList nodeList = document.getElementsByTagName("item");
        JsonWriter newsCache=new JsonWriter(new FileWriter("newsCache.json"));

        for(int i=0, size1=nodeList.getLength();i<size1;i++){
            currentItem=new RssItem();
            newsCache.beginArray();
            newsCache.beginObject();
            newsCache.name("item");
            newsCache.beginArray();
            newsCache.beginObject();
            newsCache.name("source");

            if(getSource().equals("rss.nytimes.com")){
                currentItem.setSource("nytimes");
                newsCache.value("nytimes");
            }
            else if(getSource().equals("www.hespress.com")){
                currentItem.setSource("hespress");
                newsCache.value("nytimes");
            }

            newsCache.endArray();
            newsCache.endObject();
            for(int y=0,size2=nodeList.item(i).getChildNodes().getLength();y<size2;y++){
                newsCache.beginArray();
                newsCache.beginObject();

                String nodeName=nodeList.item(i).getChildNodes().item(y).getNodeName();
                String content=nodeList.item(i).getChildNodes().item(y).getTextContent();
                switch(nodeName){
                    case "media:thumbnail" :
                    case "media:content" :
                        currentItem.setImageUrl(nodeList.item(i).getChildNodes().item(y).
                                getAttributes().getNamedItem("url").getTextContent());
                        newsCache.name("media");
                        newsCache.value(nodeList.item(i).getChildNodes().item(y).
                                getAttributes().getNamedItem("url").getTextContent());
                        break;
                    case "title" :
                        currentItem.setTitle(content);
                        newsCache.name("title");
                        newsCache.value(content);
                        break;
                    case "link" :
                        currentItem.setLink(content);
                        newsCache.name("link");
                        newsCache.value(content);
                        break;
                    case "pubDate" :
                        currentItem.setPubDate(currentItem.getSdf().parse(content));
                        newsCache.name("pubDate");
                        newsCache.value(content);
                        break;
                    case "category" :
                        currentItem.setCategory(content);
                        break;
                    case "description" :
                        currentItem.setDescription(content);
                        break;
                }
                newsCache.endArray();
                newsCache.endObject();
            }
            newsCache.endArray();
            newsCache.endObject();
            newsCache.close();
            if(currentItem.getPubDate().getTime()> System.currentTimeMillis()-DAY ){
                rssItemList.add(currentItem);
            }
        }
        return rssItemList ;
    }
}
