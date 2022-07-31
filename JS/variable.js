// ============= Declear variables =============
// var a = 3;
// var a = 4;

// let b = a;
// let b = 5;

// const c = 4;
// c = 5;

/**
 * Prefer let than var, why ?? see the difference below !!!
 * Prefer const than let
 * Do not use var in ES6
 */

// =============== let vs var (scopre) ===================
/**
 * var is function scope, let is block scope
 */
// function func1() {
//     for (let i = 0; i < 3; i++) {
//         console.log(i);
//     }
//     console.log(i);

//     for (var i = 0; i < 3; i++) {
//         console.log(i);
//     }
//     console.log(i);
// }
// func1();


// ============= let vs var (redeclaration) ===============
// var a = "this is a var";
// var a = "this is a var decleared again";
// console.log(a);

// let b = "this is a let";
// let b = "this is a let decleared again";
// console.log(b);

// ============= const ===============
/**
 * const can not be reassigned !!
 */
// const a = 100;
// a = 50;
// const a = "a";
// const a = "a again";
// console.log(a);
