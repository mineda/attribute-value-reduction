package br.ita;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileAttributeValueReducer extends SymbolicAttributeValueReducer {
	
	private List<Integer> headersToFilter;
	private Attribute[] attributes;
	private GroupedAttribute[] groupedAttributes;
	private String[] headers;
	
	public FileAttributeValueReducer() {
		setHeadersToFilter(new ArrayList<Integer>());
	}

	public void generateGroupsForHeaders(String fileName, String separator, Boolean hasHeader, Integer classIndex, String trueValue, String falseValue, String headers) throws IOException {
    	String[] indexes = headers.split(separator);
    	for(String index: indexes) {
    		headersToFilter.add(Integer.valueOf(index));
    	}
    	headersToFilter.add(classIndex);
    	generateGroups(fileName, separator, hasHeader, classIndex, trueValue, falseValue);
	}
	
	public void generateGroupsForAllHeaders(String fileName, String separator, Boolean hasHeader, Integer classIndex, String trueValue, String falseValue) throws IOException {
		headersToFilter.add(classIndex);
		generateGroups(fileName, separator, hasHeader, classIndex, trueValue, falseValue);
	}
	
	private void generateGroups(String fileName, String separator, Boolean hasHeader, Integer classIndex, String trueValue, String falseValue) throws IOException {
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
				for(int i = 0; i < attributes.length; i++) {
					attributes[i].addValueWithClass(record[i], record[classIndex]);
				}
			}
		}
		groupedAttributes = new GroupedAttribute[attributes.length];
		for(int i = 0; i < attributes.length; i++) {
			Attribute attribute = attributes[i];
			GroupedAttribute newAttribute = null;
			if(!headersToFilter.contains(i)) {
				System.out.println("Attribute: " + attribute.getName());
				System.out.println("IG: " + attribute.getInformationGain());
				for(String value: attribute.values()) {
					System.out.println(value + ": " + attribute.valueCount(value) + "(" + trueValue + ": " + attribute.getValuesByClass().get(value).get(trueValue) + ", " + falseValue + ": " + attribute.getValuesByClass().get(value).get(falseValue) + ")");
				}
				newAttribute = group(attribute, trueValue);
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
	}
	
	public void groupValues(String inputFileName, String outputFileName, String separator, Boolean hasHeader) throws IOException {
		File file = new File(inputFileName);
		FileReader fileReader = new FileReader(file);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		String header = null;
		if(hasHeader) {
			header = bufferedReader.readLine();
		}
		File outputFile = new File(outputFileName);
		FileWriter fileWriter = new FileWriter(outputFile);
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		if(hasHeader) {
			bufferedWriter.write(header);
			bufferedWriter.newLine();
		}
		String line;
		while((line = bufferedReader.readLine()) != null) {
			String[] record = line.split(separator);
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
		fileReader.close();
	}
	
	public List<Integer> getHeadersToFilter() {
		return headersToFilter;
	}

	public void setHeadersToFilter(List<Integer> headersToFilter) {
		this.headersToFilter = headersToFilter;
	}

	public Attribute[] getAttributes() {
		return attributes;
	}

	public void setAttributes(Attribute[] attributes) {
		this.attributes = attributes;
	}

	public GroupedAttribute[] getGroupedAttributes() {
		return groupedAttributes;
	}

	public void setGroupedAttributes(GroupedAttribute[] groupedAttributes) {
		this.groupedAttributes = groupedAttributes;
	}

	public String[] getHeaders() {
		return headers;
	}

	public void setHeaders(String[] headers) {
		this.headers = headers;
	}


}
