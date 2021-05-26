package com.metanonia.infura.run;

import com.metanonia.infura.utils.Mysql;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class genHashBinaryFile {
    public static void main(String[] args) {
        Mysql mysql = null;
        String USER_NAME;
        String PASSWORD;

        if(args.length != 2) {
            System.out.println("user_name passord");
            System.exit(0);
        }
        USER_NAME = args[0];
        PASSWORD = args[1];

        try {
            mysql = new Mysql(USER_NAME, PASSWORD);
            Connection conn = mysql.getConn();

            File file = new File("./blockHash.bin");
            File file1 = new File("./txRootHash.bin");
            File file2 = new File("./xorHash.bin");
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            BufferedWriter writer1 = new BufferedWriter(new FileWriter(file1));
            BufferedWriter writer2 = new BufferedWriter(new FileWriter(file2));

            String Sql = "select blockHash, txRoot from eth_block order by blockNumber";
            PreparedStatement psmt = conn.prepareStatement(Sql);
            ResultSet result = psmt.executeQuery();

            while(result.next()) {
                String block = result.getString("blockHash").substring(2);
                String txRoot = result.getString("txRoot").substring(2);
                BigInteger blockHash = new BigInteger(block, 16);
                BigInteger txHash = new BigInteger(txRoot, 16);
                BigInteger xorHash = blockHash.xor(txHash);
                String sBinanry = "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
                                + "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
                                + "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
                                + blockHash.toString(2);
                String tBinanry = "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
                        + txHash.toString(2);
                String xBinanry = "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
                        + xorHash.toString(2);
                sBinanry = sBinanry.substring(sBinanry.length()-256);
                tBinanry = tBinanry.substring(tBinanry.length()-256);
                xBinanry = xBinanry.substring(xBinanry.length()-256);

                writer.write(sBinanry);
                writer1.write(tBinanry);
                writer2.write(xBinanry);
            }
            writer.close();
            writer1.close();
            writer2.close();
            psmt.close();
            conn.close();
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
