---
title:      "Ignoring Tests"
date:       2015-07-26
excerpt:    "Incremental improvement: ignoring tests."
categories: testing
tags:       unit testing
layout:     post
---

{% include commit.html ref="fd9028f3c3841be02fa6d4b225d670a41cae8268" %}

In the course of making a formatter for our lambda calculus terms, I stumbled on a situation where the ability to ignore a test was needed. Let's see how to add "ignored" tests to our testing framework.

Similar to JUnit, I've added an [`@Ignore`](https://github.com/alexleighton/alonzo-java/blob/fd9028f3c3841be02fa6d4b225d670a41cae8268/tst/alonzo/unit/Ignore.java) annotation for marking tests as ignored. Next, we need to a new kind of TestResult:

{% highlight java %}
{% github_sample alexleighton/alonzo-java/blob/fd9028f3c3841be02fa6d4b225d670a41cae8268/tst/alonzo/unit/TestResult.java 144 151 %}
{% endhighlight %}
{% include github_sample_ref.html ref="fd9028f3c3841be02fa6d4b225d670a41cae8268" file="/tst/alonzo/unit/TestResult.java" s=144 e=151 %}

[`RunnableTest`](https://github.com/alexleighton/alonzo-java/blob/fd9028f3c3841be02fa6d4b225d670a41cae8268/tst/alonzo/unit/RunnableTest.java) needs to be changed in order to understand ignored tests. When making the change, I chose to not construct the test class instance or run the test method, opting for an early return with no work done. Further, as this addition made [`RunnableTest.get()`](https://github.com/alexleighton/alonzo-java/blob/fd9028f3c3841be02fa6d4b225d670a41cae8268/tst/alonzo/unit/RunnableTest.java#L66) too large for my taste, I refactored construction of the test class instance into a method.

{% highlight java %}
{% github_sample alexleighton/alonzo-java/blob/fd9028f3c3841be02fa6d4b225d670a41cae8268/tst/alonzo/unit/RunnableTest.java 66 70 %}
{% endhighlight %}
{% include github_sample_ref.html ref="fd9028f3c3841be02fa6d4b225d670a41cae8268" file="/tst/alonzo/unit/RunnableTest.java" s=66 e=70 %}

{% highlight java %}
{% github_sample alexleighton/alonzo-java/blob/fd9028f3c3841be02fa6d4b225d670a41cae8268/tst/alonzo/unit/RunnableTest.java 88 90 %}
{% endhighlight %}
{% include github_sample_ref.html ref="fd9028f3c3841be02fa6d4b225d670a41cae8268" file="/tst/alonzo/unit/RunnableTest.java" s=88 e=90 %}

Finally, at the top level of [`TestRunner`](https://github.com/alexleighton/alonzo-java/blob/fd9028f3c3841be02fa6d4b225d670a41cae8268/tst/alonzo/unit/TestRunner.java#L35), all that's needed is a simple collection of ignored test results and the addition of an ignored section in the overall result output.

{% highlight java %}
{% github_sample alexleighton/alonzo-java/blob/fd9028f3c3841be02fa6d4b225d670a41cae8268/tst/alonzo/unit/TestRunner.java 45 46 %}
{% endhighlight %}
{% include github_sample_ref.html ref="fd9028f3c3841be02fa6d4b225d670a41cae8268" file="/tst/alonzo/unit/TestRunner.java" s=45 e=46 %}


A good test of any software system is how easy it is to adjust the system incrementally. I'm pleased with how easy it was to adjust our testing framework in this way.


