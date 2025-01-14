# 从0开始手写一个Spring

![Java Version](https://img.shields.io/badge/Java%20Version-17%2B-green)

> 作为一名Java程序员，每天总是和业务逻辑打交道。
>
> 时间久了，也自然会觉得有些许枯燥。
>
> 所以，我打算从零开始，手写一个自己Spring。
>
> 由于我的基础薄弱，想要完全从0开始写难免有些困难。
>
> 所以决定学习廖雪峰的Spring教程，一步步来实现一个完整的Spring。
>
> 希望自己能从中学到很多知识，也能帮助到大家。
>
> 学习参考链接：https://liaoxuefeng.com/books/summerframework/introduction/index.html

## Day 0: Spring简要回顾

Spring最核心的部分就是IOC和AOP，即控制反转和面向切面编程。

IOC是一种设计思想，它可以用来降低组件之间的耦合度，让对象之间的依赖关系交给Spring来管理。

AOP是一种编程思想，它可以用来在不修改代码的情况下，增加额外的功能。

Spring的核心组件有：

- Spring Core：Spring的核心模块，包括IoC和DI。
- Spring AOP：Spring的面向切面编程模块，包括AspectJ。
- Spring Web：Spring的Web模块，包括Spring MVC。
- Spring Data：Spring的数据访问模块，包括Spring Data JPA和Spring Data JDBC。
- Spring Boot：Spring Boot可以快速构建单个、微服务或整体应用。

等等，最重要的就是Core和AOP模块了，所以我们首先就要写好IOC和AOP的部分。

## Day 1: 实现IOC容器（1）

### 1. 实现类扫描 @ComponentScan注解

@ComponentScan注解可以扫描指定包下的所有@Component注解的类。

我们要实现类似这个注解的功能，就要通过类加载器，获取到指定路径下的所有类。然后解析类的路径。

- **先自定义一个资源**

![image-20240828172129213](https://s2.loli.net/2024/08/28/N9sEKnXqhjf5oCz.png)

- **然后创建一个资源加载器**

![image-20240828172339208](https://s2.loli.net/2024/08/28/flzONAMJy8Fjain.png)

- **通过获取类加载器的方法，加载具体的类**

![image-20240828172426597](https://s2.loli.net/2024/08/28/n9EeXzM2ahpFULV.png)

![image-20240828172500789](https://s2.loli.net/2024/08/28/sv4UOgm1TIfNCyJ.png)

- **通过测试**

![image-20240828172700512](https://s2.loli.net/2024/08/28/b2PSu8OZtUYi1JB.png)

## Day 2: 实现IOC容器（2）

### 2. 实现属性注入@Value注解

在spring中的属性，一般是键值对的形式。`eg：spring.datasource.name:mysql`

所以我们通过Map来实现属性保存和注入

- **属性保存**

![image-20240829173057011](https://s2.loli.net/2024/08/29/aTh7sveVdpg8MDx.png)

在Spring中，属性在xml文件中；在SpringBoot中，属性在yaml文件中。所以这两种方式我们都要解析

- **通过解析类路径，读取指定路径下的属性配置文件**

![image-20240829175533382](https://s2.loli.net/2024/08/29/KnkZLmEc3U1qzsV.png)

- **如果是yaml文件，就将文件中的属性配置转化为扁平的Map对象**

![image-20240829175958059](https://s2.loli.net/2024/08/29/OhH1e9bKLyxPCnZ.png)

## Day 3: 实现IOC容器（3）

### 3. 创建BeanDefinition

我们知道在Spring中，Bean有多种类型。

但这里我们为了简化，只给他一种类型，并且只有一种命名方式。

由此我们也可以想到，可以用一个Map来存所有的Bean，并且通过注解的方式创建和使用

- **创建注解**

![image-20240831201408684](https://s2.loli.net/2024/08/31/EdcBWNT7UAvCu3J.png)

- **作用于注解的方法示例**（通过类反射机制获取到注解和注解里面的变量值）

![image-20240901104021321](https://s2.loli.net/2024/09/01/pNgcEwKf2zqMTnt.png)

- **采用一个Map来管理所有的Bean**

![image-20240901181618974](https://s2.loli.net/2024/09/01/cbWsURTNFg5hYlz.png)

- **在构造方法中，通过扫描指定package下的所有类，然后将其中的Bean都注入到beans这个Map容器中**

![image-20240901191857117](https://s2.loli.net/2024/09/01/VhQUNmfW7C9inoA.png)

- **在扫描时，先要获取到启动类的@ComponentScan注解，然后获取注解的包路径，然后用之前实现的ResourceResolver类扫描**

![image-20240901192609617](https://s2.loli.net/2024/09/01/9Hwi2QTkaAoEcfj.png)

- **已经获得到了所有的类，接下来就判断这些类是否是Bean，如果是的话，就将他们加入到容器中**

![image-20240901193210274](https://s2.loli.net/2024/09/01/8m5zIjFfLuXsbDn.png)

- 自定义一个Application以后通过测试

![image-20240901194922523](https://s2.loli.net/2024/09/01/K7wnFXgEhlMAmUN.png)

## Day 4: 实现IOC容器（4）

### 4. 创建Bean实例

> 所以，对于IoC容器来说，创建Bean的过程分两步：
>
> 1. 创建Bean的实例，此时必须注入强依赖；
> 2. 对Bean实例进行Setter方法注入和字段注入。
>
> 第一步如果遇到循环依赖则直接报错，第二步则不需要关心有没有循环依赖。
>
> ——By：廖雪峰

我们是分这两步对Bean进行创建，原因如下：

1. 如果第一步遇到循环依赖的问题，那么是无法解决的。假如我们创建A B两个对象，B的创建需要A，A的创建需要B，那么谁也创建不了
2. 如果我们第二步遇到循环依赖的问题，那么就不用担心，因为我用A给B赋值的时候，A已经存在了，在程序上是走得通的

我们现在要进行第一步，对Bean的创建，那么我们就相当于**人为注入了一个强依赖**，需要进行循环依赖的判断

- **新建一个集合，用来检测依赖循环**

![image-20240910172213561](https://s2.loli.net/2024/09/10/XGF4Kk6as5UxWE3.png)

- **创建Bean**

![image-20240910172329236](https://s2.loli.net/2024/09/10/slQXgIFNeZaW1xB.png)

我们这里为了简化，只创建单例模式的Bean

- **创建单例模式的Bean**

![image-20240910172651138](https://s2.loli.net/2024/09/10/wZupmocFNdiVHkn.png)

- **注入属性**

![image-20240911143657149](https://s2.loli.net/2024/09/11/6CdHZJEODy4LRUK.png)

在注入属性时，需要先检查属性是否合法，然后再通过propertyResolver获取@Value注解对应的属性值，将其注入到具体的字段/方法当中

![image-20240911145802596](https://s2.loli.net/2024/09/11/nJ3FXA2QTpY7Sxa.png)

如果是@Autowired注入，则需要先通过反射获取到对应的Bean，然后进行注入

![image-20240911150110205](https://s2.loli.net/2024/09/11/koihUfR8caKWHdM.png)

自定义Bean后进行测试

![image-20240911151459521](https://s2.loli.net/2024/09/11/i6xuVvIep4lS1X2.png)

## Day 5: 实现IOC容器（5）

### 5. 初始化Bean

我们已经完成了强依赖注入 现在我们需要进行弱依赖注入

> **强依赖注入**：依赖项在对象创建时必须提供，通常使用构造函数注入或方法注入。这种方式提供了更高的代码稳定性和可测试性。
>
> **弱依赖注入**：依赖项是可选的，通常使用Setter方法注入或`@Autowired`的`required`属性为`false`。这种方式提供了更大的灵活性，但可能会增加处理依赖缺失的复杂性。
>
> ——By：ChatGPT

这里我们只需要查找`@Value`和`@Autowired`注入对应方法即可

- **注入字段属性，调用初始化方法**

![image-20240912102901335](https://s2.loli.net/2024/09/12/FiCWr3y14ekv2JY.png)

- **注入属性调用现有字段方法，初始化调用实例的初始化方法**

![image-20240912103006019](https://s2.loli.net/2024/09/12/dTr1RgniuSQv3VM.png)

- **注入属性时，扫描字段属性进行注入**

![image-20240912103038607](https://s2.loli.net/2024/09/12/WMx2GbqLETHOjeN.png)

- **调用初始化方法时，如果已有方法，那么立即执行；如果没有传入，需要先通过类扫描工具扫描到具体方法后调用**

![image-20240912103207262](https://s2.loli.net/2024/09/12/3uZzlU2dKPjAWoh.png)

- 测试并通过

![image-20240912104307614](https://s2.loli.net/2024/09/12/E96vJZVgx4GLsAB.png)

## Day 6: 实现IOC容器（6）

### 6. 实现BeanPostProcessor

> `BeanPostProcessor` 是 Spring 框架中的一个接口，它允许你在 Spring 容器管理的 Bean 实例化之后、初始化之前，以及在整个生命周期中进行自定义的处理。这个接口为开发者提供了一个强大的机制，用于在 Bean 的创建和初始化过程中执行额外的操作。
>
> ——By：ChatGPT

因为BeanPostProcessor是一个接口，所以我们在项目中只需要定义一个接口即可，然后再测试类中实现接口并测试具体功能

![image-20240914213920214](https://s2.loli.net/2024/09/14/zyvTGP7H3hoNipj.png)

- **在ApplicationContext中添加用来存放BeanPostProcessor的容器**

检测所有的Bean，看他们是否是BeanPostProcessor类型的Bean，如果是就加入到专门的容器里

![image-20240918192223907](https://s2.loli.net/2024/09/18/KbFe3zZLyicRXOu.png)

基于Class类的方法进行判断

![image-20240918192305260](https://s2.loli.net/2024/09/18/X8HqCnAjeQ7l6D4.png)

- **如果BeanPostProcessor替换了原有Bean，需要在对应BeanDefination里面进行实例的修改**

![image-20240918193658812](https://s2.loli.net/2024/09/18/j7IwXJEbvmaDBUx.png)

- **获取代理后的Bean实例**

![image-20240918194436843](https://s2.loli.net/2024/09/18/bvBQatuFwDApzOx.png)

> 关于为什么要更新BeanDefination

![image-20240918194519539](https://s2.loli.net/2024/09/18/6MdWjPwsRxZvY2i.png)

- 通过测试

![image-20240918195415936](https://s2.loli.net/2024/09/18/tIk6edVONAu84Sb.png)

关于SecondProxyBean：是通过`final OriginBean target;`并注入OriginBean的字段属性，实现了代理。其余两个分别是构造器代理和属性注入代理

## Day 7: 实现IOC容器（7）

### 7. 实现IOC容器

- **创建IOC容器接口**

![image-20240918200658763](https://s2.loli.net/2024/09/18/ogQ8L1GzxICDfHA.png)

- **实现供用户使用的功能**

![image-20240918201045998](https://s2.loli.net/2024/09/18/h67mrcMSN29bZOK.png)

- **最后让之前实现的容器继承这个接口**

![image-20240918201103847](https://s2.loli.net/2024/09/18/sh9zLbecvn2qJ4w.png)

IOC容器完成！

## Day 8: 实现AOP（1）

AOP的实现主要有基于代理的方式（JDK动态代理和CGLIB代理）和AspectJ方式。

> **JDK动态代理**：使用Java的反射机制，为接口创建代理。只能对实现了接口的类进行代理。
>
> **CGLIB代理**：通过生成子类的方式来实现代理，不需要接口，适用于没有实现接口的类。
>
> **AspectJ**：一种功能更强大的AOP框架，提供编译时、类加载时和运行时的代理，支持更复杂的切面定义
>
> ——By：ChatGPT

对于Spring中具体的用法：

> Spring实际上内置了多种代理机制，如果一个Bean声明的类型是接口，那么Spring直接使用Java标准库实现对接口的代理，如果一个Bean声明的类型是Class，那么Spring就使用CGLIB动态生成字节码实现代理。
>
> 除了实现代理外，还得有一套机制让用户能定义代理。Spring又提供了多种方式：
>
> 1. 用AspectJ的语法来定义AOP，比如`execution(public * com.itranswarp.service.*.*(..))`；
> 2. 用注解来定义AOP，比如用`@Transactional`表示开启事务。
>
> ——By：廖雪峰

这里我们为了简化需求，只支持Annotation模式的AOP机制，并且采用动态生成字节码的方式实现。

~~常用的动态代理方式是JDK和CGLIB，这里我们决定使用CGLIB来熟悉其用法~~

由于CGLIB已经不支持对于Java 17 以上版本的支持，所以我们和廖雪峰老师一样采用ByteBuddy

首先我们需要实现用于代理的解决工具类

### 8. 实现ProxyResolver

- **导入ByteBuddy的依赖**

```
        <dependency>
            <groupId>net.bytebuddy</groupId>
            <artifactId>byte-buddy</artifactId>
            <version>LATEST</version>
        </dependency>
```

- **使用ByteBuddy实现基本的代理方法**

![image-20240921102912508](https://s2.loli.net/2024/09/21/w4IdohKfgQ317AV.png)

- **手动设置Polite注解，并作用于方法上**

![image-20240921104050968](https://s2.loli.net/2024/09/21/gO4zy9NGa1Q2Vbh.png)

- **实现InvocationHandler类并重写Invoke方法**（这里替换了字符串里面的标点符号）

![image-20240921104514563](https://s2.loli.net/2024/09/21/LmXfwd1aZQeuAiC.png)

- 通过测试

![image-20240921104605077](https://s2.loli.net/2024/09/21/BDqSkQVvRsreif6.png)

## Day 9: 实现AOP（2）

### 9. 实现Around

我们手动自定义一个注解@Around，来表示在这个注解环绕的部分执行特定的方法

注解的value表示的是应该按什么名字查找拦截器

![image-20240924155558102](https://s2.loli.net/2024/09/24/7EHoLxw3VhJtA8R.png)

然后定义一个原始Bean，并使用@Around注解

![image-20240924155656721](https://s2.loli.net/2024/09/24/zFotEhjJOmwXgB3.png)

为此，我们要手动定义一个AroundInvocationHandler，让其实现InvocationHandler，并重写invoke方法

![image-20240924160345018](https://s2.loli.net/2024/09/24/Gh9dTtEl87BUw6O.png)

之后我们在IOC容器中装配这个AOP，但实际上是通过AnnotationProxyBeanPostProcessor装配的，Around继承了Annotation

![image-20240924160508522](https://s2.loli.net/2024/09/24/z2pUX5K4EhBcyql.png)

所以我们应当创建AnnotationProxyBeanPostProcessor来装配AOP

这个类实现了我们之前的BeanPostProcessor

![image-20240924160823645](https://s2.loli.net/2024/09/24/GrdT9t2Lg1X5S47.png)

>上述`AroundProxyBeanPostProcessor`的机制非常简单：检测每个Bean实例是否带有`@Around`注解，如果有，就根据注解的值查找Bean作为`InvocationHandler`，最后创建Proxy，返回前保存了原始Bean的引用，因为IoC容器在后续的注入阶段要把相关依赖和值注入到原始Bean。
>
>——By：廖雪峰

AroundProxyBeanPostProcessor的代码如下：

![image-20240924164706223](https://s2.loli.net/2024/09/24/5toRxbvINCmfFSy.png)

> 总结一下，Summer Framework提供的包括：
>
> - `Around`注解；
> - `AroundProxyBeanPostProcessor`实现AOP。
>
> 客户端代码需要提供的包括：
>
> - 带`@Around`注解的原始Bean；
> - 实现`InvocationHandler`的Bean，名字与`@Around`注解value保持一致。
>
> ——By：廖雪峰

实现了Around以后，我们还需要继续实现Before和After的方法。但是由于Around本身实际也包含了Before和After，所以我们可以通过适配器的方式让他俩适配Around。

![image-20240924190651834](https://s2.loli.net/2024/09/24/bIwBXD8i6vUOtnG.png)

![](https://s2.loli.net/2024/09/24/bIwBXD8i6vUOtnG.png)

由于后续可能有自定义注解AOP的情况，所以我们将Around部分代码提取成抽象类AnnotationProxyBeanPostProcessor，让Around继承这部分

至此，完成手写AOP部分

## Day 10: 实现JDBC和事务（1）

> Spring对JDBC数据库的支持主要包括：
>
> 1. 提供了一个`JdbcTemplate`和`NamedParameterJdbcTemplate`模板类，可以方便地操作JDBC；
> 2. 支持流行的ORM框架，如Hibernate、JPA等；
> 3. 支持声明式事务，只需要通过简单的注解配置即可实现事务管理。
>
> ——By：廖雪峰

我们这里为了简化，只实现JdbcTemplate以及声明式事务

### 10. 实现JdbcTemplate

我们仿照SpringBoot的方式，使用HikariCP作为连接池

先导入对应的依赖：

```
<!--        数据库连接池-->
        <dependency>
            <groupId>com.zaxxer</groupId>
            <artifactId>HikariCP</artifactId>
            <version>5.0.1</version>
        </dependency>
<!--        JDBC驱动-->
        <dependency>
            <groupId>org.xerial</groupId>
            <artifactId>sqlite-jdbc</artifactId>
            <version>3.41.2.2</version>
            <scope>test</scope>
        </dependency>
```

如果要使用数据库的话，需要像廖老师一样在yml文件中配置具体的DataSource举例如下：

```
summer:
  datasource:
    url: jdbc:sqlite:test.db
    driver-class-name: org.sqlite.JDBC
    username: sa
    password: 
```

