package com.bajdcc.util.lexer.main;

import com.bajdcc.util.lexer.Lexer;
import com.bajdcc.util.lexer.token.Token;
import com.bajdcc.util.lexer.token.TokenType;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;

public class Main {

	public static void main(String[] args) {
		String filename = "E:/vmm.cpp";
		try {
			// 读文件
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String line;
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				sb.append(line).append(System.lineSeparator());
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
			for (; ; ) {
				token = lexer.scan();
				if (token == null) {
					continue;
				}
				if (token.getType() == TokenType.EOF) {
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
