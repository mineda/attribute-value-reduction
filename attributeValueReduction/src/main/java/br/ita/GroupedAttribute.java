package br.ita;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupedAttribute extends Attribute {
	
	Map<String, String> translation;
	
	public GroupedAttribute() {
		this("noname");
	}
	
	public GroupedAttribute(String name) {
		super(name);
		translation = new HashMap<String, String>();
	}
	
	public String getGroup(String value) {
		return translation.get(value);
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

}
