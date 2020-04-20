package is.RealEstate.realEstateMate.ui;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import is.RealEstate.realEstateMate.R;
import is.RealEstate.realEstateMate.entities.Property;

/**
 * Klasi sem hjálpar okkur að birta Property listann snyrtilega á homepage og upphafssíðu með hjálp list_item.xml skránnar
 */
public class PropertyAdapter extends ArrayAdapter<Property> {

    private Context mContext;
    private List<Property> propertiesList = new ArrayList<>();

    public PropertyAdapter(@NonNull Context context, @LayoutRes List<Property> list) {
        super(context, 0 , list);
        mContext = context;
        propertiesList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_item,parent,false);

        Property currentProperty = propertiesList.get(position);

        TextView name = (TextView) listItem.findViewById(R.id.textView_name);
        name.setText(currentProperty.getStreetName() + " " + currentProperty.getStreetNumber());

        TextView size = (TextView) listItem.findViewById(R.id.textView_size);
        size.setText(String.valueOf(currentProperty.getPropertySize()) + "m2");

        TextView price = (TextView) listItem.findViewById(R.id.textView_price);
        price.setText(String.valueOf(currentProperty.getPrice()/1000000) + " mil.");

        ImageView propertyImage = (ImageView) listItem.findViewById(R.id.imageView_property);
        String imageId = currentProperty.getImage1(); //Get image id of current property
        String url = "http://10.0.2.2:9090/Image/" + imageId;
        Picasso.get().load(url).into(propertyImage); //Loadar mynd inn a imageView
        return listItem;
    }
}