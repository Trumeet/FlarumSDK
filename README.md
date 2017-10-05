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

# 使用

## 概述
所有 API 都有 **同步** 和 **异步** 方法，比如说：
```java
// 同步
Result<Forum> api.getForumInfo() throws IOException {}
// 异步
Call getForumInfo(new Callback<Forum>() {
         @Override
         public void onResponse(Call call, Result<Forum> result) {
             // 成功
         }

         @Override
         public void onFailure(Call call, Throwable e) {
             // 出错
         }
     });
```

# API Manager

`Flarum` 是 APIManager，所有 API 都需要用它访问。您要首先使用
```java
Flarum.create(String baseUrl);
```
创建 APIManager。`baseUrl` 是 **论坛地址**，我们会根据它访问 API，如 `discuss.flarum.org`。（不能带有 scheme，如 https://。默认使用 https。TODO）

# Result

Result 是所有返回结果的包裹类，它包含如下信息：
* rawResponse：OkHttp 的源 Response
* mainAttr：主要信息（泛型 T）
* object：解析后的 JSON Api 对象。包含了所有信息，比如 `include`，`relationships` ，`errors`，`links` 等。

# JSONApiObject
这是反序列化后的 Json Api 对象：
* data：所有数据，**主 attributes**、**include**、**relationship** 都在里面。您需要将 `Resource` 转换为自己需要的类型。
* links：（可选的）links 对象，如 上一页下一页 链接
* errors 和 hasErrors：错误信息

# Resource
每一个 Resource 对应一个 `data` 对象。它是所有 Model 的超类。它同时包含 `id`、`type` 等信息。
您需要将它转换为自己需要的类型。

# 分页
所有 API 中带有 `page` 参数的方法均可以实现分页。您可以输入0至任意数的页数。如果返回的 data 为空，则说明该页面不存在。
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
