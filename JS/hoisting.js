//================= How JS execute ===============
/**
 * JS is not a compiled language like Java, C++. Which need to be compiled first
 * JS is a interpreter language
 *
 * which means: interprete one line, execute one line
 *
 * what what if we have the following case
 */


//================= Hoisting ================

/**
 * Example 1:
 */
// func1();    // error or what?
// function func1() {
//     console.log("this is func1");
// }


/**
 * Example 2:
 */
// function func1() {
//     a = 10;
//     var a;
//     console.log(a);
// }
// func1();

/**
 * Example 3:
 */
// function func1() {
//     a = 10;
//     let a;
//     console.log(a);
// }
// func1();


/** Conclusion
 *  1. 只会提升变量的声明
 *  2. 只会hoisting function, var 定义的变量(这也是为什么var是function scope, 其定义会被提升到当前function顶部)
 */

// Hoisting Example 1
test();
function test() {
    console.log("test1");
}
/** result: test1 函数被提升
 */

// Hoisting Example 2
console.log(c); // c = ?
var c = 10;
/** result: undefined 只有变量c本身的【声明】被提升，但赋值不会被提升
 */

// 预编译例子
function test2() {
    console.log(a); // 1 = ?
    var a = 1;

    console.log (a); // 2 = ?
    function a() {
        console.log("===");
    }

    var b = function() {
        console.log("+++");
    }
    console.log(b); // 3 = ?
}
/** result:
 *  1 = f a() { console.log("==="); }
 *  函数优先级高于变量，且函数没有被执行
 *  2 = 1
 *  a被赋值后打印，为1
 *  3 = f () { console.log("+++"); }
 *  var b被赋值为函数体，且没有执行
 */

// 执行上下文实例
function f1() {
    console.log("f1");
    f2();
}
function f2() {
    console.log("f2");
    f3();
}
function f3() {
    console.log("f3");
    f4();
}
function f1() {
    console.log("f4");
}
// 现在执行f1()
f1();
/** result: f1 -- f2 -- f3 -- f4
 */
// 代码执行前，会立即创建一个全局执行上下文 (Global Execution Context)
// 创建完了全局的执行上下文之后，把GEC放入到执行环境栈 ECS(EC Stack)
// 首先f1入栈，执行f1()，打印f1()，执行f2()
// f2入栈，执行f2()，打印f2()，执行f3()
// f3入栈，执行f3()，打印f3()，执行f4()
// f4入栈，执行f4()，打印f4()，结束
// 此时f4()没有显性的return，js引擎会有一个默认的return
// f4()出栈
// stack栈顶为f3()，f3()默认return，f3()出栈
// stack栈顶为f2()，f2()默认return，f2()出栈
// stack栈顶为f1()，f1()默认return，f1()出栈
// ======================================================================================

// 第一步： 扫描函数传进来的(实参)参数
// 第二步： 扫描函数声明
// 第三步： 扫描变量声明
console.log(a1);    // ?
console.log(b1);    // ?
console.log(f1);    // ?

// 函数变量声明
var a1 = 19;
var a2 = 20;
var a3 = "hello";
var b1 = {name : "John Doe"};

// 函数可以先调用再声明 why?
// GEC创建扫描时拿到的是整个函数体

// 函数调用
var res = f1(a1, a2);
console.log(res);

// 函数声明
function f1(a, b) {
    // f1函数的执行上下文
    // 1： 扫描函数： a = 19, b = 20
    // 2： 扫描函数声明： f2 = function(){}
    // 3： 扫描变量声明： t = undefined, m = undefined
    var t = 0;
    var m = 10;

    console.log(a); // ?
    console.log(b); // ?

    function f2() {
        console.log("this is f2!");
    }
    f2();
    return a + b;
}

// GEC先被创建，扫描
// VO --> [ 2 -> f1(body), 3 -> a1(undefined), a2(undefined),
// a3(undefined), b1(undefined), res(undefined)]
// 没有1，先2(扫描函数声明)，后3(扫描变量声明)
// 在扫描f1时，因为f1是一个函数，所以在这一瞬间，f1中已经有了EC
// 但是EC中还没有AO，在执行f1函数的前一刻，AO才会被创建
// 同时，也正是因为扫描时拿到的是整个函数体，所以函数可以先调用再声明

// GEC执行
// VO --> [ 2 -> f1(body), 3 -> a1(undefined -> 19), a2(undefined -> 20),
// a3(undefined -> 'hello'), b1(undefined -> {}), res(undefined -> 39)]
// 对变量进行赋值
// 此时变量res进行了f1函数调用，之前在扫描时提到f1中已经有了EC，但还没有AO

// 此时在f1调用前一刻立即创建了一个f1 EC下的AO，然后进入函数体，再重复进行三部曲
// f1 EC 扫描
// AO -> [1 -> a=19, b=20, 2 -> f2(body), 3 -> t(undefined), m(undefined)]
// 同样，扫描到f2时，f2中也已经有了EC，但还没有AO

// f1 EC 执行
// AO -> [1 -> a=19, b=20, 2 -> f2(body), 3 -> t(undefined -> 0), m(undefined -> 10)]

// 执行f2，立即创建f2 EC下的AO，进入函数体，重复三部曲
// f2 EC
// AO -> []
// 执行f2时就会打印string "this is f2!"，f2执行完成

// 最后f1执行到最后一行，return a + b，返回数值给上面的res