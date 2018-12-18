package org.parc.restes.util;

import org.parc.restes.entity.Index;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import static org.parc.restes.util.ESConstant.ptn1;


/**
 * Created by xusiao on 2018/5/10.
 */
public class CurlUtil {

    public static List<Index> indexList(String ip, String restPort) {
        List<Index> indexList = new ArrayList<>();
        String[] cmds = {"curl", ip + ":" + restPort + "/_cat/indices?v"};
        ProcessBuilder pb = new ProcessBuilder(cmds);
        pb.redirectErrorStream(true);
        Process p;
        try {
            p = pb.start();
            BufferedReader br = null;
            String line = null;
            boolean iscontent = false;

            br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = br.readLine()) != null) {
                if (iscontent) {
                    String[] content = line.split("\\s+");
                    String index = content[2];
                    Matcher m = ptn1.matcher(index);
                    if (m.find()) {
                        Index tmpIndex = new Index();
                        tmpIndex.setName(index);
                        tmpIndex.setOpstatus(content[1]);
                        tmpIndex.setHealth(content[0]);
                        indexList.add(tmpIndex);
                    }
                }
                if (line.contains("health"))
                    iscontent = true;

            }
            br.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        indexList.sort((o1, o2) -> {
            String index1 = o1.getName();
            String index2 = o2.getName();
            Matcher m1 = ptn1.matcher(index1);
            Matcher m2 = ptn1.matcher(index2);
            if (m1.find()) {
                index1 = m1.group();
            }
            if (m2.find()) {
                index2 = m2.group();
            }
            int date1 = Integer.parseInt(index1.substring(index1.lastIndexOf('-') + 1));
            int date2 = Integer.parseInt(index2.substring(index1.lastIndexOf('-') + 1));
            return date2 - date1;
        });
        return indexList;
    }
}
