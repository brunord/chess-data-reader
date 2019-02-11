package pgn.util
/**
  * There is a set of tags defined for mandatory use for archival storage of PGN data.
  * This is the STR (Seven Tag Roster). The interpretation of these tags is fixed as is the order in which they appear.
  * Although the definition and use of additional tag names and semantics is permitted and encouraged when needed,
  * the STR is the common ground that all programs should follow for public data interchange.
  *
  * For import format, the order of tag pairs is not important. For export format,
  * the STR tag pairs appear before any other tag pairs. (The STR tag pairs must also appear in order;
  * this order is described below). Also for export format, any additional tag pairs appear in ASCII order by tag name.
  */

object SevenTagRoster {

  val EVENT = "Event" //(the name of the tournament or match event)

  val SITE = "Site" //(the location of the event)

  val DATE = "Date" //(the starting date of the game)

  val ROUND = "Round" //(the playing round ordinal of the game)

  val WHITE = "White" //(the player of the white pieces)

  val BLACK = "Black" //(the player of the black pieces)

  val RESULT = "Result" // (the result of the game)

}