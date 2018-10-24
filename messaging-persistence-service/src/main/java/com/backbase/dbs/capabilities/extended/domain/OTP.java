package com.backbase.dbs.capabilities.extended.domain;

import com.backbase.buildingblocks.persistence.model.AdditionalPropertiesAware;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;


@Entity
@Table(name = "otp")
public class OTP implements AdditionalPropertiesAware, Persistable<String> {
    @Id
    @GeneratedValue(
            generator = "otp-generator"
    )
    @GenericGenerator(
            name = "otp-generator",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", unique = true)
    private String id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "otp")
    private Integer otp;

    @Column(name = "verified")
    private Boolean verified;

    @ElementCollection(fetch = FetchType.EAGER)
    @MapKeyColumn(name = "property_key")
    @Column(name = "property_value")
    @CollectionTable(name = "ext_otp", joinColumns = @JoinColumn(name = "ext_otp_id"))
    private Map<String, String> additions = new HashMap<>();


    public String getId() {
        return id;
    }

    public void setId(String uuid) {
        this.id = uuid;
    }

    @Override
    public boolean isNew() {
        return id == null;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getOtp() {
        return otp;
    }

    public void setOtp(Integer otp) {
        this.otp = otp;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    @Override
    public Map<String, String> getAdditions() {
        return additions;
    }

    @Override
    public void setAdditions(Map<String, String> additions) {
        this.additions = additions;
    }
}
