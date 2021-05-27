package com.es.core.model.order;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class OrderDataDTO {

    @NotEmpty
    @Pattern(regexp = "^[\\p{L} .'-]+$")
    private String firstName;

    @NotEmpty
    @Pattern(regexp = "^[\\p{L} .'-]+$")
    private String lastName;

    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z0-9,\\s]+$")
    private String address;

    @NotEmpty
    @Pattern(regexp = "^\\+\\d{12}$")
    private String phone;

    private String additionalInformation;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }
}
