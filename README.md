# SpringBoot的定时任务的加强工具
SpringBoot的定时任务的加强工具，实现定时任务动态管理,兼容原生springScheduled无需对原本的定时任务进行修改

## 引入jar包
导入项目 &emsp;&emsp;`maven仓库正在审核`
```xml
<dependency>
    <groupId>com.gyx.scheduled</groupId>
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


## 后续计划
* 完成springScheduled全兼容，目前只兼容cron
* 后续加入可视化管理
* 调度日志
* 集群任务统一管理
