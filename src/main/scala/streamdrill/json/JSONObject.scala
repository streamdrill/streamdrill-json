/*
 * Copyright (c) 2015, streamdrill UG (haftungsbeschränkt)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package streamdrill.json

import java.text.SimpleDateFormat
import java.util.{Date, Locale, TimeZone}

import scala.collection.JavaConverters._

/**
 * A JSON object representation that wraps the most common data types.
 * It is using java data types internally.
 *
 * @author Mikio L. Braun
 * @author Matthas L. Jugel
 *
 * @param obj The object that is wrapped in this JSONObject
 */
class JSONObject(obj: Any) {
  def isMap = obj.isInstanceOf[java.util.Map[String, Any] @unchecked]

  def toMap = obj.asInstanceOf[java.util.Map[String, Any]]

  def toArray = obj.asInstanceOf[java.util.List[Any]]

  def isArray = obj.isInstanceOf[java.util.List[Any] @unchecked]

  def isBoolean = obj.isInstanceOf[Boolean]

  def toBoolean = obj.asInstanceOf[Boolean]

  def isNumber = obj.isInstanceOf[Number]

  def toNumber = obj.asInstanceOf[Number]

  def toDouble: Double = obj match {
    case s: String => s.toDouble
    case d: Double => d
    case b: java.math.BigDecimal => b.doubleValue()
    case i: Int => i.toDouble
    case l: Long => l.toDouble
    case _ => throw new ClassCastException("Cannot cast " + obj + " to Double")
  }

  def toLong: Long = obj match {
    case s: String => s.toLong
    case i: Int => i
    case l: Long => l
    case d: Double if d.toLong == d => d.toLong
    case _ => throw new ClassCastException("Cannot cast " + obj + " to Long")
  }

  def toInt: Int = obj match {
    case s: String => s.toInt
    case l: Long => l.toInt
    case i: Int => i
    case d: Double if d.toInt == d => d.toInt
    case _ => throw new ClassCastException("Cannot cast " + obj + " to Int")
  }

  // Introduced new String() here to make sure we get a copy and not just
  // a substring to a much larger string.
  override def toString: String = if (obj == null)
    "null"
  else
    new String(obj.toString)

  def get(key: String): JSONObject = new JSONObject(toMap.get(key))

  def getOrElse(key: String, value: Any): JSONObject = {
    val o = toMap.get(key)
    if (o == null)
      new JSONObject(value)
    else
      new JSONObject(o)
  }

  def getIf(key: String): Option[JSONObject] = {
    val o = toMap.get(key)
    if (o == null)
      None
    else
      Some(new JSONObject(o))
  }

  def get(idx: Int): JSONObject = new JSONObject(toArray.get(idx))

  def getString(key: String) = get(key).toString

  def getStringOrElse(key: String, v: String) = getOrElse(key, v).toString

  def getStringIf(key: String): Option[String] = getIf(key).map(_.toString)

  def getLong(key: String) = get(key).toLong

  def getLongOrElse(key: String, v: Long) = getOrElse(key, v).toLong

  def getLongIf(key: String): Option[Long] = getIf(key).map(_.toLong)

  def getInt(key: String) = get(key).toInt

  def getIntOrElse(key: String, v: Int) = getOrElse(key, v).toInt

  def getIntIf(key: String): Option[Int] = getIf(key).map(_.toInt)

  def getBoolean(key: String) = get(key).toBoolean

  def getBooleanOrElse(key: String, v: Boolean) = getOrElse(key, v).toBoolean

  def getBooleanIf(key: String): Option[Boolean] = getIf(key).map(_.toBoolean)

  def getArray(key: String) = get(key).toArray

  def getNumber(key: String): Number = get(key).toNumber

  def getDouble(key: String): Double = get(key).toDouble

  def getSeq(key: String): Seq[JSONObject] = get(key).asSeq

  private val dateFormat = new ThreadLocal[SimpleDateFormat]() {
    override def initialValue() = {
      val df = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy", Locale.ENGLISH)
      df.setTimeZone(TimeZone.getTimeZone("UTC"))
      df
    }
  }

  def getDate(key: String): Date = dateFormat.get.parse(getString(key))

  def has(key: String): Boolean = toMap.containsKey(key)

  def keys: Seq[String] = if (isMap) {
    var r = Seq[String]()
    val it = toMap.keySet.iterator
    while (it.hasNext)
      r :+= it.next
    r
  } else Seq()

  def length: Int = if (isArray)
    toArray.size
  else if (isMap)
    toMap.size
  else
    0

  def ifNonEmpty(stringField: String)(block: => Unit) {
    if (has(stringField) && getStringOrElse(stringField, "") != "")
      block
  }

  def asSeq: Seq[JSONObject] = toArray.asScala.map(new JSONObject(_))
}
