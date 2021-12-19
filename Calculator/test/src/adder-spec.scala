package Calculator

import chiseltest._
import chisel3._
import chisel3.util._
import chisel3.stage.ChiselStage
import org.scalatest.{Matchers, FlatSpec}

import Calculator._

class AdderTest extends FlatSpec with ChiselScalatestTester with Matchers {
    behavior of ""

    it should "work" in {
        test(new Adder) { c =>
            c.reset.poke(true.B)
            c.clock.step()
            c.reset.poke(false.B)
            c.io.in.op_1.poke(1.U)
            c.io.in.op_2.poke(1.U)
            c.clock.step()
            c.io.out.result.expect(2.U)

            //reset
            c.reset.poke(true.B)
            c.clock.step()
            c.reset.poke(false.B)

        // val r = scala.util.Random
        // var seq: List[Int] = List()
        // val target_seq = List(0, 1, 0, 0, 1)
        // for (i <- 0 until 10000) {
        //     val random_bool = r.nextBoolean
        //     if (random_bool) {
        //         seq = 1 :: seq
        //         c.io.in.signal.poke(1.U)
        //         print(1)
        //     } else {
        //         seq = 0 :: seq
        //         c.io.in.signal.poke(0.U)
        //         print(0)
        //     }
        //     if (seq.length > 5) {
        //         seq = seq.dropRight(1)
        //     }
        //     c.clock.step()
        //     if (seq == target_seq) {
        //         c.io.out.found.expect(1.U)
        //         print("found\n")
        //     } else {
        //         c.io.out.found.expect(0.U)
        //     }
        // }

        }
    }
}
