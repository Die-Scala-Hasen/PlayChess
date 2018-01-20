package core.uchess.model.impl

import core.uchess.util.Point
import org.scalatest.WordSpec
import org.scalatest.Matchers
import play.api.libs.json.Json

class GameFieldTest extends WordSpec with Matchers {
  "A Gamefield should be scalable and contains specific figures " when {
    "it is very small" in {
      val gameFieldToTest = GameField(1)
      gameFieldToTest.size shouldBe 1
      gameFieldToTest.gameField.get(Point(0, 0)) shouldBe Some(King('b'))
    }
    "it is small" in {
      val gameFieldToTest = GameField(3)
      gameFieldToTest.size shouldBe 3
      gameFieldToTest.gameField.get(Point(1, 0)) shouldBe Some(King('b'))
      gameFieldToTest.gameField.get(Point(1, 2)) shouldBe Some(King('w'))
    }
    "it is normal" in {
      val gameFieldToTest = GameField(8)
      gameFieldToTest.size shouldBe 8

      gameFieldToTest.gameField.get(Point(0, 0)) shouldBe Some(Tower('b'))
      gameFieldToTest.gameField.get(Point(7, 0)) shouldBe Some(Tower('b'))
      gameFieldToTest.gameField.get(Point(0, 7)) shouldBe Some(Tower('w'))
      gameFieldToTest.gameField.get(Point(7, 7)) shouldBe Some(Tower('w'))

      gameFieldToTest.gameField.get(Point(1, 0)) shouldBe Some(Knight('b'))
      gameFieldToTest.gameField.get(Point(6, 0)) shouldBe Some(Knight('b'))
      gameFieldToTest.gameField.get(Point(1, 7)) shouldBe Some(Knight('w'))
      gameFieldToTest.gameField.get(Point(6, 7)) shouldBe Some(Knight('w'))

      gameFieldToTest.gameField.get(Point(2, 0)) shouldBe Some(Bishop('b'))
      gameFieldToTest.gameField.get(Point(5, 0)) shouldBe Some(Bishop('b'))
      gameFieldToTest.gameField.get(Point(2, 7)) shouldBe Some(Bishop('w'))
      gameFieldToTest.gameField.get(Point(5, 7)) shouldBe Some(Bishop('w'))

      gameFieldToTest.gameField.get(Point(3, 0)) shouldBe Some(Queen('b'))
      gameFieldToTest.gameField.get(Point(3, 7)) shouldBe Some(Queen('w'))

      gameFieldToTest.gameField.get(Point(4, 0)) shouldBe Some(King('b'))
      gameFieldToTest.gameField.get(Point(4, 7)) shouldBe Some(King('w'))

      gameFieldToTest.gameField.get(Point(0, 1)) shouldBe Some(Pawn('b'))
      gameFieldToTest.gameField.get(Point(1, 1)) shouldBe Some(Pawn('b'))
      gameFieldToTest.gameField.get(Point(2, 1)) shouldBe Some(Pawn('b'))
      gameFieldToTest.gameField.get(Point(3, 1)) shouldBe Some(Pawn('b'))
      gameFieldToTest.gameField.get(Point(4, 1)) shouldBe Some(Pawn('b'))
      gameFieldToTest.gameField.get(Point(5, 1)) shouldBe Some(Pawn('b'))
      gameFieldToTest.gameField.get(Point(6, 1)) shouldBe Some(Pawn('b'))
      gameFieldToTest.gameField.get(Point(7, 1)) shouldBe Some(Pawn('b'))

      gameFieldToTest.gameField.get(Point(0, 6)) shouldBe Some(Pawn('w'))
      gameFieldToTest.gameField.get(Point(1, 6)) shouldBe Some(Pawn('w'))
      gameFieldToTest.gameField.get(Point(2, 6)) shouldBe Some(Pawn('w'))
      gameFieldToTest.gameField.get(Point(3, 6)) shouldBe Some(Pawn('w'))
      gameFieldToTest.gameField.get(Point(4, 6)) shouldBe Some(Pawn('w'))
      gameFieldToTest.gameField.get(Point(5, 6)) shouldBe Some(Pawn('w'))
      gameFieldToTest.gameField.get(Point(6, 6)) shouldBe Some(Pawn('w'))
      gameFieldToTest.gameField.get(Point(7, 6)) shouldBe Some(Pawn('w'))


    }
    "it is big" in {
      val gameFieldToTest = GameField(16)
      gameFieldToTest.size shouldBe 16
      gameFieldToTest.gameField.get(Point(8, 0)) shouldBe Some(King('b'))
      gameFieldToTest.gameField.get(Point(8, 15)) shouldBe Some(King('w'))
    }
    "it is very big" in {
      val gameFieldToTest = GameField(50)
      gameFieldToTest.size shouldBe 50
      gameFieldToTest.gameField.get(Point(25, 0)) shouldBe Some(King('b'))
      gameFieldToTest.gameField.get(Point(25, 49)) shouldBe Some(King('w'))
    }
  }
  "A Gamefield" should {
    "have a specific toString Method" in {
      val printOfGameField = "\n  " +
        "| a| b| c| d| e| f| g| h|\n" +
        "==+-----------------------+\n" +
        "1 |bT|bk|bB|bQ|bK|bB|bk|bT|\n" +
        "2 |bP|bP|bP|bP|bP|bP|bP|bP|\n" +
        "3 |  |  |  |  |  |  |  |  |\n" +
        "4 |  |  |  |  |  |  |  |  |\n" +
        "5 |  |  |  |  |  |  |  |  |\n" +
        "6 |  |  |  |  |  |  |  |  |\n" +
        "7 |wP|wP|wP|wP|wP|wP|wP|wP|\n" +
        "8 |wT|wk|wB|wQ|wK|wB|wk|wT|\n" +
        "==+-----------------------+"

      val gameFieldToTest = GameField(8)
      gameFieldToTest.toString shouldBe printOfGameField
    }
    "have a specific toJson Method" in {

      val jsonOfGameField = "{\"gamefield\":[" +
        "{\"figur\":\"bTower\",\"x\":0,\"y\":0}," +
        "{\"figur\":\"bKnight\",\"x\":1,\"y\":0}," +
        "{\"figur\":\"bBishop\",\"x\":2,\"y\":0}," +
        "{\"figur\":\"bQueen\",\"x\":3,\"y\":0}," +
        "{\"figur\":\"bKing\",\"x\":4,\"y\":0}," +
        "{\"figur\":\"bBishop\",\"x\":5,\"y\":0}," +
        "{\"figur\":\"bKnight\",\"x\":6,\"y\":0}," +
        "{\"figur\":\"bTower\",\"x\":7,\"y\":0}," +
        "{\"figur\":\"bPawn\",\"x\":0,\"y\":1}," +
        "{\"figur\":\"bPawn\",\"x\":1,\"y\":1}," +
        "{\"figur\":\"bPawn\",\"x\":2,\"y\":1}," +
        "{\"figur\":\"bPawn\",\"x\":3,\"y\":1}," +
        "{\"figur\":\"bPawn\",\"x\":4,\"y\":1}," +
        "{\"figur\":\"bPawn\",\"x\":5,\"y\":1}," +
        "{\"figur\":\"bPawn\",\"x\":6,\"y\":1}," +
        "{\"figur\":\"bPawn\",\"x\":7,\"y\":1}," +
        "{\"figur\":\"wPawn\",\"x\":0,\"y\":6}," +
        "{\"figur\":\"wPawn\",\"x\":1,\"y\":6}," +
        "{\"figur\":\"wPawn\",\"x\":2,\"y\":6}," +
        "{\"figur\":\"wPawn\",\"x\":3,\"y\":6}," +
        "{\"figur\":\"wPawn\",\"x\":4,\"y\":6}," +
        "{\"figur\":\"wPawn\",\"x\":5,\"y\":6}," +
        "{\"figur\":\"wPawn\",\"x\":6,\"y\":6}," +
        "{\"figur\":\"wPawn\",\"x\":7,\"y\":6}," +
        "{\"figur\":\"wTower\",\"x\":0,\"y\":7}," +
        "{\"figur\":\"wKnight\",\"x\":1,\"y\":7}," +
        "{\"figur\":\"wBishop\",\"x\":2,\"y\":7}," +
        "{\"figur\":\"wQueen\",\"x\":3,\"y\":7}," +
        "{\"figur\":\"wKing\",\"x\":4,\"y\":7}," +
        "{\"figur\":\"wBishop\",\"x\":5,\"y\":7}," +
        "{\"figur\":\"wKnight\",\"x\":6,\"y\":7}," +
        "{\"figur\":\"wTower\",\"x\":7,\"y\":7}]}"

      val gameFieldToTest = GameField(8)
      gameFieldToTest.toJson shouldBe Json.parse(jsonOfGameField)
    }
  }
}
