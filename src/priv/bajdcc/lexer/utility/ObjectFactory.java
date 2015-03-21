package priv.bajdcc.lexer.utility;

import org.vibur.objectpool.PoolObjectFactory;

/**
 * 对象池
 * 
 * @author bajdcc
 * @param T
 *            对象类型
 */
public abstract class ObjectFactory<T> implements PoolObjectFactory<T> {

	public boolean readyToTake(T obj) {
		return true;
	}

	public boolean readyToRestore(T obj) {
		return true;
	}

	public void destroy(T obj) {

	}
}
