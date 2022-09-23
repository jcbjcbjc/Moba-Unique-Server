# Moba-Unique-Server
 分布式帧同步服务器框架
 
### 1 实现的功能
- 完整的User服务
- 实现房间管理和匹配机制
- 提供多客户端实时同步帧功能
- 三层分布式架构高可用高负载量
- 支持replay和实时观战机制

### 2 JAVA帧同步服务器后端框架实现及性能分析
login层：负责选择区服
Game层：一个Game服务器就为一个区服
Battle层：负责战斗的独立服务器
![image](https://user-images.githubusercontent.com/91889375/164408950-0cd0c680-0bd4-447f-a9da-43ba0122b120.png)


这里的IO处理即接收传输层的数据之后再Decode和encode用的应用层协议是websocket因为要支持前端的websocket    websocket是和http一样的应用层协议只不过websocket在处理即时通讯时浪费带宽更小速度更快https://zhuanlan.zhihu.com/p/32052530

迁移到unity 或者 UE4/5 则需要传统的socket   所以到时候需要改一下decoder  直接用传输层的UDP或者是TCP  用TCP的话黏包拆包问题netty可以直接继承现成的解码器decoder但是unity需要自己实现（不过以现在发送消息的频率可以不用实现就是如果while循环发送的话可能会出现一些意想不到的情况(=^=)...）

###### 性能分析  

使用netty框架及优化后单个GameServer可以同时承担数万的并发量（具体取决于服务器性能）由于Battleserver可进行水平横向负载均衡即可以开好几个所以单服务器可以承担数万人同时在线，如果是多服务器就是开更多服

如果是帧同步框架java的性能和c++一样甚至更优

### 3   客户端框架实现
对应客户端版本https://github.com/jcbjcbjc/unique-project-client

![image](https://user-images.githubusercontent.com/91889375/164409201-9e9958bc-d10c-4bcd-a4fb-95bd5f986794.png)

### 4   基于框架的实现

基于此帧同步框架可以开发房间类型的联机游戏有
1  强同步多人联机游戏（apex）
2  元宇宙虚拟现实产品
3  社交产品

### 5    配置及运行方法
具体见doc文档
### 6 主要技术栈和特点
网络框架+协议栈：网络框架用的是高性能IO库Netty，协议栈为低延迟的可靠UDP协议KCP+FEC前向纠错技术（同时支持websocket协议以支持H5游戏）

服务器架构：服务器采用三层架构为分区分层模型，通过登录服务层LoginServer，大厅服务器GameServer和战斗服务器BattleServer两层来实现业务分离和负载均衡，其中可以开多个BattleServer来进行负载均衡，服务器内部通过HTTPS的Restful调用来在多服务器之间传递消息

数据库：mysql+myBatis

序列化工具：protobuf

性能：性能可以调整Netty参数以及KCP参数以达到最大，已通过高性能线程池及其他手段优化

###  6 更新日志

###  7 更新计划
1，增加redis集群和mysql集群
2，使用分布式数据库解决分区问题
3，拆分Game层，形成多进程结构，以restful调用为基础构建完整的通信机制









