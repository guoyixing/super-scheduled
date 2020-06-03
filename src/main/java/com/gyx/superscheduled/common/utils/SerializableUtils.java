package com.gyx.superscheduled.common.utils;

import com.gyx.superscheduled.model.IncObjectOutputStream;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SerializableUtils {

    public static void toFile(Object obj, String fileSrc, String fileName) {
        fileSrc = existFile(fileSrc);
        fileSrc = fileSrc + File.separator + fileName;
        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(fileSrc))) {
            os.writeObject(obj);
            os.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void toIncFile(Object obj, String fileSrc, String fileName) {
        fileSrc = existFile(fileSrc);
        File file = new File(fileSrc + File.separator + fileName);
        try (ObjectOutputStream os = new IncObjectOutputStream(file)) {
            os.writeObject(obj);
            os.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public  static <T> List<T> fromIncFile(String fileSrc, String fileName) {
        List<T> list = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileSrc + File.separator + fileName))) {
            T obj = null;
            while ((obj = (T) ois.readObject()) != null){
                list.add(obj);
            }
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    /**
     * 判断文件夹是否存在，如果不存在就创建
     * @param fileSrc 文件夹路径
     */
    private static String existFile(String fileSrc) {
        if (StringUtils.isEmpty(fileSrc)) {
            fileSrc = "";
        } else {
            File file = new File(fileSrc);
            //如果文件夹不存在
            if (!file.exists()) {
                //创建文件夹
                file.mkdir();
            }
        }
        return fileSrc;
    }
}
