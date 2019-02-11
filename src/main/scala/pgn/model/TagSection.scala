package pgn.model

case class TagSection (tagPairs: List[TagPair]){

  val tagPairMap = tagPairs.map(t => t.tagName -> t).toMap

  def tagPair(tagName: String) = if(tagPairMap.contains(tagName)) tagPairMap(tagName) else TagPair(tagName, "")

  override def toString: String = tagPairs.mkString("\n")

}
