package com.metanonia.infura.run;

import com.metanonia.infura.utils.Mysql;
import okhttp3.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

public class getDaonTr {
    final static  String DAON_ADDRESS = "0x80504F30f38b4149D95faE61BdE9F3Ee204ea9d5";
    public static void main(String[] args) {
        if(args.length != 6){
            System.out.println("getDaonTr <infura_projectid> <infura_secretkey>  blockNumber_from blockNumber_to mysql_id mysql_password");
            System.exit(1);
        }

        final String InfuraUrl = "https://mainnet.infura.io/v3/";
        Mysql mysql = null;
        String infuraProjectId = args[0];
        String infuraSecretKey = args[1];
        Integer from = Integer.parseInt(args[2]);
        Integer to = Integer.parseInt(args[3]);
        String USER_NAME = args[4];
        String PASSWORD = args[5];

        try {
         //   mysql = new Mysql(USER_NAME, PASSWORD);
          //  Connection conn = mysql.getConn();

            HttpService infura = new HttpService(InfuraUrl + infuraProjectId);
            infura.addHeader("Authorization", Credentials.basic("",infuraSecretKey))  ;
            Web3j web3 = Web3j.build(infura);

            String Sql = "insert into eth_block (blockNumber, blockHash, txRoot, blockMiner, blockTime, mixHash, uncleHash, receiptHash) "
                    + "values (?, ?, ?, ?, ?, ?, ?, ?)";
        //    PreparedStatement psmt = conn.prepareStatement(Sql);
            for(int i=from; i<to; i++) {
                try {
                    EthBlock.Block block = web3.ethGetBlockByNumber(new DefaultBlockParameterNumber(i), true)
                            .send().getBlock();
                    /**
                    BigInteger blockNumber = block.getNumber();
                    String blockHash = block.getHash();
                    BigInteger blockTimestamp = block.getTimestamp();
                    String txRoot = block.getTransactionsRoot();
                    String maxHash = block.getMixHash();
                    String uncleHash = block.getSha3Uncles();
                    String receiptHash = block.getReceiptsRoot();
                    String blockMiner = block.getMiner();
                     **/
                    List<EthBlock.TransactionResult> txlists = block.getTransactions();
                    for(int j=0; j<txlists.size(); j++) {
                        EthBlock.TransactionObject tx = (EthBlock.TransactionObject) txlists.get(j).get();
                        String txHash = tx.getHash();
                        String fromAddress = tx.getFrom();
                        BigInteger gasLimit = tx.getGas();
                        BigInteger gasPrice = tx.getGasPrice();
                        String blockHash = tx.getBlockHash();
                        BigInteger blockNumber = tx.getBlockNumber();
                        BigInteger nonce = tx.getNonce();
                        BigInteger value = tx.getValue();
                        TransactionReceipt receipt = web3.ethGetTransactionReceipt(txHash).send().getTransactionReceipt().get();
                        BigInteger gasUsed = receipt.getGasUsed();
                        String revertReason = receipt.getRevertReason();
                        String status = receipt.getStatus();
                        boolean isTxOK = receipt.isStatusOK();

                        if(tx.getTo() != null) {
                            String toAddress = tx.getTo().toLowerCase();
                            if (toAddress.matches(DAON_ADDRESS.toLowerCase())) {
                                String InputStr = tx.getInput();
                                String method = InputStr.substring(0,10);
                                switch (method) {
                                    case "0xa9059cbb": // transfer(address to, unit256 amount)
                                        String toAddr = "0x"+ InputStr.substring(34,74); // length 64 -> 32 byte -> 256bit
                                        BigInteger amount = new BigInteger(InputStr.substring(74), 16);
                                        System.out.println("Transfer From:" + fromAddress + " To:" + toAddr + " amount:" + amount);
                                        break;
                                    case "0x42966c68": // burn(unit256 amount)
                                        BigInteger burn = new BigInteger(InputStr.substring(10), 16);
                                        System.out.println("Burn From:" + fromAddress + " amount:" + burn);
                                    default:
                                        System.out.println(InputStr);
                                }

                            }
                        }
                    }
/**
                    psmt.clearParameters();
                    psmt.setBigDecimal(1, new BigDecimal(blockNumber));
                    psmt.setString(2, blockHash);
                    psmt.setString(3, txRoot);
                    psmt.setString(4, blockMiner);
                    psmt.setInt(5, blockTimestamp.intValue());
                    psmt.setString(6, maxHash);
                    psmt.setString(7, uncleHash);
                    psmt.setString(8, receiptHash);

                    psmt.executeUpdate();
 **/
                } catch (IOException e) {
                    continue;
                }
            }
        //    psmt.close();
            web3.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(mysql != null) mysql.close();
        }
        System.exit(0);
    }
}
