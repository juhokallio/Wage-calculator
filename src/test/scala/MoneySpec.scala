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
}
