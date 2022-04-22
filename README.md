# Moba-Unique-Server
 帧同步服务器框架
# 网络同步


之前是一些介绍
想看配置的直接去最后

### 1  I/O模型及I/O处理单元

***线程模型***

BIO

NIO 

AIO

***一些技术***

多线程

多进程

I/O复用

**内核**

内核和用户空间(Linux)

**高效事件处理模式**

reactor模型

Proactor模型



***c#的异步I/O模型***



##### Java的Netty框架IO模型

![image](https://user-images.githubusercontent.com/91889375/164409449-f0b938de-deca-43d9-a181-6a04e7d8d296.png)

### 2   网络协议
![image](https://user-images.githubusercontent.com/91889375/164409564-64de1302-dd66-4c36-b5e2-61a1affbaf9c.png)

##### 基础特性

UDP没有发送缓冲区只有接收缓冲区每一次发送都是独立的寻路路径就像随时发送

##### 基于UDP优势

1提高信息传递的实时性一般用在游戏和音视频领域

在网络不好的情况下 也能低延迟保持低的丢包率（重复发包）

##### 具体优化

在UDP基础在应用层上加上更为灵活的ACT确认机制，重传机制（超时重传，序号重传机制），fec前向纠错机制用来在最低延迟的情况下解决网络抖动丢包问题

jitterbuffer算法设置抖动缓冲区  对数据包丢失，乱序，延迟情况进行处理，平滑的向解码模块也就是处理模块（channel接收数据要先解码）输出数据帧，抵抗弱网情况         这里的技术主要用于状态同步这样没有帧来精确控制的情况

这里比较成熟的就是KCP协议

https://github.com/skywind3000/kcp

https://github.com/skywind3000/kcp/wiki/Network-Layer

KCP协议应用广泛

需要加上fec技术否则网络差的话丢包还是无法解决

谈丢包问题的话，如果网络状况很好，基本上不会丢包，那么KCP自带的快速重传机制就会帮我们把数据重发，但是如果网络条件非常差，这个重传机制就有些鸡肋了，因为快速重传的那一次依旧可能丢，比如丢了6帧，需要重传6次，但是使用守望先锋的那种前向纠错（FEC）技术只要一次成功就可以恢复所有丢失的包，概率比六次成功高很多，这样弱网表现会好很多

### 3    同步策略及其优化

##### 弱同步（回合制这种）

就没有同步策略直接发就行

##### 帧同步

###### 同步方法

![image](https://user-images.githubusercontent.com/91889375/164409611-28be1c25-c8fa-4ec2-8ba1-abbb0e57f9f4.png)

我这里的实现是以服务器为固定的时间线

服务器：1 以固定帧率转发上一帧到达服务器的玩家操作

​               2 接收玩家操作

​               3  接到补帧请求后补帧

客户端： 1 每隔一段时间收集操作发给服务器（暂定66ms即一个帧长度）

​                2  接收服务器下发帧

​                3 接收服务器的补帧

​                4  update以渲染帧率的速度执行帧推进和缺帧检测（这样可以追上最新帧）

随机数种子 定点数

天然支持回放和观战直播 就是下发当前帧-1处理就行。

关于预测回滚的问题：帧同步来说当服务器的响应大于100ms即一帧以上时间到达客户端也就是网络延迟时会进行预测并在接收到服务器权威帧后回滚   平时的话可以预测个一帧但是可能没有多大必要



 Tips:这里接收下发帧和接收补帧都只是接收不对帧做任何处理  虽然之前本来是接收到补帧后立刻处理 下发帧也是接收到才处理但是这样无法追上最新帧  所以改成对服务器的响应只是接收帧  处理帧和缺帧检测外部update执行

另外还有别的帧同步实现方法比如上图中加上了ack确认应答重传机制这样就不需要客户端进行检测是否缺帧

实际上真正实现的方法是KCP+FEC前向纠错技术



以及客户端也可以有自己的帧逻辑时间线即客户端每隔一帧时间推进一帧，但是这样会比较复杂因为客户端的帧数要领先于服务端且领先多少需要根据网络延迟，但是这样可能效果更好这个见之后的状态帧同步也就是守望先锋的著名的网络方案  见2017的GDC

##### 状态同步

这里就简单说一下因为我也没有实现状态同步

陈以能学长提到过状态同步有更多优化方法，这里的主要有两种 一个是预测回滚机制，一个是延迟补偿机制，前者是客户端的行为，后者是服务端的行为，过度使用前者不是很安全，因为怕客户端作弊。延迟补偿就是当一个玩家的操作到达时服务器会根据延迟回滚到客户端发出这个指令的时候进行这个指令的处理之后再滚回来。一般游戏会有延迟补偿的阈值，延迟超过这个才会补偿，这就是吃子弹现象的原因（比如Apex打中了但是服务器判定没中说明延迟刚好在阈值之下有延迟但是不补偿）

这里简单引用一下别人的ECS架构下的状态同步吧

[谈一谈弱网络下手游的网络同步（二） - 知乎 (zhihu.com)](https://zhuanlan.zhihu.com/p/43840865?utm_source=qq&utm_medium=social&utm_oi=1014987097945628672)

##### 状态帧同步

这里要深入了解的话可以去看2017GDC 是非常经典的网络同步方案 学长推荐的

以下内容来自https://www.lfzxb.top/nkgmoba-skillandnetwork/的个人博客

如果想详细了解就去看一下这个博客吧

客户端和服务端都各自维护着整局游戏所有的实体和数据，差别就是客户端不负责逻辑计算，逻辑计算是服务器权威的，客户端要能从服务器发来的帧数据恢复到和服务器发送数据那一帧相同的世界状态

对于客户端来说，本地有两种实体类型，Local（玩家操控的实体本身）和Remote（其他玩家和网络化实体），其中Local会进行预测和回滚，但Remote只会从服务端接收的数据中直接重置状态，所以即使同一个实体，在不同的客户端上（一个作为Local，一个作为Remote）它所接受的数据格式也是不一样的

对于服务端来说，每个网络化实体都有一个Statescript组件，其中有StatescriptSyncManager负责数据同步工作，StatescriptDeltas就是用来记录变化的数据内容了，最后通过StatescriptGhosts记录每个客户端对于此实体的认知（即数据同步程度），并且将最终要传输的数据放到StatescriptPackets中，他们的对应关系就是一个实体->一个Statescript组件->一个StatescriptDeltas->多个StatescriptGhosts->多个StatescriptPackets

[![image-20210302203338808](https://myfirstblog.oss-cn-hangzhou.aliyuncs.com/typoraImages_3/20210907125426.png!webp)](https://myfirstblog.oss-cn-hangzhou.aliyuncs.com/typoraImages_3/20210907125426.png!webp)





之所以StatescriptGhosts和StatescriptPackets之间是多对多的关系，有两个原因

- 每个客户端网络质量不一样，接受的数据可能参差不齐，有丢包的的情况需要服务器重传相关数据，那么这个StatescriptPackets就和传往其他客户端的StatescriptPackets不一样了
- 上文提到的由于客户端和Entity的关系不同（ Local和Remote的区别 ）

那么对于一个StatescriptDeltas而言，他是一个当前帧起始到结束期间变化的数据集合，由于每个客户端网络质量不一致，他会在所有客户端都收到这些数据后才会从服务端移除

###### ***基础网络同步***

守望先锋使用的同步方案为状态同步+帧同步结合的方式，通俗点说就是将状态同步的原本离散更新数据与帧同步的按固定帧更新（确定性）结合起来，这样我们有每一帧的数据内容和状态可以方便的进行回滚操作。

然后有几个名词和概念需要明确一下：

- RTT（Round-Trip Time）：Ping值 ，也就是说从客户端发送一条信息到收到服务器的回复所用时间
- 缓冲帧时长：这个是服务器收到客户端发来的消息进行缓冲的时长，用于应对丢包的特殊情况，这些缓存的信息会在推迟这个缓存的时长后处理，然后发回客户端，这个缓冲帧时长至少为一个帧步进时间长度，因为这样才能填补一帧的丢包，否则就毫无意义，当然，如果网络条件异常恶劣，这个缓冲帧时长就会很大，比如会长达5个帧步进，这样的话，最多可以容忍5帧的丢包，因为第6帧的发来的数据包中包含了前五帧所有的操作，这样可以填补上前面空缺的5帧。最后，如果第6帧的数据包也丢了，那就只能复制使用上一次客户端有效输入的数据了。

所以客户端总共需要领先服务器半个RTT和一个缓冲帧时长的时间才能抹除网络通信的延迟的影响，保证客户端发出帧和服务端处理帧对的上

[![v2-8a54474393ea5c1bd9a73fc087457ab4_b](https://myfirstblog.oss-cn-hangzhou.aliyuncs.com/typoraImages_3/20210907125604.webp)](https://myfirstblog.oss-cn-hangzhou.aliyuncs.com/typoraImages_3/20210907125604.webp)





同时，我们看到，整个过程我们为了高响应速度和支持自定义多帧数据打包，自己处理了丢包，重发，所以完全可以使用UDP，甚至是KCP作为网络传输协议来获得更高的性能（如果是TCP的话，丢失了从90-95这五帧的包后，就算96帧的包到达了，也只会等到90-95帧的包从服务器重发，客户端收到并处理后，才能处理96帧的包，这就会导致恶劣网络环境下的延迟问题被再次放大）

###### ***预测和回滚***

我们来用这张图理解预测和回滚操作

[![img](https://myfirstblog.oss-cn-hangzhou.aliyuncs.com/typoraImages_3/20210907130147.jpeg!webp)](https://myfirstblog.oss-cn-hangzhou.aliyuncs.com/typoraImages_3/20210907130147.jpeg!webp)

img



CF代表Command Frame，ICF代表StateScript的内部Command Frame，稳定状态下，这两个命令帧步进应该是一致的，这里我们在客户端第100帧的时候按下一个按键，在第103帧抬起，此期间客户端依旧在前进，进行各种预表现，比如技能释放，动画播放等等，同时也会把这段操作发送到服务器，经过一个RTT后传回客户端（这里应该是为了便于讲解，忽略了服务器的缓存帧时长），但是此时客户端来到了105帧，但是收到的是自己100帧的回包，所以要根据这个100帧的回包，回滚到100帧的状态，然后从101帧模拟到105帧，注意，这个往前的模拟过程是原子过程，不可被打断，想象成一个while循环就行了。

这就是预测回滚的全过程。

###### ***网络总结***

这里我们模拟一个环境来体会整个网络传输过程，一个帧步进需要16ms，ping值是128ms，缓存帧时长为16ms，所以客户端比服务端领先5个帧步进（16 + 128/2 = 80ms），我们客户端发出的消息A的帧步进为100，所以在A消息结构体中记录的帧步进为100，此时服务端帧步进为95，此时时间继续往前推移，服务端会在第99帧（此时客户端帧步进为104）收到消息A，然后再等待缓冲帧的时长，将在第100帧（此时客户端帧步进为105）处理完毕回复客户端，客户端收到消息的帧步进为109，但是消息A是第100帧的数据，所以客户端要从第101帧开始重新演算到第109帧



### 4  服务端框架

##### 游戏服务器集群和web服务器集群比较

###### web服务器的框架演进

![image.png](https://img-blog.csdnimg.cn/img_convert/58df088713332a67f8cadc5a9e1a289a.png)
![image](https://user-images.githubusercontent.com/91889375/164409940-7ab311fb-cd41-4838-b4c2-89e0ba417e68.png)


![image](https://user-images.githubusercontent.com/91889375/164409896-906d7ea2-f644-44f7-9cb6-abb25a0ee89d.png)

###### 高效分布式服务框架的重要性（微服务架构的核心）

主要有两个:

spring cloud 框架

Dubbo框架：

***feature*：**

- Transparent interface based RPC
- Intelligent load balancing
- Automatic service registration and discovery
- High extensibility
- Runtime traffic routing
- Visualized service governance



https://github.com/apache/dubbo

https://blog.csdn.net/mysmnyc/article/details/120006471

![Architecture](https://camo.githubusercontent.com/e11a2ff9575abc290657ba3fdbff5d36f1594e7add67a72e0eda32e449508eef/68747470733a2f2f647562626f2e6170616368652e6f72672f696d67732f6172636869746563747572652e706e67)

dubbo的服务注册中心为Zookeeper（开源的），服务监控中心为dubbo-monitor



0.服务容器负责启动，加载，运行服务提供者。
1.服务提供者在启动时，向注册中心注册自己提供的服务。
2.服务消费者在启动时，向注册中心订阅自己所需的服务。
3.注册中心返回服务提供者地址列表给消费者，如果有变更，注册中心将基于长连接推送变更数据给消费者。
4.服务消费者，从提供者地址列表中，基于软负载均衡算法，选一台提供者进行调用，如果调用失败，再选另一台调用。
5.服务消费者和提供者，在内存中累计调用次数和调用时间，定时每分钟发送一次统计数据到监控中心

rpc调用可以设置成阻塞或者是非阻塞



对于服务提供方和服务消费方来说，每个提供方和消费方都由不同团队维护和管理，且可以会兼具提供方和服务方的身份（即提供服务的同时要调用其他服务）所以需要服务总线来统一发布服务和订阅服务





###### 游戏服务器的基础框架(mmorpg其他的和这个相似)

![image](https://user-images.githubusercontent.com/91889375/164409337-634c730f-efbb-4e3a-83c9-1845623a8611.png)

##### c#后端框架实现以及游戏展示
具体c# .net框架的底层怎么调用的可以看文档
https://docs.microsoft.com/en-us/dotnet/standard/asynchronous-programming-patterns/calling-synchronous-methods-asynchronously

##### JAVA帧同步服务器后端框架实现及性能分析

###### 完整框架（后续可不进行修改）除了匹配模块

![image](https://user-images.githubusercontent.com/91889375/164408950-0cd0c680-0bd4-447f-a9da-43ba0122b120.png)


这里的IO处理即接收传输层的数据之后再Decode和encode用的应用层协议是websocket因为要支持前端的websocket    websocket是和http一样的应用层协议只不过websocket在处理即时通讯时浪费带宽更小速度更快https://zhuanlan.zhihu.com/p/32052530

迁移到unity 或者 UE4/5 则需要传统的socket   所以到时候需要改一下decoder  直接用传输层的UDP或者是TCP  用TCP的话黏包拆包问题netty可以直接继承现成的解码器decoder但是unity需要自己实现（不过以现在发送消息的频率可以不用实现就是如果while循环发送的话可能会出现一些意想不到的情况(=^=)...）

###### 性能分析  

使用netty框架及优化后单个GameServer可以同时承担数万的并发量（具体取决于服务器性能）由于Battleserver可进行水平横向负载均衡即可以开好几个所以单服务器可以承担数万人同时在线，如果是多服务器就是开更多服

如果是帧同步框架java的性能和c++一样甚至更优

### 5   客户端框架实现

![image](https://user-images.githubusercontent.com/91889375/164409201-9e9958bc-d10c-4bcd-a4fb-95bd5f986794.png)

### 6   基于框架的网游实现

基于此帧同步框架可以开发房间类型的联机游戏比如apex这种或者社交产品（meta  etc）客户端只需按单机游戏的思路去做

修改：1  Gameserver的Dao数据库层可以根据需求添加

​           2   GameServer的Data层加一些需要的数据 比如这个moba就需要角色的相关属性（移速那些的）  之前那个BBB棋类游戏就要一              些常规地图的属性信息作为缓存数据  可以从数据库调不过对性能是一种浪费

​           3    匹配机制可以改改现在的匹配就是有人就行（=……=）

​    

我在写这个的时候借鉴了很多网上别人的代码有些是原创的，但是像netty框架定时任务mybtrit数据库这些就是借鉴别人的代码了所以可以说实现这个框架还是很简单的所以大家可以自己也可以写一套出来毕竟现成的东西还是很多的属于是配置就行（=&=）（可能需要亿点计网 java的小知识）

### 7    配置














