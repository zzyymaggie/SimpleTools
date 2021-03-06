package xyz.zzyymaggie.util;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import xyz.zzyymaggie.model.BaseReadModel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelUtil {

    public static List<BaseReadModel> simpleReadJavaModel(String filename) {
        List<BaseReadModel> result = new ArrayList<>();
        InputStream inputStream = FileUtil.getResourcesFileInputStream(filename);
        List<Object> data = EasyExcelFactory.read(inputStream, new Sheet(1, 1, BaseReadModel.class));
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Object item : data) {
            BaseReadModel model = (BaseReadModel) item;
            if (model.getDiffVal() > model.getAvgVal()) {
                throw new RuntimeException("浮动范围必须小于平均值");
            }
            result.add(model);
        }
        return result;
    }

    public static void print(List<BaseReadModel> datas) {
        int i = 0;
        for (Object ob : datas) {
            i++;
            System.out.println(i + ":" + ob);
        }
    }

    public static void writeV2007() throws IOException {
        OutputStream out = new FileOutputStream("/Users/sophia/Documents/2007.xlsx");
        ExcelWriter writer = EasyExcelFactory.getWriter(out);
        //写第一个sheet, sheet1  数据全是List<String> 无模型映射关系
        Sheet sheet1 = new Sheet(1, 0);
        sheet1.setSheetName("第一个sheet");

        //设置列宽 设置每列的宽度
//        Map columnWidth = new HashMap();
//        columnWidth.put(0,10000);columnWidth.put(1,40000);columnWidth.put(2,10000);columnWidth.put(3,10000);
//        sheet1.setColumnWidthMap(columnWidth);
        //设置表头
//        sheet1.setHead(DataUtil.createTestListStringHead());
        //or 设置自适应宽度
        sheet1.setAutoWidth(Boolean.TRUE);
        writer.write1(DataUtil.createTestListObject(), sheet1);

        writer.finish();
        out.close();
    }

    public static void writeData(String filename, List<List<Object>> data, List<List<Object>> originData) throws IOException {
        OutputStream out = new FileOutputStream(filename);
        ExcelWriter writer = EasyExcelFactory.getWriter(out);

        Sheet sheet1 = new Sheet(1, 0);
        sheet1.setSheetName("输入");
        sheet1.setAutoWidth(Boolean.TRUE);
        sheet1.setHead(DataUtil.createHead0());
        writer.write1(originData, sheet1);

        //写第一个sheet, sheet1  数据全是List<String> 无模型映射关系
        Sheet sheet2 = new Sheet(2, 0);
        sheet2.setSheetName("输出结果");
        Map columnWidth = new HashMap();
        columnWidth.put(0, 3000);
        columnWidth.put(1, 3000);
        columnWidth.put(2, 3000);
        columnWidth.put(3, 3000);
        columnWidth.put(4, 3000);
        sheet2.setColumnWidthMap(columnWidth);
        sheet2.setHead(DataUtil.createTestListStringHead());
        writer.write1(data, sheet2);
        //关闭资源
        writer.finish();
        out.close();
    }
}
