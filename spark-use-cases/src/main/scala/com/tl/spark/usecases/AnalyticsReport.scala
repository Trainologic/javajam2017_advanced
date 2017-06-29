package com.tl.spark.usecases

import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}
import org.apache.spark.sql.functions._

import scala.io.StdIn

/**
  * Created by alexlanda on 01/06/2017.
  */
object AnalyticsReport extends App {
  
  case class Flight(Year: Int 
  , Month: Int
  , DayofMonth: Int
  , DayOfWeek: Int
  , DepTime: String
  , CRSDepTime: Int
  , ArrTime: String
  , CRSArrTime: Int
  , UniqueCarrier: String
  , FlightNum: Int
  , TailNum: String
  , ActualElapsedTime: String
  , CRSElapsedTime: Int
  , AirTime: String
  , ArrDelay: String
  , DepDelay: String
  , Origin: String
  , Dest: String
  , Distance: Int
  , TaxiIn: String
  , TaxiOut: String
  , Cancelled: Int
  , CancellationCode: String
  , Diverted: Int
  , CarrierDelay: String
  , WeatherDelay: String
  , NASDelay: String
  , SecurityDelay: String
  , LateAircraftDelay: String)

  case class Counter(airport: String, origin: Int, dest: Int)


  val spark = SparkSession
    .builder()
    .appName("Analytics report")
    .master("local[4]")
    .getOrCreate()

  import spark.implicits._

  val flightsDF: Dataset[Flight] = spark.read.option("inferSchema",true)
    .option("header", true)
    .csv("src/main/resources/flights.csv").as[Flight]

  flightsDF.printSchema()
  val sortDesc = col("count").desc

  flightsDF.where("Cancelled > 0").groupBy(flightsDF("Origin")).count().sort(sortDesc).show()
  flightsDF.where("Cancelled > 0").groupBy(flightsDF("Dest")).count().sort(sortDesc).show()


  //Optimization number 1 - caching
  val cached = flightsDF.where("Cancelled > 0").cache()
  println("cached")
  cached.groupBy(flightsDF("Origin")).count().sort(sortDesc).show()
  cached.groupBy(flightsDF("Dest")).count().sort(sortDesc).show()


  //Optimization number 2 - single pass - no caching
  flightsDF.where("Cancelled > 0")
    .flatMap(r => Seq(Counter(r.Origin,1,0), Counter(r.Dest,0,1)))
    .groupBy(col("airport"))
    .agg(sum(col("origin")).as("departed"),sum(col("dest")).as("arrived"))
    .sort(desc("departed"),desc("arrived"))
    .show()

  //Optimization number 2 - single pass - no caching - pure sql api :)
  flightsDF.where("Cancelled > 0")
    .select("Origin","Dest")
    .withColumn("locations",array(col("Origin"),col("Dest")))
    .select(explode(col("locations")).as("airport"),
      when($"airport"===$"Origin",1).otherwise(0).as("origin"),
      when($"airport"===$"Dest",1).otherwise(0).as("dest")
    )
    .groupBy(col("airport"))
    .agg(sum(col("origin")).as("departed"),sum(col("dest")).as("arrived"))
    .sort(desc("departed"),desc("arrived"))
    .show()
  StdIn.readLine()

}
