package priv.bajdcc.grammar.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import priv.bajdcc.grammar.Grammar;
import priv.bajdcc.lexer.error.RegexException;
import priv.bajdcc.syntax.error.SyntaxException;

@SuppressWarnings("unused")
public class TestGrammar2 {

	public static void main(String[] args) {
		try {
			String[] exprs=new String[]{
			"struct http_stat,",
			"static char *create_authorization_line (const char *, const char ),",
			"char create_authorization_line (const char),",
			"char a[2+7][8/8],",
			"struct request_header { enum rp release_policy,} *headers,",
			"static struct request * request_new (const char *method, char *arg) {}",
			"void main(){req->hcapacity = 8,}",
			"void main(){req->headers = xnew_array (struct request_header, req->hcapacity),}",
			"void m(){a->p = (void *)name,}",
			"void m(){int * a = sizeof(*a),}",
			"const char *p = strchr (header, ':'),",
			"struct { int a, } ,",
			"static bool known_authentication_scheme_p (const char *, const char *),",
			"void m(){const char *p = strchr (header, ':');}",
			"void m(){int *i; size = 0;}",
			"*aaa",
			"void* m(){int *i;}",
			"static int \r\nrequest_send (const int request_req, int fd, int *warc_tmp){char request_string, p;}",
			};
			/*BufferedReader br = new BufferedReader(new FileReader("E:/http.c"));
			String line = "";
			StringBuffer sb = new StringBuffer();
			while ((line = br.readLine()) != null) {
				sb.append(line + System.getProperty("line.separator"));
			}
			br.close();*/
			Grammar grammar = new Grammar(exprs[exprs.length - 1]);
			System.out.println(grammar.toString());
			//FileWriter fw = new FileWriter("E:/testgrammar.txt");
			//fw.append(grammar.toString());
			//fw.close();
		} catch (RegexException e) {
			System.err.println(e.getPosition() + "," + e.getMessage());
			e.printStackTrace();
		} catch (SyntaxException e) {
			System.err.println(e.getPosition() + "," + e.getMessage() + " "
					+ e.getInfo());
			e.printStackTrace();
		//} catch (IOException e) {
		//	System.err.println(e.getMessage());
		//	e.printStackTrace();
		}
	}
}
