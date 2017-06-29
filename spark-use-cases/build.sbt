name := "spark-use-cases"

version := "1.0"

scalaVersion := "2.11.11"


val sparkVersion = "2.1.1"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core"      % sparkVersion,
  "org.apache.spark" %% "spark-streaming" % sparkVersion,
  "org.apache.spark" %% "spark-sql"       % sparkVersion
)