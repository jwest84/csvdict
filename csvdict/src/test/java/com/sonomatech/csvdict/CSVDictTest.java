package com.sonomatech.csvdict;

import com.sonomatech.csvdict.CSVDict;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import junit.framework.TestCase;

public class CSVDictTest extends TestCase {

    private CSVDict speciesDict1;
    private CSVDict speciesDict2;

    public CSVDictTest() {        
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream("species_ivDB.csv")));
            speciesDict1 = new CSVDict(br, ',', 3);
            speciesDict2 = new CSVDict("species_ivDB.csv", ',', 3, CSVDictTest.class.getClassLoader());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }



    public void testConstructor1() throws Exception {
        System.out.println("\n ***** CSVDictTest Test*****");
        System.out.println("CSVDict constructor 1");
        assertEquals(1789, speciesDict1.getRowCount());
    }


    public void testConstructor2() throws Exception {
        System.out.println("CSVDict constructor 2");
        assertEquals(1789, speciesDict2.getRowCount());
    }
    
    public void testOrder() throws Exception {
        String[] specNames = speciesDict2.getColumn("ScientificName");
        assertEquals("Abies alba", specNames[0]);
    }

    public void testGetString() throws Exception {
        System.out.println("getString");
        String result = speciesDict1.getString("Platanus racemosa", 4);
        assertEquals("0.5", result);
    }
    
    public void testGetFloat() throws Exception {
        System.out.println("getFloat");
        float result = speciesDict2.getFloat("Platanus racemosa", 4);
        assertEquals(0.500F, result, 0.0F);
    }
    
    public void testGetInteger() throws Exception {
        System.out.println("getInteger");
        int result = speciesDict1.getInteger("Platanus racemosa", 2);
        assertEquals(4, result);
    }
    
    public void testFilter1() throws Exception {
        String[] filtered = speciesDict2.getFilteredColumn("TSN", "canopy", "2");
        //values in canopy column are either 0 or 1, 
        //so grabbing all TSN values with canopy == 2 should return an empty set
        assertTrue(filtered.length == 0);
    }
    
    public void testFilter2() throws Exception {
        //give me all TSN values where TSN == 28748
        String[] filtered = speciesDict2.getFilteredColumn("TSN", "TSN", "28748");
        //since TSN values are unique, array should only contain 1 value
        //and it should be that value
        assertTrue(filtered.length == 1);
        assertTrue(filtered[0].equals("28748"));
    }
}