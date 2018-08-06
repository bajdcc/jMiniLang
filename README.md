# jMiniLang - GLR Compiler and Virtual Machine (*Kotlin*)

[![Build status](https://ci.appveyor.com/api/projects/status/axmcy1gijdbw3rj9?svg=true)](https://ci.appveyor.com/project/bajdcc/jminilang)

设计思路：https://zhuanlan.zhihu.com/p/28540783

视频演示：https://www.bilibili.com/video/av13294962/

[语法参考（LR状态转移表）](https://github.com/bajdcc/jMiniLang/blob/master/Grammar.md)

*一言以蔽之，本项目涉及的思想包括：*

- 编译原理（涵盖正则文法(com.bajdcc.util.lexer)、LR1文法(com.bajdcc.LALR1)、LL1文法(com.bajdcc.LL1)），重点：**语法制导翻译、自定义语义动作**，包含自动机的生成(非确定性有限自动机-NFA、确定性有限自动机-DFA、非确定性下推自动机-NPDA、确定性下推自动机-PDA)、LR或LL表的生成(com.bajdcc.LALR1/LL1.syntax)、语法分析(com.bajdcc.LALR1.grammar)、语义分析(com.bajdcc.LALR1.semantic)、语法树的生成(com.bajdcc.LALR1.grammar.tree)、中间代码的生成(com.bajdcc.LALR1.grammar.codegen)，其中LR分析部分要感谢vczh大牛提供的C++源码
- 虚拟机(com.bajdcc.LALR1.interpret)，包含基于栈的虚拟机指令的设计(com.bajdcc.LALR1.grammar.runtime)（**没有指针，只有引用**）、外部方法导入、二进制码生成、隐性类型转换、实现N元运算
- 语法特性(com.bajdcc.LALR1.grammar.Grammar)，包含foreach/yield的实现、Lambda的实现、管道的实现、import导入代码页的实现、实现try/catch，及一些语法糖
- 操作系统，包含多进程的实现(RuntimeProcess)、微服务架构（`ModuleTask`）、基于管道的进程同步机制的实现（`ModuleProc`）、用户进程的实现（`ModuleUserBase`意思是可以挂掉而不影响系统）
- Web网页服务器的实现(com.bajdcc.web)，包含REST接口的实现、REST服务与jMiniLang用户进程的消息传递机制、Spring-boot的使用
- UI(com.bajdcc.LALR1.ui)，包含部分SVG指令的绘制、操作系统层面的UI服务设计、控制台的实现、Ctrl-C指令的实现、对话框Dialog的实现、支持中文宽字符的显示、支持RGB24位彩色字符的显示、支持背景颜色的设置
- 基于jMiniLang语言实现的面向对象特性（`ModuleClass`参照JS的原型链）
- 函数式编程接口的实现（`ModuleFunction`）
- LISP的jMiniLang实现（`ModuleLisp`），[B站视频链接](https://www.bilibili.com/video/av13294962/?p=2)
- 语言集成查询（LINQ）的jMiniLang实现（`ModuleStdBase`，参考Vlpp），类似Java 8 Stream链式/流式操作

*一言以蔽之，本项目涉及的玩法包括：*

- Spring-boot与layui制作的管理后台，包括资源查看、文档查看、**在线编译**！
- `UserService` RING3级用户服务，实现**FORK**、**管道**、**互斥**。
- **开发中** 【C语言解释器】类似[CParser](https://github.com/bajdcc/CParser)的类设计，参考GO语言库，[代码](https://github.com/bajdcc/jMiniLang/blob/master/src/main/resources/com/bajdcc/code/module/user/ModuleUserCParser.txt)
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

***jMiniLang*** is a simplified compiler/vm framework. Developed by ***bajdcc***.
*PS.* ***LR Analysis*** refers to ***VFS*** developed by [*vczh*](https://github.com/vczh "Github page of vczh").

本项目是一个LR编译器、虚拟机一体化工程，并且对虚拟机进行了拓展，参考了操作系统设计的思想。

#### Features（特性）

1. 词法分析阶段。Lexer which generates *NFA* and *DFA*.
2. 语法分析、词法分析、制导翻译。Parser which implements *LALR(1) Grammar* with optional semantic action.
3. 语义分析。Customized semantic analysis.
4. 可打印语法树。Printable syntax tree.
5. 基于栈的自定义指令集。Stack-based instruction design.
6. Kotlin本地方法导入。Native method.
7. 代码页导入/导出。Import and export of code page.
8. 代码页序列化。Serializable code page.
9. 匿名函数及闭包。**Lambda functions and Closure**.
10. 语法/词法错误提示。Display grammar and semantic errors.
11. 管道机制。**Pipe**.
12. 多进程机制。**Multiple process**.
13. 同步/异步执行代码。Load file with Sync/Async.
14. 虚拟机。**Virtual machine**.
15. 支持彩色界面。**Support Colorful GUI**.
16. 函数式编程。**Functional programming**.
17. **LISP**.
18. 网络流。**Socket stream**.
19. 虚拟文件系统。Save/Load file or VFS.
20. 基于原型的类设计。**Class prototype**.
21. **Bash Interface**.
22. 数组/词典初始化。Array/Map initialize list.
23. 异常机制。**Try/Catch/Throw**.
24. 行为树。**Behavior Tree**, including PC network simulator.
25. 用户级进程。**RING 3 Process**, including User Service, `fork`.
26. 网页服务器。**Web Server**, including Online Compiler and Runner.
27. C语言解析。**CParser** class on `ModuleUser`.

#### What it generates（产生）

- 正则表达式、状态机。Structures of *Regex*, *NFA* and *DFA* Table.
- 分析表。Structures of *LL*/*LR* Table.
- 语义分析指令。Structures of semantic instructions.
- 语法树。Structures of syntax tree.
- 代码页。Structures of code page.
- 虚拟机指令。Virtual machine instructions.
- 运行时环境。Runtime environment.

#### Virtual Machine OS

An OS running on *jMiniLang* compiler and interpreter.

Now has commands:（现在主窗口支持的cmd命令）

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

Tasks:（使用方法如：`@system halt`）

- System
- Utility
- Remote
- UI
- Store
- Proc

UI:（使用方法如：`@ui on clock`）

- Clock
- Hitokoto
- Monitor

Toggle UI:

- `task ui on/off clock`
- `task ui on/off hitokoto`
- `task ui on/off monitor`

Implemented IPC, usage:（微服务）

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

Tests:（测试命令，直接在主窗口cmd输入，Ctrl-C中止）

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

Implemented MSG, usage:（远程控制）

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

- exec：执行代码
- exec_file：读文件执行代码
- kill：中止用户进程
- info：取得用户进程状态（用于浏览器远程回调）

[USER HANDLE](https://github.com/bajdcc/jMiniLang/blob/master/src/main/kotlin/com/bajdcc/LALR1/grammar/runtime/service/IRuntimeUserService.kt):（用户级进程支持的句柄种类）

- pipe：管道，类似Go中的chan，用于跨进程同步，读阻塞，写不阻塞。
- share：共享，同步跨进程数据共享。
- file：文件，虚拟文件接口，同步操作。
- window：窗口，创建JFrame窗口，异步，包括绘制、消息。
- net：网络，包括HTTP请求，OkHttp实现，异步。

Dependencies:（使用的开源库，下面为部分）

- JSON格式化：fastjson
- 实现远程命令SSH：netty
- 后端及API：spring-boot
- 网页模版：thymeleaf
- 前端交互：vue
- 前端样式：layui
- Markdown文档转换：flexmark
- 数据结构：guava
- HTTP请求：okhttp
- JAR打包：shadow

#### Manual

[*Simplified Chinese Version*](https://raw.githubusercontent.com/bajdcc/jMiniLang/master/%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E.pdf "Manual - Simplified Chinese")

#### Example

**Web Server**

1. **Spring Boot API**, port 8080
2. **Java NIO**, port 8088
3. Render Markdown using FlexMark

*Front-end: LayUI（前端）*

**1. Spring Boot API**

> Front-end: LayUI + Vue.js
>
> API: Json + RestController
>
> Back-end: jMiniLang API Handler (RING 3 Process)

**Run on Server**

** Online Compiler Example V: GUI User Window **

![Screenshot GUI-1](https://raw.githubusercontent.com/bajdcc/jMiniLang/master/screenshots/gui-1.png)

[window.txt](https://github.com/bajdcc/jMiniLang/blob/master/src/main/resources/com/bajdcc/code/fs/window.txt)

```
import "user.base";
var w = g_window("test window");
var width = 800;
var height = 600;
var border = 10;
w."msg"(0, width, height); // CREATE
w."svg"('M', border, border);
w."svg"('L', width - border, border);
w."svg"('L', width - border, height - border);
w."svg"('L', border, height - border);
w."svg"('L', border, border);
w."svg"('M', border * 2, border * 2);
w."svg"('S', width - border * 4, height - border * 4);
w."str"(1, g_string_rep("Hello world! ", 20));
w."svg"('m', 0, 200);
w."str"(1, g_string_rep("Hello world! ", 20));
w."svg"('m', 0, 200);
w."str"(0, g_string_rep("Hello world! ", 20));
g_sleep_s(1);
w."msg"(2, 0, 0); // WAIT FOR CLOSE
```

** Online Compiler Example IV: Mutex **

```javascript
import "user.base";

var channel = g_pipe("TEST-MUTEX");
var goods = g_share("TEST-MUTEX#GOOD", g_from([]));
var index = g_share("TEST-MUTEX#INDEX", 0);
g_create_dir("/example-mutex");

var new_id = func ~() -> index."set!"(lambda(a) -> a++);
var enqueue = func ~(id) -> goods."get!"(lambda(a) -> a."push"(id));
var dequeue = func ~() -> goods."get!"(lambda(a) -> a."pop"());

var consumer_id = func ~(id) -> "/example-mutex/consumer-" + id;
var producer_id = func ~(id) -> "/example-mutex/producer-" + id;

var consumer = func ~(id) {
    var obj;
    var now = g_get_timestamp();
    channel."writeln"("消费者 #" + id + " 已启动");
    foreach (var i : g_range(1, 5)) {
        while (g_is_null(obj := dequeue())) {}
        channel."writeln"("消费者 #" + id + " 收到：" + obj);
    }
    channel."writeln"("消费者 #" + id + " 已退出");
    var span = g_get_timestamp() - now;
    g_write_file(consumer_id(id), "消费者 #" + id + " 用时 " + span + "ms", true, true);
};

var producer = func ~(id) {
    var obj;
    var now = g_get_timestamp();
    channel."writeln"("生产者 #" + id + " 已启动");
    foreach (var i : g_range(1, 5)) {
        enqueue(obj := new_id());
        channel."writeln"("生产者 #" + id + " 发送：" + obj);
    }
    channel."writeln"("生产者 #" + id + " 已退出");
    var span = g_get_timestamp() - now;
    g_write_file(producer_id(id), "生产者 #" + id + " 用时 " + span + "ms", true, true);
};

var child = false;

foreach (var i : g_range(1, 5)) {
    if (g_fork() == -1) {
        consumer(i);
        child := true;
        break;
    }
    if (g_fork() == -1) {
        producer(i);
        child := true;
        break;
    }
}

if (child) { return; }

if (g_fork() == -1) {
    var i = 0;
    while (i < 10) {
        foreach (var id : g_range(1, 5)) {
            if (g_query_file(consumer_id(id)) == 1) {
                i++;
                channel."writeln"(g_read_file(consumer_id(id)));
                g_delete_file(consumer_id(id));
            }
            if (g_query_file(producer_id(id)) == 1) {
                i++;
                channel."writeln"(g_read_file(producer_id(id)));
                g_delete_file(producer_id(id));
            }
        }
    }
    channel."write"(g_noop_true);
    g_delete_file("/example-mutex");
    return;
}

channel."pipe"(g_system_output());
```

**Output:**

```
运行成功！PID：24
消费者 #1 已启动
生产者 #1 已启动
消费者 #2 已启动
生产者 #2 已启动
消费者 #3 已启动
生产者 #3 已启动
消费者 #4 已启动
生产者 #4 已启动
消费者 #5 已启动
生产者 #5 已启动
生产者 #1 发送：1
消费者 #2 收到：1
生产者 #2 发送：2
消费者 #3 收到：2
生产者 #3 发送：3
...
消费者 #3 已退出
生产者 #4 发送：19
消费者 #4 收到：19
消费者 #4 已退出
生产者 #5 发送：20
消费者 #5 收到：20
消费者 #5 已退出
生产者 #1 发送：21
生产者 #1 已退出
消费者 #1 收到：21
消费者 #1 已退出
生产者 #2 发送：22
生产者 #2 已退出
生产者 #3 发送：23
生产者 #3 已退出
生产者 #4 发送：24
生产者 #4 已退出
生产者 #5 发送：25
生产者 #5 已退出
消费者 #2 收到：22
消费者 #2 收到：23
消费者 #2 收到：24
消费者 #2 收到：25
消费者 #2 已退出
生产者 #1 用时 106ms
消费者 #2 用时 131ms
生产者 #2 用时 107ms
消费者 #3 用时 91ms
生产者 #3 用时 104ms
消费者 #4 用时 88ms
生产者 #4 用时 101ms
消费者 #5 用时 89ms
生产者 #5 用时 100ms
消费者 #1 用时 108ms

正常退出
```

** Online Compiler Example III: Fork **

*`Fork` support `yield`*

```javascript
import "user.base";

var channel = g_pipe("TEST-FORK");

var pid = g_null;
if ((pid := g_fork()) != -1) { // 父进程读取管道
    g_puts("父进程 PID：" + g_pid());
    g_puts("父进程 FORK 返回：" + pid);
    g_puts(channel, "读取管道：");
    channel."pipe"(g_system_output());
} else { // 子进程写入管道
    channel."writeln"("子进程 FORK 返回：" + pid);
    var range = yield ~() { // 枚举器
        for (var i = 0; i < 3; i++) {
            yield g_fork(); // 枚举返回值
        }
    };
    foreach (var i : range()) {
        var txt = "这是一条测试消息！ PID：" + g_pid() + " 编号：" + i;
        channel."writeln"(txt);//写管道
        g_sleep_s(1);
    }
    channel."write"(g_noop_true);//发送管道关闭信号
}

```

**Output:**

```
运行成功！PID：24
父进程 PID：24
父进程 FORK 返回：25
class= system::pipe 字符串(system::pipe)
读取管道：
子进程 FORK 返回：-1
这是一条测试消息！ PID：25 编号：26
这是一条测试消息！ PID：26 编号：-1
这是一条测试消息！ PID：32 编号：-1
这是一条测试消息！ PID：33 编号：-1
这是一条测试消息！ PID：25 编号：32
这是一条测试消息！ PID：26 编号：33
这是一条测试消息！ PID：32 编号：38
这是一条测试消息！ PID：33 编号：39
这是一条测试消息！ PID：38 编号：-1
这是一条测试消息！ PID：39 编号：-1
这是一条测试消息！ PID：40 编号：-1
这是一条测试消息！ PID：41 编号：-1
这是一条测试消息！ PID：25 编号：40
这是一条测试消息！ PID：26 编号：41

正常退出
```

** Online Compiler Example II: Pipe **

**Reader**

```javascript
import "user.base";

var channel = g_pipe("TEST");
g_puts(channel, "读取管道：");
channel."pipe"(g_system_output());//将管道重定向至输出流
```

**Writer**

```javascript
import "user.base";

var channel = g_pipe("TEST");
g_puts(channel, "写入管道：");
for (var i = 0; i < 10; i++) {
    var txt = "这是一条测试消息！ 编号：" + i;
    channel."write"(txt + g_endl);//写管道
    g_puts(txt);
    g_sleep_s(1);
}
g_puts();
channel."write"(g_noop_true);//发送管道关闭信号
```

![Screenshot 108](https://raw.githubusercontent.com/bajdcc/jMiniLang/master/screenshots/web-8.gif)

----

![Screenshot 107](https://raw.githubusercontent.com/bajdcc/jMiniLang/master/screenshots/web-7.gif)

![Screenshot 106](https://raw.githubusercontent.com/bajdcc/jMiniLang/master/screenshots/web-6.png)

![Screenshot 107](https://raw.githubusercontent.com/bajdcc/jMiniLang/master/screenshots/web-7.gif)

** Online Compiler Example I: Hanoi **

[hanoi.txt](https://github.com/bajdcc/jMiniLang/blob/master/src/main/resources/com/bajdcc/code/fs/example/hanoi.txt)

```javascript
import "user.base";
var move = func ~(i, x, y) ->
    g_puts(g_to_string(i) + ": " + g_to_string(x) + " -> " + g_to_string(y));
var h = call (func ~(f) ->
    call (func [
    "实现Y Combinator",
    "Y = f -> (x -> f x x) (x -> f x x)",
    "相关网页——https://www.cnblogs.com/bajdcc/p/5757410.html"
    ] ~(h) -> h(h))(
        lambda(x) -> lambda(i, a, b, c) ->
            call (f(x(x)))(i, a, b, c)))
(lambda(f) -> lambda(i, a, b, c) {
    if (i == 1) {
        move(i, a, c);
    } else {
        f(i - 1, a, c, b);
        move(i, a, c);
        f(i - 1, b, a, c);
    }
});
h(3, 'A', 'B', 'C');
```

**Online Documentation**

![Screenshot 105](https://raw.githubusercontent.com/bajdcc/jMiniLang/master/screenshots/web-5.png)

![Screenshot 102](https://raw.githubusercontent.com/bajdcc/jMiniLang/master/screenshots/web-4.png)

![Screenshot 103](https://raw.githubusercontent.com/bajdcc/jMiniLang/master/screenshots/web-3.gif)

**Back-end**

[api.txt](https://github.com/bajdcc/jMiniLang/blob/master/src/main/resources/com/bajdcc/code/fs/api.txt)

----

**2. Java NIO**

[URTest.txt](https://github.com/bajdcc/jMiniLang/blob/master/src/main/resources/com/bajdcc/code/os/user/routine/URTest.txt#L1037)

![Screenshot 101](https://raw.githubusercontent.com/bajdcc/jMiniLang/master/screenshots/web-1.png)

**User mode**
![Screenshot 100](https://raw.githubusercontent.com/bajdcc/jMiniLang/master/screenshots/usermode-1.gif)

**LINQ Example**

[URTest.txt](https://github.com/bajdcc/jMiniLang/blob/master/src/main/resources/com/bajdcc/code/os/user/routine/URTest.txt#L899)

**Bash Example**

[URTest.txt](https://github.com/bajdcc/jMiniLang/blob/master/src/main/resources/com/bajdcc/code/os/user/routine/URTest.txt#L724)

**Tail optimization （尾递归优化）**

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

**0. Class （Omitted 省略）**

[URTest.txt](https://github.com/bajdcc/jMiniLang/blob/master/src/main/resources/com/bajdcc/code/os/user/routine/URTest.txt#L578)

**1. Lambda: Y Combinator of Hanoi （见上面的例子）**

Hidden, see *Online Compiler Example I: Hanoi* above. 

**2. Lambda: Trampoline （Omitted 省略）**

**3. List: LinkedList （Omitted 省略）**

**4. Multi-Process: Pipe （Omitted 省略）**

**5. Multi-Process: Consumer-Producer Model （生产者-消费者模型）**

*See online compiler example above. 见上面的例子。*

**6. Multi-Process: PC and Router （多进程，Omitted 省略）**

**7. Functional programming （函数式编程）**

[ModuleFunction.txt](https://github.com/bajdcc/jMiniLang/blob/master/src/main/resources/com/bajdcc/code/module/ModuleFunction.txt)

**以上省略的内容可见此README的历史版本。**

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