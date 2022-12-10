package controllers

import models.readToday
import org.apache.spark.sql.SparkSession
import org.scalatest.{BeforeAndAfter, flatspec}
import org.scalatest.matchers.should.Matchers

class readTodaySepc extends flatspec.AnyFlatSpec with Matchers with BeforeAndAfter{

  implicit var spark: SparkSession = _

  before {
    spark = SparkSession
      .builder()
      .appName("DataReader")
      .master("local[*]")
      .getOrCreate()
    spark.sparkContext.setLogLevel("ERROR")
  }

  after {
    if (spark != null) {
      spark.stop()
    }
  }

  behavior of "Spark"

  it should "get the data frame" in {
    val DataReader = new readToday("Dec1_trending_video.csv")
    val df = DataReader.getDF2()
    df.show()
  }

}
