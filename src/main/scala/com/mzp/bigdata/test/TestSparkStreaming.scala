package com.mzp.bigdata.test

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

object TestSparkStreaming {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[2]").setAppName("NetworkWordCount")
    val ssc = new StreamingContext(conf, Seconds(5))

    // Create a DStream that will connect to hostname:port, like localhost:9999
    // 安装netcat 放到system32文件夹下
    // 在cmd里输入命令 nc -L -p 9999 -v
    // 开启本代码，然后在cmd里输入一些文字
    ////ADJFI
    val lines = ssc.socketTextStream("localhost", 9999)


    // Split each line into words
    val words = lines.flatMap(_.split(" "))

    // Count each word in each batch
    val pairs = words.map(word => (word, 1))
    val wordCounts = pairs.reduceByKey(_ + _)

    // Print the first ten elements of each RDD generated in this DStream to the console
    wordCounts.print()

    ssc.start()             // Start the computation
    ssc.awaitTermination()  // Wait for the computation to terminate
  }
}
