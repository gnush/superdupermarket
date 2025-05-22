package io.github.gnush.datasource.db;

import io.github.gnush.commodity.ExpirationDate;
import io.github.gnush.currency.Currency;
import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "commodities")
public class CommodityEntity {
    @Id
    @Column(name = "commodity_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private CommodityCategory category = new CommodityCategory();

    @NotNull
    @Column(nullable = false)
    private String label;

    @NotNull
    @Column(nullable = false)
    private String currency;

    @NotNull
    @Column(nullable = false)
    private BigDecimal basePrice;

    @Column(nullable = false)
    private int quality;

    @NotNull
    @Convert(converter = ExpirationDateConverter.class)
    private ExpirationDate expirationDate;

    @NotNull
    @Column(nullable = false)
    private List<String> extraAttributes;

    public CommodityEntity() {
        this.label = "";
        this.currency = "";
        this.basePrice = BigDecimal.ZERO;
        this.expirationDate = ExpirationDate.DoesNotExpire.instance();
        this.extraAttributes = Collections.emptyList();
    }

    public CommodityEntity(@NotNull String label, @NotNull Currency basePrice, int quality, @NotNull ExpirationDate expirationDate) {
        this.label = label;
        this.currency = basePrice.currencyCode();
        this.basePrice = basePrice.amount();
        this.expirationDate = expirationDate;
        this.quality = quality;
        this.extraAttributes = Collections.emptyList();
    }

    public CommodityEntity(@NotNull String category, @NotNull String label, @NotNull Currency basePrice, int quality, @NotNull ExpirationDate expirationDate) {
        this.category = new CommodityCategory(category);
        this.label = label;
        this.currency = basePrice.currencyCode();
        this.basePrice = basePrice.amount();
        this.expirationDate = expirationDate;
        this.quality = quality;
        this.extraAttributes = Collections.emptyList();
    }

    public CommodityEntity(@NotNull String label, @NotNull Currency basePrice, int quality, @NotNull ExpirationDate expirationDate, @NotNull List<String> extraAttributes) {
        this.label = label;
        this.currency = basePrice.currencyCode();
        this.basePrice = basePrice.amount();
        this.expirationDate = expirationDate;
        this.quality = quality;
        this.extraAttributes = extraAttributes;
    }

    public CommodityEntity(@NotNull String category, @NotNull String label, @NotNull Currency basePrice, int quality, @NotNull ExpirationDate expirationDate, @NotNull List<String> extraAttributes) {
        this.category = new CommodityCategory(category);
        this.label = label;
        this.currency = basePrice.currencyCode();
        this.basePrice = basePrice.amount();
        this.expirationDate = expirationDate;
        this.quality = quality;
        this.extraAttributes = extraAttributes;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public @NotNull CommodityCategory getCategory() {
        return category;
    }

    public void setCategory(@NotNull CommodityCategory category) {
        this.category = category;
    }

    public @NotNull String getLabel() {
        return label;
    }

    public void setLabel(@NotNull String label) {
        this.label = label;
    }

    public @NotNull String getCurrency() {
        return currency;
    }

    public void setCurrency(@NotNull String currency) {
        this.currency = currency;
    }

    public @NotNull BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(@NotNull BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public @NotNull ExpirationDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(@NotNull ExpirationDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public @NotNull List<String> getExtraAttributes() {
        return extraAttributes;
    }

    public void setExtraAttributes(@NotNull List<String> extraAttributes) {
        this.extraAttributes = extraAttributes;
    }

    @Override
    public String toString() {
        return "CommodityEntity{" +
                "id=" + id +
                ", category=" + category +
                ", label='" + label + '\'' +
                ", currency='" + currency + '\'' +
                ", basePrice=" + basePrice +
                ", expirationDate=" + expirationDate +
                ", quality=" + quality +
                ", extraAttributes=" + extraAttributes +
                '}';
    }
}
