---
title: "An Intuitive Formatter"
date:    2015-10-24
excerpt:    "Implementing a formatter/pretty-printer for the syntax we've defined."
categories: "formatting"
tags:       lambda-calculus syntax formatting
layout:     post
---

{% include commit.html ref="1f0b04e71923642f3b8d654390591970c7632d57" %}


One of the first things I do when I define the syntax for a new language is implement a formatter — code to produce a textual representation of expressions in the language. Humans are intensely visual, so having a way to look at the structures you are constructing in code is a great way of aiding understanding. Further, since this a programming language we're creating, the ability to print expressions is necessary for the interpreter. If you haven't read [yesterday's post]({{ page.previous.url | prepend: site.baseurl }}) and are unfamiliar with the lambda calculus, I recommend reading it before proceeding.

## Formatting

The interface our formatter needs to implement is as follows: given an [`OutputStream`](https://docs.oracle.com/javase/8/docs/api/java/io/OutputStream.html), output a string representation of the given [`ASTNode`](https://github.com/alexleighton/alonzo-java/blob/1f0b04e71923642f3b8d654390591970c7632d57/src/alonzo/ast/ASTNode.java).

{% highlight java %}
{% github_sample alexleighton/alonzo-java/blob/1f0b04e71923642f3b8d654390591970c7632d57/src/alonzo/ast/fmt/Formatter.java 25 33 %}
{% endhighlight %}
{% include github_sample_ref.html ref="1f0b04e71923642f3b8d654390591970c7632d57" file="/src/alonzo/ast/fmt/Formatter.java" s=25 e=32 %}


## Formatting Var

Remember that the definition of a variable ([`Var`](https://github.com/alexleighton/alonzo-java/blob/1f0b04e71923642f3b8d654390591970c7632d57/src/alonzo/ast/Var.java)) is a sequence of characters, not including the punctuation used for other expression types:

```
<var> ::= [^()λ. ]+
```

The test for our formatter is likewise simple. When we have a variable containing a name, the string representation should just be the name. <small>For now, ignore that we're testing 2 different formatters here, this post only concerns the recursive formatter.</small>

{% highlight java %}
{% github_sample alexleighton/alonzo-java/blob/1f0b04e71923642f3b8d654390591970c7632d57/tst/alonzo/ast/fmt/FormatterTest.java 18 24 %}
{% endhighlight %}
{% include github_sample_ref.html ref="1f0b04e71923642f3b8d654390591970c7632d57" file="/tst/alonzo/ast/fmt/FormatterTest.java" s=18 e=24 %}

Given the quite simple definition of `Var`, formatting it is a one-liner:

{% highlight java %}
{% github_sample alexleighton/alonzo-java/blob/1f0b04e71923642f3b8d654390591970c7632d57/src/alonzo/ast/fmt/RecursiveFormatter.java 26 28 %}
{% endhighlight %}
{% include github_sample_ref.html ref="1f0b04e71923642f3b8d654390591970c7632d57" file="/src/alonzo/ast/fmt/RecursiveFormatter.java" s=26 e=28 %}


## Formatting Fun

Formatting functions is a bit more tricky than formatting variables. Notice the recursive definition of a function as being an expression that contains another expression:

```
<function-definition> ::= '(λ' <var> '.' <expr> ')'
```

But first, the test. Straightforward, as usual:

{% highlight java %}
{% github_sample alexleighton/alonzo-java/blob/1f0b04e71923642f3b8d654390591970c7632d57/tst/alonzo/ast/fmt/FormatterTest.java 26 32 %}
{% endhighlight %}
{% include github_sample_ref.html ref="1f0b04e71923642f3b8d654390591970c7632d57" file="/tst/alonzo/ast/fmt/FormatterTest.java" s=26 e=32 %}

The recursive definition of [`Fun`](https://github.com/alexleighton/alonzo-java/blob/1f0b04e71923642f3b8d654390591970c7632d57/src/alonzo/ast/Fun.java) makes defining the formatter in a recursive manner a really natural and intuitive choice. So, to format a function, we output the punctuation of the function, then recursively call format on the substructures: the parameter and the body of the function:

{% highlight java %}
{% github_sample alexleighton/alonzo-java/blob/1f0b04e71923642f3b8d654390591970c7632d57/src/alonzo/ast/fmt/RecursiveFormatter.java 39 45 %}
{% endhighlight %}
{% include github_sample_ref.html ref="1f0b04e71923642f3b8d654390591970c7632d57" file="/src/alonzo/ast/fmt/RecursiveFormatter.java" s=39 e=45 %}

## Formatting App

Function application ([`App`](https://github.com/alexleighton/alonzo-java/blob/1f0b04e71923642f3b8d654390591970c7632d57/src/alonzo/ast/App.java)) is formatted in much the same way function was. Again, `App` is recursively defined as being composed of other expressions:

```
<function-application> ::= '(' <expr> ' ' <expr> ')'
```

The test for formatting `App` makes use of all the other `ASTNode`s, asking the formatter to format an application of the identity function to a free variable `y`.

{% highlight java %}
{% github_sample alexleighton/alonzo-java/blob/1f0b04e71923642f3b8d654390591970c7632d57/tst/alonzo/ast/fmt/FormatterTest.java 34 40 %}
{% endhighlight %}
{% include github_sample_ref.html ref="1f0b04e71923642f3b8d654390591970c7632d57" file="/tst/alonzo/ast/fmt/FormatterTest.java" s=34 e=40 %}

The implementation is very similar to `Fun`, outputting the relevant punctuation, then recursively calling format on the function and argument substructures.

{% highlight java %}
{% github_sample alexleighton/alonzo-java/blob/1f0b04e71923642f3b8d654390591970c7632d57/src/alonzo/ast/fmt/RecursiveFormatter.java 47 53 %}
{% endhighlight %}
{% include github_sample_ref.html ref="1f0b04e71923642f3b8d654390591970c7632d57" file="/src/alonzo/ast/fmt/RecursiveFormatter.java" s=47 e=53 %}


## Putting It All Together

With all the pieces of [`RecursiveFormatter`](https://github.com/alexleighton/alonzo-java/blob/1f0b04e71923642f3b8d654390591970c7632d57//src/alonzo/ast/fmt/RecursiveFormatter.java) together, we can format arbitrarily complicated lambda calculus expressions. I've added a "complicated" test to the suite showing that our formatter can format the [Y-combinator](https://en.wikipedia.org/wiki/Fixed-point_combinator#Fixed_point_combinators_in_lambda_calculus):

{% highlight java %}
{% github_sample alexleighton/alonzo-java/blob/1f0b04e71923642f3b8d654390591970c7632d57/tst/alonzo/ast/fmt/FormatterTest.java 42 49 %}
{% endhighlight %}
{% include github_sample_ref.html ref="1f0b04e71923642f3b8d654390591970c7632d57" file="/tst/alonzo/ast/fmt/FormatterTest.java" s=42 e=49 %}

_**Next time on Alonzo**_: they taught you not to use recursion in Java; what to do when your stack gets blown?
Stay tuned to find out.
