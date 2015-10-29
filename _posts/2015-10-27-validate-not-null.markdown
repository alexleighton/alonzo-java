---
title:      "Validate.notNull"
date:       2015-10-27
excerpt:    ""
categories: "brief"
tags:       brief preconditions
layout:     post
brief:      true
commit:     a9f345db226c3f434487495b58d8a24fd378f007
---

I've added a utility class for Validating preconditions, heavily inspired by the [`Validate`](https://commons.apache.org/proper/commons-lang/javadocs/api-3.4/org/apache/commons/lang3/Validate.html#notNull(T,%20java.lang.String,%20java.lang.Object...)) class found in [Apache Commons](https://commons.apache.org/proper/commons-lang/).

{% highlight java %}
{% github_sample alexleighton/alonzo-java/blob/a9f345db226c3f434487495b58d8a24fd378f007/src/alonzo/common/Validate.java 11 26 %}
{% endhighlight %}
{% include github_sample_ref.html ref="a9f345db226c3f434487495b58d8a24fd378f007" file="/src/alonzo/common/Validate.java" s=11 e=26 %}

Semantic correctness is an important part of software design for me. I use strong typing because it's a cheap (cheaper than finding them at runtime) way of discovering and preventing bugs. In keeping with this ideology, I believe in failing fast. Discovering a bug or error during the running of a program as soon as it appears makes debugging easier (due to quicker root-causing). Ensuring that objects are constructed correctly or that objects to be used abide by expectations makes writing logic around them easier.

I use Validate to ensure that expected preconditions hold, or else fail immediately. Typically I will use Validate assertions to guarantee that an object is correct on construction, or else fail construction. This makes it harder to pass around broken objects, another aspect of failing fast. So far I've only needed to check for null. However, it is highly likely that I will be adding to this class in the future.
