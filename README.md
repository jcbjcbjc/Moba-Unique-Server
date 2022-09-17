# Moba-Unique-Server
 分布式帧同步服务器框架
 
###  1 主要技术栈和特点
网络框架+协议栈：网络框架用的是高性能IO库Netty，协议栈为低延迟的可靠UDP协议KCP+FEC前向纠错技术（同时支持websocket协议以支持H5游戏）

服务器架构：服务器采用三层架构为分区分层模型，通过网关服务层Gateway，大厅服务器GameServer和战斗服务器BattleServer两层来实现业务分离和负载均衡，其中可以开多个BattleServer来进行负载均衡，服务器内部通过HTTPS的RPC调用来在多服务器之间传递消息
![3049683c946cba031e1becabe4649a3](https://user-images.githubusercontent.com/91889375/172151299-3410c76f-3a28-4141-912e-b5d572a19a84.jpg)

数据库：mysql+myBatis

序列化工具：protobuf

性能：性能可以调整Netty参数以及KCP参数以达到最大，已通过高性能线程池及其他手段优化

###  2 核心同步和网络技术

##### 预测回滚技术：        

上一帧权威状态+权威输入+预测输入=这一帧的最终状态  

上一帧权威状态+权威输入=这一帧的权威状态 

玩家的每一步操作都会作为预测输入储存在predictedInputList中并会立即执行此操作，每一帧服务器返回权威输入时将 predictedInputList中已经变成权威输入的预测输入移除，然后将客户端回滚至上一个确定的权威状态应用权威输入形成这一帧的权威状态再应用predictedInputList中所有的预测输入形成这一帧的最终状态 

##### 包压缩：
##### 前向纠错：
为了进一步提高传输速度，下层协议也许会使用前向纠错技术。需要注意，前向纠错会根据冗余信息解出原始数据包。相同的原始数据包不要两次input到KCP，否则将会导致 kcp以为对方重发了，这样会产生更多的ack占用额外带宽。

比如下层协议使用最简单的冗余包：单个数据包除了自己外，还会重复存储一次上一个数据包，以及上上一个数据包的内容：

Fn = (Pn, Pn-1, Pn-2)

P0 = (0, X, X)
P1 = (1, 0, X)
P2 = (2, 1, 0)
P3 = (3, 2, 1)
这样几个包发送出去，接收方对于单个原始包都可能被解出3次来（后面两个包任然会重复该包内容），那么这里需要记录一下，一个下层数据包只会input给kcp一次，避免过多重复ack带来的浪费。


### 3 JAVA帧同步服务器后端框架实现及性能分析
login层：负责选择区服
Game层：一个Game服务器就为一个区服
Battle层：负责战斗的独立服务器
![image](https://user-images.githubusercontent.com/91889375/164408950-0cd0c680-0bd4-447f-a9da-43ba0122b120.png)


这里的IO处理即接收传输层的数据之后再Decode和encode用的应用层协议是websocket因为要支持前端的websocket    websocket是和http一样的应用层协议只不过websocket在处理即时通讯时浪费带宽更小速度更快https://zhuanlan.zhihu.com/p/32052530

迁移到unity 或者 UE4/5 则需要传统的socket   所以到时候需要改一下decoder  直接用传输层的UDP或者是TCP  用TCP的话黏包拆包问题netty可以直接继承现成的解码器decoder但是unity需要自己实现（不过以现在发送消息的频率可以不用实现就是如果while循环发送的话可能会出现一些意想不到的情况(=^=)...）

###### 性能分析  

使用netty框架及优化后单个GameServer可以同时承担数万的并发量（具体取决于服务器性能）由于Battleserver可进行水平横向负载均衡即可以开好几个所以单服务器可以承担数万人同时在线，如果是多服务器就是开更多服

如果是帧同步框架java的性能和c++一样甚至更优

### 4   客户端框架实现

![image](https://user-images.githubusercontent.com/91889375/164409201-9e9958bc-d10c-4bcd-a4fb-95bd5f986794.png)

### 5   基于框架的网游实现

基于此帧同步框架可以开发房间类型的联机游戏比如apex这种或者社交产品（meta  etc）客户端只需按单机游戏的思路去做

修改：

1 GameServer的dao数据库层可以加上需要的字段数据

2 匹配机制可以自己在matchmanager里自己定 目前提供单/多人匹配 和房间开启

3 游戏人数和其他配置可以自己配置
4 需要自定义传递的网络信息可以自己在proto文件里修改

我在写这个的时候借鉴了很多网上别人的代码有些是原创的，但是像netty框架定时任务mybtrit数据库这些就是借鉴别人的代码了所以可以说实现这个框架还是很简单的所以大家可以自己也可以写一套出来毕竟现成的东西还是很多的属于是配置就行（=&=）（可能需要亿点计网 java的小知识）

### 6    配置

1本地运行  在本地（这里指windows环境）用idea打开后选择maven环境和jdk1.8这两个配置就行了
需要有mysql5.7 在配置文件里改自己的用户名密码

2 linux服务器运行   将程序打包成jar包 用xftp等传输文件软件传到服务器上运行以下代码运行
  运行
   nohup java -jar   GameServer-0.0.1-SNAPSHOT.jar  >GameServer.log  2>&1 &
  nohup java -jar   BattleServer-0.0.1-SNAPSHOT.jar  >BattleServer.log  2>&1 &

  查看进程关闭
  ps -ef|grep java
  关闭进程
  kill -9 PID

3 配置 需要在配置文件里改自己数据库的用户名密码那些的
       如果想改绑定端口的话可以自行修改 要开多个BattleServer的话在GameServer里加上IP地址即可

###  7 更新日志
###  8 更新计划
1，增加redis集群和mysql集群
2，使用分布式数据库解决分区问题
3，拆分Game层，形成多进程结构，以restful调用为基础构建完整的通信机制









