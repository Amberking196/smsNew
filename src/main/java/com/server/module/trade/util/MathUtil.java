package com.server.module.trade.util;

public class MathUtil {
	//获取最大值的下标  
    public static int getMaxIndex(Integer[] arr){  
        int maxIndex = 0;   //获取到的最大值的角标  
        for(int i=0; i<arr.length; i++){  
            if(arr[i] > arr[maxIndex]){  
                maxIndex = i;  
            }  
        }  
        return maxIndex;  
    }  
      
    //获取最大值  
    public static int getMaxNum(Integer[] arr){  
        int maxNum = arr[0];  
        for(int i=0; i<arr.length; i++){  
            if(arr[i] > maxNum){  
                maxNum = arr[i];  
            }  
        }  
        return maxNum;  
    }  
}
