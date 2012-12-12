/*
Copyright (c) 2012, Sonoma Technology, Inc.
All rights reserved.

Redistribution and use in source and binary forms, with or without 
modification, are permitted provided that the following conditions 
are met:

    * Redistributions of source code must retain the above copyright 
      notice, this list of conditions and the following disclaimer.

    * Redistributions in binary form must reproduce the above copyright 
      notice, this list of conditions and the following disclaimer in 
      the documentation and/or other materials provided with the 
      distribution.

    * Neither the name of Sonoma Technology, Inc. nor the names of 
      its contributors may be used to endorse or promote products 
      derived from this software without specific prior written 
      permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS 
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS 
FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE 
COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, 
INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL 
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR 
BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF 
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF 
THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF 
SUCH DAMAGE. 
*/



package com.sonomatech.csvdict;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Properties;
import java.util.logging.Logger;
import javax.lang.model.SourceVersion;



/**
 *This class generates classes from CSV files.  It is meant to be used as a
 *command-line tool using the main() method, and is driven by a properties file
 *which lists the CSV files, the index of the key
 *to be used when instancing the generated class that corresponds to the CSV file,
 *and whether the key is an int or a String.  Here is an example of invoking the
 *class generator from an ant build script:
 *<PRE>
 *<code>
 *{@code
 *          <target name="-pre-compile">
 *              <java classname="com.sti.justice.csvdict.ClassSourceGenerator">
 *              <classpath>
 *                  <pathelement location="lib\justice-1.0-SNAPSHOT.jar"/>
 *              </classpath>
 *              <arg line="generated\csg.properties csv generated" />
 *              </java>
 *          </target>
 *}
 *</code>
 *</PRE>
 *The command line arguments designate the properties file, the folder where the CSV files are,
 *and the folder to put the generated classes.
 * 
 *The generated classes define simple accessors to the CSV file.  The field to
 *use as the key is hard-coded by this class generator, and then the desired
 *value of the key is the single argument to the pseudoconstructor of the
 *generated class:<PRE>
 *
 *<code>
 *           int keyVal = getFooID();
 *           MyCSVAccessorFoo fooVars = MyCSVAccessorFoo.getInstance(keyVal);
 *</code>
 *
 *The object contains the values from the selected row of the CSV file, which
 *are accessed directly:
 *<code>
 *          float adjustedWindSpeed = baseWindSpeed * fooVars.windSpeedAdjustmentFactor;
 *</code>
 *
 *Here is an example of the properties file, and below that, an example of a generated class:
 *
 * <FONT color="indianred">
 *# ------------- ClassSourceGenerator properties file ----------------------------

# This properties file contains one line per CSV file. The property name is the
# CSV file name. The property value typically includes three items, separated by
# commas:
#
#       (1) The full classname of the class to be generated
#       (2) The index of the column in the CSV file to be used as the key
#       (3) The type of the key, which must be either "int" or "String"
#
# The client code may need to access a CSV file using more than one key.  In
# that case a class will be generated for each additional key, and the
# property value will include 3 additional comma-separated items for each
# additional key:
#
#       (1) The index of the column in the CSV file to be used as the key
#       (2) The type of the key, which must be either "int" or "String"
#       (3) A string to be appended to the class name already specified.
#           (This must be unique for each additional key.)
#
# The first line below is an example of how to specify generating two classes
# from a CSV file. The first generated class name for woody_ivDB.csv will be
# "WoodyIV" and the data will be accessed with that class using the first
# (zero-based) column as an int key. The second class name will be "WoodyIV_ByName"
# and the data will be accessed with that class using the second column as a String
# key.

woody_ivDB.csv = com.sti.fuelbed.calculators.WoodyIV,0,int,1,String, _ByName
wfa_ivDB.csv = com.sti.fuelbed.calculators.WoodyFuelAccumIV,0,int
basalAccumulations_ivDb.csv = com.sti.fuelbed.calculators.BasalAccumIV,0,int
duff_ivDB.csv = com.sti.fuelbed.calculators.DuffIV,0,int
ladder_ivDB.csv = com.sti.fuelbed.calculators.LadderIV,0,int
lichen_ivDB.csv = com.sti.fuelbed.calculators.LichenIV,0,int
litter_ivDB.csv = com.sti.fuelbed.calculators.LitterIV,0,int
moistureScenarios_ivDB.csv = com.sti.fuelbed.calculators.MoistureScenario,0,int
moss_ivDB.csv = com.sti.fuelbed.calculators.MossIV,0,int
needledrape_ivDB.csv = com.sti.fuelbed.calculators.NeedleDrapeIV,0,int
shrubBioEq.csv = com.sti.fuelbed.calculators.ShrubBioEqIV,0,int
species_ivDB.csv = com.sti.fuelbed.calculators.SpeciesIV,0,int
squirrelMiddens_ivDB.csv = com.sti.fuelbed.calculators.SquirrelMiddensIV,0,int
fccs_summary_calculations.csv = com.sti.fuelbed.calculators.testers.SummaryCalculations,1,int
# -------------------------------------------------------------------
 * </FONT>
 *Here is an example of a generated class:
 *<FONT color ="darkslateblue">
 *package com.sti.fuelbed.calculators;

import com.sti.justice.csvdict.CSVDict.CSVDictException;
import com.sti.justice.csvdict.IntKeyCSVDict;
import java.util.HashMap;


public class BasalAccumIV {

    private static HashMap<Object, BasalAccumIV> cache = new HashMap<Object, BasalAccumIV>();

    public static BasalAccumIV getInstance(int MapID)
        throws CSVDictException
    {
        BasalAccumIV instance = cache.get(MapID);
        if (instance == null) {
            instance = new BasalAccumIV(MapID);
            cache.put(MapID, instance);
        }
        return instance;
    }



    public int MapID;
    public float Wl;
    public float SV;
    public float PD;
    public float HC;

    private BasalAccumIV(int MapID)
        throws CSVDictException
    {
        this.MapID = MapID;
        IntKeyCSVDict dict = new IntKeyCSVDict("basalAccumulations_ivDb.csv", ',', 0);
        Wl = dict.getFloat(MapID, 1);
        SV = dict.getFloat(MapID, 2);
        PD = dict.getFloat(MapID, 3);
        HC = dict.getFloat(MapID, 4);
    }
}
 * </FONT></PRE>
 */



public class RowClassSourceGenerator
{
    private static final Logger logger = Logger.getLogger(RowClassSourceGenerator.class.getSimpleName());
    
    private static String getMethodCall(String type, String idName, int fieldIndex)
    {
        if (type.equals("int"))
            return "dict.getInteger(" + idName + ", " + fieldIndex + ")";
        else if (type.equals("float"))
            return "dict.getFloat(" + idName + ", " + fieldIndex + ")";
        else
            return "dict.getString(" + idName + ", " + fieldIndex + ")";
    }



    // field types are determined as follows:
    //   int:  only if the key type is int and the field is the key field
    //   float: if not int (as above), and if the field can be parsed as a float
    //   String: if not int or float
    private static String[] determimeFieldTypes(CSVDict dict, int keyIndex, boolean isInt, String className)
    {
        String[] fieldNames = dict.getFieldNames();
        String[] types = new String[fieldNames.length];

        for (int i = 0; i < fieldNames.length; i++) {
            if (keyIndex == i)
                types[i] = isInt ? "int" : "String";
            else {
                String[] colVals = dict.getColumn(i);
                for (String val : colVals) {
                    if (val != null && !val.isEmpty()) {
                        try {
                            Float.parseFloat(val);
                            types[i] = "float";
                        } catch(NumberFormatException nfe2) {
                            types[i] = "String";
                        }
                        break;
                    }
                }
                if (types[i] == null)
                    types[i] = "String";
            }
        }

        return types;
    }



    // convert the CSV column header name to a valid Java identifier
    private static String makeVarName(String fName) {
        String varName = fName;
        for (int i = 0; i < fName.length(); i++) {
            char c = fName.charAt(i);
            if (!Character.isDigit(c) && !Character.isLetter(c))
                varName = varName.replace(c, '_');
        }
        while (Character.isDigit(varName.charAt(0)) || SourceVersion.isKeyword(varName))
            varName = "_" + varName;
        return varName;
    }



    // returns the substring that starts with character following the last occurence of the
    // specified character and goes to the end.
    private static String endStr(String str, char prev) {
        return str.substring(str.lastIndexOf(prev) + 1);
    }


    // generates the class from the CSV file
    private static void genClass(String csvFilePath, String generatedFileDir, String fullClassName, int keyIndex, boolean intKey)
    {
        try {
            StringBuilder source = new StringBuilder();
            BufferedReader br = new BufferedReader(new FileReader(csvFilePath));
            CSVDict dict = intKey ? new IntKeyCSVDict(br, ',', keyIndex) : new CSVDict(br, ',', keyIndex);
            String keyName = makeVarName(dict.getFieldNames()[keyIndex]);
            String cArg = intKey ? "int " + keyName : "String " + keyName;
            br.close();
            String shortClassName = endStr(fullClassName, '.');

            source.append("package " + fullClassName.substring(0, fullClassName.lastIndexOf('.')) + ";\n\n");
            source.append("import com.sti.justice.csvdict.CSVDict.CSVDictException;\n");
            if (intKey)
                source.append("import com.sti.justice.csvdict.IntKeyCSVDict;\n");
            else
                source.append("import com.sti.justice.csvdict.CSVDict;\n");
            source.append("import java.util.HashMap;\n");

            source.append("\n\npublic class " + shortClassName + " {\n");

            source.append("\n    public static HashMap<Object, " + shortClassName + "> cache = new HashMap<Object, " + shortClassName + ">();\n");
            if (intKey) {
                source.append("\n    private static final int CUSTOM_START = 5000;");
                source.append("\n    private static int getNext" + keyName + "() {");
                source.append("\n        int ID = CUSTOM_START;");
                source.append("\n        for (Object key : cache.keySet()) {");
                source.append("\n            Integer keyInt = (Integer)key;");
                source.append("\n            if (keyInt > ID)");
                source.append("\n                ID = keyInt;");
                source.append("\n        }");
                source.append("\n        ID += 1;");
                source.append("\n        return ID > CUSTOM_START ? ID : CUSTOM_START;");        
                source.append("\n    }\n"); 

                source.append("\n    synchronized public static int addRow(" + shortClassName + " ms) {");
                source.append("\n        int id = getNext" + keyName + "();");
                source.append("\n        cache.put(id, ms);");
                source.append("\n        return id;");
                source.append("\n    }\n");
            }
            
            source.append("\n    synchronized public static " + shortClassName + " getInstance(" + cArg + ")");
            source.append("\n        throws CSVDictException\n    {");
            source.append("\n        " + shortClassName + " instance = cache.get(" + keyName + ");");
            source.append("\n        if (instance == null) {");
            source.append("\n            instance = new " + shortClassName + "(" + keyName + ");");
            source.append("\n            cache.put(" + keyName + ", instance);");
            source.append("\n        }");
            source.append("\n        return instance;");
            source.append("\n    }\n\n\n\n");

            String[] fieldTypes = determimeFieldTypes(dict, keyIndex, intKey, fullClassName);

            int fCnt = 0;
            for (String fieldName : dict.getFieldNames())
                source.append("    public " + fieldTypes[fCnt++] + " " + makeVarName(fieldName) + ";\n");

            source.append("\n    private " + shortClassName + "(" + cArg + ")\n");
            source.append("        throws CSVDictException\n");
            source.append("    {\n");
            source.append("        this." + keyName + " = " + keyName + ";\n");

            if (intKey)
                source.append("        IntKeyCSVDict dict = new IntKeyCSVDict(\"" + endStr(csvFilePath, File.separatorChar) + "\", ',', " + keyIndex + ");\n");
            else
                source.append("        CSVDict dict = new CSVDict(\"" + endStr(csvFilePath, File.separatorChar) + "\", ',', " + keyIndex + ");\n");

            fCnt = 0;
            for (String memberName : dict.getFieldNames()) {
                if (fCnt != keyIndex) {
                    String dictCall = getMethodCall(fieldTypes[fCnt], keyName, fCnt);
                    source.append("        " + makeVarName(memberName) + " = " + dictCall + ";\n");
                }
                fCnt += 1;
            }

            source.append("    }\n");
            
            source.append("\n    public " + shortClassName + "(){}\n");
            
            source.append("}\n");

            String fullSourceFilePath = generatedFileDir + File.separator + fullClassName.replace(".", File.separator) + ".java";
            String fullSourceDirPath = fullSourceFilePath.substring(0, fullSourceFilePath.lastIndexOf(File.separator));
            new File(fullSourceDirPath).mkdirs();
            PrintWriter pw = new PrintWriter(fullSourceFilePath);
            pw.println(source.toString());
            pw.flush();pw.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }



    /**
     * Generates a set of classes, driven by a properties file
     * @param args <PRE>There are exactly 3 required arguments:
     *  (1) the path to the properties file
     *  (2) the path to the directory that contains the CSV files
     *  (3) the path to the directory where the generated class files will be written
     * </PRE>
     */
    public static void main(String[] args)
    {
        if (args == null || args.length < 3) {
            logger.info("must specify properties file, CSV dir, and generated src dir");
            return;
        }

        String logMsg = "GENERATING SOURCES USING " + args[0];
        Properties props = new Properties();
        String[] csvFileProps = null;
        try {
            props.load(new FileInputStream(args[0]));
            String csvDir = args[1];
            String generatedDir = args[2];
            logMsg += "\n    CSV dir: " + csvDir;
            logMsg += "\n    src dir: " + generatedDir;

            for (String csvFileName : props.stringPropertyNames()) {
                if (!csvFileName.endsWith(".csv"))
                    continue;
                String csvFilePath = csvDir + File.separator + csvFileName;
                csvFileProps = props.getProperty(csvFileName).split(",");
                String fullClassName = csvFileProps[0].trim();
                int pInd = 1;
                while (pInd < csvFileProps.length) {
                    int keyIndex = Integer.parseInt(csvFileProps[pInd++]);
                    boolean intKey = csvFileProps[pInd++].trim().equals("int");
                    String suffixedClassName = fullClassName;
                    if (pInd > 3)
                        suffixedClassName = fullClassName + csvFileProps[pInd++].trim();
                    logMsg += ("\n        GENERATED " + endStr(suffixedClassName, '.') + ".java from " +
                               endStr(csvFilePath, File.separatorChar));
                    genClass(csvFilePath, generatedDir, suffixedClassName, keyIndex, intKey);
                }
            }
            logger.info(logMsg);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
