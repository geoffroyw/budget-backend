package io.yac.budget.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by geoffroy on 07/02/2016.
 */
@Entity
public class Category extends TimestampableEntity {

    private Long id;

    private String name;

    private List<Transaction> transactions = new ArrayList<>();

    public Category() {
    }

    private Category(Long id, String name, List<Transaction> transactions) {
        this.id = id;
        this.name = name;
        this.transactions = transactions;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_seq")
    @SequenceGenerator(name = "category_seq", sequenceName = "category_seq")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "name", nullable = false, unique = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToMany(mappedBy = "categories")
    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public static class Builder {
        private Long id;
        private String name;
        private List<Transaction> transactions;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder transactions(List<Transaction> transactions) {
            this.transactions = transactions;
            return this;
        }

        public Category build() {
            return new Category(id, name, transactions);
        }
    }
}
