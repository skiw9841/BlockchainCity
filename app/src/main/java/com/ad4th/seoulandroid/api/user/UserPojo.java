
package com.ad4th.seoulandroid.api.user;

import java.math.BigInteger;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserPojo {

    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("votingCoins")
    @Expose
    private List<VotingCoin> votingCoins = null;
    @SerializedName("coins")
    @Expose
    private List<Coin> coins = null;
    @SerializedName("icxBalance")
    @Expose
    private BigInteger icxBalance;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<VotingCoin> getVotingCoins() {
        return votingCoins;
    }

    public void setVotingCoins(List<VotingCoin> votingCoins) {
        this.votingCoins = votingCoins;
    }

    public List<Coin> getCoins() {
        return coins;
    }

    public void setCoins(List<Coin> coins) {
        this.coins = coins;
    }

    public BigInteger getIcxBalance() {
        return icxBalance;
    }

    public void setIcxBalance(BigInteger icxBalance) {
        this.icxBalance = icxBalance;
    }

}
