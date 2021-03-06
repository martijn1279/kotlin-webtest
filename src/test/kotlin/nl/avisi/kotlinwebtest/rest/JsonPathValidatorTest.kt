package nl.avisi.kotlinwebtest.rest

import nl.avisi.kotlinwebtest.ExecutionContext
import nl.avisi.kotlinwebtest.TestConfiguration
import org.junit.Test

class JsonPathValidatorTest {
    companion object {
        private val context = ExecutionContext(TestConfiguration())
    }

    @Test
    fun testJsonPath() {
        val actual = """
            [
                {
                    "foo": {
                        "bar": [
                            2018,
                            29,
                            10
                        ]
                    }
                }
            ]
            """
        val validator = JsonPathValidator("$[0].foo.bar", listOf(2018, 29, 10))
        val result = validator.validate(context, request(), response(actual))
        kotlin.test.assertTrue(result.message) { result.success }
    }

    @Test
    fun testIllegalJsonPath() {
        val actual = """
            [
                {
                    "foo": {
                        "bar": [
                            2018,
                            29,
                            10
                        ]
                    }
                }
            ]
            """
        val validator = JsonPathValidator("--", listOf(2018, 29, 10))
        val result = validator.validate(context, request(), response(actual))
        kotlin.test.assertFalse(result.message) { result.success }
    }
}