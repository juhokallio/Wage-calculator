package juho.wagecalculator

import org.scalatest.FlatSpec

/**
  * Created by juho on 3/23/16.
  */
class TimeStampSpec extends FlatSpec {

  "timestamp" should "have correct total quarters" in {
    assert(TimeStamp("1:00").quarters == 4)
    assert(TimeStamp("1:15").quarters == 5)
  }

  "gap" should "be correct when second timestamp is later" in {
    assert(TimeStamp("1:00").gap(TimeStamp("2:00")) == 4)
    assert(TimeStamp("1:00").gap(TimeStamp("2:15")) == 5)
  }

  "gap" should "be zero when second timestamp is not later" in {
    assert(TimeStamp("1:00").gap(TimeStamp("0:15")) == 0)
    assert(TimeStamp("2:45").gap(TimeStamp("2:45")) == 0)
  }

  "isBefore" should "return true when the compared timestamp is later" in {
    assert(TimeStamp("4:30").isBefore(TimeStamp("6:00")))
  }

  "isBefore" should "return false when the compared timestamp is earlier" in {
    assert(!TimeStamp("19:30").isBefore(TimeStamp("6:00")))
  }

  "isBefore" should "return false when the timestamps are the same" in {
    assert(!TimeStamp("6:00").isBefore(TimeStamp("6:00")))
  }

  "constructing" should "throw IllegalArgumentException with empty string" in {
    intercept[IllegalArgumentException] {
      TimeStamp("")
    }
  }

  "constructing" should "throw IllegalArgumentException with bad minutes" in {
    intercept[IllegalArgumentException] {
      TimeStamp("12:14")
    }
    intercept[IllegalArgumentException] {
      TimeStamp("12:60")
    }
  }

  "constructing" should "throw IllegalArgumentException with bad hours" in {
    intercept[IllegalArgumentException] {
      TimeStamp("25:00")
    }
  }

  "constructing" should "throw IllegalArgumentException with bad time with ok minutes and hours" in {
    intercept[IllegalArgumentException] {
      TimeStamp("24:15")
    }
  }
}
