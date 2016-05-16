package io.yac.categories.api;


import java.util.List;

/**
 * Created by geoffroy on 07/02/2016.
 */
public class CategoryResource {

    private Long id;

    private String name;

    private List<Long> transactions;

    public CategoryResource() {
    }

    private CategoryResource(Long id, String name,
                             List<Long> transactions) {
        this.id = id;
        this.name = name;
        this.transactions = transactions;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Long> transactions) {
        this.transactions = transactions;
    }

    public static class Builder {
        private Long id;
        private String name;
        private List<Long> transactions;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder transactions(List<Long> transactions) {
            this.transactions = transactions;
            return this;
        }

        public CategoryResource build() {
            return new CategoryResource(id, name, transactions);
        }
    }
}
