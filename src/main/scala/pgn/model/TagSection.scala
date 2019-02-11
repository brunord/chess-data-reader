package pgn.model

case class TagSection (tagPairs: List[TagPair]){

  val tagPairMap = tagPairs.map(t => t.tagName -> t).toMap

  def tagPair(tagName: String) = tagPairMap(tagName)

  override def toString: String = tagPairs.mkString("\n")

}
