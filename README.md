# lightconf
## 配置中心，实现方案

#### 说明

##### 1. 存储系统

其实配置文件不外乎就是一个key-value的配置项集合。当然，如果要支持多个环境，还有分组，顶多也就是加多几个字段，总体来说是一个非常简单的数据模型。另外，配置项更新并不会很频繁，配置生效时间其实要求也不会太高，数据量也不会很大。事务性方面要求会比较高的，丢失，不一致对于配置项来说是不可接受的。

所以，用普通的DB如MySQL就可以了。很多开源的配置中心采用zookeeper，其实是想利用ZK的订阅功能，实现配置更新通知客户端。ZK当然是可以的，但是个人觉得这里有点大材小用。

可以使用本地内存或者Redis缓存提高查询性能。或者像阿里的Diamond，定时将配置项dump到本地文件。另外，客户端也应该做缓存，一来减少对server的压力，二来server挂掉还可以容灾。

##### 2. 配置下发应用

以前的配置是随着应用一起发布的，应用启动时候加载本地配置文件的。现在配置数据统一发布到配置中心，所以需要一种机制讲数据下发到应用。

有两种方式：

推：这个实时性最高，但是需要应用与配置中心保持长连接，netty实现是个不错的选择。
拉：实时性相对差一些，如果不做增量更新的话对配置中心也会造成不必要的压力。不过实现会简单很多。
个人认为对于配置中心这种业务场景，拉的方式其实是足够的。另外，采用推的方式，也可以在启动的时候全量拉取，后续只下推变更数据。

##### 3. 变更通知

如果是推送的方式，那么server可以把每次变更都即使发送给订阅的客户端。 如果是拉取的方式，则可以通过比较client和server的数据的MD5值来实现有效变更通知。server会下发数据和md5给client，client请求的时候会带上md5，如果md5变化，则server会重新下发数据，否则返回“无变更”的状态码给client。

##### 4. 灰度发布

有时候我们需要先把配置项灰度到某台机器看看效果如何，再决定是要全量还是回滚。

有两种解决方案：

1，配置项增加一个host属性，表示这个配置项只“发布”给某些IP。
2，定义一个优先级，客户端优先加载本地配置文件，这样如果某些机器上的应用需要特殊配置，那么可以采用老的方式上去修改其本地配置文件。
Diamond就是采用第二种方式的，这种方式还有一个好处就是兼容了老的配置方式，可以做到无缝过渡。另外，本地配置文件也起到一个本地容灾的作用。