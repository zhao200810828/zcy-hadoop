package com.zcy.hadoop;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.log4j.LogManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.apache.log4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * java接口对Hadoop进行操作
 * 1.配置环境变量：HADOOP_HOME
 * HADOOP_USER_NAME
 * Created by zcy on 2019/03/06
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring配置文件
public class TestHadoop {
    private org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    private Logger eventLog = LogManager.getLogger("EventLog");

    /**
     * 连接Hadoop
     */
    public FileSystem connectHadoop() {
        String nameNodeUrl = "hdfs://172.17.101.36:9820";
        String nameNodeName = "fs.defaultFS";
        FileSystem fs = null;
        Configuration configuration = new Configuration();
        try {
            configuration.set(nameNodeName, nameNodeUrl);
            fs = FileSystem.get(configuration);
            logger.info("连接成功：Path={}", fs.getFileStatus(new Path("/")));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return fs;
    }

    /**
     * 创建目录
     *
     * @throws Exception 异常
     */
    @Test
    public void mkdirFolder() throws Exception {
        FileSystem fs = connectHadoop();
        String folderName = "/input";
        fs.mkdirs(new Path(folderName));
    }

    /**
     * 上传文件到Hadoop
     *
     * @throws Exception 异常
     */
    @Test
    public void uploadFile() throws Exception {
        FileSystem fs = connectHadoop();
        //定义本地上传的文件路径
        String localFilePath = "D://";
        //定义上传文件
        String fileName = "test2.txt";
        //定义要上传到的文件夹
        String uploadFolder = "/input/";

        InputStream in = new FileInputStream(localFilePath + fileName);
        OutputStream out = fs.create(new Path(uploadFolder + fileName));

        IOUtils.copyBytes(in, out, 4096, true);

    }

    /**
     * 从Hadoop获取文件
     *
     * @throws Exception 异常
     */
    @Test
    public void getFileFromHadoop() throws Exception {
        FileSystem fs = connectHadoop();
        //定义要下载路径
        String downloadPath = "/input/";
        //定义要下载的文件名
        String downloadFileName = "aiwm-call-management-1.0-SNAPSHOT.jar";
        //定义要保存的路径
        String savePath = "D://" + downloadFileName;

        InputStream in = fs.open(new Path(downloadPath + downloadFileName));
        OutputStream out = new FileOutputStream(savePath);
        IOUtils.copyBytes(in, out, 4096, true);
    }

    /**
     * 删除文件
     * delete(path,boolean)
     * boolean如果为true，将进行递归删除，子目录及文件都会删除
     * false 只删除当前
     *
     * @throws Exception
     */
    @Test
    public void deleteFile() throws Exception {
        FileSystem fs = connectHadoop();
        //要删除的文件路径
        String deleteFilePath = "/input/data.txt";
        Boolean deleteResult = fs.delete(new Path(deleteFilePath), true);
        logger.info("删除文件：={}", deleteResult);
    }

    /**
     * 遍历指定目录下所有的文件
     * @throws Exception 异常
     */
    @Test
    public void getAllFile()throws Exception{
        FileSystem fs = connectHadoop();
        //定义要获取的目录
        String getPath = "/";
        FileStatus[] statuses = fs.listStatus(new Path(getPath));
        for (FileStatus file: statuses
                ) {
            logger.info("fileName={}",file.getPath().getName());
        }
    }

    @Test
    public void otherOption() throws Exception{
        FileSystem fs = connectHadoop();
    }

}