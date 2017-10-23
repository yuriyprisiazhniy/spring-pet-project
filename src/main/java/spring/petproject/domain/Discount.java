package spring.petproject.domain;

public class Discount implements Comparable<Discount>{
    private String reason;
    private byte discount;

    public Discount(String reason, byte discount) {
        this.reason = reason;
        this.discount = discount;
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

        return discount == discount1.discount;
    }

    @Override
    public int hashCode() {
        return (int) discount;
    }

    public String getReason() {
        return reason;
    }

    public byte getDiscount() {
        return discount;
    }
}
