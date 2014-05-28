package org.jfu.test.transaction.po;

import java.io.Serializable;

public class User implements Serializable {

    private static final long serialVersionUID = 139417726146946624L;

    private Integer id;
    private String name;
    private String email;
    private Integer addressId;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public Integer getAddressId() {
        return addressId;
    }
    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }
    @Override
    public String toString() {
        return "User [id=" + id + ", name=" + name + ", email=" + email
                + ", addressId=" + addressId + "]";
    }

}
