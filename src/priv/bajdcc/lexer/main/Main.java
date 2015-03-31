package priv.bajdcc.lexer.main;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;

import priv.bajdcc.lexer.Lexer;
import priv.bajdcc.lexer.token.Token;
import priv.bajdcc.lexer.token.TokenType;

public class Main {

	public static void main(String[] args) {
		String filename = "E:/vmm.cpp";
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
			lexer.discard(TokenType.COMMENT);
			lexer.discard(TokenType.WHITESPACE);
			lexer.discard(TokenType.ERROR);
			Token token;
			PrintStream ps = new PrintStream(new FileOutputStream(
					"E:\\output.txt"));
			for (;;) {
				token = lexer.scan();
				if (token == null) {
					continue;
				}
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
