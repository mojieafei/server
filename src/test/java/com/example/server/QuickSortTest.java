package com.example.server;

import java.util.Arrays;

public class QuickSortTest {
    public static void main(String[] args) {
        int[] arr = {1,12,321,3,545,67,7,8,8,9,9,77867,63,52,523,4,32,423,5};
        quickSort(arr,0,arr.length-1);
        System.out.println(Arrays.toString(arr));
    }

    public static void quickSort(int[] arr, int low, int high){
        // 判断大小
        if(low >= high){
            return;
        }
        // 取中
        int index = partition(arr, low,high);
        // 遍历左边
        quickSort(arr,0, index-1);
        // 遍历右边
        quickSort(arr,index+1,high);
    }

    public static int partition(int[] arr, int low, int high){
        int flag = arr[high];
        int i = low - 1;
        for (int j = low;j<=high;j++){
            if(arr[j] < flag){
                i++;
                swap(arr,i,j);
            }
        }
        // 将基准元素放到正确的位置上
        swap(arr, i + 1, high);
        return i + 1;
    }

    public static void swap(int[] arr,int i , int j){
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
