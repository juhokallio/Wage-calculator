package juho.wagecalculator

import org.scalatest.FlatSpec

/**
  * Created by juho on 6/26/16.
  */
class MoneySpec extends FlatSpec {

  "toString" should "be correct after construction" in {
    assert(Money.fromCurrency(1, 25).toString == "$1.25")
    assert(Money.fromCurrency(1, 0).toString == "$1.00")
    assert(Money.fromCurrency(0, 1).toString == "$0.01")
    assert(Money.fromCurrency(0, 99).toString == "$0.99")
    assert(Money.fromCurrency(0, 0).toString == "$0.00")
  }

  "multiply" should "work correctly" in {
    assert((Money.fromCurrency(0, 99) * 2).toString == "$1.98")
  }

  "plus" should "work correctly" in {
    assert((Money.fromCurrency(0, 99) + Money.fromCurrency(0, 1)).toString == "$1.00")
  }

  "plus" should "work correctly with different dividers" in {
    assert((Money.fromCurrency(0, 40).divideBy4() + Money.fromCurrency(0, 1)).toString == "$0.11")
  }

  "divideBy4" should "work correctly without fractions" in {
    assert(Money.fromCurrency(4, 4).divideBy4().toString == "$1.01")
    assert(Money.fromCurrency(1, 4).divideBy4().toString == "$0.26")
  }

  "divideBy4" should "work correctly with fractions" in {
    assert(Money.fromCurrency(0, 3).divideBy4().toString == "$0.01")
    assert(Money.fromCurrency(0, 2).divideBy4().toString == "$0.01")
    assert(Money.fromCurrency(0, 1).divideBy4().toString == "$0.00")
  }

  "divideBy4" should "work correctly with multiple divides" in {
    assert(Money.fromCurrency(0, 64).divideBy4().divideBy4().toString == "$0.04")
    assert(Money.fromCurrency(0, 3).divideBy4().divideBy4().toString == "$0.00")
    assert(Money.fromCurrency(0, 8).divideBy4().divideBy4().toString == "$0.01")
  }

  "operations" should "work correctly together" in {
    val quarterly: Money = Money.fromCurrency(3, 75).divideBy4()
    assert(quarterly.toString == "$0.94", "division failed")

    val extra25: Money = quarterly.divideBy4()
    assert(extra25.toString == "$0.23", "double division failed")

    val quarterWith25extra: Money = quarterly + extra25
    assert(quarterWith25extra.toString == "$1.17", "plus failed")

    val hourWith25extra: Money = quarterWith25extra * 4
    assert(hourWith25extra.toString == "$4.69", "multiply failed")
  }
}
