package dsa.practical.hp;

import com.sun.xml.internal.ws.util.StringUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.*;

public class SoftwareList {

    private Node root;
    Software software;
    SoftwareAttribute attribute;
    SoftwareComparator comp = new SoftwareComparator();
    String fileSoftware = "softwares.txt";
    int lastPos;
    boolean addNew = true;

    public SoftwareList() {
        this.root = null;
        loadData();
    }

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    public void Add(Software soft, SoftwareAttribute att, int pos) {
        root = Insert(root, soft, att, pos);
    }

    private Node Insert(Node root, Software soft, SoftwareAttribute att, int pos) {
        if (root == null) {
            root = new Node(soft, att, pos);
        } else {
            if (comp.compare(root.getSoftware(), soft) > 0) {
                root.setLeft(Insert(root.getLeft(), soft, att, pos));
            } else if (comp.compare(root.getSoftware(), soft) < 0) {
                root.setRight(Insert(root.getRight(), soft, att, pos));
            } else {
                System.out.println("The quantity of " + soft.getName() + " is updated to " + att.getQuanity());
                root.getAttributes().setQuanity(att.getQuanity());
                updateFile(root.getPosition(), att.getQuanity());
                addNew = false;
            }
        }
        return root;
    }

    private void updateFile(int pos, int newQuantity) {
        try {
            FileReader f = new FileReader(fileSoftware);
            BufferedReader br = new BufferedReader(f);
            String s, updated = "";
            String[] line;
            while ((s = br.readLine()) != null) {
                s = s.trim();
                if (s.length() > 0) {
                    StringTokenizer stk = new StringTokenizer(s, ";");
                    line = s.split(";");
                    if (Integer.parseInt(line[0]) != pos) {
                        updated += s + "\n";
                    } else {
                        line[3] = "" + newQuantity;
                        for (int i = 0; i < line.length; i++) {
                            updated += line[i] + ";";
                        }
                        updated += "\n";
                    }
                }
            }
            br.close();
            f.close();
            FileWriter fw = new FileWriter(fileSoftware);
            fw.write(updated);
            fw.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void loadData() {
        try {
            FileReader f = new FileReader(fileSoftware);
            BufferedReader br = new BufferedReader(f);
            String s;
            while ((s = br.readLine()) != null) {
                s = s.trim();
                if (s.length() > 0) {
                    StringTokenizer stk = new StringTokenizer(s, ";");
                    int pos = Integer.parseInt(stk.nextToken());
                    String name = stk.nextToken();
                    String version = stk.nextToken();
                    int quantity = Integer.parseInt(stk.nextToken());
                    int price = Integer.parseInt(stk.nextToken());
                    Software soft = new Software(name, version);
                    SoftwareAttribute att = new SoftwareAttribute(quantity, price);
                    Add(soft, att, pos);
                    lastPos = pos;
                }
            }
            br.close();
            f.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void displaySoftwareList() {
        if (root == null) {
            System.out.println("Empty!");
        } else {
            System.out.println(String.format("%s%20s%5s%5s", "Software Name", " || Version || ", "Quantity || ", "Price"));
            printList(root);
        }

    }

    private void printList(Node root) {
        if (root != null) {
            printList(root.getLeft());
            System.out.println(root);
            printList(root.getRight());
        }
    }

    private boolean valid(String s) {
        if (!s.matches("[a-zA-Z0-9\\s]+") || s.isEmpty()) {
            return false;
        }
        return true;
    }

    private boolean validNumber(String s) {
        if (!s.matches("\\d+") || s.isEmpty()) {
            return false;
        }
        return true;
    }

    public void addNew() {
        addNew = true;
        Scanner sc = new Scanner(System.in);
        String name, version, quantity, price;
        System.out.println("Enter new Software name:");
        do {
            name = StringUtils.capitalize(sc.nextLine().trim().toLowerCase());
        } while (valid(name));

        System.out.println("Enter new Software version:");
        do {
        version = sc.nextLine().trim();
        } while (valid(version));
        System.out.println("Enter new Software quantity:");
        do {
            quantity = sc.nextLine().trim();
        } while (!validNumber(quantity));
        System.out.println("Enter new Software price:");
        do {
            price = sc.nextLine().trim();
        } while (!validNumber(price));
        Software newSoft = new Software(name, version);
        SoftwareAttribute newAttribute = new SoftwareAttribute(Integer.parseInt(quantity), Integer.parseInt(price));
        Add(newSoft, newAttribute, lastPos);
        if (addNew) {
            lastPos++;
            String s = lastPos + ";" + name + ";" + version + ";" + quantity + ";" + price;
            System.out.println(s);
            saveFile(s);
        }
    }

    private Node LookUp(Node root, Software soft) {
        if (root != null) {
            if (comp.compare(root.getSoftware(), soft) > 0) {
                return LookUp(root.getLeft(), soft);
            }
            if (comp.compare(root.getSoftware(), soft) == 0) {
                return root;
            }
            if (comp.compare(root.getSoftware(), soft) < 0) {
                return LookUp(root.getRight(), soft);
            }
        }
        return null;
    }

    public void purchase() {
        Scanner sc = new Scanner(System.in);
        Node purchased;
        System.out.println("Enter software name to purchase:");
        String name = StringUtils.capitalize(sc.nextLine().trim().toLowerCase());
        System.out.println("Enter software version to purchase:");
        String version = sc.nextLine().trim();
        Software purchaseSoft = new Software(name, version);
        System.out.println(purchaseSoft);
        purchased = LookUp(root, purchaseSoft);
        System.out.println(purchased);
        if (purchased == null) {
            System.out.println("The software is not exitsted!");
        } else {
            System.out.println("Enter number copies of " + purchased.getSoftware());
            System.out.println("Enter quantity to purchase:(Must be greater than 0)");
            String quantity = sc.nextLine().trim();
            int newQuantity = purchased.getAttributes().getQuanity() - Integer.parseInt(quantity);
            if (newQuantity < 0) {
                System.out.println("Not enough copy remain!");
            } else {
                if (newQuantity == 0) {
                    root = Delete(purchased.getSoftware());
                } else {
                    purchased.getAttributes().setQuanity(newQuantity);
                }
                updateFile(purchased.getPosition(), newQuantity);
                System.out.println(purchased.getSoftware() + ", Quantity:" + quantity + " is purchased!");
            }
        }

    }

    private void saveFile(String s) {
        try {
            File f = new File(fileSoftware);
            FileWriter fw = new FileWriter(f, true);
            fw.append("\n" + s);
            fw.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void exit() {
        try {
            int pos;
            List<String> softwareList = new ArrayList<>();
            FileReader f = new FileReader(fileSoftware);
            BufferedReader br = new BufferedReader(f);
            String[] lineContent;
            String s, updated = "";
            while ((s = br.readLine()) != null) {
                softwareList.add(s);
            }
            for (int i = 0; i < softwareList.size(); i++) {
                lineContent = softwareList.get(i).split(";");
                if (Integer.parseInt(lineContent[3]) == 0) {
                    pos = Integer.parseInt(lineContent[0]);
                    softwareList.set(i, softwareList.get(softwareList.size() - 1));
                    softwareList.get(i).replaceFirst(String.valueOf(softwareList.size() - 1), String.valueOf(i));
                    softwareList.remove(softwareList.size() - 1);
                }

            }
            br.close();
            f.close();
            FileWriter fw = new FileWriter(fileSoftware);
            PrintWriter pw = new PrintWriter(fw);
            for (int i = 0; i < softwareList.size(); i++) {
                if (i == softwareList.size() - 1) {
                    pw.print(softwareList.get(i));
                } else {
                    pw.println(softwareList.get(i));
                }
            }
            pw.close();
            fw.close();
            System.exit(0);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //Find the successor(at-most one child - right child)
    private static Node Successor(Node curNode) {
        while (curNode.getLeft() != null) {
            curNode = curNode.getLeft();
        }
        return curNode;
    }

    private Node Delete(Software soft) {
        //Store parent node
        Node parent = null;

        //Start from root
        Node curNode = getRoot();

        //Search in BST list and set parent
        while (curNode != null && comp.compare(curNode.getSoftware(), soft) != 0) {
            //Update parent
            parent = curNode;

            if (comp.compare(curNode.getSoftware(), soft) > 0) {
                curNode = curNode.getLeft();
            } else {
                curNode = curNode.getRight();
            }
        }
        //if software is not found in list
        if (curNode == null) {
            return getRoot();
        }

        // if delete node is a leaf node (no children)
        if (curNode.getLeft() == null && curNode.getRight() == null) {
            //Set the parent point to this node == null
            if (curNode != getRoot()) {
                if (parent.getLeft() == curNode) {
                    parent.setLeft(null);
                } else {
                    parent.setRight(null);
                }
            } //curNode = root (only one element in list)
            else {
                setRoot(null);
            }
        } //if delete node has 2 children
        else if (curNode.getLeft() != null && curNode.getRight() != null) {
            // Find successor of delete node
            Node successor = Successor(curNode.getRight());

            //Store data successor
            Software successorSoftware = successor.getSoftware();
            SoftwareAttribute successorSoftwareAttribute = successor.getAttributes();

            //Delete successor
            Delete(successor.getSoftware());
            System.out.println("Delete Successor");

            //Copy data of successor to current node
            curNode.setSoftware(successorSoftware);
            curNode.setAttributes(successorSoftwareAttribute);
            System.out.println("Copy Data");
        } // if delete node has only one child, replace the node by it's child
        else {
            //Find child node
            Node child = (curNode.getLeft() != null) ? curNode.getLeft() : curNode.getRight();

            if (curNode != getRoot()) {
                if (parent.getLeft() == curNode) {
                    parent.setLeft(child);
                } else {
                    parent.setRight(child);
                }
            } //if delete node is root
            else {
                setRoot(child);

            }
        }
        return getRoot();
    }
}
