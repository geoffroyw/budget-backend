package io.yac.budget.domain;

/**
 * Created by geoffroy on 07/02/2016.
 */
public enum TransactionType {
    INCOME("INCOME"), EXPENSE("EXPENSE");

    private String externalName;

    TransactionType(String externalName) {

        this.externalName = externalName;
    }

    public static TransactionType fromExternalName(String externalName) {
        for (TransactionType transactionType : TransactionType.values()) {
            if (transactionType.getExternalName().equalsIgnoreCase(externalName)) {
                return transactionType;
            }

        }
        return null;
    }

    public String getExternalName() {
        return externalName;
    }
}
