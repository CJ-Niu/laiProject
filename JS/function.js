// ================= Function in JS ================
/**
 * Function is first class citizen in JS. It means you can use it like a variable, you can
 * use it anywhere.
 */

/**
 * Using 'function' keyword
 */
// function test1() {
//     console.log("test1");
// }
// test1();


/**
 * Anonymous function
 */

// example 1:
// const func1 = function(val) {
//     console.log(val)
// }
// const arr = [1, 2, 3];
// arr.forEach(func1);

// example 2:
// const arr = [1, 2, 3];
// arr.forEach(function(val) {
//     console.log(val)
// });


/**
 * Arrow Function (introduced in ES6)
 * Why we want it? 1). Shorter functions 2) auto bind of this
 */
const arr = [1,2,3];
arr.map((val) => {console.log(val)});
