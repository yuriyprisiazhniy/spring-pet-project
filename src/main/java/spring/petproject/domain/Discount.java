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

    public String getReason() {
        return reason;
    }

    public byte getDiscount() {
        return discount;
    }
}
