package is.RealEstate.realEstateMate.entities;

import java.util.List;

public class FilteredOffers {

    private List<Offer> offers;

    public FilteredOffers() {

    }

    public FilteredOffers(List<Offer> offers) {
        this.offers = offers;
    }

    public List<Offer> getOffers() {
        return offers;
    }

    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }
}
