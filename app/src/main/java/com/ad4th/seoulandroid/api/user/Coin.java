
package com.ad4th.seoulandroid.api.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;

public class Coin {

    @SerializedName("tokenName")
    @Expose
    private String tokenName;
    @SerializedName("tokenSymbol")
    @Expose
    private String tokenSymbol;
    @SerializedName("balance")
    @Expose
    private BigInteger balance;
    @SerializedName("contractAddress")
    @Expose
    private String contractAddress;

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public String getTokenSymbol() {
        return tokenSymbol;
    }

    public void setTokenSymbol(String tokenSymbol) {
        this.tokenSymbol = tokenSymbol;
    }

    public BigInteger getBalance() {
        return balance;
    }

    public void setBalance(BigInteger balance) {
        this.balance = balance;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

}
