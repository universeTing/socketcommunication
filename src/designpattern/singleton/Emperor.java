package designpattern.singleton;

/**
 * @author wangyt
 * @program socketcommunication
 * @description 皇帝类、在同一时期只能有一个独苗，永远都只有一个实例，无论什么时候都只有一个，
 *              不管谁找的都是同一个类，同一个皇帝。
 * @create 2019-12-01 12:00
 **/
public class Emperor {
    private static final Emperor emperor = new Emperor();

    private Emperor(){

    }

    public static Emperor getInstance(){
        return emperor;
    }

    public void sayHello(){
        System.out.println("我就是王皇后!....~L~  2019-10-11");
    }
}
