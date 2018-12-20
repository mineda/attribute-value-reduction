package br.ita;

import java.util.HashMap;
import java.util.Map;

public class Attribute {

	private Map<String, Integer> values;
	private String name;
	private Map<String, Map<String, Integer>> valuesByClass;
	private Float informationGain;
	private Float informationGainRatio;
	private Map<String, Integer> classValues;
	private Integer valueCount = 0;

	public Attribute(String name) {
		this.setValues(new HashMap<String, Integer>());
		this.setValuesByClass(new HashMap<String, Map<String, Integer>>());
		this.setName(name);
		this.classValues = new HashMap<String, Integer>();
	}

	public Attribute() {
		this("noname");
	}

	public void addValue(String value) {
		value = value.trim();
		if (values.containsKey(value)) {
			values.put(value, values.get(value) + 1);
		} else {
			values.put(value, 1);
		}
	}

	public void addValueWithClass(String value, String classValue) {
		value = value.trim();
		valueCount++;
		if (values.containsKey(value)) {
			values.put(value, values.get(value) + 1);
			if (valuesByClass.get(value).containsKey(classValue)) {
				Integer count = valuesByClass.get(value).get(classValue);
				valuesByClass.get(value).put(classValue, count + 1);
			} else {
				valuesByClass.get(value).put(classValue, 1);
			}
		} else {
			values.put(value, 1);
			valuesByClass.put(value, new HashMap<String, Integer>());
			valuesByClass.get(value).put(classValue, 1);
		}
		if (classValues.containsKey(classValue)) {
			classValues.put(classValue, classValues.get(classValue) + 1);
		} else {
			classValues.put(classValue, 1);
		}
	}

	public void addValuesWithClass(String value, String classValue,
			Integer amount) {
		value = value.trim();
		valueCount += amount;
		if (values.containsKey(value)) {
			values.put(value, values.get(value) + amount);
			if (valuesByClass.get(value).containsKey(classValue)) {
				Integer count = valuesByClass.get(value).get(classValue);
				valuesByClass.get(value).put(classValue, count + amount);
			} else {
				valuesByClass.get(value).put(classValue, amount);
			}
		} else {
			values.put(value, amount);
			valuesByClass.put(value, new HashMap<String, Integer>());
			valuesByClass.get(value).put(classValue, amount);
		}
		if (classValues.containsKey(classValue)) {
			classValues.put(classValue, classValues.get(classValue) + amount);
		} else {
			classValues.put(classValue, amount);
		}
	}

	public Integer getValueByClassAmount(String value, String classValue) {
		if (!valuesByClass.get(value).containsKey(classValue)) {
			return 0;
		}
		return valuesByClass.get(value).get(classValue);
	}

	public Integer getValueAmount(String value) {
		if (!values.containsKey(value)) {
			return 0;
		}
		return values.get(value);
	}

	public String[] values() {
		String[] valuesArray = new String[values.keySet().size()];
		return values.keySet().toArray(valuesArray);
	}

	public String[] classes() {
		String[] classesArray = new String[classValues.keySet().size()];
		return classValues.keySet().toArray(classesArray);
	}

	public Integer valueCount(String value) {
		return values.get(value);
	}

	public Float valueProportionByClass(String value, String classValue) {
		if (!valuesByClass.get(value).containsKey(classValue)) {
			return 0f;
		}
		return valuesByClass.get(value).get(classValue) * 1f
				/ values.get(value);
	}

	/**
	 * @return the values
	 */
	public Map<String, Integer> getValues() {
		return values;
	}

	/**
	 * @param values
	 *            the values to set
	 */
	public void setValues(Map<String, Integer> values) {
		this.values = values;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the valuesByClass
	 */
	public Map<String, Map<String, Integer>> getValuesByClass() {
		return valuesByClass;
	}

	/**
	 * @param valuesByClass
	 *            the valuesByClass to set
	 */
	public void setValuesByClass(Map<String, Map<String, Integer>> valuesByClass) {
		this.valuesByClass = valuesByClass;
	}

	/**
	 * @return the informationGain
	 */
	public Float getInformationGain() {
		if (informationGain == null) {
			Float totalEntropy = 0f;
			for (String classValue : classValues.keySet()) {
				if (classValues.get(classValue).equals(0)) {
					return 0f;
				}
				totalEntropy -= 1f * classValues.get(classValue) / valueCount
						* log(1f * classValues.get(classValue) / valueCount, 2);
			}
			Float partialEntropy = 0f;
			for (String value : values.keySet()) {
				Float temp = 0f;
				for (String classValue : valuesByClass.get(value).keySet()) {
					if (valuesByClass.get(value).get(classValue).equals(0)) {
						temp = 0f;
						break;
					}
					temp -= 1f
							* valuesByClass.get(value).get(classValue)
							/ values.get(value)
							* log(1f * valuesByClass.get(value).get(classValue)
									/ values.get(value), 2);
				}
				partialEntropy += 1f * temp * values.get(value) / valueCount;
			}
			informationGain = totalEntropy - partialEntropy;
		}
		return informationGain;
	}

	public Float getEntropyByValue(String value) {
		Float entropy = 0f;
		for (String classValue : valuesByClass.get(value).keySet()) {
			if (valuesByClass.get(value).get(classValue).equals(0)) {
				entropy = 0f;
				break;
			}
			entropy -= 1f
					* valuesByClass.get(value).get(classValue)
					/ values.get(value)
					* log(1f * valuesByClass.get(value).get(classValue)
							/ values.get(value), 2);
		}
		return entropy;
	}
	
	public Float getClassProportion(String classValue) {
		Integer total = 0;
		for(String classVal: classValues.keySet()) {
			total += classValues.get(classVal);
		}
		return 1f * classValues.get(classValue) / total;
	}

	private float log(Float value, Integer base) {
		return (float) (Math.log(value) / Math.log(base));
	}

	/**
	 * @param informationGain
	 *            the informationGain to set
	 */
	public void setInformationGain(Float informationGain) {
		this.informationGain = informationGain;
	}

	/**
	 * @return the informationGainRatio
	 */
	public Float getInformationGainRatio() {
		return informationGainRatio;
	}

	/**
	 * @param informationGainRatio
	 *            the informationGainRatio to set
	 */
	public void setInformationGainRatio(Float informationGainRatio) {
		this.informationGainRatio = informationGainRatio;
	}

}
