package com.pixelite.covidtracker;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

public class DonateActivity extends AppCompatActivity implements View.OnClickListener, PurchasesUpdatedListener {

    private MaterialToolbar toolbar;
    private Button btcButton;
    private Button ethButton;
    private Button moneyButton;

    private String sku = "donate_covid";
    private BillingClient billingClient;
    private List skuList = new ArrayList();
    private SkuDetails mSkuDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);

        toolbar = findViewById(R.id.toolbarDonate);
        btcButton = findViewById(R.id.btc_Donate);
        ethButton = findViewById(R.id.eth_Donate);
        moneyButton = findViewById(R.id.money_Donate);

        skuList.add(sku);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btcButton.setOnClickListener(this);
        ethButton.setOnClickListener(this);

        setupBillingClient();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btc_Donate:
                ClipboardManager btcClipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData btcClipData = ClipData.newPlainText("btcAddress","1JsQbpxCYZG81kp4HeFvYu2foZiBfnRWht");
                btcClipboardManager.setPrimaryClip(btcClipData);
                Toast.makeText(DonateActivity.this, "Copied BTC Wallet Address", Toast.LENGTH_SHORT).show();
                break;

            case R.id.eth_Donate:
                ClipboardManager ethClipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData ethClipData = ClipData.newPlainText("ethAddress","0x3432DA91c839C9315AeeFd5f2da23Ae60ee2aE72");
                ethClipboardManager.setPrimaryClip(ethClipData);
                Toast.makeText(DonateActivity.this, "Copied ETH Wallet Address", Toast.LENGTH_SHORT).show();
                break;

        }
    }

    private void setupBillingClient() {
        billingClient = BillingClient.newBuilder(this).enablePendingPurchases().setListener(this).build();
        billingClient.startConnection(new BillingClientStateListener(){

            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is setup successfully
                    loadAllSKUs();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        });
    }

    private void loadAllSKUs() {
        if (billingClient.isReady())
        {
            SkuDetailsParams params = SkuDetailsParams.newBuilder()
                    .setSkusList(skuList)
                    .setType(BillingClient.SkuType.INAPP)
                    .build();

            billingClient.querySkuDetailsAsync(params, new SkuDetailsResponseListener() {
                @Override
                public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> skuDetailsList) {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
                            && !skuDetailsList.isEmpty())
                    {
                        for (Object skuDetailsObject : skuDetailsList) {
                            final SkuDetails skuDetails = (SkuDetails) skuDetailsObject;

                            if (skuDetails.getSku() == sku)
                                mSkuDetails = skuDetails;
                            moneyButton.setEnabled(true);

                            moneyButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    BillingFlowParams billingFlowParams = BillingFlowParams
                                            .newBuilder()
                                            .setSkuDetails(skuDetails)
                                            .build();
                                    billingClient.launchBillingFlow(DonateActivity.this, billingFlowParams);

                                }
                            });
                        }
                    }
                }
            });
        }
        else
            Toast.makeText(DonateActivity.this, "billingclient not ready", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> purchases) {
        int responseCode = billingResult.getResponseCode();
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
                && purchases != null) {
            for (Purchase purchase : purchases) {
                handlePurchase(purchase);
            }
        }
    }

    private void handlePurchase(Purchase purchase) {
    }
}
