package com.backbase.dbs.capabilities.extended.domain;

import com.backbase.buildingblocks.persistence.model.AdditionalPropertiesAware;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

/**
 * An example entity implementing {@link com.backbase.buildingblocks.persistence.model.AdditionalPropertiesAware} for API extensions and an analyzed field 'name' for Hibernate Search.
 * Keep identifiers below 30 characters for use with older Oracle Database versions, or 128 bytes if using Oracle 12c Release 2 and later.
 */
@Entity
@Table(name = "messaging")
public class OTP implements AdditionalPropertiesAware, Persistable<String> {
    @Id
    @GeneratedValue(
            generator = "messaging-generator"
    )
    @GenericGenerator(
            name = "messaging-generator",
            strategy = "com.backbase.dbs.capabilities.extended.domain.generator.CustomIdGenerator"
    )
    @Column(name = "id", unique = true)
    private String id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "transaction_type")
    private String transactionType;

    @Column(name = "transaction_value")
    private Integer transactionValue;

    @Column(name = "messaging")
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

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Integer getTransactionValue() {
        return transactionValue;
    }

    public void setTransactionValue(Integer transactionValue) {
        this.transactionValue = transactionValue;
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
