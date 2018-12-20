package br.ita;

import java.io.IOException;
import java.util.Date;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	//AttributeValueReducer avr = new SupervisedRatio();
    	AttributeValueReducer avr = new SymbolicAttributeValueReducer(false);
    	FileAttributeValueReducer reducer = new FileAttributeValueReducer(avr);
    	try {
    		Date begin = new Date();
    		//String path = "E:\\Mushroom\\1\\agaricus-lepiota", separator = ","; 
    		//String trueValue = "p", falseValue = "e", headersFilter = "16";
    		//Integer classIndex = 0;
    		String path = "E:\\Transacoes_637\\10\\transacoes_grupo_637_colunas_selecionadas", separator = ";"; 
    		String trueValue = "1", falseValue = "0", headersFilter = "0;3;10;11;12;13;14;15;17;18;22";
    		Integer classIndex = 21;
    		reducer.generateGroupsForHeaders(path + "_training.csv", separator, true, classIndex, trueValue, falseValue, headersFilter);
    		reducer.groupValues(path + "_training.csv", path + "_training_grouped.csv", separator, true);
    		reducer.groupValues(path + "_test.csv", path + "_test_grouped.csv", separator, true);
    		reducer.groupValues(path + ".csv", path + "_grouped.csv", separator, true);
    		Date end = new Date();
    		System.out.println(end.getTime() - begin.getTime());
    	}
    	catch(IOException e) {
    		e.printStackTrace();
    	}
    }
}
