/**
 * @author banbao
 * @comment 4位数的吸血鬼数字:
 *          ab*cd = efgh => sort(a,b,c,d) = sort(e,f,g,h)
 */

import java.util.ArrayList;

public class Test01{
    public static void main(String[] args){
        for(int i = 10; i < 99; ++i){
            for(int j = i; j < 99; ++j){
                int ij = i * j;
                // 不是 4 位数
                if(ij < 1000) continue;
                // i,j
                ArrayList<Integer> factor = new ArrayList<>();
                factor.clear();
                factor.add(i%10);
                factor.add(i/10);
                factor.add(j%10);
                factor.add(j/10);
                // lambda 表达式(java 8)
                factor.sort((a,b)->(a-b));
                // ij
                ArrayList<Integer> product = new ArrayList<>();
                product.clear();
                int[] ten = {1,10,100,1000};
                for(int k = 0;k < 4; ++k)
                    product.add(ij/ten[k]%10);
                product.sort((a,b)->(a-b));
                // compare
                if(factor.equals(product))
                    System.out.println(i + "*" + j + "=" + ij);
            }
        }
    }
}

/*
 * output :
 *
 * 15*93=1395
 * 21*60=1260
 * 21*87=1827
 * 27*81=2187
 * 30*51=1530
 * 35*41=1435
 * 80*86=6880
 */
