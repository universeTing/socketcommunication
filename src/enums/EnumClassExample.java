package enums;

import java.util.Scanner;

public enum EnumClassExample {
    SMALL("S"),
    MEDIUM("M"),
    LARGE("L"),
    EXTRA_LARGE("XL")
    ;

    private String abbreviation;

    // 构造函数，传入尺寸
    private EnumClassExample(String abbreviation){
        this.abbreviation = abbreviation;
    }

    public String getAbbreviation() {
        return abbreviation;
    }


    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("Enter a size : (SMALL, MEDIUM,LARGE, EXTRA_LARGE)");
        String input = in.next().toUpperCase(); //转换为大写
        EnumClassExample size = Enum.valueOf(EnumClassExample.class, input);//根据输入获取值,返回指定名字、给定类的枚举常量
        System.out.println("size = "+ size);
        System.out.println("abbreviation = "+ size.getAbbreviation());
        if(size == EnumClassExample.EXTRA_LARGE){
            System.out.print("GOOD JOB -- YOU PAID ATTENTION TO THE ");
            System.out.println(size.toString()); //返回枚举常量名
        }

        System.out.println("位置为第" + size.ordinal());  //返回枚举常量名
        System.out.println(size.compareTo(EnumClassExample.SMALL)); //对比次序，在small前面返回负数，在small后面返回正数既其位置，相等返回0
        Class kclass = size.getClass();

    }
}
