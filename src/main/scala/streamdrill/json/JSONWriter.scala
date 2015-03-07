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
import java.util.{Locale, TimeZone}

import scala.collection.JavaConverters._

trait HasToJSON {
  def toJSON: String
}

/**
 * JSON object serializer. Writes an object as a JSON represention.
 * Handles java and scala types equally.
 */
object JSONWriter {
  //val dateFormat = "EEE, dd MMM yyyy HH:mm:ss Z"
  val dateFormat = new ThreadLocal[SimpleDateFormat]() {
    override def initialValue() = {
      val df = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy", Locale.ENGLISH)
      df.setTimeZone(TimeZone.getTimeZone("UTC"))
      df

    }
  }

  /**
   * Write an arbitrary data object as json. Handles Java and Scala types.
   *
   * @param v the value that should be represented as JSON
   * @return a string representation of the value
   */
  def toJSON[T](v: T)(implicit manifest: Manifest[T]): String = {
    (v: @unchecked) match {
      case b: Boolean => if (b) "true" else "false"
      case i: Int => i.toString
      case l: Long => l.toString
      case x: Double if x.isInfinite || x.isNaN => "0.0"
      case x: Double => java.lang.Double.toString(x)
      case n: Number => n.toString
      case s: String => formatString(s)
      case d: java.util.Date => formatDate(d)
      case m: Map[String, _] @unchecked => formatMap(m)
      case m: java.util.Map[String, _] @unchecked => formatMap(m.asScala.toMap)
      case l: List[_] @unchecked => formatList(l)
      case l: java.util.List[_] => formatSeq(l.asScala.toSeq)
      case s: Seq[_] => formatSeq(s)
      case st: Set[_] => formatSeq(st.toSeq)
      case a: Array[Byte] => formatByteArray(a)
      case ia: Array[Int] => formatIntArray(ia)
      case la: Array[Long] => formatLongArray(la)
      case da: Array[Double] => formatDoubleArray(da)
      case sa: Array[String] => formatStringArray(sa)
      case h: HasToJSON => h.toJSON
      case t: Product => formatSeq(t.productIterator.toSeq)
      case j: JSONObject => j match {
        case _ if j.isMap => toJSON(j.toMap)
        case _ if j.isArray => toJSON(j.toArray)
        case _ if j.isBoolean => toJSON(j.toBoolean)
        case _ if j.isNumber => toJSON(j.toNumber)
        case _ => toJSON(j.toString)
      }
      case _ => if (v == null)
        "null"
      else
        throw new IllegalArgumentException("Don't know how to format '%s' in JSON".format(v.toString))
    }
  }

  def formatString(s: String): String = {
    if (s == "\\N") {
      "null"
    } else {
      "\"" + escapeString(s) + "\""
    }
  }

  def isPrint(c: String): Boolean = """(\p{Print})""".r.findFirstIn(c).isDefined

  def escapeString(s: String): String = {
    //JSONValue.escape(s)
    s.map {
      case '\"' => "\\\""
      case '\\' => "\\\\"
      case c: Char if isPrint(c.toString) && c < 128 => c
      case '\b' => "\\b"
      case '\f' => "\\f"
      case '\n' => "\\n"
      case '\t' => "\\t"
      case '\r' => "\\r"
      case c: Char => "\\u%04x".format(c.toInt)
    }.mkString
  }

  def formatDate(d: java.util.Date): String = {
    "\"" + dateFormat.get.format(d) + "\""
  }

  def formatMap(m: Map[String, Any]): String = {
    "{" + m.map {kv => formatString(kv._1) + ":" + toJSON(kv._2)}.mkString(",") + "}"
  }

  def formatList(l: List[Any]): String = {
    "[" + l.map {e => toJSON(e)}.mkString(",") + "]"
  }

  def formatSeq(s: Seq[Any]): String = {
    "[" + s.map {e => toJSON(e)}.mkString(",") + "]"
  }

  def formatByteArray(array: Array[Byte]): String = {
    val out: StringBuilder = new StringBuilder()
    out.append("\"")
    for (b <- array) {
      out.append("%02x" format b)
    }
    out.append("\"")
    out.toString()
  }

  def formatIntArray(array: Array[Int]): String = "[" + array.map(i => toJSON(i)).mkString(",") + "]"

  def formatLongArray(array: Array[Long]): String = "[" + array.map(i => toJSON(i)).mkString(",") + "]"

  def formatDoubleArray(array: Array[Double]): String = "[" + array.map(i => toJSON(i)).mkString(",") + "]"

  def formatStringArray(array: Array[String]): String = "[" + array.map(i => toJSON(i)).mkString(",") + "]"
}
