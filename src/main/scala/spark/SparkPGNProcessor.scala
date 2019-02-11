package spark

import org.apache.spark.SparkContext
import org.apache.spark.sql.{DataFrame, Row}
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions.{col, lag, monotonically_increasing_id}
import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import pgn.model.PGNGame
import pgn.parser.PGNParser


object SparkPGNProcessor  extends java.io.Serializable {

  def buildGamesDataframe(sparkContext: SparkContext, pgnFilesPaths: List[String], pgnTagKeys: List[String]) : DataFrame = {

    val hiveContext = new HiveContext(sparkContext)

    sparkContext.hadoopConfiguration.set("textinputformat.record.delimiter","\n\n")

    val window = Window.partitionBy().orderBy("row_counter")
    val df = hiveContext.read.text(pgnFilesPaths:_*)
      .withColumn("row_counter", monotonically_increasing_id())
      .withColumn("tag_pairs", lag(col("value"), 1, null).over(window))
      .drop(col("row_counter")).filter(col("value").startsWith("1."))
      .toDF("move_text", "tag_pairs").select("tag_pairs", "move_text")

    val rdd = df.map((row => buildRowFromPGN(PGNParser.parseString(row.getString(0) + " " + row.getString(1)).games.head, pgnTagKeys)))

    hiveContext.createDataFrame(rdd, schema(pgnTagKeys))
  }

  private def buildRowFromPGN(game: PGNGame, pgnTagKeys: List[String]): Row = {
    var fields = List[String]()
    pgnTagKeys.foreach(value => {fields = fields :+ game.tagPairValue(value.toString)})
    Row.fromSeq(fields :+ game.moveTextSection.toString)
  }

  private def schema(pgnTagKeys: List[String]): StructType = {
    var fields = List[StructField]()
    pgnTagKeys.foreach(value => {fields = fields :+ StructField(value.toString, StringType)})
    StructType(fields :+ StructField("movetext", StringType))
  }
}