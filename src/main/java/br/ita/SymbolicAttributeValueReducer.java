package br.ita;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SymbolicAttributeValueReducer implements AttributeValueReducer {
	
	private Boolean groupByEntropy = false;
	
	private Float informationLoss = 0.05f;
	
	private Boolean compositeName = false;
	
	public SymbolicAttributeValueReducer(Boolean compositeName) {
		setCompositeName(compositeName);
	}
	
	public SymbolicAttributeValueReducer() {
	}
	
	public GroupedAttribute group(Attribute attribute, String classValue) {
		Float originalInformationGain = attribute.getInformationGain();
		Float[] probabilities = new Float[attribute.values().length];
		Map<Float, Collection<String>> probabilitiesValues = new HashMap<Float, Collection<String>>();
		Integer count = 0;
		for(String value: attribute.values()) {
			Float probability = 0f;
//			System.out.println("Value: " + value + " H: " + attribute.getEntropyByValue(value) 
//					+ " P: " + attribute.valueProportionByClass(value, classValue)
//					+ " Total: " + attribute.getValueAmount(value)
//					+ " " + classValue + ": " + attribute.getValueByClassAmount(value, classValue));
			if(groupByEntropy) {
				probability = attribute.getEntropyByValue(value);
			}
			else {
				probability = attribute.valueProportionByClass(value, classValue);
			}
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
		Float calculatedInformationLoss = 1f;
		List<Integer> splits = new ArrayList<Integer>();
		splits.add(probabilities.length);
		GroupedAttribute newAttribute = null;
		while(calculatedInformationLoss > informationLoss) {
			Integer begin = 0;
			List<Integer> newSplits = new ArrayList<Integer>();
			for(Integer split: splits) {
				Integer newSplit = nestedMean(probabilities, begin, split);
				if(newSplit != split && newSplit != begin) {
					newSplits.add(newSplit);
				}
				begin = split;
			}
			Integer splitsSize = splits.size();
			splits.addAll(newSplits);
			if(splitsSize.equals(splits.size())) {
				throw new UnreachableInformationLossException("Minimum IL (" + calculatedInformationLoss + ") achieved. Target IL: " + informationLoss);
			}
			Collections.sort(splits);
			begin = 0;
			newAttribute = new GroupedAttribute(attribute.getName());
			Integer groupNumber = 1;
			for(Integer split: splits) {
				//System.out.println("Split: " + split);
				String groupName = "{";
				List<String> values = new ArrayList<String>();
				Float lastProbability = -1f;
				for(int i = begin; i < split; i ++) {
					if(!probabilities[i].equals(lastProbability)) {
						for(String value: probabilitiesValues.get(probabilities[i])) {
							//System.out.println(probabilities[i] + " - " + value);
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
				if(!compositeName) {
					groupName = attribute.getName() + groupNumber;
				}
				for(String value: values) {
					newAttribute.addTranslation(value, groupName);
					for(String classValueAttribute: attribute.classes()) {
						newAttribute.addValuesWithClass(groupName, classValueAttribute, attribute.getValueByClassAmount(value, classValueAttribute));
					}
				}
				begin = split;
				groupNumber++;
			}
			Float newInformationGain = newAttribute.getInformationGain();
			//System.out.println("Grouped Attribute: " + newAttribute.getName());
			//System.out.println("Original IG: " + originalInformationGain);
			//System.out.println("IG: " + newInformationGain);
			//for(String value: newAttribute.values()) {
				//System.out.print(value + ": " + newAttribute.valueCount(value) + " - ");
				//for(String translationValue: newAttribute.getTranslationValues(value)) {
					//System.out.print(translationValue + " ");
				//}
				//System.out.println();
			//}
			Float previousCalculatedInformationLoss = calculatedInformationLoss;
			calculatedInformationLoss = 1f - newInformationGain / originalInformationGain;
			if(previousCalculatedInformationLoss.equals(calculatedInformationLoss)) {
				throw new UnreachableInformationLossException("Minimum IL (" + calculatedInformationLoss + ") achieved. Target IL: " + informationLoss);
			}
			//System.out.println("Information loss: " + quality);
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

	public Boolean getGroupByEntropy() {
		return groupByEntropy;
	}

	public void setGroupByEntropy(Boolean groupByEntropy) {
		this.groupByEntropy = groupByEntropy;
	}

	public Float getInformationLoss() {
		return informationLoss;
	}

	public void setInformationLoss(Float informationLoss) {
		this.informationLoss = informationLoss;
	}

	public Boolean getCompositeName() {
		return compositeName;
	}

	public void setCompositeName(Boolean compositeName) {
		this.compositeName = compositeName;
	}

}
