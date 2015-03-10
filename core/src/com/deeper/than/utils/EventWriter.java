package com.deeper.than.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import com.badlogic.gdx.utils.Json;
import com.deeper.than.Event;
	/* 
	 * Class:	EventWriter.java
	 * Author:	Nick
	 * Purpose:	Write event JSON files
	 */
public class EventWriter {

	public static void main(String[] args) {
		System.out.print("Enter the event title: ");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		String title=null;
		try{
			title = br.readLine();
		} catch (IOException ioe){
			System.err.println("Error reading title...");
			ioe.printStackTrace();
			System.exit(1);
		}
		String text=null;
		System.out.print("Enter the event description: ");
		try{
			text = br.readLine();
		} catch (IOException ioe){
			System.err.println("Error reading text...");
			ioe.printStackTrace();
			System.exit(1);
		}
		ArrayList<String> inputs = new ArrayList<String>();
		String temp="0";
		while(!temp.equals("-1")){
			System.out.print("Enter input response (-1 to exit): ");
			try{
				temp=br.readLine();
				if(!temp.equals("-1")) inputs.add(temp);
			} catch (IOException ioe){
				System.err.println("Error reading inputs...");
				ioe.printStackTrace();
				System.exit(1);
			}
		}
		
		Event event = new Event(title, text, inputs);
		Json json = new Json();
		String output = json.prettyPrint(event);
		System.out.println(output);
		PrintWriter out;
		try {
			out = new PrintWriter("../android/assets/events/"+title+".event");
			out.println(output);
			out.close();
		} catch (FileNotFoundException e) {
			System.err.println("Error: unable to make event file!");
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("File " + title +".event successfully written!");
	}

}
