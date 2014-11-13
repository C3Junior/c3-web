package org.aphreet.c3.model

import net.liftweb.mapper.{ MappedLongForeignKey, LongKeyedMetaMapper, IdPK, LongKeyedMapper }

class KnowledgeUnitParentAssociation extends LongKeyedMapper[KnowledgeUnitParentAssociation] with IdPK {
  def getSingleton = KnowledgeUnitParentAssociation
  object child extends MappedLongForeignKey(this, KnowledgeUnit)
  object parent extends MappedLongForeignKey(this, KnowledgeUnit)
}

object KnowledgeUnitParentAssociation extends KnowledgeUnitParentAssociation with LongKeyedMetaMapper[KnowledgeUnitParentAssociation]
