package br.ita;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		String path = "E:\\Transacoes_637\\10\\transacoes_grupo_637_colunas_selecionadas";
		String inputFileName = path + ".csv";
		String training = path + "_training.csv";
		String test = path + "_test.csv";
		
		String line, separator = ";", trueValue = "1", falseValue = "0";
		Integer k = 0, countTrue = 0, countFalse = 0, max = 10, classIndex = 21;
		Boolean hasHeader = true;
		
		try {
		
			File file = new File(inputFileName);
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String header = null;
			if(hasHeader) {
				header = bufferedReader.readLine();
			}
			File outputFileTraining = new File(training);
			FileWriter fileWriterTraining = new FileWriter(outputFileTraining);
			BufferedWriter bufferedWriterTraining = new BufferedWriter(fileWriterTraining);
			File outputFileTest = new File(test);
			FileWriter fileWriterTest = new FileWriter(outputFileTest);
			BufferedWriter bufferedWriterTest = new BufferedWriter(fileWriterTest);
		
			if(hasHeader) {
				bufferedWriterTraining.write(header);
				bufferedWriterTraining.newLine();
				bufferedWriterTest.write(header);
				bufferedWriterTest.newLine();
			}
			
			while((line = bufferedReader.readLine()) != null) {
				String[] record = line.split(separator);
				String classValue = record[classIndex];
				if(classValue.equals(trueValue)) {
					countTrue++;
				}
				else {
					countFalse++;
				}
				if(countTrue.equals(max)) {
					countTrue = 0;
				}
				if(countFalse.equals(max)) {
					countFalse = 0;
				}
				if(classValue.equals(trueValue) && countTrue.equals(k) || 
					classValue.equals(falseValue) && countFalse.equals(k)) {
					bufferedWriterTest.write(line);
					bufferedWriterTest.newLine();				
				}
				else {
					bufferedWriterTraining.write(line);
					bufferedWriterTraining.newLine();				
				}
			}
			bufferedWriterTraining.flush();
			bufferedWriterTraining.close();
			bufferedWriterTest.flush();
			bufferedWriterTest.close();
			fileReader.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}

}
