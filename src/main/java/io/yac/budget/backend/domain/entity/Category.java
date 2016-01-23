package io.yac.budget.backend.domain.entity;

import javax.persistence.*;
import java.util.List;

/**
 * Created by geoffroy on 23/01/2016.
 */
@Entity
@Table(name = "category", uniqueConstraints = {@UniqueConstraint(columnNames = "name")},
       indexes = {@Index(columnList = "name", name = "category_name_index")})
public class Category {
    private Long id;
    private String name;
    private List<Transaction> transactions;


    @Id
    @SequenceGenerator(name = "seq_category_id", sequenceName = "seq_category_id", initialValue = 1,
                       allocationSize = 19)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_category_id")
    @Column(name = "id", nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToMany
    @JoinTable(name = "transaction_category",
               joinColumns = {@JoinColumn(name = "category_id", referencedColumnName = "id")},
               inverseJoinColumns = {@JoinColumn(name = "transaction_id", referencedColumnName = "id")})
    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
