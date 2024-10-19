import com.hankcs.hanlp.HanLP;

import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;

/**
 * @author Xu
 * @Date 2024/9/14 13:30
 * @Description 软工作业2：个人项目-论文查重
 */
public class Utils {

    public static String getHash(String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            return new BigInteger(1, messageDigest.digest(str.getBytes(StandardCharsets.UTF_8))).toString(2);
        } catch (Exception ignore) {
            return str;
        }
    }

    public static String getSimHash(String str) {
        if (str.length() < 66) {
            System.out.println("输入文本过短");
        }
        int[] weight = new int[128];
        List<String> keywordList = HanLP.extractKeyword(str, str.length());
        int size = keywordList.size();
        int i = 0;
        for (String keyword : keywordList) {
            StringBuilder hash = new StringBuilder(getHash(keyword));
            if (hash.length() < 128) {
                int distance = 128 - hash.length();
                hash.append("0".repeat(Math.max(0, distance)));
            }
            for (int j = 0; j < weight.length; j++) {
                if (hash.charAt(j) == '1') {
                    weight[j] += (10 - (i / (size / 10)));
                } else {
                    weight[j] -= (10 - (i / (size / 10)));
                }
            }
            i++;
        }
        StringBuilder simHash = new StringBuilder();
        for (int k : weight) {
            if (k > 0) {
                simHash.append("1");
            } else simHash.append("0");
        }
        return simHash.toString();
    }

    public static String readTxt(String txtPath) {
        StringBuilder str = new StringBuilder();
        String strLine;

        // 将 txt文件按行读入 str中
        File file = new File(txtPath);
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            // 字符串拼接
            while ((strLine = bufferedReader.readLine()) != null) {
                str.append(strLine);
            }
            // 关闭资源
            inputStreamReader.close();
            bufferedReader.close();
            fileInputStream.close();
        } catch (IOException ignore) {
        }
        return str.toString();
    }

    public static void writeTxt(String  str,String txtPath){
        File file = new File(txtPath);
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(file, true);
            fileWriter.write(str, 0, str.length());
            fileWriter.write("\r\n");
            // 关闭资源
            fileWriter.close();
        } catch (IOException ignore) {
        }
    }

    public static int getHammingDistance(String simHash1, String simHash2){
        if(simHash1.length()!=simHash2.length())
            return -1;
        int distance=0;
        System.out.println("str1的simHash值："+simHash1);
        System.out.println("str2的simHash值："+simHash2);
        for (int i=0;i<simHash1.length();i++){
            if(simHash1.charAt(i)!=simHash2.charAt(i))
                distance++;

        }
        System.out.println("海明距离为："+distance);
        return distance;
    }

    private static double getSimilarity(int distance){
        return 1-distance/128.0;
    }

    public static double getSimilarity(String str1,String str2){
        String simHash = getSimHash(str1);
        String simHash1 = getSimHash(str2);
        int hammingDistance = getHammingDistance(simHash, simHash1);
        return getSimilarity(hammingDistance);
    }

}
