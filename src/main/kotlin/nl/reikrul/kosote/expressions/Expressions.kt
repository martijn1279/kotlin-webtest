package nl.reikrul.kosote.expressions

import nl.reikrul.kosote.ExecutionContext
import java.util.regex.Pattern


interface Expression

class ConstantExpression(val value: String) : Expression

class PropertyExpression(val name: String) : Expression {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PropertyExpression) return false

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}

fun property(name: String): PropertyExpression =
        PropertyExpression(name)

class XPathExpression(val path: String) : Expression {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is XPathExpression) return false

        if (path != other.path) return false

        return true
    }

    override fun hashCode(): Int {
        return path.hashCode()
    }
}

fun xpath(path: String): XPathExpression =
        XPathExpression(path)

class ExpressionEvaluator(private val executionContext: ExecutionContext) {

    fun evaluate(expression: Expression): String? =
            when (expression) {
                is ConstantExpression -> expression.value
                is PropertyExpression -> executionContext.properties[expression.name]
                else -> error("Unable to evaluate expression: $expression")
            }
}

fun findExpressions(text: String): List<Pair<String, PropertyExpression>> {
    val matcher = expressionRegex.matcher(text)
    val matches = mutableListOf<String>()
    while (matcher.find()) {
        matches.add(matcher.group(1))
    }
    return matches.map { Pair("#{$it}", PropertyExpression(it)) }.toList()
}

private val expressionRegex = Pattern.compile("#\\{([0-9a-zA-Z_-]+)}")