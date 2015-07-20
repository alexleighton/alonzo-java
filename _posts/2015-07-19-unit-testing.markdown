---
title:      "Unit Testing Framework Foundations"
date:       2015-07-19
excerpt:    "Minimal unit testing framework, driven from the top down."
categories: testing
tags:       unit testing
layout:     post
---

## Testing

I'm a big fan of automated testing, for many reasons. It provides the easiest and most reliable way to see if your code is working, aside from strong static typing. Further, in my experience, driving interface design from the perspective of making it easy to test tends to make for good interfaces.

In order to test our interpreter, it will be helpful to have a testing framework which will automate the process of running tests, and collecting and presenting test results. This isn't strictly necessary, as we could just ad-hoc design our tests, with a `println` indicating success here and a `resultCode` indicating failure there. I think we'll have more confidence in our results if they're all delivered the same way, through the same testing mechanisms.

Now if you've ever designed a testing framework, you'll have quickly realized that like every other piece of software, having automated tests to ensure your testing framework still works is important. But we've got a bit of a quandary. Testing your testing framework seems valuable, but also like we're getting into a "turtles all the way down" situation. At some point, you have to trust that the code you're writing is correct. In our case, I started with a dumb test to be driven by the framework, essentially testing correctness of the testing framework manually.


The first test:
{% highlight java %}
{% github_sample alexleighton/alonzo-java/blob/add2fe75a054079bad27765233a1bd566d7a3ebe/tst/alonzo/FooTest.java 5 11 %}
{% endhighlight %}
{% include github_sample_ref.html ref="add2fe75a054079bad27765233a1bd566d7a3ebe" file="/tst/alonzo/FooTest.java" s=5 e=11 %}


The call to the test runner:
{% highlight java %}
{% github_sample alexleighton/alonzo-java/blob/add2fe75a054079bad27765233a1bd566d7a3ebe/tst/alonzo/TestingMain.java 6 10 %}
{% endhighlight %}
{% include github_sample_ref.html ref="add2fe75a054079bad27765233a1bd566d7a3ebe" file="/tst/alonzo/TestingMain.java" s=6 e=10 %}


I started with these two classes before writing any of the testing framework code. In general, I believe in the value of test-driven development. Without going too much into TDD and its positives and negatives, here's a benefit I've found from practicing TDD. Driving the code from a test usually results in a better, more usable interface. So, in this instance, I wrote the test the way I wanted to write a test, and wrote the code to run the test in a way that seemed easy to run tests. Honestly, this top-down thinking is one of the hardest things for new developers to really grok and practice. It is key in choosing your abstractions well and in writing interfaces that you actually would want to use.

So far, the architecture of the testing framework is relatively simple. The [`TestRunner`](https://github.com/alexleighton/alonzo-java/blob/add2fe75a054079bad27765233a1bd566d7a3ebe/tst/alonzo/unit/TestRunner.java) collects tests, then runs them, collecting the [`TestResults`](https://github.com/alexleighton/alonzo-java/blob/add2fe75a054079bad27765233a1bd566d7a3ebe/tst/alonzo/unit/TestResult.java), and finally displaying those results. In order to run the tests, the `TestRunner` makes use of the [`RunnableTest`](https://github.com/alexleighton/alonzo-java/blob/add2fe75a054079bad27765233a1bd566d7a3ebe/tst/alonzo/unit/RunnableTest.java) class, which does all the heavy lifting. It searches the class for any methods labeled with the [`@Test`](https://github.com/alexleighton/alonzo-java/blob/add2fe75a054079bad27765233a1bd566d7a3ebe/tst/alonzo/unit/Test.java) annotation, and the constructor. `RunnableTest` implements the [`Supplier<TestResult>`](https://docs.oracle.com/javase/8/docs/api/java/util/function/Supplier.html) interface, by running the wrapped test and constructing a result. `RunnableTest` works in concert with the static methods in [`Assert`](https://github.com/alexleighton/alonzo-java/blob/add2fe75a054079bad27765233a1bd566d7a3ebe/tst/alonzo/unit/Assert.java) which throw [`AssertionFailureException`](https://github.com/alexleighton/alonzo-java/blob/add2fe75a054079bad27765233a1bd566d7a3ebe/tst/alonzo/unit/AssertionFailureException.java) to indicate a test failure.

Now we have a testing framework with which to test the code we write for the interpreter.
