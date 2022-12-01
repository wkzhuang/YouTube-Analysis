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
    val DataReader = new DataReadFromCSV("videos-stats.csv")
    val df = DataReader.getDF
    df.show()
    df.printSchema()
  }

  it should "get the likes" in {
    val DataReader = new DataReadFromCSV("videos-stats.csv")
    val df = DataReader.getLikes
    df.show()
  }

  it should "get the group by keyword" in {
    val DataReader = new DataReadFromCSV("videos-stats.csv")
    val df = DataReader.getKeyword
    df.show()
    df.printSchema()
  }

  it should "get by month" in {
    val DataReader = new DataReadFromCSV("videos-stats.csv")
    val df = DataReader.getByMonth
    df.show()

  }

}
