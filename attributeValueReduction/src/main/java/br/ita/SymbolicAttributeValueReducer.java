package br.ita;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SymbolicAttributeValueReducer implements AttributeValueReducer {
	
	public GroupedAttribute group(Attribute attribute, String classValue) {
		Float originalInformationGain = attribute.getInformationGain();
		Float[] probabilities = new Float[attribute.values().length];
		Map<Float, Collection<String>> probabilitiesValues = new HashMap<Float, Collection<String>>();
		Integer count = 0;
		for(String value: attribute.values()) {
			Float probability = attribute.valueProportionByClass(value, classValue);
			probabilities[count++] = probability;
			if(probabilitiesValues.containsKey(probability)) {
				probabilitiesValues.get(probability).add(value);
			}
			else {
				Collection<String> values = new ArrayList<String>();
				values.add(value);
				probabilitiesValues.put(probability, values);
			}
		}
		Arrays.sort(probabilities);
		Float quality = 0f;
		List<Integer> splits = new ArrayList<Integer>();
		splits.add(probabilities.length);
		GroupedAttribute newAttribute = null;
		while(quality < 0.95f) {
			Integer begin = 0;
			List<Integer> newSplits = new ArrayList<Integer>();
			for(Integer split: splits) {
				Integer newSplit = nestedMean(probabilities, begin, split);
				if(newSplit != split && newSplit != begin) {
					newSplits.add(newSplit);
				}
				begin = split;
			}
			splits.addAll(newSplits);
			Collections.sort(splits);
			begin = 0;
			newAttribute = new GroupedAttribute(attribute.getName());
			for(Integer split: splits) {
				System.out.println("Split: " + split);
				String groupName = "{";
				List<String> values = new ArrayList<String>();
				Float lastProbability = -1f;
				for(int i = begin; i < split; i ++) {
					if(!probabilities[i].equals(lastProbability)) {
						for(String value: probabilitiesValues.get(probabilities[i])) {
							System.out.println(probabilities[i] + " - " + value);
							values.add(value);
						}
					}
					lastProbability = probabilities[i];
				}
				for(int i = 0; i < values.size(); i ++) {
					groupName += values.get(i);
					if(i < values.size() - 1) {
						groupName += " ";
					}
				}
				groupName += "}";
				for(String value: values) {
					newAttribute.addTranslation(value, groupName);
					for(String classValueAttribute: attribute.classes()) {
						newAttribute.addValuesWithClass(groupName, classValueAttribute, attribute.getValueByClassAmount(value, classValueAttribute));
					}
				}
				begin = split;
			}
			Float newInformationGain = newAttribute.getInformationGain();
			System.out.println("Grouped Attribute: " + newAttribute.getName());
			System.out.println("IG: " + newAttribute.getInformationGain());
			for(String value: newAttribute.values()) {
				System.out.print(value + ": " + newAttribute.valueCount(value) + "(p: " + newAttribute.getValuesByClass().get(value).get("p") + ", e: " + newAttribute.getValuesByClass().get(value).get("e") + ") - ");
				for(String translationValue: newAttribute.getTranslationValues(value)) {
					System.out.print(translationValue + " ");
				}
				System.out.println();
			}
			quality = newInformationGain / originalInformationGain;
		}
		return newAttribute;
	}
	
	public Integer nestedMean(Float[] origin, Integer begin, Integer end) {
		if(begin >= end) {
			return end;
		}
		Float mean = 0f;
		for(int i = begin; i < end; i++) {
			mean += origin[i];
		}
		mean /= (end - begin);
		for(int i = begin; i < end; i++) {
			if(origin[i] >= mean) {
				return i;
			}
		}
		return end;
	}

}
