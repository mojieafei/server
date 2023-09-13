package com.example.server.cos;


import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RestController(value = "cosTest")
public class CosTest {

    private static List<CasePojo> data = new ArrayList<>();

    @GetMapping(value = "getResult")
    public String getResult(){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<br>caseId,caseId,cos<br/>");
        // caseId 分组
        Map<Integer, List<CasePojo>> group = data.stream().collect(Collectors.groupingBy(CasePojo::getCaseId));
        Map<Integer, List<CasePojo>> collect = new TreeMap<>(group);
        collect.entrySet().forEach(entry -> {
            Map<Float,String> map = new TreeMap<Float,String>(Comparator.reverseOrder()){};
            collect.entrySet().forEach(entryElse -> {
                Integer key = entry.getKey();
                Integer key1 = entryElse.getKey();
                Float cos = getCos(entry.getValue(), entryElse.getValue());
                // System.out.println("caseId :" + key +",caseId :" + key1 + ",cos:"+ cos);
                // 存放结果集 直接用treeMap排序 排序忽略并列 默认加入第一个
                map.putIfAbsent(cos,"" + key + ","+ key1);
            });
            // 取前三名
            AtomicInteger sign = new AtomicInteger(0);
            map.forEach((aFloat, s) -> {
                if(sign.getAndIncrement() < 3){
                    System.out.println(s + "," + aFloat);
                    stringBuffer.append("<br>" + s + "," + aFloat + "<br/>");
//                    stringBuffer.append(System.getProperty("line.separator"));
                }
            });
        });
        return stringBuffer.toString();
    }

    private static Float getCos(List<CasePojo> x, List<CasePojo> y){
        BigDecimal molecule = new BigDecimal(String.valueOf(BigDecimal.ZERO)); // 分子
        BigDecimal denominator = new BigDecimal(String.valueOf(BigDecimal.ZERO)); // 分母
        BigDecimal denominatorTempX = new BigDecimal(String.valueOf(BigDecimal.ZERO));
        BigDecimal denominatorTempY = new BigDecimal(String.valueOf(BigDecimal.ZERO));
        Map<Integer, BigDecimal> weightX = new HashMap<>();
        for (CasePojo caseX : x) {
            weightX.put(caseX.getTagId(), caseX.getWeight());
        }
        Map<Integer, BigDecimal> weightY = new HashMap<>();
        for (CasePojo caseY : y) {
            weightY.put(caseY.getTagId(), caseY.getWeight());
        }
        // tag交集
        Set<Integer> tagAll = Sets.union(weightX.keySet(), weightY.keySet());
        for (Integer tag : tagAll) {
            BigDecimal weight_x = weightX.containsKey(tag) ? weightX.get(tag) : BigDecimal.ZERO;
            BigDecimal weight_y = weightY.containsKey(tag) ? weightY.get(tag) : BigDecimal.ZERO;
            molecule = molecule.add(weight_x.multiply(weight_y));
            denominatorTempX = denominatorTempX.add(weight_x.multiply(weight_x));
            denominatorTempY = denominatorTempY.add(weight_y.multiply(weight_y));
        }
        denominator = denominator.add(BigDecimal.valueOf(Math.sqrt(denominatorTempX.doubleValue()) * Math.sqrt(denominatorTempY.doubleValue())));
        if(denominator.equals(BigDecimal.ZERO)){
            return 0F;
        }
        return molecule.divide(denominator, RoundingMode.HALF_UP).floatValue(); // 单精度
    }


    static {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
//        BufferedReader br = null;
//        try {
//            br = new BufferedReader(new InputStreamReader(new FileInputStream("/cos_input.txt")));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
        try {
        Resource resource = resourceLoader.getResource("classpath:cos_input.txt");

            InputStream inputStream = resource.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String str = null;
            while ((str = bufferedReader.readLine()) != null){
                if(str.contains("caseid")){ // 跳过首行
                    continue;
                }
                String[] s = str.split("\t");
                CasePojo casePojo = new CasePojo(Integer.valueOf(s[0]), Integer.valueOf(s[1]), new BigDecimal(s[2]));
                data.add(casePojo);
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

@Data
@AllArgsConstructor
class CasePojo{
    private Integer caseId;
    private Integer tagId;
    private BigDecimal weight;
}
