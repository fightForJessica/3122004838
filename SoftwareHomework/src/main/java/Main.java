import java.text.NumberFormat;

/**
 * @author Xu
 * @Date 2024/9/14 13:26
 * @Description 软工作业2：个人项目-论文查重
 */
public class Main {

    // 文件写入、写出路径
    private static final String ORIGIN_PATH = "src/main/resources/OriginalTxt";
    private static final String WRITE_PATH = "src/main/resources/OriginalTxt";

    public static void main(String[] args) {

        String originStr = Utils.readTxt(ORIGIN_PATH);
        NumberFormat numberFormat = NumberFormat.getPercentInstance();
        numberFormat.setMaximumFractionDigits(2);

        for (int i = 1; i <= 5; i++){
            String curCheckPath = "src/main/resources/CopyTxt" + i;
            System.out.println("当前检查的文章路径:" + curCheckPath);
            String curCheckStr = Utils.readTxt(curCheckPath);
            double similarity = Utils.getSimilarity(originStr, curCheckStr);
            String format = numberFormat.format(similarity);
            System.out.println("当前文章与原文的相似程度为：" + format);
            Utils.writeTxt("第" + i + "篇文章与原文的相似程度为：" + format, WRITE_PATH);
        }
    }

}
