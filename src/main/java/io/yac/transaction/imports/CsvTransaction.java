package io.yac.transaction.imports;

/**
 * Created by geoffroy on 25/06/2016.
 */
public class CsvTransaction {
    private String date;
    private String name;
    private String incomeAmount;
    private String expenseAmount;
    private String isConfirmed;
    private String paymentMeanName;
    private String currency;
    private String bankAccount;

    private String category;

    public CsvTransaction() {
    }

    public CsvTransaction(String date, String name, String incomeAmount, String expenseAmount, String isConfirmed,
                          String paymentMeanName, String currency, String bankAccount, String category) {
        this.date = date;
        this.name = name;
        this.incomeAmount = incomeAmount;
        this.expenseAmount = expenseAmount;
        this.isConfirmed = isConfirmed;
        this.paymentMeanName = paymentMeanName;
        this.currency = currency;
        this.bankAccount = bankAccount;
        this.category = category;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIncomeAmount() {
        return incomeAmount;
    }

    public void setIncomeAmount(String incomeAmount) {
        this.incomeAmount = incomeAmount;
    }

    public String getExpenseAmount() {
        return expenseAmount;
    }

    public void setExpenseAmount(String expenseAmount) {
        this.expenseAmount = expenseAmount;
    }

    public String getIsConfirmed() {
        return isConfirmed;
    }

    public void setIsConfirmed(String isConfirmed) {
        this.isConfirmed = isConfirmed;
    }

    public String getPaymentMeanName() {
        return paymentMeanName;
    }

    public void setPaymentMeanName(String paymentMeanName) {
        this.paymentMeanName = paymentMeanName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public static class Builder {
        private String date;
        private String name;
        private String incomeAmount;
        private String expenseAmount;
        private String isConfirmed;
        private String paymentMeanName;
        private String category;
        private String currency;
        private String bankAccount;

        public Builder date(String date) {
            this.date = date;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder incomeAmount(String incomeAmount) {
            this.incomeAmount = incomeAmount;
            return this;
        }

        public Builder expenseAmount(String expenseAmount) {
            this.expenseAmount = expenseAmount;
            return this;
        }

        public Builder isConfirmed(String isConfirmed) {
            this.isConfirmed = isConfirmed;
            return this;
        }

        public Builder paymentMeanName(String paymentMeanName) {
            this.paymentMeanName = paymentMeanName;
            return this;
        }

        public Builder category(String category) {
            this.category = category;
            return this;
        }

        public Builder currency(String currency) {
            this.currency = currency;
            return this;
        }

        public Builder bankAccount(String bankAccount) {
            this.bankAccount = bankAccount;
            return this;
        }

        public CsvTransaction build() {
            return new CsvTransaction(date, name, incomeAmount, expenseAmount, isConfirmed, paymentMeanName, currency,
                    bankAccount, category);
        }
    }
}
