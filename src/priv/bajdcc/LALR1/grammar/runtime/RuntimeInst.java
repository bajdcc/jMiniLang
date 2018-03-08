package priv.bajdcc.LALR1.grammar.runtime;

/**
 * 【中间代码】运行时指令集
 *
 * @author bajdcc
 */
public enum RuntimeInst {
    ihalt,            // 停止
    inop,             // 空指令
    ipush,            // [op1]进栈
    ipop,             // 出栈
    iopena,           // 过程参数准备进栈
    ipushx,           // 空进栈
    ipushz,           // 参数零进栈
    ipusha,           // 数据栈[top]进调用栈
    ipushn,           // 非数进栈
    iloada,           // 调用栈进数据栈[top]
    icall,            // 过程调用
    icallx,           // 外部过程调用，数据段
    ically,           // 外部过程调用，参数栈
    iret,             // 过程返回

    iyldl,            // 协程返回
    iyldr,            // 协程进入
    iyldx,            // 协程销毁
    iyldy,            // 协程创建
    iyldi,            // 协程队列数据入队，从栈
    iyldo,            // 协程队列数据出队，从栈

    iscpi,            // 进入命名空间
    iscpo,            // 离开命名空间

    iload,            // 数值载入
    iloadv,           // 变量载入
    iloadx,           // 外部变量载入
    ildfun,           // 函数地址载入
    irefun,           // 声明递归函数
	istore,           // 栈顶[top]数据存入[top-1]索引指向的位置（引用）
	iassign,          // 栈顶[top]数据存入[top-1]索引指向的位置（引用）
	ialloc,           // 栈顶[top]数据存入[top-1]索引指向的位置（新建）

    iimp,             // 导入[top]

    ijmp,             // 跳转
    ijz,              // 为零，[top]=0跳转到绝对地址op1
    ijnz,             // 非零，[top]<>0跳转到绝对地址op1
    ijt,              // [top]为真跳转到绝对地址op1，弹出数据
    ijf,              // [top]为假跳转到绝对地址op1，弹出数据
    ijtx,             // [top]为真跳转到绝对地址op1，不弹出数据
    ijfx,             // [top]为假跳转到绝对地址op1，不弹出数据
    ijnan,            // 结果为非数（用户迭代中止）
    ijyld,            // 已创建协程则跳转

    iinc,             // 自增，[top]++
    idec,             // 自减，[top]--
    inot,             // 逻辑非，![top]
    iinv,             // 算术非，~[top]

    iadd,             // 加，[top]+[top-1]
    isub,             // 减，[top]-[top-1]
    imul,             // 乘，[top]*[top-1]
    idiv,             // 除，[top]/[top-1]
    imod,             // 余，[top]%[top-1]
    iand,             // 算术或逻辑与，[top]&[top-1]
    iandl,            // 逻辑或逻辑与，[top]&&[top-1]
    ior,              // 算术或逻辑或，[top]|[top-1]
    iorl,             // 逻辑或逻辑或，[top]||[top-1]
    ixor,             // 异或，[top]^[top-1]
    ishl,             // 左移，[top]<<[top-1]
    ishr,             // 右移，[top]>>[top-1]

    icl,              // 小于，[top]<[top-1]
    icg,              // 大于，[top]>[top-1]
    icle,             // 小于等于，[top]<=[top-1]
    icge,             // 大于等于，[top]>=[top-1]
    ice,              // 等于，[top]=[top-1]
    icne,             // 不等于，[top]!=[top-1]

    iarr,             // 生成数组于[top]
    imap,             // 生成字典于[top]
}
