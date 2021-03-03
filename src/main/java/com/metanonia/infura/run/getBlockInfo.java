package com.metanonia.infura.run;

import com.metanonia.infura.utils.Mysql;
import okhttp3.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.Connection;

public class getBlockInfo {

    public static void main(String[] args) {
        if(args.length != 4){
            System.out.println("getBlockInfo <infura_projectid> <infura_secretkey>  blockNumber_from blockNumber_to");
            System.exit(1);
        }
        final String USER_NAME = "metanonia";
        final String PASSWORD = "test!@#";
        final String InfuraUrl = "https://mainnet.infura.io/v3/";
        Mysql mysql = null;
        String infuraProjectId = args[0];
        String infuraSecretKey = args[1];
        Integer from = Integer.parseInt(args[2]);
        Integer to = Integer.parseInt(args[3]);

        try {
            mysql = new Mysql(USER_NAME, PASSWORD);
            Connection conn = mysql.getConn();

            HttpService infura = new HttpService(InfuraUrl + infuraProjectId);
            infura.addHeader("Authorization", Credentials.basic("",infuraSecretKey))  ;
            Web3j web3 = Web3j.build(infura);

            for(int i=from; i<to; i++) {
                try {
                    EthBlock.Block block = web3.ethGetBlockByNumber(new DefaultBlockParameterNumber(i), false)
                            .send().getBlock();
                    BigInteger blockNumber = block.getNumber();
                    String blockHash = block.getHash();
                    BigInteger blockTimestamp = block.getTimestamp();
                    String txRoot = block.getTransactionsRoot();
                    String blockMiner = block.getMiner();
                    System.out.println("Block:" + blockHash + ":" + String.valueOf(blockHash.length()));
                    System.out.println("Tx: " + txRoot + ":" + String.valueOf(txRoot.length()));
                } catch (IOException e) {
                    continue;
                }
            }

            web3.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(mysql != null) mysql.close();
        }

    }
}
