package priv.bajdcc.lexer.main;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import priv.bajdcc.lexer.lexer.Lexer;
import priv.bajdcc.lexer.token.Token;
import priv.bajdcc.lexer.token.TokenType;

public class Main {

	public static void main(String[] args) {
		String filename = "R:/vmm.cpp";
		try {
			// 读文件
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String line = "";
			StringBuffer sb = new StringBuffer();
			while ((line = br.readLine()) != null) {
				sb.append(line + System.getProperty("line.separator"));
			}
			br.close();
			String context = sb.toString();
			// 词法分析
			Lexer lexer = new Lexer(context);
			Token token;
			PrintStream ps = new PrintStream(new FileOutputStream("R:\\output.txt"));
			for (;;) {
				token = lexer.scan();
				if (token.m_kToken == TokenType.EOF) {
					break;
				}
				ps.println(token.toString());
				ps.flush();
			}
			ps.close();
		} catch (Exception e) {
			System.err.print(e.getMessage());
			e.printStackTrace();
		}
	}
}
