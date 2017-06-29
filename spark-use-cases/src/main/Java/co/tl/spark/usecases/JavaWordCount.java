package co.tl.spark.usecases;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.Arrays;
import java.util.Map;

/**
 * Created by alexlanda on 23/06/2017.
 */
public class JavaWordCount {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("Java Word Count");
        JavaSparkContext sc = new JavaSparkContext(conf);

        Map<String, Integer> counts = sc.textFile("src/main/resources/book.txt")
                .flatMap(s -> Arrays.asList(s.split(" ")).iterator())
                .mapToPair(word -> new Tuple2<>(word, 1))
                .reduceByKey((a, b) -> a + b)
                .collectAsMap();
//        Map<String, Integer> countsMap = counts.collectAsMap();
    }
}
