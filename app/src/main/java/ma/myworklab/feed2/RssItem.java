package ma.myworklab.feed2;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


class RssItem {

    private  int id;
    private String source;
    private String title;
    private String description;
    private String link;
    private String imageUrl;
    private Date pubDate;
    private String category="";
    private SimpleDateFormat sdf = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss", Locale.getDefault());

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        if(source.equals("nytimes")) setId(R.drawable.nytimes);
        else if(source.equals("nytimes")) setId(R.drawable.hespress);
        this.source = source;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    SimpleDateFormat getSdf() {
        return sdf;
    }

    void setSdf(SimpleDateFormat sdf) {
        this.sdf = sdf;
    }

    void setTitle(String title) {
        this.title = title;
    }

    String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    String getLink() {
        return link;
    }

    void setLink(String link) {
        this.link = link;
    }

    String getImageUrl() {
        return imageUrl;
    }

    void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    Date getPubDate() {
        return pubDate;
    }

    void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    String getCategory() {
        return category;
    }

    void setCategory(String category) {
        if(!category.isEmpty()){
            this.category += category+" ";
        }
    }
}
