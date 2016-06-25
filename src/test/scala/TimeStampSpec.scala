import org.scalactic.TolerantNumerics
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by juho on 3/23/16.
  */
class TimeStampSpec extends FlatSpec {

  "timestamp" should "have correct total quarters" in {
    assert(TimeStamp("1:00").quarters == 4)
    assert(TimeStamp("1:15").quarters == 5)
  }
}
