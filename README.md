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

