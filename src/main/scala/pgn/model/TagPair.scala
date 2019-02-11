package pgn.model

case class TagPair (tagName: String, tagValue: String){

  override def toString: String = "[" + tagName + " " + """"""" + tagValue + """"""" + "]"
}
