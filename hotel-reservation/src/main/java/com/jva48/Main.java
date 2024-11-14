package com.jva48;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        Scanner scanner = new  Scanner(System.in);
        String input = scanner.nextLine();

        System.out.println(input);
        scanner.close();
    }
}