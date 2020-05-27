# SpringBoot的定时任务的加强工具
SpringBoot的定时任务的加强工具，实现定时任务动态管理,完全兼容原生@Scheduled注解,无需对原本的定时任务进行修改

## 引入jar包
```xml
<dependency>
    <groupId>com.github.guoyixing</groupId>
    <artifactId>spring-boot-starter-super-scheduled</artifactId>
    <version>0.1.0</version>
</dependency>
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
      superScheduledManager.addScheduled(name, "0/2 * * * * ?");
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
## 版本更新
### 0.1.0版
* 只兼容原生@Scheduled注解cron属性
### 0.2.0版
* 完全兼容原生@Scheduled注解

## 后续计划
* 后续加入可视化管理
* 调度日志
* 集群任务统一管理
