package com.bajdcc.LALR1.interpret.module;

import com.bajdcc.LALR1.grammar.Grammar;
import com.bajdcc.LALR1.grammar.runtime.*;
import com.bajdcc.util.ResourceLoader;

import java.math.BigInteger;
import java.util.List;

/**
 * 【模块】文件模块
 *
 * @author bajdcc
 */
public class ModuleFile implements IInterpreterModule {

	private static ModuleFile instance = new ModuleFile();
	private RuntimeCodePage runtimeCodePage;

	public static ModuleFile getInstance() {
		return instance;
	}

	@Override
	public String getModuleName() {
		return "sys.file";
	}

	@Override
	public String getModuleCode() {
		return ResourceLoader.load(getClass());
	}

	@Override
	public RuntimeCodePage getCodePage() throws Exception {
		if (runtimeCodePage != null)
			return runtimeCodePage;

		String base = ResourceLoader.load(getClass());

		Grammar grammar = new Grammar(base);
		RuntimeCodePage page = grammar.getCodePage();
		IRuntimeDebugInfo info = page.getInfo();
		buildMethod(info);

		return runtimeCodePage = page;
	}

	private void buildMethod(IRuntimeDebugInfo info) {
		info.addExternalFunc("g_create_file_internal", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "创建文件";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kString, RuntimeObjectType.kInt, RuntimeObjectType.kString};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				String name = args.get(0).getObj().toString();
				int mode = ((BigInteger) args.get(1).getObj()).intValue();
				String encoding = args.get(2).getObj().toString();
				return new RuntimeObject(status.getService().getFileService().create(name, mode, encoding));
			}
		});
		info.addExternalFunc("g_destroy_file_internal", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "关闭文件";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kPtr};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				int handle = (int) args.get(0).getObj();
				return new RuntimeObject(status.getService().getFileService().destroy(handle));
			}
		});
		info.addExternalFunc("g_write_file_internal", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "写文件";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kPtr, RuntimeObjectType.kChar};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				int handle = (int) args.get(0).getObj();
				char ch = (char) args.get(1).getObj();
				return new RuntimeObject(status.getService().getFileService().write(handle, ch));
			}
		});
		info.addExternalFunc("g_write_file_string_internal", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "写文件（字串）";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kPtr, RuntimeObjectType.kString};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				int handle = (int) args.get(0).getObj();
				String str = (String) args.get(1).getObj();
				return new RuntimeObject(status.getService().getFileService().writeString(handle, str));
			}
		});
		info.addExternalFunc("g_read_file_internal", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "读文件";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kPtr};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				int handle = (int) args.get(0).getObj();
				int ch = status.getService().getFileService().read(handle);
				if (ch == -1)
					return null;
				return new RuntimeObject((char) ch);
			}
		});
		info.addExternalFunc("g_read_file_string_internal", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "读文件（行）";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kPtr};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				int handle = (int) args.get(0).getObj();
				String str = status.getService().getFileService().readString(handle);
				return new RuntimeObject(str);
			}
		});
		info.addExternalFunc("g_query_file", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "判断文件是否存在";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kString};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				String filename = String.valueOf(args.get(0).getObj());
				return new RuntimeObject(status.getService().getFileService().exists(filename));
			}
		});
		info.addExternalFunc("g_read_file_vfs_utf8", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "读文件（一次性），限VFS";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kString};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				String filename = String.valueOf(args.get(0).getObj());
				return new RuntimeObject(status.getService().getFileService().readAll(filename));
			}
		});
	}
}
