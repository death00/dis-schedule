# dis-schedule
简单的分布式定时调度组件

基于注解实现分布式定时调度，借用数据库或redis实现分布式锁，弥补了spring boot本身`@Scheduled`不支持分布式的缺点，且较为简单，容易上手。

## 使用

在正常的定时任务上使用注解`@DisSchedule`，定义任务的间隔时间

```java
    @DisSchedule(duration = 1)
    @Scheduled(cron = "0 0/1 * * * ?")
    public void testSchedule() {
        logger.info("输出");
    }
```
