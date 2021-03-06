package io.yac.categories.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.yac.auth.user.model.User;
import io.yac.bankaccount.domain.TransactionCollectionSerializer;
import io.yac.common.api.View;
import io.yac.common.domain.TimestampableEntity;
import io.yac.transaction.domain.Transaction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by geoffroy on 07/02/2016.
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Category extends TimestampableEntity {

    @JsonView(View.Default.class)
    private Long id;

    @JsonView(View.Default.class)
    private String name;

    @JsonView(View.Default.class)
    @JsonSerialize(using = TransactionCollectionSerializer.class)
    private List<Transaction> transactions = new ArrayList<>();

    private User owner;

    public Category() {
    }

    private Category(Long id, String name, List<Transaction> transactions, User owner) {
        this.id = id;
        this.name = name;
        this.transactions = transactions;
        this.owner = owner;
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

    @ManyToMany(mappedBy = "categories", fetch = FetchType.EAGER)
    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    @Override public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Category category = (Category) o;
        return Objects.equals(id, category.id) &&
                Objects.equals(name, category.name) &&
                Objects.equals(owner, category.owner);
    }

    @Override public int hashCode() {
        return Objects.hash(id, name, owner);
    }

    @Override public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", owner=" + owner +
                '}';
    }

    public static class Builder {
        private Long id;
        private String name;
        private List<Transaction> transactions;
        private User owner;

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

        public Builder owner(User owner) {
            this.owner = owner;
            return this;
        }

        public Category build() {
            return new Category(id, name, transactions, owner);
        }
    }
}
