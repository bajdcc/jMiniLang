
# jMiniLang - A Simple Interpreter (*Java*)
===========================

***jMiniLang*** is a simplified interpreter framework. Developed by ***bajdcc***.
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

#### What it generates

- Structures of *Regex*, *NFA* and *DFA* Table.
- Structures of *LL*/*LR* Table.
- Structures of semantic instructions.
- Structures of syntax tree.
- Structures of code page.
- Virtual machine instructions.
- Runtime environment.

#### Manual

[*Simplified Chinese Version*](https://raw.githubusercontent.com/bajdcc/jMiniLang/master/%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E.pdf "Manual - Simplified Chinese")

#### Example

**Lambda: Y Combinator of Hanoi**

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

**Lambda: Trampoline**

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

**List: LinkedList**

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

#### Screenshot

*Screenshot 1 - Code*
![Screenshot 1](https://bajdcc.github.io/host/screenshot/jMiniLang_1.png)

*Screenshot 2 - Results*
![Screenshot 2](https://bajdcc.github.io/host/screenshot/jMiniLang_2.png)

*Screenshot 3 - Y-Combinator*
![Screenshot 3](https://bajdcc.github.io/host/screenshot/jMiniLang_3.png)
