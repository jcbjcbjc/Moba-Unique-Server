# Moba-Unique-Server
 分布式帧同步服务器框架
 
 
###  1 主要技术栈和特点
网络框架+协议栈：网络框架用的是高性能IO库Netty，协议栈为低延迟的可靠UDP协议KCP+FEC前向纠错技术（同时支持websocket协议以支持H5游戏）

服务器架构：服务器采用双层架构，通过大厅服务器GameServer和战斗服务器BattleServer两层来实现业务分离和负载均衡，其中可以开多个BattleServer来进行负载均衡，服务器内部通过HTTPS的RPC调用来在多服务器之间传递消息

数据库：mysql+myBatis

序列化工具：protobuf

性能：性能可以调整Netty参数以及KCP参数以达到最大，已通过高性能线程池及其他手段优化




### 2 JAVA帧同步服务器后端框架实现及性能分析


![image](https://user-images.githubusercontent.com/91889375/164408950-0cd0c680-0bd4-447f-a9da-43ba0122b120.png)


这里的IO处理即接收传输层的数据之后再Decode和encode用的应用层协议是websocket因为要支持前端的websocket    websocket是和http一样的应用层协议只不过websocket在处理即时通讯时浪费带宽更小速度更快https://zhuanlan.zhihu.com/p/32052530

迁移到unity 或者 UE4/5 则需要传统的socket   所以到时候需要改一下decoder  直接用传输层的UDP或者是TCP  用TCP的话黏包拆包问题netty可以直接继承现成的解码器decoder但是unity需要自己实现（不过以现在发送消息的频率可以不用实现就是如果while循环发送的话可能会出现一些意想不到的情况(=^=)...）

###### 性能分析  

使用netty框架及优化后单个GameServer可以同时承担数万的并发量（具体取决于服务器性能）由于Battleserver可进行水平横向负载均衡即可以开好几个所以单服务器可以承担数万人同时在线，如果是多服务器就是开更多服

如果是帧同步框架java的性能和c++一样甚至更优

### 3   客户端框架实现

![image](https://user-images.githubusercontent.com/91889375/164409201-9e9958bc-d10c-4bcd-a4fb-95bd5f986794.png)

### 4   基于框架的网游实现

基于此帧同步框架可以开发房间类型的联机游戏比如apex这种或者社交产品（meta  etc）客户端只需按单机游戏的思路去做

修改：

1 GameServer的dao数据库层可以加上需要的字段数据

2 匹配机制可以自己在matchmanager里自己定 目前提供单/多人匹配 和房间开启

3 游戏人数和其他配置可以自己配置
4 需要自定义传递的网络信息可以自己在proto文件里修改

我在写这个的时候借鉴了很多网上别人的代码有些是原创的，但是像netty框架定时任务mybtrit数据库这些就是借鉴别人的代码了所以可以说实现这个框架还是很简单的所以大家可以自己也可以写一套出来毕竟现成的东西还是很多的属于是配置就行（=&=）（可能需要亿点计网 java的小知识）

### 5    配置

1本地运行  在本地（这里指windows环境）用idea打开后选择maven环境和jdk1.8这两个配置就行了

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

###  6 更新日志

1 现在没有预测回滚之后会在客户端加上适当的预测回滚机制







