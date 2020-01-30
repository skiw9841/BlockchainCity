package com.ad4th.seoulandroid.ICON;

import android.os.Build;

import com.ad4th.seoulandroid.utils.Constants;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import foundation.icon.icx.Call;
import foundation.icon.icx.IconService;
import foundation.icon.icx.KeyWallet;
import foundation.icon.icx.SignedTransaction;
import foundation.icon.icx.Transaction;
import foundation.icon.icx.TransactionBuilder;
import foundation.icon.icx.Wallet;
import foundation.icon.icx.data.Address;
import foundation.icon.icx.data.Bytes;
import foundation.icon.icx.data.IconAmount;
import foundation.icon.icx.data.ScoreApi;
import foundation.icon.icx.transport.http.HttpProvider;
import foundation.icon.icx.transport.jsonrpc.RpcItem;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class ICX {

    private IconService iconService;
    public Wallet wallet;

    public ICX(String _privateKey) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
        iconService = new IconService(new HttpProvider(httpClient, Constants.URL));
        wallet = KeyWallet.load(new Bytes(_privateKey));
    }

    public BigInteger balanceof() throws IOException {
//        Address address = new Address("hx0000000000000000000000000000000000000000");
        Address address = wallet.getAddress();
        BigInteger balance = iconService.getBalance(address).execute();
        System.out.println("balance:" + balance);
        return balance;
    }

    public void transfer(String _toAddress) throws IOException {
        BigInteger networkId = new BigInteger("3");
        Address fromAddress = wallet.getAddress();
        Address toAddress = new Address(_toAddress);
        BigInteger value = IconAmount.of("1", IconAmount.Unit.ICX).toLoop();
//        BigInteger stepLimit = new BigInteger("75000");
        BigInteger stepLimit = getDefaultStepCost().multiply(new BigInteger("2"));
        long timestamp = System.currentTimeMillis() * 1000L;
        BigInteger nonce = new BigInteger("1");

        Transaction transaction = TransactionBuilder.newBuilder()
                .nid(networkId)
                .from(fromAddress)
                .to(toAddress)
                .value(value)
                .stepLimit(stepLimit)
                .timestamp(new BigInteger(Long.toString(timestamp)))
                .nonce(nonce)
                .build();

        SignedTransaction signedTransaction = new SignedTransaction(transaction, wallet);
        Bytes hash = iconService.sendTransaction(signedTransaction).execute();
        System.out.println("txHash:"+hash);
    }


    public BigInteger getDefaultStepCost() throws IOException {
        // APIs that Governance SCORE provides.
        // "getStepCosts" : a table of the step costs for each actions.
        String methodName = "getStepCosts";
        // Check input and output parameters of api if you need
        Map<String, ScoreApi> governanceScoreApiMap = getGovernanceScoreApi();
        ScoreApi api = governanceScoreApiMap.get(methodName);
        System.out.println("[getStepCosts]\ninputs:" + api.getInputs() + "\noutputs:" + api.getOutputs());

        Call<RpcItem> call = new Call.Builder()
                .to(Constants.GOVERNANCE_ADDRESS)
                .method(methodName)
                .build();
        RpcItem result = iconService.call(call).execute();
        return result.asObject().getItem("default").asInteger();
    }


    public Map<String, ScoreApi> getGovernanceScoreApi() throws IOException {
        List<ScoreApi> apis = iconService.getScoreApi(Constants.GOVERNANCE_ADDRESS).execute();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return apis.stream().collect(Collectors.toMap(ScoreApi::getName, api -> api));
        }
        else
        {
            return null;
        }
    }

}
