package plazadecomidas.restaurants.domain.model;

public class Restaurant {

    private final Long id;
    private final String name;
    private final String address;
    private final Long ownerId;
    private final String phoneNumber;
    private final String logoUrl;
    private final String nit;

    public Restaurant(Long id, String name, String address, Long ownerId, String phoneNumber, String logoUrl, String nit) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.ownerId = ownerId;
        this.phoneNumber = phoneNumber;
        this.logoUrl = logoUrl;
        this.nit = nit;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public String getNit() {
        return nit;
    }
}
