package com.example.WorldBankingApplication.utils;

import java.time.Year;

//this class creates a static fields that we will use everywhere
public class AccountUtils {

    public static final  String ACCOUNT_EXIST_CODE= "001";
    public  static final  String ACCOUNT_EXISTS_MESSAGE= "This user already has an account created!";

    public static final  String ACCOUNT_CREATION_SUCCESS_CODE= "002";
    public static  final  String ACCOUNT_CREATION_SUCCESS_MESSAGE = "Account has been created Successfully";

    public static final  String ACCOUNT_NUMBER_NON_EXISTS_CODE= "003";
    public  static final  String ACCOUNT_NUMBER_NON_EXISTS_MESSAGE= "Provided account number does not exists!";

    public static final  String ACCOUNT_NUMBER_FOUND_CODE= "004";
    public static final  String ACCOUNT_NUMBER_FOUND_MESSAGE= "Account number found";

    public static final String ACCOUNT_CREDITED_SUCCESS_CODE= "005";
    public static final String ACCOUNT_CREDITED_SUCCESS_MESSAGE="Account credited successfully";

    public static final String ACCOUNT_BALANCE_INSUFFICIENT_CODE ="006";
    public static final String ACCOUNT_BALANCE_INSUFFICIENT_MESSAGE= "Account balance is insufficient";

    public static final String ACCOUNT_DEBITED_SUCCESS_CODE= "007";
    public static final String ACCOUNT_DEBITED_SUCCESS_MESSAGE= "Account debited successfully";



    public static String generateAccountNumber(){
        /**
         * This algorithm will assume that your account number will be a total of 10 digits
         * since w basically have 10 digits account number in Nigeria
         */
        // 1. Get the current year
        Year currentYear= Year.now();

        //2. Get 6 random digits
        int min =100000;
        int max =999999;

        //Generate a random number between min and max
        int randomNumber = (int)Math.floor(Math.random()*(max - min + 1) + min);

        //Convert current year and random six number to string and the concatenate them
        String year = String.valueOf(currentYear);
        String randomNum = String.valueOf(randomNumber);

        //append both year and random number to generate the 10 digit account numbers
        StringBuilder accountNumber = new StringBuilder();

        return accountNumber.append(year).append(randomNum).toString();
    }
}
