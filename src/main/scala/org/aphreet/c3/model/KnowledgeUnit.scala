package org.aphreet.c3.model

import net.liftweb.mapper._
import net.liftweb.util.FieldError
import net.liftweb.common.Full
import net.liftweb.mapper.Cmp

class KnowledgeUnit extends LongKeyedMapper[KnowledgeUnit] with IdPK with ManyToMany {
  def getSingleton = KnowledgeUnit

  object owner extends MappedLongForeignKey(this, User) {
  }

  object name extends MappedString(this, 64) {
    override def validations = nonEmpty _ :: isUnique _ :: Nil

    def isUnique(s: String): List[FieldError] = {
      if (!KnowledgeItem.find(Cmp(KnowledgeItem.name, OprEnum.Eql, Full(s.toLowerCase), None, Full("LOWER"))).isEmpty)
        List(FieldError(this, "KnowledgeItem with name " + s + " already exists"))
      else Nil
    }

    private def nonEmpty(s: String) =
      if (s.isEmpty) List(FieldError(this, "KnowledgeItem's name cannot be empty"))
      else Nil

  }

  object description extends MappedText(this)

  object resource extends MappedText(this)

  object parents extends MappedManyToMany(
    KnowledgeUnitParentAssociation,
    KnowledgeUnitParentAssociation.child,
    KnowledgeUnitParentAssociation.parent,
    KnowledgeUnit)
}

object KnowledgeUnit extends KnowledgeUnit with LongKeyedMetaMapper[KnowledgeUnit] {
  override def dbTableName = "KnowledgeUnits"
}
