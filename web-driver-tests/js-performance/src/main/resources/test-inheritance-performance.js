function testPerformance() {
    return {
        prototypeChain: testPrototypeChain(),
        prototype: testPrototype(),
        nonPrototype: testNonPrototype(),
        testFunction: testFunction(),
        direct: testDirect(),
        longPrototypeChain: testLongPrototypeChain(),
        prototypeWithGeter: testPrototypeWithGeter()
    }
}

function testPrototypeChain() {
    var start = new Date().getTime();
    function Parent() { this.delta = 10; };

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
        for(var i = 0; i < 1000; i++) {
            for(var j = 0; j < 1000; j++) {
                for(var k = 0; k < 1000; k++) {
                    counter += child.delta;
                }
            }
        }
        console.log('Final result: ' + counter);
    }

    nestedFn();
    var end = new Date().getTime();
    var diff = end - start;
    return diff;
}


function testPrototype() {
    var start = new Date().getTime();
    function Parent() {  };
    Parent.prototype.delta = 10;

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
        var child = new Parent();
        var counter = 0;
        for(var i = 0; i < 1000; i++) {
            for(var j = 0; j < 1000; j++) {
                for(var k = 0; k < 1000; k++) {
                    counter += child.delta;
                }
            }
        }
        console.log('Final result: ' + counter);
    }

    nestedFn();
    var end = new Date().getTime();
    var diff = end - start;
    return diff;
}

function testPrototypeWithGeter() {
    var start = new Date().getTime();
    function Parent() {  };
    Parent.prototype.delta = 10;
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
        for(var i = 0; i < 1000; i++) {
            for(var j = 0; j < 1000; j++) {
                for(var k = 0; k < 1000; k++) {
                    counter += child.getDelta();
                }
            }
        }
        console.log('Final result: ' + counter);
    }

    nestedFn();
    var end = new Date().getTime();
    var diff = end - start;
    return diff;
}

function testNonPrototype() {
    var start = new Date().getTime();
    function Parent() { this.delta = 10; };

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
        var child = new Parent();
        var counter = 0;
        for(var i = 0; i < 1000; i++) {
            for(var j = 0; j < 1000; j++) {
                for(var k = 0; k < 1000; k++) {
                    counter += child.delta;
                }
            }
        }
        console.log('Final result: ' + counter);
    }

    nestedFn();
    var end = new Date().getTime();
    var diff = end - start;
    return diff;
}

function testFunction() {
    var start = new Date().getTime();
    function getDelta(object) {
        return object.delta;
    }

    function nestedFn() {
        var child = {delta: 10};
        var counter = 0;
        for(var i = 0; i < 1000; i++) {
            for(var j = 0; j < 1000; j++) {
                for(var k = 0; k < 1000; k++) {
                    counter += getDelta(child);
                }
            }
        }
        console.log('Final result: ' + counter);
    }

    nestedFn();
    var end = new Date().getTime();
    var diff = end - start;
    return diff;
}






function testLongPrototypeChain() {
    var start = new Date().getTime();
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

    function SubParent() { this.delta = 10; };

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
        for(var i = 0; i < 1000; i++) {
            for(var j = 0; j < 1000; j++) {
                for(var k = 0; k < 1000; k++) {
                    counter += child.sub.delta;
                }
            }
        }
        console.log('Final result: ' + counter);
    }

    nestedFn();
    var end = new Date().getTime();
    var diff = end - start;
    return diff;
}



function testPrototypeChain() {
    var start = new Date().getTime();
    function Parent() { this.delta = 10; };

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
        for(var i = 0; i < 1000; i++) {
            for(var j = 0; j < 1000; j++) {
                for(var k = 0; k < 1000; k++) {
                    counter += child.delta;
                }
            }
        }
        console.log('Final result: ' + counter);
    }

    nestedFn();
    var end = new Date().getTime();
    var diff = end - start;
    return diff;
}





function testDirect() {
    var start = new Date().getTime();


    function nestedFn() {
        var delta = 10;
        var counter = 0;
        for(var i = 0; i < 1000; i++) {
            for(var j = 0; j < 1000; j++) {
                for(var k = 0; k < 1000; k++) {
                    counter += delta;
                }
            }
        }
        console.log('Final result: ' + counter);
    }

    nestedFn();
    var end = new Date().getTime();
    var diff = end - start;
    return diff;
}

return testPerformance();