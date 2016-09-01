package priv.bajdcc.LALR1.grammar.runtime;

/**
 * 【虚拟机】运行时异常
 * 
 * @author bajdcc
 *
 */
@SuppressWarnings("serial")
public class RuntimeException extends Exception {

	public enum RuntimeError {
		EXIT("程序退出"), NULL("未知"), WRONG_CODEPAGE("代码页错误"), WRONG_INST("非法指令"), WRONG_COROUTINE(
				"协程错误"), NULL_STACK("堆栈为空"), NULL_QUEUE("队列为空"), WRONG_OPERATOR(
				"非法操作"), DUP_PAGENAME("代码页名称冲突"), READONLY_VAR("变量不可修改"), WRONG_IMPORT(
				"导入模块名错误"), WRONG_EXPORT("导出模块名错误"), WRONG_LOAD_EXTERN(
				"导入外部符号错误"), WRONG_ARGCOUNT("参数个数不一致"), WRONG_ARGTYPE("参数类型不一致"), NULL_OPERATOR(
				"空值运算"), WRONG_FUNCNAME("过程不存在"), STACK_OVERFLOW("堆栈溢出"), ARG_OVERFLOW(
				"参数过多"), UNDEFINED_CONVERT("未定义的强制转换"), INVALID_INDEX("索引无效"),
				PROCESS_OVERFLOW("进程数量已达最大值"), EMPTY_CALLSTACK("调用栈为空");

		private String message;

		RuntimeError(String message) {
			this.message = message;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
	}

	public RuntimeException(RuntimeError kError, int position, String info) {
		this.position = position;
		this.kError = kError;
		this.info = info;
	}

	private int position = -1;
	private RuntimeError kError = RuntimeError.NULL;
	private String info = "";

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public RuntimeError getkError() {
		return kError;
	}

	public void setkError(RuntimeError kError) {
		this.kError = kError;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
}
