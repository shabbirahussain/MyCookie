package org.hshabbir.practice.csv_parser

import org.scalatest.PrivateMethodTester
import org.scalatest.flatspec.AnyFlatSpec

import scala.collection.parallel.ParIterable

class SolutionTest extends AnyFlatSpec with PrivateMethodTester {
  val parseInputs = PrivateMethod[ParIterable[(String, Int)]]('parseInputs)
  "Timezones" should "be converted to UTC" in {
    val actual = Solution invokePrivate parseInputs(
      Seq(
        "a,2018-12-08T23:00:00-01:00", // This is +1d in UTC
        "b,2018-12-08T23:00:00+00:00", // This is +0d in UTC
        "c,2018-12-08T23:00:00+01:00", // This is +0d in UTC
      ).iterator
    )
    assertResult(ParIterable(
      ("a", 20181209),
      ("b", 20181208),
      ("c", 20181208),
    ))(actual)
  }

  "Invalid inputs" should "be ignored by the parser" in {
    val actual = Solution invokePrivate parseInputs(
      Seq(
        "a,,2018-12-08T23:00:00-01:00", // More columns
        "2018-12-08T23:00:00-01:00",    // Less columns
        "",                             // Empty Inputs
        "a,2018-99-08T23:00:00-01:00",  // Invalid Dates
      ).iterator
    )
    assert(actual.isEmpty)
  }

  "getMostActiveCookies" should "find multiple most active cookies across timezones" in {
    val actual = Solution.getMostActiveCookies(Seq(
      "a,2018-12-08T23:00:00-01:00", // Note: this is next day in UTC
      "a,2018-12-09T23:00:00+00:00",
      "b,2018-12-09T23:00:00+00:00",
      "b,2018-12-09T03:00:00+00:00",
      "c,2018-12-09T13:00:00+00:00",
      "d,2018-12-07T01:00:00+00:00",
      "d,2018-12-07T13:00:00+00:00",
      "d,2018-12-07T02:00:00+00:00", // `d` is most prominent but falls on a wrong date.
    ).iterator, 20181209)
    assertResult(Set(
      "a", "b"
    ))(actual.toSet)
  }

  "Wrong dates" should "yield empty results" in {
    val actual = Solution.getMostActiveCookies(Seq(
      "a,2018-12-08T23:00:00-01:00",
    ).iterator, 20201209)
    assert(actual.isEmpty)
  }
}
