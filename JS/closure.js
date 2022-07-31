// ====================== closure ======================
// An inner function always has access to the vars and parameters of its outer function
// even after the outer function has returned
// 当我一个函数返回的是一个函数体，并且这个函数体还拿着它外面这个函数的局部变量，
// 这样就形成了一个闭包

// 定义一个外层函数
function add() {
    // 局部变量 arg
    var n = 2;
    var sol = function() {
        console.log(n);
    };
    // 把另一个函数作为返回值
    return sol;
    // return的就是inner函数体
    // add()就是inner函数的outer函数
}

// 调用add函数，并把返回的inner函数赋给了res
var res = add();
// 此时res就指向add()的inner函数
// inner函数又可以访问到outer函数里的局部变量n
// 现在，[inner函数 + n] = 一个闭包
console.log(res);   // ?
/** result:
 * 之前返回的sol只是inner函数体，这里打印但没执行
 * f() {
 *       console.log(n);
 *     }
 */

// 如果想执行的话
res();
/** result: 2
 */

// ====================== example ======================
function test() {
    var n = 10;
    function add() {
        n++;
        console.log(n);
    }
    function reduce() {
        n--;
        console.log(n);
    }
    return [add, reduce];
}

var res = test();
console.log(res);   // 拿到的是函数体，只打印没执行
// 执行
//res[0]();           // 11
//res[0]();           // 12
//res[1]();           // 11 - !!! n变成了共享私有属性

// ====================== 分析原理 ======================
// 一上来创建时就有一个test1.EC，只扫描没执行，所以还没有AO
// 但test1.EC下面的scope chain已经存在，里面有的是当前环境下的scope chain
// (会做一个copy，然后放进test1.EC的scope chain里面)
// 此时test1下的scope chain和GEC下的scope chain是相等的
function test1() {
    var a = 1;

    function test2() {
        var b = 2;
        console.log(a);
    }
    return test2;       // 如果此处return test2()
}                       // 有了"()"就代表先执行，再return执行结果
var c = 3;

var test3 = test1();
console.log(test3);     // test2 function body
test3();                // 1

// Global EC 创建
// global.EC = {
//      VO : [ test1: function [ [[scope chain]] : [global.VO] ],
//             test3: undefined -> return value of global.VO.test1,
//             c: undefined -> 3]
//      [[scope chain]] : [global.VO]
// }

// scope chain是用来存放变量scope
// 实际就是一个像stack一样的链状结构，stack最底层就是最内部的inner函数
// 因为inner函数可以访问outer函数的变量，而outer函数不能访问inner函数的
// GEC scope chain里面存的是变量Object，指向GEC自己的的VO对象1

// 执行test1
var test3 = test1();
// 函数test1 EC 先创建再执行
// test1.EC = {
//      AO      : [ test2: func,
//                  a: undefined -> 1 ]
//      [[scope chain]] : [test1.AO, global.VO]
// }

// 函数test1 EC销毁，正常情况test1.AO会被销毁
// 但其实不会，因为test2还拽着test1.AO

// 执行test3
test3();
// 函数test3 EC 先创建再执行 (test3 = test2)
// test2(test3).EC = {
//      AO      :  [ b: 2,
//                   a: ? ]
//      [[scope chain]] : [test2.AO, test1.AO, global.VO]
// }

// 所有function的AO都是存在heap上面的，所以stack最底层inner function的
// scope chain会指向着所有inner function内部需要用到的outer function的变量
// 哪怕outer function被返回了，它之前所对应的AO此时还会有inner function的
// scope chain作为reference指着，所以它还能被访问，且不会被GC掉

// ====================== 练习 ======================
console.log("练习===== Lian Xi =====");

function practice1() {
    var arr = [];

    for(var i = 0; i < 4; i++) {
        arr[i] = function () {
            console.log(i);
        };
    }
    return arr;
}

var sol = practice1();
console.log(sol);
// result: array of function
// [ [function], [function], [function], [function] ]
// 每一个[function]的scope都储存着两个东西
// 1. practice1()中的变量i
// 2. Global EC

for(var j = 0; j < sol.length; j++) {
    sol[j]();
}
// result: [4, 4, 4, 4]
// why? practice1()中的for loop其实等价于:
//     var i = 0;
//     for(i < 4) {
//         arr[i] = function () {
//             console.log(i);
//         };
//         i++;
//     }
// arr[i]存放的是4个函数体，每一个函数都会打一个i
// 当跳出for loop循环时，i已经变成了4
// 每一个array i都是一个closure，当打印i的时候
// 读取的都是outer函数的i

// ====================== How to fix ======================
// 1. 可以把var改成let - let具有块级作用域
// 2. IIFE - 立即执行函数表达式 (函数体)(表示立即执行这个函数)
function practice2() {
    var arr = [];

    for (var i = 0; i < 4; i++) {
        (function (j) {             // 接受变量j (0,1,2,3)
            console.log(j);         // j和里面的函数也是一个闭包
            arr[j] = function () {  // 把函数push进array里
                console.log(j);     // 此时执行函数访问的是立即执行函数里的j
            }                       // 而不是i
        })(i); // 每个循环，立即执行
    }
    return arr;
}
// arr[j]中的每一个函数，它的scope chain下面有三项
// 1. Global EC
// 2. practice2()
// 3. 立即执行函数下的j

var sol = practice2();
console.log(sol);
for (var j = 0; j < sol.length; j++) {
    sol[j]();
}
// result: [0, 1, 2, 3]