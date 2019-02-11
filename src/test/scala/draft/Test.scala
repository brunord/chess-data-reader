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


object Test{

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("my test").setMaster("local[*]")
    val sc = new SparkContext(conf)
    val sqlContext = new HiveContext(sc)

    sc.hadoopConfiguration.set("textinputformat.record.delimiter","\n\n")

    val df = {

      val window = Window.partitionBy().orderBy("row_counter")

      sqlContext.read.text("/Users/brunorodrigues/games.pgn")
        .withColumn("row_counter", monotonically_increasing_id())
        .withColumn("tag_pairs", lag(col("value"), 1, null).over(window))
        .drop(col("row_counter")).filter(col("value").startsWith("1."))
        .toDF("move_text", "tag_pairs").select("tag_pairs", "move_text").limit(1)
    }

    def parseRow(row: Row) : Row = {
      val tagPairSectiono = row.getString(0)
      val moveTextSection = row.getString(1)

      val pgn = PGNParser.parseString(tagPairSectiono + " " + moveTextSection)
      val game = pgn.games.head

      val r = buildRowFromPGN(game, moveTextSection)
      println(r)
      r
    }

    def buildRowFromPGN(game: PGNGame, moveText: String): Row = {

      Row.fromSeq((for(value <- LichessTagPair.values) yield game.tagPairValue(value.toString)).toSeq)
    }

    val schema = {
      val fields = for(value <- LichessTagPair.values) yield StructField(value.toString, StringType)
       /**
        List(
        StructField(LichessTagPair.BLACK.toString, StringType),
        StructField(LichessTagPair.BLACK_ELO.toString, StringType),
        StructField(LichessTagPair.BL.toString, StringType),
        StructField(LichessTagPair.UTC_TIME.toString, StringType),
        StructField(LichessTagPair.WHITE.toString, StringType),
        StructField(LichessTagPair.BLACK.toString, StringType),
        StructField(LichessTagPair.RESULT.toString, StringType),
        StructField(LichessTagPair.WHITE_ELO.toString, StringType),
        StructField(LichessTagPair.BLACK_ELO.toString, StringType),
        StructField(LichessTagPair.WHITE_RATING_DIFF.toString, StringType),
        StructField(LichessTagPair.BLACK_RATING_DIFF.toString, StringType),
        StructField(LichessTagPair.ECO.toString, StringType),
        StructField(LichessTagPair.TERMINATION.toString, StringType),
        StructField(LichessTagPair.TIME_CONTROL.toString, StringType),
        StructField(LichessTagPair.OPENING.toString, StringType),
        StructField("game", StringType)
      )**/
      println(fields.toList)
      StructType(fields.toList)
    }
    //df.show()
    println(schema)
    sqlContext.createDataFrame(df.map(parseRow), schema).show()

    val values = for(value <- LichessTagPair.values) yield value.toString
    //println(values.toList)
    //println(Row.fromSeq(values.toSeq))

    //println(Row.fromSeq(values.toSeq))
  }



}

