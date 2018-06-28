# jMiniLang - GLR Compiler and Virtual Machine (*Java*)

设计思路：https://zhuanlan.zhihu.com/p/28540783

视频演示：https://www.bilibili.com/video/av13294962/

*一言以蔽之，本项目涉及的思想包括：*

- 编译原理（涵盖正则文法(com.bajdcc.util.lexer)、LR1文法(com.bajdcc.util.LALR1)、LL1文法(com.bajdcc.util.LL1)），包含自动机的生成(NFA,DFA,NPDA,PDA)、LR或LL表的生成(com.bajdcc.LALR1/LL1.syntax)、语法分析(com.bajdcc.LALR1.grammar)、语义分析(com.bajdcc.LALR1.semantic)、语法树的生成(com.bajdcc.LALR1.grammar.tree)、中间代码的生成(com.bajdcc.LALR1.grammar.codegen)，其中LR分析部分要感谢vczh大牛提供的C++源码
- 虚拟机(com.bajdcc.LALR1.interpret)，包含基于栈的虚拟机指令的设计(com.bajdcc.LALR1.grammar.runtime)（没有指针，只有引用）、外部方法导入、二进制码生成、隐性类型转换、实现N元运算
- 语法特性(com.bajdcc.LALR1.grammar.Grammar)，包含foreach/yield的实现、Lambda的实现、管道的实现、import导入代码页的实现、实现try/catch
- 操作系统，包含多进程的实现(RuntimeProcess)、微服务架构（`ModuleTask`）、基于管道的进程同步机制的实现（`ModuleProc`）、用户进程的实现（`ModuleUserBase`意思是可以挂掉而不影响系统）
- Web网页服务器的实现(com.bajdcc.web)，包含REST接口的实现、REST服务与jMiniLang用户进程的消息传递机制、Spring-boot的使用
- UI(com.bajdcc.LALR1.ui)，包含部分SVG指令的绘制、操作系统层面的UI服务设计、控制台的实现、Ctrl-C指令的实现、对话框Dialog的实现、支持中文宽字符的显示、支持RGB24位彩色字符的显示、支持背景颜色的设置
- 基于jMiniLang语言实现的面向对象特性（`ModuleClass`参照JS的原型链）
- 函数式编程接口的实现（`ModuleFunction`）
- LISP的jMiniLang实现（`ModuleLisp`）
- LINQ的jMiniLang实现（`ModuleStdBase`，参考Vlpp）

*一言以蔽之，本项目涉及的玩法包括：*

- Shell层面的管道机制，类似`echo a | > b.txt`等，语法层面有Bash接口的实现
- 基于Map数据的原型链实现面向对象特性（`ModuleClass`），应用有：状态机实例--百度新闻（URNews）、行为树实例-AI（URAI）、状态机实例-歌词动画（URMusic）、图论-路由距离算法-PC（URPC）
- BadApple黑白动画播放（`test badapple`），测试IO性能
- SSH机制（`ModuleNet`），采用netty实现远程命令
- Spring-boot制作而成的网页服务器(localhost:8080)，与我们的jMiniLang语言进行交互，可以查看jMiniLang虚拟机的各项指标
- 哲学家进餐问题（`test philo`、`test philo2`）
- LISP的实现（`test lisp`）
- LINQ的实现（`test linq`）
- 一个自制的基于NIO的简易HTTP服务器（`test web`）
- *还有一些其他的好玩的但不想费力介绍的冷门内容，上面的部分内容我懒得截图了*

----

***jMiniLang*** is a simplified compiler framework. Developed by ***bajdcc***.
*PS.* ***LR Analysis*** refers to ***VFS*** developed by [*vczh*](https://github.com/vczh "Github page of vczh").

#### Features

1. Lexer which generates *NFA* and *DFA*.
2. Parser which implements *LALR(1) Grammar* with optional semantic action.
3. Customized semantic analysis.
4. Printable syntax tree.
5. Stack-based instruction design.
6. Native method.
7. Import and export of code page.
8. Serializable code page.
9. **Lambda functions and Closure**.
10. Display grammar and semantic errors.
11. **Pipe**.
12. **Multiple process**.
13. Load file with Sync/Async.
14. **Virtual machine**.
15. **Support Colorful GUI**.
16. **Functional programming**.
17. **LISP**.
18. **Socket stream**.
19. Save/Load file or VFS.
20. **Class prototype**.
21. **Bash Interface**.
22. Array/Map initialize list.
23. **Try/Catch/Throw**.
24. **Behavior Tree**, including PC network simulator.
25. **RING 3 Process**.
26. **Web Server**.

#### What it generates

- Structures of *Regex*, *NFA* and *DFA* Table.
- Structures of *LL*/*LR* Table.
- Structures of semantic instructions.
- Structures of syntax tree.
- Structures of code page.
- Virtual machine instructions.
- Runtime environment.

#### Virtual Machine OS

An OS running on *jMiniLang* compiler and interpreter.

Now has commands:

- echo
- dup
- pipe
- grep
- proc
- range
- task
- sleep
- time
- count
- **msg**
- **news**(refer: https://github.com/bajdcc/NewsApp)
- **bash**
- replace
- util
- **ai**
- **pc**
- **music**

Tasks:

- System
- Utility
- Remote
- UI
- Store
- Proc

UI:

- Clock
- Hitokoto
- Monitor

Toggle UI:

- `task ui on/off clock`
- `task ui on/off hitokoto`
- `task ui on/off monitor`

Implemented IPC, usage:

- `task system now` -> Get system time
- `task util calc 1+2*3` -> Val = 7
- `task ui print hello world` -> Remote window
- `task ui path M 100 100 L 200 200` -> SVG

Utility:

- `task util doc g_func_fold` -> Document
- `task util reverse ...`
- `task util toupper ...`
- `task util sum ...`
- `task util product ...`
- `task util palindrome ...`

Tests:

- `test philo/philo2`: Multi-processing and synchronization
- `test lisp`: [LISP language](https://zhuanlan.zhihu.com/p/29243574)
- `test font`: Support Chinese Language(wide font)
- `test fork`: Test fork
- `test class`: Test AOP and Prototype for class
- `test bash`: Test bash interface
- `test try`: Test try/catch
- `test badapple`: Test ascii output, code in [BadApple](https://github.com/bajdcc/tinix/blob/master/user/app/badapple.c)
- `test dialog`: Test **JOptionPane.showXXXDialog**
- `test linq`: Test LINQ
- `test proc`: Test Ring 3 API
- `test proc2`: Test Ring 3 code with input
- `test web`: HTTP Web Server

Implemented MSG, usage:

- Create server: `msg server PORT | filter pipe`
- Create client: `other pipe | msg connect IP:PORT`

PC command:

- `pc add A 10 10 100 100`
- `pc remove A`
- `pc msg A B`

LINQ:

- `from(list)` or `from(array)`
- `range(begin, end)`
- Function: select, where, first, last, max, sum, for_each, group_by, distinct, union, etc.

TASK PROC:

- exec
- kill
- info

#### Manual

[*Simplified Chinese Version*](https://raw.githubusercontent.com/bajdcc/jMiniLang/master/%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E.pdf "Manual - Simplified Chinese")

#### Example

**Web Server**

1. **Spring Boot API**, port 8080
2. **Java NIO**, port 8088
3. Render Markdown using FlexMark

*Front-end: LayUI*

**1. Spring Boot API**

> Front-end: LayUI + Vue.js
>
> API: Json + RestController
>
> Back-end: jMiniLang API Handler (RING 3 Process)

**Run on Server**

![Screenshot 106](https://raw.githubusercontent.com/bajdcc/jMiniLang/master/screenshots/web-6.png)

**Online Documentation**

![Screenshot 105](https://raw.githubusercontent.com/bajdcc/jMiniLang/master/screenshots/web-5.png)

![Screenshot 102](https://raw.githubusercontent.com/bajdcc/jMiniLang/master/screenshots/web-4.png)

![Screenshot 103](https://raw.githubusercontent.com/bajdcc/jMiniLang/master/screenshots/web-3.gif)

**Back-end**

```javascript
import "user.base";
import "user.web";
g_disable_result();
var ctx = g_web_get_api();
if (g_is_null(ctx)) { return; }
var route = g_string_split(ctx["route"], "/");

g_printn("API Request: " + ctx["route"]);

// QUERY
if (route[0] == "query") {
if (route[1] == "info") {
    ctx["resp"] := [
        [ "主机名", g_info_get_hostname() ],
        [ "IP地址", g_info_get_ip() ],
        [ "Java 运行时环境版本", g_env_get("java.version") ],
        [ "Java 运行时环境供应商", g_env_get("java.vendor") ],
        [ "Java 供应商的 URL", g_env_get("java.vendor.url") ],
        [ "Java 安装目录", g_env_get("java.home") ],
        [ "Java 虚拟机规范版本", g_env_get("java.vm.specification.version") ],
        [ "Java 虚拟机规范供应商", g_env_get("java.vm.specification.vendor") ],
        [ "Java 虚拟机规范名称", g_env_get("java.vm.specification.name") ],
        [ "Java 虚拟机实现版本", g_env_get("java.vm.version") ],
        [ "Java 虚拟机实现供应商", g_env_get("java.vm.vendor") ],
        [ "Java 虚拟机实现名称", g_env_get("java.vm.name") ],
        [ "Java 运行时环境规范版本", g_env_get("java.specification.version") ],
        [ "Java 运行时环境规范供应商", g_env_get("java.specification.vendor") ],
        [ "Java 运行时环境规范名称", g_env_get("java.specification.name") ],
        [ "Java 类格式版本号", g_env_get("java.class.version") ],
        //[ "Java 类路径", g_env_get("java.class.path") ],
        //[ "加载库时搜索的路径列表", g_env_get("java.library.path") ],
        //[ "默认的临时文件路径", g_env_get("java.io.tmpdir") ],
        //[ "要使用的 JIT 编译器的名称", g_env_get("java.compiler") ],
        //[ "一个或多个扩展目录的路径", g_env_get("java.ext.dirs") ],
        [ "操作系统的名称", g_env_get("os.name") ],
        [ "操作系统的架构", g_env_get("os.arch") ],
        [ "操作系统的版本", g_env_get("os.version") ],
        //[ "文件分隔符(在 UNIX 系统中是\"/\")", g_env_get("file.separator") ],
        //[ "路径分隔符(在 UNIX 系统中是\":\")", g_env_get("path.separator") ],
        //[ "行分隔符(在 UNIX 系统中是\"/n\")", g_env_get("line.separator") ],
        [ "用户的账户名称", g_env_get("user.name") ],
        [ "用户的主目录", g_env_get("user.home") ],
        [ "用户的当前工作目录", g_env_get("user.dir") ]
    ];
} else if (route[1] == "env") {
    ctx["resp"] := [
        [ "唯一标识", g_env_get_guid() ],
        [ "作者", g_author() ],
        [ "当前版本", g_version() ],
        [ "仓库地址", g_github_repo() ]
    ];
} else if (route[1] == "resource") {
    ctx["resp"] := [
        [ "速度", g_res_get_speed() ],
        [ "进程数", g_res_get_proc_size() ],
        [ "管道数", g_res_get_pipe_size() ],
        [ "共享数", g_res_get_share_size() ],
        [ "文件数", g_res_get_file_size() ],
        [ "虚拟文件数", g_res_get_vfs_size() ]
    ];
} else if (route[1] == "proc") {
    ctx["resp"] := g_res_get_proc();
} else if (route[1] == "pipe") {
    ctx["resp"] := g_res_get_pipe();
} else if (route[1] == "share") {
    ctx["resp"] := g_res_get_share();
} else if (route[1] == "file") {
    ctx["resp"] := g_res_get_file();
} else if (route[1] == "vfs") {
    ctx["resp"] := g_res_get_vfs_list();
}
// MARKDOWN
} else if (route[0] == "md") {
    if (route[1] == "readme") {
        ctx["resp"] := g_web_markdown(g_load_resource("/com/bajdcc/code/fs/md/readme.md"));
    } else if (route[1] == "api") {
        //....
    }
} else if (route[0] == "vfs") { // VFS
    var url = route[1];
    url := g_string_replace(url, "_", "/");
    var file = g_res_get_vfs(url);
    var txt = "";
    if (g_not_null(file)) {
        txt := "# File: " + url + g_endl + "```" + g_endl + file + g_endl + "```";
        ctx["resp"] := g_web_markdown(txt);
    } else {
        txt := "# File not exists";
        ctx["resp"] := g_web_markdown(txt);
    }
}
g_web_set_api(ctx);
```

----

**2. Java NIO**

![Screenshot 101](https://raw.githubusercontent.com/bajdcc/jMiniLang/master/screenshots/web-1.png)

```javascript
import "user.base";
import "user.web";
/*
    g_web_get_context = map(code, request, response, header, mime, __ctx__)
    code* - 数字状态码, 200
    request - (headers(map), method, uri, version, protocol, url, port, host, path, query, authority)
    response* - support string, ""
    mime* - mime, application/octet-stream
    content_type* - {0: string, 1: VFS, 2: Resource, 3: File}, 0
    __ctx__ - http context
*/
var ctx = g_web_get_context();
if (g_is_null(ctx)) { return; }
if (ctx["request"]["uri"] == "/") {
    // 主页
    var html =
"
<html>
<head>
    <meta charset=\"UTF-8\">
    <link rel=\"shortcut icon\" href=\"/favicon.ico\" />
    <meta name=\"renderer\" content=\"webkit\">
    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\">
    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1\">
    <link rel=\"stylesheet\" href=\"/layui/css/layui.css\"  media=\"all\">
    <title>jMiniLang Web Server</title>
</head>
<body>
    <blockquote class=\"layui-elem-quote layui-text\">
      <p>jMiniLang 语言实现的网页服务器</p>
      <p>作者：" + g_author() + "</p>
      <p>链接：" + g_github_repo() + "</p>
      <p>前端：LayUI</p>
    </blockquote>
    <fieldset class=\"layui-elem-field layui-field-title\" style=\"margin-top: 20px; padding: 10px;\">
      <legend>请求内容</legend>
    </fieldset>
    <div class=\"layui-form\">
      <table class=\"layui-table\">
        <colgroup>
          <col width=\"100\">
          <col width=\"450\">
        </colgroup>
        <thead>
          <tr>
            <th>Key</th>
            <th>Value</th>
          </tr>
        </thead>
        <tbody>
          <tr><td>URL</td><td>" + ctx["request"]["url"]+ "</td></tr>
          <tr><td>Port</td><td>" + ctx["request"]["port"]+ "</td></tr>
          <tr><td>Host</td><td>" + ctx["request"]["host"]+ "</td></tr>
          <tr><td>Path</td><td>" + ctx["request"]["path"]+ "</td></tr>
          <tr><td>Extension</td><td>" + ctx["request"]["ext"]+ "</td></tr>
          <tr><td>Query</td><td>" + ctx["request"]["query"]+ "</td></tr>
          <tr><td>Authority</td><td>" + ctx["request"]["authority"]+ "</td></tr>
          <tr><td>User-Agent</td><td>" + ctx["request"]["headers"]["User-Agent"]+ "</td></tr>
          <tr><td>Accept</td><td>" + ctx["request"]["headers"]["Accept"]+ "</td></tr>
          <tr><td>Accept-Encoding</td><td>" + ctx["request"]["headers"]["Accept-Encoding"]+ "</td></tr>
          <tr><td>Accept-Language</td><td>" + ctx["request"]["headers"]["Accept-Language"]+ "</td></tr>
          <tr><td>Cache-Control</td><td>" + ctx["request"]["headers"]["Cache-Control"]+ "</td></tr>
          <tr><td>Connection</td><td>" + ctx["request"]["headers"]["Connection"]+ "</td></tr>
        </tbody>
      </table>
    </div>
    <script src=\"/layui/layui.js\" charset=\"utf-8\"></script>
</body>
</html>
";
    ctx["response"] := html;
    ctx["mime"] := "html-utf8";
} else {
    var ext = ctx["request"]["ext"];
    if (g_is_null(ext)) {
        ctx["code"] := 404;
    } else {
        ctx["response"] := ctx["request"]["path"];
        ctx["mime"] := ext;
        ctx["content_type"] := 2;
    }
}
g_web_set_context(ctx);
```

**User mode**
![Screenshot 100](https://raw.githubusercontent.com/bajdcc/jMiniLang/master/screenshots/usermode-1.gif)

**LINQ Example**
```javascript
var linq = g_new("linq::class");
var print_list = lambda(name, list) {
    putn("N=" + list."count"() + " F=" + list."first"() + " L=" + list."last"() +
        " MA=" + list."max"() + " MI=" + list."min"() +
        " AN=" + list."any"(lambda(a)->a>10) + " AL=" + list."all"(lambda(a)->a<6));
    list."for_each"(lambda(i, e) -> putn(name + " [" + i + "] = " + e));
    putln();
};

var print_list2 = lambda(name, list) {
    list."for_each"(lambda(i, e) -> putn(name + " [" + i + "] = " + e));
    putln();
};

putn("\n  [[ Enumerator ]]\n");

// LINQ::FROM[1..5]
print_list("LINQ::FROM[1..5]", linq."from"([1, 2, 3, 4, 5]));

// LINQ::RANGE(1,5)
print_list("LINQ::RANGE(1,5)", linq."range"(1, 5));

// LINQ::RANGE(5,1)
print_list("LINQ::RANGE(5,1)", linq."range"(5, 1));

// LINQ::SELECT(a -> 2*a+1)
print_list("LINQ::RANGE(1,5)::SELECT(a->2*a+1)",
    linq."range"(1, 5)."select"(lambda(a) -> 2*a)."select"(lambda(a) -> a+1));

// LINQ::WHERE(a -> a%2==0)
print_list("LINQ::RANGE(1,5)::WHERE(a->a%2==0)",
    linq."range"(1, 5)."where"(lambda(a) -> a%2==0));

// LINQ::CONCAT
print_list("LINQ::CONCAT",
    linq."range"(2, 6)."select"(lambda(a)->2*a-1)."concat"(linq."range"(10, 3)."where"(lambda(a)->a%2==0)));

// LINQ::TAKE,SKIP,REPEAT
print_list("LINQ::TAKE,SKIP,REPEAT", linq."range"(1, 10)."skip"(3)."take"(3)."repeat"(2));

// LINQ::SELECT_MANY
print_list("LINQ::SELECT_MANY", linq."range"(1, 3)."select_many"(lambda(a)->linq."from"([a, 2*a])));

// LINQ::UNION
print_list("LINQ::UNION", linq."range"(4, 8)."union"(linq."range"(5, 1)."select"(lambda(a)->2*a)));

// LINQ::GROUP_BY
print_list2("LINQ::GROUP_BY",
    linq."range"(1, 5)."select"(lambda(a)->{"a":a})."repeat"(2)
        ."group_by"(lambda(a)->a["a"])
        ."select"(lambda(a)->"key: " + a."key" + ", count: " + a."value"."count"() + ", sum: " + a."value"."sum"()));
```

Output:
```

  [[ Enumerator ]]

N=5 F=1 L=5 MA=5 MI=1 AN=false AL=true
LINQ::FROM[1..5] [0] = 1
LINQ::FROM[1..5] [1] = 2
LINQ::FROM[1..5] [2] = 3
LINQ::FROM[1..5] [3] = 4
LINQ::FROM[1..5] [4] = 5

N=5 F=1 L=5 MA=5 MI=1 AN=false AL=true
LINQ::RANGE(1,5) [0] = 1
LINQ::RANGE(1,5) [1] = 2
LINQ::RANGE(1,5) [2] = 3
LINQ::RANGE(1,5) [3] = 4
LINQ::RANGE(1,5) [4] = 5

N=5 F=5 L=1 MA=5 MI=1 AN=false AL=true
LINQ::RANGE(5,1) [0] = 5
LINQ::RANGE(5,1) [1] = 4
LINQ::RANGE(5,1) [2] = 3
LINQ::RANGE(5,1) [3] = 2
LINQ::RANGE(5,1) [4] = 1

N=5 F=3 L=11 MA=11 MI=3 AN=true AL=false
LINQ::RANGE(1,5)::SELECT(a->2*a+1) [0] = 3
LINQ::RANGE(1,5)::SELECT(a->2*a+1) [1] = 5
LINQ::RANGE(1,5)::SELECT(a->2*a+1) [2] = 7
LINQ::RANGE(1,5)::SELECT(a->2*a+1) [3] = 9
LINQ::RANGE(1,5)::SELECT(a->2*a+1) [4] = 11

N=2 F=2 L=4 MA=4 MI=2 AN=false AL=true
LINQ::RANGE(1,5)::WHERE(a->a%2==0) [0] = 2
LINQ::RANGE(1,5)::WHERE(a->a%2==0) [1] = 4

N=9 F=3 L=4 MA=11 MI=3 AN=true AL=false
LINQ::CONCAT [0] = 3
LINQ::CONCAT [1] = 5
LINQ::CONCAT [2] = 7
LINQ::CONCAT [3] = 9
LINQ::CONCAT [4] = 11
LINQ::CONCAT [5] = 10
LINQ::CONCAT [6] = 8
LINQ::CONCAT [7] = 6
LINQ::CONCAT [8] = 4

N=6 F=4 L=6 MA=6 MI=4 AN=false AL=false
LINQ::TAKE,SKIP,REPEAT [0] = 4
LINQ::TAKE,SKIP,REPEAT [1] = 5
LINQ::TAKE,SKIP,REPEAT [2] = 6
LINQ::TAKE,SKIP,REPEAT [3] = 4
LINQ::TAKE,SKIP,REPEAT [4] = 5
LINQ::TAKE,SKIP,REPEAT [5] = 6

N=6 F=1 L=6 MA=6 MI=1 AN=false AL=false
LINQ::SELECT_MANY [0] = 1
LINQ::SELECT_MANY [1] = 2
LINQ::SELECT_MANY [2] = 2
LINQ::SELECT_MANY [3] = 4
LINQ::SELECT_MANY [4] = 3
LINQ::SELECT_MANY [5] = 6

N=7 F=4 L=2 MA=10 MI=2 AN=false AL=false
LINQ::UNION [0] = 4
LINQ::UNION [1] = 5
LINQ::UNION [2] = 6
LINQ::UNION [3] = 7
LINQ::UNION [4] = 8
LINQ::UNION [5] = 10
LINQ::UNION [6] = 2

LINQ::GROUP_BY [0] = key: 1, count: 2, sum: 2
LINQ::GROUP_BY [1] = key: 2, count: 2, sum: 4
LINQ::GROUP_BY [2] = key: 3, count: 2, sum: 6
LINQ::GROUP_BY [3] = key: 4, count: 2, sum: 8
LINQ::GROUP_BY [4] = key: 5, count: 2, sum: 10
```

**Bash Example**

See `test bash` command.
```bash
@ui on monitor
range 1 10
range 1 10 | dup 2 | grep 2
range 1 10 | > &
range 1 10 | > $/tmp/a
< $/tmp/a | dup 2 | grep 2 | > &
< $/tmp/a | dup 2 | grep 2 | count | > &
< $/tmp/a | sleep 1 | pipe 10 | > &
range 1 10 | replace 1 $ 2 $ 3 | > &
range 1 10 | replace task util calc $ * $ | bash | > &
range 1 10 | replace task util calc $ + 1 | bash | util product | > &
range -5 10 | replace task util calc $ * $ * $ | bash | util sum | > &
echo Made_by_bajdcc | > &
```

**Tail optimization**

```javascript
var g_tail_opt = func ["尾递归优化"] ~(fun, args) {
    var x = lambda(a) { throw a; };
    var fact = fun(x);
    for (;;) {
        try {
            return g_call_apply(fact, args);
        } catch (e) {
            args := e;
        }
    }
};

// Usage
g_printn("Factorial(10) = " + g_tail_opt(
    lambda(f) -> lambda(n, total) -> n <= 1 ? total : f([n - 1, total * n]),
    [10, 1]));
g_printn("Fibonacci(10) = " + g_tail_opt(
    lambda(f) -> lambda(n, a, total) -> n <= 1 ? total : f([n - 1, total, a + total]),
    [10, 0, 1]));
```

**0. Class**

```javascript
import "sys.class";
// create context
var ctx = call g_create_context();

g_register_class(ctx, "shape", lambda(this) {
    this."type" := "shape";
    this."get_area" := lambda(this) -> 0;
    this."get_index" := lambda(this, i) -> i;
}, g_null);
g_register_class(ctx, "square", lambda(this) {
    this."type" := "square";
    this."a" := 0;
    this."b" := 0;
    this."get_area" := lambda(this) -> this."a" * this."b";
    this."to_string" := lambda(this) -> this."type" + " a=" + this."a" + " b=" + this."b" + " area=" + this."get_area"();
}, "shape");
g_register_class(ctx, "circle", lambda(this) {
    this."type" := "circle";
    this."r" := 0;
    this."get_area" := lambda(this) -> 3.14 * this."r" * this."r";
    this."to_string" := lambda(this) -> this."type" + " r=" + this."r" + " area=" + this."get_area"();
}, "shape");

var square = g_create_class(ctx, "square");
square."a" := 5;
square."b" := 6;
var circle = g_create_class(ctx, "circle");
circle."r" := 10;
circle."s" := square;

print(square."to_string"());
print(circle."to_string"());

square."a" := 100;
circle."s"."b" := 120;

print("" + square."type"
    + " a=" + square."a"
    + " b=" + square."b"
    + " area=" + square."get_area"()
    + " index=" + square."get_index"(1));
print("" + circle."type"
    + " r=" + circle."r"
    + " sa=" + circle."s"."a"
    + " sb=" + circle."s"."b"
    + " area=" + circle."get_area"()
    + " sarea=" + circle."s"."get_area"()
    + " index=" + circle."get_index"(2));

print("");
print(g_string_rep("-", 16));
print("2. Hook");
print(g_string_rep("-", 16));

// 返回null，因为before回调中没有调用next()，直接拦截
// 因此before_1后直接退出，before_2和after_1没有执行
var before_1 = lambda(class, name, this, next) -> print("HOOKED BEFORE 1: " + class + "::" + name);
g_hook_add_before(square, "get_area", before_1);
var before_2 = lambda(class, name, this, next) -> print("HOOKED BEFORE 2: " + class + "::" + name);
g_hook_add_before(square, "get_area", before_2);
var after_1 = lambda(class, name, this, ret, next) -> print("HOOKED AFTER 1: " + class + "::" + name);
g_hook_add_after(square, "get_area", after_1);

print("A " + square."type" + " area=" + square."get_area"()); // failed

g_hook_remove_before(square, "get_area", before_1);
g_hook_remove_before(square, "get_area", before_2);
g_hook_remove_after(square, "get_area", after_1);

// 返回12000，因为before和after都调用next()，未拦截
before_1 := lambda(class, name, this, next) {
    print("HOOKED BEFORE 1: " + class + "::" + name);
    return next();
};
g_hook_add_before(square, "get_area", before_1);
before_2 := lambda(class, name, this, next) {
    print("HOOKED BEFORE 2: " + class + "::" + name);
    return next();
};
g_hook_add_before(square, "get_area", before_2);
after_1 := lambda(class, name, this, r, next) {
    print("HOOKED AFTER  3: " + class + "::" + name + "=" + r);
    return next();
};
g_hook_add_after(square, "get_area", after_1);

print("B " + square."type" + " area=" + square."get_area"());

g_hook_remove_before(square, "get_area", before_1);
g_hook_remove_before(square, "get_area", before_2);
g_hook_remove_after(square, "get_area", after_1);

// 返回12346，直接拦截了，因为before_2中没有调用next()
// 因此返回12345+1，但当before_2返回next()+12345时
// get_area还是返回12000，拦截没有效果，因为都调用了next()
before_1 := lambda(class, name, this, next) {
    print("HOOKED BEFORE 4: " + class + "::" + name);
    return next() + 1;
};
g_hook_add_before(square, "get_area", before_1);
before_2 := lambda(class, name, this, next) {
    print("HOOKED BEFORE 5: " + class + "::" + name);
    return 12345;
};
g_hook_add_before(square, "get_area", before_2);
after_1 := lambda(class, name, this, r, next) {
    print("HOOKED AFTER  6: " + class + "::" + name + "=" + r);
    return next() + 10;
};
g_hook_add_after(square, "get_area", after_1);

print("C " + square."type" + " area=" + square."get_area"());

g_hook_remove_before(square, "get_area", before_1);
g_hook_remove_before(square, "get_area", before_2);

// 返回12010，因为有两个after，after_7没有调用next()，直接拦截
var after_2 = lambda(class, name, this, r, next) {
    print("HOOKED AFTER  7: " + class + "::" + name + "=" + r);
    return r;
};
g_hook_add_after(square, "get_area", after_2);

print("D " + square."type" + " area=" + square."get_area"());

g_hook_remove_after(square, "get_area", after_1);
g_hook_remove_after(square, "get_area", after_2);

print("E " + square."type" + " area=" + square."get_area"());

before_1 := lambda(class, name, this, next, arg1) {
    print("HOOKED BEFORE 5: " + class + "::" + name);
    return 10000;
};
g_hook_add_before(square, "get_index", before_1);

print("F " + square."type" + " index=" + square."get_index"(1));

g_hook_remove_before(square, "get_index", before_1);
```

**1. Lambda: Y Combinator of Hanoi**

*Code:*

```javascript
import "sys.base";
var move = func ~(i, x, y) {
    call g_printn(call g_to_string(i) + ": " + 
        call g_to_string(x) + " -> " + call g_to_string(y));
};
var hanoi = func ~(f) {
    var fk = func ~(i, a, b, c) {
        if (i == 1) {
            call move(i, a, c);
        } else {
            call f(i - 1, a, c, b);
            call move(i, a, c);
            call f(i - 1, b, a, c);
        }
    };
    return fk;
};
var h = call (func ~(f) {
    var fx = func ~(x) {
        var fn = func ~(i, a, b, c) {
            var vf = call f(call x(x));
            return call vf(i, a, b, c);
        };
        return fn;
    };
    return call (func ~(h) -> call h(h))(fx);
})(hanoi);
call h(3, 'A', 'B', 'C');
```

*Result:*

```c
1: A -> C
2: A -> B
1: C -> B
3: A -> C
1: B -> A
2: B -> C
1: A -> C
```

**2. Lambda: Trampoline**

*Code:*

```javascript
import "sys.base";
call g_printn("Trampoline example:");
var repeat = func ~(operation, count) {
    var repeat0 = func ~() {
        if (count <= 0) { return; }
        call operation(count);
        return call repeat(operation, --count);
    };
    return repeat0;
};
var print = func ~(n) -> call g_printn("n: " + n);
var tfun = func ~() -> call repeat(print, 5);
call(func ~(f) {
    while (!(call g_is_null(f)) && (call g_get_type(f) == "函数")) {
        let f = call f(); // Trampoline, like CPS.
    }
})(tfun);
```

*Result:*

```c
Trampoline example:
n: 5
n: 4
n: 3
n: 2
n: 1
```

**3. List: LinkedList**

*Code:*

```javascript
import "sys.base";
import "sys.list";
var create_node = func ~(data) {
    var new_node = g_new_map;
    call g_map_put(new_node, "data", data);
    call g_map_put(new_node, "prev", g_null);
    call g_map_put(new_node, "next", g_null);
    return new_node;
};
var append = func ~(head, obj) {
    var new_node = call create_node(obj);
    call g_map_put(new_node, "next", head);
    call g_map_put(head, "prev", new_node);
    return new_node;
};
var head = call create_node(0);
foreach (var i : call g_range(1, 10)) {
    let head = call append(head, i);
}
var p = head;
while (!call g_is_null(p)) {
    call g_printn(call g_map_get(p, "data"));
    let p = call g_map_get(p, "next");
}
```

*Result:*

```c
10
9
8
7
6
5
4
3
2
1
0
```

**4. Multi-Process: Pipe**

*Code:*

```javascript
import "sys.base";
import "sys.proc";
var proc = func ~() {
    var handle = call g_create_pipe("test");
    var print = func ~(ch) -> call g_print(ch);
    var pid = call g_get_pid();
    if (pid == 0) {
        foreach (var i : call g_range(1, 10)) {
            call g_printn("[" + call g_get_pid() + "] Hello world!");
            call g_write_pipe(handle, "" + i);
        }
        call g_destroy_pipe(handle);
    } else {
        call g_printn("[" + call g_get_pid() + "] Hello world!");
        call g_read_pipe(handle, print);
    }
};
call g_create_process(proc);
call proc();
```

*Result:*

```c
[1] Hello world!
[0] Hello world!
1[0] Hello world!
2[0] Hello world!
3[0] Hello world!
[0] Hello world!
4[0] Hello world!
5[0] Hello world!
6[0] Hello world!
7[0] Hello world!
8[0] Hello world!
91
```

**5. Multi-Process: Consumer-Producer Model**

*Code:*

```javascript
import "sys.base";
import "sys.list";
import "sys.proc";

var goods = [];
call g_start_share("goods", goods);
var index = 1;
call g_start_share("index", index);
var consumer = func ~() {
    for (;;) {
        var goods = call g_query_share("goods");
        if (call g_is_null(goods)) {
            break;
        }
        var g = call g_array_pop(goods);
        if (!call g_is_null(g)) {
            call g_printn("Consumer#" + call g_get_pid() + " ---- get " + g);
        }
    }
    call g_printn("Consumer#" + call g_get_pid() + " exit");
};
var producer = func ~() {
    foreach (var i : call g_range(1, 5)) {
        var goods = call g_reference_share("goods");
        call g_lock_share("index");
        var index = call g_reference_share("index");
        call g_printn("Producer#" + call g_get_pid() + " ++++ put " + index);
        call g_array_add(goods, index);
        index++;
        call g_stop_share("index");
        call g_unlock_share("index");
        call g_stop_share("goods");
    }
    call g_printn("Producer#" + call g_get_pid() + " exit");
};
var create_consumer = func ~(n) {
    var handles = [];
    foreach (var i : call g_range(1, n)) {
        var h = call g_create_process(consumer);
        call g_array_add(handles, h);
        call g_printn("[" + i + "] Create consumer: #" + h);
    }
    return handles;
};
var create_producer = func ~(n) {
    var handles = [];
    foreach (var i : call g_range(1, n)) {
        var h = call g_create_process(producer);
        call g_array_add(handles, h);
        call g_printn("[" + i + "] Create producer: #" + h);
    }
    return handles;
};

var consumers = call create_consumer(3);
var producers = call create_producer(4);
call g_printn("Waiting for producers to exit...");
call g_join_process_array(producers);
call g_printn("Producers exit");
call g_printn("Waiting for consumers to exit...");
call g_stop_share("index");
call g_stop_share("goods");
call g_join_process_array(consumers);
call g_printn("Consumers exit");
```

*Result:*

```c
[1] Create consumer: #1
[2] Create consumer: #2
[3] Create consumer: #3
[1] Create producer: #4
[2] Create producer: #5
Producer#4 ++++ put 1
Consumer#3 ---- get 1
[3] Create producer: #6
Producer#5 ++++ put 2
[4] Create producer: #7
Consumer#2 ---- get 2
Producer#4 ++++ put 3
Waiting for producers to exit...
Consumer#1 ---- get 3
Producer#7 ++++ put 4
Consumer#2 ---- get 4
Producer#7 ++++ put 5
Consumer#3 ---- get 5
Producer#5 ++++ put 6
Consumer#2 ---- get 6
Producer#5 ++++ put 7
Consumer#1 ---- get 7
Producer#7 ++++ put 8
Consumer#3 ---- get 8
Producer#6 ++++ put 9
Consumer#3 ---- get 9
Producer#5 ++++ put 10
Consumer#2 ---- get 10
Producer#7 ++++ put 11
Consumer#1 ---- get 11
Producer#4 ++++ put 12
Consumer#3 ---- get 12
Producer#5 ++++ put 13
Consumer#1 ---- get 13
Producer#5 exit
Producer#6 ++++ put 14
Consumer#2 ---- get 14
Producer#4 ++++ put 15
Consumer#3 ---- get 15
Producer#7 ++++ put 16
Consumer#2 ---- get 16
Producer#7 exit
Producer#6 ++++ put 17
Consumer#1 ---- get 17
Producer#4 ++++ put 18
Consumer#1 ---- get 18
Producer#4 exit
Producer#6 ++++ put 19
Consumer#1 ---- get 19
Producer#6 ++++ put 20
Consumer#3 ---- get 20
Producer#6 exit
Producers exit
Waiting for consumers to exit...
Consumer#3 exit
Consumer#1 exit
Consumer#2 exit
Consumers exit
```

**6. Multi-Process: PC and Router**

*Code:*

```javascript
import "sys.base";
import "sys.list";
import "sys.proc";

var pc_router = 5;
call g_start_share("pc_router", pc_router);

var pc = func ~(index) {
    var pc_router = call g_query_share("pc_router");
    var name = "pc_" + index;
    var router = index / pc_router;
    var display = "PC #" + index;
    call g_printn(display + " started");
    call g_sleep(50);
    var handle = call g_create_pipe(name);
    call g_printn(display + " connecting...");
    var router_connection = "router#" + router;
    for (;;) {
        call g_sleep(100);
        call g_lock_share(router_connection);
        var connection = call g_query_share(router_connection);
        if (call g_is_null(connection)) {
            call g_unlock_share(router_connection);
            continue;
        }
        call g_printn(display + " connecting to #" + router);
        call g_array_add(connection, index);
        call g_unlock_share(router_connection);
        break;
    }
    var get_id = func ~(ch) {
        if (ch == '@') {
            call g_printn(display + " connected to router #" + router);
        }
    };
    call g_read_pipe(handle, get_id);
    call g_sleep(1000);
    call g_printn(display + " stopped");
};

var router = func ~(index) {
    var pc_router = call g_query_share("pc_router");
    var name = "router_" + index;
    var display = "Router #" + index;
    call g_printn(display + " started");
    var router_connection = "router#" + index;
    var connection = [];
    var list = [];
    call g_start_share(router_connection, connection);
    var connected = 0;
    var handle_pc = func ~(args) {
        var connected = call g_array_get(args, 0);
        var pc_router = call g_array_get(args, 1);
        var router_connection = call g_array_get(args, 2);
        var display = call g_array_get(args, 3);
        var list = call g_array_get(args, 4);
        for (;;) {
            call g_sleep(100);
            call g_lock_share(router_connection);
            var connection = call g_query_share(router_connection);
            var new_pc = call g_array_pop(connection);
            if (call g_is_null(new_pc)) {
                call g_unlock_share(router_connection);
                continue;
            }
            connected++;
            call g_array_add(list, new_pc);
            call g_printn(display + " connecting to pc #" + new_pc);
            call g_unlock_share(router_connection);
            var name = "pc_" + new_pc;
            var handle = call g_create_pipe(name);
            call g_destroy_pipe(handle);
            call g_printn(display + " connected to #" + new_pc);
            if (connected == pc_router) { break; }
        }
    };
    var args = [];
    call g_array_add(args, connected);
    call g_array_add(args, pc_router);
    call g_array_add(args, router_connection);
    call g_array_add(args, display);
    call g_array_add(args, list);
    call g_join_process(call g_create_process_args(handle_pc, args));
    var stop_pc = func ~(args) {
        var display = call g_array_get(args, 3);
        var list = call g_array_get(args, 4);
        var size = call g_array_size(list);
        for (var i = 0; i < size; i++) {
            call g_sleep(10);
            var name = "pc_" + call g_array_get(list, i);
            var handle = call g_create_pipe(name);
            call g_write_pipe(handle, "!");
            call g_printn(display + " disconnected with #" + i);
        }
    };
    call g_sleep(100);
    call g_join_process(call g_create_process_args(stop_pc, args));
    call g_printn(display + " stopped");
};

var create_pc = func ~(n) {
    var handles = [];
    foreach (var i : call g_range(0, n - 1)) {
        var h = call g_create_process_args(pc, i);
        call g_array_add(handles, h);
        call g_printn("Create pc: #" + i);
    }
    return handles;
};
var create_router = func ~(n) {
    var handles = [];
    foreach (var i : call g_range(0, n - 1)) {
        var h = call g_create_process_args(router, i);
        call g_array_add(handles, h);
        call g_printn("Create router: #" + i);
    }
    return handles;
};

call g_printn("Starting pc...");
var pcs = call create_pc(5);
call g_printn("Starting router...");
var routers = call create_router(1);
call g_join_process_array(pcs);
call g_join_process_array(routers);
```

*Result:*

```c
Starting pc...
Create pc: #0
PC #0 started
Create pc: #1
PC #1 started
Create pc: #2
PC #2 started
Create pc: #3
PC #3 started
Create pc: #4
PC #4 started
Starting router...
Create router: #0
Router #0 started
PC #0 connecting...
PC #1 connecting...
PC #2 connecting...
PC #3 connecting...
PC #4 connecting...
PC #0 connecting to #0
PC #1 connecting to #0
PC #2 connecting to #0
PC #3 connecting to #0
PC #4 connecting to #0
Router #0 connecting to pc #4
Router #0 connected to #4
Router #0 connecting to pc #3
Router #0 connected to #3
Router #0 connecting to pc #2
Router #0 connected to #2
Router #0 connecting to pc #1
Router #0 connected to #1
Router #0 connecting to pc #0
Router #0 connected to #0
Router #0 disconnected with #0
Router #0 disconnected with #1
Router #0 disconnected with #2
Router #0 disconnected with #3
Router #0 disconnected with #4
Router #0 stopped
PC #4 stopped
PC #3 stopped
PC #2 stopped
PC #1 stopped
PC #0 stopped
```

**7. Functional programming**

```
import "sys.base";
import "sys.list";
var g_func_max = func ~(a, b) -> a > b ? a : b;
var g_func_min = func ~(a, b) -> a < b ? a : b;
var g_func_lt = func ~(a, b) -> a < b;
var g_func_lte = func ~(a, b) -> a <= b;
var g_func_gt = func ~(a, b) -> a > b;
var g_func_gte = func ~(a, b) -> a >= b;
var g_func_eq = func ~(a, b) -> a == b;
var g_func_neq = func ~(a, b) -> a != b;
var g_func_add = func ~(a, b) -> a + b;
var g_func_sub = func ~(a, b) -> a - b;
var g_func_mul = func ~(a, b) -> a * b;
var g_func_div = func ~(a, b) -> a / b;
var g_func_and = func ~(a, b) -> a && b;
var g_func_or = func ~(a, b) -> a || b;
export "g_func_max";
export "g_func_min";
export "g_func_lt";
export "g_func_lte";
export "g_func_gt";
export "g_func_gte";
export "g_func_eq";
export "g_func_neq";
export "g_func_add";
export "g_func_sub";
export "g_func_mul";
export "g_func_div";
export "g_func_and";
export "g_func_or";
var g_func_curry = func ~(a, b) {
    var _curry = func ~(c) -> call a(b, c);
    return _curry;
};
var g_func_swap = func ~(a) {
    var _swap = func ~(b, c) -> call a(c, b);
    return _swap;
};
export "g_func_curry";
export "g_func_swap";
var g_func_1 = func ~(a) -> a;
export "g_func_1";
var g_func_always_1 = func ~(a) -> 1;
export "g_func_always_1";
var g_func_always_true = func ~(a) -> true;
export "g_func_always_true";

var g_func_xsl = func ["数组遍历闭包-foldl"] ~(l) {
    var len = call g_array_size(l);
    var idx = 0;
    var _xsl = func ~() {
        if (idx == len) { return g__; }
        var d = call g_array_get(l, idx);
        idx++;
        var _xsl_ = func ~() -> d;
        return _xsl_;
    };
    return _xsl;
};
export "g_func_xsl";
var g_func_xsr = func ["数组遍历闭包-foldr"] ~(l) {
    var idx = call g_array_size(l) - 1;
    var _xsr = func ~() {
        if (idx < 0) { return g__; }
        var d = call g_array_get(l, idx);
        idx--;
        var _xsr_ = func ~() -> d;
        return _xsr_;
    };
    return _xsr;
};
export "g_func_xsr";
// ----------------------------------------------
var g_func_fold = func 
    [
        "函数名：g_func_fold",
        "参数解释：",
        "  - name: 套用的折叠函数",
        "  - list: 需处理的数组",
        "  - init: 初始值(不用则为空)",
        "  - xs: 数组遍历方式(xsl=从左到右,xsr=从右到左)",
        "  - map: 对遍历的每个元素施加的变换",
        "  - arg: 对二元操作进行包装(默认=g_func_1,例=g_func_swap)",
        "  - filter: 对map后的元素进行过滤(true则处理)"
    ]
    ~(name, list, init, xs, map, arg, filter) {
    var len = call g_array_size(list);
    if (len == 0) { return g__; }
    var val = g__;
    var x = g__;
    if (call g_is_null(init)) {
        if (len == 1) { return call g_array_get(list, 0); }
        let x = call xs(list);
        let val = call x();
        let val = call val();
        let val = call map(val);
    } else {
        let x = call xs(list);
        let val = init;
    }
    var n = name;
    let n = call arg(n);
    for (;;) {
        var v2 = call x();
        if (call g_is_null(v2)) { break; }
        let v2 = call v2();
        let v2 = call map(v2);
        if (call filter(v2)) {
            let val = call n(val, v2);
        }
    }
    return val;
};
export "g_func_fold";
// ----------------------------------------------
var g_func_apply = func ~(name, list) ->
    call g_func_apply_arg(name, list, "g_func_1");
export "g_func_apply";
var g_func_apply_arg = func ~(name, list, arg) ->
    call g_func_fold(name, list, g__, "g_func_xsl", "g_func_1", arg, "g_func_always_true");
export "g_func_apply_arg";
var g_func_applyr = func ~(name, list) ->
    call g_func_applyr_arg(name, list, "g_func_1");
export "g_func_applyr";
var g_func_applyr_arg = func ~(name, list, arg) ->
    call g_func_fold(name, list, g__, "g_func_xsr", "g_func_1", arg, "g_func_always_true");
export "g_func_applyr_arg";
// ----------------------------------------------
var g_func_map = func ~(list, arg) ->
    call g_func_fold("g_array_add", list, g_new_array, "g_func_xsl", arg, "g_func_1", "g_func_always_true");
export "g_func_map";
var g_func_mapr = func ~(list, arg) ->
    call g_func_fold("g_array_add", list, g_new_array, "g_func_xsr", arg, "g_func_1", "g_func_always_true");
export "g_func_mapr";
var g_func_length = func ~(list) ->
    call g_func_fold("g_func_add", list, 0, "g_func_xsl", "g_func_always_1", "g_func_1", "g_func_always_true");
export "g_func_length";
var g_func_filter = func ~(list, filter) ->
    call g_func_fold("g_array_add", list, g_new_array, "g_func_xsl", "g_func_1", "g_func_1", filter);
export "g_func_filter";
// ----------------------------------------------
var take_filter = func ~(n) {
    var idx = 0;
    var end = n;
    var _take_filter = func ~(a) -> idx++ <= end;
    return _take_filter;
};
var drop_filter = func ~(n) {
    var idx = 0;
    var end = n;
    var _drop_filter = func ~(a) -> idx++ > end;
    return _drop_filter;
};
var g_func_take = func ~(list, n) ->
    call g_func_fold("g_array_add", list, g_new_array, "g_func_xsl", "g_func_1", "g_func_1", call take_filter(n));
export "g_func_take";
var g_func_taker = func ~(list, n) ->
    call g_func_fold("g_array_add", list, g_new_array, "g_func_xsr", "g_func_1", "g_func_1", call take_filter(n));
export "g_func_taker";
var g_func_drop = func ~(list, n) ->
    call g_func_fold("g_array_add", list, g_new_array, "g_func_xsl", "g_func_1", "g_func_1", call drop_filter(n));
export "g_func_drop";
var g_func_dropr = func ~(list, n) ->
    call g_func_fold("g_array_add", list, g_new_array, "g_func_xsr", "g_func_1", "g_func_1", call drop_filter(n));
export "g_func_dropr";
// ----------------------------------------------
var func_zip = func ~(name, a, b, xs) {
    var val = [];
    var xa = call xs(a);
    var xb = call xs(b);
    for (;;) {
        var _a = call xa();
        var _b = call xb();
        if (call g_is_null(_a) || call g_is_null(b)) {
            break;
        }
        var c = call name(call _a(), call _b());
        call g_array_add(val, c);
    }
    return val;
};
var g_func_zip = func ~(name, a, b) ->
    call func_zip(name, a, b, "g_func_xsl");
export "g_func_zip";
var g_func_zipr = func ~(name, a, b) ->
    call func_zip(name, a, b, "g_func_xsr");
export "g_func_zipr";
// ----------------------------------------------
var g_func_applicative = func ~(f, a, b) -> call f(a, call b(a));
export "g_func_applicative";
var g_func_import_string_module = func ~() { import "sys.string"; };
export "g_func_import_string_module";
```

#### Screenshot

*Screenshot 1 - Code*
![Screenshot 1](https://raw.githubusercontent.com/bajdcc/jMiniLang/master/screenshots/jMiniLang_1.png)

*Screenshot 2 - Results*
![Screenshot 2](https://raw.githubusercontent.com/bajdcc/jMiniLang/master/screenshots/jMiniLang_2.png)

*Screenshot 3 - Y-Combinator*
![Screenshot 3](https://raw.githubusercontent.com/bajdcc/jMiniLang/master/screenshots/jMiniLang_3.png)

*Screenshot 4 - OS Virtual Machine with GUI*
![Screenshot 4](https://raw.githubusercontent.com/bajdcc/jMiniLang/master/screenshots/jMiniLang_4.png)

*Screenshot 5 - Remote window*
![Screenshot 5](https://raw.githubusercontent.com/bajdcc/jMiniLang/master/screenshots/zhihu-1.png)

*Screenshot 6 - Functional programming*
![Screenshot 6](https://raw.githubusercontent.com/bajdcc/jMiniLang/master/screenshots/zhihu-2.png)

*Screenshot 7 - 哲学家就餐*

专栏：https://zhuanlan.zhihu.com/p/29008180

![Screenshot 7](https://raw.githubusercontent.com/bajdcc/jMiniLang/master/screenshots/zhihu-3.png)

*Screenshot 8 - LISP*

专栏：https://zhuanlan.zhihu.com/p/29243574

![Screenshot 8](https://raw.githubusercontent.com/bajdcc/jMiniLang/master/screenshots/zhihu-4.png)

*Screenshot 9 - 网络流*

专栏：https://zhuanlan.zhihu.com/p/32692408

![Screenshot 9](https://raw.githubusercontent.com/bajdcc/jMiniLang/master/screenshots/zhihu-5.jpg)