package br.ita;


public class SupervisedRatio implements AttributeValueReducer {
	
	public GroupedAttribute group(Attribute attribute, String classValue) {
		GroupedAttribute newAttribute = new GroupedAttribute(attribute.getName());
		for(String value: attribute.values()) {
			String groupName = format(attribute.valueProportionByClass(value, classValue));
			newAttribute.addTranslation(value, groupName);
			for(String classValueAttribute: attribute.classes()) {
				newAttribute.addValuesWithClass(groupName, classValueAttribute, attribute.getValueByClassAmount(value, classValueAttribute));
			}
		}
		newAttribute.setDefaultValue(format(attribute.getClassProportion(classValue)));
		return newAttribute;
	}
	
	private String format(Float number) {
		return String.format("%.10f", number).replace(",", ".");
	}
	
}
