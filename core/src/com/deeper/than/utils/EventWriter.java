package com.deeper.than.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import com.badlogic.gdx.utils.Json;
import com.deeper.than.DTLEvent;
import com.deeper.than.Response;
	/* 
	 * Class:	EventWriter.java
	 * Author:	Nick
	 * Purpose:	Write event JSON files
	 */
public class EventWriter {
	private static final String PATH = "../android/assets/events/";
	
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
		ArrayList<Response> responses = new ArrayList<Response>();
		String temp="0";
		while(!temp.equals("-1")){
			System.out.print("Enter input response (-1 to exit): ");
			try{
				temp=br.readLine();
				if(temp.equals("-1")) break;
				String nextEvent = null;
				String yn=null;
				System.out.print("Does this response trigger another event?(y/n): ");
				yn=br.readLine();
				if(yn.toLowerCase().startsWith("y")){
					System.out.print("Enter the title of the event it triggers: ");
					nextEvent = br.readLine();
				}
				if(!temp.equals("-1")) responses.add(new Response(temp, nextEvent));
			} catch (IOException ioe){
				System.err.println("Error reading inputs...");
				ioe.printStackTrace();
				System.exit(1);
			}
		}
		
		DTLEvent event = new DTLEvent(title, text, responses);
		Json json = new Json();
		String fileTitle = title.replace(' ', '_');
		String output = json.prettyPrint(event);
		System.out.println(output);
		PrintWriter out;
		try {
			out = new PrintWriter(PATH+fileTitle+".event");
			out.println(output);
			out.close();
		} catch (FileNotFoundException e) {
			System.err.println("Error: unable to make event file!");
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("File " + fileTitle +".event successfully written!");
	}

}
