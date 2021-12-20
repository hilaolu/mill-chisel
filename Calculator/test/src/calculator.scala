package Calculator

import chiseltest._
import chisel3._
import chisel3.util._
import chisel3.stage.ChiselStage
import org.scalatest.{Matchers, FlatSpec}

import scala.math.pow

import Calculator._

class CalculatorTest extends FlatSpec with ChiselScalatestTester with Matchers {
    behavior of ""

    it should "work" in {
        test(new Calculator) { c =>
            c.reset.poke(true.B)
            c.clock.step()
            c.reset.poke(false.B)
            c.io.in.op_1.poke(3.U)
            c.io.in.op_2.poke(6.U)
            c.io.in.start.poke(true.B)
            c.io.in.opcode.poke("b010".U)
            c.clock.step()
            c.io.in.start.poke(false.B)
            for (i <- 0 until 40) {
                //println(c.io.out.result.peek())
                c.clock.step()
            }

            //c.io.out.result.expect(18.U)

            c.io.in.op_2.poke(5.U)
            c.io.in.start.poke(true.B)
            c.io.in.opcode.poke("b010".U)
            c.clock.step()
            c.io.in.start.poke(false.B)

            for (i <- 0 until 40) {
                //println(c.io.out.result.peek())
                c.clock.step()
            }

            c.io.in.op_2.poke(5.U)
            c.io.in.start.poke(true.B)
            c.io.in.opcode.poke("b101".U)
            c.clock.step()
            c.io.in.start.poke(false.B)

            for (i <- 0 until 40) {
                //println(c.io.out.result.peek())
                //println(c.io.out.op_2.peek())
                c.clock.step()
            }

            // c.io.out.result.expect(18.U)

            //reset
            c.reset.poke(true.B)
            c.clock.step()
            c.reset.poke(false.B)

            val INT_MAX = pow(2, 32).toLong - 1

            val r = scala.util.Random

        // for (i <- 0 until 10000) {
        //     val random_op_1 = r.nextLong.abs % (INT_MAX + 1)
        //     val random_op_2 = r.nextLong.abs % (INT_MAX + 1)
        //     val random_result =
        //         (random_op_1 + random_op_2) % (INT_MAX + 1)
        //     println(
        //       "let's test " + random_op_1 + "+" + random_op_2 + "=" + random_result
        //     )
        //     c.io.in.op_1.poke(random_op_1.asUInt(32.W))
        //     c.io.in.op_2.poke(random_op_2.asUInt(32.W))
        //     c.clock.step()
        //     c.io.out.result.expect(random_result.asUInt(32.W))
        // }

        }
    }
}
