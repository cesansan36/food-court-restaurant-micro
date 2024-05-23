package plazadecomidas.restaurants.domain.model;

import plazadecomidas.restaurants.domain.exception.EmptyFieldException;
import plazadecomidas.restaurants.domain.exception.FieldRuleInvalidException;
import plazadecomidas.restaurants.domain.util.DomainConstants;
import plazadecomidas.restaurants.domain.util.Validator;

public class Restaurant {

    private final Long id;
    private final String name;
    private final String address;
    private final Long ownerId;
    private final String phoneNumber;
    private final String logoUrl;
    private final String nit;

    public Restaurant(Long id, String name, String address, Long ownerId, String phoneNumber, String logoUrl, String nit) {

        validateFields(name, address, ownerId, phoneNumber, logoUrl, nit);

        name = name.trim();
        address = address.trim();
        phoneNumber = phoneNumber.trim();
        logoUrl = logoUrl.trim();
        nit = nit.trim();

        this.id = id;
        this.name = name;
        this.address = address;
        this.ownerId = ownerId;
        this.phoneNumber = phoneNumber;
        this.logoUrl = logoUrl;
        this.nit = nit;
    }

    private void validateFields(String name, String address, Long ownerId, String phoneNumber, String logoUrl, String nit) {

        if (Validator.isFieldEmpty(name)) {
            throw new EmptyFieldException(String.format(DomainConstants.EMPTY_FIELD_MESSAGE, DomainConstants.RestaurantFields.NAME));
        }
        if (Validator.isFieldEmpty(address)) {
            throw new EmptyFieldException(String.format(DomainConstants.EMPTY_FIELD_MESSAGE, DomainConstants.RestaurantFields.ADDRESS));
        }
        if (ownerId == null) {
            throw new EmptyFieldException(String.format(DomainConstants.EMPTY_FIELD_MESSAGE, DomainConstants.RestaurantFields.OWNER_ID));
        }
        if (Validator.isFieldEmpty(phoneNumber)) {
            throw new EmptyFieldException(String.format(DomainConstants.EMPTY_FIELD_MESSAGE, DomainConstants.RestaurantFields.PHONE_NUMBER));
        }
        if (Validator.isFieldEmpty(logoUrl)) {
            throw new EmptyFieldException(String.format(DomainConstants.EMPTY_FIELD_MESSAGE, DomainConstants.RestaurantFields.LOGO_URL));
        }
        if (Validator.isFieldEmpty(nit)) {
            throw new EmptyFieldException(String.format(DomainConstants.EMPTY_FIELD_MESSAGE, DomainConstants.RestaurantFields.NIT));
        }

        if (!Validator.isValidNit(nit)) {
            throw new FieldRuleInvalidException(DomainConstants.NIT_INVALID_MESSAGE);
        }
        if (!Validator.isValidName(name)) {
            throw new FieldRuleInvalidException(DomainConstants.NAME_INVALID_MESSAGE);
        }
        if (!Validator.isValidPhoneNumber(phoneNumber)) {
            throw new FieldRuleInvalidException(DomainConstants.PHONE_NUMBER_CHAR_AMOUNT_EXCEEDED);
        }
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

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", ownerId=" + ownerId +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", logoUrl='" + logoUrl + '\'' +
                ", nit='" + nit + '\'' +
                '}';
    }
}
