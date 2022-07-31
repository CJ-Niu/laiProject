//=============== Scopes in JS ===================
/**
 * In JS, there are 3 types of scope,
 * 1) Global Scope: can be visited anywhere in this JS file
 * 2) Local Scope:
 *      2.1 Function Scope: can be visited anywhere in this function
 *      2.2 Block Scope: can be visited only in this block
 */


//=============== Scope Demo1 =====================
// function foo1(){  //global scope
//     //local scope 1
//     const a = "a";

//     function foo2() {
//         //local scope 2
//         const b = "b";
//     }
// }

// function foo3(){  //global scope
//     //local scope 3
// }

//=============== Scope Demo2 =====================
// let a = 1; // global scope

// function func1() {
//     console.log("a: ", a);

//     let b = 2;
//     function func2() {
//         let c = 3;
//         console.log("b: ", b);
//     }
//     func2();
//     console.log("c: ", c);
// }
// func1();   // what will be printed?


//=============== Scope Demo3 ===============
// if (true) {
//     let b = 2;
// }
// console.log("b", b);


/**
 * Question Time: Why Inner function can access the outter function defined varible??
 * closure
 */