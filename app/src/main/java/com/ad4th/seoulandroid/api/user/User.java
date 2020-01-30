
package com.ad4th.seoulandroid.api.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("detailedAddress")
    @Expose
    private String detailedAddress;
    @SerializedName("birthDate")
    @Expose
    private String birthDate;
    @SerializedName("walletAddress")
    @Expose
    private String walletAddress;
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("completedVoting1")
    @Expose
    private Boolean completedVoting1;
    @SerializedName("completedVoting2")
    @Expose
    private Boolean completedVoting2;
    @SerializedName("completedVoting3")
    @Expose
    private Boolean completedVoting3;
    @SerializedName("txId1")
    @Expose
    private String txId1;
    @SerializedName("txId2")
    @Expose
    private String txId2;
    @SerializedName("txId3")
    @Expose
    private String txId3;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDetailedAddress() {
        return detailedAddress;
    }

    public void setDetailedAddress(String detailedAddress) {
        this.detailedAddress = detailedAddress;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getWalletAddress() {
        return walletAddress;
    }

    public void setWalletAddress(String walletAddress) {
        this.walletAddress = walletAddress;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getCompletedVoting1() {
        return completedVoting1;
    }

    public void setCompletedVoting1(Boolean completedVoting1) {
        this.completedVoting1 = completedVoting1;
    }

    public Boolean getCompletedVoting2() {
        return completedVoting2;
    }

    public void setCompletedVoting2(Boolean completedVoting2) {
        this.completedVoting2 = completedVoting2;
    }

    public Boolean getCompletedVoting3() {
        return completedVoting3;
    }

    public void setCompletedVoting3(Boolean completedVoting3) {
        this.completedVoting3 = completedVoting3;
    }

    public String getTxId1() {
        return txId1;
    }

    public void setTxId1(String txId1) {
        this.txId1 = txId1;
    }

    public String getTxId2() {
        return txId2;
    }

    public void setTxId2(String txId2) {
        this.txId2 = txId2;
    }

    public String getTxId3() {
        return txId3;
    }

    public void setTxId3(String txId3) {
        this.txId3 = txId3;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}
