/**
 * Main function of the test
 */
function testPerformance() {
    return {
        direct: getExecutionTime(testDirect),
        testFunction: getExecutionTime(testFunction),
        nonPrototype: getExecutionTime(testNonPrototype),
        prototypeChain: getExecutionTime(testPrototypeChain),
        prototype: getExecutionTime(testPrototype),
        longPrototypeChain: getExecutionTime(testLongPrototypeChain),
        prototypeWithGetter: getExecutionTime(testPrototypeWithGetter)

    }
}

/**
 * Value that will be used in functions.
 */
var DELTA = 10;

/**
 * Number of iterations^3
 */
var ITERATIONS = 1000;

function getExecutionTime(f) {
    var start = new Date().getTime();
    f();
    var end = new Date().getTime();
    var diff = end - start;
    return diff;
}

/**
 * Prototype chain with 5 children and property on parent object.
 * ChildE->ChildD->ChildC->ChildB->ChildA->Parent->delta
 */
function testPrototypeChain() {

    function Parent() { this.delta = DELTA; };

    function ChildA(){};
    ChildA.prototype = new Parent();
    function ChildB(){}
    ChildB.prototype = new ChildA();
    function ChildC(){}
    ChildC.prototype = new ChildB();
    function ChildD(){};
    ChildD.prototype = new ChildC();
    function ChildE(){};
    ChildE.prototype = new ChildD();

    function nestedFn() {
        var child = new ChildE();
        var counter = 0;
        for(var i = 0; i < ITERATIONS; i++) {
            for(var j = 0; j < ITERATIONS; j++) {
                for(var k = 0; k < ITERATIONS; k++) {
                    counter += child.delta;
                }
            }
        }
        console.log('Final result: ' + counter);
    }
    nestedFn();
}

/**
 * Prototype chain with 1 children and property on parent object
 * ChildA->Parent->delta
 */
function testPrototype() {
    function Parent() {  };
    Parent.prototype.delta = DELTA;

    function ChildA(){};
    ChildA.prototype = new Parent();

    function nestedFn() {
        var child = new Parent();
        var counter = 0;
        for(var i = 0; i < ITERATIONS; i++) {
            for(var j = 0; j < ITERATIONS; j++) {
                for(var k = 0; k < ITERATIONS; k++) {
                    counter += child.delta;
                }
            }
        }
        console.log('Final result: ' + counter);
    }

    nestedFn();
}

/**
 * Prototype chain with 5 children and method to get property on parent object
 * ChildE->ChildD->ChildC->ChildB->ChildA->Parent->getDelta()
 */
function testPrototypeWithGetter() {
    function Parent() {  };
    Parent.prototype.delta = DELTA;
    Parent.prototype.getDelta = function() {
        return this.delta;
    };

    function ChildA(){};
    ChildA.prototype = new Parent();
    function ChildB(){}
    ChildB.prototype = new ChildA();
    function ChildC(){}
    ChildC.prototype = new ChildB();
    function ChildD(){};
    ChildD.prototype = new ChildC();
    function ChildE(){};
    ChildE.prototype = new ChildD();

    function nestedFn() {
        var child = new ChildE();
        var counter = 0;
        for(var i = 0; i < ITERATIONS; i++) {
            for(var j = 0; j < ITERATIONS; j++) {
                for(var k = 0; k < ITERATIONS; k++) {
                    counter += child.getDelta();
                }
            }
        }
        console.log('Final result: ' + counter);
    }

    nestedFn();
}

/**
 * Property available on target object without prototype chain
 * Parent->delta
 */
function testNonPrototype() {
    function Parent() { this.delta = DELTA; };
    function nestedFn() {
        var child = new Parent();
        var counter = 0;
        for(var i = 0; i < ITERATIONS; i++) {
            for(var j = 0; j < ITERATIONS; j++) {
                for(var k = 0; k < ITERATIONS; k++) {
                    counter += child.delta;
                }
            }
        }
        console.log('Final result: ' + counter);
    }

    nestedFn();
}

/**
 * Property available on target object, but value is retrieved from helper/util function (non OOP style)
 * getDelta(object)
 */
function testFunction() {
    function getDelta(object) {
        return object.delta;
    }

    function nestedFn() {
        var child = {delta: DELTA};
        var counter = 0;
        for(var i = 0; i < ITERATIONS; i++) {
            for(var j = 0; j < ITERATIONS; j++) {
                for(var k = 0; k < ITERATIONS; k++) {
                    counter += getDelta(child);
                }
            }
        }
        console.log('Final result: ' + counter);
    }

    nestedFn();
}

/**
 * More complex prototype chain.
 * Parent object has object with prototype chain of 5 children, which contain target property.
 * Also parent object has 5 children.
 * So to access target object (Parent.sub.delta) following chain pass is required:
 * ChildE->ChildD->ChildC->ChildB->ChildA->Parent->SubChildE->SubChildD->SubChildC->SubChildB->SubChildA->SubParent->delta
 */
function testLongPrototypeChain() {
    function Parent() {  };
    Parent.prototype.sub = new SubChildE();
    function ChildA(){};
    ChildA.prototype = new Parent();
    function ChildB(){}
    ChildB.prototype = new ChildA();
    function ChildC(){}
    ChildC.prototype = new ChildB();
    function ChildD(){};
    ChildD.prototype = new ChildC();
    function ChildE(){};
    ChildE.prototype = new ChildD();
    function SubParent() { this.delta = DELTA; };
    function SubChildA(){};
    SubChildA.prototype = new SubParent();
    function SubChildB(){}
    SubChildB.prototype = new SubChildA();
    function SubChildC(){}
    SubChildC.prototype = new SubChildB();
    function SubChildD(){};
    SubChildD.prototype = new SubChildC();
    function SubChildE(){};
    SubChildE.prototype = new SubChildD();


    function nestedFn() {
        var child = new ChildE();
        var counter = 0;
        for(var i = 0; i < ITERATIONS; i++) {
            for(var j = 0; j < ITERATIONS; j++) {
                for(var k = 0; k < ITERATIONS; k++) {
                    counter += child.sub.delta;
                }
            }
        }
        console.log('Final result: ' + counter);
    }

    nestedFn();
}

/**
 * Test target value directly as local variable
 */
function testDirect() {
    function nestedFn() {
        var delta = DELTA;
        var counter = 0;
        for(var i = 0; i < ITERATIONS; i++) {
            for(var j = 0; j < ITERATIONS; j++) {
                for(var k = 0; k < ITERATIONS; k++) {
                    counter += delta;
                }
            }
        }
        console.log('Final result: ' + counter);
    }
    nestedFn();
}
