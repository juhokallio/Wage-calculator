package juho.wagecalculator

import org.scalatest.FlatSpec

/**
  * Created by juho on 3/23/16.
  */
class WageCalculatorSpec extends FlatSpec {

  "totalQuarters" should "be correct" in {
    assert(WageCalculator.totalQuarters(TimeStamp("1:00"), TimeStamp("2:00")) == 4)
    assert(WageCalculator.totalQuarters(TimeStamp("4:00"), TimeStamp("9:00")) == 20)
  }

  "totalQuarters" should "be correct with overnight shift" in {
    assert(WageCalculator.totalQuarters(TimeStamp("23:00"), TimeStamp("1:00")) == 8)
  }


  "normalQuarters" should "be correct with short new hours" in {
    assert(WageCalculator.normalQuarters(0, TimeStamp("11:00"), TimeStamp("12:00")) == 4)
    assert(WageCalculator.normalQuarters(30, TimeStamp("11:00"), TimeStamp("12:00")) == 2)
    assert(WageCalculator.normalQuarters(32, TimeStamp("11:00"), TimeStamp("12:00")) == 0)
    assert(WageCalculator.normalQuarters(34, TimeStamp("11:00"), TimeStamp("12:00")) == 0)
    assert(WageCalculator.normalTime(TimeStamp("4:00"), TimeStamp("9:00")) == 12)
    assert(WageCalculator.normalTime(TimeStamp("17:15"), TimeStamp("19:00")) == 3)
  }

  "normalQuarters" should "be correct when outside normal hours" in {
    assert(WageCalculator.normalQuarters(0, TimeStamp("19:00"), TimeStamp("22:00")) == 0)
    assert(WageCalculator.normalQuarters(30, TimeStamp("23:00"), TimeStamp("5:00")) == 0)
  }

  "normalQuarters" should "be correct with long new hours" in {
    assert(WageCalculator.normalQuarters(0, TimeStamp("1:00"), TimeStamp("19:00")) == 32)
    assert(WageCalculator.normalQuarters(1, TimeStamp("7:00"), TimeStamp("22:00")) == 31)
    assert(WageCalculator.normalQuarters(0, TimeStamp("5:00"), TimeStamp("17:00")) == 32)
    assert(WageCalculator.normalQuarters(1, TimeStamp("1:00"), TimeStamp("20:00")) == 31)

  }

  "normalTime" should "count all the hours between 6:00-18:00" in {
    assert(WageCalculator.normalTime(TimeStamp("6:00"), TimeStamp("18:00")) == 48)
    assert(WageCalculator.normalTime(TimeStamp("5:00"), TimeStamp("19:00")) == 48)
  }

  "normalQuarters" should "be correct with night work included" in {
    assert(WageCalculator.normalQuarters(0, TimeStamp("3:00"), TimeStamp("1:00")) == 32)
    assert(WageCalculator.normalQuarters(0, TimeStamp("17:00"), TimeStamp("1:00")) == 4)
    assert(WageCalculator.normalQuarters(32, TimeStamp("17:00"), TimeStamp("1:00")) == 0)
    assert(WageCalculator.normalQuarters(30, TimeStamp("17:00"), TimeStamp("1:00")) == 2)
  }

  "normalQuarters" should "be correct with night work included with both normal morning and evening hours" in {
    assert(WageCalculator.normalQuarters(0, TimeStamp("17:00"), TimeStamp("7:00")) == 8)
    assert(WageCalculator.normalQuarters(4, TimeStamp("17:00"), TimeStamp("7:00")) == 8)
    assert(WageCalculator.normalQuarters(28, TimeStamp("17:00"), TimeStamp("7:00")) == 4)
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
    assert(WageCalculator.eveningWork(TimeStamp("17:00"), TimeStamp("2:00")) == 32)
    assert(WageCalculator.eveningWork(TimeStamp("3:00"), TimeStamp("2:00")) == 44)
  }


  "plus25work" should "be correct with normal days" in {
    assert(WageCalculator.plus25work(0, TimeStamp("6:00"), TimeStamp("7:00")) == 0)
    assert(WageCalculator.plus25work(0, TimeStamp("6:00"), TimeStamp("14:00")) == 0)
    assert(WageCalculator.plus25work(0, TimeStamp("6:00"), TimeStamp("14:30")) == 2)
    assert(WageCalculator.plus25work(0, TimeStamp("6:00"), TimeStamp("16:00")) == 8)
    assert(WageCalculator.plus25work(0, TimeStamp("6:00"), TimeStamp("17:00")) == 8)
  }

  "plus25work" should "be correct with prior work" in {
    assert(WageCalculator.plus25work(1, TimeStamp("6:00"), TimeStamp("14:00")) == 1)
    assert(WageCalculator.plus25work(9, TimeStamp("6:00"), TimeStamp("14:00")) == 8)
  }

  "plus25work" should "be empty with overnight shift" in {
    assert(WageCalculator.plus25work(0, TimeStamp("22:00"), TimeStamp("7:00")) == 4)
    assert(WageCalculator.plus25work(2, TimeStamp("22:00"), TimeStamp("7:00")) == 6)
    assert(WageCalculator.plus25work(22, TimeStamp("22:00"), TimeStamp("7:00")) == 8)
    assert(WageCalculator.plus25work(2, TimeStamp("22:00"), TimeStamp("6:00")) == 2)
  }


  "plus50work" should "be correct with normal days" in {
    assert(WageCalculator.plus50work(0, TimeStamp("6:00"), TimeStamp("7:00")) == 0)
    assert(WageCalculator.plus50work(0, TimeStamp("6:00"), TimeStamp("16:00")) == 0)
    assert(WageCalculator.plus50work(0, TimeStamp("6:00"), TimeStamp("17:15")) == 5)
    assert(WageCalculator.plus50work(0, TimeStamp("6:00"), TimeStamp("18:00")) == 8)
    assert(WageCalculator.plus50work(0, TimeStamp("7:00"), TimeStamp("19:00")) == 8)
    assert(WageCalculator.plus50work(0, TimeStamp("7:00"), TimeStamp("20:00")) == 8)
  }

  "plus50work" should "be correct with prior work" in {
    assert(WageCalculator.plus50work(1, TimeStamp("6:00"), TimeStamp("16:00")) == 1)
    assert(WageCalculator.plus50work(9, TimeStamp("6:00"), TimeStamp("16:00")) == 8)
  }

  "plus50work" should "be empty with overnight shift" in {
    assert(WageCalculator.plus50work(0, TimeStamp("22:00"), TimeStamp("9:00")) == 4)
    assert(WageCalculator.plus50work(2, TimeStamp("22:00"), TimeStamp("9:00")) == 6)
    assert(WageCalculator.plus50work(22, TimeStamp("22:00"), TimeStamp("9:00")) == 8)
    assert(WageCalculator.plus50work(2, TimeStamp("22:00"), TimeStamp("8:00")) == 2)
  }


  "plus100work" should "be correct with normal days" in {
    assert(WageCalculator.plus100work(0, TimeStamp("6:00"), TimeStamp("7:00")) == 0)
    assert(WageCalculator.plus100work(0, TimeStamp("6:00"), TimeStamp("18:00")) == 0)
    assert(WageCalculator.plus100work(0, TimeStamp("6:00"), TimeStamp("18:15")) == 1)
    assert(WageCalculator.plus100work(0, TimeStamp("6:00"), TimeStamp("23:00")) == 20)
  }

  "plus100work" should "be correct with prior work" in {
    assert(WageCalculator.plus100work(1, TimeStamp("6:00"), TimeStamp("18:00")) == 1)
  }

  "plus100work" should "be empty with overnight shift" in {
    assert(WageCalculator.plus100work(0, TimeStamp("22:00"), TimeStamp("11:00")) == 4)
    assert(WageCalculator.plus100work(2, TimeStamp("22:00"), TimeStamp("11:00")) == 6)
    assert(WageCalculator.plus100work(12, TimeStamp("22:00"), TimeStamp("11:00")) == 16)
    assert(WageCalculator.plus100work(2, TimeStamp("22:00"), TimeStamp("10:00")) == 2)
  }


  "compensation" should "be correct with normal day" in {
    assert(WageCalculator.compensation(0, TimeStamp("11:00"), TimeStamp("12:00")).toString == "$3.75")
    assert(WageCalculator.compensation(0, TimeStamp("11:00"), TimeStamp("11:30")).toString == "$1.88")
  }

  "compensation" should "be correct with evening work" in {
    assert(WageCalculator.compensation(0, TimeStamp("22:00"), TimeStamp("23:00")).toString == "$4.90")
    assert(WageCalculator.compensation(0, TimeStamp("22:00"), TimeStamp("22:15")).toString == "$1.23")
  }

  "compensation" should "be correct with long day" in {
    assert(WageCalculator.compensation(0, TimeStamp("6:00"), TimeStamp("14:00")).toString == "$30.00")
    assert(WageCalculator.compensation(0, TimeStamp("6:00"), TimeStamp("15:00")).toString == "$34.69")
  }
}
