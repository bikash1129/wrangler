# Data Prep

![cm-available](https://cdap-users.herokuapp.com/assets/cm-available.svg)
![cdap-transform](https://cdap-users.herokuapp.com/assets/cdap-transform.svg)
[![Build Status](https://travis-ci.org/cdapio/hydrator-plugins.svg?branch=develop)](https://travis-ci.org/cdapio/hydrator-plugins)
[![Coverity Scan Build Status](https://scan.coverity.com/projects/11434/badge.svg)](https://scan.coverity.com/projects/hydrator-wrangler-transform)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.cdap.wrangler/wrangler-core/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.cdap.wrangler/wrangler-core)
[![Javadoc](https://javadoc-emblem.rhcloud.com/doc/io.cdap.wrangler/wrangler-core/badge.svg)](http://www.javadoc.io/doc/io.cdap.wrangler/wrangler-core)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Join CDAP community](https://cdap-users.herokuapp.com/badge.svg?t=wrangler)](https://cdap-users.herokuapp.com?t=1)

A collection of libraries, a pipeline plugin, and a CDAP service for performing data
cleansing, transformation, and filtering using a set of data manipulation instructions
(directives). These instructions are either generated using an interactive visual tool or
are manually created.

- Learn the core [concepts](wrangler-docs/concepts.md)
- Data Prep Transform is [documented here](wrangler-transform/wrangler-docs/data-prep-transform.md)
- [Data Prep Cheatsheet](wrangler-docs/cheatsheet.md)

---

## 🚀 New Features

More info in [upcoming features](wrangler-docs/upcoming-features.md)

### ✅ User-Defined Directives (UDDs)
Allows developers to create custom directives for CDAP Wrangler.

- [How to create a custom directive](wrangler-docs/custom-directive.md)
- [Token types supported](../api/src/main/java/io/cdap/wrangler/api/parser/TokenType.java)
- [Directive migration guide](wrangler-docs/directive-migration.md)
- [Grammar documentation](wrangler-docs/grammar/grammar-info.md)
- [Directive internals guide](wrangler-docs/udd-internal.md)

---

## 🆕 Custom Directive: `aggregate-stats`

### Description:
A custom directive to **aggregate byte size and time duration** across rows. Useful for computing total size and total/average time from raw data.

### Syntax:
```wrangler
aggregate-stats :<byte_column> :<time_column> '<output_byte_column>' '<output_time_column>';
