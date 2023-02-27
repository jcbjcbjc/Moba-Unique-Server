# Moba-Unique-Server
 分布式帧同步服务器框架
 
## 使用框架完成的游戏
Brick Break Block    https://github.com/jcbjcbjc/BrickBlockBreak
 
### 1 实现的功能
- 完整的**User**服务
- 实现**房间管理和多人实时匹配机制**
- 提供多客户端**实时同步**帧功能
- 三层**分布式架构**高可用高负载量
- 支持**replay**和**实时观战**机制
- 支持**TCP**协议**KCP**协议**websocket**协议
- 支持**断线重连机制**
#### **Tips**:main分支由于出于稳定性考虑没有使用KCP协议而是TCP协议以及禁用了断线重连，这两个功能维护在 KCP+断线重连 分支

### 2 JAVA帧同步服务器后端框架实现及性能分析
- Gateway层：网关层负责对接客户端，减轻GameServer性能压力，封装服务层（若只是测试则不需要）
- Login层：登录服务器（若只有一个GameServer即一个区服则不需要）
- Game层：一个Game服务器就为一个区服
- Battle层：负责战斗的独立服务器
![image](https://user-images.githubusercontent.com/91889375/164408950-0cd0c680-0bd4-447f-a9da-43ba0122b120.png)

### 3   客户端框架实现
对应客户端版本https://github.com/jcbjcbjc/unique-project-client

![image](https://user-images.githubusercontent.com/91889375/164409201-9e9958bc-d10c-4bcd-a4fb-95bd5f986794.png)

### 4   基于框架的实现

基于此帧同步框架可以开发房间类型的联机游戏有
- 1  强同步多人联机游戏（apex）
- 2  元宇宙虚拟现实产品
- 3  强交互社交产品

目前基于此帧同步框架可以开发的联机游戏有
- Brick Break Block    https://github.com/jcbjcbjc/BrickBlockBreak

### 5    配置及运行方法
最简单部署:
- 在GameServer配置文件里改自己数据库用户名密码（mysql5.27以上）
- 运行GameServer
- 运行BattleServer
- 具体部署方法和集群搭建见doc文档
### 6 主要技术栈和特点
- 网络框架+协议栈：网络框架用的是高性能IO库Netty，协议栈为低延迟的可靠UDP协议KCP+FEC前向纠错技术（同时支持websocket协议以支持H5游戏）

- 服务器架构：服务器采用三层架构为分区分层模型，通过登录服务层LoginServer，大厅服务器GameServer和战斗服务器BattleServer两层来实现业务分离和负载均衡，其中可以开多个BattleServer来进行负载均衡，服务器内部通过HTTPS的Restful调用来在多服务器之间传递消息

- 数据库：mysql+myBatis

- 序列化工具：protobuf

- 性能：性能可以调整Netty参数以及KCP参数以达到最大，已通过高性能线程池及其他手段优化

## Tips
有任何问题请提issues









