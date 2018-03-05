package dupre.com.outerspacemanager.outerspacemanager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by sdupre on 05/03/2018.
 */

public class BuildingAdapter extends ArrayAdapter<Building>{

    private final Context context;
    private final Building[] values;
    public BuildingAdapter(Context context, Building[] values) {
        super(context,R.layout.activity_bat, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.building_layout, parent, false);






        //TextView textView = (TextView) rowView.findViewById(R.id.);
        //ImageView imageView = (ImageView) rowView.findViewById(R.id.);
        //textView.setText(values[position].toString());
        //new DownLoadImageTask(imageView).execute(values[position].getImageURL());

        return rowView;
    }

    class DownLoadImageTask extends AsyncTask<String,Void,Bitmap> {
        ImageView imageView;

        public DownLoadImageTask(ImageView imageView){
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String...urls){
            String urlOfImage = urls[0];
            Bitmap logo = null;
            try{
                InputStream is = new URL(urlOfImage).openStream();
                logo = BitmapFactory.decodeStream(is);
            }catch(Exception e){ // Catch the download exception
                e.printStackTrace();
            }
            return logo;
        }

        protected void onPostExecute(Bitmap result){
            imageView.setImageBitmap(result);
        }
}
