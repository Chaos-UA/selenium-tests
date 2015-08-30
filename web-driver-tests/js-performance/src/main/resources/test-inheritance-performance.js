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
var DELTA = 10;
var ITERATIONS = 1000;

function getExecutionTime(f) {
    var start = new Date().getTime();
    f();
    var end = new Date().getTime();
    var diff = end - start;
    return diff;
}

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
