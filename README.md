# Kansi

A simple console text styling library for Kotlin.

## Installing

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("net.exoad:kansi:1.0")
}
```

## Usage

A minimal example using the `String.kansi` extension:

```kotlin
import net.exoad.kansi.*

fun main() {
    // prints "Hello, Kansi!" in green and bold (if your terminal supports ANSI)
    println("<fg:green bold>Hello, Kansi!</fg:green bold>".kansi)
}
```

## Specifications

### String Templating

This method involves directly injecting the tags into the string for processing instead of using a more discrete method such as direct
function calling or direct injection of the tags via string concatenation.

String templating is done via the following syntax:

```
<tag1 tag2 tag3>string content</tag1 tag2 tag3>
```

It resembles XML/HTML markup, but instead of having attributes and only one name per node, there are allowed to be multiple tag names per node.


#### Elements

**Nodes**

Nodes are represented by the opening and closing angle brackets in the following syntax:

```
<tag-name-expr>
</tag-name-expr>
```

Nodes are allowed to be given tags to them to give them a value, more on that below.

This separates them into 2 categories:
1. Opening Node - Represented without a forward slash
2. Closing Node - Represented with a forward slash

Nodes can be nested with each other to form nested nodes:

```
<a>
    <b>
        <c>
</a b c>
```

> Note: Here you see that the closing node has all 3 opening tagged nodes within, this simplifies the expression of having to explicitly close each one individually.
> Additionally, you can also ignore closing nodes if all of them are at the top level of the declaration like so:
> 
> ```<a b c>``` 

**Tag Expression**

Tags are used to give value to a node on what its children's properties from the opening node until the related closing node. Tags are canonical alphanumeric strings
that delimited either with dashes, underscores, or colons.

A basic tag is one that does not consist of colons and is formed only with an alphanumeric string like the word "blue1" which signifies the color "blue" shade "1".

```
<a-basic-tag>
</a-basic-tag>
```

A classed tag consists of using dashes to understand particular instances of what this tag will target on the properties of the children.

A builtin class is "fg" and "bg" which represent whether a certain property should go on the foreground or background of the text's coloring.

```
<fg:blue1>
Hello World!
</fg:blue1>
```

> Note that within a node, you can only have one tag expression of the same type!
> 
> For example: `<fg:blue1 fg:blue1></fg:blue1>` will result in a parse error because there are two `fg:blue1` instances.

> Note that only specific tags are allowed to be classed, and note that classed tags _cannot_ be unclassed meaning specifying just `blue1` is ambiguous to kansi to determining
> whether you want to the foreground or background or both.

**Precedence**

Properties travel/propagate down the tree from the specifying node, they do not travel up the tree or apply to neighboring nodes.

#### Sample

```kotlin
val str = """
<fg:blue1 bold>
Hello World!
</fg:blue1 bold>
""".trimMargin()
```

is translated to printing a blue foreground and bolded `"Hello World!"`

### Definitions

You can view the full ANSI definition table and helper functions here:

[ANSIDefinitions.kt](src/main/kotlin/net.exoad.kansi/ANSIDefinitions.kt)
