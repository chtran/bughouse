package edu.brown.cs32.bughouse.controller;

import java.util.Scanner;

public class Main {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		try {
			if (!isNumber(args[0])) {
				System.out.println("ERROR: Port must be a position number greater than 1024");
				return;
			}
			
			int port = Integer.parseInt(args[0]);

			BughouseServer server = new BughouseServer(port);

			// Listen for any commandline input; quit on "exit" or emptyline
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
		} finally {
			scanner.close();
		}
	}
	
	/**
	 * Checks if given string is a positive integer greater than 1024
	 * @param num
	 * @return true if num is an integer > 1024, false if not
	 */
	private static boolean isNumber(String num) {
		try {
			int n = Integer.parseInt(num);
			if (n <= 1024)
				return false;
			
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
