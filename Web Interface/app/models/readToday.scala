package models

import org.apache.spark.sql
import org.apache.spark.sql.SparkSession

class readToday(resource: String) {

  val spark: SparkSession = SparkSession
    .builder()
    .appName("readToday")
    .config("spark.some.config.option", "some-value")
    .master("local[*]")
    .getOrCreate()

  spark.sparkContext.setLogLevel("ERROR")

  val path = "app/resources/Dec8_trending_video.csv"

  val df = spark.read.format("csv")
    .option("header", "true")
    .option("inferSchema", true)
    .option("multiLine", true)
    .load(path)

  def getDF(): Seq[Seq[String]] = {
    val result = df.select("title", "viewCount","likeCount","commentCount","category").na.fill("null")
    val returnSeq = result.collect().map(row => row.toSeq.map(_.toString)).toSeq
    returnSeq
  }

  def getDF2(): sql.DataFrame = {
    df
  }

  def getCategoryList: List[Any] = {
    val listwithC = df.select("Keyword").distinct().rdd.map(row => row(0)).collect().toList
    listwithC
  }

}
