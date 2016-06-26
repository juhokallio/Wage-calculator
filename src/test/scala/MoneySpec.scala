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
}
