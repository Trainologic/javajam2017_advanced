package com.tl.spark.deepdive

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions._

import scala.io.StdIn

object WindowFunctions extends App {

    //What is the difference between the revenue of each product and the revenue of the
    // best-selling product in the same category of that product?
    val spark = SparkSession
      .builder()
      .appName("Spark App")
      .master("local[5]")
      .config("spark.logLineage","true")
      .getOrCreate()

  val revenue = spark.read
    .option("infer_schema","true")
    .option("header","true")
    .csv("src/main/resources/revenues.csv")

    revenue.printSchema()
    revenue.show()

  val query = """SELECT
  product,
  category,
  revenue
  FROM (
    SELECT
      product,
    category,
    revenue,
    dense_rank() OVER (PARTITION BY category ORDER BY revenue DESC) as rank
      FROM productRevenue) tmp
    WHERE
  rank <= 2"""

  revenue.createOrReplaceTempView("productRevenue")
  spark.sql(query).show()
  StdIn.readLine()



}
