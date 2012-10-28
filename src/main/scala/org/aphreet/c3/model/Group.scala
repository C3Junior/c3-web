/**
 * Copyright (c) 2011, Dmitry Ivanov
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *

 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following disclaimer
 * in the documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the IFMO nor the names of its contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.aphreet.c3.model

import net.liftweb.mapper._
import net.liftweb.util.FieldError
import xml.{NodeSeq, Text}
import net.liftweb.common.{Full, Failure, Empty, Box}
import net.liftweb.http.SHtml
import org.aphreet.c3.apiaccess.C3
import net.liftweb.util.Helpers._
import com.ifunsoftware.c3.access.fs.C3File
import com.ifunsoftware.c3.access.C3AccessException
import org.apache.commons.httpclient.HttpStatus

class Group extends LongKeyedMapper[Group] with IdPK with ManyToMany{

  thisgroup =>

  def getSingleton = Group

  object owner extends MappedLongForeignKey(this,User){
    override def toForm = Box(SHtml.selectObj[User](User.findAll.map(user => (user,user.email.is)),User.currentUser, usr => thisgroup.owner(usr)))
  }

  object users extends MappedManyToMany(UserGroup, UserGroup.group, UserGroup.user, User)

  object description extends MappedTextarea(this,2048){
    override def textareaRows  = 10
    override def textareaCols = 50
    override def displayName = "Group description"
  }

  object name extends MappedString(this,64){

    def isUnique(s: String): List[FieldError] = {
      if(!Group.find(By(Group.name,s),NotBy(Group.id,thisgroup.id)).isEmpty ) List(FieldError(this,Text("Group with this name already exists")))
      else Nil
    }

    override def validations = isUnique _ :: super.validations
  }

  object isOpenRegistration extends MappedBoolean(this){
    override def defaultValue = true
  }

  object isOpenView extends MappedBoolean(this) {
    override def defaultValue = true
  }

  def getChildren(): List[C3Resource] = getChildren("")

  def getChildren(directory: String) : List[C3Resource] = {

    var resources: List[C3Resource] = List()

    val children = C3().getFile(baseFilePath + directory).asDirectory.children()
    
    for(child <- children){
      if(child.isDirectory){
        resources = Catalog( name = child.name, group = this, created = child.versions.head.date) :: resources
      } else{
        resources = File(group = this, fullpath = directory + "/" + child.name, created = child.versions.head.date) :: resources
      }
    }
    resources
  }

  def getFile(path: String): Box[File] = tryo {
      C3().getFile(baseFilePath + path)
      File(group = this, fullpath = path) }


  override def delete_! : Boolean = {
    for(user <- users) {
      UserGroup.find(By(UserGroup.user,user), By(UserGroup.group,this)).map(_.delete_!).openOr()
    }
    super.delete_!
  }

  def baseFilePath = "/" + this.name.is + "/files"

  def createLink: NodeSeq = Text("/groups/" + id.is)
}

object Group extends Group with LongKeyedMetaMapper[Group] {

  override def dbTableName = "groups"

  override def fieldOrder = name :: Nil

  def findByName(name: String) = find(By(Group.name, name))

}