package br.ita;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupedAttribute extends Attribute {
	
	private Map<String, String> translation;
	private String defaultValue = "N/A";
	
	public GroupedAttribute() {
		this("noname");
	}
	
	public GroupedAttribute(String name) {
		super(name);
		translation = new HashMap<String, String>();
	}
	
	public String getGroup(String value) {
		if(translation.containsKey(value)) {
			return translation.get(value);
		}
		if(defaultValue == null) {
			return value;
		}
		return defaultValue;
	}
	
	public void addTranslation(String value, String group) {
		translation.put(value, group);
	}
	
	public List<String> getTranslationValues(String group) {
		List<String> values = new ArrayList<String>();
		for(String value: translation.keySet()) {
			if(translation.get(value).equals(group)) {
				values.add(value);
			}
		}
		return values;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

}
