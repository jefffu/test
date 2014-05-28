package org.jfu.test.transaction.po;

import java.io.Serializable;

public class Address implements Serializable{

    private static final long serialVersionUID = 6924383925278413285L;
    private Integer id;
    private String detail;
    private Integer postcode;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getDetail() {
        return detail;
    }
    public void setDetail(String detail) {
        this.detail = detail;
    }
    public Integer getPostcode() {
        return postcode;
    }
    public void setPostcode(Integer postcode) {
        this.postcode = postcode;
    }
    @Override
    public String toString() {
        return "Address [id=" + id + ", detail=" + detail + ", postcode="
                + postcode + "]";
    }

}
