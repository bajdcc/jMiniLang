package com.bajdcc.LALR1.grammar.runtime;

import com.bajdcc.LALR1.grammar.runtime.data.RuntimeArray;
import com.bajdcc.LALR1.grammar.runtime.data.RuntimeFuncObject;
import com.bajdcc.LALR1.grammar.runtime.data.RuntimeMap;
import com.bajdcc.util.lexer.token.TokenType;

/**
 * 【运行时】运行时对象
 *
 * @author bajdcc
 */
public class RuntimeObject implements Cloneable {

	private Object obj = null;
	private Object symbol = null;
	private RuntimeObjectType type = RuntimeObjectType.kNull;
	private long[] flag = null;

	public RuntimeObject(Object obj) {
		this.obj = obj;
		calcTypeFromObject();
	}

	public RuntimeObject(Object obj, RuntimeObjectType type) {
		this.obj = obj;
		this.type = type;
	}

	public RuntimeObject(RuntimeObject obj) {
		copyFrom(obj);
	}

	static RuntimeObject createObject(RuntimeObject obj) {
		return new RuntimeObject(obj);
	}

	public static RuntimeObjectType fromObject(Object obj) {
		if (obj == null) {
			return RuntimeObjectType.kNull;
		}
		if (obj instanceof String) {
			return RuntimeObjectType.kString;
		}
		if (obj instanceof Character) {
			return RuntimeObjectType.kChar;
		}
		if (obj instanceof Long) {
			return RuntimeObjectType.kInt;
		}
		if (obj instanceof Double) {
			return RuntimeObjectType.kReal;
		}
		if (obj instanceof Boolean) {
			return RuntimeObjectType.kBool;
		}
		if (obj instanceof Integer) {
			return RuntimeObjectType.kPtr;
		}
		if (obj instanceof RuntimeFuncObject) {
			return RuntimeObjectType.kFunc;
		}
		if (obj instanceof RuntimeArray) {
			return RuntimeObjectType.kArray;
		}
		if (obj instanceof RuntimeMap) {
			return RuntimeObjectType.kMap;
		}
		return RuntimeObjectType.kObject;
	}

	private void calcTypeFromObject() {
		type = fromObject(obj);
	}

	public Object getObj() {
		return obj;
	}

	static TokenType toTokenType(RuntimeObjectType obj) {
		switch (obj) {
			case kBool:
				return TokenType.BOOL;
			case kChar:
				return TokenType.CHARACTER;
			case kInt:
				return TokenType.INTEGER;
			case kReal:
				return TokenType.DECIMAL;
			case kString:
				return TokenType.STRING;
			case kPtr:
				return TokenType.POINTER;
			default:
				return TokenType.ERROR;
		}
	}

	void copyFrom(RuntimeObject obj) {
		if (obj != null) {
			this.obj = obj.obj; // 注意：这里是浅拷贝！
			this.type = obj.type;
			this.symbol = obj.symbol;
			if (obj.flag != null)
				this.flag = obj.flag;
			else
				this.flag = obj.flag = new long[1];
		} else {
			calcTypeFromObject();
		}
	}

	public int getInt() {
		if (type == RuntimeObjectType.kInt)
			return (int) (long) obj;
		return (int) obj;
	}

	public long getLong() {
		if (type == RuntimeObjectType.kPtr)
			return (long) (int) obj;
		return (long) obj;
	}

	public double getDouble() {
		return (double) obj;
	}

	public RuntimeArray getArray() {
		return (RuntimeArray) obj;
	}

	public RuntimeMap getMap() {
		return (RuntimeMap) obj;
	}

	public String getString() {
		return (String) obj;
	}

	public String getTypeName() {
		return fromObject(obj).getName();
	}

	public boolean getBool() {
		return (boolean) obj;
	}

	public String getTypeString() {
		return fromObject(obj).toString();
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public RuntimeObjectType getType() {
		return type;
	}

	public void setType(RuntimeObjectType type) {
		this.type = type;
	}

	public void setSymbol(Object symbol) {
		this.symbol = symbol;
	}

	public Object getSymbol() {
		return this.symbol;
	}

	public long getFlag() {
		return flag == null ? 0 : flag[0];
	}

	public void setFlag(long flag) {
		if (this.flag == null)
			this.flag = new long[1];
		this.flag[0] = flag;
	}

	public char getChar() {
		return (char) obj;
	}

	public long getTypeIndex() {
		return fromObject(obj).ordinal();
	}

	public RuntimeObject clone() {
		if (obj == null) {
			return new RuntimeObject(null);
		}
		RuntimeObject o = null;
		try {
			o = (RuntimeObject) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		if (obj instanceof RuntimeArray) {
			return new RuntimeObject(new RuntimeArray((RuntimeArray) obj)); // 引用类型要创建
		}
		if (obj instanceof RuntimeMap) {
			return new RuntimeObject(new RuntimeMap((RuntimeMap) obj)); // 引用类型要创建
		}
		return o;
	}

	private static boolean equals_flag(long a[], long b[]) {
		if (a == null || b == null)
			return false;
		return a[0] == b[0];
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj instanceof RuntimeObject) {
			RuntimeObject o = (RuntimeObject) obj;
			if (this.obj == null)
				return o.obj == null && this.type.equals(o.type) && equals_flag(this.flag, o.flag);
			if (this.type.equals(o.type) && equals_flag(this.flag, o.flag)) {
				return this.obj.equals(o.obj);
			}
			return false;
		}
		return super.equals(obj);
	}
	@Override
	public int hashCode() {
		return obj == null ? super.hashCode() : obj.hashCode();
	}

	@Override
	public String toString() {
		return " " + String.valueOf(symbol) + " " + type.getName() +
				("(" + String.valueOf(obj) + ")") +
				(flag == null ? "" : (flag[0] == 0L ? "" : ("#" + String.valueOf(flag[0]))));
	}
}
