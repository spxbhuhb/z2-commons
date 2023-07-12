# Z2 Commons

[![Maven Central](https://img.shields.io/maven-central/v/hu.simplexion.z2/z2-commons)](https://mvnrepository.com/artifact/hu.simplexion.z2/z2-core)
[![GitHub License](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)
![Kotlin](https://img.shields.io/github/languages/top/spxbhuhb/z2-commons)

Common utility functions for Kotlin Multiplatform programming.

Status: **experimental**

# Dependency

```kotlin
implementation("hu.simplexion.z2:z2-commons:2023.7.12")
```

# Utility Functions

| Type  | Name                                                                                  | Function                                 | Platform |
|-------|---------------------------------------------------------------------------------------|------------------------------------------|----------|
| fun   | [`ByteArray.toHexString`](src/commonMain/kotlin/hu/simplexion/z2/commons/util/hex.kt) | Converts a byte array into a hex string. | Common   |
| fun   | [`fourRandomInt`](src/commonMain/kotlin/hu/simplexion/z2/commons/util/random.kt)      | Get 4 random Int values.                 | Js, JVM  |
| fun   | [`vmNowMicro`](src/commonMain/kotlin/hu/simplexion/z2/commons/util/clock.kt)          | Virtual machine time in microseconds.    | Js, JVM  |
| class | [`UUID`](src/commonMain/kotlin/hu/simplexion/z2/commons/util/uuid.kt)                 | Type bound UUID 4 implementation         | Js, JVM  |

# I18N

* [Commons](https://github.com/spxbhuhb/z2-commons) is enough for single-language applications
* [I18N](https://github.com/spxbhuhb/z2-i18n) is intended for multi-language applications

For text localization define objects that extend `LocalizedTextStore`:

```kotlin
object loginStrings : LocalizedTextStore(UUID("7f56e8dd-8bf0-49e7-a567-eb81adc501ed")) {
    val account by "Account"
}

fun Z2.loginForm() =
    div {
        textField(label = loginStrings.account)
    }
```

| Class/Interface      | Description                                                          |
|----------------------|----------------------------------------------------------------------|
| `LocalizedText`      | Textual information that should be translated to the current locale. |
| `LocalizedIcon`      | Name of an icon that should be converted to the current locale.      |
| `LocalizedTextStore` | A text store that contains `LocalizedText` instances.                |
| `LocalizedIconStore` | An icon store that contains `LocalizedIcon` instances.               |

## License

> Copyright (c) 2020-2023 Simplexion Kft, Hungary and contributors
>
> Licensed under the Apache License, Version 2.0 (the "License");
> you may not use this work except in compliance with the License.
> You may obtain a copy of the License at
>
>    http://www.apache.org/licenses/LICENSE-2.0
>
> Unless required by applicable law or agreed to in writing, software
> distributed under the License is distributed on an "AS IS" BASIS,
> WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
> See the License for the specific language governing permissions and
> limitations under the License.