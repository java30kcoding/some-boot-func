package cn.itlou.learnjava8;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

/**
 *
 */
public class Exercise {

    public static void main(String[] args) {

        Trader raoul = new Trader("Raoul", "Cambridge");
        Trader mario = new Trader("Mario","Milan");
        Trader alan = new Trader("Alan","Cambridge");
        Trader brian = new Trader("Brian","Cambridge");
        List<Transaction> transactions = Arrays.asList(
                new Transaction(brian, 2011, 300),
                new Transaction(raoul, 2012, 1000),
                new Transaction(raoul, 2011, 400),
                new Transaction(mario, 2012, 710),
                new Transaction(mario, 2012, 700),
                new Transaction(alan, 2012, 950)
        );

        //找出2011年发生的所有交易，并按交易额排序（从低到高）
        List<Transaction> tr2011 = transactions.stream()
                .filter(transaction -> transaction.getYear() == 2011)
                .sorted(Comparator.comparing(Transaction:: getValue))
                .collect(toList());

        tr2011.stream().forEach(System.out :: println);

        //交易员都在哪些不同的城市工作过
        List<String> citys = transactions.stream()
                .map(transaction -> transaction.getTrader().getCity())
                .distinct().collect(toList());

        citys.stream().forEach(System.out :: println);

        //查找所有来自于剑桥的交易员，并按姓名排序
        List<Trader> traders = transactions.stream()
                .map(Transaction:: getTrader)
                .filter(trader -> trader.getCity()
                        .equals("Cambridge"))
                .distinct().sorted(Comparator.comparing(Trader:: getName))
                .collect(toList());

        traders.stream().forEach(System.out :: println);

        //返回所有交易员的姓名字符串，按字母顺序排序
        String names = transactions.stream()
                .map(transaction -> transaction.getTrader().getName())
                .distinct()
                .sorted()
                .reduce("", (a, b) -> a + b);

        System.out.println(names);

        //有没有交易员是在米兰工作的
        boolean isMilan = transactions.stream()
                .anyMatch(transaction -> transaction.getTrader().getCity().equals("Milan"));

        System.out.println(isMilan);

        //打印生活在剑桥的交易员的所有交易额
        transactions.stream().filter(transaction -> transaction.getTrader().getCity().equals("Cambridge"))
                .map(Transaction:: getValue)
                .forEach(System.out :: println);

        //所有交易中，最高的交易额是多少
        Optional<Integer> maxValue = transactions.stream()
                .map(Transaction:: getValue)
                .reduce(Integer :: max);

        System.out.println(maxValue);



    }

}
