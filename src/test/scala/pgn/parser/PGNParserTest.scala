package pgn.parser

import org.scalatest.FunSuite
import pgn.util.LichessTagPair

import scala.io.Source

class PGNParserTest extends FunSuite{

  val lichessSingleGamePgnSample = Source.fromURL(getClass.getResource("/lichessSingleGame.pgn")).mkString

  test("pgn.parser.PGNParser.parse"){

    val pgn = PGNParser.parseString(lichessSingleGamePgnSample)
    val game = pgn.games.head

    assert(pgn.games.size == 1)
    assert(game.tagPairValue(LichessTagPair.EVENT.toString) == "Rated Classical game")
    assert(game.tagPairValue(LichessTagPair.BLACK.toString) == "mamalak")
    assert(game.tagPairValue(LichessTagPair.WHITE.toString) == "BFG9k")
    assert(game.tagPairValue(LichessTagPair.SITE.toString) == "https://lichess.org/j1dkb5dw")
    assert(game.tagPairValue(LichessTagPair.RESULT.toString) == "1-0")
    assert(game.tagPairValue(LichessTagPair.OPENING.toString) == "French Defense: Normal Variation")
    assert(game.tagPairValue(LichessTagPair.ECO.toString) == "C00")
    assert(game.tagPairValue(LichessTagPair.BLACK_RATING_DIFF.toString) == "-8")
    assert(game.tagPairValue(LichessTagPair.WHITE_RATING_DIFF.toString) == "+5")
    assert(game.tagPairValue(LichessTagPair.TERMINATION.toString) == "Normal")
    assert(game.tagPairValue(LichessTagPair.TIME_CONTROL.toString) == "600+8")

    assert(pgn.toString == lichessSingleGamePgnSample)
  }
}