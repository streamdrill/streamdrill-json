# streamdrill-json

Version 1.0 (initial public release)

A wrapper for representing, parsing and serialization of Scala and Java types as JSON.
This library uses the [json-smart parser](https://github.com/netplex/json-smart-v1) for
parsing but provides its own fast json serialization.

BUILDING
========

This is a standard maven project, so type "mvn package" to create the jar file.

In order to use the library in your project, add the following dependencies to
your pom.xml:

```HTML
<dependency>
    <groupId>streamdrill</groupId>
    <artifactId>streamdrill-json</artifactId>
    <version>1.0</version>
</dependency>
```

streamdrill-json is written in Scala and currently uses version 2.11.2.

LICENSE
=======

Copyright (c) 2015, streamdrill UG (haftungsbeschränkt)
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this
   list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice,
   this list of conditions and the following disclaimer in the documentation
   and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

