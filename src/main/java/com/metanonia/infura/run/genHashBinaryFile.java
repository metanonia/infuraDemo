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
            File file2 = new File("./xorBlockMixHash.bin");
            File file3 = new File("./cBlockMixHash.bin");
            File file4 = new File("./cMixTxHash.bin");
            File file5 = new File("./cBlockTxHash.bin");
            File file6 = new File("./mixHash.bin");
            File file7 = new File("./uncleHash.bin");
            File file8 = new File("./receiptHash.bin");
            File file9 = new File("./xorBlockTxHash.bin");
            File file10 = new File("./xorMixTxHash.bin");
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            BufferedWriter writer1 = new BufferedWriter(new FileWriter(file1));
            BufferedWriter writer2 = new BufferedWriter(new FileWriter(file2));
            BufferedWriter writer3 = new BufferedWriter(new FileWriter(file3));
            BufferedWriter writer4 = new BufferedWriter(new FileWriter(file4));
            BufferedWriter writer5 = new BufferedWriter(new FileWriter(file5));
            BufferedWriter writer6 = new BufferedWriter(new FileWriter(file6));
            BufferedWriter writer7 = new BufferedWriter(new FileWriter(file7));
            BufferedWriter writer8 = new BufferedWriter(new FileWriter(file8));
            BufferedWriter writer9 = new BufferedWriter(new FileWriter(file9));
            BufferedWriter writer10 = new BufferedWriter(new FileWriter(file10));

            String Sql = "select blockHash, txRoot, mixHash, uncleHash, receiptHash from eth_block order by blockNumber";
            PreparedStatement psmt = conn.prepareStatement(Sql);
            ResultSet result = psmt.executeQuery();

            while(result.next()) {
                String block = result.getString("blockHash").substring(2);
                String txRoot = result.getString("txRoot").substring(2);
                String mixString = result.getString("mixHash").substring(2);
                String uncleString = result.getString("uncleHash").substring(2);
                String receiptString = result.getString("receiptHash").substring(2);
                BigInteger blockHash = new BigInteger(block, 16);
                BigInteger txHash = new BigInteger(txRoot, 16);
                BigInteger mixHash = new BigInteger(mixString, 16);
                BigInteger uncleHash = new BigInteger(uncleString, 16);
                BigInteger receiptHash = new BigInteger(receiptString, 16);
                BigInteger xorBlockMixHash = blockHash.xor(mixHash);
                BigInteger xorBlockTxHash = blockHash.xor(txHash);
                BigInteger xorMixTxHash = mixHash.xor(txHash);
                String sBinanry = "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
                                + "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
                                + "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
                                + blockHash.toString(2);
                String tBinanry = "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
                        + txHash.toString(2);
                String xBlockMixBinanry = "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
                        + xorBlockMixHash.toString(2);
                String xBlockTxBinanry = "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
                        + xorBlockTxHash.toString(2);
                String xMixTxBinanry = "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
                        + xorMixTxHash.toString(2);
                String mixBinary = "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
                        + mixHash.toString(2);
                String uncleBinary = "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
                        + uncleHash.toString(2);
                String receiptBinary = "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
                        + receiptHash.toString(2);
                sBinanry = sBinanry.substring(sBinanry.length()-256);
                tBinanry = tBinanry.substring(tBinanry.length()-256);
                xBlockMixBinanry = xBlockMixBinanry.substring(xBlockMixBinanry.length()-256);
                xBlockTxBinanry = xBlockTxBinanry.substring(xBlockTxBinanry.length()-256);
                xMixTxBinanry = xMixTxBinanry.substring(xMixTxBinanry.length()-256);
                mixBinary = mixBinary.substring(mixBinary.length()-256);
                uncleBinary = uncleBinary.substring(uncleBinary.length()-256);
                receiptBinary = receiptBinary.substring(receiptBinary.length()-256);
                String csBinanry = compactBinaryString(sBinanry);
                String ctBinanry = compactBinaryString(tBinanry);
                String cmBinanry = compactBinaryString(mixBinary);
                String cbtBinary = "";
                for(int i=0; i<csBinanry.length() && i<ctBinanry.length(); i++) {
                    cbtBinary = cbtBinary + csBinanry.substring(i,i+1) + ctBinanry.substring(i,i+1);
                }
                String cbmBinary = "";
                for(int i=0; i<csBinanry.length() && i<ctBinanry.length(); i++) {
                    cbmBinary = cbmBinary + csBinanry.substring(i,i+1) + cmBinanry.substring(i,i+1);
                }
                String cmtBinary = "";
                for(int i=0; i<csBinanry.length() && i<ctBinanry.length(); i++) {
                    cmtBinary = cmtBinary + cmBinanry.substring(i,i+1) + ctBinanry.substring(i,i+1);
                }

                writer.write(sBinanry);
                writer1.write(tBinanry);
                writer2.write(xBlockMixBinanry);
                writer3.write(cbmBinary);
                writer4.write(cmtBinary);
                writer5.write(cbtBinary);
                writer6.write(mixBinary);
                writer7.write(uncleBinary);
                writer8.write(receiptBinary);
                writer9.write(xBlockTxBinanry);
                writer10.write(xMixTxBinanry);
            }
            writer.close();
            writer1.close();
            writer2.close();
            writer3.close();
            writer4.close();
            writer5.close();
            writer6.close();
            writer7.close();
            writer8.close();
            writer9.close();
            writer10.close();
            psmt.close();
            conn.close();
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    static String compactBinaryString(String org) {
        int len = org.length();
        if(len%2 != 0) org = org + "0";
        len = org.length()/2;
        String ret = "";
        for(int i=0; i<len; i++) {
            String a1 = org.substring(i*2,i*2+1);
            String a2 = org.substring(i*2+1,(i+1)*2);
            if(a1.matches("0") == false && a1.matches("1") == false) return null;
            if(a2.matches("0") == false && a2.matches("1") == false) return null;
            int i1 = Integer.parseInt(a1);
            int i2 = Integer.parseInt(a2);
            if((i1-i2) == 0) ret = ret+"0";
            else ret = ret + "1";
        }
        return ret;
    }
}
