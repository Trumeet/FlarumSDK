# FlarumSDK

[![](https://jitpack.io/v/Trumeet/FlarumSDK.svg)](https://jitpack.io/#Trumeet/FlarumSDK)

Flarum SDK for Java and Android (Alpha)

# 依赖

* Add it in your root build.gradle at the end of repositories:
```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

* 添加依賴

```
dependencies {
        compile 'com.github.Trumeet:FlarumSDK:v1.0_alpha_03'
}
```

# 概述

## API Manager

`Flarum` 是 APIManager，所有 API 都需要用它访问。您要首先使用
```java
Flarum.create(String baseUrl);
```
创建 APIManager。`baseUrl` 是 **论坛地址**，我们会根据它访问 API，如 `discuss.flarum.org`。
您可以在 baseUrl 中加入自定义 scheme，如 `https://bbs.letitfly.me`。如果您没有传递 Scheme，我们将会使用 HTTP。

## RequestBuilder

调用所有 API 方法都会返回一个包含基本请求的 `RequestBuilder`。它包含 URL、Token、转换器 等信息。
您可以通过它自定义请求参数和发起请求。

### 分页
可以通过 `RequestBuilder#setPage(int page)` 设置页码。
您可以输入0至任意数的页数。如果返回的 data 为空，则说明该页面不存在。

### 过滤器

RequestBuilder 可以设置多个过滤器（Filter）。比如：过滤帖子、用户名 等。

例子：
```java
// 获取ID为1的帖子下所有回复，第二页
List<Post> posts = flarum.getAllPosts()
    // 设置 帖子ID 为 1
    .addFilter("discussion", "1")
    // 设置 第二页
    .setPage(2)
    // 执行请求，返回 Result
    .execute()
    // 主对象
    .mainAttr;
```

**不是每个API都支持分页和过滤，您需要根据注释设定。**

## Result

Result 是所有返回结果的包裹类，它包含如下信息：
* rawResponse：OkHttp 的源 Response
* mainAttr：主要信息（泛型 T）
* object：解析后的 JSON Api 对象。包含了所有信息，比如 `include`，`relationships` ，`errors`，`links` 等。

## JSONApiObject
这是反序列化后的 Json Api 对象：
* data：数据，对应 Json 的 Primary data，由于可能是数组或单个对象，这里都保存为 List。您需要将 `Resource` 转换为自己需要的类型。
* links：链接，对应 Json 的 [链接](http://jsonapi.org.cn/format/#document-structure-links)。
* errors：错误，对应 Json 的 [错误对象](http://jsonapi.org.cn/format/#errors-error-objects) 列表。

## Data
每一个 Data 对应一个 `data` 对象。它是所有 Model 的超类。它同时包含 `id`、`type`、`relationships`、`includes` 等信息。
您需要将它转换为自己需要的类型。

-----

# 使用

[JavaDoc](https://jitpack.io/com/github/Trumeet/FlarumSDK/v1.0_alpha_03/javadoc)

-----

# Dependencies

* [Gson](https://github.com/google/gson/)
* [OkHttp](http://square.github.io/okhttp/)

# Licenses
使用此项目请确保您遵守 `Apache License 2.0`
```
Copyright (C) 2017 Trumeet

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
