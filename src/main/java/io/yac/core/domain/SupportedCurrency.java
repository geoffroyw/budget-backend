package io.yac.core.domain;

/**
 * Created by geoffroy on 20/02/2016.
 */
public enum SupportedCurrency {
    EUR(true, "EUR"),
    USD(true, "USD"),
    JPY(false, "JPY"),
    BGN(false, "BGN"),
    CZK(false, "CZK"),
    DKK(false, "DKK"),
    GBP(true, "GBP"),
    HUF(false, "HUF"),
    PLN(false, "PLN"),
    RON(false, "RON"),
    SEK(true, "SEK"),
    CHF(true, "CHF"),
    NOK(true, "NOK"),
    HRK(false, "HRK"),
    RUB(false, "RUB"),
    TRY(false, "TRY"),
    AUD(false, "AUD"),
    BRL(false, "BRL"),
    CAD(false, "CAD"),
    CNY(false, "CNY"),
    HKD(false, "HKD"),
    IDR(false, "IDR"),
    ILS(false, "ILS"),
    INR(false, "INR"),
    KRW(false, "KRW"),
    MXN(false, "MXN"),
    MYR(false, "MYR"),
    NZD(false, "NZD"),
    PHP(false, "PHP"),
    SGD(false, "SGD"),
    THB(false, "THB"),
    ZAR(false, "ZAR");

    private final boolean isMajor;
    private final String externalName;

    SupportedCurrency(boolean isMajor, String externalName) {

        this.isMajor = isMajor;
        this.externalName = externalName;
    }

    public static SupportedCurrency fromExternalName(String currency) {
        for (SupportedCurrency supportedCurrency : SupportedCurrency.values()) {
            if (supportedCurrency.getExternalName().equalsIgnoreCase(currency)) {
                return supportedCurrency;
            }
        }
        return null;
    }

    public boolean isMajor() {
        return isMajor;
    }

    public String getExternalName() {
        return externalName;
    }
}
