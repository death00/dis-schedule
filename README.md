# dis-schedule
简单的分布式定时调度组件

基于注解实现分布式定时调度，借用数据库或redis实现分布式锁，弥补了spring boot本身`@Scheduled`不支持分布式的缺点，且较为简单，容易上手。

## 使用

### 配置阶段

#### Bean的注入

参考`top.death00.dis.schedule.config`下的`ServiceConfig`，mongodb和redis的实现方式选择一种。

#### 使用注解

在正常的定时任务上使用注解`@DisSchedule`，定义任务的名称、间隔时间、间隔时间单位

```java
    @DisSchedule(name = "testSchedule", duration = 1, unit = DisScheduleUnit.MINUTES)
    @Scheduled(cron = "0 0/1 * * * ?")
    public void testSchedule() {
        logger.info("输出");
    }
```

#### 配置serverName

参考`resources`中任意一个目录下的`example.conf`，在文件中配置好当前服务的serverName，要求不能重名。
```
serverName=schedule1
```

### 启动阶段

#### /schedule/add/serverName

使当前server生效，可以执行定时调度任务

#### /schedule/remove/serverName

使当前server失效，无法执行定时调度任务

## 分布式锁

### mongodb

>数据库的分布式锁是依赖于唯一索引，表是`disScheduleRecord`，索引是`date`(任务执行所属的时间)和`name`(服务的serverName)。
>
>当多个服务在向数据库插入数据时，只有一个服务是成功的，和它同时执行插入的服务将抛出异常`DuplicateKeyException`，在它之后慢一点执行的将直接查询就失败。

因为现在数据库用的是`mongodb`，采用的语句是`update({query}, {$setOnInsert : {}})`，`mongodb`在执行的时候是分为两步:

1. 查询数据库是否存在匹配query条件的语句，如果有，直接失败，不抛异常，返回值`UpdateResult`的`upsertedId`为null

2. 如果query条件没有匹配的数据，则执行`setOnInsert`操作

这样的话，当有多个语句同时执行的话，可能存在同时有几条语句的`query`条件都满足，因此都会执行`setOnInsert`，这就需要`唯一索引`来保证了。-

### redis

1. key为`disScheduleServerName`的set，存储线上有效的serverName。
2. 通过`sismeber`命令确定`disScheduleServerName`是否含有当前server，从而决定是否执行。
3. 通过`setNx`争抢分布式锁，`value`为服务名，设置成功的服务执行。