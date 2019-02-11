package pgn.model

case class MoveTextSection(moveTextElements: List[MoveTextElement], termination: GameTermination){

  override def toString: String = moveTextElements.mkString(" ") + " " + termination
}
