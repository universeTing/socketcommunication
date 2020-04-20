package designpattern.singleton;

/**
 * @author
 * @program socketcommunication
 * @description 获取单例的一个测试，大臣类，永远都是见的同一个皇帝。
 * @create 2019-12-01 12:05
 **/
public class Minister {
    public static void main(String[] args) {
        for (int i = 0; i < 3; i++) {
            Emperor emperor = Emperor.getInstance();
            emperor.sayHello();
        }
    }
}
