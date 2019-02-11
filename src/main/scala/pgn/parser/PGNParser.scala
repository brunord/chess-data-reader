package pgn.parser

import pgn.model._

import scala.io.BufferedSource
import scala.util.parsing.combinator.RegexParsers


object PGNParser extends RegexParsers{

  // PGN token regex
  val tagPairNameRegex = "[a-zA-Z0-9_]+".r
  val suffixRegex = "(\\s*[\\!\\?]+)".r
  val moveSANRegex = "([PNBRQK]?[a-h]?[1-8]?x?[a-h][1-8](\\=[NBRQK])?|O(-?O){1,2})[\\+#]?".r
  val moveCommentRegex = "\\{(.*?)\\}".r
  val moveNumberRegex = "^[0-9]*[1-9][0-9]*".r

  // PGN token parsers
  private def tagPairValue: Parser[String] = """"[^"]*"""".r ^^ { str => str.substring(1, str.length - 1) }

  private def tagPair: Parser[TagPair] = "[" ~> tagPairNameRegex ~ tagPairValue <~ "]" ^^ { case id  ~ lit => TagPair(id, lit) }

  private def tagSection: Parser[TagSection] = rep(tagPair) ^^ { case tags => TagSection(tags) }

  private def moveSAN: Parser[MoveTextSAN] = moveSANRegex ~ opt(suffixRegex) ~ opt(moveCommentRegex) ^^ {
    case move ~ Some(suffix) ~ None => MoveTextSAN(move, suffix, "")
    case move ~ None ~ None => MoveTextSAN(move, "", "")
    case move ~ None ~ Some(comments) => MoveTextSAN(move, "", comments)
    case move ~ Some(suffix) ~ Some(comments) => MoveTextSAN(move, suffix, comments)
  }

  private def whiteMoveNumberIndication: Parser[MoveNumber] =  moveNumberRegex ~ "." ^^ {
    case move ~ _ => MoveNumber(move.toInt)
  }

  private def blackMoveNumberIndication: Parser[MoveNumber] =  moveNumberRegex ~ "." ~ "." ~ "." ^^ {
    case move ~ _ ~ _ ~ _ => MoveNumber(move.toInt)
  }

  private def element: Parser[MoveTextElement] = whiteMoveNumberIndication ~ moveSAN ~ opt(blackMoveNumberIndication) ~ opt(moveSAN)  ^^ {
    case number ~ whiteMove ~ Some(_) ~ Some(blackMove)  => MoveTextElement(number, whiteMove, blackMove)
    case number ~ whiteMove ~ None ~ Some(blackMove)  => MoveTextElement(number, whiteMove, blackMove)
    case number ~ whiteMove ~ None ~ None  => MoveTextElement(number, whiteMove, MoveTextSAN("", "", ""))
  }

  private def gameTermination: Parser[GameTermination] = "(1\\-0)|(0\\-1)|(1/2-1/2)|(\\*)".r ^^ {case t => GameTermination(t)}

  private def moveTextSection: Parser[MoveTextSection] = rep1(element) ~ gameTermination ^^ {
    case elements ~ termination => MoveTextSection(elements, termination)
  }

  private def game: Parser[PGNGame] = tagSection ~ moveTextSection ^^ { case ts ~ ms => PGNGame(ts, ms) }

  private def games: Parser[PGNDatabase] = rep(game) ^^ { case games => PGNDatabase(games) }

  def parse(pgn: BufferedSource): PGNDatabase = parseString(pgn.mkString)

  def parseString(pgnContent: String) = parseAll(games, pgnContent).get
}