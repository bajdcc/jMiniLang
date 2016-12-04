package priv.bajdcc.LALR1.interpret.os.user.routine;

import priv.bajdcc.LALR1.interpret.os.IOSCodePage;

/**
 * 【用户态】脚本解释器
 *
 * @author bajdcc
 */
public class URShell implements IOSCodePage {
	@Override
	public String getName() {
		return "/usr/p/sh";
	}

	@Override
	public String getCode() {
		return "import \"sys.base\";\n" +
				"import \"sys.list\";\n" +
				"import \"sys.string\";\n" +
				"import \"sys.proc\";\n" +
				"\n" +
				"// PRINT WELCOME\n" +
				"var welcome = func [\"WELCOME\"] ~() {\n" +
				"    call g_println();\n" +
				"    call g_printn(\" ________  ________        ___  ________  ________  ________     \");\n" +
				"    call g_printn(\"|\\\\   __  \\\\|\\\\   __  \\\\      |\\\\  \\\\|\\\\   ___ \\\\|\\\\   ____\\\\|\\\\   ____\\\\    \");\n" +
				"    call g_printn(\"\\\\ \\\\  \\\\|\\\\ /\\\\ \\\\  \\\\|\\\\  \\\\     \\\\ \\\\  \\\\ \\\\  \\\\_|\\\\ \\\\ \\\\  \\\\___|\\\\ \\\\  \\\\___|    \");\n" +
				"    call g_printn(\" \\\\ \\\\   __  \\\\ \\\\   __  \\\\  __ \\\\ \\\\  \\\\ \\\\  \\\\ \\\\\\\\ \\\\ \\\\  \\\\    \\\\ \\\\  \\\\       \");\n" +
				"    call g_printn(\"  \\\\ \\\\  \\\\|\\\\  \\\\ \\\\  \\\\ \\\\  \\\\|\\\\  \\\\\\\\_\\\\  \\\\ \\\\  \\\\_\\\\\\\\ \\\\ \\\\  \\\\____\\\\ \\\\  \\\\____  \");\n" +
				"    call g_printn(\"   \\\\ \\\\_______\\\\ \\\\__\\\\ \\\\__\\\\ \\\\________\\\\ \\\\_______\\\\ \\\\_______\\\\ \\\\_______\\\\\");\n" +
				"    call g_printn(\"    \\\\|_______|\\\\|__|\\\\|__|\\\\|________|\\\\|_______|\\\\|_______|\\\\|_______|\");\n" +
				"    call g_println();\n" +
				"};\n" +
				"call g_join_process(call g_create_user_process(welcome));\n" +
				"\n" +
				"var parse_cmd_1 = func [\"PARSE\"] ~(arg) {\n" +
				"    var pid = call g_get_pid();\n" +
				"    var parse = call g_map_get(arg, \"parse\");\n" +
				"    var cmd = call g_map_get(arg, \"args\");\n" +
				"    var parent = call g_map_get(arg, \"parent\");\n" +
				"    var exe = call g_array_pop(cmd);\n" +
				"    let exe = call g_string_trim(exe);\n" +
				"    var args = call g_string_split(exe, \" \");\n" +
				"    var exec = call g_array_get(args, 0);\n" +
				"    call g_array_remove(args, 0);\n" +
				"    var share = {};\n" +
				"    call g_map_put(share, \"args\", args);\n" +
				"    var path = \"/usr/p/\" + exec;\n" +
				"    var child = call g_load_user_x(path);\n" +
				"    call g_start_share(\"PID#\" + child, share);\n" +
				"    if (child+1 == 0) {\n" +
				"        call g_printn(\"Cannot execute '\"+path+\"'.\");\n" +
				"        var p = call g_wait_pipe(\"PIPEIN#\" + parent);\n" +
				"        call g_sleep(50);\n" +
				"        call g_destroy_pipe(p);\n" +
				"        return;\n" +
				"    }\n" +
				"    call g_map_put(share, \"child\", child);\n" +
				"    if (call g_array_size(cmd) > 0) {\n" +
				"        var _args_ = {};\n" +
				"        call g_map_put(_args_, \"parse\", parse);\n" +
				"        call g_map_put(_args_, \"args\", cmd);\n" +
				"        call g_map_put(_args_, \"parent\", child);\n" +
				"        call g_create_user_process_args(parse, _args_);\n" +
				"    }\n" +
				"    var in = call g_create_pipe(\"PIPEIN#\" + parent);\n" +
				"    var out = call g_create_pipe(\"PIPEOUT#\" + child);\n" +
				"    var f1 = func ~(ch, in) {\n" +
				"        call g_write_pipe(in, ch);\n" +
				"    };\n" +
				"    call g_read_pipe_args(out, f1, in);\n" +
				"    call g_destroy_pipe(in);\n" +
				"};\n" +
				"\n" +
				"var parse_cmd = func [\"PARSE\"] ~(cmd, parse) {\n" +
				"    var pid = call g_get_pid();\n" +
				"    var exe = call g_array_pop(cmd);\n" +
				"    let exe = call g_string_trim(exe);\n" +
				"    var args = call g_string_split(exe, \" \");\n" +
				"    var exec = call g_array_get(args, 0);\n" +
				"    call g_array_remove(args, 0);\n" +
				"    var share = {};\n" +
				"    call g_map_put(share, \"args\", args);\n" +
				"    var path = \"/usr/p/\" + exec;\n" +
				"    var child = call g_load_user_x(path);\n" +
				"    if (child+1 == 0) {\n" +
				"        call g_printn(\"Cannot execute '\"+path+\"'.\");\n" +
				"        return;\n" +
				"    }\n" +
				"    call g_start_share(\"PID#\" + child, share);\n" +
				"    if (call g_array_size(cmd) > 0) {\n" +
				"        var _args_ = {};\n" +
				"        call g_map_put(_args_, \"parse\", parse);\n" +
				"        call g_map_put(_args_, \"args\", cmd);\n" +
				"        call g_map_put(_args_, \"parent\", child);\n" +
				"        call g_create_user_process_args(parse, _args_);\n" +
				"    }\n" +
				"    var f = func ~(ch) -> call g_print(ch);\n" +
				"    var out = call g_create_pipe(\"PIPEOUT#\" + child);\n" +
				"    call g_read_pipe(out, f);\n" +
				"};\n" +
				"\n" +
				"// GET STDIO cmd\n" +
				"var get_input = func [\"INPUT\"] ~(arg) {\n" +
				"    var this = call g_array_get(arg, 0);\n" +
				"    var parse = call g_array_get(arg, 1);\n" +
				"    call g_print(\"$ \");\n" +
				"    var cmd = call g_stdin_read_line();\n" +
				"    let cmd = call g_string_trim(cmd);\n" +
				"    if (call g_string_length(cmd) == 0) {\n" +
				"        call g_printn(\"Error: no cmd\");\n" +
				"        return;\n" +
				"    }\n" +
				"    if (cmd == \"exit\") {\n" +
				"        var handle = call g_create_pipe(\"int#10\");\n" +
				"        call g_write_pipe(handle, 'E');\n" +
				"        return;\n" +
				"    }\n" +
				"    let cmd = call g_string_split(cmd, \"\\\\|\");\n" +
				"    call parse_cmd(cmd, parse);\n" +
				"    call g_create_user_process_args(this, arg);\n" +
				"};\n" +
				"var _args_ = [];\n" +
				"call g_array_add(_args_, get_input);\n" +
				"call g_array_add(_args_, parse_cmd_1);\n" +
				"call g_create_user_process_args(get_input, _args_);\n";
	}
}
