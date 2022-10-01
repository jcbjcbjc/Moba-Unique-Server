# Moba-Unique-Server
 分布式帧同步服务器框架
 
### 1 实现的功能
- 完整的**User**服务
- 实现**房间管理和匹配机制**
- 提供多客户端**实时同步**帧功能
- 三层**分布式架构**高可用高负载量
- 支持**replay**和**实时观战**机制
- 支持**TCP**协议**KCP**协议**websocket**协议

### 2 JAVA帧同步服务器后端框架实现及性能分析
- Gateway层：网关层负责对接客户端，减轻GameServer性能压力，封装服务层
- Game层：一个Game服务器就为一个区服
- Battle层：负责战斗的独立服务器
![image](https://user-images.githubusercontent.com/91889375/164408950-0cd0c680-0bd4-447f-a9da-43ba0122b120.png)

(=^=)...）

### 3   客户端框架实现
对应客户端版本https://github.com/jcbjcbjc/unique-project-client

![image](https://user-images.githubusercontent.com/91889375/164409201-9e9958bc-d10c-4bcd-a4fb-95bd5f986794.png)

### 4   基于框架的实现

基于此帧同步框架可以开发房间类型的联机游戏有
- 1  强同步多人联机游戏（apex）
- 2  元宇宙虚拟现实产品
- 3  强交互社交产品

### 5    配置及运行方法
具体见doc文档
### 6 主要技术栈和特点
- 网络框架+协议栈：网络框架用的是高性能IO库Netty，协议栈为低延迟的可靠UDP协议KCP+FEC前向纠错技术（同时支持websocket协议以支持H5游戏）

- 服务器架构：服务器采用三层架构为分区分层模型，通过登录服务层LoginServer，大厅服务器GameServer和战斗服务器BattleServer两层来实现业务分离和负载均衡，其中可以开多个BattleServer来进行负载均衡，服务器内部通过HTTPS的Restful调用来在多服务器之间传递消息

- 数据库：mysql+myBatis

- 序列化工具：protobuf

- 性能：性能可以调整Netty参数以及KCP参数以达到最大，已通过高性能线程池及其他手段优化

###  6 更新日志

###  7 更新计划
- 1，增加redis集群和mysql集群
- 2，使用分布式数据库解决分区问题
- 3，拆分Game层，形成多进程结构，以restful调用为基础构建完整的通信机制









