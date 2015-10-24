---
title:      "Introduction to the Lambda Calculus"
date:       2015-10-23
excerpt:    "Defining the syntax of the Lambda Calculus, with a quick discussion of semantics."
categories: "lambda-calculus"
tags:       lambda-calculus syntax
layout:     post
---

{% include commit.html ref="989223b15bb40f8a7461019a31469752a11ea9bf" %}


The [Lambda Calculus](https://en.wikipedia.org/wiki/Lambda_calculus) (λ-calculus) is a theoretical programming language originally used as part of an investigation into the foundations of mathematics. [Alonzo Church](https://en.wikipedia.org/wiki/Alonzo_Church) (the namesake of this project) later published a paper on just the computational part of the λ-calculus. Our project here is to implement a full-featured interpreter for this simple programming language.

## Syntax

The λ-calculus has a very simple syntax, making it a great first programming language to implement. There are only 3 forms in the expression-based syntax:

```
<expr> ::= <var> | <function-definition> | <function-application>

<function-definition> ::= '(λ' <var> '.' <expr> ')'

<function-application> ::= '(' <expr> ' ' <expr> ')'

<var> ::= [^()λ. ]+
```

This basic [BNF](https://en.wikipedia.org/wiki/Backus%E2%80%93Naur_Form) description of the syntax can be read as an expression is defined as the following 3 forms. First, an expression can be a `<var>`, which is defined as a series of characters not including any of `'('`, `')'`, `'λ'`, `'.'`, or `' '`. Second, an expression can be a `<function-definition>`, which consists of the characters `'(λ'`, followed by a `<var>`, followed by the character `'.'`, followed by an `<expr>`, followed by the character `')'`. Lastly, an expression can be a `<function-application>`, which consists of the character `'('`, followed by an `<expr>`, followed by the character `' '`, followed by an `<expr>`, followed by the character `')'`.

This definition comprises the entirety of the syntax of the language. When we get to defining the parser for this language, we will very likely extend this definition to include whitespace between many of the syntax pieces, as extra whitespace enhances readability for humans. For now though, this definition will suffice for giving you a feeling for the different forms the language can take.


### Variable Reference

A λ-calculus variable reference is similar in nature to the variable references you are used to in more mainstream programming languages. Like Java for instance:

{% highlight java %}
int varName = 5;
System.out.println("Value = " + varName);
{% endhighlight %}

In the code example above, we see the assignment of the value `5` to the variable `varName`, and then a reference to the variable on the next line where `varName` is concatenated to the string `"Value = "`. Like the second line in the Java example, a variable reference syntax form indicates a reference to a variable that was defined "previously" in the λ-calculus program.

We represent a λ-calculus variable like so:

{% highlight java %}
{% github_sample alexleighton/alonzo-java/blob/989223b15bb40f8a7461019a31469752a11ea9bf/src/alonzo/ast/Var.java 2 17 %}
{% endhighlight %}
{% include github_sample_ref.html ref="989223b15bb40f8a7461019a31469752a11ea9bf" file="/src/alonzo/ast/Var.java" s=2 e=17 %}

Our `Var` has a container for the textual name of the variable, and a way of accessing that name. A key implementation detail here is that I've made `Var` an immutable object, in order to allow referencing the object anywhere (even appearing multiple times in the same tree).

### Function Definition

The Lambda Calculus gets its name from the lambda (λ) symbol in the definition of a function. This is also where many mainstream functions get the name `lambda` for referring to an anonymous-function-as-a-value. Functions in the λ-calculus are a simplified version of the functions you are used to in other languages. These functions only have one parameter, with the body of the function being another expression. Simplicity is the reason for the season with the λ-calculus.

Multi-parameter functions can be simulated: `int foo(int x, int y, int z) { return z; }` becomes `(λx.(λy.(λz.z)))` in the λ-calculus. As can be seen in the example, λ-calculus functions are anonymous (not named), and a function of three parameters is instead a function of one parameter, returning a function of one parameter, returning a function of one parameter, returning the final result.

We represent a λ-calculus function as an immutable container for the `Var` parameter, and the `ASTNode` body:

{% highlight java %}
{% github_sample alexleighton/alonzo-java/blob/989223b15bb40f8a7461019a31469752a11ea9bf/src/alonzo/ast/Fun.java 2 23 %}
{% endhighlight %}
{% include github_sample_ref.html ref="989223b15bb40f8a7461019a31469752a11ea9bf" file="/src/alonzo/ast/Fun.java" s=2 e=23 %}


### Function Application

Function application is the third form a λ-calculus expression can take. It represents the application of a function to one argument. In Java this looks like `someFunction(someArgument)`. In the λ-calculus it looks like `(someFunction someArgument)`. For the sake of removing ambiguity, the application is parenthesized, as "`expr1 expr2 expr3`" could mean `expr3` is the argument to `expr2` and the result is the argument to `expr1`.

We represent a λ-calculus function application as an immutable container for the `ASTNode` function, and the `ASTNode` argument:

{% highlight java %}
{% github_sample alexleighton/alonzo-java/blob/989223b15bb40f8a7461019a31469752a11ea9bf/src/alonzo/ast/App.java 2 24 %}
{% endhighlight %}
{% include github_sample_ref.html ref="989223b15bb40f8a7461019a31469752a11ea9bf" file="/src/alonzo/ast/App.java" s=2 e=24 %}

The reason both the left and right sides of the application are `ASTNode`s is that, due to the semantics of the language, the left side may only become a function through runtime interpretation of the program.


## Semantics

Computation in the λ-calculus is achieved through variable substitution and structural interpretation of the resulting functions. What I mean here is that computation is performed by substituting expressions for variables where possible until there are no more substitutions possible, and then inspecting the resulting expression structure to get an "answer". We'll talk more about the semantics of the language once it's time to implement evaluation. For now, here's an example program and the result of evaluating it:

```
((λn.(λf.(λx.(n ((n f) x))))) (λf.(λx.x)))
```
evaluates to:

```
(λf.(λx.(f x)))
```

Here, we see the "successor" function `(λn.(λf.(λx.(n ((n f) x)))))` given the argument `(λf.(λx.x))`, the number 0. The result `(λf.(λx.(f x)))` is the number 1. Don't stress out too much on how these particular representations of `+1` and `0` were picked, just learn from this as a real-world example of a small λ-calculus program. We will cover these representations and evaluation strategies later. Suffice it to say that we take the `0` function and substitute it in the `+1` function everywhere the parameter `n` is, and continue substituting until we get `1`.

To be continued...
