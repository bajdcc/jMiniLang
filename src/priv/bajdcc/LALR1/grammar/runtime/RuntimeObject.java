package priv.bajdcc.LALR1.grammar.runtime;

import java.math.BigDecimal;
import java.math.BigInteger;

import priv.bajdcc.LALR1.grammar.runtime.data.RuntimeFuncObject;

/**
 * 【运行时】运行时对象
 *
 * @author bajdcc
 */
public class RuntimeObject implements Cloneable {

	private Object obj = null;
	private RuntimeObjectType type = RuntimeObjectType.kNull;
	private boolean readonly = false;
	private boolean copyable = true;

	public RuntimeObject(Object obj) {
		this.obj = obj;
		calcTypeFromObject();
	}

	public RuntimeObject(Object obj, RuntimeObjectType type) {
		this.obj = obj;
		this.type = type;
	}

	public RuntimeObject(Object obj, boolean readonly) {
		this.obj = obj;
		this.readonly = readonly;
		calcTypeFromObject();
	}

	public RuntimeObject(Object obj, boolean readonly, boolean copyable) {
		this.obj = obj;
		this.readonly = readonly;
		this.copyable = copyable;
		calcTypeFromObject();
	}
	
	public RuntimeObject(Object obj, RuntimeObjectType type, boolean readonly, boolean copyable) {
		this.obj = obj;
		this.type = type;
		this.readonly = readonly;
		this.copyable = copyable;
	}

	public RuntimeObject(RuntimeObject obj) {
		copyFrom(obj);
	}

	public void copyFrom(RuntimeObject obj) {
		if (obj != null) {
			this.obj = obj.obj;
			this.readonly = obj.readonly;
			this.copyable = obj.copyable;
			this.type = obj.type;
		} else {
			calcTypeFromObject();
		}
	}

	public static RuntimeObject createObject(RuntimeObject obj) {
		if (obj == null) {
			return null;
		}
		return new RuntimeObject(obj);
	}

	public void calcTypeFromObject() {
		type = fromObject(obj);
	}

	public Object getObj() {
		return obj;
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

	public boolean isReadonly() {
		return readonly;
	}

	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
	}

	public boolean isCopyable() {
		return copyable;
	}

	public void setCopyable(boolean copyable) {
		this.copyable = copyable;
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
		if (obj instanceof BigInteger) {
			return RuntimeObjectType.kInt;
		}
		if (obj instanceof BigDecimal) {
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
		return RuntimeObjectType.kNull;
	}

	public RuntimeObject clone() {
		try {
			return (RuntimeObject) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return new RuntimeObject(null);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(type.getName());
		sb.append(obj == null ? "(null)" : "(" + obj.toString() + ")");
		sb.append(readonly ? 'R' : 'r');
		sb.append(copyable ? 'C' : 'c');
		return sb.toString();
	}
}
