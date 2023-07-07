Common utility functions for Kotlin Multiplatform programming.

# Dependency

```kotlin
implementation("hu.simplexion.z2:z2-commons:2023.7.8-SNAPSHOT")
```

# Functions

| Type  | Name                    | Function                                 | Platform |
|-------|-------------------------|------------------------------------------|----------|
| fun   | `ByteArray.toHexString` | Converts a byte array into a hex string. | Common   |
| fun   | `fourRandomInt`         | Get 4 random Int values.                 | Js, JVM  |
| fun   | `vmNowMicro`            | Virtual machine time in microseconds.    | Js, JVM  |
| class | `UUID`                  | Type bound UUID 4 implementation         | Js, JVM  |
