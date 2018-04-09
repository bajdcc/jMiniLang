package priv.bajdcc.util.lexer.test;

import priv.bajdcc.util.lexer.error.RegexException;
import priv.bajdcc.util.lexer.regex.Regex;

import java.util.Scanner;

public class TestRegex {

	public static void main(String[] args) {
		try {
			Scanner scanner = new Scanner(System.in);
			String str = scanner.nextLine();
			Regex ra = new Regex(str, true);
			String context = scanner.nextLine();
			String match = ra.match(context);
			if (match == null) {
				System.err.println("failed");
			} else {
				System.out.println(match);
			}
			scanner.close();
		} catch (RegexException e) {
			System.err.println(e.getPosition() + "," + e.getMessage());
			e.printStackTrace();
		}
	}
}
