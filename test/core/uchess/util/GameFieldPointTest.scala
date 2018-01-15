package core.uchess.util

import core.uchess.util.GameFieldPoint
import core.uchess.util.Point
import org.scalatest.WordSpec
import org.scalatest.Matchers

class GameFieldPointTest extends WordSpec with Matchers {
  "A GamFieldPoint should encode an real Point" when{
    "it gets valid coordination's" in{
      val pointToTest = GameFieldPoint("b",7)
      pointToTest shouldBe Point(1,6)
    }
  }
  "A GamFieldPoint should return an Point including Int.MinValue" when{
    "it gets invalid coordination's" in{
      val pointToTest = GameFieldPoint("zzz",666)
      pointToTest shouldBe Point(-2147483648,-2147483648)
    }
  }
}
