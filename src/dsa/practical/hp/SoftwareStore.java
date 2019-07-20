

package dsa.practical.hp;


import java.util.*;

public class SoftwareStore {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        SoftwareList softwareList = new SoftwareList();
        int choice;
        String s;
        do {
            System.out.println("=====SOFTWARE STORE=====");
            System.out.println("1. Add a new software");
            System.out.println("2. Purchase a software");
            System.out.println("3. Software List");
            System.out.println("4. Exit");
            do {                
                s = sc.nextLine().trim();
            } while (!s.matches("[1-4]{1}"));
            choice = Integer.parseInt(s);
            switch (choice){
                case 1:
                    softwareList.addNew();
                    break;
                case 2:
                    softwareList.purchase();
                    break;
                case 3:
                    softwareList.displaySoftwareList();
                    break;
                case 4:
                    softwareList.exit();
                    break;
            }
        } while (1 <= choice && choice <=3);
    }

}

