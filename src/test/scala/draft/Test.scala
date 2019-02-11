package draft

import org.apache.spark.sql.{Row, SQLContext}
import org.apache.spark.sql.expressions.Window
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.sql.types.{DataType, StringType, StructField, StructType}
import pgn.model.PGNGame
import pgn.parser.PGNParser
import pgn.util.LichessTagPair
import spark.SparkPGNProcessor


object Test{

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("my test").setMaster("local[*]")
    val sc = new SparkContext(conf)

    SparkPGNProcessor.buildGamesDataframe(sc, List("/Users/brunorodrigues/games.pgn"), LichessTagPair.stringValues)
      .groupBy("White").count().orderBy(col("count").desc).show(10);
  }
}

