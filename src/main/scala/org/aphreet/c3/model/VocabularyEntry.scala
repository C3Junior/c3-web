package org.aphreet.c3.model

import net.liftweb.common.Full
import net.liftweb.mapper._
import net.liftweb.util.FieldError

class VocabularyEntry extends LongKeyedMapper[VocabularyEntry] with IdPK {
  def getSingleton = VocabularyEntry

  object key extends MappedString(this, 64) {
    override def validations = nonEmpty _ :: isUnique _ :: Nil

    def isUnique(s: String): List[FieldError] = {
      if (VocabularyEntry
        .findAll(
          Cmp(VocabularyEntry.key, OprEnum.Eql, Full(s.toLowerCase), None, Full("LOWER")),
          By(VocabularyEntry.vocabulary, vocabulary)).nonEmpty)
        List(FieldError(this, "Entry with key " + s + " already exists"))
      else Nil
    }

    def nonEmpty(s: String) =
      if (s.isEmpty) List(FieldError(this, "Entry's key cannot be empty"))
      else Nil
  }

  object value extends MappedString(this, 64) {
    override def validations = nonEmpty _ :: Nil

    def nonEmpty(s: String) =
      if (s.isEmpty) List(FieldError(this, "Entry's value cannot be empty"))
      else Nil
  }

  object vocabulary extends MappedLongForeignKey(this, Vocabulary)

}

object VocabularyEntry extends VocabularyEntry with LongKeyedMetaMapper[VocabularyEntry] {

  override def dbTableName = "vocabularyEntries"

  override def fieldOrder = Nil
}
