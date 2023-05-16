## Quarkus CLI Plugins

![jbang](/src/jbang.png "jbang")

[Ioannis Canellos](https://iocanel.com)

---

## The Quarkus CLI evolution

- 2019 version 0.11.0 
- 2023 version 3.0.3 

|||

### v0.11.0

- 3 commands

![quarkus --help in 0.11.0](/src/quarkus-help-0.11.0.png "quarkus --help in 0.11.0")

|||

### v3.0.3

- 32 spread in 6 sub CLI

![quarkus --help in 3.0.3](/src/quarkus-help-3.0.3.png "quarkus --help in 3.0.3")

|||

### The future ?

We need optional / pluggable subcommands

---

## Quarkus CLI Plugins

Optional subcommands
- Multiple sources
  - executables
  - jbang
    - java source files
    - jars
    - maven coordinates

---

![jbang](/src/jbang.png "jbang")

|||

## Java is cool but ...

![neck snap](/src/neck.gif "neck snap")

- compile and run
- package & directory structure
- dealing with dependencies classpath

|||

## Java

![java poster child of carpal tunnel syndrome](/src/java-carpal-tunnel.png "java poster child of carpal tunnel syndrome")

" - Doom Emacs configuration files!"

|||

## No, there is another

.. way of writing simple java programs!

![jbang tweet](/src/jbang-tweet.png "jbang tweet")

|||

## Hello JBang

```java
//usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS com.github.lalyos:jfiglet:0.0.9
import com.github.lalyos.jfiglet.FigletFont;

public class hello {
    public static void main(String[] args) throws Exception {
        String asciiArt = FigletFont
         .convertOneLine("Hello " + args[0]);
        System.out.println(asciiArt);
    }
}
```

|||

## A whole new level
- Multiple sources
  - local files
  - URL
  - Git repositories
  - Maven artifacts
  - gist
- Aliases
- Remote catalogs
  - group and distribute
  - important for Quarkus

---

## More demo ...

- Teach how to use CLI plugins
- Inspire with ideas for your plugins

|||

# JBang Hello example

- Show how to use jbang hello.java
- Show how to use ./hello.java
- Show how to use as plugin (quarkus hello)
  - quarkus --help
  - quarkus plug add hello.java
  - show catalog ?

|||

# Introduction to plugin catalogs
- Show quakrusio catalog
  - quarkus plug list --installable
- Add Max's jbang catalog
- Use explain with hello.java 
- Use explain with present.java

|||

# Local vs Global Plugins
- Add explain again to user scope
  - explain the difference
- Move to root
- quarkus plug list
  - show the 

|||

# Wipe global catalog and jbang
- Demonstrate how jbang is automatically installed

|||

# Ideas
- Add iocanel catalog
- quarkus create from-quickstart
  - quarkus-helm demo
- quarkus create from-github
- quarkus generate-entity
- quarkus generate-data
