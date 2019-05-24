# Java8笔记

## 关于Java8的新特性

​	推荐参考

> <<Java8实战>>较简单，循序渐进
>
> <<写给大忙人的JavaSE8>>较难，干脆

## Java8的Lambda

​	初步理解，Lambda就是把方法(函数)作为值使用；例如：

```java
File[] hiddenFiles = new File(".").listFiles(File :: isHidden);
```

​	其中，File的方法isHidden()，作为值传给了listFiles()方法

​	是编程方式类似`Scala`的函数式编程

| 参数                 | 箭头 | 主体                                      |
| -------------------- | ---- | ----------------------------------------- |
| (Apple a1, Apple a2) | ->   | a1.getWeight().compareTo(a2.getWeight()); |

​	参数个数和类型不限，主体可以有多行且返回值会自行判断

​	相比于`Collection`用来存储和访问数据，`Stream`更多是对数据进行计算并能更好的利用CPU

​	Lambda可以理解为一种匿名函数：它没有名称，但有参数列表、函数主体、返回类型，可能还有一个可以抛出的异常的列表。

### Lambda在何处使用

​	必须在`函数式接口`上使用

​	`函数式接口`：是只定义一个抽象方法的接口

### 方法引用

​	方法引用让你可以重复使用现有的方法定义，并像Lambda一样传递它们

​	本质就是写Lambda的代码变得更少了，功能实现的却是相同的

| Lambda表达式               | 方法引用           |
| -------------------------- | ------------------ |
| (Apple a) -> a.getWeight() | Apple :: getWeight |

### 归约

​	将流中所有元素反复结合起来，得到一个值，这样的查询被归类为归约操作

​	`元素求和`

```java
int sum = numbers.stream().reduce(0, (a, b) -> a + b);
```



## Java8的流(Stream)

​	什么是流？

​	从支持数据处理操作的源生成的一系列元素

​	**流只能消费一次**

​	流操作有两类：中间操作和终端操作。

流的好处：

​	**声明性——更简洁，更易读**

​	**可复合——更灵活**

​	**可并行——性能更好**

​	Stream分为Stream和ParallerStream串行和并行流

## Java8的Optional

​	它可以优雅的判断是否为null



# Lambda和Stream实战

​	最近在公司处理复杂Json串并计算数据用到了Lambda和Stream

```java
import com.alibaba.fastjson.JSONArray;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 身份证/手机号信贷逾期计算
 *
 * @author yuanyl
 * @version 1.0
 * @since 2019/05/23 9:13
 */
public class DpdCalculate {

    private DpdCalculate(){}

    /**
     * 获得信贷逾期次数
     *
     * @param jsonArray
     * @return 信贷逾期次数
     */
    public static int getDpdTimes(JSONArray jsonArray){
        //JSON数组返回的List：逾期的具体时间和次数
        List<RiskDetailYuqiTimeDate> yuqiTimeList = TrimJson.getYuqiTime(JSONArray.parseArray(TrimJson.getYuqiTimes(jsonArray).getOverdue_details()));
        //使用stream求overdue_count的和
        Optional<Integer> sum = yuqiTimeList.stream()
                .map(RiskDetailYuqiTimeDate::getOverdue_count)
                .reduce((x, y)-> x + y);
        return sum.get();
    }

    /**
     * 获得信贷逾期平台次数
     *
     * @param jsonArray
     * @return 信贷逾期平台次数
     */
    public static int getDpdplatformcnt(JSONArray jsonArray){
        return Integer.valueOf(TrimJson.getYuqiTimes(jsonArray).getDiscredit_times());
    }

    /**
     * 最大逾期金额区间
     *
     * @param jsonArray
     * @return 最大逾期金额区间
     */
    public static String getMaxdpdamountrange(JSONArray jsonArray){
//        List<RiskDetailYuqiTimeDate> ranges = yuqiTimeList.stream()
//        .filter(t -> t.getOverdue_amount_range() != null)
//        .sorted(Comparator.comparing((a, b) -> {
//            RiskDetailYuqiTimeDate :: compareMaxdpdamountrange
//        }))
//        .collect(Collectors.toList());
        List<RiskDetailYuqiTimeDate> yuqiTimeList = TrimJson.getYuqiTime(JSONArray.parseArray(TrimJson.getYuqiTimes(jsonArray).getOverdue_details()));
        yuqiTimeList.sort(RiskDetailYuqiTimeDate :: compareMaxdpdamountrange);
        return yuqiTimeList.get(0).getOverdue_amount_range();
    }

    /**
     * 获得信贷逾期最大笔数
     *
     * @param jsonArray
     * @return 信贷逾期最大笔数
     */
    public static int getMaxdpd(JSONArray jsonArray){
        List<RiskDetailYuqiTimeDate> yuqiTimeList = TrimJson.getYuqiTime(JSONArray.parseArray(TrimJson.getYuqiTimes(jsonArray).getOverdue_details()));
        return yuqiTimeList.stream()
                .map(RiskDetailYuqiTimeDate::getOverdue_count)
                .max(Integer::compare).get();
    }

    /**
     * 逾期时间
     *
     * @param jsonArray
     * @return 逾期时间集合按从早到晚排序
     */
    public static List<RiskDetailYuqiTimeDate> getDpddateear(JSONArray jsonArray){
        List<RiskDetailYuqiTimeDate> yuqiTimeList = TrimJson.getYuqiTime(JSONArray.parseArray(TrimJson.getYuqiTimes(jsonArray).getOverdue_details()));
        return yuqiTimeList.stream()
                .sorted(Comparator.comparing(RiskDetailYuqiTimeDate::getOverdue_time))
                .collect(Collectors.toList());
    }

    /**
     * 最大逾期时间区间
     *
     * @param jsonArray
     * @return 最大逾期时间区间字符串
     */
    public static String getDpddatelatest(JSONArray jsonArray){
        List<RiskDetailYuqiTimeDate> yuqiTimeList = TrimJson.getYuqiTime(JSONArray.parseArray(TrimJson.getYuqiTimes(jsonArray).getOverdue_details()));
        yuqiTimeList.sort(RiskDetailYuqiTimeDate :: compareIdDpd);
        return yuqiTimeList.get(0).getOverdue_day_range();
    }


}

```

