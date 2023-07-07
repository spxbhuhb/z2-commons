Common utility functions for Kotlin Multiplatform programming.

# Dependency

```kotlin
implementation("hu.simplexion.z2:z2-commons:2023.7.8-SNAPSHOT")
```

# Functions

| Type  | Name                                                                                  | Function                                 | Platform |
|-------|---------------------------------------------------------------------------------------|------------------------------------------|----------|
| fun   | [`ByteArray.toHexString`](src/commonMain/kotlin/hu/simplexion/z2/commons/util/hex.kt) | Converts a byte array into a hex string. | Common   |
| fun   | [`fourRandomInt`](src/commonMain/kotlin/hu/simplexion/z2/commons/util/random.kt)      | Get 4 random Int values.                 | Js, JVM  |
| fun   | [`vmNowMicro`](src/commonMain/kotlin/hu/simplexion/z2/commons/util/clock.kt)          | Virtual machine time in microseconds.    | Js, JVM  |
| class | [`UUID`](src/commonMain/kotlin/hu/simplexion/z2/commons/util/uuid.kt)                 | Type bound UUID 4 implementation         | Js, JVM  |
