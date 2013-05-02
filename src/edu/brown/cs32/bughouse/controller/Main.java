package edu.brown.cs32.bughouse.controller;

import java.util.Scanner;

public class Main {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		try {
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
}
