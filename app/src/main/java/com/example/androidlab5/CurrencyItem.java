package com.example.androidlab5;

public class CurrencyItem {
    private String targetCurrency;
    private String exchangeRate;

    public CurrencyItem(String targetCurrency, String exchangeRate) {
        this.targetCurrency = targetCurrency;
        this.exchangeRate = exchangeRate;
    }

    public String getTargetCurrency() {
        return targetCurrency;
    }

    public String getExchangeRate() {
        return exchangeRate;
    }

    @Override
    public String toString() {
        return targetCurrency + " - " + exchangeRate;
    }
}