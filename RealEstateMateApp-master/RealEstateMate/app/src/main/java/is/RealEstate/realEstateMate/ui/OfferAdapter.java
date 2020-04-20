package is.RealEstate.realEstateMate.ui;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import is.RealEstate.realEstateMate.R;
import is.RealEstate.realEstateMate.entities.Offer;

/**
 * Klasi sem hjálpar okkur að birta Offer listann snyrtilega á homepage með hjálp list_item_offer.xml skránnar
 */
public class OfferAdapter extends ArrayAdapter<Offer> {

    private Context mContext;
    private List<Offer> offersList = new ArrayList<>();

    public OfferAdapter(@NonNull Context context, @LayoutRes List<Offer> list) {
        super(context, 0 , list);
        mContext = context;
        offersList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_item_offer,parent,false);

        Offer currentOffer = offersList.get(position);

        TextView name = (TextView) listItem.findViewById(R.id.textView_propertyid);
        name.setText("Property ID: " + String.valueOf(currentOffer.getPropertyID()));

        TextView price = (TextView) listItem.findViewById(R.id.textView_amount);
        price.setText("Offer Amount: " + String.valueOf(currentOffer.getOfferAmount()));

        return listItem;
    }
}