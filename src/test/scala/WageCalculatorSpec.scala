import org.scalactic.TolerantNumerics
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by juho on 3/23/16.
  */
class WageCalculatorSpec extends FlatSpec {

  "totalQuarters" should "be correct" in {
    assert(WageCalculator.totalQuarters(TimeStamp("1:00"), TimeStamp("2:00")) == 4)
  }

  "normalQuarters" should "be correct with short new hours" in {
    assert(WageCalculator.normalQuarters(0, TimeStamp("1:00"), TimeStamp("2:00")) == 4)
    assert(WageCalculator.normalQuarters(30, TimeStamp("1:00"), TimeStamp("2:00")) == 2)
    assert(WageCalculator.normalQuarters(32, TimeStamp("1:00"), TimeStamp("2:00")) == 0)
    assert(WageCalculator.normalQuarters(34, TimeStamp("1:00"), TimeStamp("2:00")) == 0)
  }

  "normalQuarters" should "be correct with long new hours" in {
    assert(WageCalculator.normalQuarters(0, TimeStamp("1:00"), TimeStamp("9:00")) == 32)
    assert(WageCalculator.normalQuarters(1, TimeStamp("1:00"), TimeStamp("9:00")) == 31)
    assert(WageCalculator.normalQuarters(0, TimeStamp("1:00"), TimeStamp("10:00")) == 32)
    assert(WageCalculator.normalQuarters(1, TimeStamp("1:00"), TimeStamp("10:00")) == 31)
  }

  "eveningWork" should "be correct with no work outside normal hours" in {
    assert(WageCalculator.eveningWork(TimeStamp("6:00"), TimeStamp("9:00")) == 0)
    assert(WageCalculator.eveningWork(TimeStamp("13:30"), TimeStamp("18:00")) == 0)
    assert(WageCalculator.eveningWork(TimeStamp("11:15"), TimeStamp("14:00")) == 0)
    assert(WageCalculator.eveningWork(TimeStamp("6:00"), TimeStamp("18:00")) == 0)
  }

  "eveningWork" should "be correct with early start" in {
    assert(WageCalculator.eveningWork(TimeStamp("4:00"), TimeStamp("9:00")) == 8)
    assert(WageCalculator.eveningWork(TimeStamp("5:15"), TimeStamp("18:00")) == 3)
    assert(WageCalculator.eveningWork(TimeStamp("3:00"), TimeStamp("4:00")) == 4)
  }

  "eveningWork" should "be correct with early morning and late evening" in {
    assert(WageCalculator.eveningWork(TimeStamp("5:00"), TimeStamp("19:00")) == 8)
  }

  "eveningWork" should "be correct with late finish" in {
    assert(WageCalculator.eveningWork(TimeStamp("18:00"), TimeStamp("19:00")) == 4)
    assert(WageCalculator.eveningWork(TimeStamp("17:15"), TimeStamp("18:15")) == 1)
    assert(WageCalculator.eveningWork(TimeStamp("19:00"), TimeStamp("20:00")) == 4)
  }

  "eveningWork" should "be correct with night work" in {
    assert(WageCalculator.eveningWork(TimeStamp("22:00"), TimeStamp("2:00")) == 16)
    assert(WageCalculator.eveningWork(TimeStamp("17:00"), TimeStamp("2:00")) == 36)
    assert(WageCalculator.eveningWork(TimeStamp("3:00"), TimeStamp("2:00")) == 88)
  }
}
