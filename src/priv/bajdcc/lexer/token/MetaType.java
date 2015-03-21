package priv.bajdcc.lexer.token;

/**
 * ×Ö·ûÀàÐÍ
 * 
 * @author bajdcc
 */
public enum MetaType {
	CHARACTER('\0'), LPARAN('('), RPARAN(')'), STAR('*'), PLUS('+'), CARET('^'), QUERY(
			'?'), LSQUARE('['), RSQUARE(']'), BAR('|'), ESCAPE('\\'), DASH('-'), LBRACE(
			'{'), RBRACE('}'), COMMA(','), DOT('.'), NEW_LINE('\n'), CARRIAGE_RETURN(
			'\r'), BACKSPACE('\b'), DOUBLE_QUOTE('\"'), SINGLE_QUOTE('\''), END(
			'\0'), ERROR('\0'), NULL('\0');

	private char ch;

	MetaType(char ch) {
		this.ch = ch;
	}

	public char getChar() {
		return ch;
	}

	public void setChar(char ch) {
		this.ch = ch;
	}
}
