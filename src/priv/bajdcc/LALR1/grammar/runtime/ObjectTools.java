package priv.bajdcc.LALR1.grammar.runtime;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class ObjectTools {
	@SuppressWarnings("unchecked")
	public static <T extends Object> T deserialize(InputStream input) {
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
