package is.RealEstate.realEstateMate.entities;

public class Property {

    public long propertyID;
    public long bedrooms;
    public long bathrooms;
    public String streetName;
    public String streetNumber;
    public long zip;
    public String town;
    public long price;
    public long propertySize;
    public String category;
    public long rooms;
    public long latitude;
    public long longitude;
    public long agentID;
    public long sellerID;
    public String image1;
    public String image2;
    public String image3;
    public String image4;


    public Property() {
    }

    public long getPropertyID() {
        return propertyID;
    }

    public void setPropertyID(long propertyID) {
        this.propertyID = propertyID;
    }

    public long getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(long bedrooms) {
        this.bedrooms = bedrooms;
    }

    public long getBathrooms() {
        return bathrooms;
    }

    public void setBathrooms(long bathrooms) {
        this.bathrooms = bathrooms;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public long getZip() {
        return zip;
    }

    public void setZip(long zip) {
        this.zip = zip;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getPropertySize() {
        return propertySize;
    }

    public void setPropertySize(long propertySize) {
        this.propertySize = propertySize;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getRooms() {
        return rooms;
    }

    public void setRooms(long rooms) {
        this.rooms = rooms;
    }

    public long getLatitude() {
        return latitude;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getImage4() {
        return image4;
    }

    public void setImage4(String image4) {
        this.image4 = image4;
    }

    public long getLongitude() {
        return longitude;
    }

    public void setLongitude(long longitude) {
        this.longitude = longitude;
    }

    public long getAgentID() {
        return agentID;
    }

    public void setAgentID(long agentID) {
        this.agentID = agentID;
    }

    public long getSellerID() {
        return sellerID;
    }

    public void setSellerID(long sellerID) {
        this.sellerID = sellerID;
    }

    public Property(long bedrooms, long bathrooms, String streetName, String streetNumber, long zip, String town, long price, long propertySize, String category, long rooms) {
//        this.propertyID = propertyID;
        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;
        this.streetName = streetName;
        this.streetNumber = streetNumber;
        this.zip = zip;
        this.town = town;
        this.price = price;
        this.propertySize = propertySize;
        this.category = category;
        this.rooms = rooms;
        this.latitude = 40;
        this.longitude = 50;
        this.agentID = 220189;
        this.sellerID = 1;
        this.image1 = "123891";
        this.image2 = "123892";
        this.image3 = "123893";
        this.image4 = "123894";
    }

    public Property(long propertyID, long bedrooms, long bathrooms, String streetName, String streetNumber, long zip, String town, long price, long propertySize, String category, long rooms, long latitude, long longitude, long agentID, long sellerID, String image1, String image2, String image3, String image4) {
        this.propertyID = propertyID;
        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;
        this.streetName = streetName;
        this.streetNumber = streetNumber;
        this.zip = zip;
        this.town = town;
        this.price = price;
        this.propertySize = propertySize;
        this.category = category;
        this.rooms = rooms;
        this.latitude = latitude;
        this.longitude = longitude;
        this.agentID = agentID;
        this.sellerID = sellerID;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.image4 = image4;
    }
}
