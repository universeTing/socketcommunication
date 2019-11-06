package algorithm;

/**
 * 从数组中找出一个数，使剩下的数的乘积最大，分析一下几种情况
 * 1、数组中无负数（包括0）
 * 2、数组中有负数
 *     2.1 数组中有奇数个负数
 *     2.2 数组中有偶数个负数
 *
 * @author  wangyt
 * @date    2019-10-28
 */
public class MakeMultiplyMaximum {

    public int removeAMumberMakeMutiplyMaximum(int[] list){
        // 统计负数的个数
        long negativeCount = 0;
        for (int i = 0; i < list.length; i++) {
            if(list[i] < 0){
                negativeCount ++;
            }
        }

        // 根据负数情况，选择要删除的负数
        int tempIndex = 0;
        if((negativeCount & 1) == 1){
            //情况1、负数个数是奇数
            for (int i = 0; i < list.length; i++) {
                if(list[i] < 0 ){
                    if(list[tempIndex] >= 0 || list[i] > list[tempIndex]){
                        //是负数 并且 负数得绝对值对小
                        tempIndex = i ;
                    }
                }
            }
            return tempIndex;
        }else{
            //情况2、负数个数是偶数
            if(list.length == negativeCount){
                //所有元素都是负数
                for (int i = 1; i < list.length; i++) {
                    if(list[i] < list[tempIndex]){
                        //找出负数最小得数，记绝对值最大得值
                        tempIndex = i ;
                    }
                }
                return tempIndex;
            }

            // 没有负数
            for (int i = 1; i < list.length; i++) {
                if(list[i] >= 0){
                    if(list[tempIndex] < 0 || list[i] < list[tempIndex]){
                        tempIndex = i;
                    }
                }
            }
            return tempIndex;
        }
    }


    public double testVariable(Double... args){
        double largest = Double.NEGATIVE_INFINITY; //-1.0 / 0.0
        for(double v : args){
            if(v > largest) largest = v;
        }
        return largest;

    }


    public static void main(String[] args) {
        int[] list = {-1,-2,-3,-4,-5,-2,-3,-9};
        MakeMultiplyMaximum m = new MakeMultiplyMaximum();
        int i = m.removeAMumberMakeMutiplyMaximum(list);
        System.out.println(list[i]);

        double v = m.testVariable(1.0, 2.0, 3.0, 4.0);
        System.out.println("v = " + v);
    }



}
