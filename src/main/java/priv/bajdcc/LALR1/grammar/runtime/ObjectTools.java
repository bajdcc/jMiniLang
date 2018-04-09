package priv.bajdcc.LALR1.grammar.runtime;

import java.io.*;

public class ObjectTools {
	@SuppressWarnings("unchecked")
	public static <T> T deserialize(InputStream input) {
		try {
			ObjectInputStream in = new ObjectInputStream(input);
			return (T) in.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void serialize(Object object, OutputStream output) {
		try {
			ObjectOutputStream out = new ObjectOutputStream(output);
			out.writeObject(object);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
