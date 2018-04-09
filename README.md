# Charles
[![Build Status](https://travis-ci.org/TonnyL/Charles.svg?branch=master)](https://travis-ci.org/TonnyL/Charles)
[ ![Download](https://api.bintray.com/packages/tonnyl/maven/Charles/images/download.svg) ](https://bintray.com/tonnyl/maven/Charles/_latestVersion)

Charles is a local file selector for Android. You can

+ Use it in Activity or Fragment
+ Select multi-media file including images, videos, audio and documents
+ Apply different themes, including two built-in themes and custom themes
+ Restrict different screen orientations
+ Find more by yourself

|  Charles Style  |  Charles Dark Style  |  Empty View  |
| :-------------: | :-------------: | :-------------: |
| ![Charles Style](./art/Charles.png) | ![Empty View](./art/CharlesDark.png) | ![Charles Empty Layout](./art/Empty.png) |

## Download
+ Add the JitPack repository to your build file:

```gradle
allprojects {
    repositories {
        jcenter()
    }
}
```

+ Add the dependency:

```gradle
dependencies {
    implementation 'io.github.tonnyl:charles:x.y.z'
}
```

## ProGuard
If you use [Glide](https://github.com/bumptech/glide) as your image engine, add rules as Glide's README says.
And add extra rule:

```pro
-dontwarn com.squareup.picasso.**
```

If you use [Picasso](https://github.com/square/picasso) as your image engine, add rules as Picasso's README says.
And add extra rule:

```pro
-dontwarn com.bumptech.glide.**
```

**Attention**: The above progurad rules are correct.

## Usage
### Permission
The library requires two permissions:

+ `android.permission.READ_EXTERNAL_STORAGE`
+ `android.permission.WRITE_EXTERNAL_STORAGE`

So if you are targeting Android 6.0+, you need to handle runtime permission request before next step.

### Simple Usage Snippet
Start `CharlesActivity` from current `Activity` or `Fragment`:

```kotlin
Charles.from(this@MainActivity)
        .choose()
        .maxSelectable(9)
        .progressRate(true)
        .theme(R.style.Charles)
        .imageEngine(GlideEngine())
        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
        .forResult(REQUEST_CODE_CHOOSE)
```

### Themes
There are two built-in themes you can use to start `CharlesActivity`:

+ `R.style.Charles` (light mode)
+ `R.style.CharlesDark` (dark mode)

And Also you can define your own theme as you wish.

### Receive Result
In `onActivityResult()` callback of the starting `Activity` or `Fragment`:

```kotlin
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == REQUEST_CODE_CHOOSE && resultCode == Activity.RESULT_OK) {
        val uris = Charles.obtainResult(data)
        val paths = Charles.obtainPathResult(data)

        Log.d("charles", "uris: $uris")
        Log.d("charles", "paths: $paths")
    }
}
```
### More
Find more details about Charles in [wiki](https://github.com/TonnyL/Charles/wiki).

## Contribution
Discussions and pull requests are welcomed 💖.

## Thanks
This library is inspired by [Matisse](https://github.com/zhihu/Matisse) and uses some of its source code.

## License
Charles is under the MIT license. See the [LICENSE](LICENSE) for more information.