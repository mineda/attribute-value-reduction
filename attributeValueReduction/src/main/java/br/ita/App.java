package br.ita;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	FileAttributeValueReducer reducer = new FileAttributeValueReducer();
    	try {
    		reducer.generateGroupsForHeaders("E:\\Mushroom\\agaricus-lepiota-training.csv", ",", true, 0, "p", "e", "0");
    		reducer.groupValues("E:\\Mushroom\\agaricus-lepiota-training.csv", "E:\\Mushroom\\agaricus-lepiota-training-grouped.csv", ",", true);
    		reducer.groupValues("E:\\Mushroom\\agaricus-lepiota-test.csv", "E:\\Mushroom\\agaricus-lepiota-test-grouped.csv", ",", true);
    	}
    	catch(IOException e) {
    		e.printStackTrace();
    	}
    }
}
