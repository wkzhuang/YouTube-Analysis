import org.apache.spark.sql
import org.apache.spark.sql.functions.{col, month, to_date, year}
import org.apache.spark.sql.{SparkSession, functions}

class DataReadFromCSV(resource: String) {

  val spark: SparkSession = SparkSession
    .builder()
    .appName("DataReadFromCSV")
    .config("spark.some.config.option","some-value")
    .master("local[*]")
    .getOrCreate()

  spark.sparkContext.setLogLevel("ERROR") // We want to ignore all of the INFO and WARN message

  val path = "src/main/resources/videos-stats.csv"

  val df = spark.read.option("header","true").csv(path)


  def getDF(): sql.DataFrame = {
    df
  }

  def getLikes: sql.DataFrame = {
    val col = df.select("Likes")
    col.show()
    col
  }

  def getKeyword: sql.DataFrame = {
    val keyword = df.groupBy("Keyword").agg(functions.sum("Likes"))
    keyword
  }

  def getByMonth: sql.DataFrame = {

    val dfwithdate = df.withColumn("Published At",to_date(col("Published At"),"yyyy-mm-dd").as("to_date"))
    val bimonthly = dfwithdate.select( "Keyword", "Published At", "Likes").withColumn("year",year(col("Published At")))
      .withColumn("month",month(col("Published At")))
    bimonthly.groupBy("Keyword").pivot("year").agg(functions.sum("Likes"))
  }

}
