package org.aphreet.c3.model

import net.liftweb.common.Full
import net.liftweb.mapper._
import net.liftweb.util.FieldError

class Vocabulary extends LongKeyedMapper[Vocabulary] with IdPK with OneToMany[Long, Vocabulary] {
  def getSingleton = Vocabulary

  object name extends MappedString(this, 64) {
    override def validations = nonEmpty _ :: isUnique _ :: Nil

    def isUnique(s: String): List[FieldError] = {
      if (!Vocabulary.find(Cmp(Vocabulary.name, OprEnum.Eql, Full(s.toLowerCase), None, Full("LOWER"))).isEmpty)
        List(FieldError(this, "Vocabulary with name " + s + " already exists"))
      else Nil
    }

    def nonEmpty(s: String) =
      if (s.isEmpty) List(FieldError(this, "Vocabulary's name cannot be empty"))
      else Nil
  }

  object entries extends MappedOneToMany(VocabularyEntry, VocabularyEntry.vocabulary)
}

object Vocabulary extends Vocabulary with LongKeyedMetaMapper[Vocabulary] {

  override def dbTableName = "vocabularies"

  override def fieldOrder = Nil
}