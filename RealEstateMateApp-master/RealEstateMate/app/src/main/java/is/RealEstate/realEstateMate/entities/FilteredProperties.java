package is.RealEstate.realEstateMate.entities;

import java.util.List;

public class FilteredProperties {

    private List<Property> properties;

    public FilteredProperties() {

    }

    public FilteredProperties(List<Property> properties) {
        this.properties = properties;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }
}
