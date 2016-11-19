package ma.myworklab.feed2;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;


class CustomAdapter extends ArrayAdapter<RssItem> {


    private RssItem item;
    private static class ViewHolder {
        TextView source;
        TextView title;
        TextView pubDate;
        ImageView picture;
    }


    CustomAdapter(Context context,  List<RssItem> list) {
        super(context, R.layout.feed_row, list);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView,@NonNull ViewGroup parent){

        item=getItem(position);
        ViewHolder view;
        if (convertView == null) {
            view = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.feed_row, parent, false);
            view.source=(TextView) convertView.findViewById(R.id.source);
            view.title=(TextView) convertView.findViewById(R.id.title);
            view.pubDate=(TextView) convertView.findViewById(R.id.pubDate);
            view.picture=(ImageView) convertView.findViewById(R.id.picture);
            convertView.setTag(view);
        }else{
            view = (ViewHolder) convertView.getTag();
        }
        if (item==null) return convertView;
        view.source.setText(item.getSource());
        view.title.setText(item.getTitle());
        view.pubDate.setText(item.getSdf().format(item.getPubDate()));
        view.picture.setImageBitmap(downloadImage(item.getImageUrl()));
        return convertView;
    }

    private Bitmap downloadImage(String url) {
        Bitmap bitmap = null;
        InputStream stream ;
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inSampleSize = 1;

        try {
            stream = getHttpConnection(url);
            if(stream!=null){
                bitmap = BitmapFactory.decodeStream(stream, null, bmOptions);
                stream.close();
            }
            else{
                Resources res = getContext().getResources();
                int id = item.getId();
                bitmap=BitmapFactory.decodeResource(res,id);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return bitmap;
    }

    private InputStream getHttpConnection(String urlString){
        InputStream stream = null;
        try {
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            httpConnection.setRequestMethod("GET");
            httpConnection.connect();
            if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                stream = httpConnection.getInputStream();
            }
            else return null;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return stream;
    }
}

