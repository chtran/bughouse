package edu.brown.cs32.bughouse.controller;

import java.util.Scanner;

public class Main {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO don't have this hard coded
		int port = 3333;

		try {
			BughouseServer server = new BughouseServer(port);

			// Listen for any commandline input; quit on "exit" or emptyline
			Scanner scanner = new Scanner(System.in);
			String line = null;
			while (scanner.hasNextLine()) {
				line = scanner.nextLine();
				if (line.length() == 0 || line.equalsIgnoreCase("exit")) {
					server.kill();
					System.exit(0);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
