package priv.bajdcc.LALR1.grammar.runtime.data;

import priv.bajdcc.LALR1.grammar.runtime.RuntimeObject;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 【运行时】运行时数组
 *
 * @author bajdcc
 */
public class RuntimeArray implements Cloneable {

	private List<RuntimeObject> array;

	public RuntimeArray() {
		array = new ArrayList<>();
	}

	public RuntimeArray(RuntimeArray obj) {
		copyFrom(obj);
	}

	public void add(RuntimeObject obj) {
		array.add(obj);
	}

	public boolean set(int index, RuntimeObject obj) {
		if (array == null) {
			return false;
		}
		if (index >= 0 && index < array.size()) {
			array.set(index, obj);
			return true;
		}
		return false;
	}

	public RuntimeObject pop() {
		if (array == null) {
			return null;
		}
		if (array.isEmpty()) {
			return null;
		}
		return array.remove(array.size() - 1);
	}

	public RuntimeObject get(int index) {
		if (index >= 0 && index < array.size()) {
			return array.get(index);
		}
		return null;
	}

	public RuntimeObject size() {
		return new RuntimeObject(BigInteger.valueOf(array.size()));
	}

	public RuntimeObject remove(int index) {
		if (index >= 0 && index < array.size()) {
			return array.remove(index);
		}
		return null;
	}

	public void reverse() {
		Collections.reverse(array);
	}

	public void copyFrom(RuntimeArray obj) {
		array = new ArrayList<>(obj.array);
	}

	public RuntimeArray clone() {
		try {
			return (RuntimeArray) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return new RuntimeArray();
	}

	@Override
	public String toString() {
		return String.valueOf(array.size());
	}
}
