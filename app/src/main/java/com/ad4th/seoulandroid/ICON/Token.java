package com.ad4th.seoulandroid.ICON;

import com.ad4th.seoulandroid.utils.Constants;

import java.io.IOException;
import java.math.BigInteger;

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
import foundation.icon.icx.transport.http.HttpProvider;
import foundation.icon.icx.transport.jsonrpc.RpcItem;
import foundation.icon.icx.transport.jsonrpc.RpcObject;
import foundation.icon.icx.transport.jsonrpc.RpcValue;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class Token {

    private static Address tokenAddress;
    private IconService iconService;
    private Wallet wallet;

    public Token(String _tokenAddress, String _privateKey)  {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
        iconService = new IconService(new HttpProvider(httpClient, Constants.URL));
        wallet = KeyWallet.load(new Bytes(_privateKey));
        tokenAddress = new Address(_tokenAddress);
    }

    public Bytes transfer(String _toAddress, String _value) throws IOException {
        BigInteger networkId = new BigInteger(Constants.NETWORK_ID);
        Address fromAddress = wallet.getAddress();
        Address toAddress = new Address(_toAddress);
        BigInteger value = IconAmount.of(_value, 18).toLoop();
        BigInteger stepLimit = new BigInteger("750000");
//        BigInteger stepLimit = getDefaultStepCost().multiply(new BigInteger("2"));
        long timestamp = System.currentTimeMillis() * 1000L;
        BigInteger nonce = new BigInteger("1");
        String methodName = "transfer";

        RpcObject params = new RpcObject.Builder()
                .put("_to", new RpcValue(toAddress))
                .put("_value", new RpcValue(value))
                .build();

        Transaction transaction = TransactionBuilder.newBuilder()
                .nid(networkId)
                .from(fromAddress)
                .to(tokenAddress)
                .stepLimit(stepLimit)
                .timestamp(new BigInteger(Long.toString(timestamp)))
                .nonce(nonce)
                .call(methodName)
                .params(params)
                .build();

        SignedTransaction signedTransaction = new SignedTransaction(transaction, wallet);
        Bytes hash = iconService.sendTransaction(signedTransaction).execute();
        System.out.println("txHash:"+hash);
        return hash;
    }

    public BigInteger balanceof(String address) throws IOException {
        Address fromAddress = new Address(address);

        RpcObject params = new RpcObject.Builder()
                .put("_owner", new RpcValue(fromAddress))
                .build();


        Call<RpcItem> call = new Call.Builder()
                .from(fromAddress)
                .to(tokenAddress)
                .method("balanceOf")
                .params(params)
                .build();
        RpcItem result = iconService.call(call).execute();

        return result.asInteger();
    }
/*
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
        return apis.stream().collect(Collectors.toMap(ScoreApi::getName, api -> api));
    }
*/

    public BigInteger getBlockHeight(Bytes hash) {

        try {
            System.out.println("Height:"+iconService.getTransaction(hash).execute().getBlockHeight() );
        } catch (IOException e) {
            e.printStackTrace();
        }

        BigInteger h = BigInteger.valueOf(0);
        try {
            h = iconService.getTransaction(hash).execute().getBlockHeight();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println("Height:"+block.getHeight() );
        return h;
    }
}
