sandbox
=======

[![CircleCI](https://circleci.com/gh/brymck/sandbox-kt.svg?style=shield)](https://circleci.com/gh/brymck/sandbox-kt)
[![codecov](https://img.shields.io/codecov/c/gh/brymck/sandbox-kt)](https://codecov.io/gh/brymck/sandbox-kt)
[![ktlint](https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg)](https://ktlint.github.io/)

This is just a sandbox of some experiments I have in Kotlin.

Examples
-------

* `coroutines` - A proof of concept of how [Kotlin coroutines][coroutines] can be used to perform IO-intensive operations that would normally block a thread

Usage
-----

Clone and build the repo with the following:

```bash
git clone git@github.com:brymck/sandbox-kt.git
cd sandbox-kt
make
```

To run one of the examples, just pass its name to `make`:

```bash
make coroutines
```

[coroutines]: https://kotlinlang.org/docs/reference/coroutines-overview.html
