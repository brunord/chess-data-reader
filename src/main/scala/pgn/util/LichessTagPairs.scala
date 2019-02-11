package pgn.util

object LichessTagPair extends Enumeration {

  type LichessTagPair = Value

  val EVENT = Value("Event")

  val SITE = Value("Site")

  val UTC_DATE = Value("UTCDate")

  val UTC_TIME = Value("UTCTime")

  val WHITE = Value("White")

  val BLACK = Value("Black")

  val RESULT = Value("Result")

  val WHITE_ELO = Value("WhiteElo")

  val BLACK_ELO = Value("BlackElo")

  val WHITE_RATING_DIFF = Value("WhiteRatingDiff")

  val BLACK_RATING_DIFF = Value("BlackRatingDiff")

  val ECO = Value("ECO")

  val TERMINATION = Value("Termination")

  val TIME_CONTROL = Value("TimeControl")

  val OPENING = Value("Opening")
}
