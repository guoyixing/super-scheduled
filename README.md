# SpringBoot的定时任务的加强工具
SpringBoot的定时任务的加强工具，实现定时任务动态管理,完全兼容原生@Scheduled注解,无需对原本的定时任务进行修改

## 引入jar包
```xml
<dependency>
    <groupId>com.github.guoyixing</groupId>
    <artifactId>spring-boot-starter-super-scheduled</artifactId>
    <version>0.2.1</version>
</dependency>
```
## 配置参数
### 1.配置定时任务线程池（不配置时采用默认参数）
```properties
#线程池大小
spring.super.scheduled.thread-pool.poolSize=30
#线程名前缀
spring.super.scheduled.thread-pool.threadNamePrefix=super
#设置是否关闭时等待执行中的任务执行完成
spring.super.scheduled.thread-pool.waitForTasksToCompleteOnShutdown=false
#设置此执行器被关闭时等待的最长时间，用于在其余容器继续关闭之前等待剩余任务执行完成
#需要将waitForTasksToCompleteOnShutdown设置为true，此配置才起作用
spring.super.scheduled.thread-pool.awaitTerminationSeconds=0
```

## 使用样例
### 1.正常使用springScheduled
```java
@SpringBootApplication
@EnableScheduling
public class TestApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }
}
```
```java
@Component
public class TestTask {
    private DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Scheduled(cron = "0/2 * * * * ?")
    public void robReceiveExpireTask() {
        System.out.println(df.format(LocalDateTime.now()) + "测试测试");
    }
}
```
### 2.定时任务动态管理
```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestApplicationTests {
  //直接注入管理器
  @Autowired
  private SuperScheduledManager superScheduledManager;

  @Test
  public void test() {
      //获取所有定时任务
      List<String> allSuperScheduledName = superScheduledManager.getAllSuperScheduledName();
      String name = allSuperScheduledName.get(0);
      //终止定时任务
      superScheduledManager.cancelScheduled(name);
      
      try {
          Thread.sleep(5000);
      } catch (InterruptedException e) {
          e.printStackTrace();
      }

      System.out.println("任务名：" + name);
      //启动定时任务
      superScheduledManager.addCronScheduled(name, "0/2 * * * * ?");
      //获取启动汇总的定时任务
      List<String> runScheduledName = superScheduledManager.getRunScheduledName();
      runScheduledName.forEach(System.out::println);

      try {
          Thread.sleep(10000);
      } catch (InterruptedException e) {
          e.printStackTrace();
      }
      //修改定时任务执行周期
      superScheduledManager.setScheduledCron(name, "0/5 * * * * ?");
  }
}
```
### 3.管理器功能
#### 3.1 获取所有定时任务
```java
List<String> allSuperScheduledName = superScheduledManager.getAllSuperScheduledName();
```
#### 3.1 获取所有启动的定时任务
```java
List<String> runScheduledName = superScheduledManager.getRunScheduledName();
```
#### 3.2 终止定时任务
```java
superScheduledManager.cancelScheduled(name);
```
#### 3.3 cron类型操作
```java
//以cron模式启动定时任务
superScheduledManager.addCronScheduled(name, "0/2 * * * * ?");
//将定时任务转为cron模式运行，并修改cron的参数值
superScheduledManager.setScheduledCron(name, "0/2 * * * * ?");
```
#### 3.4 FixedRate类型操作
```java
//以FixedRate模式启动定时任务
//上一次开始执行之后2秒后再执行，首次运行延迟1秒
superScheduledManager.addFixedRateScheduled(name, 2000L,1000L);
//首次运行不进行延迟
superScheduledManager.addFixedRateScheduled(name, 2000L);
//将定时任务转为FixedRate模式运行，并修改执行间隔的参数值
superScheduledManager.setScheduledFixedRate(name, 2000L);
```
#### 3.5 FixedDelay类型操作
```java
//以FixedDelay模式启动定时任务
//上一次执行完毕之后2秒后再执行，首次运行延迟1秒
superScheduledManager.addFixedDelayScheduled(name, 2000L,1000L);
//首次运行不进行延迟
superScheduledManager.addFixedDelayScheduled(name, 2000L);
//将定时任务转为FixedDelay模式运行，并修改执行间隔的参数值
superScheduledManager.setScheduledFixedDelay(name, 2000L);
```
#### 3.6 手动执行一次
```java
superScheduledManager.runScheduled(name);
```
### 4.Api接口
#### 4.1 获取所有定时任务
`GET` /scheduled/name/all
#### 4.2 获取启动的定时任务
`GET` /scheduled/name/run
#### 4.3 手动执行一次任务
`POST` /scheduled/{name}/run
#### 4.4 终止定时任务
`DELETE` /scheduled/{name}
#### 4.5 cronApi
##### 4.5.1 以cron类型启动Scheduled
`POST` /scheduled/cron/{name}/add <br/>
参数：`text` \[cron\]
##### 4.5.2 将定时任务转为cron模式运行，并修改cron的参数值
`POST` /scheduled/cron/{name}/set <br/>
参数：`text` \[cron\]
#### 4.6 fixedDelayApi
##### 4.6.1 以FixedDelay模式启动定时任务
`POST` /scheduled/fixedDelay/{name}/add/{fixedDelay}/{initialDelay}
##### 4.6.2 以FixedDelay模式启动定时任务（不延迟）
`POST` /scheduled/fixedDelay/{name}/add/{fixedDelay}
##### 4.6.3 将定时任务转为FixedDelay模式运行，并修改执行间隔的参数值
`POST` /scheduled/fixedDelay/{name}/set/{fixedDelay}
#### 4.7 fixedRateApi
##### 4.7.1 以FixedRate模式启动定时任务
`POST` /scheduled/fixedRate/{name}/add/{fixedRate}/{initialDelay}
##### 4.7.2 以FixedRate模式启动定时任务（不延迟）
`POST` /scheduled/fixedRate/{name}/add/{fixedRate}
##### 4.7.3 将定时任务转为FixedRate模式运行，并修改执行间隔的参数值
`POST` /scheduled/fixedRate/{name}/set/{fixedRate}


## 版本更新
### 0.1.0版
* 只兼容原生@Scheduled注解cron属性
### 0.2.0版
* 完全兼容原生@Scheduled注解
### 0.2.1版
* 完善Manager功能
* 修复大量bug
### 0.3.0版
* 添加api接口
* 添加定时任务线程池配置

## 后续计划
* 后续加入可视化管理
* 调度日志
* 集群任务统一管理
