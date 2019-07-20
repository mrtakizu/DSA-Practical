package dsa.practical.hp;

public class SoftwareAttribute {
    private int quanity, price;

    public SoftwareAttribute(int quanity, int price) {
        this.quanity = quanity;
        this.price = price;
    }

    public int getQuanity() {
        return quanity;
    }

    public void setQuanity(int quanity) {
        this.quanity = quanity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "PackageAttribute{" + "quanity=" + quanity + ", price=" + price + '}';
    }
    
    
}

