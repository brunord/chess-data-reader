package pgn.model

case class MoveTextElement (moveNumberIndication: MoveNumber, whiteMove: MoveTextSAN , blackMove: MoveTextSAN){

  override def toString: String = {

    if(blackMove.toString.isEmpty)
      moveNumberIndication + ". " + whiteMove
    else
      moveNumberIndication + ". " + whiteMove + " " + blackMove
  }
}
