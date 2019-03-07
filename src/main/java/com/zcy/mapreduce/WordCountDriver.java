package com.zcy.mapreduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;


/**
 * 相当于运行在yarn中的客户端
 * @author Administrator
 *
 */
public class WordCountDriver {

    public static void main(String[] args) throws IOException {
        //输入路径
        String dst = "hdfs://172.17.101.36:9820/input";
        //输出路径，必须是不存在的，空文件加也不行。
        String dstOut = "/output/wordcount";

        Configuration conf = new Configuration();
        //如果是打包在linux上运行，则不需要写这两行代码
        //指定运行在yarn中
        /*conf.set("mapreduce.framework.name", "yarn");
        //指定resourcemanager的主机名
        conf.set("yarn.resourcemanager.hostname", "test36");*/
        //conf.addResource("config.xml");
        System.out.println("fs.defaultFS: "+conf.get("fs.defaultFS"));

        Job job = Job.getInstance(conf);

        //使得hadoop可以根据类包，找到jar包在哪里
        job.setJarByClass(WordCountDriver.class);

        //指定Mapper的类
        job.setMapperClass(WordCountMapper.class);
        //指定reduce的类
        job.setReducerClass(WordCountReduce.class);

        //设置Mapper输出的类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(LongWritable.class);

        //设置最终输出的类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);

        //指定输入文件的位置，这里为了灵活，接收外部参数
        FileInputFormat.setInputPaths(job, new Path(args[0]));
        //指定输出文件的位置
        Path outPath = new Path(args[1]);
        FileSystem fileSystem = outPath.getFileSystem(conf);
        if(fileSystem.exists(outPath)){
            fileSystem.delete(outPath, true);// true的意思是，就算output子文件夹，也一带删除
        }
        FileOutputFormat.setOutputPath(job, outPath);

        //将job中的参数，提交到yarn中运行
        //job.submit();
        try {
            job.waitForCompletion(true);
            //这里的为true,会打印执行结果
        } catch (ClassNotFoundException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
