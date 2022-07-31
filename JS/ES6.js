let a = 1,
    b = 2,
    c = 3;

let [a, b, c] = [1, 2, 3];

let [a, b, ...rest] = [10, 20, 30, 40, 50];
console.log(rest); // [30, 40, 50]
console.log(a, b); // 10, 20

// 对象解构
let {a : va, b : vb} = {a : 1, b : 2};
console.log(va, vb);    // 1, 2

let {a, b, ...rest} = {a : 1, b : 2, c : 3, d : 4, f : 5};
console.log(a, b);  // 1, 2
console.log(rest);  // {c : 3, d : 4, f : 5}    rest是个对象



