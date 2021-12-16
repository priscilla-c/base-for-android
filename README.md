# 一个暂时没想好具体干啥的APP
Created by Priscilla Cheung 2021年12月15日17:00:29.

## 目录

* [技术选型](#技术选型)
  设计到某一种功能所采用的技术选型
* [具体功能](#具体功能)
  每一个类所对应的功能
* [维护相关](#维护相关)
  维护注意事项  

## 技术选型
| 名称 | 选型 |
| --- | --- |
|数据库|ObjectBox|
|图片加载|Glide|
|组件通信|EventBus|
|数据持久化(键值对)|MMKV|

[返回目录](#目录)
## 具体功能
* [普通页面](#普通页面)
* [工具类](#工具类)
* [其他类](#其他类)

[返回目录](#目录)
### 普通页面
| 页面名称 | 对应页面 |  
| --- | --- |

[返回具体功能](#具体功能)

-----------------------------------------------------
### 工具类
| 工具类名称 | 对应功能 |  
| --- | --- |
| extra | 主要工具类 |
| Simple | View扩展方法 |
| ApiService | API接口 |
| Process | 协程(线程调度) |
| scope | Retrofit绑定ApiService |
| Store | MMKV、ObjectBox初始化 |
| KeyValueStore | MMKV工具类) |
| DataBaseStore | ObjectBox工具类 ) |

[返回具体功能](#具体功能)

-----------------------------------------------------
### 其他类
| 其他名称 | 对应功能 |
| --- | --- |
|AppContext|Activity管理类|
|BaseApplication|Application基类|
|MyApplication|Application|
| coroutines | 返回格式处理 |
| interceptor | OkHttp拦截器 |
[返回具体功能](#具体功能)

-----------------------------------------------------
## 维护相关

[返回目录](#目录)
