package pgn.model


case class MoveTextSAN (move: String, suffix: String, comment: String){

  override def toString: String = {

    if(comment.isEmpty)
      move + suffix
    else
      move + suffix + " " + comment
  }
}
