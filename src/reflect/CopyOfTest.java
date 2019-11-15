package reflect;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * @date 2019-11-15
 * @author wangyt
 */
public class CopyOfTest {
    public static void main(String[] args) {
        int[] a = {1,2,3};
        a = (int[]) goodCopyOf(a,a.length+10);
        //[1, 2, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
        System.out.println(Arrays.toString(a));
    }

    /**
     * 这个方法提供一个新的数组，类型跟 a 传入的类型一样，并且容量更大，这个新数组把a的元素都复制过去
     * @param a
     * @param newLength
     * @return  返回一个更大的包含a的数组
     */
    public static Object goodCopyOf(Object a , int newLength){
        Class cl = a.getClass();
        if(!cl.isArray()) {
            return null;
        }
        // 确定数组对应的类型是什么
        Class componentType = cl.getComponentType();
        int length = Array.getLength(a);
        // 构建一个新数组
        Object newArray = Array.newInstance(componentType,newLength);
        System.arraycopy(a,0,newArray,0,Math.min(length,newLength));
        // 返回指定位置上的内容，需要给定类型
        System.out.println(Array.getInt(a,1) + "  *****");
        return newArray;
    }


}
