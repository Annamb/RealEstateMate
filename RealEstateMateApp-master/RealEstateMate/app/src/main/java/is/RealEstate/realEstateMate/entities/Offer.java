package is.RealEstate.realEstateMate.entities;

public class Offer {

    private long offerID;
    public long propertyID;
    public long offerAmount;
    private long userID;


    public Offer() {
    }

    public long getOfferID() {
        return offerID;
    }

    public void setOfferID(long offerID) {
        this.offerID = offerID;
    }

    public long getPropertyID() {
        return propertyID;
    }

    public void setPropertyID(long propertyID) {
        this.propertyID = propertyID;
    }

    public long getOfferAmount() {
        return offerAmount;
    }

    public void setOfferAmount(long offerAmount) {
        this.offerAmount = offerAmount;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public Offer(long offerID, long propertyID, long offerAmount, long userID) {
        this.offerID = offerID;
        this.propertyID = propertyID;
        this.offerAmount = offerAmount;
        this.userID = userID;
    }
}
