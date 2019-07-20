package dsa.practical.hp;

import java.util.*;

public class SoftwareComparator implements Comparator<Software>{

    @Override
    public int compare(Software software1, Software software2) {
        Software s1  = software1;
        Software s2  = software2;
        if(s1.getName().equalsIgnoreCase(s2.getName())){
            return s1.getVersion().compareToIgnoreCase(s2.getVersion());
        }
        return s1.getName().compareToIgnoreCase(s2.getName());
    }

}

