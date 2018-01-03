package br.ita;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	String fileName = "D:\\Mushroom\\agaricus-lepiota.txt";
    	String output = "D:\\Mushroom\\agaricus-lepiota-grouped.txt";
    	String separator = ",";
    	Integer classIndex = 0;
    	String trueValue = "p";
    	String falseValue = "e";
    	Boolean hasHeader = true;
        List<String[]> input = new ArrayList<String[]>();
        Attribute[] attributes = null;
        try {
			File file = new File(fileName);
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line, lineHeader = null;
			String[] header = null;
			if(hasHeader) {
				lineHeader = bufferedReader.readLine();
				header = lineHeader.split(separator);
				attributes = new Attribute[header.length];
				for(int i = 0; i < attributes.length; i++) {
					attributes[i] = new Attribute(header[i]);
				}
			}
			while((line = bufferedReader.readLine()) != null) {
				String[] record = line.split(separator);
				if(!hasHeader && attributes == null) {
					attributes = new Attribute[record.length];
					for(int i = 0; i < attributes.length; i++) {
						attributes[i] = new Attribute();
					}
				}
				if(record.length == attributes.length) {
					input.add(record);
					for(int i = 0; i < attributes.length; i++) {
						attributes[i].addValueWithClass(record[i], record[classIndex]);
					}
				}
				//System.out.println(line);
			}
			GroupedAttribute[] groupedAttributes = new GroupedAttribute[attributes.length];
			for(int i = 0; i < attributes.length; i++) {
				Attribute attribute = attributes[i];
				GroupedAttribute newAttribute = null;
				if(i != classIndex) {
					System.out.println("Attribute: " + attribute.getName());
					System.out.println("IG: " + attribute.getInformationGain());
					for(String value: attribute.values()) {
						System.out.println(value + ": " + attribute.valueCount(value) + "(" + trueValue + ": " + attribute.getValuesByClass().get(value).get(trueValue) + ", " + falseValue + ": " + attribute.getValuesByClass().get(value).get(falseValue) + ")");
					}
					Grouping grouping = new Grouping();
					newAttribute = grouping.group(attribute, trueValue);
	//				System.out.println("Grouped Attribute: " + newAttribute.getName());
	//				System.out.println("IG: " + newAttribute.getInformationGain());
	//				for(String value: newAttribute.values()) {
	//					System.out.println(value + ": " + newAttribute.valueCount(value) + "(p: " + newAttribute.getValuesByClass().get(value).get("p") + ", e: " + newAttribute.getValuesByClass().get(value).get("e") + ")");
	//				}
				}
				else {
					newAttribute = new GroupedAttribute(attribute.getName());
					for(String value: attribute.values()) {
						newAttribute.addTranslation(value, value);
					}
				}
				groupedAttributes[i] = newAttribute;
			}
			fileReader.close();
			
			File outputFile = new File(output);
			FileWriter fileWriter = new FileWriter(outputFile);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			if(hasHeader) {
				bufferedWriter.write(lineHeader);
				bufferedWriter.newLine();
			}
			for(String[] record: input) {
				for(int i = 0; i < record.length; i++) {
					String item = record[i];
					bufferedWriter.write(groupedAttributes[i].getGroup(item));
					if(i < record.length - 1) {
						bufferedWriter.write(separator);
					}
				}
				bufferedWriter.newLine();
			}
			bufferedWriter.flush();
			bufferedWriter.close();
        }
        catch(Exception e) {
        	e.printStackTrace();
        }
    }
}
