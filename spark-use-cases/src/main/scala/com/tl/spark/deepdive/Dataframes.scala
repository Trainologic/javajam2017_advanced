package com.tl.spark.deepdive

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types._

object Dataframes extends App {
  val spark = SparkSession
    .builder()
    .appName("Spark App")
    .master("local[4]")
    .getOrCreate()
  import spark.implicits._
  case class NumSum(n: Int, d: Int)

  val df = spark.sparkContext.parallelize(1 to 5).map(i => (i, i + i))
    .toDF("value", "double")
  val ds = df.withColumnRenamed("value","n").withColumnRenamed("double","d").as[NumSum]
  ds.show()

  val ds2 = spark.sparkContext.parallelize(1 to 5).map(i => NumSum(i,i*2)).toDS()
  ds2.show()

  val jsonSchema = new StructType().add("time", TimestampType).add("action", StringType)
}
