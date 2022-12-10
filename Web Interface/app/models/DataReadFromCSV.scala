package models

import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.regression.LinearRegression
import org.apache.spark.sql
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}

class DataReadFromCSV(resource: String) {
  val spark: SparkSession = SparkSession
    .builder()
    .appName("DataReadFromCSV")
    .config("spark.some.config.option","some-value")
    .master("local[*]")
    .getOrCreate()

  spark.sparkContext.setLogLevel("ERROR") // We want to ignore all of the INFO and WARN message
  val path = "app/resources/US_youtube_trending_data.csv"
  val df = spark.read.option("header","true").option("inferSchema","true").csv(path)

  def getDF: sql.DataFrame = {
    df
  }

  def getSelectedColumns:sql.DataFrame = {

    /* converting all column type into integer */
    val intDf: DataFrame = df.withColumn("categoryId",col("categoryId").cast("integer"))
    .withColumn("view_count",col("view_count").cast("integer"))
    .withColumn("likes",col("likes").cast("integer"))
    .withColumn("comment_count",col("comment_count").cast("integer"))

    val dataset: DataFrame = intDf.select("publishedAt","categoryId","view_count","likes","comment_count","category")
    val dataFrame: Dataset[Row] = dataset.filter((dataset("publishedAt").isNotNull) && (dataset("categoryId").isNotNull) && (dataset("view_count").isNotNull)  && (dataset("likes").isNotNull) && (dataset("category").isNotNull) && (dataset("comment_count").isNotNull))
    dataFrame
  }

  def dateConversion: sql.DataFrame = {

    val dataFrame = getSelectedColumns.filter(getSelectedColumns("publishedAt").isNotNull)
      .withColumn("dateFormat", date_format(getSelectedColumns("publishedAt"), "yyyy-MM-dd"))

    val datawithIntDate: Dataset[Row] = dataFrame.withColumn("DateinDouble", year(dataFrame("dateFormat"))+(month(dataFrame("dateFormat"))*1/12)+(dayofmonth(dataFrame("dateFormat")))*0.1/31)
     .drop("dateFormat","publishedAt")
    val finalDf: Dataset[Row] = datawithIntDate.filter(datawithIntDate("DateinDouble").isNotNull)
    finalDf


  }

  def getCategoryList: List[Any] = {
    val df = dateConversion
    val listwithC: List[Any] = df.select("category").distinct().rdd.map(row => row(0)).collect().toList
    listwithC
  }

  def getCategoryName(category:String): sql.DataFrame = {
    val data = dateConversion
    val keyword: Dataset[Row] = data.where(data("category") === category)
    val w = Window.orderBy("DateinDouble")  //sort by asc
    val leadDf: DataFrame = keyword.withColumn("target_likes",lead("likes",1).over(w))
    leadDf.show(10)
    leadDf
  }


  def modelTraining(categoryName: String) : Seq[Seq[String]]= {

    val data = getCategoryName(categoryName)
    val df = data.where(data("category")=== categoryName)
    val dataToPredict: Dataset[Row] = data.filter(data("target_likes").isNull)
    val filteredData = data.filter(data("target_likes").isNotNull)
    val renamedDataset = filteredData.withColumnRenamed("target_likes", "label")

    val assembler: VectorAssembler = new VectorAssembler()
      .setInputCols(Array("DateinDouble","comment_count","view_count"))
      .setOutputCol("features")
      .setHandleInvalid("skip")

    val dataFrame: DataFrame = assembler.transform(renamedDataset)
    val vectoredpredictData = assembler.transform(dataToPredict)
    dataFrame.filter(dataFrame("DateinDouble").isNotNull && dataFrame("comment_count").isNotNull && dataFrame("view_count").isNotNull && dataFrame("label").isNotNull).show()

    // Splitting the data into training and test set
    val Array(training, test) = dataFrame.randomSplit(Array(0.8, 0.2), 42)
    val lr = new LinearRegression()
    val lrModel = lr.fit(training)
    val testDataSummary = lrModel.evaluate(test)
    println(s"Coefficients: ${lrModel.coefficients} Intercept: ${lrModel.intercept}")
    println(s"RMSE: ${testDataSummary.rootMeanSquaredError}")
    println(s"r2: ${testDataSummary.r2}")
    vectoredpredictData.drop("target_likes").show()

    // predicting the like count for video for user given category
    val finalresult: DataFrame = lrModel.transform(vectoredpredictData.drop("target_likes")).drop("features")
    finalresult.collect().map(row => row.toSeq.map(_.toString)).toSeq
    }

}
