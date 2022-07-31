//================ Dynamically vs. Staticlly Typed ===============

// Difference
/**
 * Dynamically: You do not have to write the type when you write your code, much fast to write script
 * language. Type will be infered during run-time.
 *
 * Statically: You have to write the type when you write your code, compiler will infer error if
 * data type is not a match
 */

// JS is Dynamiclly Typed and Weakly typed
/**
 * The bad news is JS, as a script language, is dynamically typed, and weakly typed, which means the
 * type is defined by the last assignment during the run time.
 *
 * This means it is very likely to introduce bug when using JS to develop a large application without
 * variable being properly typed.
 */

// Solution ? Just Properly Type it!!!
/**
 * TypeScript, Flow gives you option to type your code. Reduce the error before you actully run
 * your code!!!!
 */

//================ Data types in JS =================
/*
 * There are two types of data: Primitive and Reference
 */

/**
 * Primitive
 */
// let length = 15;                         // number
// let fullName = "Li Lei";                 // string
// let flag = true;                         // boolean
// let a = null;                            // null       存在但是是空的
// let b = undefined;                       // undefined  根本不存在

/**
 * Reference
 */
// let func = () => {};                        // function
// let name = {                                // object
//     lastName: 'Li',
//     firstName: 'Lei',
//     fullName: ""
// };
// let arr = [1, 2, 3];                        // arrary

// // console.log(name.fullName);              // undefined, not error!!!!
// console.log(name1);                      // not defined, error!!!
