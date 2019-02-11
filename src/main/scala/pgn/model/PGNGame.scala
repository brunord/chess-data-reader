package pgn.model

case class PGNGame (tagSection: TagSection, moveTextSection: MoveTextSection){

  override def toString: String = tagSection + "\n\n" + moveTextSection

  def tagPairValue(tagName: String) : String = tagSection.tagPair(tagName).tagValue
}
