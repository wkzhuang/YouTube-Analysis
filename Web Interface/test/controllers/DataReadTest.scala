package controllers

import models.DataReadFromCSV
import org.apache.spark.sql.SparkSession
import org.scalatest.matchers.should.Matchers
import org.scalatest.{BeforeAndAfter, flatspec}

class DataReadTest extends flatspec.AnyFlatSpec with Matchers with BeforeAndAfter{

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
    val DataReader = new DataReadFromCSV("US_youtube_trending_data.csv")
    val df = DataReader.getDF
    df.show()
    df.printSchema()
  }

  it should "get selected columns" in {
    val DataReader = new DataReadFromCSV("US_youtube_trending_data.csv")
    val df = DataReader.getSelectedColumns
    df.show()
    df.printSchema()
    df.columns.size shouldBe 6
  }

  it should "get DateConversion in Int" in {
    val DataReader = new DataReadFromCSV("Final_Trending_dataset_3.csv")
    val df = DataReader.dateConversion
    df.show()
    df.schema("DateinDouble").dataType.typeName shouldBe  "double"

  }

  it should "get df by categoryName" in {
    val DataReader = new DataReadFromCSV("US_youtube_trending_data.csv")
    val df = DataReader.getCategoryName("Gaming")
    df.show()
    df.columns.size shouldBe 7
  }

  it should "get trained model" in {
    val DataReader = new DataReadFromCSV(resource = "US_youtube_trending_data.csv")
    val df = DataReader.modelTraining("Gaming")
    df
  }

}
