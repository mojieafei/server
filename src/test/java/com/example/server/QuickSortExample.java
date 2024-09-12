package com.example.server;

public class QuickSortExample {

    // 交换数组中两个元素的位置
    public static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    // 划分函数，选择一个基准元素，将数组划分为两部分
    public static int partition(int[] arr, int low, int high) {
        // 选择最后一个元素作为基准
        int pivot = arr[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            // 如果当前元素小于等于基准元素，则将其与 i+1 位置的元素交换
            if (arr[j] <= pivot) {
                i++;
                swap(arr, i, j);
            }
        }
        // 将基准元素放到正确的位置上
        swap(arr, i + 1, high);
        return i + 1;
    }

    // 快速排序函数
    public static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            // 获取划分点的索引
            int pivotIndex = partition(arr, low, high);
            // 对划分点左边的子数组进行快速排序
            quickSort(arr, low, pivotIndex - 1);
            // 对划分点右边的子数组进行快速排序
            quickSort(arr, pivotIndex + 1, high);
        }
    }

    public static void main(String[] args) {
        int[] arr = {12, 11, 13, 5, 6,7,12,109,213,22,33};
        System.out.println("原始数组:");
        for (int num : arr) {
            System.out.print(num + " ");
        }

        quickSort(arr, 0, arr.length - 1);

        System.out.println("\n 排序后的数组:");
        for (int num : arr) {
            System.out.print(num + " ");
        }
    }
}