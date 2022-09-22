# unique-project-client
 
 ### 1 Introduction
 a Client FrameWork Corresponding to Server FrameWork https://github.com/jcbjcbjc/Moba-Unique-Server
 ### 2 主要技术栈
 游戏引擎：Unity
 网络框架：C# .net原生API  由事件驱动
 网络协议：基于UDP的可靠协议KCP
 整体架构：
![image](https://user-images.githubusercontent.com/91889375/169481208-853e91c1-23ef-4fee-8994-20e91fa49703.png)
同步技术：帧同步（实现预测回滚技术）具体实现方法见https://github.com/jcbjcbjc/Moba-Unique-Server
序列化工具：Protobuf

实现功能：大厅：   登录注册服务，匹配服务（邀请队友或自己匹配，或者是开房间直接进入游戏），聊天服务，基本的玩家User服务

  GameLogic:   实现帧同步框架 只需对应写入具体逻辑
  
  UI： 提供完整的UI管理框架 解决UI管理，缓存等问题
         
### 3 优势
开发方便，只需写入对应逻辑和UI开发即可完成开发

