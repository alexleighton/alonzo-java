---
title:      "Format-String All the Things!"
date:       2015-10-29
excerpt:    ""
categories: "brief"
tags:       brief format-strings exceptions
layout:     post
brief:      true
commit:     1f0b04e71923642f3b8d654390591970c7632d57
---

I've picked up a practice recently: any time I need to provide a message (typically for errors or logging), I make sure the function in question takes `varargs` and then pass them to [`String.format`](http://docs.oracle.com/javase/8/docs/api/java/lang/String.html#format-java.lang.String-java.lang.Object...-). Most of the time I provide a message, I eventually want to use a format-string and it's just lame to have to call `String.format` every time.

{% highlight java %}
{% github_sample alexleighton/alonzo-java/blob/1f0b04e71923642f3b8d654390591970c7632d57/tst/alonzo/unit/RunnableTest.java 83 83 %}
{% endhighlight %}
{% include github_sample_ref.html ref="1f0b04e71923642f3b8d654390591970c7632d57" file="/tst/alonzo/unit/RunnableTest.java" s=83 e=83 %}
