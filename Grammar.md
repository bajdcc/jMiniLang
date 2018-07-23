# 初始状态 #
状态[0]： program[0] -> * stmt_list [0] 

# 状态转换图 #
## 状态[0]： 

项目：program[0] -> * stmt_list [0] 

规则：program

----

&emsp;&emsp;&emsp;&emsp;到达状态[1]: stmt_list[0] -> * stmt [0]  [ stmt_list [1] ] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[21: `FOR`，关键字，FOR]&emsp;[54: `THROW`，关键字，THROW]&emsp;[41: `VARIABLE`，关键字，VARIABLE]&emsp;[1: `BOOLEAN`，布尔，(null)]&emsp;[44: `CALL`，关键字，CALL]&emsp;[3: `CHARACTER`，字符，(null)]&emsp;[23: `IF`，关键字，IF]&emsp;[50: `SET`，关键字，SET]&emsp;[5: `DECIMAL`，实数，(null)]&emsp;[96: `LPA`，操作符，LPARAN]&emsp;[45: `IMPORT`，关键字，IMPORT]&emsp;[51: `INVOKE`，关键字，INVOKE]&emsp;[47: `YIELD`，关键字，YIELD]&emsp;[0: `ID`，标识符，(null)]&emsp;[27: `RETURN`，关键字，RETURN]&emsp;[49: `LAMBDA`，关键字，LAMBDA]&emsp;[57: `INC_OP`，操作符，PLUS_PLUS]&emsp;[4: `INTEGER`，整数，(null)]&emsp;[58: `DEC_OP`，操作符，MINUS_MINUS]&emsp;[46: `EXPORT`，关键字，EXPORT]&emsp;[100: `LBR`，操作符，LBRACE]&emsp;[92: `SEMI`，操作符，SEMI]&emsp;[86: `NOT`，操作符，BIT_NOT]&emsp;[87: `NOT_OP`，操作符，LOGICAL_NOT]&emsp;[48: `FOREACH`，关键字，FOREACH]&emsp;[98: `LSQ`，操作符，LSQUARE]&emsp;[43: `LET`，关键字，LET]&emsp;[8: `BREAK`，关键字，BREAK]&emsp;[52: `TRY`，关键字，TRY]&emsp;[12: `CONTINUE`，关键字，CONTINUE]&emsp;[40: `WHILE`，关键字，WHILE]&emsp;[2: `LITERAL`，字符串，(null)]&emsp;



## 状态[1]： 

项目：stmt_list[0] -> * stmt [0]  [ stmt_list [1] ] 

规则：stmt_list

----

&emsp;&emsp;&emsp;&emsp;到达状态[2]: stmt[0] -> * ( stmt_stmt [0] | stmt_ctrl [0] | block_stmt [0] ) 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[21: `FOR`，关键字，FOR]&emsp;[54: `THROW`，关键字，THROW]&emsp;[41: `VARIABLE`，关键字，VARIABLE]&emsp;[1: `BOOLEAN`，布尔，(null)]&emsp;[44: `CALL`，关键字，CALL]&emsp;[3: `CHARACTER`，字符，(null)]&emsp;[23: `IF`，关键字，IF]&emsp;[50: `SET`，关键字，SET]&emsp;[5: `DECIMAL`，实数，(null)]&emsp;[96: `LPA`，操作符，LPARAN]&emsp;[45: `IMPORT`，关键字，IMPORT]&emsp;[51: `INVOKE`，关键字，INVOKE]&emsp;[47: `YIELD`，关键字，YIELD]&emsp;[0: `ID`，标识符，(null)]&emsp;[27: `RETURN`，关键字，RETURN]&emsp;[49: `LAMBDA`，关键字，LAMBDA]&emsp;[57: `INC_OP`，操作符，PLUS_PLUS]&emsp;[4: `INTEGER`，整数，(null)]&emsp;[58: `DEC_OP`，操作符，MINUS_MINUS]&emsp;[46: `EXPORT`，关键字，EXPORT]&emsp;[100: `LBR`，操作符，LBRACE]&emsp;[92: `SEMI`，操作符，SEMI]&emsp;[86: `NOT`，操作符，BIT_NOT]&emsp;[87: `NOT_OP`，操作符，LOGICAL_NOT]&emsp;[48: `FOREACH`，关键字，FOREACH]&emsp;[98: `LSQ`，操作符，LSQUARE]&emsp;[43: `LET`，关键字，LET]&emsp;[8: `BREAK`，关键字，BREAK]&emsp;[52: `TRY`，关键字，TRY]&emsp;[12: `CONTINUE`，关键字，CONTINUE]&emsp;[40: `WHILE`，关键字，WHILE]&emsp;[2: `LITERAL`，字符串，(null)]&emsp;



## 状态[2]： 

项目：stmt[0] -> * ( stmt_stmt [0] | stmt_ctrl [0] | block_stmt [0] ) 

规则：stmt

----

&emsp;&emsp;&emsp;&emsp;到达状态[3]: stmt_stmt[0] -> * [ stmt_exp [0] ]  `SEMI` 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[57: `INC_OP`，操作符，PLUS_PLUS]&emsp;[4: `INTEGER`，整数，(null)]&emsp;[41: `VARIABLE`，关键字，VARIABLE]&emsp;[58: `DEC_OP`，操作符，MINUS_MINUS]&emsp;[1: `BOOLEAN`，布尔，(null)]&emsp;[44: `CALL`，关键字，CALL]&emsp;[92: `SEMI`，操作符，SEMI]&emsp;[3: `CHARACTER`，字符，(null)]&emsp;[100: `LBR`，操作符，LBRACE]&emsp;[86: `NOT`，操作符，BIT_NOT]&emsp;[87: `NOT_OP`，操作符，LOGICAL_NOT]&emsp;[50: `SET`，关键字，SET]&emsp;[5: `DECIMAL`，实数，(null)]&emsp;[98: `LSQ`，操作符，LSQUARE]&emsp;[43: `LET`，关键字，LET]&emsp;[8: `BREAK`，关键字，BREAK]&emsp;[96: `LPA`，操作符，LPARAN]&emsp;[51: `INVOKE`，关键字，INVOKE]&emsp;[12: `CONTINUE`，关键字，CONTINUE]&emsp;[2: `LITERAL`，字符串，(null)]&emsp;[0: `ID`，标识符，(null)]&emsp;[49: `LAMBDA`，关键字，LAMBDA]&emsp;

----

&emsp;&emsp;&emsp;&emsp;到达状态[4]: stmt_ctrl[0] -> * ( ret [0] | port [0] | if [0] | for [0] | foreach [0] | while [0] | try [0] | throw [0] ) 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[21: `FOR`，关键字，FOR]&emsp;[54: `THROW`，关键字，THROW]&emsp;[46: `EXPORT`，关键字，EXPORT]&emsp;[45: `IMPORT`，关键字，IMPORT]&emsp;[52: `TRY`，关键字，TRY]&emsp;[23: `IF`，关键字，IF]&emsp;[40: `WHILE`，关键字，WHILE]&emsp;[48: `FOREACH`，关键字，FOREACH]&emsp;[47: `YIELD`，关键字，YIELD]&emsp;[27: `RETURN`，关键字，RETURN]&emsp;

----

&emsp;&emsp;&emsp;&emsp;到达状态[5]: block_stmt[0] -> * block [0] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进



## 状态[3]： 

项目：stmt_stmt[0] -> * [ stmt_exp [0] ]  `SEMI` 

规则：stmt_stmt

----

&emsp;&emsp;&emsp;&emsp;到达状态[6]: stmt_exp[0] -> * ( var [0] | call [0] | cycle_ctrl [0] | exp [0] ) 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[57: `INC_OP`，操作符，PLUS_PLUS]&emsp;[4: `INTEGER`，整数，(null)]&emsp;[41: `VARIABLE`，关键字，VARIABLE]&emsp;[58: `DEC_OP`，操作符，MINUS_MINUS]&emsp;[1: `BOOLEAN`，布尔，(null)]&emsp;[44: `CALL`，关键字，CALL]&emsp;[3: `CHARACTER`，字符，(null)]&emsp;[100: `LBR`，操作符，LBRACE]&emsp;[86: `NOT`，操作符，BIT_NOT]&emsp;[87: `NOT_OP`，操作符，LOGICAL_NOT]&emsp;[50: `SET`，关键字，SET]&emsp;[5: `DECIMAL`，实数，(null)]&emsp;[98: `LSQ`，操作符，LSQUARE]&emsp;[43: `LET`，关键字，LET]&emsp;[8: `BREAK`，关键字，BREAK]&emsp;[96: `LPA`，操作符，LPARAN]&emsp;[51: `INVOKE`，关键字，INVOKE]&emsp;[12: `CONTINUE`，关键字，CONTINUE]&emsp;[2: `LITERAL`，字符串，(null)]&emsp;[0: `ID`，标识符，(null)]&emsp;[49: `LAMBDA`，关键字，LAMBDA]&emsp;

----

&emsp;&emsp;&emsp;&emsp;到达状态[7]: stmt_stmt[0] ->  [ stmt_exp [0] ]  `SEMI`  *

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 92(92: `SEMI`，操作符，SEMI)

&emsp;&emsp;&emsp;&emsp;指令：通过  [Action]



## 状态[4]： 

项目：stmt_ctrl[0] -> * ( ret [0] | port [0] | if [0] | for [0] | foreach [0] | while [0] | try [0] | throw [0] ) 

规则：stmt_ctrl

----

&emsp;&emsp;&emsp;&emsp;到达状态[8]: ret[0] -> * ( `YIELD` [1] | `RETURN` )  [ exp [0] ]  `SEMI` 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[47: `YIELD`，关键字，YIELD]&emsp;[27: `RETURN`，关键字，RETURN]&emsp;

----

&emsp;&emsp;&emsp;&emsp;到达状态[9]: port[0] -> * ( `IMPORT` [1] | `EXPORT` [2] )  `LITERAL` [0]  `SEMI` 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[46: `EXPORT`，关键字，EXPORT]&emsp;[45: `IMPORT`，关键字，IMPORT]&emsp;

----

&emsp;&emsp;&emsp;&emsp;到达状态[10]: if[0] -> * `IF`  `LPA`  exp [0]  `RPA`  block [1]  [ `ELSE`  ( block [2] | if [3] ) ] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[23: `IF`，关键字，IF]&emsp;

----

&emsp;&emsp;&emsp;&emsp;到达状态[11]: for[0] -> * `FOR`  `LPA`  [ var [0] ]  `SEMI`  [ exp [1] ]  `SEMI`  [ ( exp [2] | var [2] ) ]  `RPA`  block [3] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[21: `FOR`，关键字，FOR]&emsp;

----

&emsp;&emsp;&emsp;&emsp;到达状态[12]: foreach[0] -> * `FOREACH`  `LPA`  `VARIABLE`  `ID` [0]  `COLON`  exp [1]  `RPA`  block [2] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[48: `FOREACH`，关键字，FOREACH]&emsp;

----

&emsp;&emsp;&emsp;&emsp;到达状态[13]: while[0] -> * `WHILE`  `LPA`  exp [0]  `RPA`  block [1] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[40: `WHILE`，关键字，WHILE]&emsp;

----

&emsp;&emsp;&emsp;&emsp;到达状态[14]: try[0] -> * `TRY`  block [1]  `CATCH`  [ `LPA`  `ID` [0]  `RPA` ]  block [2] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[52: `TRY`，关键字，TRY]&emsp;

----

&emsp;&emsp;&emsp;&emsp;到达状态[15]: throw[0] -> * `THROW`  exp [0]  `SEMI` 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[54: `THROW`，关键字，THROW]&emsp;



## 状态[5]： 

项目：block_stmt[0] -> * block [0] 

规则：block_stmt

----

&emsp;&emsp;&emsp;&emsp;到达状态[16]: block[0] -> * `LBR`  [ stmt_list [0] ]  `RBR` 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[100: `LBR`，操作符，LBRACE]&emsp;



## 状态[6]： 

项目：stmt_exp[0] -> * ( var [0] | call [0] | cycle_ctrl [0] | exp [0] ) 

规则：stmt_exp

----

&emsp;&emsp;&emsp;&emsp;到达状态[17]: var[0] -> * ( `VARIABLE` [11] | `LET` [12] )  `ID` [0]  [ `ASSIGN`  ( func [1] | exp [2] ) ] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[41: `VARIABLE`，关键字，VARIABLE]&emsp;[43: `LET`，关键字，LET]&emsp;

----

&emsp;&emsp;&emsp;&emsp;到达状态[18]: call[0] -> * `CALL` [4]  ( `LPA`  ( func [0] | exp [3] )  `RPA` | `ID` [1] )  `LPA`  [ exp_list [2] ]  `RPA` 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[44: `CALL`，关键字，CALL]&emsp;

----

&emsp;&emsp;&emsp;&emsp;到达状态[19]: cycle_ctrl[0] -> * ( `BREAK` [0] | `CONTINUE` [0] ) 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[8: `BREAK`，关键字，BREAK]&emsp;[12: `CONTINUE`，关键字，CONTINUE]&emsp;

----

&emsp;&emsp;&emsp;&emsp;到达状态[20]: exp[0] -> * exp01 [0]  [ ( `INTEGER` [10] | `DECIMAL` [10] ) ] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[57: `INC_OP`，操作符，PLUS_PLUS]&emsp;[4: `INTEGER`，整数，(null)]&emsp;[58: `DEC_OP`，操作符，MINUS_MINUS]&emsp;[1: `BOOLEAN`，布尔，(null)]&emsp;[3: `CHARACTER`，字符，(null)]&emsp;[100: `LBR`，操作符，LBRACE]&emsp;[86: `NOT`，操作符，BIT_NOT]&emsp;[87: `NOT_OP`，操作符，LOGICAL_NOT]&emsp;[50: `SET`，关键字，SET]&emsp;[5: `DECIMAL`，实数，(null)]&emsp;[98: `LSQ`，操作符，LSQUARE]&emsp;[96: `LPA`，操作符，LPARAN]&emsp;[51: `INVOKE`，关键字，INVOKE]&emsp;[2: `LITERAL`，字符串，(null)]&emsp;[0: `ID`，标识符，(null)]&emsp;[49: `LAMBDA`，关键字，LAMBDA]&emsp;



## 状态[7]： 

项目：stmt_stmt[0] ->  [ stmt_exp [0] ]  `SEMI`  *

规则：stmt_stmt

----

&emsp;&emsp;&emsp;&emsp;到达状态[21]: stmt[0] ->  ( stmt_stmt [0] | stmt_ctrl [0] | block_stmt [0] )  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> stmt[0] -> * ( stmt_stmt [0] | stmt_ctrl [0] | block_stmt [0] ) 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> stmt_stmt 0



## 状态[8]： 

项目：ret[0] -> * ( `YIELD` [1] | `RETURN` )  [ exp [0] ]  `SEMI` 

规则：ret

----

&emsp;&emsp;&emsp;&emsp;到达状态[22]: ret[0] ->  ( `YIELD` [1] | `RETURN` )  * [ exp [0] ]  `SEMI` 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 47(47: `YIELD`，关键字，YIELD)

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 1

----

&emsp;&emsp;&emsp;&emsp;到达状态[22]: ret[0] ->  ( `YIELD` [1] | `RETURN` )  * [ exp [0] ]  `SEMI` 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 27(27: `RETURN`，关键字，RETURN)

&emsp;&emsp;&emsp;&emsp;指令：通过



## 状态[9]： 

项目：port[0] -> * ( `IMPORT` [1] | `EXPORT` [2] )  `LITERAL` [0]  `SEMI` 

规则：port

----

&emsp;&emsp;&emsp;&emsp;到达状态[23]: port[0] ->  ( `IMPORT` [1] | `EXPORT` [2] )  * `LITERAL` [0]  `SEMI` 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 45(45: `IMPORT`，关键字，IMPORT)

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 1

----

&emsp;&emsp;&emsp;&emsp;到达状态[23]: port[0] ->  ( `IMPORT` [1] | `EXPORT` [2] )  * `LITERAL` [0]  `SEMI` 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 46(46: `EXPORT`，关键字，EXPORT)

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 2



## 状态[10]： 

项目：if[0] -> * `IF`  `LPA`  exp [0]  `RPA`  block [1]  [ `ELSE`  ( block [2] | if [3] ) ] 

规则：if

----

&emsp;&emsp;&emsp;&emsp;到达状态[24]: if[0] ->  `IF`  * `LPA`  exp [0]  `RPA`  block [1]  [ `ELSE`  ( block [2] | if [3] ) ] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 23(23: `IF`，关键字，IF)

&emsp;&emsp;&emsp;&emsp;指令：通过



## 状态[11]： 

项目：for[0] -> * `FOR`  `LPA`  [ var [0] ]  `SEMI`  [ exp [1] ]  `SEMI`  [ ( exp [2] | var [2] ) ]  `RPA`  block [3] 

规则：for

----

&emsp;&emsp;&emsp;&emsp;到达状态[25]: for[0] ->  `FOR`  * `LPA`  [ var [0] ]  `SEMI`  [ exp [1] ]  `SEMI`  [ ( exp [2] | var [2] ) ]  `RPA`  block [3] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 21(21: `FOR`，关键字，FOR)

&emsp;&emsp;&emsp;&emsp;指令：通过  [Action]



## 状态[12]： 

项目：foreach[0] -> * `FOREACH`  `LPA`  `VARIABLE`  `ID` [0]  `COLON`  exp [1]  `RPA`  block [2] 

规则：foreach

----

&emsp;&emsp;&emsp;&emsp;到达状态[26]: foreach[0] ->  `FOREACH`  * `LPA`  `VARIABLE`  `ID` [0]  `COLON`  exp [1]  `RPA`  block [2] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 48(48: `FOREACH`，关键字，FOREACH)

&emsp;&emsp;&emsp;&emsp;指令：通过  [Action]



## 状态[13]： 

项目：while[0] -> * `WHILE`  `LPA`  exp [0]  `RPA`  block [1] 

规则：while

----

&emsp;&emsp;&emsp;&emsp;到达状态[27]: while[0] ->  `WHILE`  * `LPA`  exp [0]  `RPA`  block [1] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 40(40: `WHILE`，关键字，WHILE)

&emsp;&emsp;&emsp;&emsp;指令：通过  [Action]



## 状态[14]： 

项目：try[0] -> * `TRY`  block [1]  `CATCH`  [ `LPA`  `ID` [0]  `RPA` ]  block [2] 

规则：try

----

&emsp;&emsp;&emsp;&emsp;到达状态[28]: try[0] ->  `TRY`  * block [1]  `CATCH`  [ `LPA`  `ID` [0]  `RPA` ]  block [2] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 52(52: `TRY`，关键字，TRY)

&emsp;&emsp;&emsp;&emsp;指令：通过



## 状态[15]： 

项目：throw[0] -> * `THROW`  exp [0]  `SEMI` 

规则：throw

----

&emsp;&emsp;&emsp;&emsp;到达状态[29]: throw[0] ->  `THROW`  * exp [0]  `SEMI` 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 54(54: `THROW`，关键字，THROW)

&emsp;&emsp;&emsp;&emsp;指令：通过



## 状态[16]： 

项目：block[0] -> * `LBR`  [ stmt_list [0] ]  `RBR` 

规则：block

----

&emsp;&emsp;&emsp;&emsp;到达状态[30]: block[0] ->  `LBR`  * [ stmt_list [0] ]  `RBR` 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 100(100: `LBR`，操作符，LBRACE)

&emsp;&emsp;&emsp;&emsp;指令：通过  [Action]



## 状态[17]： 

项目：var[0] -> * ( `VARIABLE` [11] | `LET` [12] )  `ID` [0]  [ `ASSIGN`  ( func [1] | exp [2] ) ] 

规则：var

----

&emsp;&emsp;&emsp;&emsp;到达状态[31]: var[0] ->  ( `VARIABLE` [11] | `LET` [12] )  * `ID` [0]  [ `ASSIGN`  ( func [1] | exp [2] ) ] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 41(41: `VARIABLE`，关键字，VARIABLE)

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 11

----

&emsp;&emsp;&emsp;&emsp;到达状态[31]: var[0] ->  ( `VARIABLE` [11] | `LET` [12] )  * `ID` [0]  [ `ASSIGN`  ( func [1] | exp [2] ) ] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 43(43: `LET`，关键字，LET)

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 12



## 状态[18]： 

项目：call[0] -> * `CALL` [4]  ( `LPA`  ( func [0] | exp [3] )  `RPA` | `ID` [1] )  `LPA`  [ exp_list [2] ]  `RPA` 

规则：call

----

&emsp;&emsp;&emsp;&emsp;到达状态[32]: call[0] ->  `CALL` [4]  * ( `LPA`  ( func [0] | exp [3] )  `RPA` | `ID` [1] )  `LPA`  [ exp_list [2] ]  `RPA` 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 44(44: `CALL`，关键字，CALL)

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 4



## 状态[19]： 

项目：cycle_ctrl[0] -> * ( `BREAK` [0] | `CONTINUE` [0] ) 

规则：cycle_ctrl

----

&emsp;&emsp;&emsp;&emsp;到达状态[33]: cycle_ctrl[0] ->  ( `BREAK` [0] | `CONTINUE` [0] )  *

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 8(8: `BREAK`，关键字，BREAK)

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 0

----

&emsp;&emsp;&emsp;&emsp;到达状态[33]: cycle_ctrl[0] ->  ( `BREAK` [0] | `CONTINUE` [0] )  *

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 12(12: `CONTINUE`，关键字，CONTINUE)

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 0



## 状态[20]： 

项目：exp[0] -> * exp01 [0]  [ ( `INTEGER` [10] | `DECIMAL` [10] ) ] 

规则：exp

----

&emsp;&emsp;&emsp;&emsp;到达状态[34]: exp01[0] -> * [ exp01 [1]  ( `EQ_ASSIGN` [2] | `ADD_ASSIGN` [2] | `SUB_ASSIGN` [2] | `MUL_ASSIGN` [2] | `DIV_ASSIGN` [2] | `AND_ASSIGN` [2] | `OR_ASSIGN` [2] | `XOR_ASSIGN` [2] | `MOD_ASSIGN` [2] ) ]  exp0 [0] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[57: `INC_OP`，操作符，PLUS_PLUS]&emsp;[4: `INTEGER`，整数，(null)]&emsp;[58: `DEC_OP`，操作符，MINUS_MINUS]&emsp;[1: `BOOLEAN`，布尔，(null)]&emsp;[44: `CALL`，关键字，CALL]&emsp;[3: `CHARACTER`，字符，(null)]&emsp;[100: `LBR`，操作符，LBRACE]&emsp;[86: `NOT`，操作符，BIT_NOT]&emsp;[87: `NOT_OP`，操作符，LOGICAL_NOT]&emsp;[50: `SET`，关键字，SET]&emsp;[5: `DECIMAL`，实数，(null)]&emsp;[98: `LSQ`，操作符，LSQUARE]&emsp;[96: `LPA`，操作符，LPARAN]&emsp;[51: `INVOKE`，关键字，INVOKE]&emsp;[2: `LITERAL`，字符串，(null)]&emsp;[0: `ID`，标识符，(null)]&emsp;[49: `LAMBDA`，关键字，LAMBDA]&emsp;



## 状态[21]： 

项目：stmt[0] ->  ( stmt_stmt [0] | stmt_ctrl [0] | block_stmt [0] )  *

规则：stmt

----

&emsp;&emsp;&emsp;&emsp;到达状态[35]: stmt_list[0] ->  stmt [0]  * [ stmt_list [1] ] 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> stmt_list[0] -> * stmt [0]  [ stmt_list [1] ] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> stmt 0



## 状态[22]： 

项目：ret[0] ->  ( `YIELD` [1] | `RETURN` )  * [ exp [0] ]  `SEMI` 

规则：ret

----

&emsp;&emsp;&emsp;&emsp;到达状态[20]: exp[0] -> * exp01 [0]  [ ( `INTEGER` [10] | `DECIMAL` [10] ) ] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[57: `INC_OP`，操作符，PLUS_PLUS]&emsp;[4: `INTEGER`，整数，(null)]&emsp;[58: `DEC_OP`，操作符，MINUS_MINUS]&emsp;[1: `BOOLEAN`，布尔，(null)]&emsp;[44: `CALL`，关键字，CALL]&emsp;[3: `CHARACTER`，字符，(null)]&emsp;[100: `LBR`，操作符，LBRACE]&emsp;[86: `NOT`，操作符，BIT_NOT]&emsp;[87: `NOT_OP`，操作符，LOGICAL_NOT]&emsp;[50: `SET`，关键字，SET]&emsp;[5: `DECIMAL`，实数，(null)]&emsp;[98: `LSQ`，操作符，LSQUARE]&emsp;[96: `LPA`，操作符，LPARAN]&emsp;[51: `INVOKE`，关键字，INVOKE]&emsp;[2: `LITERAL`，字符串，(null)]&emsp;[0: `ID`，标识符，(null)]&emsp;[49: `LAMBDA`，关键字，LAMBDA]&emsp;

----

&emsp;&emsp;&emsp;&emsp;到达状态[36]: ret[0] ->  ( `YIELD` [1] | `RETURN` )  [ exp [0] ]  `SEMI`  *

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 92(92: `SEMI`，操作符，SEMI)

&emsp;&emsp;&emsp;&emsp;指令：通过



## 状态[23]： 

项目：port[0] ->  ( `IMPORT` [1] | `EXPORT` [2] )  * `LITERAL` [0]  `SEMI` 

规则：port

----

&emsp;&emsp;&emsp;&emsp;到达状态[37]: port[0] ->  ( `IMPORT` [1] | `EXPORT` [2] )  `LITERAL` [0]  * `SEMI` 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 2(2: `LITERAL`，字符串，(null))

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 0



## 状态[24]： 

项目：if[0] ->  `IF`  * `LPA`  exp [0]  `RPA`  block [1]  [ `ELSE`  ( block [2] | if [3] ) ] 

规则：if

----

&emsp;&emsp;&emsp;&emsp;到达状态[38]: if[0] ->  `IF`  `LPA`  * exp [0]  `RPA`  block [1]  [ `ELSE`  ( block [2] | if [3] ) ] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 96(96: `LPA`，操作符，LPARAN)

&emsp;&emsp;&emsp;&emsp;指令：通过



## 状态[25]： 

项目：for[0] ->  `FOR`  * `LPA`  [ var [0] ]  `SEMI`  [ exp [1] ]  `SEMI`  [ ( exp [2] | var [2] ) ]  `RPA`  block [3] 

规则：for

----

&emsp;&emsp;&emsp;&emsp;到达状态[39]: for[0] ->  `FOR`  `LPA`  * [ var [0] ]  `SEMI`  [ exp [1] ]  `SEMI`  [ ( exp [2] | var [2] ) ]  `RPA`  block [3] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 96(96: `LPA`，操作符，LPARAN)

&emsp;&emsp;&emsp;&emsp;指令：通过



## 状态[26]： 

项目：foreach[0] ->  `FOREACH`  * `LPA`  `VARIABLE`  `ID` [0]  `COLON`  exp [1]  `RPA`  block [2] 

规则：foreach

----

&emsp;&emsp;&emsp;&emsp;到达状态[40]: foreach[0] ->  `FOREACH`  `LPA`  * `VARIABLE`  `ID` [0]  `COLON`  exp [1]  `RPA`  block [2] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 96(96: `LPA`，操作符，LPARAN)

&emsp;&emsp;&emsp;&emsp;指令：通过



## 状态[27]： 

项目：while[0] ->  `WHILE`  * `LPA`  exp [0]  `RPA`  block [1] 

规则：while

----

&emsp;&emsp;&emsp;&emsp;到达状态[41]: while[0] ->  `WHILE`  `LPA`  * exp [0]  `RPA`  block [1] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 96(96: `LPA`，操作符，LPARAN)

&emsp;&emsp;&emsp;&emsp;指令：通过



## 状态[28]： 

项目：try[0] ->  `TRY`  * block [1]  `CATCH`  [ `LPA`  `ID` [0]  `RPA` ]  block [2] 

规则：try

----

&emsp;&emsp;&emsp;&emsp;到达状态[16]: block[0] -> * `LBR`  [ stmt_list [0] ]  `RBR` 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[100: `LBR`，操作符，LBRACE]&emsp;



## 状态[29]： 

项目：throw[0] ->  `THROW`  * exp [0]  `SEMI` 

规则：throw

----

&emsp;&emsp;&emsp;&emsp;到达状态[20]: exp[0] -> * exp01 [0]  [ ( `INTEGER` [10] | `DECIMAL` [10] ) ] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[57: `INC_OP`，操作符，PLUS_PLUS]&emsp;[4: `INTEGER`，整数，(null)]&emsp;[58: `DEC_OP`，操作符，MINUS_MINUS]&emsp;[1: `BOOLEAN`，布尔，(null)]&emsp;[44: `CALL`，关键字，CALL]&emsp;[3: `CHARACTER`，字符，(null)]&emsp;[100: `LBR`，操作符，LBRACE]&emsp;[86: `NOT`，操作符，BIT_NOT]&emsp;[87: `NOT_OP`，操作符，LOGICAL_NOT]&emsp;[50: `SET`，关键字，SET]&emsp;[5: `DECIMAL`，实数，(null)]&emsp;[98: `LSQ`，操作符，LSQUARE]&emsp;[96: `LPA`，操作符，LPARAN]&emsp;[51: `INVOKE`，关键字，INVOKE]&emsp;[2: `LITERAL`，字符串，(null)]&emsp;[0: `ID`，标识符，(null)]&emsp;[49: `LAMBDA`，关键字，LAMBDA]&emsp;



## 状态[30]： 

项目：block[0] ->  `LBR`  * [ stmt_list [0] ]  `RBR` 

规则：block

----

&emsp;&emsp;&emsp;&emsp;到达状态[1]: stmt_list[0] -> * stmt [0]  [ stmt_list [1] ] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[21: `FOR`，关键字，FOR]&emsp;[54: `THROW`，关键字，THROW]&emsp;[41: `VARIABLE`，关键字，VARIABLE]&emsp;[1: `BOOLEAN`，布尔，(null)]&emsp;[44: `CALL`，关键字，CALL]&emsp;[3: `CHARACTER`，字符，(null)]&emsp;[23: `IF`，关键字，IF]&emsp;[50: `SET`，关键字，SET]&emsp;[5: `DECIMAL`，实数，(null)]&emsp;[96: `LPA`，操作符，LPARAN]&emsp;[45: `IMPORT`，关键字，IMPORT]&emsp;[51: `INVOKE`，关键字，INVOKE]&emsp;[47: `YIELD`，关键字，YIELD]&emsp;[0: `ID`，标识符，(null)]&emsp;[27: `RETURN`，关键字，RETURN]&emsp;[49: `LAMBDA`，关键字，LAMBDA]&emsp;[57: `INC_OP`，操作符，PLUS_PLUS]&emsp;[4: `INTEGER`，整数，(null)]&emsp;[58: `DEC_OP`，操作符，MINUS_MINUS]&emsp;[46: `EXPORT`，关键字，EXPORT]&emsp;[100: `LBR`，操作符，LBRACE]&emsp;[92: `SEMI`，操作符，SEMI]&emsp;[86: `NOT`，操作符，BIT_NOT]&emsp;[87: `NOT_OP`，操作符，LOGICAL_NOT]&emsp;[48: `FOREACH`，关键字，FOREACH]&emsp;[98: `LSQ`，操作符，LSQUARE]&emsp;[43: `LET`，关键字，LET]&emsp;[8: `BREAK`，关键字，BREAK]&emsp;[52: `TRY`，关键字，TRY]&emsp;[12: `CONTINUE`，关键字，CONTINUE]&emsp;[40: `WHILE`，关键字，WHILE]&emsp;[2: `LITERAL`，字符串，(null)]&emsp;

----

&emsp;&emsp;&emsp;&emsp;到达状态[42]: block[0] ->  `LBR`  [ stmt_list [0] ]  `RBR`  *

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 101(101: `RBR`，操作符，RBRACE)

&emsp;&emsp;&emsp;&emsp;指令：通过  [Action]



## 状态[31]： 

项目：var[0] ->  ( `VARIABLE` [11] | `LET` [12] )  * `ID` [0]  [ `ASSIGN`  ( func [1] | exp [2] ) ] 

规则：var

----

&emsp;&emsp;&emsp;&emsp;到达状态[43]: var[0] ->  ( `VARIABLE` [11] | `LET` [12] )  `ID` [0]  * [ `ASSIGN`  ( func [1] | exp [2] ) ] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 0(0: `ID`，标识符，(null))

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 0  [Action]



## 状态[32]： 

项目：call[0] ->  `CALL` [4]  * ( `LPA`  ( func [0] | exp [3] )  `RPA` | `ID` [1] )  `LPA`  [ exp_list [2] ]  `RPA` 

规则：call

----

&emsp;&emsp;&emsp;&emsp;到达状态[44]: call[0] ->  `CALL` [4]  ( `LPA`  * ( func [0] | exp [3] )  `RPA` | `ID` [1] )  `LPA`  [ exp_list [2] ]  `RPA` 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 96(96: `LPA`，操作符，LPARAN)

&emsp;&emsp;&emsp;&emsp;指令：通过

----

&emsp;&emsp;&emsp;&emsp;到达状态[45]: call[0] ->  `CALL` [4]  ( `LPA`  ( func [0] | exp [3] )  `RPA` | `ID` [1] )  * `LPA`  [ exp_list [2] ]  `RPA` 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 0(0: `ID`，标识符，(null))

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 1



## 状态[33]： 

项目：cycle_ctrl[0] ->  ( `BREAK` [0] | `CONTINUE` [0] )  *

规则：cycle_ctrl

----

&emsp;&emsp;&emsp;&emsp;到达状态[46]: stmt_exp[0] ->  ( var [0] | call [0] | cycle_ctrl [0] | exp [0] )  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> stmt_exp[0] -> * ( var [0] | call [0] | cycle_ctrl [0] | exp [0] ) 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> cycle_ctrl 0



## 状态[34]： 

项目：exp01[0] -> * [ exp01 [1]  ( `EQ_ASSIGN` [2] | `ADD_ASSIGN` [2] | `SUB_ASSIGN` [2] | `MUL_ASSIGN` [2] | `DIV_ASSIGN` [2] | `AND_ASSIGN` [2] | `OR_ASSIGN` [2] | `XOR_ASSIGN` [2] | `MOD_ASSIGN` [2] ) ]  exp0 [0] 

规则：exp01

----

&emsp;&emsp;&emsp;&emsp;到达状态[47]: exp0[0] -> * exp1 [0]  [ `QUERY` [4]  exp0 [6]  `COLON` [5]  exp0 [7] ] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[57: `INC_OP`，操作符，PLUS_PLUS]&emsp;[4: `INTEGER`，整数，(null)]&emsp;[58: `DEC_OP`，操作符，MINUS_MINUS]&emsp;[1: `BOOLEAN`，布尔，(null)]&emsp;[44: `CALL`，关键字，CALL]&emsp;[3: `CHARACTER`，字符，(null)]&emsp;[100: `LBR`，操作符，LBRACE]&emsp;[86: `NOT`，操作符，BIT_NOT]&emsp;[87: `NOT_OP`，操作符，LOGICAL_NOT]&emsp;[50: `SET`，关键字，SET]&emsp;[5: `DECIMAL`，实数，(null)]&emsp;[98: `LSQ`，操作符，LSQUARE]&emsp;[96: `LPA`，操作符，LPARAN]&emsp;[51: `INVOKE`，关键字，INVOKE]&emsp;[2: `LITERAL`，字符串，(null)]&emsp;[0: `ID`，标识符，(null)]&emsp;[49: `LAMBDA`，关键字，LAMBDA]&emsp;



## 状态[35]： 

项目：stmt_list[0] ->  stmt [0]  * [ stmt_list [1] ] 

规则：stmt_list

----

&emsp;&emsp;&emsp;&emsp;到达状态[1]: stmt_list[0] -> * stmt [0]  [ stmt_list [1] ] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[21: `FOR`，关键字，FOR]&emsp;[54: `THROW`，关键字，THROW]&emsp;[41: `VARIABLE`，关键字，VARIABLE]&emsp;[1: `BOOLEAN`，布尔，(null)]&emsp;[44: `CALL`，关键字，CALL]&emsp;[3: `CHARACTER`，字符，(null)]&emsp;[23: `IF`，关键字，IF]&emsp;[50: `SET`，关键字，SET]&emsp;[5: `DECIMAL`，实数，(null)]&emsp;[96: `LPA`，操作符，LPARAN]&emsp;[45: `IMPORT`，关键字，IMPORT]&emsp;[51: `INVOKE`，关键字，INVOKE]&emsp;[47: `YIELD`，关键字，YIELD]&emsp;[0: `ID`，标识符，(null)]&emsp;[27: `RETURN`，关键字，RETURN]&emsp;[49: `LAMBDA`，关键字，LAMBDA]&emsp;[57: `INC_OP`，操作符，PLUS_PLUS]&emsp;[4: `INTEGER`，整数，(null)]&emsp;[58: `DEC_OP`，操作符，MINUS_MINUS]&emsp;[46: `EXPORT`，关键字，EXPORT]&emsp;[100: `LBR`，操作符，LBRACE]&emsp;[92: `SEMI`，操作符，SEMI]&emsp;[86: `NOT`，操作符，BIT_NOT]&emsp;[87: `NOT_OP`，操作符，LOGICAL_NOT]&emsp;[48: `FOREACH`，关键字，FOREACH]&emsp;[98: `LSQ`，操作符，LSQUARE]&emsp;[43: `LET`，关键字，LET]&emsp;[8: `BREAK`，关键字，BREAK]&emsp;[52: `TRY`，关键字，TRY]&emsp;[12: `CONTINUE`，关键字，CONTINUE]&emsp;[40: `WHILE`，关键字，WHILE]&emsp;[2: `LITERAL`，字符串，(null)]&emsp;

----

&emsp;&emsp;&emsp;&emsp;到达状态[48]: program[0] ->  stmt_list [0]  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> program[0] -> * stmt_list [0] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> stmt_list 0

----

&emsp;&emsp;&emsp;&emsp;到达状态[49]: stmt_list[0] ->  stmt [0]  [ stmt_list [1] ]  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> stmt_list[0] ->  stmt [0]  * [ stmt_list [1] ] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> stmt_list 1

----

&emsp;&emsp;&emsp;&emsp;到达状态[50]: block[0] ->  `LBR`  [ stmt_list [0] ]  * `RBR` 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> block[0] ->  `LBR`  * [ stmt_list [0] ]  `RBR` 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> stmt_list 0



## 状态[36]： 

项目：ret[0] ->  ( `YIELD` [1] | `RETURN` )  [ exp [0] ]  `SEMI`  *

规则：ret

----

&emsp;&emsp;&emsp;&emsp;到达状态[51]: stmt_ctrl[0] ->  ( ret [0] | port [0] | if [0] | for [0] | foreach [0] | while [0] | try [0] | throw [0] )  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> stmt_ctrl[0] -> * ( ret [0] | port [0] | if [0] | for [0] | foreach [0] | while [0] | try [0] | throw [0] ) 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> ret 0



## 状态[37]： 

项目：port[0] ->  ( `IMPORT` [1] | `EXPORT` [2] )  `LITERAL` [0]  * `SEMI` 

规则：port

----

&emsp;&emsp;&emsp;&emsp;到达状态[52]: port[0] ->  ( `IMPORT` [1] | `EXPORT` [2] )  `LITERAL` [0]  `SEMI`  *

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 92(92: `SEMI`，操作符，SEMI)

&emsp;&emsp;&emsp;&emsp;指令：通过



## 状态[38]： 

项目：if[0] ->  `IF`  `LPA`  * exp [0]  `RPA`  block [1]  [ `ELSE`  ( block [2] | if [3] ) ] 

规则：if

----

&emsp;&emsp;&emsp;&emsp;到达状态[20]: exp[0] -> * exp01 [0]  [ ( `INTEGER` [10] | `DECIMAL` [10] ) ] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[57: `INC_OP`，操作符，PLUS_PLUS]&emsp;[4: `INTEGER`，整数，(null)]&emsp;[58: `DEC_OP`，操作符，MINUS_MINUS]&emsp;[1: `BOOLEAN`，布尔，(null)]&emsp;[44: `CALL`，关键字，CALL]&emsp;[3: `CHARACTER`，字符，(null)]&emsp;[100: `LBR`，操作符，LBRACE]&emsp;[86: `NOT`，操作符，BIT_NOT]&emsp;[87: `NOT_OP`，操作符，LOGICAL_NOT]&emsp;[50: `SET`，关键字，SET]&emsp;[5: `DECIMAL`，实数，(null)]&emsp;[98: `LSQ`，操作符，LSQUARE]&emsp;[96: `LPA`，操作符，LPARAN]&emsp;[51: `INVOKE`，关键字，INVOKE]&emsp;[2: `LITERAL`，字符串，(null)]&emsp;[0: `ID`，标识符，(null)]&emsp;[49: `LAMBDA`，关键字，LAMBDA]&emsp;



## 状态[39]： 

项目：for[0] ->  `FOR`  `LPA`  * [ var [0] ]  `SEMI`  [ exp [1] ]  `SEMI`  [ ( exp [2] | var [2] ) ]  `RPA`  block [3] 

规则：for

----

&emsp;&emsp;&emsp;&emsp;到达状态[17]: var[0] -> * ( `VARIABLE` [11] | `LET` [12] )  `ID` [0]  [ `ASSIGN`  ( func [1] | exp [2] ) ] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[41: `VARIABLE`，关键字，VARIABLE]&emsp;[43: `LET`，关键字，LET]&emsp;

----

&emsp;&emsp;&emsp;&emsp;到达状态[53]: for[0] ->  `FOR`  `LPA`  [ var [0] ]  `SEMI`  * [ exp [1] ]  `SEMI`  [ ( exp [2] | var [2] ) ]  `RPA`  block [3] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 92(92: `SEMI`，操作符，SEMI)

&emsp;&emsp;&emsp;&emsp;指令：通过



## 状态[40]： 

项目：foreach[0] ->  `FOREACH`  `LPA`  * `VARIABLE`  `ID` [0]  `COLON`  exp [1]  `RPA`  block [2] 

规则：foreach

----

&emsp;&emsp;&emsp;&emsp;到达状态[54]: foreach[0] ->  `FOREACH`  `LPA`  `VARIABLE`  * `ID` [0]  `COLON`  exp [1]  `RPA`  block [2] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 41(41: `VARIABLE`，关键字，VARIABLE)

&emsp;&emsp;&emsp;&emsp;指令：通过



## 状态[41]： 

项目：while[0] ->  `WHILE`  `LPA`  * exp [0]  `RPA`  block [1] 

规则：while

----

&emsp;&emsp;&emsp;&emsp;到达状态[20]: exp[0] -> * exp01 [0]  [ ( `INTEGER` [10] | `DECIMAL` [10] ) ] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[57: `INC_OP`，操作符，PLUS_PLUS]&emsp;[4: `INTEGER`，整数，(null)]&emsp;[58: `DEC_OP`，操作符，MINUS_MINUS]&emsp;[1: `BOOLEAN`，布尔，(null)]&emsp;[44: `CALL`，关键字，CALL]&emsp;[3: `CHARACTER`，字符，(null)]&emsp;[100: `LBR`，操作符，LBRACE]&emsp;[86: `NOT`，操作符，BIT_NOT]&emsp;[87: `NOT_OP`，操作符，LOGICAL_NOT]&emsp;[50: `SET`，关键字，SET]&emsp;[5: `DECIMAL`，实数，(null)]&emsp;[98: `LSQ`，操作符，LSQUARE]&emsp;[96: `LPA`，操作符，LPARAN]&emsp;[51: `INVOKE`，关键字，INVOKE]&emsp;[2: `LITERAL`，字符串，(null)]&emsp;[0: `ID`，标识符，(null)]&emsp;[49: `LAMBDA`，关键字，LAMBDA]&emsp;



## 状态[42]： 

项目：block[0] ->  `LBR`  [ stmt_list [0] ]  `RBR`  *

规则：block

----

&emsp;&emsp;&emsp;&emsp;到达状态[55]: func[0] ->  ( `FUNCTION` [10] | `YIELD` )  [ `LSQ`  doc_list [0]  `RSQ` ]  ( `ID` [1] | `NOT` [1] )  `LPA`  [ var_list [2] ]  `RPA`  ( `PTR_OP`  scope [3] | block [4] )  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> func[0] ->  ( `FUNCTION` [10] | `YIELD` )  [ `LSQ`  doc_list [0]  `RSQ` ]  ( `ID` [1] | `NOT` [1] )  `LPA`  [ var_list [2] ]  `RPA`  * ( `PTR_OP`  scope [3] | block [4] ) 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> block 4

----

&emsp;&emsp;&emsp;&emsp;到达状态[56]: lambda[0] ->  `LAMBDA` [1]  `LPA`  [ var_list [2] ]  `RPA`  ( `PTR_OP`  scope [3] | block [4] )  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> lambda[0] ->  `LAMBDA` [1]  `LPA`  [ var_list [2] ]  `RPA`  * ( `PTR_OP`  scope [3] | block [4] ) 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> block 4

----

&emsp;&emsp;&emsp;&emsp;到达状态[57]: if[0] ->  `IF`  `LPA`  exp [0]  `RPA`  block [1]  * [ `ELSE`  ( block [2] | if [3] ) ] 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> if[0] ->  `IF`  `LPA`  exp [0]  `RPA`  * block [1]  [ `ELSE`  ( block [2] | if [3] ) ] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> block 1

----

&emsp;&emsp;&emsp;&emsp;到达状态[58]: if[0] ->  `IF`  `LPA`  exp [0]  `RPA`  block [1]  [ `ELSE`  ( block [2] | if [3] )  *] 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> if[0] ->  `IF`  `LPA`  exp [0]  `RPA`  block [1]  [ `ELSE`  * ( block [2] | if [3] ) ] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> block 2

----

&emsp;&emsp;&emsp;&emsp;到达状态[59]: for[0] ->  `FOR`  `LPA`  [ var [0] ]  `SEMI`  [ exp [1] ]  `SEMI`  [ ( exp [2] | var [2] ) ]  `RPA`  block [3]  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> for[0] ->  `FOR`  `LPA`  [ var [0] ]  `SEMI`  [ exp [1] ]  `SEMI`  [ ( exp [2] | var [2] ) ]  `RPA`  * block [3] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> block 3

----

&emsp;&emsp;&emsp;&emsp;到达状态[60]: while[0] ->  `WHILE`  `LPA`  exp [0]  `RPA`  block [1]  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> while[0] ->  `WHILE`  `LPA`  exp [0]  `RPA`  * block [1] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> block 1

----

&emsp;&emsp;&emsp;&emsp;到达状态[61]: foreach[0] ->  `FOREACH`  `LPA`  `VARIABLE`  `ID` [0]  `COLON`  exp [1]  `RPA`  block [2]  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> foreach[0] ->  `FOREACH`  `LPA`  `VARIABLE`  `ID` [0]  `COLON`  exp [1]  `RPA`  * block [2] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> block 2

----

&emsp;&emsp;&emsp;&emsp;到达状态[62]: block_stmt[0] ->  block [0]  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> block_stmt[0] -> * block [0] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> block 0

----

&emsp;&emsp;&emsp;&emsp;到达状态[63]: try[0] ->  `TRY`  block [1]  * `CATCH`  [ `LPA`  `ID` [0]  `RPA` ]  block [2] 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> try[0] ->  `TRY`  * block [1]  `CATCH`  [ `LPA`  `ID` [0]  `RPA` ]  block [2] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> block 1

----

&emsp;&emsp;&emsp;&emsp;到达状态[64]: try[0] ->  `TRY`  block [1]  `CATCH`  [ `LPA`  `ID` [0]  `RPA` ]  block [2]  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> try[0] ->  `TRY`  block [1]  `CATCH`  * [ `LPA`  `ID` [0]  `RPA` ]  block [2] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> block 2

----

&emsp;&emsp;&emsp;&emsp;到达状态[64]: try[0] ->  `TRY`  block [1]  `CATCH`  [ `LPA`  `ID` [0]  `RPA` ]  block [2]  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> try[0] ->  `TRY`  block [1]  `CATCH`  [ `LPA`  `ID` [0]  `RPA` ]  * block [2] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> block 2



## 状态[43]： 

项目：var[0] ->  ( `VARIABLE` [11] | `LET` [12] )  `ID` [0]  * [ `ASSIGN`  ( func [1] | exp [2] ) ] 

规则：var

----

&emsp;&emsp;&emsp;&emsp;到达状态[65]: var[0] ->  ( `VARIABLE` [11] | `LET` [12] )  `ID` [0]  [ `ASSIGN`  * ( func [1] | exp [2] ) ] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 94(94: `ASSIGN`，操作符，ASSIGN)

&emsp;&emsp;&emsp;&emsp;指令：通过

----

&emsp;&emsp;&emsp;&emsp;到达状态[46]: stmt_exp[0] ->  ( var [0] | call [0] | cycle_ctrl [0] | exp [0] )  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> stmt_exp[0] -> * ( var [0] | call [0] | cycle_ctrl [0] | exp [0] ) 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> var 0

----

&emsp;&emsp;&emsp;&emsp;到达状态[66]: for[0] ->  `FOR`  `LPA`  [ var [0] ]  * `SEMI`  [ exp [1] ]  `SEMI`  [ ( exp [2] | var [2] ) ]  `RPA`  block [3] 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> for[0] ->  `FOR`  `LPA`  * [ var [0] ]  `SEMI`  [ exp [1] ]  `SEMI`  [ ( exp [2] | var [2] ) ]  `RPA`  block [3] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> var 0

----

&emsp;&emsp;&emsp;&emsp;到达状态[67]: for[0] ->  `FOR`  `LPA`  [ var [0] ]  `SEMI`  [ exp [1] ]  `SEMI`  [ ( exp [2] | var [2] )  *]  `RPA`  block [3] 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> for[0] ->  `FOR`  `LPA`  [ var [0] ]  `SEMI`  [ exp [1] ]  `SEMI`  * [ ( exp [2] | var [2] ) ]  `RPA`  block [3] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> var 2



## 状态[44]： 

项目：call[0] ->  `CALL` [4]  ( `LPA`  * ( func [0] | exp [3] )  `RPA` | `ID` [1] )  `LPA`  [ exp_list [2] ]  `RPA` 

规则：call

----

&emsp;&emsp;&emsp;&emsp;到达状态[68]: func[0] -> * ( `FUNCTION` [10] | `YIELD` )  [ `LSQ`  doc_list [0]  `RSQ` ]  ( `ID` [1] | `NOT` [1] )  `LPA`  [ var_list [2] ]  `RPA`  ( `PTR_OP`  scope [3] | block [4] ) 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[42: `FUNCTION`，关键字，FUNCTION]&emsp;[47: `YIELD`，关键字，YIELD]&emsp;

----

&emsp;&emsp;&emsp;&emsp;到达状态[20]: exp[0] -> * exp01 [0]  [ ( `INTEGER` [10] | `DECIMAL` [10] ) ] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[57: `INC_OP`，操作符，PLUS_PLUS]&emsp;[4: `INTEGER`，整数，(null)]&emsp;[58: `DEC_OP`，操作符，MINUS_MINUS]&emsp;[1: `BOOLEAN`，布尔，(null)]&emsp;[44: `CALL`，关键字，CALL]&emsp;[3: `CHARACTER`，字符，(null)]&emsp;[100: `LBR`，操作符，LBRACE]&emsp;[86: `NOT`，操作符，BIT_NOT]&emsp;[87: `NOT_OP`，操作符，LOGICAL_NOT]&emsp;[50: `SET`，关键字，SET]&emsp;[5: `DECIMAL`，实数，(null)]&emsp;[98: `LSQ`，操作符，LSQUARE]&emsp;[96: `LPA`，操作符，LPARAN]&emsp;[51: `INVOKE`，关键字，INVOKE]&emsp;[2: `LITERAL`，字符串，(null)]&emsp;[0: `ID`，标识符，(null)]&emsp;[49: `LAMBDA`，关键字，LAMBDA]&emsp;



## 状态[45]： 

项目：call[0] ->  `CALL` [4]  ( `LPA`  ( func [0] | exp [3] )  `RPA` | `ID` [1] )  * `LPA`  [ exp_list [2] ]  `RPA` 

规则：call

----

&emsp;&emsp;&emsp;&emsp;到达状态[69]: call[0] ->  `CALL` [4]  ( `LPA`  ( func [0] | exp [3] )  `RPA` | `ID` [1] )  `LPA`  * [ exp_list [2] ]  `RPA` 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 96(96: `LPA`，操作符，LPARAN)

&emsp;&emsp;&emsp;&emsp;指令：通过



## 状态[46]： 

项目：stmt_exp[0] ->  ( var [0] | call [0] | cycle_ctrl [0] | exp [0] )  *

规则：stmt_exp

----

&emsp;&emsp;&emsp;&emsp;到达状态[70]: stmt_stmt[0] ->  [ stmt_exp [0] ]  * `SEMI` 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> stmt_stmt[0] -> * [ stmt_exp [0] ]  `SEMI` 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> stmt_exp 0



## 状态[47]： 

项目：exp0[0] -> * exp1 [0]  [ `QUERY` [4]  exp0 [6]  `COLON` [5]  exp0 [7] ] 

规则：exp0

----

&emsp;&emsp;&emsp;&emsp;到达状态[71]: exp1[0] -> * [ exp1 [1]  ( `AND_OP` [2] | `OR_OP` [2] ) ]  exp2 [0] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[57: `INC_OP`，操作符，PLUS_PLUS]&emsp;[4: `INTEGER`，整数，(null)]&emsp;[58: `DEC_OP`，操作符，MINUS_MINUS]&emsp;[1: `BOOLEAN`，布尔，(null)]&emsp;[44: `CALL`，关键字，CALL]&emsp;[3: `CHARACTER`，字符，(null)]&emsp;[100: `LBR`，操作符，LBRACE]&emsp;[86: `NOT`，操作符，BIT_NOT]&emsp;[87: `NOT_OP`，操作符，LOGICAL_NOT]&emsp;[50: `SET`，关键字，SET]&emsp;[5: `DECIMAL`，实数，(null)]&emsp;[98: `LSQ`，操作符，LSQUARE]&emsp;[96: `LPA`，操作符，LPARAN]&emsp;[51: `INVOKE`，关键字，INVOKE]&emsp;[2: `LITERAL`，字符串，(null)]&emsp;[0: `ID`，标识符，(null)]&emsp;[49: `LAMBDA`，关键字，LAMBDA]&emsp;



## 状态[48]： 

项目：program[0] ->  stmt_list [0]  *

规则：program

----

&emsp;&emsp;&emsp;&emsp;到达状态[48]: program[0] ->  stmt_list [0]  *

&emsp;&emsp;&emsp;&emsp;类型：结束

&emsp;&emsp;&emsp;&emsp;指令：翻译结束	=> program



## 状态[49]： 

项目：stmt_list[0] ->  stmt [0]  [ stmt_list [1] ]  *

规则：stmt_list

----

&emsp;&emsp;&emsp;&emsp;到达状态[48]: program[0] ->  stmt_list [0]  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> program[0] -> * stmt_list [0] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> stmt_list 0

----

&emsp;&emsp;&emsp;&emsp;到达状态[49]: stmt_list[0] ->  stmt [0]  [ stmt_list [1] ]  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> stmt_list[0] ->  stmt [0]  * [ stmt_list [1] ] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> stmt_list 1

----

&emsp;&emsp;&emsp;&emsp;到达状态[50]: block[0] ->  `LBR`  [ stmt_list [0] ]  * `RBR` 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> block[0] ->  `LBR`  * [ stmt_list [0] ]  `RBR` 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> stmt_list 0



## 状态[50]： 

项目：block[0] ->  `LBR`  [ stmt_list [0] ]  * `RBR` 

规则：block

----

&emsp;&emsp;&emsp;&emsp;到达状态[42]: block[0] ->  `LBR`  [ stmt_list [0] ]  `RBR`  *

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 101(101: `RBR`，操作符，RBRACE)

&emsp;&emsp;&emsp;&emsp;指令：通过  [Action]



## 状态[51]： 

项目：stmt_ctrl[0] ->  ( ret [0] | port [0] | if [0] | for [0] | foreach [0] | while [0] | try [0] | throw [0] )  *

规则：stmt_ctrl

----

&emsp;&emsp;&emsp;&emsp;到达状态[21]: stmt[0] ->  ( stmt_stmt [0] | stmt_ctrl [0] | block_stmt [0] )  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> stmt[0] -> * ( stmt_stmt [0] | stmt_ctrl [0] | block_stmt [0] ) 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> stmt_ctrl 0



## 状态[52]： 

项目：port[0] ->  ( `IMPORT` [1] | `EXPORT` [2] )  `LITERAL` [0]  `SEMI`  *

规则：port

----

&emsp;&emsp;&emsp;&emsp;到达状态[51]: stmt_ctrl[0] ->  ( ret [0] | port [0] | if [0] | for [0] | foreach [0] | while [0] | try [0] | throw [0] )  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> stmt_ctrl[0] -> * ( ret [0] | port [0] | if [0] | for [0] | foreach [0] | while [0] | try [0] | throw [0] ) 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> port 0



## 状态[53]： 

项目：for[0] ->  `FOR`  `LPA`  [ var [0] ]  `SEMI`  * [ exp [1] ]  `SEMI`  [ ( exp [2] | var [2] ) ]  `RPA`  block [3] 

规则：for

----

&emsp;&emsp;&emsp;&emsp;到达状态[20]: exp[0] -> * exp01 [0]  [ ( `INTEGER` [10] | `DECIMAL` [10] ) ] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[57: `INC_OP`，操作符，PLUS_PLUS]&emsp;[4: `INTEGER`，整数，(null)]&emsp;[58: `DEC_OP`，操作符，MINUS_MINUS]&emsp;[1: `BOOLEAN`，布尔，(null)]&emsp;[44: `CALL`，关键字，CALL]&emsp;[3: `CHARACTER`，字符，(null)]&emsp;[100: `LBR`，操作符，LBRACE]&emsp;[86: `NOT`，操作符，BIT_NOT]&emsp;[87: `NOT_OP`，操作符，LOGICAL_NOT]&emsp;[50: `SET`，关键字，SET]&emsp;[5: `DECIMAL`，实数，(null)]&emsp;[98: `LSQ`，操作符，LSQUARE]&emsp;[96: `LPA`，操作符，LPARAN]&emsp;[51: `INVOKE`，关键字，INVOKE]&emsp;[2: `LITERAL`，字符串，(null)]&emsp;[0: `ID`，标识符，(null)]&emsp;[49: `LAMBDA`，关键字，LAMBDA]&emsp;

----

&emsp;&emsp;&emsp;&emsp;到达状态[72]: for[0] ->  `FOR`  `LPA`  [ var [0] ]  `SEMI`  [ exp [1] ]  `SEMI`  * [ ( exp [2] | var [2] ) ]  `RPA`  block [3] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 92(92: `SEMI`，操作符，SEMI)

&emsp;&emsp;&emsp;&emsp;指令：通过



## 状态[54]： 

项目：foreach[0] ->  `FOREACH`  `LPA`  `VARIABLE`  * `ID` [0]  `COLON`  exp [1]  `RPA`  block [2] 

规则：foreach

----

&emsp;&emsp;&emsp;&emsp;到达状态[73]: foreach[0] ->  `FOREACH`  `LPA`  `VARIABLE`  `ID` [0]  * `COLON`  exp [1]  `RPA`  block [2] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 0(0: `ID`，标识符，(null))

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 0  [Action]



## 状态[55]： 

项目：func[0] ->  ( `FUNCTION` [10] | `YIELD` )  [ `LSQ`  doc_list [0]  `RSQ` ]  ( `ID` [1] | `NOT` [1] )  `LPA`  [ var_list [2] ]  `RPA`  ( `PTR_OP`  scope [3] | block [4] )  *

规则：func

----

&emsp;&emsp;&emsp;&emsp;到达状态[74]: var[0] ->  ( `VARIABLE` [11] | `LET` [12] )  `ID` [0]  [ `ASSIGN`  ( func [1] | exp [2] )  *] 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> var[0] ->  ( `VARIABLE` [11] | `LET` [12] )  `ID` [0]  [ `ASSIGN`  * ( func [1] | exp [2] ) ] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> func 1

----

&emsp;&emsp;&emsp;&emsp;到达状态[75]: call[0] ->  `CALL` [4]  ( `LPA`  ( func [0] | exp [3] )  * `RPA` | `ID` [1] )  `LPA`  [ exp_list [2] ]  `RPA` 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> call[0] ->  `CALL` [4]  ( `LPA`  * ( func [0] | exp [3] )  `RPA` | `ID` [1] )  `LPA`  [ exp_list [2] ]  `RPA` 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> func 0



## 状态[56]： 

项目：lambda[0] ->  `LAMBDA` [1]  `LPA`  [ var_list [2] ]  `RPA`  ( `PTR_OP`  scope [3] | block [4] )  *

规则：lambda

----

&emsp;&emsp;&emsp;&emsp;到达状态[76]: type[0] ->  ( `ID` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `INTEGER` [0] | `DECIMAL` [0] | `LITERAL` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `CHARACTER` [0] | `BOOLEAN` [0] | `LPA`  exp [1]  `RPA` | call [1] | lambda [2] | set [1] | invoke [1] | array [1] | map [1] )  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> type[0] -> * ( `ID` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `INTEGER` [0] | `DECIMAL` [0] | `LITERAL` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `CHARACTER` [0] | `BOOLEAN` [0] | `LPA`  exp [1]  `RPA` | call [1] | lambda [2] | set [1] | invoke [1] | array [1] | map [1] ) 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> lambda 2



## 状态[57]： 

项目：if[0] ->  `IF`  `LPA`  exp [0]  `RPA`  block [1]  * [ `ELSE`  ( block [2] | if [3] ) ] 

规则：if

----

&emsp;&emsp;&emsp;&emsp;到达状态[77]: if[0] ->  `IF`  `LPA`  exp [0]  `RPA`  block [1]  [ `ELSE`  * ( block [2] | if [3] ) ] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 16(16: `ELSE`，关键字，ELSE)

&emsp;&emsp;&emsp;&emsp;指令：通过

----

&emsp;&emsp;&emsp;&emsp;到达状态[51]: stmt_ctrl[0] ->  ( ret [0] | port [0] | if [0] | for [0] | foreach [0] | while [0] | try [0] | throw [0] )  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> stmt_ctrl[0] -> * ( ret [0] | port [0] | if [0] | for [0] | foreach [0] | while [0] | try [0] | throw [0] ) 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> if 0

----

&emsp;&emsp;&emsp;&emsp;到达状态[58]: if[0] ->  `IF`  `LPA`  exp [0]  `RPA`  block [1]  [ `ELSE`  ( block [2] | if [3] )  *] 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> if[0] ->  `IF`  `LPA`  exp [0]  `RPA`  block [1]  [ `ELSE`  * ( block [2] | if [3] ) ] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> if 3



## 状态[58]： 

项目：if[0] ->  `IF`  `LPA`  exp [0]  `RPA`  block [1]  [ `ELSE`  ( block [2] | if [3] )  *] 

规则：if

----

&emsp;&emsp;&emsp;&emsp;到达状态[51]: stmt_ctrl[0] ->  ( ret [0] | port [0] | if [0] | for [0] | foreach [0] | while [0] | try [0] | throw [0] )  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> stmt_ctrl[0] -> * ( ret [0] | port [0] | if [0] | for [0] | foreach [0] | while [0] | try [0] | throw [0] ) 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> if 0

----

&emsp;&emsp;&emsp;&emsp;到达状态[58]: if[0] ->  `IF`  `LPA`  exp [0]  `RPA`  block [1]  [ `ELSE`  ( block [2] | if [3] )  *] 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> if[0] ->  `IF`  `LPA`  exp [0]  `RPA`  block [1]  [ `ELSE`  * ( block [2] | if [3] ) ] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> if 3



## 状态[59]： 

项目：for[0] ->  `FOR`  `LPA`  [ var [0] ]  `SEMI`  [ exp [1] ]  `SEMI`  [ ( exp [2] | var [2] ) ]  `RPA`  block [3]  *

规则：for

----

&emsp;&emsp;&emsp;&emsp;到达状态[51]: stmt_ctrl[0] ->  ( ret [0] | port [0] | if [0] | for [0] | foreach [0] | while [0] | try [0] | throw [0] )  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> stmt_ctrl[0] -> * ( ret [0] | port [0] | if [0] | for [0] | foreach [0] | while [0] | try [0] | throw [0] ) 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> for 0



## 状态[60]： 

项目：while[0] ->  `WHILE`  `LPA`  exp [0]  `RPA`  block [1]  *

规则：while

----

&emsp;&emsp;&emsp;&emsp;到达状态[51]: stmt_ctrl[0] ->  ( ret [0] | port [0] | if [0] | for [0] | foreach [0] | while [0] | try [0] | throw [0] )  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> stmt_ctrl[0] -> * ( ret [0] | port [0] | if [0] | for [0] | foreach [0] | while [0] | try [0] | throw [0] ) 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> while 0



## 状态[61]： 

项目：foreach[0] ->  `FOREACH`  `LPA`  `VARIABLE`  `ID` [0]  `COLON`  exp [1]  `RPA`  block [2]  *

规则：foreach

----

&emsp;&emsp;&emsp;&emsp;到达状态[51]: stmt_ctrl[0] ->  ( ret [0] | port [0] | if [0] | for [0] | foreach [0] | while [0] | try [0] | throw [0] )  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> stmt_ctrl[0] -> * ( ret [0] | port [0] | if [0] | for [0] | foreach [0] | while [0] | try [0] | throw [0] ) 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> foreach 0



## 状态[62]： 

项目：block_stmt[0] ->  block [0]  *

规则：block_stmt

----

&emsp;&emsp;&emsp;&emsp;到达状态[21]: stmt[0] ->  ( stmt_stmt [0] | stmt_ctrl [0] | block_stmt [0] )  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> stmt[0] -> * ( stmt_stmt [0] | stmt_ctrl [0] | block_stmt [0] ) 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> block_stmt 0



## 状态[63]： 

项目：try[0] ->  `TRY`  block [1]  * `CATCH`  [ `LPA`  `ID` [0]  `RPA` ]  block [2] 

规则：try

----

&emsp;&emsp;&emsp;&emsp;到达状态[78]: try[0] ->  `TRY`  block [1]  `CATCH`  * [ `LPA`  `ID` [0]  `RPA` ]  block [2] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 53(53: `CATCH`，关键字，CATCH)

&emsp;&emsp;&emsp;&emsp;指令：通过



## 状态[64]： 

项目：try[0] ->  `TRY`  block [1]  `CATCH`  [ `LPA`  `ID` [0]  `RPA` ]  block [2]  *

规则：try

----

&emsp;&emsp;&emsp;&emsp;到达状态[51]: stmt_ctrl[0] ->  ( ret [0] | port [0] | if [0] | for [0] | foreach [0] | while [0] | try [0] | throw [0] )  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> stmt_ctrl[0] -> * ( ret [0] | port [0] | if [0] | for [0] | foreach [0] | while [0] | try [0] | throw [0] ) 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> try 0



## 状态[65]： 

项目：var[0] ->  ( `VARIABLE` [11] | `LET` [12] )  `ID` [0]  [ `ASSIGN`  * ( func [1] | exp [2] ) ] 

规则：var

----

&emsp;&emsp;&emsp;&emsp;到达状态[68]: func[0] -> * ( `FUNCTION` [10] | `YIELD` )  [ `LSQ`  doc_list [0]  `RSQ` ]  ( `ID` [1] | `NOT` [1] )  `LPA`  [ var_list [2] ]  `RPA`  ( `PTR_OP`  scope [3] | block [4] ) 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[42: `FUNCTION`，关键字，FUNCTION]&emsp;[47: `YIELD`，关键字，YIELD]&emsp;

----

&emsp;&emsp;&emsp;&emsp;到达状态[20]: exp[0] -> * exp01 [0]  [ ( `INTEGER` [10] | `DECIMAL` [10] ) ] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[57: `INC_OP`，操作符，PLUS_PLUS]&emsp;[4: `INTEGER`，整数，(null)]&emsp;[58: `DEC_OP`，操作符，MINUS_MINUS]&emsp;[1: `BOOLEAN`，布尔，(null)]&emsp;[44: `CALL`，关键字，CALL]&emsp;[3: `CHARACTER`，字符，(null)]&emsp;[100: `LBR`，操作符，LBRACE]&emsp;[86: `NOT`，操作符，BIT_NOT]&emsp;[87: `NOT_OP`，操作符，LOGICAL_NOT]&emsp;[50: `SET`，关键字，SET]&emsp;[5: `DECIMAL`，实数，(null)]&emsp;[98: `LSQ`，操作符，LSQUARE]&emsp;[96: `LPA`，操作符，LPARAN]&emsp;[51: `INVOKE`，关键字，INVOKE]&emsp;[2: `LITERAL`，字符串，(null)]&emsp;[0: `ID`，标识符，(null)]&emsp;[49: `LAMBDA`，关键字，LAMBDA]&emsp;



## 状态[66]： 

项目：for[0] ->  `FOR`  `LPA`  [ var [0] ]  * `SEMI`  [ exp [1] ]  `SEMI`  [ ( exp [2] | var [2] ) ]  `RPA`  block [3] 

规则：for

----

&emsp;&emsp;&emsp;&emsp;到达状态[53]: for[0] ->  `FOR`  `LPA`  [ var [0] ]  `SEMI`  * [ exp [1] ]  `SEMI`  [ ( exp [2] | var [2] ) ]  `RPA`  block [3] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 92(92: `SEMI`，操作符，SEMI)

&emsp;&emsp;&emsp;&emsp;指令：通过



## 状态[67]： 

项目：for[0] ->  `FOR`  `LPA`  [ var [0] ]  `SEMI`  [ exp [1] ]  `SEMI`  [ ( exp [2] | var [2] )  *]  `RPA`  block [3] 

规则：for

----

&emsp;&emsp;&emsp;&emsp;到达状态[79]: for[0] ->  `FOR`  `LPA`  [ var [0] ]  `SEMI`  [ exp [1] ]  `SEMI`  [ ( exp [2] | var [2] ) ]  `RPA`  * block [3] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 97(97: `RPA`，操作符，RPARAN)

&emsp;&emsp;&emsp;&emsp;指令：通过



## 状态[68]： 

项目：func[0] -> * ( `FUNCTION` [10] | `YIELD` )  [ `LSQ`  doc_list [0]  `RSQ` ]  ( `ID` [1] | `NOT` [1] )  `LPA`  [ var_list [2] ]  `RPA`  ( `PTR_OP`  scope [3] | block [4] ) 

规则：func

----

&emsp;&emsp;&emsp;&emsp;到达状态[80]: func[0] ->  ( `FUNCTION` [10] | `YIELD` )  * [ `LSQ`  doc_list [0]  `RSQ` ]  ( `ID` [1] | `NOT` [1] )  `LPA`  [ var_list [2] ]  `RPA`  ( `PTR_OP`  scope [3] | block [4] ) 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 42(42: `FUNCTION`，关键字，FUNCTION)

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 10  [Action]

----

&emsp;&emsp;&emsp;&emsp;到达状态[80]: func[0] ->  ( `FUNCTION` [10] | `YIELD` )  * [ `LSQ`  doc_list [0]  `RSQ` ]  ( `ID` [1] | `NOT` [1] )  `LPA`  [ var_list [2] ]  `RPA`  ( `PTR_OP`  scope [3] | block [4] ) 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 47(47: `YIELD`，关键字，YIELD)

&emsp;&emsp;&emsp;&emsp;指令：通过  [Action]



## 状态[69]： 

项目：call[0] ->  `CALL` [4]  ( `LPA`  ( func [0] | exp [3] )  `RPA` | `ID` [1] )  `LPA`  * [ exp_list [2] ]  `RPA` 

规则：call

----

&emsp;&emsp;&emsp;&emsp;到达状态[81]: exp_list[0] -> * exp [0]  [ `COMMA`  exp_list [1] ] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[57: `INC_OP`，操作符，PLUS_PLUS]&emsp;[4: `INTEGER`，整数，(null)]&emsp;[58: `DEC_OP`，操作符，MINUS_MINUS]&emsp;[1: `BOOLEAN`，布尔，(null)]&emsp;[44: `CALL`，关键字，CALL]&emsp;[3: `CHARACTER`，字符，(null)]&emsp;[100: `LBR`，操作符，LBRACE]&emsp;[86: `NOT`，操作符，BIT_NOT]&emsp;[87: `NOT_OP`，操作符，LOGICAL_NOT]&emsp;[50: `SET`，关键字，SET]&emsp;[5: `DECIMAL`，实数，(null)]&emsp;[98: `LSQ`，操作符，LSQUARE]&emsp;[96: `LPA`，操作符，LPARAN]&emsp;[51: `INVOKE`，关键字，INVOKE]&emsp;[2: `LITERAL`，字符串，(null)]&emsp;[0: `ID`，标识符，(null)]&emsp;[49: `LAMBDA`，关键字，LAMBDA]&emsp;

----

&emsp;&emsp;&emsp;&emsp;到达状态[82]: call[0] ->  `CALL` [4]  ( `LPA`  ( func [0] | exp [3] )  `RPA` | `ID` [1] )  `LPA`  [ exp_list [2] ]  `RPA`  *

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 97(97: `RPA`，操作符，RPARAN)

&emsp;&emsp;&emsp;&emsp;指令：通过



## 状态[70]： 

项目：stmt_stmt[0] ->  [ stmt_exp [0] ]  * `SEMI` 

规则：stmt_stmt

----

&emsp;&emsp;&emsp;&emsp;到达状态[7]: stmt_stmt[0] ->  [ stmt_exp [0] ]  `SEMI`  *

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 92(92: `SEMI`，操作符，SEMI)

&emsp;&emsp;&emsp;&emsp;指令：通过  [Action]



## 状态[71]： 

项目：exp1[0] -> * [ exp1 [1]  ( `AND_OP` [2] | `OR_OP` [2] ) ]  exp2 [0] 

规则：exp1

----

&emsp;&emsp;&emsp;&emsp;到达状态[83]: exp2[0] -> * [ exp2 [1]  ( `OR` [2] | `XOR` [2] | `AND` [2] ) ]  exp3 [0] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[57: `INC_OP`，操作符，PLUS_PLUS]&emsp;[4: `INTEGER`，整数，(null)]&emsp;[58: `DEC_OP`，操作符，MINUS_MINUS]&emsp;[1: `BOOLEAN`，布尔，(null)]&emsp;[44: `CALL`，关键字，CALL]&emsp;[3: `CHARACTER`，字符，(null)]&emsp;[100: `LBR`，操作符，LBRACE]&emsp;[86: `NOT`，操作符，BIT_NOT]&emsp;[87: `NOT_OP`，操作符，LOGICAL_NOT]&emsp;[50: `SET`，关键字，SET]&emsp;[5: `DECIMAL`，实数，(null)]&emsp;[98: `LSQ`，操作符，LSQUARE]&emsp;[96: `LPA`，操作符，LPARAN]&emsp;[51: `INVOKE`，关键字，INVOKE]&emsp;[2: `LITERAL`，字符串，(null)]&emsp;[0: `ID`，标识符，(null)]&emsp;[49: `LAMBDA`，关键字，LAMBDA]&emsp;



## 状态[72]： 

项目：for[0] ->  `FOR`  `LPA`  [ var [0] ]  `SEMI`  [ exp [1] ]  `SEMI`  * [ ( exp [2] | var [2] ) ]  `RPA`  block [3] 

规则：for

----

&emsp;&emsp;&emsp;&emsp;到达状态[20]: exp[0] -> * exp01 [0]  [ ( `INTEGER` [10] | `DECIMAL` [10] ) ] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[57: `INC_OP`，操作符，PLUS_PLUS]&emsp;[4: `INTEGER`，整数，(null)]&emsp;[58: `DEC_OP`，操作符，MINUS_MINUS]&emsp;[1: `BOOLEAN`，布尔，(null)]&emsp;[44: `CALL`，关键字，CALL]&emsp;[3: `CHARACTER`，字符，(null)]&emsp;[100: `LBR`，操作符，LBRACE]&emsp;[86: `NOT`，操作符，BIT_NOT]&emsp;[87: `NOT_OP`，操作符，LOGICAL_NOT]&emsp;[50: `SET`，关键字，SET]&emsp;[5: `DECIMAL`，实数，(null)]&emsp;[98: `LSQ`，操作符，LSQUARE]&emsp;[96: `LPA`，操作符，LPARAN]&emsp;[51: `INVOKE`，关键字，INVOKE]&emsp;[2: `LITERAL`，字符串，(null)]&emsp;[0: `ID`，标识符，(null)]&emsp;[49: `LAMBDA`，关键字，LAMBDA]&emsp;

----

&emsp;&emsp;&emsp;&emsp;到达状态[17]: var[0] -> * ( `VARIABLE` [11] | `LET` [12] )  `ID` [0]  [ `ASSIGN`  ( func [1] | exp [2] ) ] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[41: `VARIABLE`，关键字，VARIABLE]&emsp;[43: `LET`，关键字，LET]&emsp;

----

&emsp;&emsp;&emsp;&emsp;到达状态[79]: for[0] ->  `FOR`  `LPA`  [ var [0] ]  `SEMI`  [ exp [1] ]  `SEMI`  [ ( exp [2] | var [2] ) ]  `RPA`  * block [3] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 97(97: `RPA`，操作符，RPARAN)

&emsp;&emsp;&emsp;&emsp;指令：通过



## 状态[73]： 

项目：foreach[0] ->  `FOREACH`  `LPA`  `VARIABLE`  `ID` [0]  * `COLON`  exp [1]  `RPA`  block [2] 

规则：foreach

----

&emsp;&emsp;&emsp;&emsp;到达状态[84]: foreach[0] ->  `FOREACH`  `LPA`  `VARIABLE`  `ID` [0]  `COLON`  * exp [1]  `RPA`  block [2] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 95(95: `COLON`，操作符，COLON)

&emsp;&emsp;&emsp;&emsp;指令：通过



## 状态[74]： 

项目：var[0] ->  ( `VARIABLE` [11] | `LET` [12] )  `ID` [0]  [ `ASSIGN`  ( func [1] | exp [2] )  *] 

规则：var

----

&emsp;&emsp;&emsp;&emsp;到达状态[46]: stmt_exp[0] ->  ( var [0] | call [0] | cycle_ctrl [0] | exp [0] )  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> stmt_exp[0] -> * ( var [0] | call [0] | cycle_ctrl [0] | exp [0] ) 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> var 0

----

&emsp;&emsp;&emsp;&emsp;到达状态[66]: for[0] ->  `FOR`  `LPA`  [ var [0] ]  * `SEMI`  [ exp [1] ]  `SEMI`  [ ( exp [2] | var [2] ) ]  `RPA`  block [3] 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> for[0] ->  `FOR`  `LPA`  * [ var [0] ]  `SEMI`  [ exp [1] ]  `SEMI`  [ ( exp [2] | var [2] ) ]  `RPA`  block [3] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> var 0

----

&emsp;&emsp;&emsp;&emsp;到达状态[67]: for[0] ->  `FOR`  `LPA`  [ var [0] ]  `SEMI`  [ exp [1] ]  `SEMI`  [ ( exp [2] | var [2] )  *]  `RPA`  block [3] 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> for[0] ->  `FOR`  `LPA`  [ var [0] ]  `SEMI`  [ exp [1] ]  `SEMI`  * [ ( exp [2] | var [2] ) ]  `RPA`  block [3] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> var 2



## 状态[75]： 

项目：call[0] ->  `CALL` [4]  ( `LPA`  ( func [0] | exp [3] )  * `RPA` | `ID` [1] )  `LPA`  [ exp_list [2] ]  `RPA` 

规则：call

----

&emsp;&emsp;&emsp;&emsp;到达状态[45]: call[0] ->  `CALL` [4]  ( `LPA`  ( func [0] | exp [3] )  `RPA` | `ID` [1] )  * `LPA`  [ exp_list [2] ]  `RPA` 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 97(97: `RPA`，操作符，RPARAN)

&emsp;&emsp;&emsp;&emsp;指令：通过



## 状态[76]： 

项目：type[0] ->  ( `ID` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `INTEGER` [0] | `DECIMAL` [0] | `LITERAL` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `CHARACTER` [0] | `BOOLEAN` [0] | `LPA`  exp [1]  `RPA` | call [1] | lambda [2] | set [1] | invoke [1] | array [1] | map [1] )  *

规则：type

----

&emsp;&emsp;&emsp;&emsp;到达状态[85]: exp10[0] ->  [ exp10 [1]  `DOT` [2] ]  type [0]  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> exp10[0] -> * [ exp10 [1]  `DOT` [2] ]  type [0] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> type 0

----

&emsp;&emsp;&emsp;&emsp;到达状态[85]: exp10[0] ->  [ exp10 [1]  `DOT` [2] ]  type [0]  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> exp10[0] ->  [ exp10 [1]  `DOT` [2] ]  * type [0] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> type 0



## 状态[77]： 

项目：if[0] ->  `IF`  `LPA`  exp [0]  `RPA`  block [1]  [ `ELSE`  * ( block [2] | if [3] ) ] 

规则：if

----

&emsp;&emsp;&emsp;&emsp;到达状态[16]: block[0] -> * `LBR`  [ stmt_list [0] ]  `RBR` 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[100: `LBR`，操作符，LBRACE]&emsp;

----

&emsp;&emsp;&emsp;&emsp;到达状态[10]: if[0] -> * `IF`  `LPA`  exp [0]  `RPA`  block [1]  [ `ELSE`  ( block [2] | if [3] ) ] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[23: `IF`，关键字，IF]&emsp;



## 状态[78]： 

项目：try[0] ->  `TRY`  block [1]  `CATCH`  * [ `LPA`  `ID` [0]  `RPA` ]  block [2] 

规则：try

----

&emsp;&emsp;&emsp;&emsp;到达状态[86]: try[0] ->  `TRY`  block [1]  `CATCH`  [ `LPA`  * `ID` [0]  `RPA` ]  block [2] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 96(96: `LPA`，操作符，LPARAN)

&emsp;&emsp;&emsp;&emsp;指令：通过

----

&emsp;&emsp;&emsp;&emsp;到达状态[16]: block[0] -> * `LBR`  [ stmt_list [0] ]  `RBR` 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[100: `LBR`，操作符，LBRACE]&emsp;



## 状态[79]： 

项目：for[0] ->  `FOR`  `LPA`  [ var [0] ]  `SEMI`  [ exp [1] ]  `SEMI`  [ ( exp [2] | var [2] ) ]  `RPA`  * block [3] 

规则：for

----

&emsp;&emsp;&emsp;&emsp;到达状态[16]: block[0] -> * `LBR`  [ stmt_list [0] ]  `RBR` 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[100: `LBR`，操作符，LBRACE]&emsp;



## 状态[80]： 

项目：func[0] ->  ( `FUNCTION` [10] | `YIELD` )  * [ `LSQ`  doc_list [0]  `RSQ` ]  ( `ID` [1] | `NOT` [1] )  `LPA`  [ var_list [2] ]  `RPA`  ( `PTR_OP`  scope [3] | block [4] ) 

规则：func

----

&emsp;&emsp;&emsp;&emsp;到达状态[87]: func[0] ->  ( `FUNCTION` [10] | `YIELD` )  [ `LSQ`  * doc_list [0]  `RSQ` ]  ( `ID` [1] | `NOT` [1] )  `LPA`  [ var_list [2] ]  `RPA`  ( `PTR_OP`  scope [3] | block [4] ) 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 98(98: `LSQ`，操作符，LSQUARE)

&emsp;&emsp;&emsp;&emsp;指令：通过

----

&emsp;&emsp;&emsp;&emsp;到达状态[88]: func[0] ->  ( `FUNCTION` [10] | `YIELD` )  [ `LSQ`  doc_list [0]  `RSQ` ]  ( `ID` [1] | `NOT` [1] )  * `LPA`  [ var_list [2] ]  `RPA`  ( `PTR_OP`  scope [3] | block [4] ) 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 0(0: `ID`，标识符，(null))

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 1  [Action]

----

&emsp;&emsp;&emsp;&emsp;到达状态[88]: func[0] ->  ( `FUNCTION` [10] | `YIELD` )  [ `LSQ`  doc_list [0]  `RSQ` ]  ( `ID` [1] | `NOT` [1] )  * `LPA`  [ var_list [2] ]  `RPA`  ( `PTR_OP`  scope [3] | block [4] ) 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 86(86: `NOT`，操作符，BIT_NOT)

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 1  [Action]



## 状态[81]： 

项目：exp_list[0] -> * exp [0]  [ `COMMA`  exp_list [1] ] 

规则：exp_list

----

&emsp;&emsp;&emsp;&emsp;到达状态[20]: exp[0] -> * exp01 [0]  [ ( `INTEGER` [10] | `DECIMAL` [10] ) ] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[57: `INC_OP`，操作符，PLUS_PLUS]&emsp;[4: `INTEGER`，整数，(null)]&emsp;[58: `DEC_OP`，操作符，MINUS_MINUS]&emsp;[1: `BOOLEAN`，布尔，(null)]&emsp;[44: `CALL`，关键字，CALL]&emsp;[3: `CHARACTER`，字符，(null)]&emsp;[100: `LBR`，操作符，LBRACE]&emsp;[86: `NOT`，操作符，BIT_NOT]&emsp;[87: `NOT_OP`，操作符，LOGICAL_NOT]&emsp;[50: `SET`，关键字，SET]&emsp;[5: `DECIMAL`，实数，(null)]&emsp;[98: `LSQ`，操作符，LSQUARE]&emsp;[96: `LPA`，操作符，LPARAN]&emsp;[51: `INVOKE`，关键字，INVOKE]&emsp;[2: `LITERAL`，字符串，(null)]&emsp;[0: `ID`，标识符，(null)]&emsp;[49: `LAMBDA`，关键字，LAMBDA]&emsp;



## 状态[82]： 

项目：call[0] ->  `CALL` [4]  ( `LPA`  ( func [0] | exp [3] )  `RPA` | `ID` [1] )  `LPA`  [ exp_list [2] ]  `RPA`  *

规则：call

----

&emsp;&emsp;&emsp;&emsp;到达状态[46]: stmt_exp[0] ->  ( var [0] | call [0] | cycle_ctrl [0] | exp [0] )  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> stmt_exp[0] -> * ( var [0] | call [0] | cycle_ctrl [0] | exp [0] ) 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> call 0

----

&emsp;&emsp;&emsp;&emsp;到达状态[76]: type[0] ->  ( `ID` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `INTEGER` [0] | `DECIMAL` [0] | `LITERAL` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `CHARACTER` [0] | `BOOLEAN` [0] | `LPA`  exp [1]  `RPA` | call [1] | lambda [2] | set [1] | invoke [1] | array [1] | map [1] )  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> type[0] -> * ( `ID` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `INTEGER` [0] | `DECIMAL` [0] | `LITERAL` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `CHARACTER` [0] | `BOOLEAN` [0] | `LPA`  exp [1]  `RPA` | call [1] | lambda [2] | set [1] | invoke [1] | array [1] | map [1] ) 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> call 1



## 状态[83]： 

项目：exp2[0] -> * [ exp2 [1]  ( `OR` [2] | `XOR` [2] | `AND` [2] ) ]  exp3 [0] 

规则：exp2

----

&emsp;&emsp;&emsp;&emsp;到达状态[89]: exp3[0] -> * [ exp3 [1]  ( `EQ_OP` [2] | `NE_OP` [2] ) ]  exp4 [0] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[57: `INC_OP`，操作符，PLUS_PLUS]&emsp;[4: `INTEGER`，整数，(null)]&emsp;[58: `DEC_OP`，操作符，MINUS_MINUS]&emsp;[1: `BOOLEAN`，布尔，(null)]&emsp;[44: `CALL`，关键字，CALL]&emsp;[3: `CHARACTER`，字符，(null)]&emsp;[100: `LBR`，操作符，LBRACE]&emsp;[86: `NOT`，操作符，BIT_NOT]&emsp;[87: `NOT_OP`，操作符，LOGICAL_NOT]&emsp;[50: `SET`，关键字，SET]&emsp;[5: `DECIMAL`，实数，(null)]&emsp;[98: `LSQ`，操作符，LSQUARE]&emsp;[96: `LPA`，操作符，LPARAN]&emsp;[51: `INVOKE`，关键字，INVOKE]&emsp;[2: `LITERAL`，字符串，(null)]&emsp;[0: `ID`，标识符，(null)]&emsp;[49: `LAMBDA`，关键字，LAMBDA]&emsp;



## 状态[84]： 

项目：foreach[0] ->  `FOREACH`  `LPA`  `VARIABLE`  `ID` [0]  `COLON`  * exp [1]  `RPA`  block [2] 

规则：foreach

----

&emsp;&emsp;&emsp;&emsp;到达状态[20]: exp[0] -> * exp01 [0]  [ ( `INTEGER` [10] | `DECIMAL` [10] ) ] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[57: `INC_OP`，操作符，PLUS_PLUS]&emsp;[4: `INTEGER`，整数，(null)]&emsp;[58: `DEC_OP`，操作符，MINUS_MINUS]&emsp;[1: `BOOLEAN`，布尔，(null)]&emsp;[44: `CALL`，关键字，CALL]&emsp;[3: `CHARACTER`，字符，(null)]&emsp;[100: `LBR`，操作符，LBRACE]&emsp;[86: `NOT`，操作符，BIT_NOT]&emsp;[87: `NOT_OP`，操作符，LOGICAL_NOT]&emsp;[50: `SET`，关键字，SET]&emsp;[5: `DECIMAL`，实数，(null)]&emsp;[98: `LSQ`，操作符，LSQUARE]&emsp;[96: `LPA`，操作符，LPARAN]&emsp;[51: `INVOKE`，关键字，INVOKE]&emsp;[2: `LITERAL`，字符串，(null)]&emsp;[0: `ID`，标识符，(null)]&emsp;[49: `LAMBDA`，关键字，LAMBDA]&emsp;



## 状态[85]： 

项目：exp10[0] ->  [ exp10 [1]  `DOT` [2] ]  type [0]  *

规则：exp10

----

&emsp;&emsp;&emsp;&emsp;到达状态[90]: exp9[0] ->  ( ( `INC_OP` [3] | `DEC_OP` [3] )  exp9 [1] | exp9 [1]  ( `INC_OP` [3] | `DEC_OP` [3] | `LSQ`  exp [5]  `RSQ` ) | exp10 [0] )  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> exp9[0] -> * ( ( `INC_OP` [3] | `DEC_OP` [3] )  exp9 [1] | exp9 [1]  ( `INC_OP` [3] | `DEC_OP` [3] | `LSQ`  exp [5]  `RSQ` ) | exp10 [0] ) 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp10 0

----

&emsp;&emsp;&emsp;&emsp;到达状态[91]: exp10[0] ->  [ exp10 [1]  * `DOT` [2] ]  type [0] 

&emsp;&emsp;&emsp;&emsp;类型：左递归

&emsp;&emsp;&emsp;&emsp;指令：左递归	=> exp10 1

&emsp;&emsp;&emsp;&emsp;预查：[93: `DOT`，操作符，DOT]&emsp;



## 状态[86]： 

项目：try[0] ->  `TRY`  block [1]  `CATCH`  [ `LPA`  * `ID` [0]  `RPA` ]  block [2] 

规则：try

----

&emsp;&emsp;&emsp;&emsp;到达状态[92]: try[0] ->  `TRY`  block [1]  `CATCH`  [ `LPA`  `ID` [0]  * `RPA` ]  block [2] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 0(0: `ID`，标识符，(null))

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 0  [Action]



## 状态[87]： 

项目：func[0] ->  ( `FUNCTION` [10] | `YIELD` )  [ `LSQ`  * doc_list [0]  `RSQ` ]  ( `ID` [1] | `NOT` [1] )  `LPA`  [ var_list [2] ]  `RPA`  ( `PTR_OP`  scope [3] | block [4] ) 

规则：func

----

&emsp;&emsp;&emsp;&emsp;到达状态[93]: doc_list[0] -> * `LITERAL` [0]  [ `COMMA`  doc_list [1] ] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[2: `LITERAL`，字符串，(null)]&emsp;



## 状态[88]： 

项目：func[0] ->  ( `FUNCTION` [10] | `YIELD` )  [ `LSQ`  doc_list [0]  `RSQ` ]  ( `ID` [1] | `NOT` [1] )  * `LPA`  [ var_list [2] ]  `RPA`  ( `PTR_OP`  scope [3] | block [4] ) 

规则：func

----

&emsp;&emsp;&emsp;&emsp;到达状态[94]: func[0] ->  ( `FUNCTION` [10] | `YIELD` )  [ `LSQ`  doc_list [0]  `RSQ` ]  ( `ID` [1] | `NOT` [1] )  `LPA`  * [ var_list [2] ]  `RPA`  ( `PTR_OP`  scope [3] | block [4] ) 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 96(96: `LPA`，操作符，LPARAN)

&emsp;&emsp;&emsp;&emsp;指令：通过



## 状态[89]： 

项目：exp3[0] -> * [ exp3 [1]  ( `EQ_OP` [2] | `NE_OP` [2] ) ]  exp4 [0] 

规则：exp3

----

&emsp;&emsp;&emsp;&emsp;到达状态[95]: exp4[0] -> * [ exp4 [1]  ( `LT` [2] | `GT` [2] | `LE_OP` [2] | `GE_OP` [2] ) ]  exp5 [0] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[57: `INC_OP`，操作符，PLUS_PLUS]&emsp;[4: `INTEGER`，整数，(null)]&emsp;[58: `DEC_OP`，操作符，MINUS_MINUS]&emsp;[1: `BOOLEAN`，布尔，(null)]&emsp;[44: `CALL`，关键字，CALL]&emsp;[3: `CHARACTER`，字符，(null)]&emsp;[100: `LBR`，操作符，LBRACE]&emsp;[86: `NOT`，操作符，BIT_NOT]&emsp;[87: `NOT_OP`，操作符，LOGICAL_NOT]&emsp;[50: `SET`，关键字，SET]&emsp;[5: `DECIMAL`，实数，(null)]&emsp;[98: `LSQ`，操作符，LSQUARE]&emsp;[96: `LPA`，操作符，LPARAN]&emsp;[51: `INVOKE`，关键字，INVOKE]&emsp;[2: `LITERAL`，字符串，(null)]&emsp;[0: `ID`，标识符，(null)]&emsp;[49: `LAMBDA`，关键字，LAMBDA]&emsp;



## 状态[90]： 

项目：exp9[0] ->  ( ( `INC_OP` [3] | `DEC_OP` [3] )  exp9 [1] | exp9 [1]  ( `INC_OP` [3] | `DEC_OP` [3] | `LSQ`  exp [5]  `RSQ` ) | exp10 [0] )  *

规则：exp9

----

&emsp;&emsp;&emsp;&emsp;到达状态[96]: exp8[0] ->  ( ( `NOT_OP` [3] | `NOT` [3] )  exp8 [1] | exp9 [0] )  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> exp8[0] -> * ( ( `NOT_OP` [3] | `NOT` [3] )  exp8 [1] | exp9 [0] ) 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp9 0

----

&emsp;&emsp;&emsp;&emsp;到达状态[97]: exp9[0] ->  ( ( `INC_OP` [3] | `DEC_OP` [3] )  exp9 [1] | exp9 [1]  * ( `INC_OP` [3] | `DEC_OP` [3] | `LSQ`  exp [5]  `RSQ` ) | exp10 [0] ) 

&emsp;&emsp;&emsp;&emsp;类型：左递归

&emsp;&emsp;&emsp;&emsp;指令：左递归	=> exp9 1

&emsp;&emsp;&emsp;&emsp;预查：[57: `INC_OP`，操作符，PLUS_PLUS]&emsp;[58: `DEC_OP`，操作符，MINUS_MINUS]&emsp;[98: `LSQ`，操作符，LSQUARE]&emsp;

----

&emsp;&emsp;&emsp;&emsp;到达状态[90]: exp9[0] ->  ( ( `INC_OP` [3] | `DEC_OP` [3] )  exp9 [1] | exp9 [1]  ( `INC_OP` [3] | `DEC_OP` [3] | `LSQ`  exp [5]  `RSQ` ) | exp10 [0] )  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> exp9[0] ->  ( ( `INC_OP` [3] | `DEC_OP` [3] )  * exp9 [1] | exp9 [1]  ( `INC_OP` [3] | `DEC_OP` [3] | `LSQ`  exp [5]  `RSQ` ) | exp10 [0] ) 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp9 1



## 状态[91]： 

项目：exp10[0] ->  [ exp10 [1]  * `DOT` [2] ]  type [0] 

规则：exp10

----

&emsp;&emsp;&emsp;&emsp;到达状态[98]: exp10[0] ->  [ exp10 [1]  `DOT` [2] ]  * type [0] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 93(93: `DOT`，操作符，DOT)

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 2



## 状态[92]： 

项目：try[0] ->  `TRY`  block [1]  `CATCH`  [ `LPA`  `ID` [0]  * `RPA` ]  block [2] 

规则：try

----

&emsp;&emsp;&emsp;&emsp;到达状态[99]: try[0] ->  `TRY`  block [1]  `CATCH`  [ `LPA`  `ID` [0]  `RPA` ]  * block [2] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 97(97: `RPA`，操作符，RPARAN)

&emsp;&emsp;&emsp;&emsp;指令：通过



## 状态[93]： 

项目：doc_list[0] -> * `LITERAL` [0]  [ `COMMA`  doc_list [1] ] 

规则：doc_list

----

&emsp;&emsp;&emsp;&emsp;到达状态[100]: doc_list[0] ->  `LITERAL` [0]  * [ `COMMA`  doc_list [1] ] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 2(2: `LITERAL`，字符串，(null))

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 0



## 状态[94]： 

项目：func[0] ->  ( `FUNCTION` [10] | `YIELD` )  [ `LSQ`  doc_list [0]  `RSQ` ]  ( `ID` [1] | `NOT` [1] )  `LPA`  * [ var_list [2] ]  `RPA`  ( `PTR_OP`  scope [3] | block [4] ) 

规则：func

----

&emsp;&emsp;&emsp;&emsp;到达状态[101]: var_list[0] -> * `ID` [0]  [ `COMMA`  var_list [1] ] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[0: `ID`，标识符，(null)]&emsp;

----

&emsp;&emsp;&emsp;&emsp;到达状态[102]: func[0] ->  ( `FUNCTION` [10] | `YIELD` )  [ `LSQ`  doc_list [0]  `RSQ` ]  ( `ID` [1] | `NOT` [1] )  `LPA`  [ var_list [2] ]  `RPA`  * ( `PTR_OP`  scope [3] | block [4] ) 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 97(97: `RPA`，操作符，RPARAN)

&emsp;&emsp;&emsp;&emsp;指令：通过



## 状态[95]： 

项目：exp4[0] -> * [ exp4 [1]  ( `LT` [2] | `GT` [2] | `LE_OP` [2] | `GE_OP` [2] ) ]  exp5 [0] 

规则：exp4

----

&emsp;&emsp;&emsp;&emsp;到达状态[103]: exp5[0] -> * [ exp5 [1]  ( `LEFT_OP` [2] | `RIGHT_OP` [2] ) ]  exp6 [0] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[57: `INC_OP`，操作符，PLUS_PLUS]&emsp;[4: `INTEGER`，整数，(null)]&emsp;[58: `DEC_OP`，操作符，MINUS_MINUS]&emsp;[1: `BOOLEAN`，布尔，(null)]&emsp;[44: `CALL`，关键字，CALL]&emsp;[3: `CHARACTER`，字符，(null)]&emsp;[100: `LBR`，操作符，LBRACE]&emsp;[86: `NOT`，操作符，BIT_NOT]&emsp;[87: `NOT_OP`，操作符，LOGICAL_NOT]&emsp;[50: `SET`，关键字，SET]&emsp;[5: `DECIMAL`，实数，(null)]&emsp;[98: `LSQ`，操作符，LSQUARE]&emsp;[96: `LPA`，操作符，LPARAN]&emsp;[51: `INVOKE`，关键字，INVOKE]&emsp;[2: `LITERAL`，字符串，(null)]&emsp;[0: `ID`，标识符，(null)]&emsp;[49: `LAMBDA`，关键字，LAMBDA]&emsp;



## 状态[96]： 

项目：exp8[0] ->  ( ( `NOT_OP` [3] | `NOT` [3] )  exp8 [1] | exp9 [0] )  *

规则：exp8

----

&emsp;&emsp;&emsp;&emsp;到达状态[104]: exp7[0] ->  [ exp7 [1]  ( `MUL` [2] | `DIV` [2] | `MOD` [2] ) ]  exp8 [0]  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> exp7[0] -> * [ exp7 [1]  ( `MUL` [2] | `DIV` [2] | `MOD` [2] ) ]  exp8 [0] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp8 0

----

&emsp;&emsp;&emsp;&emsp;到达状态[104]: exp7[0] ->  [ exp7 [1]  ( `MUL` [2] | `DIV` [2] | `MOD` [2] ) ]  exp8 [0]  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> exp7[0] ->  [ exp7 [1]  ( `MUL` [2] | `DIV` [2] | `MOD` [2] )  *]  exp8 [0] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp8 0

----

&emsp;&emsp;&emsp;&emsp;到达状态[96]: exp8[0] ->  ( ( `NOT_OP` [3] | `NOT` [3] )  exp8 [1] | exp9 [0] )  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> exp8[0] ->  ( ( `NOT_OP` [3] | `NOT` [3] )  * exp8 [1] | exp9 [0] ) 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp8 1



## 状态[97]： 

项目：exp9[0] ->  ( ( `INC_OP` [3] | `DEC_OP` [3] )  exp9 [1] | exp9 [1]  * ( `INC_OP` [3] | `DEC_OP` [3] | `LSQ`  exp [5]  `RSQ` ) | exp10 [0] ) 

规则：exp9

----

&emsp;&emsp;&emsp;&emsp;到达状态[105]: exp9[0] ->  ( ( `INC_OP` [3] | `DEC_OP` [3] )  exp9 [1] | exp9 [1]  ( `INC_OP` [3] | `DEC_OP` [3] | `LSQ`  exp [5]  `RSQ` )  *| exp10 [0] ) 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 57(57: `INC_OP`，操作符，PLUS_PLUS)

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 3

----

&emsp;&emsp;&emsp;&emsp;到达状态[105]: exp9[0] ->  ( ( `INC_OP` [3] | `DEC_OP` [3] )  exp9 [1] | exp9 [1]  ( `INC_OP` [3] | `DEC_OP` [3] | `LSQ`  exp [5]  `RSQ` )  *| exp10 [0] ) 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 58(58: `DEC_OP`，操作符，MINUS_MINUS)

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 3

----

&emsp;&emsp;&emsp;&emsp;到达状态[106]: exp9[0] ->  ( ( `INC_OP` [3] | `DEC_OP` [3] )  exp9 [1] | exp9 [1]  ( `INC_OP` [3] | `DEC_OP` [3] | `LSQ`  * exp [5]  `RSQ` ) | exp10 [0] ) 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 98(98: `LSQ`，操作符，LSQUARE)

&emsp;&emsp;&emsp;&emsp;指令：通过



## 状态[98]： 

项目：exp10[0] ->  [ exp10 [1]  `DOT` [2] ]  * type [0] 

规则：exp10

----

&emsp;&emsp;&emsp;&emsp;到达状态[107]: type[0] -> * ( `ID` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `INTEGER` [0] | `DECIMAL` [0] | `LITERAL` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `CHARACTER` [0] | `BOOLEAN` [0] | `LPA`  exp [1]  `RPA` | call [1] | lambda [2] | set [1] | invoke [1] | array [1] | map [1] ) 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[4: `INTEGER`，整数，(null)]&emsp;[1: `BOOLEAN`，布尔，(null)]&emsp;[44: `CALL`，关键字，CALL]&emsp;[3: `CHARACTER`，字符，(null)]&emsp;[100: `LBR`，操作符，LBRACE]&emsp;[50: `SET`，关键字，SET]&emsp;[5: `DECIMAL`，实数，(null)]&emsp;[98: `LSQ`，操作符，LSQUARE]&emsp;[96: `LPA`，操作符，LPARAN]&emsp;[51: `INVOKE`，关键字，INVOKE]&emsp;[2: `LITERAL`，字符串，(null)]&emsp;[0: `ID`，标识符，(null)]&emsp;[49: `LAMBDA`，关键字，LAMBDA]&emsp;



## 状态[99]： 

项目：try[0] ->  `TRY`  block [1]  `CATCH`  [ `LPA`  `ID` [0]  `RPA` ]  * block [2] 

规则：try

----

&emsp;&emsp;&emsp;&emsp;到达状态[16]: block[0] -> * `LBR`  [ stmt_list [0] ]  `RBR` 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[100: `LBR`，操作符，LBRACE]&emsp;



## 状态[100]： 

项目：doc_list[0] ->  `LITERAL` [0]  * [ `COMMA`  doc_list [1] ] 

规则：doc_list

----

&emsp;&emsp;&emsp;&emsp;到达状态[108]: doc_list[0] ->  `LITERAL` [0]  [ `COMMA`  * doc_list [1] ] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 91(91: `COMMA`，操作符，COMMA)

&emsp;&emsp;&emsp;&emsp;指令：通过

----

&emsp;&emsp;&emsp;&emsp;到达状态[109]: func[0] ->  ( `FUNCTION` [10] | `YIELD` )  [ `LSQ`  doc_list [0]  * `RSQ` ]  ( `ID` [1] | `NOT` [1] )  `LPA`  [ var_list [2] ]  `RPA`  ( `PTR_OP`  scope [3] | block [4] ) 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> func[0] ->  ( `FUNCTION` [10] | `YIELD` )  [ `LSQ`  * doc_list [0]  `RSQ` ]  ( `ID` [1] | `NOT` [1] )  `LPA`  [ var_list [2] ]  `RPA`  ( `PTR_OP`  scope [3] | block [4] ) 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> doc_list 0

----

&emsp;&emsp;&emsp;&emsp;到达状态[110]: doc_list[0] ->  `LITERAL` [0]  [ `COMMA`  doc_list [1] ]  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> doc_list[0] ->  `LITERAL` [0]  [ `COMMA`  * doc_list [1] ] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> doc_list 1



## 状态[101]： 

项目：var_list[0] -> * `ID` [0]  [ `COMMA`  var_list [1] ] 

规则：var_list

----

&emsp;&emsp;&emsp;&emsp;到达状态[111]: var_list[0] ->  `ID` [0]  * [ `COMMA`  var_list [1] ] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 0(0: `ID`，标识符，(null))

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 0  [Action]



## 状态[102]： 

项目：func[0] ->  ( `FUNCTION` [10] | `YIELD` )  [ `LSQ`  doc_list [0]  `RSQ` ]  ( `ID` [1] | `NOT` [1] )  `LPA`  [ var_list [2] ]  `RPA`  * ( `PTR_OP`  scope [3] | block [4] ) 

规则：func

----

&emsp;&emsp;&emsp;&emsp;到达状态[112]: func[0] ->  ( `FUNCTION` [10] | `YIELD` )  [ `LSQ`  doc_list [0]  `RSQ` ]  ( `ID` [1] | `NOT` [1] )  `LPA`  [ var_list [2] ]  `RPA`  ( `PTR_OP`  * scope [3] | block [4] ) 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 56(56: `PTR_OP`，操作符，POINTER)

&emsp;&emsp;&emsp;&emsp;指令：通过  [Action]

----

&emsp;&emsp;&emsp;&emsp;到达状态[16]: block[0] -> * `LBR`  [ stmt_list [0] ]  `RBR` 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[100: `LBR`，操作符，LBRACE]&emsp;



## 状态[103]： 

项目：exp5[0] -> * [ exp5 [1]  ( `LEFT_OP` [2] | `RIGHT_OP` [2] ) ]  exp6 [0] 

规则：exp5

----

&emsp;&emsp;&emsp;&emsp;到达状态[113]: exp6[0] -> * [ exp6 [1]  ( `ADD` [2] | `SUB` [2] ) ]  exp7 [0] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[57: `INC_OP`，操作符，PLUS_PLUS]&emsp;[4: `INTEGER`，整数，(null)]&emsp;[58: `DEC_OP`，操作符，MINUS_MINUS]&emsp;[1: `BOOLEAN`，布尔，(null)]&emsp;[44: `CALL`，关键字，CALL]&emsp;[3: `CHARACTER`，字符，(null)]&emsp;[100: `LBR`，操作符，LBRACE]&emsp;[86: `NOT`，操作符，BIT_NOT]&emsp;[87: `NOT_OP`，操作符，LOGICAL_NOT]&emsp;[50: `SET`，关键字，SET]&emsp;[5: `DECIMAL`，实数，(null)]&emsp;[98: `LSQ`，操作符，LSQUARE]&emsp;[96: `LPA`，操作符，LPARAN]&emsp;[51: `INVOKE`，关键字，INVOKE]&emsp;[2: `LITERAL`，字符串，(null)]&emsp;[0: `ID`，标识符，(null)]&emsp;[49: `LAMBDA`，关键字，LAMBDA]&emsp;



## 状态[104]： 

项目：exp7[0] ->  [ exp7 [1]  ( `MUL` [2] | `DIV` [2] | `MOD` [2] ) ]  exp8 [0]  *

规则：exp7

----

&emsp;&emsp;&emsp;&emsp;到达状态[114]: exp6[0] ->  [ exp6 [1]  ( `ADD` [2] | `SUB` [2] ) ]  exp7 [0]  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> exp6[0] -> * [ exp6 [1]  ( `ADD` [2] | `SUB` [2] ) ]  exp7 [0] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp7 0

----

&emsp;&emsp;&emsp;&emsp;到达状态[114]: exp6[0] ->  [ exp6 [1]  ( `ADD` [2] | `SUB` [2] ) ]  exp7 [0]  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> exp6[0] ->  [ exp6 [1]  ( `ADD` [2] | `SUB` [2] )  *]  exp7 [0] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp7 0

----

&emsp;&emsp;&emsp;&emsp;到达状态[115]: exp7[0] ->  [ exp7 [1]  * ( `MUL` [2] | `DIV` [2] | `MOD` [2] ) ]  exp8 [0] 

&emsp;&emsp;&emsp;&emsp;类型：左递归

&emsp;&emsp;&emsp;&emsp;指令：左递归	=> exp7 1

&emsp;&emsp;&emsp;&emsp;预查：[80: `MUL`，操作符，TIMES]&emsp;[81: `DIV`，操作符，DIVIDE]&emsp;[82: `MOD`，操作符，MOD]&emsp;



## 状态[105]： 

项目：exp9[0] ->  ( ( `INC_OP` [3] | `DEC_OP` [3] )  exp9 [1] | exp9 [1]  ( `INC_OP` [3] | `DEC_OP` [3] | `LSQ`  exp [5]  `RSQ` )  *| exp10 [0] ) 

规则：exp9

----

&emsp;&emsp;&emsp;&emsp;到达状态[96]: exp8[0] ->  ( ( `NOT_OP` [3] | `NOT` [3] )  exp8 [1] | exp9 [0] )  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> exp8[0] -> * ( ( `NOT_OP` [3] | `NOT` [3] )  exp8 [1] | exp9 [0] ) 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp9 0

----

&emsp;&emsp;&emsp;&emsp;到达状态[97]: exp9[0] ->  ( ( `INC_OP` [3] | `DEC_OP` [3] )  exp9 [1] | exp9 [1]  * ( `INC_OP` [3] | `DEC_OP` [3] | `LSQ`  exp [5]  `RSQ` ) | exp10 [0] ) 

&emsp;&emsp;&emsp;&emsp;类型：左递归

&emsp;&emsp;&emsp;&emsp;指令：左递归	=> exp9 1

&emsp;&emsp;&emsp;&emsp;预查：[57: `INC_OP`，操作符，PLUS_PLUS]&emsp;[58: `DEC_OP`，操作符，MINUS_MINUS]&emsp;[98: `LSQ`，操作符，LSQUARE]&emsp;

----

&emsp;&emsp;&emsp;&emsp;到达状态[90]: exp9[0] ->  ( ( `INC_OP` [3] | `DEC_OP` [3] )  exp9 [1] | exp9 [1]  ( `INC_OP` [3] | `DEC_OP` [3] | `LSQ`  exp [5]  `RSQ` ) | exp10 [0] )  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> exp9[0] ->  ( ( `INC_OP` [3] | `DEC_OP` [3] )  * exp9 [1] | exp9 [1]  ( `INC_OP` [3] | `DEC_OP` [3] | `LSQ`  exp [5]  `RSQ` ) | exp10 [0] ) 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp9 1



## 状态[106]： 

项目：exp9[0] ->  ( ( `INC_OP` [3] | `DEC_OP` [3] )  exp9 [1] | exp9 [1]  ( `INC_OP` [3] | `DEC_OP` [3] | `LSQ`  * exp [5]  `RSQ` ) | exp10 [0] ) 

规则：exp9

----

&emsp;&emsp;&emsp;&emsp;到达状态[20]: exp[0] -> * exp01 [0]  [ ( `INTEGER` [10] | `DECIMAL` [10] ) ] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[57: `INC_OP`，操作符，PLUS_PLUS]&emsp;[4: `INTEGER`，整数，(null)]&emsp;[58: `DEC_OP`，操作符，MINUS_MINUS]&emsp;[1: `BOOLEAN`，布尔，(null)]&emsp;[44: `CALL`，关键字，CALL]&emsp;[3: `CHARACTER`，字符，(null)]&emsp;[100: `LBR`，操作符，LBRACE]&emsp;[86: `NOT`，操作符，BIT_NOT]&emsp;[87: `NOT_OP`，操作符，LOGICAL_NOT]&emsp;[50: `SET`，关键字，SET]&emsp;[5: `DECIMAL`，实数，(null)]&emsp;[98: `LSQ`，操作符，LSQUARE]&emsp;[96: `LPA`，操作符，LPARAN]&emsp;[51: `INVOKE`，关键字，INVOKE]&emsp;[2: `LITERAL`，字符串，(null)]&emsp;[0: `ID`，标识符，(null)]&emsp;[49: `LAMBDA`，关键字，LAMBDA]&emsp;



## 状态[107]： 

项目：type[0] -> * ( `ID` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `INTEGER` [0] | `DECIMAL` [0] | `LITERAL` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `CHARACTER` [0] | `BOOLEAN` [0] | `LPA`  exp [1]  `RPA` | call [1] | lambda [2] | set [1] | invoke [1] | array [1] | map [1] ) 

规则：type

----

&emsp;&emsp;&emsp;&emsp;到达状态[116]: type[0] ->  ( `ID` [0]  * [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `INTEGER` [0] | `DECIMAL` [0] | `LITERAL` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `CHARACTER` [0] | `BOOLEAN` [0] | `LPA`  exp [1]  `RPA` | call [1] | lambda [2] | set [1] | invoke [1] | array [1] | map [1] ) 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 0(0: `ID`，标识符，(null))

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 0

----

&emsp;&emsp;&emsp;&emsp;到达状态[76]: type[0] ->  ( `ID` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `INTEGER` [0] | `DECIMAL` [0] | `LITERAL` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `CHARACTER` [0] | `BOOLEAN` [0] | `LPA`  exp [1]  `RPA` | call [1] | lambda [2] | set [1] | invoke [1] | array [1] | map [1] )  *

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 4(4: `INTEGER`，整数，(null))

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 0

----

&emsp;&emsp;&emsp;&emsp;到达状态[76]: type[0] ->  ( `ID` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `INTEGER` [0] | `DECIMAL` [0] | `LITERAL` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `CHARACTER` [0] | `BOOLEAN` [0] | `LPA`  exp [1]  `RPA` | call [1] | lambda [2] | set [1] | invoke [1] | array [1] | map [1] )  *

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 5(5: `DECIMAL`，实数，(null))

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 0

----

&emsp;&emsp;&emsp;&emsp;到达状态[117]: type[0] ->  ( `ID` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `INTEGER` [0] | `DECIMAL` [0] | `LITERAL` [0]  * [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `CHARACTER` [0] | `BOOLEAN` [0] | `LPA`  exp [1]  `RPA` | call [1] | lambda [2] | set [1] | invoke [1] | array [1] | map [1] ) 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 2(2: `LITERAL`，字符串，(null))

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 0

----

&emsp;&emsp;&emsp;&emsp;到达状态[76]: type[0] ->  ( `ID` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `INTEGER` [0] | `DECIMAL` [0] | `LITERAL` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `CHARACTER` [0] | `BOOLEAN` [0] | `LPA`  exp [1]  `RPA` | call [1] | lambda [2] | set [1] | invoke [1] | array [1] | map [1] )  *

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 3(3: `CHARACTER`，字符，(null))

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 0

----

&emsp;&emsp;&emsp;&emsp;到达状态[76]: type[0] ->  ( `ID` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `INTEGER` [0] | `DECIMAL` [0] | `LITERAL` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `CHARACTER` [0] | `BOOLEAN` [0] | `LPA`  exp [1]  `RPA` | call [1] | lambda [2] | set [1] | invoke [1] | array [1] | map [1] )  *

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 1(1: `BOOLEAN`，布尔，(null))

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 0

----

&emsp;&emsp;&emsp;&emsp;到达状态[118]: type[0] ->  ( `ID` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `INTEGER` [0] | `DECIMAL` [0] | `LITERAL` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `CHARACTER` [0] | `BOOLEAN` [0] | `LPA`  * exp [1]  `RPA` | call [1] | lambda [2] | set [1] | invoke [1] | array [1] | map [1] ) 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 96(96: `LPA`，操作符，LPARAN)

&emsp;&emsp;&emsp;&emsp;指令：通过

----

&emsp;&emsp;&emsp;&emsp;到达状态[18]: call[0] -> * `CALL` [4]  ( `LPA`  ( func [0] | exp [3] )  `RPA` | `ID` [1] )  `LPA`  [ exp_list [2] ]  `RPA` 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[44: `CALL`，关键字，CALL]&emsp;

----

&emsp;&emsp;&emsp;&emsp;到达状态[119]: lambda[0] -> * `LAMBDA` [1]  `LPA`  [ var_list [2] ]  `RPA`  ( `PTR_OP`  scope [3] | block [4] ) 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[49: `LAMBDA`，关键字，LAMBDA]&emsp;

----

&emsp;&emsp;&emsp;&emsp;到达状态[120]: set[0] -> * `SET`  exp [3]  `PROPERTY`  exp [4]  `ASSIGN`  exp [2] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[50: `SET`，关键字，SET]&emsp;

----

&emsp;&emsp;&emsp;&emsp;到达状态[121]: invoke[0] -> * `INVOKE` [0]  exp [1]  `PROPERTY`  exp [2]  `PROPERTY`  `LPA`  [ exp_list [3] ]  `RPA` 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[51: `INVOKE`，关键字，INVOKE]&emsp;

----

&emsp;&emsp;&emsp;&emsp;到达状态[122]: array[0] -> * `LSQ`  [ exp_list [0] ]  `RSQ` 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[98: `LSQ`，操作符，LSQUARE]&emsp;

----

&emsp;&emsp;&emsp;&emsp;到达状态[123]: map[0] -> * `LBR`  [ map_list [0] ]  `RBR` 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[100: `LBR`，操作符，LBRACE]&emsp;



## 状态[108]： 

项目：doc_list[0] ->  `LITERAL` [0]  [ `COMMA`  * doc_list [1] ] 

规则：doc_list

----

&emsp;&emsp;&emsp;&emsp;到达状态[93]: doc_list[0] -> * `LITERAL` [0]  [ `COMMA`  doc_list [1] ] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[2: `LITERAL`，字符串，(null)]&emsp;



## 状态[109]： 

项目：func[0] ->  ( `FUNCTION` [10] | `YIELD` )  [ `LSQ`  doc_list [0]  * `RSQ` ]  ( `ID` [1] | `NOT` [1] )  `LPA`  [ var_list [2] ]  `RPA`  ( `PTR_OP`  scope [3] | block [4] ) 

规则：func

----

&emsp;&emsp;&emsp;&emsp;到达状态[124]: func[0] ->  ( `FUNCTION` [10] | `YIELD` )  [ `LSQ`  doc_list [0]  `RSQ` ]  * ( `ID` [1] | `NOT` [1] )  `LPA`  [ var_list [2] ]  `RPA`  ( `PTR_OP`  scope [3] | block [4] ) 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 99(99: `RSQ`，操作符，RSQUARE)

&emsp;&emsp;&emsp;&emsp;指令：通过



## 状态[110]： 

项目：doc_list[0] ->  `LITERAL` [0]  [ `COMMA`  doc_list [1] ]  *

规则：doc_list

----

&emsp;&emsp;&emsp;&emsp;到达状态[109]: func[0] ->  ( `FUNCTION` [10] | `YIELD` )  [ `LSQ`  doc_list [0]  * `RSQ` ]  ( `ID` [1] | `NOT` [1] )  `LPA`  [ var_list [2] ]  `RPA`  ( `PTR_OP`  scope [3] | block [4] ) 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> func[0] ->  ( `FUNCTION` [10] | `YIELD` )  [ `LSQ`  * doc_list [0]  `RSQ` ]  ( `ID` [1] | `NOT` [1] )  `LPA`  [ var_list [2] ]  `RPA`  ( `PTR_OP`  scope [3] | block [4] ) 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> doc_list 0

----

&emsp;&emsp;&emsp;&emsp;到达状态[110]: doc_list[0] ->  `LITERAL` [0]  [ `COMMA`  doc_list [1] ]  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> doc_list[0] ->  `LITERAL` [0]  [ `COMMA`  * doc_list [1] ] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> doc_list 1



## 状态[111]： 

项目：var_list[0] ->  `ID` [0]  * [ `COMMA`  var_list [1] ] 

规则：var_list

----

&emsp;&emsp;&emsp;&emsp;到达状态[125]: var_list[0] ->  `ID` [0]  [ `COMMA`  * var_list [1] ] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 91(91: `COMMA`，操作符，COMMA)

&emsp;&emsp;&emsp;&emsp;指令：通过

----

&emsp;&emsp;&emsp;&emsp;到达状态[126]: func[0] ->  ( `FUNCTION` [10] | `YIELD` )  [ `LSQ`  doc_list [0]  `RSQ` ]  ( `ID` [1] | `NOT` [1] )  `LPA`  [ var_list [2] ]  * `RPA`  ( `PTR_OP`  scope [3] | block [4] ) 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> func[0] ->  ( `FUNCTION` [10] | `YIELD` )  [ `LSQ`  doc_list [0]  `RSQ` ]  ( `ID` [1] | `NOT` [1] )  `LPA`  * [ var_list [2] ]  `RPA`  ( `PTR_OP`  scope [3] | block [4] ) 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> var_list 2

----

&emsp;&emsp;&emsp;&emsp;到达状态[127]: lambda[0] ->  `LAMBDA` [1]  `LPA`  [ var_list [2] ]  * `RPA`  ( `PTR_OP`  scope [3] | block [4] ) 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> lambda[0] ->  `LAMBDA` [1]  `LPA`  * [ var_list [2] ]  `RPA`  ( `PTR_OP`  scope [3] | block [4] ) 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> var_list 2

----

&emsp;&emsp;&emsp;&emsp;到达状态[128]: var_list[0] ->  `ID` [0]  [ `COMMA`  var_list [1] ]  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> var_list[0] ->  `ID` [0]  [ `COMMA`  * var_list [1] ] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> var_list 1



## 状态[112]： 

项目：func[0] ->  ( `FUNCTION` [10] | `YIELD` )  [ `LSQ`  doc_list [0]  `RSQ` ]  ( `ID` [1] | `NOT` [1] )  `LPA`  [ var_list [2] ]  `RPA`  ( `PTR_OP`  * scope [3] | block [4] ) 

规则：func

----

&emsp;&emsp;&emsp;&emsp;到达状态[129]: scope[0] -> * exp [0] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进  [Action]

&emsp;&emsp;&emsp;&emsp;预查：[57: `INC_OP`，操作符，PLUS_PLUS]&emsp;[4: `INTEGER`，整数，(null)]&emsp;[58: `DEC_OP`，操作符，MINUS_MINUS]&emsp;[1: `BOOLEAN`，布尔，(null)]&emsp;[44: `CALL`，关键字，CALL]&emsp;[3: `CHARACTER`，字符，(null)]&emsp;[100: `LBR`，操作符，LBRACE]&emsp;[86: `NOT`，操作符，BIT_NOT]&emsp;[87: `NOT_OP`，操作符，LOGICAL_NOT]&emsp;[50: `SET`，关键字，SET]&emsp;[5: `DECIMAL`，实数，(null)]&emsp;[98: `LSQ`，操作符，LSQUARE]&emsp;[96: `LPA`，操作符，LPARAN]&emsp;[51: `INVOKE`，关键字，INVOKE]&emsp;[2: `LITERAL`，字符串，(null)]&emsp;[0: `ID`，标识符，(null)]&emsp;[49: `LAMBDA`，关键字，LAMBDA]&emsp;



## 状态[113]： 

项目：exp6[0] -> * [ exp6 [1]  ( `ADD` [2] | `SUB` [2] ) ]  exp7 [0] 

规则：exp6

----

&emsp;&emsp;&emsp;&emsp;到达状态[130]: exp7[0] -> * [ exp7 [1]  ( `MUL` [2] | `DIV` [2] | `MOD` [2] ) ]  exp8 [0] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[57: `INC_OP`，操作符，PLUS_PLUS]&emsp;[4: `INTEGER`，整数，(null)]&emsp;[58: `DEC_OP`，操作符，MINUS_MINUS]&emsp;[1: `BOOLEAN`，布尔，(null)]&emsp;[44: `CALL`，关键字，CALL]&emsp;[3: `CHARACTER`，字符，(null)]&emsp;[100: `LBR`，操作符，LBRACE]&emsp;[86: `NOT`，操作符，BIT_NOT]&emsp;[87: `NOT_OP`，操作符，LOGICAL_NOT]&emsp;[50: `SET`，关键字，SET]&emsp;[5: `DECIMAL`，实数，(null)]&emsp;[98: `LSQ`，操作符，LSQUARE]&emsp;[96: `LPA`，操作符，LPARAN]&emsp;[51: `INVOKE`，关键字，INVOKE]&emsp;[2: `LITERAL`，字符串，(null)]&emsp;[0: `ID`，标识符，(null)]&emsp;[49: `LAMBDA`，关键字，LAMBDA]&emsp;



## 状态[114]： 

项目：exp6[0] ->  [ exp6 [1]  ( `ADD` [2] | `SUB` [2] ) ]  exp7 [0]  *

规则：exp6

----

&emsp;&emsp;&emsp;&emsp;到达状态[131]: exp5[0] ->  [ exp5 [1]  ( `LEFT_OP` [2] | `RIGHT_OP` [2] ) ]  exp6 [0]  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> exp5[0] -> * [ exp5 [1]  ( `LEFT_OP` [2] | `RIGHT_OP` [2] ) ]  exp6 [0] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp6 0

----

&emsp;&emsp;&emsp;&emsp;到达状态[131]: exp5[0] ->  [ exp5 [1]  ( `LEFT_OP` [2] | `RIGHT_OP` [2] ) ]  exp6 [0]  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> exp5[0] ->  [ exp5 [1]  ( `LEFT_OP` [2] | `RIGHT_OP` [2] )  *]  exp6 [0] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp6 0

----

&emsp;&emsp;&emsp;&emsp;到达状态[132]: exp6[0] ->  [ exp6 [1]  * ( `ADD` [2] | `SUB` [2] ) ]  exp7 [0] 

&emsp;&emsp;&emsp;&emsp;类型：左递归

&emsp;&emsp;&emsp;&emsp;指令：左递归	=> exp6 1

&emsp;&emsp;&emsp;&emsp;预查：[78: `ADD`，操作符，PLUS]&emsp;[79: `SUB`，操作符，MINUS]&emsp;



## 状态[115]： 

项目：exp7[0] ->  [ exp7 [1]  * ( `MUL` [2] | `DIV` [2] | `MOD` [2] ) ]  exp8 [0] 

规则：exp7

----

&emsp;&emsp;&emsp;&emsp;到达状态[133]: exp7[0] ->  [ exp7 [1]  ( `MUL` [2] | `DIV` [2] | `MOD` [2] )  *]  exp8 [0] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 80(80: `MUL`，操作符，TIMES)

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 2

----

&emsp;&emsp;&emsp;&emsp;到达状态[133]: exp7[0] ->  [ exp7 [1]  ( `MUL` [2] | `DIV` [2] | `MOD` [2] )  *]  exp8 [0] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 81(81: `DIV`，操作符，DIVIDE)

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 2

----

&emsp;&emsp;&emsp;&emsp;到达状态[133]: exp7[0] ->  [ exp7 [1]  ( `MUL` [2] | `DIV` [2] | `MOD` [2] )  *]  exp8 [0] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 82(82: `MOD`，操作符，MOD)

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 2



## 状态[116]： 

项目：type[0] ->  ( `ID` [0]  * [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `INTEGER` [0] | `DECIMAL` [0] | `LITERAL` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `CHARACTER` [0] | `BOOLEAN` [0] | `LPA`  exp [1]  `RPA` | call [1] | lambda [2] | set [1] | invoke [1] | array [1] | map [1] ) 

规则：type

----

&emsp;&emsp;&emsp;&emsp;到达状态[134]: type[0] ->  ( `ID` [0]  [ `LPA` [3]  * [ exp_list [4] ]  `RPA` ] | `INTEGER` [0] | `DECIMAL` [0] | `LITERAL` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `CHARACTER` [0] | `BOOLEAN` [0] | `LPA`  exp [1]  `RPA` | call [1] | lambda [2] | set [1] | invoke [1] | array [1] | map [1] ) 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 96(96: `LPA`，操作符，LPARAN)

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 3

----

&emsp;&emsp;&emsp;&emsp;到达状态[85]: exp10[0] ->  [ exp10 [1]  `DOT` [2] ]  type [0]  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> exp10[0] -> * [ exp10 [1]  `DOT` [2] ]  type [0] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> type 0

----

&emsp;&emsp;&emsp;&emsp;到达状态[85]: exp10[0] ->  [ exp10 [1]  `DOT` [2] ]  type [0]  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> exp10[0] ->  [ exp10 [1]  `DOT` [2] ]  * type [0] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> type 0



## 状态[117]： 

项目：type[0] ->  ( `ID` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `INTEGER` [0] | `DECIMAL` [0] | `LITERAL` [0]  * [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `CHARACTER` [0] | `BOOLEAN` [0] | `LPA`  exp [1]  `RPA` | call [1] | lambda [2] | set [1] | invoke [1] | array [1] | map [1] ) 

规则：type

----

&emsp;&emsp;&emsp;&emsp;到达状态[135]: type[0] ->  ( `ID` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `INTEGER` [0] | `DECIMAL` [0] | `LITERAL` [0]  [ `LPA` [3]  * [ exp_list [4] ]  `RPA` ] | `CHARACTER` [0] | `BOOLEAN` [0] | `LPA`  exp [1]  `RPA` | call [1] | lambda [2] | set [1] | invoke [1] | array [1] | map [1] ) 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 96(96: `LPA`，操作符，LPARAN)

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 3

----

&emsp;&emsp;&emsp;&emsp;到达状态[85]: exp10[0] ->  [ exp10 [1]  `DOT` [2] ]  type [0]  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> exp10[0] -> * [ exp10 [1]  `DOT` [2] ]  type [0] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> type 0

----

&emsp;&emsp;&emsp;&emsp;到达状态[85]: exp10[0] ->  [ exp10 [1]  `DOT` [2] ]  type [0]  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> exp10[0] ->  [ exp10 [1]  `DOT` [2] ]  * type [0] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> type 0



## 状态[118]： 

项目：type[0] ->  ( `ID` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `INTEGER` [0] | `DECIMAL` [0] | `LITERAL` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `CHARACTER` [0] | `BOOLEAN` [0] | `LPA`  * exp [1]  `RPA` | call [1] | lambda [2] | set [1] | invoke [1] | array [1] | map [1] ) 

规则：type

----

&emsp;&emsp;&emsp;&emsp;到达状态[20]: exp[0] -> * exp01 [0]  [ ( `INTEGER` [10] | `DECIMAL` [10] ) ] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[57: `INC_OP`，操作符，PLUS_PLUS]&emsp;[4: `INTEGER`，整数，(null)]&emsp;[58: `DEC_OP`，操作符，MINUS_MINUS]&emsp;[1: `BOOLEAN`，布尔，(null)]&emsp;[44: `CALL`，关键字，CALL]&emsp;[3: `CHARACTER`，字符，(null)]&emsp;[100: `LBR`，操作符，LBRACE]&emsp;[86: `NOT`，操作符，BIT_NOT]&emsp;[87: `NOT_OP`，操作符，LOGICAL_NOT]&emsp;[50: `SET`，关键字，SET]&emsp;[5: `DECIMAL`，实数，(null)]&emsp;[98: `LSQ`，操作符，LSQUARE]&emsp;[96: `LPA`，操作符，LPARAN]&emsp;[51: `INVOKE`，关键字，INVOKE]&emsp;[2: `LITERAL`，字符串，(null)]&emsp;[0: `ID`，标识符，(null)]&emsp;[49: `LAMBDA`，关键字，LAMBDA]&emsp;



## 状态[119]： 

项目：lambda[0] -> * `LAMBDA` [1]  `LPA`  [ var_list [2] ]  `RPA`  ( `PTR_OP`  scope [3] | block [4] ) 

规则：lambda

----

&emsp;&emsp;&emsp;&emsp;到达状态[136]: lambda[0] ->  `LAMBDA` [1]  * `LPA`  [ var_list [2] ]  `RPA`  ( `PTR_OP`  scope [3] | block [4] ) 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 49(49: `LAMBDA`，关键字，LAMBDA)

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 1  [Action]



## 状态[120]： 

项目：set[0] -> * `SET`  exp [3]  `PROPERTY`  exp [4]  `ASSIGN`  exp [2] 

规则：set

----

&emsp;&emsp;&emsp;&emsp;到达状态[137]: set[0] ->  `SET`  * exp [3]  `PROPERTY`  exp [4]  `ASSIGN`  exp [2] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 50(50: `SET`，关键字，SET)

&emsp;&emsp;&emsp;&emsp;指令：通过



## 状态[121]： 

项目：invoke[0] -> * `INVOKE` [0]  exp [1]  `PROPERTY`  exp [2]  `PROPERTY`  `LPA`  [ exp_list [3] ]  `RPA` 

规则：invoke

----

&emsp;&emsp;&emsp;&emsp;到达状态[138]: invoke[0] ->  `INVOKE` [0]  * exp [1]  `PROPERTY`  exp [2]  `PROPERTY`  `LPA`  [ exp_list [3] ]  `RPA` 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 51(51: `INVOKE`，关键字，INVOKE)

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 0



## 状态[122]： 

项目：array[0] -> * `LSQ`  [ exp_list [0] ]  `RSQ` 

规则：array

----

&emsp;&emsp;&emsp;&emsp;到达状态[139]: array[0] ->  `LSQ`  * [ exp_list [0] ]  `RSQ` 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 98(98: `LSQ`，操作符，LSQUARE)

&emsp;&emsp;&emsp;&emsp;指令：通过



## 状态[123]： 

项目：map[0] -> * `LBR`  [ map_list [0] ]  `RBR` 

规则：map

----

&emsp;&emsp;&emsp;&emsp;到达状态[140]: map[0] ->  `LBR`  * [ map_list [0] ]  `RBR` 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 100(100: `LBR`，操作符，LBRACE)

&emsp;&emsp;&emsp;&emsp;指令：通过



## 状态[124]： 

项目：func[0] ->  ( `FUNCTION` [10] | `YIELD` )  [ `LSQ`  doc_list [0]  `RSQ` ]  * ( `ID` [1] | `NOT` [1] )  `LPA`  [ var_list [2] ]  `RPA`  ( `PTR_OP`  scope [3] | block [4] ) 

规则：func

----

&emsp;&emsp;&emsp;&emsp;到达状态[88]: func[0] ->  ( `FUNCTION` [10] | `YIELD` )  [ `LSQ`  doc_list [0]  `RSQ` ]  ( `ID` [1] | `NOT` [1] )  * `LPA`  [ var_list [2] ]  `RPA`  ( `PTR_OP`  scope [3] | block [4] ) 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 0(0: `ID`，标识符，(null))

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 1  [Action]

----

&emsp;&emsp;&emsp;&emsp;到达状态[88]: func[0] ->  ( `FUNCTION` [10] | `YIELD` )  [ `LSQ`  doc_list [0]  `RSQ` ]  ( `ID` [1] | `NOT` [1] )  * `LPA`  [ var_list [2] ]  `RPA`  ( `PTR_OP`  scope [3] | block [4] ) 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 86(86: `NOT`，操作符，BIT_NOT)

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 1  [Action]



## 状态[125]： 

项目：var_list[0] ->  `ID` [0]  [ `COMMA`  * var_list [1] ] 

规则：var_list

----

&emsp;&emsp;&emsp;&emsp;到达状态[101]: var_list[0] -> * `ID` [0]  [ `COMMA`  var_list [1] ] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[0: `ID`，标识符，(null)]&emsp;



## 状态[126]： 

项目：func[0] ->  ( `FUNCTION` [10] | `YIELD` )  [ `LSQ`  doc_list [0]  `RSQ` ]  ( `ID` [1] | `NOT` [1] )  `LPA`  [ var_list [2] ]  * `RPA`  ( `PTR_OP`  scope [3] | block [4] ) 

规则：func

----

&emsp;&emsp;&emsp;&emsp;到达状态[102]: func[0] ->  ( `FUNCTION` [10] | `YIELD` )  [ `LSQ`  doc_list [0]  `RSQ` ]  ( `ID` [1] | `NOT` [1] )  `LPA`  [ var_list [2] ]  `RPA`  * ( `PTR_OP`  scope [3] | block [4] ) 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 97(97: `RPA`，操作符，RPARAN)

&emsp;&emsp;&emsp;&emsp;指令：通过



## 状态[127]： 

项目：lambda[0] ->  `LAMBDA` [1]  `LPA`  [ var_list [2] ]  * `RPA`  ( `PTR_OP`  scope [3] | block [4] ) 

规则：lambda

----

&emsp;&emsp;&emsp;&emsp;到达状态[141]: lambda[0] ->  `LAMBDA` [1]  `LPA`  [ var_list [2] ]  `RPA`  * ( `PTR_OP`  scope [3] | block [4] ) 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 97(97: `RPA`，操作符，RPARAN)

&emsp;&emsp;&emsp;&emsp;指令：通过



## 状态[128]： 

项目：var_list[0] ->  `ID` [0]  [ `COMMA`  var_list [1] ]  *

规则：var_list

----

&emsp;&emsp;&emsp;&emsp;到达状态[126]: func[0] ->  ( `FUNCTION` [10] | `YIELD` )  [ `LSQ`  doc_list [0]  `RSQ` ]  ( `ID` [1] | `NOT` [1] )  `LPA`  [ var_list [2] ]  * `RPA`  ( `PTR_OP`  scope [3] | block [4] ) 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> func[0] ->  ( `FUNCTION` [10] | `YIELD` )  [ `LSQ`  doc_list [0]  `RSQ` ]  ( `ID` [1] | `NOT` [1] )  `LPA`  * [ var_list [2] ]  `RPA`  ( `PTR_OP`  scope [3] | block [4] ) 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> var_list 2

----

&emsp;&emsp;&emsp;&emsp;到达状态[127]: lambda[0] ->  `LAMBDA` [1]  `LPA`  [ var_list [2] ]  * `RPA`  ( `PTR_OP`  scope [3] | block [4] ) 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> lambda[0] ->  `LAMBDA` [1]  `LPA`  * [ var_list [2] ]  `RPA`  ( `PTR_OP`  scope [3] | block [4] ) 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> var_list 2

----

&emsp;&emsp;&emsp;&emsp;到达状态[128]: var_list[0] ->  `ID` [0]  [ `COMMA`  var_list [1] ]  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> var_list[0] ->  `ID` [0]  [ `COMMA`  * var_list [1] ] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> var_list 1



## 状态[129]： 

项目：scope[0] -> * exp [0] 

规则：scope

----

&emsp;&emsp;&emsp;&emsp;到达状态[20]: exp[0] -> * exp01 [0]  [ ( `INTEGER` [10] | `DECIMAL` [10] ) ] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[57: `INC_OP`，操作符，PLUS_PLUS]&emsp;[4: `INTEGER`，整数，(null)]&emsp;[58: `DEC_OP`，操作符，MINUS_MINUS]&emsp;[1: `BOOLEAN`，布尔，(null)]&emsp;[44: `CALL`，关键字，CALL]&emsp;[3: `CHARACTER`，字符，(null)]&emsp;[100: `LBR`，操作符，LBRACE]&emsp;[86: `NOT`，操作符，BIT_NOT]&emsp;[87: `NOT_OP`，操作符，LOGICAL_NOT]&emsp;[50: `SET`，关键字，SET]&emsp;[5: `DECIMAL`，实数，(null)]&emsp;[98: `LSQ`，操作符，LSQUARE]&emsp;[96: `LPA`，操作符，LPARAN]&emsp;[51: `INVOKE`，关键字，INVOKE]&emsp;[2: `LITERAL`，字符串，(null)]&emsp;[0: `ID`，标识符，(null)]&emsp;[49: `LAMBDA`，关键字，LAMBDA]&emsp;



## 状态[130]： 

项目：exp7[0] -> * [ exp7 [1]  ( `MUL` [2] | `DIV` [2] | `MOD` [2] ) ]  exp8 [0] 

规则：exp7

----

&emsp;&emsp;&emsp;&emsp;到达状态[142]: exp8[0] -> * ( ( `NOT_OP` [3] | `NOT` [3] )  exp8 [1] | exp9 [0] ) 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[57: `INC_OP`，操作符，PLUS_PLUS]&emsp;[4: `INTEGER`，整数，(null)]&emsp;[58: `DEC_OP`，操作符，MINUS_MINUS]&emsp;[1: `BOOLEAN`，布尔，(null)]&emsp;[44: `CALL`，关键字，CALL]&emsp;[3: `CHARACTER`，字符，(null)]&emsp;[100: `LBR`，操作符，LBRACE]&emsp;[86: `NOT`，操作符，BIT_NOT]&emsp;[87: `NOT_OP`，操作符，LOGICAL_NOT]&emsp;[50: `SET`，关键字，SET]&emsp;[5: `DECIMAL`，实数，(null)]&emsp;[98: `LSQ`，操作符，LSQUARE]&emsp;[96: `LPA`，操作符，LPARAN]&emsp;[51: `INVOKE`，关键字，INVOKE]&emsp;[2: `LITERAL`，字符串，(null)]&emsp;[0: `ID`，标识符，(null)]&emsp;[49: `LAMBDA`，关键字，LAMBDA]&emsp;



## 状态[131]： 

项目：exp5[0] ->  [ exp5 [1]  ( `LEFT_OP` [2] | `RIGHT_OP` [2] ) ]  exp6 [0]  *

规则：exp5

----

&emsp;&emsp;&emsp;&emsp;到达状态[143]: exp4[0] ->  [ exp4 [1]  ( `LT` [2] | `GT` [2] | `LE_OP` [2] | `GE_OP` [2] ) ]  exp5 [0]  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> exp4[0] -> * [ exp4 [1]  ( `LT` [2] | `GT` [2] | `LE_OP` [2] | `GE_OP` [2] ) ]  exp5 [0] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp5 0

----

&emsp;&emsp;&emsp;&emsp;到达状态[143]: exp4[0] ->  [ exp4 [1]  ( `LT` [2] | `GT` [2] | `LE_OP` [2] | `GE_OP` [2] ) ]  exp5 [0]  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> exp4[0] ->  [ exp4 [1]  ( `LT` [2] | `GT` [2] | `LE_OP` [2] | `GE_OP` [2] )  *]  exp5 [0] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp5 0

----

&emsp;&emsp;&emsp;&emsp;到达状态[144]: exp5[0] ->  [ exp5 [1]  * ( `LEFT_OP` [2] | `RIGHT_OP` [2] ) ]  exp6 [0] 

&emsp;&emsp;&emsp;&emsp;类型：左递归

&emsp;&emsp;&emsp;&emsp;指令：左递归	=> exp5 1

&emsp;&emsp;&emsp;&emsp;预查：[59: `LEFT_OP`，操作符，LEFT_SHIFT]&emsp;[60: `RIGHT_OP`，操作符，RIGHT_SHIFT]&emsp;



## 状态[132]： 

项目：exp6[0] ->  [ exp6 [1]  * ( `ADD` [2] | `SUB` [2] ) ]  exp7 [0] 

规则：exp6

----

&emsp;&emsp;&emsp;&emsp;到达状态[145]: exp6[0] ->  [ exp6 [1]  ( `ADD` [2] | `SUB` [2] )  *]  exp7 [0] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 78(78: `ADD`，操作符，PLUS)

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 2

----

&emsp;&emsp;&emsp;&emsp;到达状态[145]: exp6[0] ->  [ exp6 [1]  ( `ADD` [2] | `SUB` [2] )  *]  exp7 [0] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 79(79: `SUB`，操作符，MINUS)

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 2



## 状态[133]： 

项目：exp7[0] ->  [ exp7 [1]  ( `MUL` [2] | `DIV` [2] | `MOD` [2] )  *]  exp8 [0] 

规则：exp7

----

&emsp;&emsp;&emsp;&emsp;到达状态[142]: exp8[0] -> * ( ( `NOT_OP` [3] | `NOT` [3] )  exp8 [1] | exp9 [0] ) 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[57: `INC_OP`，操作符，PLUS_PLUS]&emsp;[4: `INTEGER`，整数，(null)]&emsp;[58: `DEC_OP`，操作符，MINUS_MINUS]&emsp;[1: `BOOLEAN`，布尔，(null)]&emsp;[44: `CALL`，关键字，CALL]&emsp;[3: `CHARACTER`，字符，(null)]&emsp;[100: `LBR`，操作符，LBRACE]&emsp;[86: `NOT`，操作符，BIT_NOT]&emsp;[87: `NOT_OP`，操作符，LOGICAL_NOT]&emsp;[50: `SET`，关键字，SET]&emsp;[5: `DECIMAL`，实数，(null)]&emsp;[98: `LSQ`，操作符，LSQUARE]&emsp;[96: `LPA`，操作符，LPARAN]&emsp;[51: `INVOKE`，关键字，INVOKE]&emsp;[2: `LITERAL`，字符串，(null)]&emsp;[0: `ID`，标识符，(null)]&emsp;[49: `LAMBDA`，关键字，LAMBDA]&emsp;



## 状态[134]： 

项目：type[0] ->  ( `ID` [0]  [ `LPA` [3]  * [ exp_list [4] ]  `RPA` ] | `INTEGER` [0] | `DECIMAL` [0] | `LITERAL` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `CHARACTER` [0] | `BOOLEAN` [0] | `LPA`  exp [1]  `RPA` | call [1] | lambda [2] | set [1] | invoke [1] | array [1] | map [1] ) 

规则：type

----

&emsp;&emsp;&emsp;&emsp;到达状态[81]: exp_list[0] -> * exp [0]  [ `COMMA`  exp_list [1] ] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[57: `INC_OP`，操作符，PLUS_PLUS]&emsp;[4: `INTEGER`，整数，(null)]&emsp;[58: `DEC_OP`，操作符，MINUS_MINUS]&emsp;[1: `BOOLEAN`，布尔，(null)]&emsp;[44: `CALL`，关键字，CALL]&emsp;[3: `CHARACTER`，字符，(null)]&emsp;[100: `LBR`，操作符，LBRACE]&emsp;[86: `NOT`，操作符，BIT_NOT]&emsp;[87: `NOT_OP`，操作符，LOGICAL_NOT]&emsp;[50: `SET`，关键字，SET]&emsp;[5: `DECIMAL`，实数，(null)]&emsp;[98: `LSQ`，操作符，LSQUARE]&emsp;[96: `LPA`，操作符，LPARAN]&emsp;[51: `INVOKE`，关键字，INVOKE]&emsp;[2: `LITERAL`，字符串，(null)]&emsp;[0: `ID`，标识符，(null)]&emsp;[49: `LAMBDA`，关键字，LAMBDA]&emsp;

----

&emsp;&emsp;&emsp;&emsp;到达状态[76]: type[0] ->  ( `ID` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `INTEGER` [0] | `DECIMAL` [0] | `LITERAL` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `CHARACTER` [0] | `BOOLEAN` [0] | `LPA`  exp [1]  `RPA` | call [1] | lambda [2] | set [1] | invoke [1] | array [1] | map [1] )  *

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 97(97: `RPA`，操作符，RPARAN)

&emsp;&emsp;&emsp;&emsp;指令：通过



## 状态[135]： 

项目：type[0] ->  ( `ID` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `INTEGER` [0] | `DECIMAL` [0] | `LITERAL` [0]  [ `LPA` [3]  * [ exp_list [4] ]  `RPA` ] | `CHARACTER` [0] | `BOOLEAN` [0] | `LPA`  exp [1]  `RPA` | call [1] | lambda [2] | set [1] | invoke [1] | array [1] | map [1] ) 

规则：type

----

&emsp;&emsp;&emsp;&emsp;到达状态[81]: exp_list[0] -> * exp [0]  [ `COMMA`  exp_list [1] ] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[57: `INC_OP`，操作符，PLUS_PLUS]&emsp;[4: `INTEGER`，整数，(null)]&emsp;[58: `DEC_OP`，操作符，MINUS_MINUS]&emsp;[1: `BOOLEAN`，布尔，(null)]&emsp;[44: `CALL`，关键字，CALL]&emsp;[3: `CHARACTER`，字符，(null)]&emsp;[100: `LBR`，操作符，LBRACE]&emsp;[86: `NOT`，操作符，BIT_NOT]&emsp;[87: `NOT_OP`，操作符，LOGICAL_NOT]&emsp;[50: `SET`，关键字，SET]&emsp;[5: `DECIMAL`，实数，(null)]&emsp;[98: `LSQ`，操作符，LSQUARE]&emsp;[96: `LPA`，操作符，LPARAN]&emsp;[51: `INVOKE`，关键字，INVOKE]&emsp;[2: `LITERAL`，字符串，(null)]&emsp;[0: `ID`，标识符，(null)]&emsp;[49: `LAMBDA`，关键字，LAMBDA]&emsp;

----

&emsp;&emsp;&emsp;&emsp;到达状态[76]: type[0] ->  ( `ID` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `INTEGER` [0] | `DECIMAL` [0] | `LITERAL` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `CHARACTER` [0] | `BOOLEAN` [0] | `LPA`  exp [1]  `RPA` | call [1] | lambda [2] | set [1] | invoke [1] | array [1] | map [1] )  *

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 97(97: `RPA`，操作符，RPARAN)

&emsp;&emsp;&emsp;&emsp;指令：通过



## 状态[136]： 

项目：lambda[0] ->  `LAMBDA` [1]  * `LPA`  [ var_list [2] ]  `RPA`  ( `PTR_OP`  scope [3] | block [4] ) 

规则：lambda

----

&emsp;&emsp;&emsp;&emsp;到达状态[146]: lambda[0] ->  `LAMBDA` [1]  `LPA`  * [ var_list [2] ]  `RPA`  ( `PTR_OP`  scope [3] | block [4] ) 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 96(96: `LPA`，操作符，LPARAN)

&emsp;&emsp;&emsp;&emsp;指令：通过



## 状态[137]： 

项目：set[0] ->  `SET`  * exp [3]  `PROPERTY`  exp [4]  `ASSIGN`  exp [2] 

规则：set

----

&emsp;&emsp;&emsp;&emsp;到达状态[20]: exp[0] -> * exp01 [0]  [ ( `INTEGER` [10] | `DECIMAL` [10] ) ] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[57: `INC_OP`，操作符，PLUS_PLUS]&emsp;[4: `INTEGER`，整数，(null)]&emsp;[58: `DEC_OP`，操作符，MINUS_MINUS]&emsp;[1: `BOOLEAN`，布尔，(null)]&emsp;[44: `CALL`，关键字，CALL]&emsp;[3: `CHARACTER`，字符，(null)]&emsp;[100: `LBR`，操作符，LBRACE]&emsp;[86: `NOT`，操作符，BIT_NOT]&emsp;[87: `NOT_OP`，操作符，LOGICAL_NOT]&emsp;[50: `SET`，关键字，SET]&emsp;[5: `DECIMAL`，实数，(null)]&emsp;[98: `LSQ`，操作符，LSQUARE]&emsp;[96: `LPA`，操作符，LPARAN]&emsp;[51: `INVOKE`，关键字，INVOKE]&emsp;[2: `LITERAL`，字符串，(null)]&emsp;[0: `ID`，标识符，(null)]&emsp;[49: `LAMBDA`，关键字，LAMBDA]&emsp;



## 状态[138]： 

项目：invoke[0] ->  `INVOKE` [0]  * exp [1]  `PROPERTY`  exp [2]  `PROPERTY`  `LPA`  [ exp_list [3] ]  `RPA` 

规则：invoke

----

&emsp;&emsp;&emsp;&emsp;到达状态[20]: exp[0] -> * exp01 [0]  [ ( `INTEGER` [10] | `DECIMAL` [10] ) ] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[57: `INC_OP`，操作符，PLUS_PLUS]&emsp;[4: `INTEGER`，整数，(null)]&emsp;[58: `DEC_OP`，操作符，MINUS_MINUS]&emsp;[1: `BOOLEAN`，布尔，(null)]&emsp;[44: `CALL`，关键字，CALL]&emsp;[3: `CHARACTER`，字符，(null)]&emsp;[100: `LBR`，操作符，LBRACE]&emsp;[86: `NOT`，操作符，BIT_NOT]&emsp;[87: `NOT_OP`，操作符，LOGICAL_NOT]&emsp;[50: `SET`，关键字，SET]&emsp;[5: `DECIMAL`，实数，(null)]&emsp;[98: `LSQ`，操作符，LSQUARE]&emsp;[96: `LPA`，操作符，LPARAN]&emsp;[51: `INVOKE`，关键字，INVOKE]&emsp;[2: `LITERAL`，字符串，(null)]&emsp;[0: `ID`，标识符，(null)]&emsp;[49: `LAMBDA`，关键字，LAMBDA]&emsp;



## 状态[139]： 

项目：array[0] ->  `LSQ`  * [ exp_list [0] ]  `RSQ` 

规则：array

----

&emsp;&emsp;&emsp;&emsp;到达状态[81]: exp_list[0] -> * exp [0]  [ `COMMA`  exp_list [1] ] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[57: `INC_OP`，操作符，PLUS_PLUS]&emsp;[4: `INTEGER`，整数，(null)]&emsp;[58: `DEC_OP`，操作符，MINUS_MINUS]&emsp;[1: `BOOLEAN`，布尔，(null)]&emsp;[44: `CALL`，关键字，CALL]&emsp;[3: `CHARACTER`，字符，(null)]&emsp;[100: `LBR`，操作符，LBRACE]&emsp;[86: `NOT`，操作符，BIT_NOT]&emsp;[87: `NOT_OP`，操作符，LOGICAL_NOT]&emsp;[50: `SET`，关键字，SET]&emsp;[5: `DECIMAL`，实数，(null)]&emsp;[98: `LSQ`，操作符，LSQUARE]&emsp;[96: `LPA`，操作符，LPARAN]&emsp;[51: `INVOKE`，关键字，INVOKE]&emsp;[2: `LITERAL`，字符串，(null)]&emsp;[0: `ID`，标识符，(null)]&emsp;[49: `LAMBDA`，关键字，LAMBDA]&emsp;

----

&emsp;&emsp;&emsp;&emsp;到达状态[147]: array[0] ->  `LSQ`  [ exp_list [0] ]  `RSQ`  *

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 99(99: `RSQ`，操作符，RSQUARE)

&emsp;&emsp;&emsp;&emsp;指令：通过



## 状态[140]： 

项目：map[0] ->  `LBR`  * [ map_list [0] ]  `RBR` 

规则：map

----

&emsp;&emsp;&emsp;&emsp;到达状态[148]: map_list[0] -> * `LITERAL` [1]  `COLON` [3]  exp [2]  [ `COMMA`  map_list [0] ] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[2: `LITERAL`，字符串，(null)]&emsp;

----

&emsp;&emsp;&emsp;&emsp;到达状态[149]: map[0] ->  `LBR`  [ map_list [0] ]  `RBR`  *

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 101(101: `RBR`，操作符，RBRACE)

&emsp;&emsp;&emsp;&emsp;指令：通过



## 状态[141]： 

项目：lambda[0] ->  `LAMBDA` [1]  `LPA`  [ var_list [2] ]  `RPA`  * ( `PTR_OP`  scope [3] | block [4] ) 

规则：lambda

----

&emsp;&emsp;&emsp;&emsp;到达状态[150]: lambda[0] ->  `LAMBDA` [1]  `LPA`  [ var_list [2] ]  `RPA`  ( `PTR_OP`  * scope [3] | block [4] ) 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 56(56: `PTR_OP`，操作符，POINTER)

&emsp;&emsp;&emsp;&emsp;指令：通过  [Action]

----

&emsp;&emsp;&emsp;&emsp;到达状态[16]: block[0] -> * `LBR`  [ stmt_list [0] ]  `RBR` 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[100: `LBR`，操作符，LBRACE]&emsp;



## 状态[142]： 

项目：exp8[0] -> * ( ( `NOT_OP` [3] | `NOT` [3] )  exp8 [1] | exp9 [0] ) 

规则：exp8

----

&emsp;&emsp;&emsp;&emsp;到达状态[151]: exp9[0] -> * ( ( `INC_OP` [3] | `DEC_OP` [3] )  exp9 [1] | exp9 [1]  ( `INC_OP` [3] | `DEC_OP` [3] | `LSQ`  exp [5]  `RSQ` ) | exp10 [0] ) 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[57: `INC_OP`，操作符，PLUS_PLUS]&emsp;[4: `INTEGER`，整数，(null)]&emsp;[58: `DEC_OP`，操作符，MINUS_MINUS]&emsp;[1: `BOOLEAN`，布尔，(null)]&emsp;[44: `CALL`，关键字，CALL]&emsp;[3: `CHARACTER`，字符，(null)]&emsp;[100: `LBR`，操作符，LBRACE]&emsp;[50: `SET`，关键字，SET]&emsp;[5: `DECIMAL`，实数，(null)]&emsp;[98: `LSQ`，操作符，LSQUARE]&emsp;[96: `LPA`，操作符，LPARAN]&emsp;[51: `INVOKE`，关键字，INVOKE]&emsp;[2: `LITERAL`，字符串，(null)]&emsp;[0: `ID`，标识符，(null)]&emsp;[49: `LAMBDA`，关键字，LAMBDA]&emsp;

----

&emsp;&emsp;&emsp;&emsp;到达状态[152]: exp8[0] ->  ( ( `NOT_OP` [3] | `NOT` [3] )  * exp8 [1] | exp9 [0] ) 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 87(87: `NOT_OP`，操作符，LOGICAL_NOT)

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 3

----

&emsp;&emsp;&emsp;&emsp;到达状态[152]: exp8[0] ->  ( ( `NOT_OP` [3] | `NOT` [3] )  * exp8 [1] | exp9 [0] ) 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 86(86: `NOT`，操作符，BIT_NOT)

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 3



## 状态[143]： 

项目：exp4[0] ->  [ exp4 [1]  ( `LT` [2] | `GT` [2] | `LE_OP` [2] | `GE_OP` [2] ) ]  exp5 [0]  *

规则：exp4

----

&emsp;&emsp;&emsp;&emsp;到达状态[153]: exp3[0] ->  [ exp3 [1]  ( `EQ_OP` [2] | `NE_OP` [2] ) ]  exp4 [0]  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> exp3[0] -> * [ exp3 [1]  ( `EQ_OP` [2] | `NE_OP` [2] ) ]  exp4 [0] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp4 0

----

&emsp;&emsp;&emsp;&emsp;到达状态[153]: exp3[0] ->  [ exp3 [1]  ( `EQ_OP` [2] | `NE_OP` [2] ) ]  exp4 [0]  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> exp3[0] ->  [ exp3 [1]  ( `EQ_OP` [2] | `NE_OP` [2] )  *]  exp4 [0] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp4 0

----

&emsp;&emsp;&emsp;&emsp;到达状态[154]: exp4[0] ->  [ exp4 [1]  * ( `LT` [2] | `GT` [2] | `LE_OP` [2] | `GE_OP` [2] ) ]  exp5 [0] 

&emsp;&emsp;&emsp;&emsp;类型：左递归

&emsp;&emsp;&emsp;&emsp;指令：左递归	=> exp4 1

&emsp;&emsp;&emsp;&emsp;预查：[88: `LT`，操作符，LESS_THAN]&emsp;[89: `GT`，操作符，GREATER_THAN]&emsp;[61: `LE_OP`，操作符，LESS_THAN_OR_EQUAL]&emsp;[62: `GE_OP`，操作符，GREATER_THAN_OR_EQUAL]&emsp;



## 状态[144]： 

项目：exp5[0] ->  [ exp5 [1]  * ( `LEFT_OP` [2] | `RIGHT_OP` [2] ) ]  exp6 [0] 

规则：exp5

----

&emsp;&emsp;&emsp;&emsp;到达状态[155]: exp5[0] ->  [ exp5 [1]  ( `LEFT_OP` [2] | `RIGHT_OP` [2] )  *]  exp6 [0] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 59(59: `LEFT_OP`，操作符，LEFT_SHIFT)

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 2

----

&emsp;&emsp;&emsp;&emsp;到达状态[155]: exp5[0] ->  [ exp5 [1]  ( `LEFT_OP` [2] | `RIGHT_OP` [2] )  *]  exp6 [0] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 60(60: `RIGHT_OP`，操作符，RIGHT_SHIFT)

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 2



## 状态[145]： 

项目：exp6[0] ->  [ exp6 [1]  ( `ADD` [2] | `SUB` [2] )  *]  exp7 [0] 

规则：exp6

----

&emsp;&emsp;&emsp;&emsp;到达状态[130]: exp7[0] -> * [ exp7 [1]  ( `MUL` [2] | `DIV` [2] | `MOD` [2] ) ]  exp8 [0] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[57: `INC_OP`，操作符，PLUS_PLUS]&emsp;[4: `INTEGER`，整数，(null)]&emsp;[58: `DEC_OP`，操作符，MINUS_MINUS]&emsp;[1: `BOOLEAN`，布尔，(null)]&emsp;[44: `CALL`，关键字，CALL]&emsp;[3: `CHARACTER`，字符，(null)]&emsp;[100: `LBR`，操作符，LBRACE]&emsp;[86: `NOT`，操作符，BIT_NOT]&emsp;[87: `NOT_OP`，操作符，LOGICAL_NOT]&emsp;[50: `SET`，关键字，SET]&emsp;[5: `DECIMAL`，实数，(null)]&emsp;[98: `LSQ`，操作符，LSQUARE]&emsp;[96: `LPA`，操作符，LPARAN]&emsp;[51: `INVOKE`，关键字，INVOKE]&emsp;[2: `LITERAL`，字符串，(null)]&emsp;[0: `ID`，标识符，(null)]&emsp;[49: `LAMBDA`，关键字，LAMBDA]&emsp;



## 状态[146]： 

项目：lambda[0] ->  `LAMBDA` [1]  `LPA`  * [ var_list [2] ]  `RPA`  ( `PTR_OP`  scope [3] | block [4] ) 

规则：lambda

----

&emsp;&emsp;&emsp;&emsp;到达状态[101]: var_list[0] -> * `ID` [0]  [ `COMMA`  var_list [1] ] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[0: `ID`，标识符，(null)]&emsp;

----

&emsp;&emsp;&emsp;&emsp;到达状态[141]: lambda[0] ->  `LAMBDA` [1]  `LPA`  [ var_list [2] ]  `RPA`  * ( `PTR_OP`  scope [3] | block [4] ) 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 97(97: `RPA`，操作符，RPARAN)

&emsp;&emsp;&emsp;&emsp;指令：通过



## 状态[147]： 

项目：array[0] ->  `LSQ`  [ exp_list [0] ]  `RSQ`  *

规则：array

----

&emsp;&emsp;&emsp;&emsp;到达状态[76]: type[0] ->  ( `ID` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `INTEGER` [0] | `DECIMAL` [0] | `LITERAL` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `CHARACTER` [0] | `BOOLEAN` [0] | `LPA`  exp [1]  `RPA` | call [1] | lambda [2] | set [1] | invoke [1] | array [1] | map [1] )  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> type[0] -> * ( `ID` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `INTEGER` [0] | `DECIMAL` [0] | `LITERAL` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `CHARACTER` [0] | `BOOLEAN` [0] | `LPA`  exp [1]  `RPA` | call [1] | lambda [2] | set [1] | invoke [1] | array [1] | map [1] ) 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> array 1



## 状态[148]： 

项目：map_list[0] -> * `LITERAL` [1]  `COLON` [3]  exp [2]  [ `COMMA`  map_list [0] ] 

规则：map_list

----

&emsp;&emsp;&emsp;&emsp;到达状态[156]: map_list[0] ->  `LITERAL` [1]  * `COLON` [3]  exp [2]  [ `COMMA`  map_list [0] ] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 2(2: `LITERAL`，字符串，(null))

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 1



## 状态[149]： 

项目：map[0] ->  `LBR`  [ map_list [0] ]  `RBR`  *

规则：map

----

&emsp;&emsp;&emsp;&emsp;到达状态[76]: type[0] ->  ( `ID` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `INTEGER` [0] | `DECIMAL` [0] | `LITERAL` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `CHARACTER` [0] | `BOOLEAN` [0] | `LPA`  exp [1]  `RPA` | call [1] | lambda [2] | set [1] | invoke [1] | array [1] | map [1] )  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> type[0] -> * ( `ID` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `INTEGER` [0] | `DECIMAL` [0] | `LITERAL` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `CHARACTER` [0] | `BOOLEAN` [0] | `LPA`  exp [1]  `RPA` | call [1] | lambda [2] | set [1] | invoke [1] | array [1] | map [1] ) 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> map 1



## 状态[150]： 

项目：lambda[0] ->  `LAMBDA` [1]  `LPA`  [ var_list [2] ]  `RPA`  ( `PTR_OP`  * scope [3] | block [4] ) 

规则：lambda

----

&emsp;&emsp;&emsp;&emsp;到达状态[129]: scope[0] -> * exp [0] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进  [Action]

&emsp;&emsp;&emsp;&emsp;预查：[57: `INC_OP`，操作符，PLUS_PLUS]&emsp;[4: `INTEGER`，整数，(null)]&emsp;[58: `DEC_OP`，操作符，MINUS_MINUS]&emsp;[1: `BOOLEAN`，布尔，(null)]&emsp;[44: `CALL`，关键字，CALL]&emsp;[3: `CHARACTER`，字符，(null)]&emsp;[100: `LBR`，操作符，LBRACE]&emsp;[86: `NOT`，操作符，BIT_NOT]&emsp;[87: `NOT_OP`，操作符，LOGICAL_NOT]&emsp;[50: `SET`，关键字，SET]&emsp;[5: `DECIMAL`，实数，(null)]&emsp;[98: `LSQ`，操作符，LSQUARE]&emsp;[96: `LPA`，操作符，LPARAN]&emsp;[51: `INVOKE`，关键字，INVOKE]&emsp;[2: `LITERAL`，字符串，(null)]&emsp;[0: `ID`，标识符，(null)]&emsp;[49: `LAMBDA`，关键字，LAMBDA]&emsp;



## 状态[151]： 

项目：exp9[0] -> * ( ( `INC_OP` [3] | `DEC_OP` [3] )  exp9 [1] | exp9 [1]  ( `INC_OP` [3] | `DEC_OP` [3] | `LSQ`  exp [5]  `RSQ` ) | exp10 [0] ) 

规则：exp9

----

&emsp;&emsp;&emsp;&emsp;到达状态[157]: exp10[0] -> * [ exp10 [1]  `DOT` [2] ]  type [0] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[4: `INTEGER`，整数，(null)]&emsp;[1: `BOOLEAN`，布尔，(null)]&emsp;[44: `CALL`，关键字，CALL]&emsp;[3: `CHARACTER`，字符，(null)]&emsp;[100: `LBR`，操作符，LBRACE]&emsp;[50: `SET`，关键字，SET]&emsp;[5: `DECIMAL`，实数，(null)]&emsp;[98: `LSQ`，操作符，LSQUARE]&emsp;[96: `LPA`，操作符，LPARAN]&emsp;[51: `INVOKE`，关键字，INVOKE]&emsp;[2: `LITERAL`，字符串，(null)]&emsp;[0: `ID`，标识符，(null)]&emsp;[49: `LAMBDA`，关键字，LAMBDA]&emsp;

----

&emsp;&emsp;&emsp;&emsp;到达状态[158]: exp9[0] ->  ( ( `INC_OP` [3] | `DEC_OP` [3] )  * exp9 [1] | exp9 [1]  ( `INC_OP` [3] | `DEC_OP` [3] | `LSQ`  exp [5]  `RSQ` ) | exp10 [0] ) 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 57(57: `INC_OP`，操作符，PLUS_PLUS)

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 3

----

&emsp;&emsp;&emsp;&emsp;到达状态[158]: exp9[0] ->  ( ( `INC_OP` [3] | `DEC_OP` [3] )  * exp9 [1] | exp9 [1]  ( `INC_OP` [3] | `DEC_OP` [3] | `LSQ`  exp [5]  `RSQ` ) | exp10 [0] ) 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 58(58: `DEC_OP`，操作符，MINUS_MINUS)

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 3



## 状态[152]： 

项目：exp8[0] ->  ( ( `NOT_OP` [3] | `NOT` [3] )  * exp8 [1] | exp9 [0] ) 

规则：exp8

----

&emsp;&emsp;&emsp;&emsp;到达状态[142]: exp8[0] -> * ( ( `NOT_OP` [3] | `NOT` [3] )  exp8 [1] | exp9 [0] ) 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[57: `INC_OP`，操作符，PLUS_PLUS]&emsp;[4: `INTEGER`，整数，(null)]&emsp;[58: `DEC_OP`，操作符，MINUS_MINUS]&emsp;[1: `BOOLEAN`，布尔，(null)]&emsp;[44: `CALL`，关键字，CALL]&emsp;[3: `CHARACTER`，字符，(null)]&emsp;[100: `LBR`，操作符，LBRACE]&emsp;[86: `NOT`，操作符，BIT_NOT]&emsp;[87: `NOT_OP`，操作符，LOGICAL_NOT]&emsp;[50: `SET`，关键字，SET]&emsp;[5: `DECIMAL`，实数，(null)]&emsp;[98: `LSQ`，操作符，LSQUARE]&emsp;[96: `LPA`，操作符，LPARAN]&emsp;[51: `INVOKE`，关键字，INVOKE]&emsp;[2: `LITERAL`，字符串，(null)]&emsp;[0: `ID`，标识符，(null)]&emsp;[49: `LAMBDA`，关键字，LAMBDA]&emsp;



## 状态[153]： 

项目：exp3[0] ->  [ exp3 [1]  ( `EQ_OP` [2] | `NE_OP` [2] ) ]  exp4 [0]  *

规则：exp3

----

&emsp;&emsp;&emsp;&emsp;到达状态[159]: exp2[0] ->  [ exp2 [1]  ( `OR` [2] | `XOR` [2] | `AND` [2] ) ]  exp3 [0]  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> exp2[0] -> * [ exp2 [1]  ( `OR` [2] | `XOR` [2] | `AND` [2] ) ]  exp3 [0] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp3 0

----

&emsp;&emsp;&emsp;&emsp;到达状态[159]: exp2[0] ->  [ exp2 [1]  ( `OR` [2] | `XOR` [2] | `AND` [2] ) ]  exp3 [0]  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> exp2[0] ->  [ exp2 [1]  ( `OR` [2] | `XOR` [2] | `AND` [2] )  *]  exp3 [0] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp3 0

----

&emsp;&emsp;&emsp;&emsp;到达状态[160]: exp3[0] ->  [ exp3 [1]  * ( `EQ_OP` [2] | `NE_OP` [2] ) ]  exp4 [0] 

&emsp;&emsp;&emsp;&emsp;类型：左递归

&emsp;&emsp;&emsp;&emsp;指令：左递归	=> exp3 1

&emsp;&emsp;&emsp;&emsp;预查：[63: `EQ_OP`，操作符，EQUAL]&emsp;[64: `NE_OP`，操作符，NOT_EQUAL]&emsp;



## 状态[154]： 

项目：exp4[0] ->  [ exp4 [1]  * ( `LT` [2] | `GT` [2] | `LE_OP` [2] | `GE_OP` [2] ) ]  exp5 [0] 

规则：exp4

----

&emsp;&emsp;&emsp;&emsp;到达状态[161]: exp4[0] ->  [ exp4 [1]  ( `LT` [2] | `GT` [2] | `LE_OP` [2] | `GE_OP` [2] )  *]  exp5 [0] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 88(88: `LT`，操作符，LESS_THAN)

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 2

----

&emsp;&emsp;&emsp;&emsp;到达状态[161]: exp4[0] ->  [ exp4 [1]  ( `LT` [2] | `GT` [2] | `LE_OP` [2] | `GE_OP` [2] )  *]  exp5 [0] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 89(89: `GT`，操作符，GREATER_THAN)

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 2

----

&emsp;&emsp;&emsp;&emsp;到达状态[161]: exp4[0] ->  [ exp4 [1]  ( `LT` [2] | `GT` [2] | `LE_OP` [2] | `GE_OP` [2] )  *]  exp5 [0] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 61(61: `LE_OP`，操作符，LESS_THAN_OR_EQUAL)

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 2

----

&emsp;&emsp;&emsp;&emsp;到达状态[161]: exp4[0] ->  [ exp4 [1]  ( `LT` [2] | `GT` [2] | `LE_OP` [2] | `GE_OP` [2] )  *]  exp5 [0] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 62(62: `GE_OP`，操作符，GREATER_THAN_OR_EQUAL)

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 2



## 状态[155]： 

项目：exp5[0] ->  [ exp5 [1]  ( `LEFT_OP` [2] | `RIGHT_OP` [2] )  *]  exp6 [0] 

规则：exp5

----

&emsp;&emsp;&emsp;&emsp;到达状态[113]: exp6[0] -> * [ exp6 [1]  ( `ADD` [2] | `SUB` [2] ) ]  exp7 [0] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[57: `INC_OP`，操作符，PLUS_PLUS]&emsp;[4: `INTEGER`，整数，(null)]&emsp;[58: `DEC_OP`，操作符，MINUS_MINUS]&emsp;[1: `BOOLEAN`，布尔，(null)]&emsp;[44: `CALL`，关键字，CALL]&emsp;[3: `CHARACTER`，字符，(null)]&emsp;[100: `LBR`，操作符，LBRACE]&emsp;[86: `NOT`，操作符，BIT_NOT]&emsp;[87: `NOT_OP`，操作符，LOGICAL_NOT]&emsp;[50: `SET`，关键字，SET]&emsp;[5: `DECIMAL`，实数，(null)]&emsp;[98: `LSQ`，操作符，LSQUARE]&emsp;[96: `LPA`，操作符，LPARAN]&emsp;[51: `INVOKE`，关键字，INVOKE]&emsp;[2: `LITERAL`，字符串，(null)]&emsp;[0: `ID`，标识符，(null)]&emsp;[49: `LAMBDA`，关键字，LAMBDA]&emsp;



## 状态[156]： 

项目：map_list[0] ->  `LITERAL` [1]  * `COLON` [3]  exp [2]  [ `COMMA`  map_list [0] ] 

规则：map_list

----

&emsp;&emsp;&emsp;&emsp;到达状态[162]: map_list[0] ->  `LITERAL` [1]  `COLON` [3]  * exp [2]  [ `COMMA`  map_list [0] ] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 95(95: `COLON`，操作符，COLON)

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 3



## 状态[157]： 

项目：exp10[0] -> * [ exp10 [1]  `DOT` [2] ]  type [0] 

规则：exp10

----

&emsp;&emsp;&emsp;&emsp;到达状态[107]: type[0] -> * ( `ID` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `INTEGER` [0] | `DECIMAL` [0] | `LITERAL` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `CHARACTER` [0] | `BOOLEAN` [0] | `LPA`  exp [1]  `RPA` | call [1] | lambda [2] | set [1] | invoke [1] | array [1] | map [1] ) 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[4: `INTEGER`，整数，(null)]&emsp;[1: `BOOLEAN`，布尔，(null)]&emsp;[44: `CALL`，关键字，CALL]&emsp;[3: `CHARACTER`，字符，(null)]&emsp;[100: `LBR`，操作符，LBRACE]&emsp;[50: `SET`，关键字，SET]&emsp;[5: `DECIMAL`，实数，(null)]&emsp;[98: `LSQ`，操作符，LSQUARE]&emsp;[96: `LPA`，操作符，LPARAN]&emsp;[51: `INVOKE`，关键字，INVOKE]&emsp;[2: `LITERAL`，字符串，(null)]&emsp;[0: `ID`，标识符，(null)]&emsp;[49: `LAMBDA`，关键字，LAMBDA]&emsp;



## 状态[158]： 

项目：exp9[0] ->  ( ( `INC_OP` [3] | `DEC_OP` [3] )  * exp9 [1] | exp9 [1]  ( `INC_OP` [3] | `DEC_OP` [3] | `LSQ`  exp [5]  `RSQ` ) | exp10 [0] ) 

规则：exp9

----

&emsp;&emsp;&emsp;&emsp;到达状态[151]: exp9[0] -> * ( ( `INC_OP` [3] | `DEC_OP` [3] )  exp9 [1] | exp9 [1]  ( `INC_OP` [3] | `DEC_OP` [3] | `LSQ`  exp [5]  `RSQ` ) | exp10 [0] ) 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[57: `INC_OP`，操作符，PLUS_PLUS]&emsp;[4: `INTEGER`，整数，(null)]&emsp;[58: `DEC_OP`，操作符，MINUS_MINUS]&emsp;[1: `BOOLEAN`，布尔，(null)]&emsp;[44: `CALL`，关键字，CALL]&emsp;[3: `CHARACTER`，字符，(null)]&emsp;[100: `LBR`，操作符，LBRACE]&emsp;[50: `SET`，关键字，SET]&emsp;[5: `DECIMAL`，实数，(null)]&emsp;[98: `LSQ`，操作符，LSQUARE]&emsp;[96: `LPA`，操作符，LPARAN]&emsp;[51: `INVOKE`，关键字，INVOKE]&emsp;[2: `LITERAL`，字符串，(null)]&emsp;[0: `ID`，标识符，(null)]&emsp;[49: `LAMBDA`，关键字，LAMBDA]&emsp;



## 状态[159]： 

项目：exp2[0] ->  [ exp2 [1]  ( `OR` [2] | `XOR` [2] | `AND` [2] ) ]  exp3 [0]  *

规则：exp2

----

&emsp;&emsp;&emsp;&emsp;到达状态[163]: exp1[0] ->  [ exp1 [1]  ( `AND_OP` [2] | `OR_OP` [2] ) ]  exp2 [0]  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> exp1[0] -> * [ exp1 [1]  ( `AND_OP` [2] | `OR_OP` [2] ) ]  exp2 [0] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp2 0

----

&emsp;&emsp;&emsp;&emsp;到达状态[163]: exp1[0] ->  [ exp1 [1]  ( `AND_OP` [2] | `OR_OP` [2] ) ]  exp2 [0]  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> exp1[0] ->  [ exp1 [1]  ( `AND_OP` [2] | `OR_OP` [2] )  *]  exp2 [0] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp2 0

----

&emsp;&emsp;&emsp;&emsp;到达状态[164]: exp2[0] ->  [ exp2 [1]  * ( `OR` [2] | `XOR` [2] | `AND` [2] ) ]  exp3 [0] 

&emsp;&emsp;&emsp;&emsp;类型：左递归

&emsp;&emsp;&emsp;&emsp;指令：左递归	=> exp2 1

&emsp;&emsp;&emsp;&emsp;预查：[84: `OR`，操作符，BIT_OR]&emsp;[85: `XOR`，操作符，BIT_XOR]&emsp;[83: `AND`，操作符，BIT_AND]&emsp;



## 状态[160]： 

项目：exp3[0] ->  [ exp3 [1]  * ( `EQ_OP` [2] | `NE_OP` [2] ) ]  exp4 [0] 

规则：exp3

----

&emsp;&emsp;&emsp;&emsp;到达状态[165]: exp3[0] ->  [ exp3 [1]  ( `EQ_OP` [2] | `NE_OP` [2] )  *]  exp4 [0] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 63(63: `EQ_OP`，操作符，EQUAL)

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 2

----

&emsp;&emsp;&emsp;&emsp;到达状态[165]: exp3[0] ->  [ exp3 [1]  ( `EQ_OP` [2] | `NE_OP` [2] )  *]  exp4 [0] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 64(64: `NE_OP`，操作符，NOT_EQUAL)

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 2



## 状态[161]： 

项目：exp4[0] ->  [ exp4 [1]  ( `LT` [2] | `GT` [2] | `LE_OP` [2] | `GE_OP` [2] )  *]  exp5 [0] 

规则：exp4

----

&emsp;&emsp;&emsp;&emsp;到达状态[103]: exp5[0] -> * [ exp5 [1]  ( `LEFT_OP` [2] | `RIGHT_OP` [2] ) ]  exp6 [0] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[57: `INC_OP`，操作符，PLUS_PLUS]&emsp;[4: `INTEGER`，整数，(null)]&emsp;[58: `DEC_OP`，操作符，MINUS_MINUS]&emsp;[1: `BOOLEAN`，布尔，(null)]&emsp;[44: `CALL`，关键字，CALL]&emsp;[3: `CHARACTER`，字符，(null)]&emsp;[100: `LBR`，操作符，LBRACE]&emsp;[86: `NOT`，操作符，BIT_NOT]&emsp;[87: `NOT_OP`，操作符，LOGICAL_NOT]&emsp;[50: `SET`，关键字，SET]&emsp;[5: `DECIMAL`，实数，(null)]&emsp;[98: `LSQ`，操作符，LSQUARE]&emsp;[96: `LPA`，操作符，LPARAN]&emsp;[51: `INVOKE`，关键字，INVOKE]&emsp;[2: `LITERAL`，字符串，(null)]&emsp;[0: `ID`，标识符，(null)]&emsp;[49: `LAMBDA`，关键字，LAMBDA]&emsp;



## 状态[162]： 

项目：map_list[0] ->  `LITERAL` [1]  `COLON` [3]  * exp [2]  [ `COMMA`  map_list [0] ] 

规则：map_list

----

&emsp;&emsp;&emsp;&emsp;到达状态[20]: exp[0] -> * exp01 [0]  [ ( `INTEGER` [10] | `DECIMAL` [10] ) ] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[57: `INC_OP`，操作符，PLUS_PLUS]&emsp;[4: `INTEGER`，整数，(null)]&emsp;[58: `DEC_OP`，操作符，MINUS_MINUS]&emsp;[1: `BOOLEAN`，布尔，(null)]&emsp;[44: `CALL`，关键字，CALL]&emsp;[3: `CHARACTER`，字符，(null)]&emsp;[100: `LBR`，操作符，LBRACE]&emsp;[86: `NOT`，操作符，BIT_NOT]&emsp;[87: `NOT_OP`，操作符，LOGICAL_NOT]&emsp;[50: `SET`，关键字，SET]&emsp;[5: `DECIMAL`，实数，(null)]&emsp;[98: `LSQ`，操作符，LSQUARE]&emsp;[96: `LPA`，操作符，LPARAN]&emsp;[51: `INVOKE`，关键字，INVOKE]&emsp;[2: `LITERAL`，字符串，(null)]&emsp;[0: `ID`，标识符，(null)]&emsp;[49: `LAMBDA`，关键字，LAMBDA]&emsp;



## 状态[163]： 

项目：exp1[0] ->  [ exp1 [1]  ( `AND_OP` [2] | `OR_OP` [2] ) ]  exp2 [0]  *

规则：exp1

----

&emsp;&emsp;&emsp;&emsp;到达状态[166]: exp0[0] ->  exp1 [0]  * [ `QUERY` [4]  exp0 [6]  `COLON` [5]  exp0 [7] ] 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> exp0[0] -> * exp1 [0]  [ `QUERY` [4]  exp0 [6]  `COLON` [5]  exp0 [7] ] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp1 0

----

&emsp;&emsp;&emsp;&emsp;到达状态[167]: exp1[0] ->  [ exp1 [1]  * ( `AND_OP` [2] | `OR_OP` [2] ) ]  exp2 [0] 

&emsp;&emsp;&emsp;&emsp;类型：左递归

&emsp;&emsp;&emsp;&emsp;指令：左递归	=> exp1 1

&emsp;&emsp;&emsp;&emsp;预查：[65: `AND_OP`，操作符，LOGICAL_AND]&emsp;[66: `OR_OP`，操作符，LOGICAL_OR]&emsp;



## 状态[164]： 

项目：exp2[0] ->  [ exp2 [1]  * ( `OR` [2] | `XOR` [2] | `AND` [2] ) ]  exp3 [0] 

规则：exp2

----

&emsp;&emsp;&emsp;&emsp;到达状态[168]: exp2[0] ->  [ exp2 [1]  ( `OR` [2] | `XOR` [2] | `AND` [2] )  *]  exp3 [0] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 84(84: `OR`，操作符，BIT_OR)

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 2

----

&emsp;&emsp;&emsp;&emsp;到达状态[168]: exp2[0] ->  [ exp2 [1]  ( `OR` [2] | `XOR` [2] | `AND` [2] )  *]  exp3 [0] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 85(85: `XOR`，操作符，BIT_XOR)

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 2

----

&emsp;&emsp;&emsp;&emsp;到达状态[168]: exp2[0] ->  [ exp2 [1]  ( `OR` [2] | `XOR` [2] | `AND` [2] )  *]  exp3 [0] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 83(83: `AND`，操作符，BIT_AND)

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 2



## 状态[165]： 

项目：exp3[0] ->  [ exp3 [1]  ( `EQ_OP` [2] | `NE_OP` [2] )  *]  exp4 [0] 

规则：exp3

----

&emsp;&emsp;&emsp;&emsp;到达状态[95]: exp4[0] -> * [ exp4 [1]  ( `LT` [2] | `GT` [2] | `LE_OP` [2] | `GE_OP` [2] ) ]  exp5 [0] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[57: `INC_OP`，操作符，PLUS_PLUS]&emsp;[4: `INTEGER`，整数，(null)]&emsp;[58: `DEC_OP`，操作符，MINUS_MINUS]&emsp;[1: `BOOLEAN`，布尔，(null)]&emsp;[44: `CALL`，关键字，CALL]&emsp;[3: `CHARACTER`，字符，(null)]&emsp;[100: `LBR`，操作符，LBRACE]&emsp;[86: `NOT`，操作符，BIT_NOT]&emsp;[87: `NOT_OP`，操作符，LOGICAL_NOT]&emsp;[50: `SET`，关键字，SET]&emsp;[5: `DECIMAL`，实数，(null)]&emsp;[98: `LSQ`，操作符，LSQUARE]&emsp;[96: `LPA`，操作符，LPARAN]&emsp;[51: `INVOKE`，关键字，INVOKE]&emsp;[2: `LITERAL`，字符串，(null)]&emsp;[0: `ID`，标识符，(null)]&emsp;[49: `LAMBDA`，关键字，LAMBDA]&emsp;



## 状态[166]： 

项目：exp0[0] ->  exp1 [0]  * [ `QUERY` [4]  exp0 [6]  `COLON` [5]  exp0 [7] ] 

规则：exp0

----

&emsp;&emsp;&emsp;&emsp;到达状态[169]: exp0[0] ->  exp1 [0]  [ `QUERY` [4]  * exp0 [6]  `COLON` [5]  exp0 [7] ] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 90(90: `QUERY`，操作符，QUERY)

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 4

----

&emsp;&emsp;&emsp;&emsp;到达状态[170]: exp0[0] ->  exp1 [0]  [ `QUERY` [4]  exp0 [6]  * `COLON` [5]  exp0 [7] ] 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> exp0[0] ->  exp1 [0]  [ `QUERY` [4]  * exp0 [6]  `COLON` [5]  exp0 [7] ] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp0 6

----

&emsp;&emsp;&emsp;&emsp;到达状态[171]: exp0[0] ->  exp1 [0]  [ `QUERY` [4]  exp0 [6]  `COLON` [5]  exp0 [7] ]  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> exp0[0] ->  exp1 [0]  [ `QUERY` [4]  exp0 [6]  `COLON` [5]  * exp0 [7] ] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp0 7

----

&emsp;&emsp;&emsp;&emsp;到达状态[172]: exp01[0] ->  [ exp01 [1]  ( `EQ_ASSIGN` [2] | `ADD_ASSIGN` [2] | `SUB_ASSIGN` [2] | `MUL_ASSIGN` [2] | `DIV_ASSIGN` [2] | `AND_ASSIGN` [2] | `OR_ASSIGN` [2] | `XOR_ASSIGN` [2] | `MOD_ASSIGN` [2] ) ]  exp0 [0]  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> exp01[0] -> * [ exp01 [1]  ( `EQ_ASSIGN` [2] | `ADD_ASSIGN` [2] | `SUB_ASSIGN` [2] | `MUL_ASSIGN` [2] | `DIV_ASSIGN` [2] | `AND_ASSIGN` [2] | `OR_ASSIGN` [2] | `XOR_ASSIGN` [2] | `MOD_ASSIGN` [2] ) ]  exp0 [0] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp0 0

----

&emsp;&emsp;&emsp;&emsp;到达状态[172]: exp01[0] ->  [ exp01 [1]  ( `EQ_ASSIGN` [2] | `ADD_ASSIGN` [2] | `SUB_ASSIGN` [2] | `MUL_ASSIGN` [2] | `DIV_ASSIGN` [2] | `AND_ASSIGN` [2] | `OR_ASSIGN` [2] | `XOR_ASSIGN` [2] | `MOD_ASSIGN` [2] ) ]  exp0 [0]  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> exp01[0] ->  [ exp01 [1]  ( `EQ_ASSIGN` [2] | `ADD_ASSIGN` [2] | `SUB_ASSIGN` [2] | `MUL_ASSIGN` [2] | `DIV_ASSIGN` [2] | `AND_ASSIGN` [2] | `OR_ASSIGN` [2] | `XOR_ASSIGN` [2] | `MOD_ASSIGN` [2] )  *]  exp0 [0] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp0 0



## 状态[167]： 

项目：exp1[0] ->  [ exp1 [1]  * ( `AND_OP` [2] | `OR_OP` [2] ) ]  exp2 [0] 

规则：exp1

----

&emsp;&emsp;&emsp;&emsp;到达状态[173]: exp1[0] ->  [ exp1 [1]  ( `AND_OP` [2] | `OR_OP` [2] )  *]  exp2 [0] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 65(65: `AND_OP`，操作符，LOGICAL_AND)

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 2

----

&emsp;&emsp;&emsp;&emsp;到达状态[173]: exp1[0] ->  [ exp1 [1]  ( `AND_OP` [2] | `OR_OP` [2] )  *]  exp2 [0] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 66(66: `OR_OP`，操作符，LOGICAL_OR)

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 2



## 状态[168]： 

项目：exp2[0] ->  [ exp2 [1]  ( `OR` [2] | `XOR` [2] | `AND` [2] )  *]  exp3 [0] 

规则：exp2

----

&emsp;&emsp;&emsp;&emsp;到达状态[89]: exp3[0] -> * [ exp3 [1]  ( `EQ_OP` [2] | `NE_OP` [2] ) ]  exp4 [0] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[57: `INC_OP`，操作符，PLUS_PLUS]&emsp;[4: `INTEGER`，整数，(null)]&emsp;[58: `DEC_OP`，操作符，MINUS_MINUS]&emsp;[1: `BOOLEAN`，布尔，(null)]&emsp;[44: `CALL`，关键字，CALL]&emsp;[3: `CHARACTER`，字符，(null)]&emsp;[100: `LBR`，操作符，LBRACE]&emsp;[86: `NOT`，操作符，BIT_NOT]&emsp;[87: `NOT_OP`，操作符，LOGICAL_NOT]&emsp;[50: `SET`，关键字，SET]&emsp;[5: `DECIMAL`，实数，(null)]&emsp;[98: `LSQ`，操作符，LSQUARE]&emsp;[96: `LPA`，操作符，LPARAN]&emsp;[51: `INVOKE`，关键字，INVOKE]&emsp;[2: `LITERAL`，字符串，(null)]&emsp;[0: `ID`，标识符，(null)]&emsp;[49: `LAMBDA`，关键字，LAMBDA]&emsp;



## 状态[169]： 

项目：exp0[0] ->  exp1 [0]  [ `QUERY` [4]  * exp0 [6]  `COLON` [5]  exp0 [7] ] 

规则：exp0

----

&emsp;&emsp;&emsp;&emsp;到达状态[47]: exp0[0] -> * exp1 [0]  [ `QUERY` [4]  exp0 [6]  `COLON` [5]  exp0 [7] ] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[57: `INC_OP`，操作符，PLUS_PLUS]&emsp;[4: `INTEGER`，整数，(null)]&emsp;[58: `DEC_OP`，操作符，MINUS_MINUS]&emsp;[1: `BOOLEAN`，布尔，(null)]&emsp;[44: `CALL`，关键字，CALL]&emsp;[3: `CHARACTER`，字符，(null)]&emsp;[100: `LBR`，操作符，LBRACE]&emsp;[86: `NOT`，操作符，BIT_NOT]&emsp;[87: `NOT_OP`，操作符，LOGICAL_NOT]&emsp;[50: `SET`，关键字，SET]&emsp;[5: `DECIMAL`，实数，(null)]&emsp;[98: `LSQ`，操作符，LSQUARE]&emsp;[96: `LPA`，操作符，LPARAN]&emsp;[51: `INVOKE`，关键字，INVOKE]&emsp;[2: `LITERAL`，字符串，(null)]&emsp;[0: `ID`，标识符，(null)]&emsp;[49: `LAMBDA`，关键字，LAMBDA]&emsp;



## 状态[170]： 

项目：exp0[0] ->  exp1 [0]  [ `QUERY` [4]  exp0 [6]  * `COLON` [5]  exp0 [7] ] 

规则：exp0

----

&emsp;&emsp;&emsp;&emsp;到达状态[174]: exp0[0] ->  exp1 [0]  [ `QUERY` [4]  exp0 [6]  `COLON` [5]  * exp0 [7] ] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 95(95: `COLON`，操作符，COLON)

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 5



## 状态[171]： 

项目：exp0[0] ->  exp1 [0]  [ `QUERY` [4]  exp0 [6]  `COLON` [5]  exp0 [7] ]  *

规则：exp0

----

&emsp;&emsp;&emsp;&emsp;到达状态[170]: exp0[0] ->  exp1 [0]  [ `QUERY` [4]  exp0 [6]  * `COLON` [5]  exp0 [7] ] 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> exp0[0] ->  exp1 [0]  [ `QUERY` [4]  * exp0 [6]  `COLON` [5]  exp0 [7] ] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp0 6

----

&emsp;&emsp;&emsp;&emsp;到达状态[171]: exp0[0] ->  exp1 [0]  [ `QUERY` [4]  exp0 [6]  `COLON` [5]  exp0 [7] ]  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> exp0[0] ->  exp1 [0]  [ `QUERY` [4]  exp0 [6]  `COLON` [5]  * exp0 [7] ] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp0 7

----

&emsp;&emsp;&emsp;&emsp;到达状态[172]: exp01[0] ->  [ exp01 [1]  ( `EQ_ASSIGN` [2] | `ADD_ASSIGN` [2] | `SUB_ASSIGN` [2] | `MUL_ASSIGN` [2] | `DIV_ASSIGN` [2] | `AND_ASSIGN` [2] | `OR_ASSIGN` [2] | `XOR_ASSIGN` [2] | `MOD_ASSIGN` [2] ) ]  exp0 [0]  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> exp01[0] -> * [ exp01 [1]  ( `EQ_ASSIGN` [2] | `ADD_ASSIGN` [2] | `SUB_ASSIGN` [2] | `MUL_ASSIGN` [2] | `DIV_ASSIGN` [2] | `AND_ASSIGN` [2] | `OR_ASSIGN` [2] | `XOR_ASSIGN` [2] | `MOD_ASSIGN` [2] ) ]  exp0 [0] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp0 0

----

&emsp;&emsp;&emsp;&emsp;到达状态[172]: exp01[0] ->  [ exp01 [1]  ( `EQ_ASSIGN` [2] | `ADD_ASSIGN` [2] | `SUB_ASSIGN` [2] | `MUL_ASSIGN` [2] | `DIV_ASSIGN` [2] | `AND_ASSIGN` [2] | `OR_ASSIGN` [2] | `XOR_ASSIGN` [2] | `MOD_ASSIGN` [2] ) ]  exp0 [0]  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> exp01[0] ->  [ exp01 [1]  ( `EQ_ASSIGN` [2] | `ADD_ASSIGN` [2] | `SUB_ASSIGN` [2] | `MUL_ASSIGN` [2] | `DIV_ASSIGN` [2] | `AND_ASSIGN` [2] | `OR_ASSIGN` [2] | `XOR_ASSIGN` [2] | `MOD_ASSIGN` [2] )  *]  exp0 [0] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp0 0



## 状态[172]： 

项目：exp01[0] ->  [ exp01 [1]  ( `EQ_ASSIGN` [2] | `ADD_ASSIGN` [2] | `SUB_ASSIGN` [2] | `MUL_ASSIGN` [2] | `DIV_ASSIGN` [2] | `AND_ASSIGN` [2] | `OR_ASSIGN` [2] | `XOR_ASSIGN` [2] | `MOD_ASSIGN` [2] ) ]  exp0 [0]  *

规则：exp01

----

&emsp;&emsp;&emsp;&emsp;到达状态[175]: exp[0] ->  exp01 [0]  * [ ( `INTEGER` [10] | `DECIMAL` [10] ) ] 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> exp[0] -> * exp01 [0]  [ ( `INTEGER` [10] | `DECIMAL` [10] ) ] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp01 0

----

&emsp;&emsp;&emsp;&emsp;到达状态[176]: exp01[0] ->  [ exp01 [1]  * ( `EQ_ASSIGN` [2] | `ADD_ASSIGN` [2] | `SUB_ASSIGN` [2] | `MUL_ASSIGN` [2] | `DIV_ASSIGN` [2] | `AND_ASSIGN` [2] | `OR_ASSIGN` [2] | `XOR_ASSIGN` [2] | `MOD_ASSIGN` [2] ) ]  exp0 [0] 

&emsp;&emsp;&emsp;&emsp;类型：左递归

&emsp;&emsp;&emsp;&emsp;指令：左递归	=> exp01 1

&emsp;&emsp;&emsp;&emsp;预查：[77: `EQ_ASSIGN`，操作符，EQ_ASSIGN]&emsp;[70: `ADD_ASSIGN`，操作符，PLUS_ASSIGN]&emsp;[71: `SUB_ASSIGN`，操作符，MINUS_ASSIGN]&emsp;[67: `MUL_ASSIGN`，操作符，TIMES_ASSIGN]&emsp;[68: `DIV_ASSIGN`，操作符，DIV_ASSIGN]&emsp;[74: `AND_ASSIGN`，操作符，AND_ASSIGN]&emsp;[76: `OR_ASSIGN`，操作符，OR_ASSIGN]&emsp;[75: `XOR_ASSIGN`，操作符，XOR_ASSIGN]&emsp;[69: `MOD_ASSIGN`，操作符，MOD_ASSIGN]&emsp;



## 状态[173]： 

项目：exp1[0] ->  [ exp1 [1]  ( `AND_OP` [2] | `OR_OP` [2] )  *]  exp2 [0] 

规则：exp1

----

&emsp;&emsp;&emsp;&emsp;到达状态[83]: exp2[0] -> * [ exp2 [1]  ( `OR` [2] | `XOR` [2] | `AND` [2] ) ]  exp3 [0] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[57: `INC_OP`，操作符，PLUS_PLUS]&emsp;[4: `INTEGER`，整数，(null)]&emsp;[58: `DEC_OP`，操作符，MINUS_MINUS]&emsp;[1: `BOOLEAN`，布尔，(null)]&emsp;[44: `CALL`，关键字，CALL]&emsp;[3: `CHARACTER`，字符，(null)]&emsp;[100: `LBR`，操作符，LBRACE]&emsp;[86: `NOT`，操作符，BIT_NOT]&emsp;[87: `NOT_OP`，操作符，LOGICAL_NOT]&emsp;[50: `SET`，关键字，SET]&emsp;[5: `DECIMAL`，实数，(null)]&emsp;[98: `LSQ`，操作符，LSQUARE]&emsp;[96: `LPA`，操作符，LPARAN]&emsp;[51: `INVOKE`，关键字，INVOKE]&emsp;[2: `LITERAL`，字符串，(null)]&emsp;[0: `ID`，标识符，(null)]&emsp;[49: `LAMBDA`，关键字，LAMBDA]&emsp;



## 状态[174]： 

项目：exp0[0] ->  exp1 [0]  [ `QUERY` [4]  exp0 [6]  `COLON` [5]  * exp0 [7] ] 

规则：exp0

----

&emsp;&emsp;&emsp;&emsp;到达状态[47]: exp0[0] -> * exp1 [0]  [ `QUERY` [4]  exp0 [6]  `COLON` [5]  exp0 [7] ] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[57: `INC_OP`，操作符，PLUS_PLUS]&emsp;[4: `INTEGER`，整数，(null)]&emsp;[58: `DEC_OP`，操作符，MINUS_MINUS]&emsp;[1: `BOOLEAN`，布尔，(null)]&emsp;[44: `CALL`，关键字，CALL]&emsp;[3: `CHARACTER`，字符，(null)]&emsp;[100: `LBR`，操作符，LBRACE]&emsp;[86: `NOT`，操作符，BIT_NOT]&emsp;[87: `NOT_OP`，操作符，LOGICAL_NOT]&emsp;[50: `SET`，关键字，SET]&emsp;[5: `DECIMAL`，实数，(null)]&emsp;[98: `LSQ`，操作符，LSQUARE]&emsp;[96: `LPA`，操作符，LPARAN]&emsp;[51: `INVOKE`，关键字，INVOKE]&emsp;[2: `LITERAL`，字符串，(null)]&emsp;[0: `ID`，标识符，(null)]&emsp;[49: `LAMBDA`，关键字，LAMBDA]&emsp;



## 状态[175]： 

项目：exp[0] ->  exp01 [0]  * [ ( `INTEGER` [10] | `DECIMAL` [10] ) ] 

规则：exp

----

&emsp;&emsp;&emsp;&emsp;到达状态[177]: exp[0] ->  exp01 [0]  [ ( `INTEGER` [10] | `DECIMAL` [10] )  *] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 4(4: `INTEGER`，整数，(null))

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 10

----

&emsp;&emsp;&emsp;&emsp;到达状态[177]: exp[0] ->  exp01 [0]  [ ( `INTEGER` [10] | `DECIMAL` [10] )  *] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 5(5: `DECIMAL`，实数，(null))

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 10

----

&emsp;&emsp;&emsp;&emsp;到达状态[46]: stmt_exp[0] ->  ( var [0] | call [0] | cycle_ctrl [0] | exp [0] )  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> stmt_exp[0] -> * ( var [0] | call [0] | cycle_ctrl [0] | exp [0] ) 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp 0

----

&emsp;&emsp;&emsp;&emsp;到达状态[74]: var[0] ->  ( `VARIABLE` [11] | `LET` [12] )  `ID` [0]  [ `ASSIGN`  ( func [1] | exp [2] )  *] 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> var[0] ->  ( `VARIABLE` [11] | `LET` [12] )  `ID` [0]  [ `ASSIGN`  * ( func [1] | exp [2] ) ] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp 2

----

&emsp;&emsp;&emsp;&emsp;到达状态[178]: exp_list[0] ->  exp [0]  * [ `COMMA`  exp_list [1] ] 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> exp_list[0] -> * exp [0]  [ `COMMA`  exp_list [1] ] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp 0

----

&emsp;&emsp;&emsp;&emsp;到达状态[179]: exp9[0] ->  ( ( `INC_OP` [3] | `DEC_OP` [3] )  exp9 [1] | exp9 [1]  ( `INC_OP` [3] | `DEC_OP` [3] | `LSQ`  exp [5]  * `RSQ` ) | exp10 [0] ) 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> exp9[0] ->  ( ( `INC_OP` [3] | `DEC_OP` [3] )  exp9 [1] | exp9 [1]  ( `INC_OP` [3] | `DEC_OP` [3] | `LSQ`  * exp [5]  `RSQ` ) | exp10 [0] ) 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp 5

----

&emsp;&emsp;&emsp;&emsp;到达状态[180]: type[0] ->  ( `ID` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `INTEGER` [0] | `DECIMAL` [0] | `LITERAL` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `CHARACTER` [0] | `BOOLEAN` [0] | `LPA`  exp [1]  * `RPA` | call [1] | lambda [2] | set [1] | invoke [1] | array [1] | map [1] ) 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> type[0] ->  ( `ID` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `INTEGER` [0] | `DECIMAL` [0] | `LITERAL` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `CHARACTER` [0] | `BOOLEAN` [0] | `LPA`  * exp [1]  `RPA` | call [1] | lambda [2] | set [1] | invoke [1] | array [1] | map [1] ) 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp 1

----

&emsp;&emsp;&emsp;&emsp;到达状态[75]: call[0] ->  `CALL` [4]  ( `LPA`  ( func [0] | exp [3] )  * `RPA` | `ID` [1] )  `LPA`  [ exp_list [2] ]  `RPA` 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> call[0] ->  `CALL` [4]  ( `LPA`  * ( func [0] | exp [3] )  `RPA` | `ID` [1] )  `LPA`  [ exp_list [2] ]  `RPA` 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp 3

----

&emsp;&emsp;&emsp;&emsp;到达状态[181]: ret[0] ->  ( `YIELD` [1] | `RETURN` )  [ exp [0] ]  * `SEMI` 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> ret[0] ->  ( `YIELD` [1] | `RETURN` )  * [ exp [0] ]  `SEMI` 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp 0

----

&emsp;&emsp;&emsp;&emsp;到达状态[182]: if[0] ->  `IF`  `LPA`  exp [0]  * `RPA`  block [1]  [ `ELSE`  ( block [2] | if [3] ) ] 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> if[0] ->  `IF`  `LPA`  * exp [0]  `RPA`  block [1]  [ `ELSE`  ( block [2] | if [3] ) ] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp 0

----

&emsp;&emsp;&emsp;&emsp;到达状态[183]: for[0] ->  `FOR`  `LPA`  [ var [0] ]  `SEMI`  [ exp [1] ]  * `SEMI`  [ ( exp [2] | var [2] ) ]  `RPA`  block [3] 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> for[0] ->  `FOR`  `LPA`  [ var [0] ]  `SEMI`  * [ exp [1] ]  `SEMI`  [ ( exp [2] | var [2] ) ]  `RPA`  block [3] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp 1

----

&emsp;&emsp;&emsp;&emsp;到达状态[67]: for[0] ->  `FOR`  `LPA`  [ var [0] ]  `SEMI`  [ exp [1] ]  `SEMI`  [ ( exp [2] | var [2] )  *]  `RPA`  block [3] 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> for[0] ->  `FOR`  `LPA`  [ var [0] ]  `SEMI`  [ exp [1] ]  `SEMI`  * [ ( exp [2] | var [2] ) ]  `RPA`  block [3] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp 2

----

&emsp;&emsp;&emsp;&emsp;到达状态[184]: while[0] ->  `WHILE`  `LPA`  exp [0]  * `RPA`  block [1] 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> while[0] ->  `WHILE`  `LPA`  * exp [0]  `RPA`  block [1] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp 0

----

&emsp;&emsp;&emsp;&emsp;到达状态[185]: foreach[0] ->  `FOREACH`  `LPA`  `VARIABLE`  `ID` [0]  `COLON`  exp [1]  * `RPA`  block [2] 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> foreach[0] ->  `FOREACH`  `LPA`  `VARIABLE`  `ID` [0]  `COLON`  * exp [1]  `RPA`  block [2] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp 1

----

&emsp;&emsp;&emsp;&emsp;到达状态[186]: set[0] ->  `SET`  exp [3]  * `PROPERTY`  exp [4]  `ASSIGN`  exp [2] 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> set[0] ->  `SET`  * exp [3]  `PROPERTY`  exp [4]  `ASSIGN`  exp [2] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp 3

----

&emsp;&emsp;&emsp;&emsp;到达状态[187]: set[0] ->  `SET`  exp [3]  `PROPERTY`  exp [4]  * `ASSIGN`  exp [2] 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> set[0] ->  `SET`  exp [3]  `PROPERTY`  * exp [4]  `ASSIGN`  exp [2] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp 4

----

&emsp;&emsp;&emsp;&emsp;到达状态[188]: set[0] ->  `SET`  exp [3]  `PROPERTY`  exp [4]  `ASSIGN`  exp [2]  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> set[0] ->  `SET`  exp [3]  `PROPERTY`  exp [4]  `ASSIGN`  * exp [2] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp 2

----

&emsp;&emsp;&emsp;&emsp;到达状态[189]: invoke[0] ->  `INVOKE` [0]  exp [1]  * `PROPERTY`  exp [2]  `PROPERTY`  `LPA`  [ exp_list [3] ]  `RPA` 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> invoke[0] ->  `INVOKE` [0]  * exp [1]  `PROPERTY`  exp [2]  `PROPERTY`  `LPA`  [ exp_list [3] ]  `RPA` 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp 1

----

&emsp;&emsp;&emsp;&emsp;到达状态[190]: invoke[0] ->  `INVOKE` [0]  exp [1]  `PROPERTY`  exp [2]  * `PROPERTY`  `LPA`  [ exp_list [3] ]  `RPA` 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> invoke[0] ->  `INVOKE` [0]  exp [1]  `PROPERTY`  * exp [2]  `PROPERTY`  `LPA`  [ exp_list [3] ]  `RPA` 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp 2

----

&emsp;&emsp;&emsp;&emsp;到达状态[191]: throw[0] ->  `THROW`  exp [0]  * `SEMI` 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> throw[0] ->  `THROW`  * exp [0]  `SEMI` 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp 0

----

&emsp;&emsp;&emsp;&emsp;到达状态[192]: map_list[0] ->  `LITERAL` [1]  `COLON` [3]  exp [2]  * [ `COMMA`  map_list [0] ] 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> map_list[0] ->  `LITERAL` [1]  `COLON` [3]  * exp [2]  [ `COMMA`  map_list [0] ] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp 2

----

&emsp;&emsp;&emsp;&emsp;到达状态[193]: scope[0] ->  exp [0]  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> scope[0] -> * exp [0] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp 0



## 状态[176]： 

项目：exp01[0] ->  [ exp01 [1]  * ( `EQ_ASSIGN` [2] | `ADD_ASSIGN` [2] | `SUB_ASSIGN` [2] | `MUL_ASSIGN` [2] | `DIV_ASSIGN` [2] | `AND_ASSIGN` [2] | `OR_ASSIGN` [2] | `XOR_ASSIGN` [2] | `MOD_ASSIGN` [2] ) ]  exp0 [0] 

规则：exp01

----

&emsp;&emsp;&emsp;&emsp;到达状态[194]: exp01[0] ->  [ exp01 [1]  ( `EQ_ASSIGN` [2] | `ADD_ASSIGN` [2] | `SUB_ASSIGN` [2] | `MUL_ASSIGN` [2] | `DIV_ASSIGN` [2] | `AND_ASSIGN` [2] | `OR_ASSIGN` [2] | `XOR_ASSIGN` [2] | `MOD_ASSIGN` [2] )  *]  exp0 [0] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 77(77: `EQ_ASSIGN`，操作符，EQ_ASSIGN)

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 2

----

&emsp;&emsp;&emsp;&emsp;到达状态[194]: exp01[0] ->  [ exp01 [1]  ( `EQ_ASSIGN` [2] | `ADD_ASSIGN` [2] | `SUB_ASSIGN` [2] | `MUL_ASSIGN` [2] | `DIV_ASSIGN` [2] | `AND_ASSIGN` [2] | `OR_ASSIGN` [2] | `XOR_ASSIGN` [2] | `MOD_ASSIGN` [2] )  *]  exp0 [0] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 70(70: `ADD_ASSIGN`，操作符，PLUS_ASSIGN)

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 2

----

&emsp;&emsp;&emsp;&emsp;到达状态[194]: exp01[0] ->  [ exp01 [1]  ( `EQ_ASSIGN` [2] | `ADD_ASSIGN` [2] | `SUB_ASSIGN` [2] | `MUL_ASSIGN` [2] | `DIV_ASSIGN` [2] | `AND_ASSIGN` [2] | `OR_ASSIGN` [2] | `XOR_ASSIGN` [2] | `MOD_ASSIGN` [2] )  *]  exp0 [0] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 71(71: `SUB_ASSIGN`，操作符，MINUS_ASSIGN)

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 2

----

&emsp;&emsp;&emsp;&emsp;到达状态[194]: exp01[0] ->  [ exp01 [1]  ( `EQ_ASSIGN` [2] | `ADD_ASSIGN` [2] | `SUB_ASSIGN` [2] | `MUL_ASSIGN` [2] | `DIV_ASSIGN` [2] | `AND_ASSIGN` [2] | `OR_ASSIGN` [2] | `XOR_ASSIGN` [2] | `MOD_ASSIGN` [2] )  *]  exp0 [0] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 67(67: `MUL_ASSIGN`，操作符，TIMES_ASSIGN)

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 2

----

&emsp;&emsp;&emsp;&emsp;到达状态[194]: exp01[0] ->  [ exp01 [1]  ( `EQ_ASSIGN` [2] | `ADD_ASSIGN` [2] | `SUB_ASSIGN` [2] | `MUL_ASSIGN` [2] | `DIV_ASSIGN` [2] | `AND_ASSIGN` [2] | `OR_ASSIGN` [2] | `XOR_ASSIGN` [2] | `MOD_ASSIGN` [2] )  *]  exp0 [0] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 68(68: `DIV_ASSIGN`，操作符，DIV_ASSIGN)

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 2

----

&emsp;&emsp;&emsp;&emsp;到达状态[194]: exp01[0] ->  [ exp01 [1]  ( `EQ_ASSIGN` [2] | `ADD_ASSIGN` [2] | `SUB_ASSIGN` [2] | `MUL_ASSIGN` [2] | `DIV_ASSIGN` [2] | `AND_ASSIGN` [2] | `OR_ASSIGN` [2] | `XOR_ASSIGN` [2] | `MOD_ASSIGN` [2] )  *]  exp0 [0] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 74(74: `AND_ASSIGN`，操作符，AND_ASSIGN)

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 2

----

&emsp;&emsp;&emsp;&emsp;到达状态[194]: exp01[0] ->  [ exp01 [1]  ( `EQ_ASSIGN` [2] | `ADD_ASSIGN` [2] | `SUB_ASSIGN` [2] | `MUL_ASSIGN` [2] | `DIV_ASSIGN` [2] | `AND_ASSIGN` [2] | `OR_ASSIGN` [2] | `XOR_ASSIGN` [2] | `MOD_ASSIGN` [2] )  *]  exp0 [0] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 76(76: `OR_ASSIGN`，操作符，OR_ASSIGN)

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 2

----

&emsp;&emsp;&emsp;&emsp;到达状态[194]: exp01[0] ->  [ exp01 [1]  ( `EQ_ASSIGN` [2] | `ADD_ASSIGN` [2] | `SUB_ASSIGN` [2] | `MUL_ASSIGN` [2] | `DIV_ASSIGN` [2] | `AND_ASSIGN` [2] | `OR_ASSIGN` [2] | `XOR_ASSIGN` [2] | `MOD_ASSIGN` [2] )  *]  exp0 [0] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 75(75: `XOR_ASSIGN`，操作符，XOR_ASSIGN)

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 2

----

&emsp;&emsp;&emsp;&emsp;到达状态[194]: exp01[0] ->  [ exp01 [1]  ( `EQ_ASSIGN` [2] | `ADD_ASSIGN` [2] | `SUB_ASSIGN` [2] | `MUL_ASSIGN` [2] | `DIV_ASSIGN` [2] | `AND_ASSIGN` [2] | `OR_ASSIGN` [2] | `XOR_ASSIGN` [2] | `MOD_ASSIGN` [2] )  *]  exp0 [0] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 69(69: `MOD_ASSIGN`，操作符，MOD_ASSIGN)

&emsp;&emsp;&emsp;&emsp;指令：读入	=> 2



## 状态[177]： 

项目：exp[0] ->  exp01 [0]  [ ( `INTEGER` [10] | `DECIMAL` [10] )  *] 

规则：exp

----

&emsp;&emsp;&emsp;&emsp;到达状态[46]: stmt_exp[0] ->  ( var [0] | call [0] | cycle_ctrl [0] | exp [0] )  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> stmt_exp[0] -> * ( var [0] | call [0] | cycle_ctrl [0] | exp [0] ) 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp 0

----

&emsp;&emsp;&emsp;&emsp;到达状态[74]: var[0] ->  ( `VARIABLE` [11] | `LET` [12] )  `ID` [0]  [ `ASSIGN`  ( func [1] | exp [2] )  *] 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> var[0] ->  ( `VARIABLE` [11] | `LET` [12] )  `ID` [0]  [ `ASSIGN`  * ( func [1] | exp [2] ) ] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp 2

----

&emsp;&emsp;&emsp;&emsp;到达状态[178]: exp_list[0] ->  exp [0]  * [ `COMMA`  exp_list [1] ] 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> exp_list[0] -> * exp [0]  [ `COMMA`  exp_list [1] ] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp 0

----

&emsp;&emsp;&emsp;&emsp;到达状态[179]: exp9[0] ->  ( ( `INC_OP` [3] | `DEC_OP` [3] )  exp9 [1] | exp9 [1]  ( `INC_OP` [3] | `DEC_OP` [3] | `LSQ`  exp [5]  * `RSQ` ) | exp10 [0] ) 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> exp9[0] ->  ( ( `INC_OP` [3] | `DEC_OP` [3] )  exp9 [1] | exp9 [1]  ( `INC_OP` [3] | `DEC_OP` [3] | `LSQ`  * exp [5]  `RSQ` ) | exp10 [0] ) 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp 5

----

&emsp;&emsp;&emsp;&emsp;到达状态[180]: type[0] ->  ( `ID` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `INTEGER` [0] | `DECIMAL` [0] | `LITERAL` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `CHARACTER` [0] | `BOOLEAN` [0] | `LPA`  exp [1]  * `RPA` | call [1] | lambda [2] | set [1] | invoke [1] | array [1] | map [1] ) 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> type[0] ->  ( `ID` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `INTEGER` [0] | `DECIMAL` [0] | `LITERAL` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `CHARACTER` [0] | `BOOLEAN` [0] | `LPA`  * exp [1]  `RPA` | call [1] | lambda [2] | set [1] | invoke [1] | array [1] | map [1] ) 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp 1

----

&emsp;&emsp;&emsp;&emsp;到达状态[75]: call[0] ->  `CALL` [4]  ( `LPA`  ( func [0] | exp [3] )  * `RPA` | `ID` [1] )  `LPA`  [ exp_list [2] ]  `RPA` 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> call[0] ->  `CALL` [4]  ( `LPA`  * ( func [0] | exp [3] )  `RPA` | `ID` [1] )  `LPA`  [ exp_list [2] ]  `RPA` 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp 3

----

&emsp;&emsp;&emsp;&emsp;到达状态[181]: ret[0] ->  ( `YIELD` [1] | `RETURN` )  [ exp [0] ]  * `SEMI` 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> ret[0] ->  ( `YIELD` [1] | `RETURN` )  * [ exp [0] ]  `SEMI` 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp 0

----

&emsp;&emsp;&emsp;&emsp;到达状态[182]: if[0] ->  `IF`  `LPA`  exp [0]  * `RPA`  block [1]  [ `ELSE`  ( block [2] | if [3] ) ] 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> if[0] ->  `IF`  `LPA`  * exp [0]  `RPA`  block [1]  [ `ELSE`  ( block [2] | if [3] ) ] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp 0

----

&emsp;&emsp;&emsp;&emsp;到达状态[183]: for[0] ->  `FOR`  `LPA`  [ var [0] ]  `SEMI`  [ exp [1] ]  * `SEMI`  [ ( exp [2] | var [2] ) ]  `RPA`  block [3] 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> for[0] ->  `FOR`  `LPA`  [ var [0] ]  `SEMI`  * [ exp [1] ]  `SEMI`  [ ( exp [2] | var [2] ) ]  `RPA`  block [3] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp 1

----

&emsp;&emsp;&emsp;&emsp;到达状态[67]: for[0] ->  `FOR`  `LPA`  [ var [0] ]  `SEMI`  [ exp [1] ]  `SEMI`  [ ( exp [2] | var [2] )  *]  `RPA`  block [3] 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> for[0] ->  `FOR`  `LPA`  [ var [0] ]  `SEMI`  [ exp [1] ]  `SEMI`  * [ ( exp [2] | var [2] ) ]  `RPA`  block [3] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp 2

----

&emsp;&emsp;&emsp;&emsp;到达状态[184]: while[0] ->  `WHILE`  `LPA`  exp [0]  * `RPA`  block [1] 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> while[0] ->  `WHILE`  `LPA`  * exp [0]  `RPA`  block [1] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp 0

----

&emsp;&emsp;&emsp;&emsp;到达状态[185]: foreach[0] ->  `FOREACH`  `LPA`  `VARIABLE`  `ID` [0]  `COLON`  exp [1]  * `RPA`  block [2] 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> foreach[0] ->  `FOREACH`  `LPA`  `VARIABLE`  `ID` [0]  `COLON`  * exp [1]  `RPA`  block [2] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp 1

----

&emsp;&emsp;&emsp;&emsp;到达状态[186]: set[0] ->  `SET`  exp [3]  * `PROPERTY`  exp [4]  `ASSIGN`  exp [2] 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> set[0] ->  `SET`  * exp [3]  `PROPERTY`  exp [4]  `ASSIGN`  exp [2] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp 3

----

&emsp;&emsp;&emsp;&emsp;到达状态[187]: set[0] ->  `SET`  exp [3]  `PROPERTY`  exp [4]  * `ASSIGN`  exp [2] 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> set[0] ->  `SET`  exp [3]  `PROPERTY`  * exp [4]  `ASSIGN`  exp [2] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp 4

----

&emsp;&emsp;&emsp;&emsp;到达状态[188]: set[0] ->  `SET`  exp [3]  `PROPERTY`  exp [4]  `ASSIGN`  exp [2]  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> set[0] ->  `SET`  exp [3]  `PROPERTY`  exp [4]  `ASSIGN`  * exp [2] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp 2

----

&emsp;&emsp;&emsp;&emsp;到达状态[189]: invoke[0] ->  `INVOKE` [0]  exp [1]  * `PROPERTY`  exp [2]  `PROPERTY`  `LPA`  [ exp_list [3] ]  `RPA` 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> invoke[0] ->  `INVOKE` [0]  * exp [1]  `PROPERTY`  exp [2]  `PROPERTY`  `LPA`  [ exp_list [3] ]  `RPA` 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp 1

----

&emsp;&emsp;&emsp;&emsp;到达状态[190]: invoke[0] ->  `INVOKE` [0]  exp [1]  `PROPERTY`  exp [2]  * `PROPERTY`  `LPA`  [ exp_list [3] ]  `RPA` 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> invoke[0] ->  `INVOKE` [0]  exp [1]  `PROPERTY`  * exp [2]  `PROPERTY`  `LPA`  [ exp_list [3] ]  `RPA` 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp 2

----

&emsp;&emsp;&emsp;&emsp;到达状态[191]: throw[0] ->  `THROW`  exp [0]  * `SEMI` 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> throw[0] ->  `THROW`  * exp [0]  `SEMI` 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp 0

----

&emsp;&emsp;&emsp;&emsp;到达状态[192]: map_list[0] ->  `LITERAL` [1]  `COLON` [3]  exp [2]  * [ `COMMA`  map_list [0] ] 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> map_list[0] ->  `LITERAL` [1]  `COLON` [3]  * exp [2]  [ `COMMA`  map_list [0] ] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp 2

----

&emsp;&emsp;&emsp;&emsp;到达状态[193]: scope[0] ->  exp [0]  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> scope[0] -> * exp [0] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp 0



## 状态[178]： 

项目：exp_list[0] ->  exp [0]  * [ `COMMA`  exp_list [1] ] 

规则：exp_list

----

&emsp;&emsp;&emsp;&emsp;到达状态[195]: exp_list[0] ->  exp [0]  [ `COMMA`  * exp_list [1] ] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 91(91: `COMMA`，操作符，COMMA)

&emsp;&emsp;&emsp;&emsp;指令：通过

----

&emsp;&emsp;&emsp;&emsp;到达状态[196]: exp_list[0] ->  exp [0]  [ `COMMA`  exp_list [1] ]  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> exp_list[0] ->  exp [0]  [ `COMMA`  * exp_list [1] ] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp_list 1

----

&emsp;&emsp;&emsp;&emsp;到达状态[197]: type[0] ->  ( `ID` [0]  [ `LPA` [3]  [ exp_list [4] ]  * `RPA` ] | `INTEGER` [0] | `DECIMAL` [0] | `LITERAL` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `CHARACTER` [0] | `BOOLEAN` [0] | `LPA`  exp [1]  `RPA` | call [1] | lambda [2] | set [1] | invoke [1] | array [1] | map [1] ) 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> type[0] ->  ( `ID` [0]  [ `LPA` [3]  * [ exp_list [4] ]  `RPA` ] | `INTEGER` [0] | `DECIMAL` [0] | `LITERAL` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `CHARACTER` [0] | `BOOLEAN` [0] | `LPA`  exp [1]  `RPA` | call [1] | lambda [2] | set [1] | invoke [1] | array [1] | map [1] ) 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp_list 4

----

&emsp;&emsp;&emsp;&emsp;到达状态[198]: type[0] ->  ( `ID` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `INTEGER` [0] | `DECIMAL` [0] | `LITERAL` [0]  [ `LPA` [3]  [ exp_list [4] ]  * `RPA` ] | `CHARACTER` [0] | `BOOLEAN` [0] | `LPA`  exp [1]  `RPA` | call [1] | lambda [2] | set [1] | invoke [1] | array [1] | map [1] ) 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> type[0] ->  ( `ID` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `INTEGER` [0] | `DECIMAL` [0] | `LITERAL` [0]  [ `LPA` [3]  * [ exp_list [4] ]  `RPA` ] | `CHARACTER` [0] | `BOOLEAN` [0] | `LPA`  exp [1]  `RPA` | call [1] | lambda [2] | set [1] | invoke [1] | array [1] | map [1] ) 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp_list 4

----

&emsp;&emsp;&emsp;&emsp;到达状态[199]: call[0] ->  `CALL` [4]  ( `LPA`  ( func [0] | exp [3] )  `RPA` | `ID` [1] )  `LPA`  [ exp_list [2] ]  * `RPA` 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> call[0] ->  `CALL` [4]  ( `LPA`  ( func [0] | exp [3] )  `RPA` | `ID` [1] )  `LPA`  * [ exp_list [2] ]  `RPA` 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp_list 2

----

&emsp;&emsp;&emsp;&emsp;到达状态[200]: array[0] ->  `LSQ`  [ exp_list [0] ]  * `RSQ` 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> array[0] ->  `LSQ`  * [ exp_list [0] ]  `RSQ` 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp_list 0

----

&emsp;&emsp;&emsp;&emsp;到达状态[201]: invoke[0] ->  `INVOKE` [0]  exp [1]  `PROPERTY`  exp [2]  `PROPERTY`  `LPA`  [ exp_list [3] ]  * `RPA` 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> invoke[0] ->  `INVOKE` [0]  exp [1]  `PROPERTY`  exp [2]  `PROPERTY`  `LPA`  * [ exp_list [3] ]  `RPA` 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp_list 3



## 状态[179]： 

项目：exp9[0] ->  ( ( `INC_OP` [3] | `DEC_OP` [3] )  exp9 [1] | exp9 [1]  ( `INC_OP` [3] | `DEC_OP` [3] | `LSQ`  exp [5]  * `RSQ` ) | exp10 [0] ) 

规则：exp9

----

&emsp;&emsp;&emsp;&emsp;到达状态[105]: exp9[0] ->  ( ( `INC_OP` [3] | `DEC_OP` [3] )  exp9 [1] | exp9 [1]  ( `INC_OP` [3] | `DEC_OP` [3] | `LSQ`  exp [5]  `RSQ` )  *| exp10 [0] ) 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 99(99: `RSQ`，操作符，RSQUARE)

&emsp;&emsp;&emsp;&emsp;指令：通过



## 状态[180]： 

项目：type[0] ->  ( `ID` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `INTEGER` [0] | `DECIMAL` [0] | `LITERAL` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `CHARACTER` [0] | `BOOLEAN` [0] | `LPA`  exp [1]  * `RPA` | call [1] | lambda [2] | set [1] | invoke [1] | array [1] | map [1] ) 

规则：type

----

&emsp;&emsp;&emsp;&emsp;到达状态[76]: type[0] ->  ( `ID` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `INTEGER` [0] | `DECIMAL` [0] | `LITERAL` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `CHARACTER` [0] | `BOOLEAN` [0] | `LPA`  exp [1]  `RPA` | call [1] | lambda [2] | set [1] | invoke [1] | array [1] | map [1] )  *

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 97(97: `RPA`，操作符，RPARAN)

&emsp;&emsp;&emsp;&emsp;指令：通过



## 状态[181]： 

项目：ret[0] ->  ( `YIELD` [1] | `RETURN` )  [ exp [0] ]  * `SEMI` 

规则：ret

----

&emsp;&emsp;&emsp;&emsp;到达状态[36]: ret[0] ->  ( `YIELD` [1] | `RETURN` )  [ exp [0] ]  `SEMI`  *

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 92(92: `SEMI`，操作符，SEMI)

&emsp;&emsp;&emsp;&emsp;指令：通过



## 状态[182]： 

项目：if[0] ->  `IF`  `LPA`  exp [0]  * `RPA`  block [1]  [ `ELSE`  ( block [2] | if [3] ) ] 

规则：if

----

&emsp;&emsp;&emsp;&emsp;到达状态[202]: if[0] ->  `IF`  `LPA`  exp [0]  `RPA`  * block [1]  [ `ELSE`  ( block [2] | if [3] ) ] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 97(97: `RPA`，操作符，RPARAN)

&emsp;&emsp;&emsp;&emsp;指令：通过



## 状态[183]： 

项目：for[0] ->  `FOR`  `LPA`  [ var [0] ]  `SEMI`  [ exp [1] ]  * `SEMI`  [ ( exp [2] | var [2] ) ]  `RPA`  block [3] 

规则：for

----

&emsp;&emsp;&emsp;&emsp;到达状态[72]: for[0] ->  `FOR`  `LPA`  [ var [0] ]  `SEMI`  [ exp [1] ]  `SEMI`  * [ ( exp [2] | var [2] ) ]  `RPA`  block [3] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 92(92: `SEMI`，操作符，SEMI)

&emsp;&emsp;&emsp;&emsp;指令：通过



## 状态[184]： 

项目：while[0] ->  `WHILE`  `LPA`  exp [0]  * `RPA`  block [1] 

规则：while

----

&emsp;&emsp;&emsp;&emsp;到达状态[203]: while[0] ->  `WHILE`  `LPA`  exp [0]  `RPA`  * block [1] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 97(97: `RPA`，操作符，RPARAN)

&emsp;&emsp;&emsp;&emsp;指令：通过



## 状态[185]： 

项目：foreach[0] ->  `FOREACH`  `LPA`  `VARIABLE`  `ID` [0]  `COLON`  exp [1]  * `RPA`  block [2] 

规则：foreach

----

&emsp;&emsp;&emsp;&emsp;到达状态[204]: foreach[0] ->  `FOREACH`  `LPA`  `VARIABLE`  `ID` [0]  `COLON`  exp [1]  `RPA`  * block [2] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 97(97: `RPA`，操作符，RPARAN)

&emsp;&emsp;&emsp;&emsp;指令：通过



## 状态[186]： 

项目：set[0] ->  `SET`  exp [3]  * `PROPERTY`  exp [4]  `ASSIGN`  exp [2] 

规则：set

----

&emsp;&emsp;&emsp;&emsp;到达状态[205]: set[0] ->  `SET`  exp [3]  `PROPERTY`  * exp [4]  `ASSIGN`  exp [2] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 102(102: `PROPERTY`，操作符，PROPERTY)

&emsp;&emsp;&emsp;&emsp;指令：通过



## 状态[187]： 

项目：set[0] ->  `SET`  exp [3]  `PROPERTY`  exp [4]  * `ASSIGN`  exp [2] 

规则：set

----

&emsp;&emsp;&emsp;&emsp;到达状态[206]: set[0] ->  `SET`  exp [3]  `PROPERTY`  exp [4]  `ASSIGN`  * exp [2] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 94(94: `ASSIGN`，操作符，ASSIGN)

&emsp;&emsp;&emsp;&emsp;指令：通过



## 状态[188]： 

项目：set[0] ->  `SET`  exp [3]  `PROPERTY`  exp [4]  `ASSIGN`  exp [2]  *

规则：set

----

&emsp;&emsp;&emsp;&emsp;到达状态[76]: type[0] ->  ( `ID` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `INTEGER` [0] | `DECIMAL` [0] | `LITERAL` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `CHARACTER` [0] | `BOOLEAN` [0] | `LPA`  exp [1]  `RPA` | call [1] | lambda [2] | set [1] | invoke [1] | array [1] | map [1] )  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> type[0] -> * ( `ID` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `INTEGER` [0] | `DECIMAL` [0] | `LITERAL` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `CHARACTER` [0] | `BOOLEAN` [0] | `LPA`  exp [1]  `RPA` | call [1] | lambda [2] | set [1] | invoke [1] | array [1] | map [1] ) 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> set 1



## 状态[189]： 

项目：invoke[0] ->  `INVOKE` [0]  exp [1]  * `PROPERTY`  exp [2]  `PROPERTY`  `LPA`  [ exp_list [3] ]  `RPA` 

规则：invoke

----

&emsp;&emsp;&emsp;&emsp;到达状态[207]: invoke[0] ->  `INVOKE` [0]  exp [1]  `PROPERTY`  * exp [2]  `PROPERTY`  `LPA`  [ exp_list [3] ]  `RPA` 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 102(102: `PROPERTY`，操作符，PROPERTY)

&emsp;&emsp;&emsp;&emsp;指令：通过



## 状态[190]： 

项目：invoke[0] ->  `INVOKE` [0]  exp [1]  `PROPERTY`  exp [2]  * `PROPERTY`  `LPA`  [ exp_list [3] ]  `RPA` 

规则：invoke

----

&emsp;&emsp;&emsp;&emsp;到达状态[208]: invoke[0] ->  `INVOKE` [0]  exp [1]  `PROPERTY`  exp [2]  `PROPERTY`  * `LPA`  [ exp_list [3] ]  `RPA` 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 102(102: `PROPERTY`，操作符，PROPERTY)

&emsp;&emsp;&emsp;&emsp;指令：通过



## 状态[191]： 

项目：throw[0] ->  `THROW`  exp [0]  * `SEMI` 

规则：throw

----

&emsp;&emsp;&emsp;&emsp;到达状态[209]: throw[0] ->  `THROW`  exp [0]  `SEMI`  *

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 92(92: `SEMI`，操作符，SEMI)

&emsp;&emsp;&emsp;&emsp;指令：通过



## 状态[192]： 

项目：map_list[0] ->  `LITERAL` [1]  `COLON` [3]  exp [2]  * [ `COMMA`  map_list [0] ] 

规则：map_list

----

&emsp;&emsp;&emsp;&emsp;到达状态[210]: map_list[0] ->  `LITERAL` [1]  `COLON` [3]  exp [2]  [ `COMMA`  * map_list [0] ] 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 91(91: `COMMA`，操作符，COMMA)

&emsp;&emsp;&emsp;&emsp;指令：通过  [Action]

----

&emsp;&emsp;&emsp;&emsp;到达状态[211]: map[0] ->  `LBR`  [ map_list [0] ]  * `RBR` 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> map[0] ->  `LBR`  * [ map_list [0] ]  `RBR` 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> map_list 0

----

&emsp;&emsp;&emsp;&emsp;到达状态[212]: map_list[0] ->  `LITERAL` [1]  `COLON` [3]  exp [2]  [ `COMMA`  map_list [0] ]  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> map_list[0] ->  `LITERAL` [1]  `COLON` [3]  exp [2]  [ `COMMA`  * map_list [0] ] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> map_list 0



## 状态[193]： 

项目：scope[0] ->  exp [0]  *

规则：scope

----

&emsp;&emsp;&emsp;&emsp;到达状态[55]: func[0] ->  ( `FUNCTION` [10] | `YIELD` )  [ `LSQ`  doc_list [0]  `RSQ` ]  ( `ID` [1] | `NOT` [1] )  `LPA`  [ var_list [2] ]  `RPA`  ( `PTR_OP`  scope [3] | block [4] )  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> func[0] ->  ( `FUNCTION` [10] | `YIELD` )  [ `LSQ`  doc_list [0]  `RSQ` ]  ( `ID` [1] | `NOT` [1] )  `LPA`  [ var_list [2] ]  `RPA`  ( `PTR_OP`  * scope [3] | block [4] ) 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> scope 3

----

&emsp;&emsp;&emsp;&emsp;到达状态[56]: lambda[0] ->  `LAMBDA` [1]  `LPA`  [ var_list [2] ]  `RPA`  ( `PTR_OP`  scope [3] | block [4] )  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> lambda[0] ->  `LAMBDA` [1]  `LPA`  [ var_list [2] ]  `RPA`  ( `PTR_OP`  * scope [3] | block [4] ) 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> scope 3



## 状态[194]： 

项目：exp01[0] ->  [ exp01 [1]  ( `EQ_ASSIGN` [2] | `ADD_ASSIGN` [2] | `SUB_ASSIGN` [2] | `MUL_ASSIGN` [2] | `DIV_ASSIGN` [2] | `AND_ASSIGN` [2] | `OR_ASSIGN` [2] | `XOR_ASSIGN` [2] | `MOD_ASSIGN` [2] )  *]  exp0 [0] 

规则：exp01

----

&emsp;&emsp;&emsp;&emsp;到达状态[47]: exp0[0] -> * exp1 [0]  [ `QUERY` [4]  exp0 [6]  `COLON` [5]  exp0 [7] ] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[57: `INC_OP`，操作符，PLUS_PLUS]&emsp;[4: `INTEGER`，整数，(null)]&emsp;[58: `DEC_OP`，操作符，MINUS_MINUS]&emsp;[1: `BOOLEAN`，布尔，(null)]&emsp;[44: `CALL`，关键字，CALL]&emsp;[3: `CHARACTER`，字符，(null)]&emsp;[100: `LBR`，操作符，LBRACE]&emsp;[86: `NOT`，操作符，BIT_NOT]&emsp;[87: `NOT_OP`，操作符，LOGICAL_NOT]&emsp;[50: `SET`，关键字，SET]&emsp;[5: `DECIMAL`，实数，(null)]&emsp;[98: `LSQ`，操作符，LSQUARE]&emsp;[96: `LPA`，操作符，LPARAN]&emsp;[51: `INVOKE`，关键字，INVOKE]&emsp;[2: `LITERAL`，字符串，(null)]&emsp;[0: `ID`，标识符，(null)]&emsp;[49: `LAMBDA`，关键字，LAMBDA]&emsp;



## 状态[195]： 

项目：exp_list[0] ->  exp [0]  [ `COMMA`  * exp_list [1] ] 

规则：exp_list

----

&emsp;&emsp;&emsp;&emsp;到达状态[81]: exp_list[0] -> * exp [0]  [ `COMMA`  exp_list [1] ] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[57: `INC_OP`，操作符，PLUS_PLUS]&emsp;[4: `INTEGER`，整数，(null)]&emsp;[58: `DEC_OP`，操作符，MINUS_MINUS]&emsp;[1: `BOOLEAN`，布尔，(null)]&emsp;[44: `CALL`，关键字，CALL]&emsp;[3: `CHARACTER`，字符，(null)]&emsp;[100: `LBR`，操作符，LBRACE]&emsp;[86: `NOT`，操作符，BIT_NOT]&emsp;[87: `NOT_OP`，操作符，LOGICAL_NOT]&emsp;[50: `SET`，关键字，SET]&emsp;[5: `DECIMAL`，实数，(null)]&emsp;[98: `LSQ`，操作符，LSQUARE]&emsp;[96: `LPA`，操作符，LPARAN]&emsp;[51: `INVOKE`，关键字，INVOKE]&emsp;[2: `LITERAL`，字符串，(null)]&emsp;[0: `ID`，标识符，(null)]&emsp;[49: `LAMBDA`，关键字，LAMBDA]&emsp;



## 状态[196]： 

项目：exp_list[0] ->  exp [0]  [ `COMMA`  exp_list [1] ]  *

规则：exp_list

----

&emsp;&emsp;&emsp;&emsp;到达状态[196]: exp_list[0] ->  exp [0]  [ `COMMA`  exp_list [1] ]  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> exp_list[0] ->  exp [0]  [ `COMMA`  * exp_list [1] ] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp_list 1

----

&emsp;&emsp;&emsp;&emsp;到达状态[197]: type[0] ->  ( `ID` [0]  [ `LPA` [3]  [ exp_list [4] ]  * `RPA` ] | `INTEGER` [0] | `DECIMAL` [0] | `LITERAL` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `CHARACTER` [0] | `BOOLEAN` [0] | `LPA`  exp [1]  `RPA` | call [1] | lambda [2] | set [1] | invoke [1] | array [1] | map [1] ) 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> type[0] ->  ( `ID` [0]  [ `LPA` [3]  * [ exp_list [4] ]  `RPA` ] | `INTEGER` [0] | `DECIMAL` [0] | `LITERAL` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `CHARACTER` [0] | `BOOLEAN` [0] | `LPA`  exp [1]  `RPA` | call [1] | lambda [2] | set [1] | invoke [1] | array [1] | map [1] ) 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp_list 4

----

&emsp;&emsp;&emsp;&emsp;到达状态[198]: type[0] ->  ( `ID` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `INTEGER` [0] | `DECIMAL` [0] | `LITERAL` [0]  [ `LPA` [3]  [ exp_list [4] ]  * `RPA` ] | `CHARACTER` [0] | `BOOLEAN` [0] | `LPA`  exp [1]  `RPA` | call [1] | lambda [2] | set [1] | invoke [1] | array [1] | map [1] ) 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> type[0] ->  ( `ID` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `INTEGER` [0] | `DECIMAL` [0] | `LITERAL` [0]  [ `LPA` [3]  * [ exp_list [4] ]  `RPA` ] | `CHARACTER` [0] | `BOOLEAN` [0] | `LPA`  exp [1]  `RPA` | call [1] | lambda [2] | set [1] | invoke [1] | array [1] | map [1] ) 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp_list 4

----

&emsp;&emsp;&emsp;&emsp;到达状态[199]: call[0] ->  `CALL` [4]  ( `LPA`  ( func [0] | exp [3] )  `RPA` | `ID` [1] )  `LPA`  [ exp_list [2] ]  * `RPA` 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> call[0] ->  `CALL` [4]  ( `LPA`  ( func [0] | exp [3] )  `RPA` | `ID` [1] )  `LPA`  * [ exp_list [2] ]  `RPA` 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp_list 2

----

&emsp;&emsp;&emsp;&emsp;到达状态[200]: array[0] ->  `LSQ`  [ exp_list [0] ]  * `RSQ` 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> array[0] ->  `LSQ`  * [ exp_list [0] ]  `RSQ` 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp_list 0

----

&emsp;&emsp;&emsp;&emsp;到达状态[201]: invoke[0] ->  `INVOKE` [0]  exp [1]  `PROPERTY`  exp [2]  `PROPERTY`  `LPA`  [ exp_list [3] ]  * `RPA` 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> invoke[0] ->  `INVOKE` [0]  exp [1]  `PROPERTY`  exp [2]  `PROPERTY`  `LPA`  * [ exp_list [3] ]  `RPA` 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> exp_list 3



## 状态[197]： 

项目：type[0] ->  ( `ID` [0]  [ `LPA` [3]  [ exp_list [4] ]  * `RPA` ] | `INTEGER` [0] | `DECIMAL` [0] | `LITERAL` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `CHARACTER` [0] | `BOOLEAN` [0] | `LPA`  exp [1]  `RPA` | call [1] | lambda [2] | set [1] | invoke [1] | array [1] | map [1] ) 

规则：type

----

&emsp;&emsp;&emsp;&emsp;到达状态[76]: type[0] ->  ( `ID` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `INTEGER` [0] | `DECIMAL` [0] | `LITERAL` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `CHARACTER` [0] | `BOOLEAN` [0] | `LPA`  exp [1]  `RPA` | call [1] | lambda [2] | set [1] | invoke [1] | array [1] | map [1] )  *

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 97(97: `RPA`，操作符，RPARAN)

&emsp;&emsp;&emsp;&emsp;指令：通过



## 状态[198]： 

项目：type[0] ->  ( `ID` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `INTEGER` [0] | `DECIMAL` [0] | `LITERAL` [0]  [ `LPA` [3]  [ exp_list [4] ]  * `RPA` ] | `CHARACTER` [0] | `BOOLEAN` [0] | `LPA`  exp [1]  `RPA` | call [1] | lambda [2] | set [1] | invoke [1] | array [1] | map [1] ) 

规则：type

----

&emsp;&emsp;&emsp;&emsp;到达状态[76]: type[0] ->  ( `ID` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `INTEGER` [0] | `DECIMAL` [0] | `LITERAL` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `CHARACTER` [0] | `BOOLEAN` [0] | `LPA`  exp [1]  `RPA` | call [1] | lambda [2] | set [1] | invoke [1] | array [1] | map [1] )  *

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 97(97: `RPA`，操作符，RPARAN)

&emsp;&emsp;&emsp;&emsp;指令：通过



## 状态[199]： 

项目：call[0] ->  `CALL` [4]  ( `LPA`  ( func [0] | exp [3] )  `RPA` | `ID` [1] )  `LPA`  [ exp_list [2] ]  * `RPA` 

规则：call

----

&emsp;&emsp;&emsp;&emsp;到达状态[82]: call[0] ->  `CALL` [4]  ( `LPA`  ( func [0] | exp [3] )  `RPA` | `ID` [1] )  `LPA`  [ exp_list [2] ]  `RPA`  *

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 97(97: `RPA`，操作符，RPARAN)

&emsp;&emsp;&emsp;&emsp;指令：通过



## 状态[200]： 

项目：array[0] ->  `LSQ`  [ exp_list [0] ]  * `RSQ` 

规则：array

----

&emsp;&emsp;&emsp;&emsp;到达状态[147]: array[0] ->  `LSQ`  [ exp_list [0] ]  `RSQ`  *

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 99(99: `RSQ`，操作符，RSQUARE)

&emsp;&emsp;&emsp;&emsp;指令：通过



## 状态[201]： 

项目：invoke[0] ->  `INVOKE` [0]  exp [1]  `PROPERTY`  exp [2]  `PROPERTY`  `LPA`  [ exp_list [3] ]  * `RPA` 

规则：invoke

----

&emsp;&emsp;&emsp;&emsp;到达状态[213]: invoke[0] ->  `INVOKE` [0]  exp [1]  `PROPERTY`  exp [2]  `PROPERTY`  `LPA`  [ exp_list [3] ]  `RPA`  *

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 97(97: `RPA`，操作符，RPARAN)

&emsp;&emsp;&emsp;&emsp;指令：通过



## 状态[202]： 

项目：if[0] ->  `IF`  `LPA`  exp [0]  `RPA`  * block [1]  [ `ELSE`  ( block [2] | if [3] ) ] 

规则：if

----

&emsp;&emsp;&emsp;&emsp;到达状态[16]: block[0] -> * `LBR`  [ stmt_list [0] ]  `RBR` 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[100: `LBR`，操作符，LBRACE]&emsp;



## 状态[203]： 

项目：while[0] ->  `WHILE`  `LPA`  exp [0]  `RPA`  * block [1] 

规则：while

----

&emsp;&emsp;&emsp;&emsp;到达状态[16]: block[0] -> * `LBR`  [ stmt_list [0] ]  `RBR` 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[100: `LBR`，操作符，LBRACE]&emsp;



## 状态[204]： 

项目：foreach[0] ->  `FOREACH`  `LPA`  `VARIABLE`  `ID` [0]  `COLON`  exp [1]  `RPA`  * block [2] 

规则：foreach

----

&emsp;&emsp;&emsp;&emsp;到达状态[16]: block[0] -> * `LBR`  [ stmt_list [0] ]  `RBR` 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[100: `LBR`，操作符，LBRACE]&emsp;



## 状态[205]： 

项目：set[0] ->  `SET`  exp [3]  `PROPERTY`  * exp [4]  `ASSIGN`  exp [2] 

规则：set

----

&emsp;&emsp;&emsp;&emsp;到达状态[20]: exp[0] -> * exp01 [0]  [ ( `INTEGER` [10] | `DECIMAL` [10] ) ] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[57: `INC_OP`，操作符，PLUS_PLUS]&emsp;[4: `INTEGER`，整数，(null)]&emsp;[58: `DEC_OP`，操作符，MINUS_MINUS]&emsp;[1: `BOOLEAN`，布尔，(null)]&emsp;[44: `CALL`，关键字，CALL]&emsp;[3: `CHARACTER`，字符，(null)]&emsp;[100: `LBR`，操作符，LBRACE]&emsp;[86: `NOT`，操作符，BIT_NOT]&emsp;[87: `NOT_OP`，操作符，LOGICAL_NOT]&emsp;[50: `SET`，关键字，SET]&emsp;[5: `DECIMAL`，实数，(null)]&emsp;[98: `LSQ`，操作符，LSQUARE]&emsp;[96: `LPA`，操作符，LPARAN]&emsp;[51: `INVOKE`，关键字，INVOKE]&emsp;[2: `LITERAL`，字符串，(null)]&emsp;[0: `ID`，标识符，(null)]&emsp;[49: `LAMBDA`，关键字，LAMBDA]&emsp;



## 状态[206]： 

项目：set[0] ->  `SET`  exp [3]  `PROPERTY`  exp [4]  `ASSIGN`  * exp [2] 

规则：set

----

&emsp;&emsp;&emsp;&emsp;到达状态[20]: exp[0] -> * exp01 [0]  [ ( `INTEGER` [10] | `DECIMAL` [10] ) ] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[57: `INC_OP`，操作符，PLUS_PLUS]&emsp;[4: `INTEGER`，整数，(null)]&emsp;[58: `DEC_OP`，操作符，MINUS_MINUS]&emsp;[1: `BOOLEAN`，布尔，(null)]&emsp;[44: `CALL`，关键字，CALL]&emsp;[3: `CHARACTER`，字符，(null)]&emsp;[100: `LBR`，操作符，LBRACE]&emsp;[86: `NOT`，操作符，BIT_NOT]&emsp;[87: `NOT_OP`，操作符，LOGICAL_NOT]&emsp;[50: `SET`，关键字，SET]&emsp;[5: `DECIMAL`，实数，(null)]&emsp;[98: `LSQ`，操作符，LSQUARE]&emsp;[96: `LPA`，操作符，LPARAN]&emsp;[51: `INVOKE`，关键字，INVOKE]&emsp;[2: `LITERAL`，字符串，(null)]&emsp;[0: `ID`，标识符，(null)]&emsp;[49: `LAMBDA`，关键字，LAMBDA]&emsp;



## 状态[207]： 

项目：invoke[0] ->  `INVOKE` [0]  exp [1]  `PROPERTY`  * exp [2]  `PROPERTY`  `LPA`  [ exp_list [3] ]  `RPA` 

规则：invoke

----

&emsp;&emsp;&emsp;&emsp;到达状态[20]: exp[0] -> * exp01 [0]  [ ( `INTEGER` [10] | `DECIMAL` [10] ) ] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[57: `INC_OP`，操作符，PLUS_PLUS]&emsp;[4: `INTEGER`，整数，(null)]&emsp;[58: `DEC_OP`，操作符，MINUS_MINUS]&emsp;[1: `BOOLEAN`，布尔，(null)]&emsp;[44: `CALL`，关键字，CALL]&emsp;[3: `CHARACTER`，字符，(null)]&emsp;[100: `LBR`，操作符，LBRACE]&emsp;[86: `NOT`，操作符，BIT_NOT]&emsp;[87: `NOT_OP`，操作符，LOGICAL_NOT]&emsp;[50: `SET`，关键字，SET]&emsp;[5: `DECIMAL`，实数，(null)]&emsp;[98: `LSQ`，操作符，LSQUARE]&emsp;[96: `LPA`，操作符，LPARAN]&emsp;[51: `INVOKE`，关键字，INVOKE]&emsp;[2: `LITERAL`，字符串，(null)]&emsp;[0: `ID`，标识符，(null)]&emsp;[49: `LAMBDA`，关键字，LAMBDA]&emsp;



## 状态[208]： 

项目：invoke[0] ->  `INVOKE` [0]  exp [1]  `PROPERTY`  exp [2]  `PROPERTY`  * `LPA`  [ exp_list [3] ]  `RPA` 

规则：invoke

----

&emsp;&emsp;&emsp;&emsp;到达状态[214]: invoke[0] ->  `INVOKE` [0]  exp [1]  `PROPERTY`  exp [2]  `PROPERTY`  `LPA`  * [ exp_list [3] ]  `RPA` 

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 96(96: `LPA`，操作符，LPARAN)

&emsp;&emsp;&emsp;&emsp;指令：通过



## 状态[209]： 

项目：throw[0] ->  `THROW`  exp [0]  `SEMI`  *

规则：throw

----

&emsp;&emsp;&emsp;&emsp;到达状态[51]: stmt_ctrl[0] ->  ( ret [0] | port [0] | if [0] | for [0] | foreach [0] | while [0] | try [0] | throw [0] )  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> stmt_ctrl[0] -> * ( ret [0] | port [0] | if [0] | for [0] | foreach [0] | while [0] | try [0] | throw [0] ) 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> throw 0



## 状态[210]： 

项目：map_list[0] ->  `LITERAL` [1]  `COLON` [3]  exp [2]  [ `COMMA`  * map_list [0] ] 

规则：map_list

----

&emsp;&emsp;&emsp;&emsp;到达状态[148]: map_list[0] -> * `LITERAL` [1]  `COLON` [3]  exp [2]  [ `COMMA`  map_list [0] ] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[2: `LITERAL`，字符串，(null)]&emsp;



## 状态[211]： 

项目：map[0] ->  `LBR`  [ map_list [0] ]  * `RBR` 

规则：map

----

&emsp;&emsp;&emsp;&emsp;到达状态[149]: map[0] ->  `LBR`  [ map_list [0] ]  `RBR`  *

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 101(101: `RBR`，操作符，RBRACE)

&emsp;&emsp;&emsp;&emsp;指令：通过



## 状态[212]： 

项目：map_list[0] ->  `LITERAL` [1]  `COLON` [3]  exp [2]  [ `COMMA`  map_list [0] ]  *

规则：map_list

----

&emsp;&emsp;&emsp;&emsp;到达状态[211]: map[0] ->  `LBR`  [ map_list [0] ]  * `RBR` 

&emsp;&emsp;&emsp;&emsp;类型：归约	=> map[0] ->  `LBR`  * [ map_list [0] ]  `RBR` 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> map_list 0

----

&emsp;&emsp;&emsp;&emsp;到达状态[212]: map_list[0] ->  `LITERAL` [1]  `COLON` [3]  exp [2]  [ `COMMA`  map_list [0] ]  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> map_list[0] ->  `LITERAL` [1]  `COLON` [3]  exp [2]  [ `COMMA`  * map_list [0] ] 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> map_list 0



## 状态[213]： 

项目：invoke[0] ->  `INVOKE` [0]  exp [1]  `PROPERTY`  exp [2]  `PROPERTY`  `LPA`  [ exp_list [3] ]  `RPA`  *

规则：invoke

----

&emsp;&emsp;&emsp;&emsp;到达状态[76]: type[0] ->  ( `ID` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `INTEGER` [0] | `DECIMAL` [0] | `LITERAL` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `CHARACTER` [0] | `BOOLEAN` [0] | `LPA`  exp [1]  `RPA` | call [1] | lambda [2] | set [1] | invoke [1] | array [1] | map [1] )  *

&emsp;&emsp;&emsp;&emsp;类型：归约	=> type[0] -> * ( `ID` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `INTEGER` [0] | `DECIMAL` [0] | `LITERAL` [0]  [ `LPA` [3]  [ exp_list [4] ]  `RPA` ] | `CHARACTER` [0] | `BOOLEAN` [0] | `LPA`  exp [1]  `RPA` | call [1] | lambda [2] | set [1] | invoke [1] | array [1] | map [1] ) 

&emsp;&emsp;&emsp;&emsp;指令：翻译	=> invoke 1



## 状态[214]： 

项目：invoke[0] ->  `INVOKE` [0]  exp [1]  `PROPERTY`  exp [2]  `PROPERTY`  `LPA`  * [ exp_list [3] ]  `RPA` 

规则：invoke

----

&emsp;&emsp;&emsp;&emsp;到达状态[81]: exp_list[0] -> * exp [0]  [ `COMMA`  exp_list [1] ] 

&emsp;&emsp;&emsp;&emsp;类型：转移

&emsp;&emsp;&emsp;&emsp;指令：移进

&emsp;&emsp;&emsp;&emsp;预查：[57: `INC_OP`，操作符，PLUS_PLUS]&emsp;[4: `INTEGER`，整数，(null)]&emsp;[58: `DEC_OP`，操作符，MINUS_MINUS]&emsp;[1: `BOOLEAN`，布尔，(null)]&emsp;[44: `CALL`，关键字，CALL]&emsp;[3: `CHARACTER`，字符，(null)]&emsp;[100: `LBR`，操作符，LBRACE]&emsp;[86: `NOT`，操作符，BIT_NOT]&emsp;[87: `NOT_OP`，操作符，LOGICAL_NOT]&emsp;[50: `SET`，关键字，SET]&emsp;[5: `DECIMAL`，实数，(null)]&emsp;[98: `LSQ`，操作符，LSQUARE]&emsp;[96: `LPA`，操作符，LPARAN]&emsp;[51: `INVOKE`，关键字，INVOKE]&emsp;[2: `LITERAL`，字符串，(null)]&emsp;[0: `ID`，标识符，(null)]&emsp;[49: `LAMBDA`，关键字，LAMBDA]&emsp;

----

&emsp;&emsp;&emsp;&emsp;到达状态[213]: invoke[0] ->  `INVOKE` [0]  exp [1]  `PROPERTY`  exp [2]  `PROPERTY`  `LPA`  [ exp_list [3] ]  `RPA`  *

&emsp;&emsp;&emsp;&emsp;类型：匹配	=> 97(97: `RPA`，操作符，RPARAN)

&emsp;&emsp;&emsp;&emsp;指令：通过



