package plazadecomidas.restaurants.domain.model;

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
            throw new FieldRuleInvalidException(String.format(DomainConstants.EMPTY_FIELD_MESSAGE, DomainConstants.Fields.NAME));
        }
        if (Validator.isFieldEmpty(address)) {
            throw new FieldRuleInvalidException(String.format(DomainConstants.EMPTY_FIELD_MESSAGE, DomainConstants.Fields.ADDRESS));
        }
        if (ownerId == null) {
            throw new FieldRuleInvalidException(String.format(DomainConstants.EMPTY_FIELD_MESSAGE, DomainConstants.Fields.OWNER_ID));
        }
        if (Validator.isFieldEmpty(phoneNumber)) {
            throw new FieldRuleInvalidException(String.format(DomainConstants.EMPTY_FIELD_MESSAGE, DomainConstants.Fields.PHONE_NUMBER));
        }
        if (Validator.isFieldEmpty(logoUrl)) {
            throw new FieldRuleInvalidException(String.format(DomainConstants.EMPTY_FIELD_MESSAGE, DomainConstants.Fields.LOGO_URL));
        }
        if (Validator.isFieldEmpty(nit)) {
            throw new FieldRuleInvalidException(String.format(DomainConstants.EMPTY_FIELD_MESSAGE, DomainConstants.Fields.NIT));
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
}
