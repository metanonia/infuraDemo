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
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;

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

            String Sql = "insert into eth_block (blockNumber, blockHash, txRoot, blockMiner, blockTime) "
                    + "values (?, ?, ?, ?, ?)";
            PreparedStatement psmt = conn.prepareStatement(Sql);
            for(int i=from; i<to; i++) {
                try {
                    EthBlock.Block block = web3.ethGetBlockByNumber(new DefaultBlockParameterNumber(i), false)
                            .send().getBlock();
                    BigInteger blockNumber = block.getNumber();
                    String blockHash = block.getHash();
                    BigInteger blockTimestamp = block.getTimestamp();
                    String txRoot = block.getTransactionsRoot();
                    String blockMiner = block.getMiner();

                    psmt.clearParameters();
                    psmt.setBigDecimal(1, new BigDecimal(blockNumber));
                    psmt.setString(2, blockHash);
                    psmt.setString(3, txRoot);
                    psmt.setString(4, blockMiner);
                    psmt.setInt(5, blockTimestamp.intValue());

                    psmt.executeUpdate();
                } catch (IOException e) {
                    continue;
                }
            }
            psmt.close();
            web3.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(mysql != null) mysql.close();
        }
        System.exit(0);
    }
}
