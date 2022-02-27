package com.karim;

import java.util.Random;

class Sorter implements Runnable {
    int[] nums;

    Sorter(int[] n) {
        nums = n;
    }

    synchronized public void run() {
        boolean isSorted = false;
        int buf;
        while (!isSorted) {
            isSorted = true;
            for (int i = 0; i < nums.length - 1; i++) {
                if (nums[i] > nums[i + 1]) {
                    isSorted = false;

                    buf = nums[i];
                    nums[i] = nums[i + 1];
                    nums[i + 1] = buf;
                }
            }
        }
    }
}

public class Main {

    public static void main(String[] args) {
        Random rnd = new Random();
        final long nanoSecondsPerMillisecond = 1000000;
        int size = 40000;
        int nums1[] = new int[size];
        for (int i = 0; i < nums1.length; i++) {
            nums1[i] = rnd.nextInt(10);
        }
        System.out.println("Изначальный массив");
        for (int i = 0; i < nums1.length; i++) {
            System.out.print(nums1[i] + " ");
        }
        int nums2_1[] = new int[size / 2];
        int nums2_2[] = new int[size / 2];
        System.arraycopy(nums1, 0, nums2_1, 0, 19999);
        System.arraycopy(nums1, 20000, nums2_2, 0, 20000);
        System.out.println();
        //////////////// Без использования потоков
        long startTime1 = System.nanoTime();
        boolean isSorted = false;
        int buf;
        while (!isSorted) {
            isSorted = true;
            for (int i = 0; i < nums1.length - 1; i++) {
                if (nums1[i] > nums1[i + 1]) {
                    isSorted = false;
                    buf = nums1[i];
                    nums1[i] = nums1[i + 1];
                    nums1[i + 1] = buf;
                }
            }
        }
        long stopTime1 = System.nanoTime();
        double elapsedTime1 = (stopTime1 - startTime1) / (nanoSecondsPerMillisecond);
        System.out.println("Массив, отсортированный в 1 потоке");
        for (int i = 0; i < nums1.length; i++) {
            System.out.print(nums1[i] + " ");
        }
        System.out.println();
        System.out.println("Время, потраченное на сортировку пузырьком массива из 40000 элементов (в миллисекундах): " + elapsedTime1);

        ////////////////////////////////// С использованием потоков
        long startTime2 = System.nanoTime();
        Thread thread1 = new Thread(new Sorter(nums2_1));
        Thread thread2 = new Thread(new Sorter(nums2_2));
        thread1.start();
        try {
            thread1.join();
        } catch (InterruptedException ex) {
        }
        thread2.start();
        try {
            thread2.join();
        } catch (InterruptedException ex) {
        }
        int middle = nums2_1.length;
        int[] nums2 = new int[nums2_1.length + nums2_2.length];
        for (int i = 0; i < (nums2_1.length + nums2_2.length); i++) {
            if (i < middle) {
                nums2[i] = nums2_1[i];
            } else {
                nums2[i] = nums2_2[i - middle];
            }
        }
        Thread thread3 = new Thread(new Sorter(nums2));
        thread3.start();
        try {
            thread3.join();
        } catch (InterruptedException ex) {
        }
        long stopTime2 = System.nanoTime();
        double elapsedTime2 = stopTime2 - startTime2;
        System.out.println("Массив, отсортированный в 2 потока");
        for (int i = 0; i < nums2.length; i++) {
            System.out.print(nums2[i] + " ");
        }
        System.out.println();
        System.out.println("Время, потраченное на сортировку пузырьком массива из 40000 элементов (в миллисекундах): " + elapsedTime2 / nanoSecondsPerMillisecond);


    }
}

