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
        compile 'com.github.Trumeet:FlarumSDK:<版本号>'
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

# 登录及 Token

## 登录

`login(LoginRequest request)`

返回 `LoginResponse`：登录结果，包含 Token 和 用户 ID。如果登录失败，则为 Null，您可以从 JsonApiObject 获取错误信息。

## Token

当您调用 `login()` 登录成功后，返回的结果中将会包含 Token。SDK 不会存储这些信息，您需要手动传递给 SDK。

`setToken(String token)` 设定 Token，以后的请求中都会带上这个 Token。

`setToken(TokenGetter getter)` 动态设定 Token，提供一个 Callback，将会在需要使用 Token 的时候回掉它。

# 论坛

## 获取论坛信息

`getForumInfo()`

返回 `Forum`：包含论坛的基本信息。

## 更新论坛信息

TODO

# 讨论（Discussions）

**Discussions** 是一个帖子

## 获取讨论列表

TODO

## 创建新讨论

TODO

## 根据 ID 获取讨论

TODO

## 更新讨论

TODO

## 删除讨论

TODO

# 帖子（Post）

**Post** 是 一个帖子的 **回复**

## 获取帖子列表

TODO

## 创建新帖子

TODO

## 根据 ID 获取帖子

TODO

## 更新帖子

TODO

## 删除帖子

TODO

# 用户

## 获取用户列表

`getUsers()`：获取全部用户

`getUsers(String query)` 根据 **用户名** 或 **gambits** 查询用户

返回：`List<User>`：用户列表


## 注册用户

`registerUser(String username, String password, String email)`

参数：用户名、密码、邮箱
返回：`User`：注册后的用户对象

## 根据 ID 或 用户名 查询用户

TODO

## 更新用户

TODO

## 删除用户

TODO

## 上传头像

TODO

## 删除头像

TODO

# 群组

群组：用户组，如：**管理员**、**版主** 等

## 获取群组列表

`getGroups`

返回：`List<Group>`：群组列表

## 创建新群组

TODO

## 更新群组

TODO

## 删除群组

TODO

# 通知

## 获取消息列表

`getMessageList()`

返回 `List<Notification>`：消息列表

## 将通知设为已阅

`markNotificationAsRead(int id)`

id: 通知 Id，可以从 `getMessageList` 获取。

返回 `Notification`：处理完的通知对象

# 标签

## 获取标签列表

`getTags()`

返回 `List<Tag>`：标签列表

## 添加标签

TODO

## 更新标签

TODO

## 删除标签

TODO

## 排序标签

TODO

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
