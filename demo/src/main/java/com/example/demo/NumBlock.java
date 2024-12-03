package com.example.demo;

import org.web3j.protocol.*;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.math.BigInteger;

public class NumBlock {
    public static void main(String[] args){
        Web3j web3 = Web3j.build(new HttpService("http://127.0.0.1:7545"));
        try{
            BigInteger blockNum = web3.ethBlockNumber().send().getBlockNumber();
            System.out.println("Visualizando: " + blockNum);

        }catch(IOException e){
            e.printStackTrace();
        }
    }
}