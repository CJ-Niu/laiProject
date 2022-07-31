// ====================== DOM ======================
// DOM - document object model 文档对象模型 - HTML下的整个文档
// DOM -> 对象，由浏览器提供的，与JS无关，所以要注意兼容性的问题
//        由执行JS脚本的环境所提供的对象

// DOM作用： 通过浏览器提供的属性和方法取操作HTML，而不能操作CSS
//           (提供一些API的接口，来操作浏览器)

// Native Object - 本地对象
//      function, Object, Array, String...etc.
// Building Object - 内置对象
//      Global下
//              parseInt - 字符转换成数字
//      Math相关
// Host Object - 数组对象
//      DOM

// ----------------------------------------
div
{
    width: 100px;
    height: 100px;
    background - color : green;
}
// -----------------

// document - 包含整个HTML，是一个Object
// document = {
//      getElementById - 全局唯一，只能在document下使用
//      getElementByClassName
//      getElementByTagName - 拿到的是一个类数组(HTMLCollection)

// html5 引入的 ()
// querySelector & querySelectorAll
// querySelector(.选择器) or (.子选择器)