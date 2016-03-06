import org.apache.spark.{SparkContext, SparkConf}

/**
 * Created by make on 3/6/16.
 */
object SparkTest {

  def main(args: Array[String]): Unit = {

    val logFile = "/home/make/log.txt" // Should be some file on your system
    val conf = new SparkConf().setAppName("Simple Application").setMaster("local")
    val sc = new SparkContext(conf) // 中文会出错吗？
    val logData = sc.textFile(logFile, 2).cache()
    val numAs = logData.filter(line => line.contains("a")).count()
    val numBs = logData.filter(line => line.contains("b")).count()
    println("Lines with a: %s, Lines with b: %s".format(numAs, numBs))

  }

}
