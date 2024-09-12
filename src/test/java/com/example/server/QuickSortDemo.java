package com.example.server;

public class QuickSortDemo {
    public static void main(String[] args) {
        int[] arr = {1,23,21,4,5,56,6,77,8,8,989,9,900,};
        System.out.println("排序qian的数组为:");
        for (int num : arr) {
            System.out.print(num + " ");
        }
        quickSort(arr,0,arr.length-1);
        System.out.println("排序后的数组为:");
        for (int num : arr) {
            System.out.print(num + " ");
        }
    }

    public static void quickSort(int[] arr, int low,int high){
        if(low < high){
            // 分区
            int index = partition(arr, low, high);
            // 排序左边的
            quickSort(arr,low ,index - 1);
            // 排序右边的
            quickSort(arr,index + 1 ,high);
        }

    }

    public  static int partition(int[] arr, int low, int high){
        // 取基准
        int temp = arr[high];
        int i = low - 1;
        // 交换小值
        for(int j = low;j < high;j ++){
            if(arr[j] <= temp){
                i++;
                swap(arr,i,j);
            }
        }
        // 摆正基准的位置
        swap(arr,i + 1 , high);
        return i + 1;
    }

    public static void swap(int[] arr, int i, int j){
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
