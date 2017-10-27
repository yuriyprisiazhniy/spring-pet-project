package spring.petproject.domain;

import spring.petproject.service.DiscountStrategy;

public class Discount implements Comparable<Discount>{
    private String reason;
    private byte discount;
    private DiscountStrategy discountStrategy;

    public Discount(DiscountStrategy discountStrategy, String reason, byte discount) {
        this.discountStrategy = discountStrategy;
        this.reason = reason;
        this.discount = discount;
    }

    public Discount() {
        this.discountStrategy = null;
        this.reason = "No discount";
        this.discount = 0;
    }

    @Override
    public int compareTo(Discount other) {
        if (other == null)
            return 1;
        return Byte.compare(discount, other.getDiscount());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Discount discount1 = (Discount) o;

        if (discount != discount1.discount) return false;
        return discountStrategy != null ? discountStrategy.equals(discount1.discountStrategy) : discount1.discountStrategy == null;
    }

    @Override
    public int hashCode() {
        int result = (int) discount;
        result = 31 * result + (discountStrategy != null ? discountStrategy.hashCode() : 0);
        return result;
    }

    public String getReason() {
        return reason;
    }

    public byte getDiscount() {
        return discount;
    }

    public DiscountStrategy getDiscountStrategy() {
        return discountStrategy;
    }
}
