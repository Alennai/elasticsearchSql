/**
 * @author shaco.zhu
 * @email shaco.zhu@dbappsecurity.com.cn
 * Date:2017年8月9日
 */
package org.parc.restes.util;

import com.alibaba.fastjson.JSONObject;
import org.parc.restes.entity.ElasticDate;
import org.parc.restes.entity.Field;
import org.parc.restes.service.IEsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ExportUtil {
    private static final Logger logger = LoggerFactory.getLogger(ExportUtil.class);
    /**
     * CSV文件列分隔符
     */
    private static final String CSV_COLUMN_SEPARATOR = ",";

    /**
     * CSV文件列分隔符
     */
    private static final String CSV_RN = "\n";

    /**
     * @param dataList 集合数据
     * @param columns  表头部数据
     */
    public static boolean doExport(List<JSONObject> dataList, List<String> columns, OutputStream os, String type,
                                   IEsService IEsService) {
        try {

            String[] colNamesArr = null;
            String[] mapKeyArr = null;

            if (columns == null || columns.isEmpty()) {
                columns = Arrays.asList(new String[]{"@timestamp", "rawEvent"});
            }
            int size = columns.size();
            colNamesArr = new String[size];
            mapKeyArr = new String[size];
            for (int i = 0; i < size; i++) {
//                String t_tmp = DictionaryCache.field(columns.get(i));
                String t_tmp="";
                Field field = IEsService.categories(type, columns.get(i));
                if (field != null) {
                    t_tmp = field.getName();
                }
                colNamesArr[i] = t_tmp;
                mapKeyArr[i] = columns.get(i);
            }

//            CsvWriter writer = new CsvWriter(os, ',', Charset.forName("gbk"));
//            writer.writeRecord(colNamesArr);

            if (null != dataList) { // 输出数据
                for (int i = 0; i < dataList.size(); i++) {
                    String[] column = new String[mapKeyArr.length];
                    for (int j = 0; j < mapKeyArr.length; j++) {
                        String data = dataList.get(i).getString(mapKeyArr[j]);
                        if ("@timestamp".equals(mapKeyArr[j])) {
                            column[j] = ElasticDate._7_2_1(data);
                        } else if ("rawEvent".equals(mapKeyArr[j])) {
                            data = data.replace(',', ';');
                            column[j] = data;
                        } else {
                            column[j] = data;
                        }
                    }
//                    writer.writeRecord(column);
                }
            }
//            writer.flush();
            os.flush();
            return true;
        } catch (Exception e) {
            logger.error("doExport错误...", e);
        }
        return false;
    }

    /**
     * @throws UnsupportedEncodingException setHeader
     */
    public static void responseSetProperties(HttpServletResponse response) throws UnsupportedEncodingException {
        // 设置文件后缀
        String fn = ElasticDate.format6(new Date()) + ".csv";
        // 读取字符编码
        String utf = "UTF-8";
        // 设置响应
        response.setContentType("application/x-download;charset=GBK");
        response.setCharacterEncoding("GBK");
        response.setHeader("Pragma", "public");
        response.setHeader("Cache-Control", "max-age=30");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fn, utf));
    }

}