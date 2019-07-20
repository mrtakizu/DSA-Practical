package dsa.practical.hp;


public class Node {
    private Node left, right;
    private Software software;
    private SoftwareAttribute attributes;
    private int position;

    public Node(Software name, SoftwareAttribute attributes, int pos) {
        this.software = name;
        this.attributes = attributes;
        this.position = pos;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public Software getSoftware() {
        return software;
    }

    public void setSoftware(Software soft) {
        this.software = soft;
    }

    public SoftwareAttribute getAttributes() {
        return attributes;
    }

    public void setAttributes(SoftwareAttribute attributes) {
        this.attributes = attributes;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
    

    @Override
    public String toString() {
        return String.format("%s%12s%10d%10d", getSoftware().getName(),getSoftware().getVersion(),getAttributes().getQuanity(),getAttributes().getPrice());
    }
    
    
    
}

