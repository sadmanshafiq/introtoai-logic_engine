package com.company;
import java.util.Scanner;
import java.io.*;
public class Main {

	public static void main(String[] args) throws FileNotFoundException {
		System.out.println("Please enter your engine method and file name.");
		System.out.println("In this format: iengine Method <Filename>");

		Scanner scanner = new Scanner(System.in);
		String engineInformation = scanner.nextLine();
        String tell = "";
        String ask = "";

		String[] splitStr = engineInformation.trim().split("\\s+");
		if (splitStr.length < 3) {
			System.out.println("Wrong Command, exiting...");
		}
		else {
            String method = splitStr[1].toUpperCase();
            String fileName = splitStr[2];
            try{
                File file = new File(fileName);
                Scanner sr = new Scanner(file);

                while (sr.hasNextLine()) {
                    String txt = sr.nextLine();
                    if (txt.contains("TELL") ) {
                        tell = sr.nextLine();
                    }
                    else if (txt.contains("ASK") ) {
                        ask = sr.nextLine();
                    }

                }

            }
            catch (FileNotFoundException e)
            {
                System.out.println("File not found");

            }

            switch (method) {
                case "TT":
                    TruthTable tt = new TruthTable(ask, tell);
                    System.out.println(tt.execute());
                    break;
                case "BC":
                    BackwardChaining bc = new BackwardChaining(ask, tell);
                    System.out.println(bc.execute());
                    break;

                case "FC":
                    ForwardChaining fc = new ForwardChaining(ask, tell);
                    System.out.println(fc.execute());
                    break;
            }
        }


/*
		//String methodFormating = splitStr[1].toUpperCase();
		//String fileNameR = "\"" + splitStr[2] + "\"";
		//FileReader fr = new FileReader(fileNameR);
		//BufferedReader br = new BufferedReader(fr);
*/
	// will need to pass the file name in the argument section of the project for intellij.


	}
}
