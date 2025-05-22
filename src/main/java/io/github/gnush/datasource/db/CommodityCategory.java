package io.github.gnush.datasource.db;

import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;

@Entity
@Table(name = "categories")
public class CommodityCategory {
    @Id
    @Column(name = "category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Column(nullable = false, unique = true)
    private String name;

    public CommodityCategory() {
        this.name = "";
    }

    public CommodityCategory(@NotNull String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public @NotNull String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "CommodityCategory{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
