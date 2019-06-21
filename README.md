##Redis
    + 高性能Key-Value服务器
    + 多种数据结构
    + 丰富功能
    + 高可用分布式
    
## Redis初识
    + Redis是什么
        * 开源
        * 基于键值对的存储服务系统
        * 支持多种数据结构
        * 高性能,功能丰富                        
    + Redis 特性回顾
        + 速度快
            * 10W OPS
            * 数据存在内存
            * 使用c语言编写
            * 单线程
        + 持久化
            * Redis 所有的数据存储在内存中,对数据的更新将异步地保存到硬盘中
        + 多种数据结构
            + 基本
                * String/Blobs/
                * Hash Tables(objects!)
                * Linked Lists
                * Sets
                * Sorted Sets
            + 衍生
                * Bitmaps
                * HyperLogLog:超小内存唯一值计算
                * GEO:地理信息定位
        + 多种编程语言
            * java
            * Python
            * ruby
            * lua
            * php
            * nodejs
        + 功能丰富
            * 发布订阅
            * lua 脚本
            * 简单事务
            * pipeline
        + 简单
            * 不依赖外部库
            * 单线程模型
        + 主从复制 
        + 高可用分布式 
            *　高可用:Redis-Sentinel(v2.8)支持高可用
            * 分布式:Redis-Cluster(V3.0)支持分布式 
    + Redis 单机安装
    + Redis 典型使用场景        
        * 缓存系统
        * 计数器
        * 消息队列系统
        * 排行榜
        * 社交网络
        * 实时系统 
## API的理解和使用 
    + 通用命令
        +　通用命令
             + keys
                + keys *
                    * 不能在生产环境中使用
                    + 怎么用
                        * 热备从节点
                        * scan
             * dbsize
             * exists key
             * del key [key...]
             + expire key seconds
                * ttl: 查看剩余过期时间
                * persist: 去除过期时间
             * type key
             
             + 时间复杂度
                * keys o(n)
                * dbsize o(1)
                * exists o(1)
                * del o(1)
                * expire o(1)
                * type o(1)
        + 数据结构和内部编码
            + string
                * raw
                * int
                * embstr
            + hash
                * hashtable
                * ziplist
            + list
                * linkedlist
                * ziplist
            + set
                * hashtable
                * intset
            + zset
                * skiplist
                * ziplist                             
        + 单线程架构
            * Redis Event Loop:I/O Multiplexing:Process command
            + 为什么快
                * 纯内存
                * 非阻塞IO(epoll)
                * 避免线程切换和竞态消耗    
            + 最佳实践
                * 一次只运行一条命令
                + 拒绝长命令
                    * keys,flushall,flushdb,slow lua script,multi/exec,operate big value(collection)
                + 其实不是单线程(对于某些操作)
                    * fysnc file descriptor
                    * close file descriptor                       
            
    + 字符串类型
        + 字符串键值结构
            * hello:world
            * counter:1
            * bits:01011001
            * key:jsonstr
            * value up to 512M 
        +  场景
            * 缓存
            * 计数器
            * 分布式锁
        + 命令
            * get:o(1)
            * set:o(1)
            * del:o(1)
            * incr:o(1)
            * decr:o(1)
            * incrby:o(1)
            * decrby:o(1)  
            * set:o(1)(无论如何都设置)
            * setnx:o(1)(不存在,才设置)
            * set xx:o(1)(存在,才设置)
            * mget:o(n)(原子操作)
            * mset:o(n)(原子操作)
            * getset:o(1)
            * append:o(1)
            * strlen:o(1)
            * incrbyfloat:o(1)
            * getrange:o(1)
            * setrange:o(1)
        + 实战
            + 记录网站每个用户个人主页的访问量
                * incr userid:pageview (单线程无竞争)
            * 缓存视频的基本信息(数据源在DB中)伪代码
            + 分布式id生成器                           
    + 哈希类型
        + 结构
            + key:value
                * key:{field1:value1,field2:value2}
            * key->string
            * value-> field 和 value 
            + 特点    
                * Mapmap
                * small redis
                * field 不能相同,value 可以 相同
        + 重要API
            * hget:o(1)
            * hset:o(1)
            * hdel:o(1)
            * hexsits:o(1)
            * hlen:o(1)
            * hmget:o(n)
            * hmset:o(n)
            + hgetall:o(n)
                * 小心使用,牢记单线程
            * hvals:o(n)
            * hkeys:o(n)
            * hsetnx:o(1)
            * hincrby:o(1)
            * hincrbyfloat:o(1)
        + 实战
            + 记录网站每个用户主页的访问量
                * hincrby user:1:info pageview count(单线程无竞争)
            * 缓存视频的基本信息(数据源在DB中)伪代码
        + string vs hash
            + string 1
                + 结构
                    * user1:age
                    * user1:name
                + 优点
                    * 直观
                    * 可以部分更新
                +缺点    
                    * 内存占用较大
                    * key 较为分散
            + string 2
                + 结构 
                    * user1:序列化的json
                + 优点
                    * 编程简单
                    * 可能节约内存                     
                + 缺点
                    * 序列化开销
                    * 设置属性要操作整个数据                                   
            + hash
                + 结构
                    * user:1 {age:xx,name:xx} 
                + 优点
                    * 直观
                    * 省空间
                    * 以部分更新
                + 缺点 
                    * 编程稍微复杂
                    * ttl 不好控制:无法单独控制field的过期时间                
    + 列表类型
        + 结构
            * key:elements
            + 特点
                * 有序
                * 可以重复
        + 重要API
            * rpush:o(1~n)
            * lpush:o(1~n)
            * hinsert:o(n)
            * lpop:o(1)
            * rpop:o(1)
            * lrem:o(n)
            * ltrim:o(n):删除
            * lrange:o(n):查询
            * lindex:o(n)
            * llen:o(1)
            * lset:o(n)
            * blpop:o(1)
            * brpop:o(1)
        + TIPS
            * lpush + lpop = stack
            * lpush + rpop = queue   
            * lpush + ltrim = capped collection  
            * lpush + brpop = message queue
    + 集合类型
        + 结构
            * key:values
            + 特定
                * 无序
                * 无重复
                * 支持集合间操作
        +　重要API
            * sadd:o(1~n)
            * srem:o(1~n)
            * scard:o
            * sismember
            * srandmember
            + smembers
                * 无序
                * 小心使用,因为要取出所有元素
            * spop
            * sdiff 
            * sinter
            * sunion
            * sinterstroe
            * sdiffstore
            * sunionstore
        + 实战
            * 标签
        + TIPS
            * sadd = tagging
            * spop/srandmember = random item
            * sadd + sinter = social graph                                
    + 有序集合类型
        + 结构
            * key:(score:value)
        + 重要API
            * zadd:o(logN)
            * zrem:o(1) 
            * zscore:o(1)
            * zincrby:o(1)
            * zcard:o(1)
            * zrange:o(logN+m) (n:元素个数,m:获取范围内元素个数)
            * zrangebyscore:o(logN+m) (n:元素个数,m:获取范围内元素个数)
            * zcount:o(logN+m) (n:元素个数,m:获取范围内元素个数)
            * zremrangebyrank:o(logN+m) (n:元素个数,m:获取范围内元素个数)
            * zremrangebyscore:o(logN+m) (n:元素个数,m:获取范围内元素个数)
            * zrevrank
            * zrevrange
            * zrevrangebyscore
            * zinterscore
            * zunionscore
        + 实战
            * 排行榜       
## Redis客户端的使用  
    * jedis
## 瑞士军刀Redis其他功能
    + 慢查询
        + 生命周期
            * 发送命令
            * 排队
            * 执行命令(慢查询在这个阶段)
            * 返回结果
        + 两个配置
            + slowlog-max-len
                * 先进先出队列
                * 固定长度
                * 保存在内存中
            + slowlog-log-slower-than
                * 慢查询阈值(单位:微秒)
                * slowlog-log-slower-than=0,记录所有命令
                * slowlog-log-slower-than<0,不记录任何命令
            + 配置方法
                +　默认值
                    * config get slowlog-max-len 128
                    * config get slowlog-log-slower-than 10000
                + 修改配置文件重启
                + 动态配置
                    * config set slowlog-max-len XXX    
                    * config set slowlog-log-slower-than XXX 
        + 三个命令
            * slowlog get [n]:获取慢查询队列
            * slowlog len : 获取慢查询队列长度
            * slowlog reset: 清空慢查询队列
        + 运维经验
            * slowlog-log-slower-than 不要设置过大,默认10ms,通常设置1ms
            * slowlog-max-len不要设置过小,通常设置1000左右
            * 理解命令生命周期
            * 定期做慢查询持久化(针对第二条)
    + pipeline
        + 什么是流水线
            * 打包命令,一起发给服务端            
        + 客户端实现
        + 与原生M操作对比
            * 原生M操作为原子操作
            * pipeline 非原子, 返回结果为顺序结果
        + 使用建议
            * Redis命令执行时间微秒级别
            * pipeline每次条数要控制(网络)
            * pipeline每次只能作用在一个Redis节点上
            * M操作与Redis区别
    + 发布订阅
        + 角色
            * 发布者(publisher) 
            * 订阅者(subscriber)
            * 频道(channel)
        + 模型
        + API
            * publish
            * subscribe
            * unsubscribe
            * psubscribe:模式订阅
            * punsubscribe:退订指定的模式
            * pubsub channels:列出至少有一个订阅者的频道
            * pubsub numsub [channe]:列出给定频道的订阅数量 
            * pubsub numpat:列出被订阅模式的数量
        + 发布订阅与消息队列
            * 发布订阅所有订阅者均能收到消息
            * 消息队列只有一个消费者能够收到消息
    + Bitmap 
        + 位图
            * 直接操作二进制位
        + API
            * setbit
            * getbit
            * bitcount:统计位图中位值为1的个数
            * bitop:做多个bitmap的and,or,not,xor
            * bitpos:[start,end 单位为字节]
        + 独立用户统计
            + set vs bitmap
                + 一亿用户,五千万独立
                    * set  32位(假设int) 32bit*50000000 = 200M
                    * bit  1位           1bit*100000000=12.5M
                + 百万用户量
                    * set  32位(假设int) 32bit*1000000 = 4M
                    * bit  1位           1bit*100000000=12.5M      
        + 使用经验
            * type=string,最大为512M
            * 注意setbit时的偏移量,可能有较大消耗
            * 位图不是绝对好    
    + HyperLogLog
        + 新的数据结构
            * 基于HyperLogLog算法:极小空间完成独立数量统计
            * 本质还是字符串:type hyperloglog_key
        + API
            * pfadd
            * pfcount
            * pfmerge
        + 内存消耗
            + 百万独立用户
                * 15KB : 1天
                * 450KB : 1月
                * 5M : 1年
        + 使用经验 
            * 是否能容忍错误(错误率:0.81%)
            * 是否需要单条数据 
    + GEO 
        + GEO 是什么
            * GEO(地理信息定位): 存储经纬度, 计算两地距离,范围计算
        + API
            * geoadd
            * geopos
            * geodist
            * georadius
        + 说明
            * type geokey = zset 
            * 没有删除API:zrem key member
## Redis持久化的取舍和选择 
    + 持久化的作用
        + 什么是持久化
            * Redis 所有的数据存储在内存中,对数据的更新将异步地保存到硬盘中
        + 持久化实现方式
            * 快照
            * 写日志            
    + RDB
        + 什么是RDB
            * 快照
        + 触发机制-主要三种方式
            + save(同步)
                * 文件策略:  新文件替换掉旧文件
                * 复杂度 : o(n)
            + bgsave(异步)
                * 文件策略:  新文件替换掉旧文件
                * 复杂度 : o(n)
            +  save vs bgsave
                + IO 类型
                    * Save:同步
                    * bgsave:异步
                + 阻塞
                    * sava:是
                    *　bgsave:是(fork时)
                + 复杂度
                    * 均为o(n)
                + 优点
                    * save:会消耗额外内存
                    * bgsave:不阻塞客户端命令
                + 缺点
                    * save:阻塞客户端命令
                    * bgsave:需要fork,消耗内存
            * 自动:建议关闭                
        + 触发机制-不容忽略的方式
            * 全量复制
            * debug reload
            * shutdown 
        + 试验
    + AOF
        + RDB 问题
            * 耗时耗性能
            * 不可控,丢失数据
        + 什么是AOF
            * 每写一条记录到内存中,同时写一条记录到日志文件
        + 三种策略
            + always
                * 每条写记录直接从缓冲区刷新到硬盘
                + 优点
                    * 不丢失数据 
                + 缺点
                    * IO开销大,一般的sata硬盘只有几百tps
            + everysec
                * 每秒钟的写记录刷新到硬盘(默认值)
                + 优点
                    * 每秒一次fsync,丢一秒数据
                + 缺点
                    * 丢一秒数据
            + no
                * os决定何时刷新
                + 优点
                    * 不用管
                + 缺点
                    * 不可控
        + 重写
            * 删除过期,简化重复,优化多次操作(对于列表,集合,有序集合)
            + 作用
                * 减少磁盘占用量
                * 加快恢复速度
            + 实现方式
                * bgrewriteaof(fork子进程)
                + AOF重写配置
                    + 配置
                        * auto-aof-rewrite-min-size:AOF文件重写需要的尺寸
                        * auto-aof-rewrite-percentage:AOF文件增长率
                    + 统计
                        * aof_current_size:AOF当前尺寸(单位:字节)
                        * aof_base_size:AOF上次启动和重写的尺寸(单位:字节)     
    + RDB与AOF 的抉择
        + RDB 与 AOF 比较
            + 启动优先级
                * RDB:低
                * AOF:高
            + 体积
                * RDB:小
                * AOF:大(BGREWRITEAOF后一样大)
            + 恢复速度
                * RDB:快
                * AOF:慢
            + 数据安全性
                * RDB:丢数据
                * AOF:根据策略而定
            + 轻重
                * RDB:重
                * AOF:轻                                                                                      
        + RDB 最佳策略
            * 关闭
            * 集中管理
            * 主节点关闭,从节点打开       
        + AOF 最佳策略    
            * 开:缓存和存储
            * AOF重写集中管理   
            * everysec
        + 最佳策略
            * 小分片 
            * 缓存或者存储
            * 监控(硬盘,内存,负载,网络)
            * 足够的内存       
## 常见的持久化开发运维问题    
    + fork
        * 同步操作
        * 与内存相关:内存越大,耗时越长(与机型有关)
        * info: latest_fork_usec
        + 改善
            * 优先使用物理机或者高效支持fork操作的虚拟化技术
            * 控制Redis实例最大可用内存:maxmemory
            * 合理配置Linux内存分配策略:vm.overcommit_memory=1
            * 降低Fork频率:放宽AOF重写自动触发时机,不必要的全量复制
    + 子进程开销和优化
        + CPU
            * 开销:RDB和AOF文件生成,属于CPU密集型
            * 优化:不做CPU绑定,不和CPU密集型应用部署在一起
        + 内存
            * 开销:fork内存开销,copy-on-write
            + 优化
                * echo never > /sys/kernel/mm/transparent_hugepage/enabled
        + 硬盘
            * 开销: AOF和RDB文件写入,可以结合iostat,iotop分析
            + 优化
                * 不要和高硬盘负载服务部署在一起:存储服务,消息队列
                * no-appendfsync-on-rewrite = yes
                * 根据写入量决定磁盘类型:例如SSD 
                * 单机多实例持久化文件目录可以考虑分盘
    +  AOF追加阻塞
        * redis 日志
        + info persistence
            * aof_delayed_fsync:0(统计的次数,具体时间段需要自己观察)
        * 查看硬盘信息    
## Redis复制的原理与优化
    + 什么是主从复制
        + 单机问题
            * 机器故障
            * 容量瓶颈
            * QPS瓶颈
        + 主从复制作用
            * 数据副本
            * 扩展读性能
        + 总结
            * master:--(1:n)--->slave
            * slave---(1:1)--->master
            * 数据流向是单项的,master->slave    
    + 主从复制的配置
        + slaveof
            * slaveof ip port
            * slaveof no one
            * 优点:不需要重启
            * 缺点:不便于管理
        + 配置
            * slaveof ip port
            * slave-read-only yes
            * 优点:统一配置
            * 缺点:需要重启
    + 全量复制和部分复制
        + 全量复制开销
            * bgsave时间
            * RDB文件网络传输时间
            * 从节点清空数据时间
            *　从节点加载RDB的时间
            * 可能的AOF重写时间
    + 故障处理
    + 开发运维常见问题 
        + 读写分离
            * 读流量分摊到从节点
            + 可能的问题
                * 复制数据延迟
                * 读到过期数据
                * 从节点故障
        + 主从配置不一致
            * 主从maxmemory不一致,丢失数据
            * 数据结构优化参数(hash-max-ziplist-entries):内存不一致  
        + 规避全量复制
            + 第一次全量复制
                * 不可避免
                + 优化
                    * 小主节点
                    * 低峰
            + 节点运行ID不匹配
                * 主节点重启(运行ID变化)
                + 故障转移
                    * 哨兵
                    * 集群
            + 复制积压缓冲区不足
                * 网络中断,部分复制无法满足偏移量的要求
                + 优化
                    * 增大复制缓冲区配置repl_backlog_size
                    * 网络增强          
        + 规避复制风暴
            + 单主节点复制风暴
                * 问题:主节点重启,多从节点复制
                + 解决: 更换复制拓扑
                    * 主节点对应一个从节点,该从节点再对应多个从节点
            + 单机器复制风暴
                * 一台机器拥有多个主节点
                + 解决:主节点分散多机器                    
## Redis Sentinel  
    + Redis 高可用实现方案 
        * 故障发现
        * 故障自动转移
        * 配置中心
        * 客户端通知
    + 主从复制高可用的问题
        * 手动故障转移
        * 写能力和存储能力受到限制  
    + 架构说明
        * 作为主节点的配置中心,而不再由客户端直接连接主节点
    + 安装配置
        * port 26380
        * daemonize yes
        * pidfile "/var/run/redis-sentinel-26380.pid"
        * logfile "26380.log"
        * dir "/opt/soft/redis/data"
    + 客户端连接
        + 客户端接入流程
            * Sentinel 地址集合
            * masterName
            * 不是代理模式,只是配置中心
    + 实现原理
        + 三个定时任务
            + 每10秒每个sentinel对master和slave执行info
                * 发现slave 节点
                * 确认主从关系
            + 每2秒每个sentinel 通过master 节点的channel交换信息(pub/sub)
                * 通过__sentinel__:hello 频道交互
                * 交互节点的"看法"和自身信息
            + 每 1秒每个sentinel对其他sentinel和redis执行ping
                * 心跳检测,失败判定依据    
        + 主观下线 和 客观下线  
            + 主观下线
                * 每个sentinel节点对redis节点失败的'偏见'
            + 客观下线
                * 所有sentinel节点对redis节点失败'达成共识'(超过quorum个统一)
                * sentinel is-master-down-by-addr
        + 领导者选举
            + 原因
                * 只有一个sentinel节点完成故障转移
            + 选举
                * 通过sentinel is-master-down-by-addr命令都希望成为领导者
                + 过程
                    * 每个做主观下线的sentinel节点向其他sentinel节点发送命令,要求将它设置为领导者
                    * 收到命令的sentinel节点如果没有同意过其他sentinel节点发送的命令,那么将同意该请求,否则拒绝
                    * 如果该sentinel节点发现自己的投票数已经超过sentinel集合半数且超过quorum,那么它将成为领导者
                    * 如果此过程中多个sentinel节点成为领导者,那么将等待一段时间重新进行选举
        + 故障转移(对于主节点)
            * 从slave节点中选出一个'合适的'节点作为新的master节点
            * 对上面的slave节点执行slaveof no one命令让其成为master节点
            * 向剩余的slave节点发送命令,让他们成为新的master节点的slave节点,复制规则和parallel-syncs参数有关
            * 更新原来master节点配置为slave,并保持对其'关注',当其恢复后命令它去复制新的master节点
            + 选择'合适'节点
                * 选择slave-priority最高的节点,如果存在则返回,不存在继续
                * 选择复制偏移量最大的slave节点,如果存在则返回,不存在则继续
                * 选择run_id最小的节点                                       
    + 常见开发运维问题 
        + 节点运维
            + 节点上线,下线
                + 下线
                    + 主节点
                        * sentinel failover <masterName>
                    + 从节点
                        * 临时下线还是永久下线,例如是否做一些清理工作.但是要考虑读写分离的情况 
                    + sentinel节点
                        * 临时下线还是永久下线,例如是否做一些清理工作
                + 上线
                    + 主节点
                        * sentinel failover 进行替换
                    + 从节点
                        * slaveof(replicaof)即可,sentinel节点可感知到 
                    + sentinel节点
                        * 参考其它sentinel节点启动即可     
            + 原因
                * 机器下线:例如过保等情况
                * 机器性能不足:例如硬件性能不足
                * 节点自身故障:例如服务不稳定    
        + 高可用读写分离
            * JedisSentinelPool     
            + 从节点的作用
                * 副本:高可用的基础
                * 扩展:读能力
                + 三个"消息"(对于从节点,主节点由sentinel来管理)
                    * +switch-master:切换主节点(从节点晋升为主节点)
                    * +convert-to-slave:切换从节点(原主节点降为从节点)
                    * 主观下线
## 初识Redis Cluster 
    + 背景
        + 需求
            * 并发量
                * 10万条/秒 
            * 数据量
            * 网络流量
        + 解决办法
            * 加机器
        + 集群
            * 规模化需求
            * 并发量:OPS
            * 数据量:'大数据'        
    + 数据分布
        + 数据分区
            + 顺序分区
                + 特点
                    * 数据分散度易倾斜
                    * 键值业务相关
                    * 可顺序访问
                    * 不支持批量操作
                + 典型产品
                    * BigTable
                    * HBase    
            + 哈希分区
                + 特点
                    * 数据分散度高
                    * 键值分布业务无关
                    * 无法顺序访问
                    * 支持批量操作
                + 典型产品
                    * 一致性哈希Memcache
                    * Redis Cluster
                    * 其它缓存产品
                + 三种情况
                    + 客户端分区
                        + 节点取余分区
                            * hash(key)%机器数
                            + 问题
                                * 节点伸缩:数据节点关系变化,导致数据迁移
                                * 不成倍扩充机器:数据迁移量大
                                * 多倍扩容:数据迁移量会减小很多(相对成倍)
                        + 一致性哈希分区
                            * 哈希 + 顺时针(优化取余)
                            * 节点伸缩:只影响临近节点,但是还有数据迁移
                            * 翻倍伸缩:保证最小迁移数据和负载均衡
                    + 服务端分区        
                        + 虚拟槽分区    
                            *　预设虚拟槽：每个槽映射一个数据子集,一般比节点数大
                            * 良好的哈希函数：例如 CRC16
                            * 服务端管理节点,槽,数据:例如 Redis Cluster   
                        
                        
    + 搭建集群
        +　Redis Cluster 架构
            * 节点
            * meet
            * 指派槽
            * 复制
        + Redis Cluster 特性
            * 复制
            * 高可用
            * 分片 
        + 安装
            + 原生安装
                * 配置开启节点
                + meet
                    * cluster meet ip port
                + 指派槽(需要脚本控制循环)
                    * cluster addslots slot [slot...]
                + 主从分派
                    * cluster replicate node-id 
            + 官方工具安装
                * src/redis-cli --cluster create   127.0.0.1:8000 127.0.0.1:8001 127.0.0.1:8002 127.0.0.1:8003 127.0.0.1:8004 127.0.0.1:8005 --cluster-replicas 1          
    + 集群伸缩
        + 扩容
            + 准备新节点
                + 新节点
                    * 集群模式
                    * 配置和其它节点统一
                    * 启动后是孤儿节点
            + 加入集群
                + 手动
                    * host1 port1> cluster meet host2 port2  
                + 作用
                    * 为他迁移槽和数据实现扩容(作为主节点)
                    * 作为从节点负责故障转移(作为从节点)
                + 使用官方工具    
                    * 加入集群:src/redis-cli --cluster add-node new_host:new_port existing_host:existing_port
                    * 设置主从:从节点host:port>cluster replicate masterNodeId
            + 迁移槽和数据
                + 原理及手动迁移
                    * 槽迁移计划
                    + 迁移数据
                        * 对目标节点发送: cluster setslot {slot} importing {sourceNodeId} 命令,让目标节点准备导入槽的数据
                        * 对源节点发送:cluster setslot {slot}  migrating {targetNodeId} 命令,让源节准备迁出槽的数据 
                        * 源节点循环执行: cluster getkeyinslot {slot} {count} 命令,每次获取count 个 属于槽的键.
                        * 在源节点执行: migrate {targetIp} {targetPort} key 0 {timeout} 命令把key 迁移
                        * 重复执行3~4,直到槽下所有的键值数据迁移到目标节点
                        * 向集群内所有主节点发送 cluster setslot {slot} node {targetNodeId} 命令,通知槽分配给目标节点
                    * 添加从节点
                + 自动迁移
                    * src/redis-cli --cluster reshard 127.0.0.1:8000
                    * 根据提示完成相应操作     
        + 缩容
            + 原理及手动
                + 下线迁移槽
                + 忘记节点
                    + 手动(需要对每一个节点执行)
                        * redis-cli>cluster forget {downNodeId}
                + 关闭节点
            + 自动
                + 转移节点
                    * src/redis-cli --cluster reshard 127.0.0.1:8000 --cluster-from sourceNodeId
                        --cluster-to targetNodeId --cluster-slot slot-count
                + 下线
                    * src/redis-cli --cluster del-node targetNodeId
                    * 先删除从节点,以免先删除主节点时进行故障转转移                                                   
    + 客户端路由
        + moved 重定向
            * 当前键值不在节点所分配的槽内
        + ask 重定向
            * 扩容or缩容引发键值随节点转移
        + smart 客户端
            + 原理:追求性能
                * 从集群中选个可运行节点,使用Cluster slots 初始化槽 和节点映射
                * 将cluster slots的结果映射到本地,为每个节点创建JedisPool
                * 准备执行命令
            + 使用
                * JedisCluster 
        + 批量操作
            * mget,mset (操作的key 必须在同一个槽)
            + 串行mget
                * 优点:编程简单,少量keys满足需求
                * 缺点:大量keys请求延迟严重
                * 网络IO:O(keys)
            + 串行IO
                * 优点:编程简单,少量节点满足需求
                * 缺点:大量node延迟严重
                * 网络IO:O(nodes)
            + 并行IO
                * 优点:利用并行特性,延迟取决于最慢的节点
                * 缺点:编程复杂,超时定位问题难
                * 网络IO:O(max_slow(node))
            + hash_tag
                * 优点:性能最高
                * 缺点:读写增加tag维护成本,tag分布易出现数据倾斜
                * 网络IO:O(1)
    + 故障转移
        + 故障发现 
            * 通过ping/pong消息实现故障发现:不需要sentinel
            + 主观下线和客观下线
                + 主观下线
                    * 某个节点认为另一个节点不可用,'偏见'
                + 客观下线
                    * 当半数以上持有槽的主节点都标记某节点主观下线
                    + 尝试客观下线
                        * 通知集群内所有标记故障节点为客观下线
                        * 通知故障节点的从节点触发故障转移流程
        + 故障恢复
            + 资格审查
                * 每个从节点检查与故障主节点的断线时间
                * 超过cluster-node-timeout * cluster-replica-validity-factor取消资格
            * 准备选举时间
            * 选举投票
            + 替换主节点
                * 当前从节点取消复制变为主节点(slaveof no one)
                * 执行clusterDelSlot撤销故障主节点负责的槽,并执行clusterAddSlot把这些槽分配给自己
                * 向集群广播自己的pong消息,表明已经替换了故障从节点 
        + 故障转移演练                            
    + 集群原理
    + 开发运维常见问题 
        + 集群完整性
            + cluster-require-full-coverage 默认为yes
                * 集群中16384个槽全部可用:保证集群完整性
                * 节点故障或者正在故障转移
                    (error)CLUSTERDOWN the cluster is down
            * 大多数业务无法容忍,cluster-require-full-coverage 建议设置为 no        
        + 带宽消耗
            * 官方建议1000个节点
            * PING/PONG消息
            * 不容忽视的带宽消耗
            + 三个方面消耗带宽
                * 消息发送频率:节点发现与其他节点最后通信时间超过 cluster-node-timeout/2 时会直接发送ping消息
                * 消息数据量:slots槽数组(2KB空间)和整个集群1/10的状态数据(10个节点状态数据约1KB)
                * 节点部署的机器规模:集群分布的机器越多且每台机器划分的节点数越均匀,则集群内整体的可用带宽越高
            + 优化
                * 避免'大'集群:避免多业务使用一个集群,大业务可以多集群
                * cluster-node-timeout:带宽和故障转移速度的均衡
                * 尽量均匀分配到多机器上:保证高可用和带宽    
        + Pub/Sub广播
            + 问题
                * publish 在集群每个节点广播:加重带宽负担
            + 解决
                * 单独'走'一套Redis Sentinel     
        + 数据倾斜
            + 数据倾斜
                * 内存倾斜
                + 原因
                    + 节点和槽分配不均匀
                        * src/redis-cli --cluster info ip:port 查看节点,槽,键值分布
                        * src/redis-cli --cluster reblance ip:port 进行均衡(谨慎使用)
                    + 不同槽对应键值数量差异比较大
                        * CRC16 正常情况下比较均匀
                        * 可能存在hash_tag
                        * cluster countkeysinslot {slot} 获取槽对应键值个数
                    + 包含bigkey
                        * bigkey: 大字符串, 几百万的元素的hash,set 等等
                        * 从节点: src/redis-cli -c -p port --bigkeys
                        * 优化: 优化数据结构
                    + 内存相关配置不一致
                        * hash-max-ziplist-value,set-max-intset-entries等
                        * 优化: 定期"检查"配置一致性
            + 请求倾斜
                * 热点key: 重要的key 或者 bigkey
                + 优化
                    * 避免bigkey
                    * 热键不要使用hash_tag
                    * 当一致性不高时,可以使用本地缓存 + MQ    
        + 读写分离
            + 只读连接
                * 集群模式下的从节点不接受任何读写请求
                * 重定向到负责的主节点
                * readonly命令可以读:连接级别命令
            + 读写分离
                * 更加复杂
                * 同样的问题: 复制延迟,读取过期数据,从节点故障
                * 修改客户端: cluster slaves {nodeId} (不建议用)    
        + 数据迁移 
            + 离线/在线迁移
                + 官方迁移工具: src/redis-cli --cluster import(通过src/redis-cli --cluster help查看拥有哪些命令) 
                    * 只能从单机迁移到集群
                    * 不支持在线迁移: source需要停写
                    * 不支持断点续传
                    * 单线程迁移:影响速度
                + 在线迁移
                    - 唯品会 redis-migrate-tool
                    - 豌豆荚: redis-port    
        + 集群vs单机
            + 集群限制
                * key 批量操作支持有限: 例如mget,mset 必须在一个 slot
                * key事务和lua支持有限:操作的key必须在一个节点
                * key是数据分区的最小粒度:不支持bigkey分区
                * 不支持多个数据库:集群模式下只有一个 db 0
                * 复制只支持一层:不支持树形复制结构
            + 分布式Redis 不一定好
                + Redis Cluster: 满足容量和性能的扩展性,很多业务"不需要"
                    * 大多数客户端性能会降低
                    * 命令无法跨节点使用:mget,mset,keys,scan,flush,sinter 等
                    * lua和事务无法跨节点使用
                    * 客户端维护更复杂: SDK和应用本身消耗(例如更多的连接池)
                * 很多场景Redis Sentinel已经足够好         
## 缓存设计与优化  
    + 缓存的受益和成本
        + 受益
            + 加速读写
                * 通过缓存加速读写速度:CPU L1/L2/L3 Cache, Linux page Cache 加速硬盘读写,浏览器缓存,Ehcache缓存数据库结果
            + 降低后端负载
                * 后端服务器通过前端缓存降低负载:业务端使用Redis降低后端MYSQL负载等
        + 成本
            + 数据不一致
                * 缓存层和数据层有时间窗口不一致,和更新策略有关
            + 代码维护成本
                * 多了一层缓存逻辑
            + 运维成本
                * 例如 Redis Cluster        
        + 使用场景
            + 降低后端负载
                * 对高消耗的 SQL: join结果集/分组统计结果缓存
            + 加速请求响应
                * 利用Redis/Memcache优化IO响应时间
            + 大量写合并为批量写
                * 如计数器先Redis累加再批量写DB              
    + 缓存的更新策略
        + LRU/LFU/FIFO算法剔除
            * 例如max-memory-policy
            * 一致性:最差
            * 维护成本:低
        + 超时剔除
            * 例如Expire
            * 一致性:较差
            * 维护成本:低
        + 主动更新
            * 开发控制生命周期    
            * 一致性:强
            * 维护成本:高
        + 使用建议
            + 低一致性
                * 最大内存和淘汰策略
            + 高一致性
                * 超时剔除和主动更新结合,最大内存和淘汰策略兜底
    + 缓存粒度问题
        + 三个角度
            * 通用性:全量属性更好
            * 占用空间:部分属性更好
            * 代码维护:表面上全量属性更好
    + 缓存穿透问题
        * 大量请求不命中
        + 原因
            * 业务代码自身问题
            * 恶意攻击,爬虫
        + 如何发现
            * 业务的响应时间
            * 业务本身问题
            * 相关指标:总调用数,缓存层命中数,存储层命中数
        + 解决方法
            + 缓存空对象
                + 问题
                * 需要更多的键
                * 缓存层和存储层数据"短期"不一致
            + 布隆过滤器拦截
                + 要求
                    * 数据集相对固定
                    * 可以更新布隆过滤器                
    + 缓存雪崩优化
        + 问题描述
            * 由于 cache 服务承载大量请求,当cache服务异常/脱机,流量直接压向后端组件(DB等),造成级联故障
        + 优化方案
            + 保证缓存高可用
                * 个别节点,个别机器,甚至是个别机房
                * 例如Redis Cluster,Redis Sentinel,VIP
            * 依赖隔离组件为后端限流
            * 提前演练:压测等    
    + 无底洞问题
        + 问题描述
            * 2010年,Facebook有了3000个Memcache节点
            * 发现问题: '加'机器性能没能提升,反而下降　
        + 问题关键
            * 更多机器 != 更高性能
            * 批量接口需求(mget,mset等)
            * 数据增长与水平扩展需求
        + 优化 IO
            + 命令本身优化:
                * 例如慢查询keys,hgetall bigkey   
                * 减少网络通信次数
                + 降低接入成本
                    * 客户端长连接/连接池, NIO    
    + 热点key的重建优化
        + 问题描述
            * 热点key+较长的重建时间
        + 三个目标
            * 减少重建缓存次数
            * 数据尽可能一致
            * 减少潜在风险
        + 两种解决
            + 互斥锁
                + 优点
                    * 思路简单
                    * 保证一致性
                + 缺点
                    * 代码复杂度增加
                    * 存在死锁风险
            + 永远不过期
                + 缓存层面
                    * 没有设置过期时间(没有用expire)
                + 功能层面
                    * 为么个value添加逻辑过期时间,但发现超过逻辑过期时间后,会使用单独的线程去重建缓存
                + 优点
                    * 基本杜绝热点key重建问题
                + 缺点
                    * 不保证一致性
                    * 逻辑过期时间增加维护成本和内存成本
## Redis云平台CacheCloud   
    + Redis规模化困扰
    + 快速构建
    + 机器部署
    + 应用接入
    + 用户功能 
    + 运维功能
## Redis布隆过滤器 
## 内存管理 
## 开发运维常见坑   