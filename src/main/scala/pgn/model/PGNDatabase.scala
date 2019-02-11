package pgn.model

case class PGNDatabase (games : List[PGNGame]){

  override def toString: String = games.mkString("\n\n")
}
