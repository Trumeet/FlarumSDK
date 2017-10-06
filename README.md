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
创建 APIManager。`baseUrl` 是 **论坛地址**，我们会根据它访问 API，如 `discuss.flarum.org`。（不能带有 scheme，如 https://。默认使用 https。TODO）

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
* data：所有数据，**主 attributes**、**include**、**relationship** 都在里面。您需要将 `Resource` 转换为自己需要的类型。
* links：（可选的）links 对象，如 上一页下一页 链接
* errors 和 hasErrors：错误信息

## Resource
每一个 Resource 对应一个 `data` 对象。它是所有 Model 的超类。它同时包含 `id`、`type` 等信息。
您需要将它转换为自己需要的类型。

-----

# 使用

[JavaDoc](https://jitpack.io/com/github/Trumeet/FlarumSDK/v1.0_alpha_03/javadoc)

-----

# Dependencies

* [JSONApi](https://github.com/faogustavo/JSONApi)
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
