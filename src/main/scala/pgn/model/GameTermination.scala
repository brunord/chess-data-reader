package pgn.model

case class GameTermination(termination: String){
  override def toString: String = termination
}
