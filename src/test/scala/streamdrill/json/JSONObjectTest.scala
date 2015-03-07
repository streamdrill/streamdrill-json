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

import org.junit.Assert._
import org.junit.Test

/**
 * Test the JSON Parser.
 *
 * @author Matthias L. Jugel
 */
class JSONObjectTest {
  @Test
  def testDoubleIsLong() {
    val double = JSONParser.parse("1.397e+09")
    assertEquals(1397000000L, double.toLong)
  }

  @Test(expected = classOf[ClassCastException])
  def testDoubleIsNotLong() {
    val double = JSONParser.parse("1.397e+02")
    assertEquals(139.7, double.toDouble, 0.0)
    double.toLong
  }

  @Test
  def testInfinity(): Unit = {
    assertEquals("0.0", JSONWriter.toJSON(+1/0.0))
    assertEquals("0.0", JSONWriter.toJSON(-1/0.0))
  }

  @Test
  def testNaN(): Unit = {
    val double = 0/0.0
    assertEquals("0.0", JSONWriter.toJSON(double))
  }
}
