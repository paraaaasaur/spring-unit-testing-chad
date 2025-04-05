# Reflection Test Utils

## ReflectionTestUtils.class

* Spring's support for testing non-public components with the use of reflection

### Methods

* Accessing fields:
    - `ReflectionTestUtils.getField(obj, fieldName);`
    - `ReflectionTestUtils.setField(obj, fieldName, setValue);`
* Invoking Methods:
    - `ReflectionTestUtils.invokeMethod(obj, methodName)`