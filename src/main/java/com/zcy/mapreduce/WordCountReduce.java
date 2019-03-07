package com.zcy.mapreduce;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * 第一个Text: 是传入的单词名称，是Mapper中传入的
 * 第二个：LongWritable 是该单词出现了多少次，这个是mapreduce计算出来的，比如 hello出现了11次
 * 第三个Text: 是输出单词的名称 ，这里是要输出到文本中的内容
 * 第四个LongWritable： 是输出时显示出现了多少次，这里也是要输出到文本中的内容
 * @author Administrator
 *
 */
public class WordCountReduce extends Reducer<Text, LongWritable, Text, LongWritable> {

    @Override
    protected void reduce(Text key, Iterable<LongWritable> values,
                          Reducer<Text, LongWritable, Text, LongWritable>.Context context) throws IOException, InterruptedException {
        long count = 0;
        for (LongWritable num : values) {
            count += num.get();
        }
        context.write(key, new LongWritable(count));
    }
}