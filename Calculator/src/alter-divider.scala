package Calculator

import chisel3._
import chisel3.util._

//warning: divide zero is not considered.
class AlterDivider extends MultiIOModule {

    val todo = DontCare

    val io = IO(
      new Protocol
    )

    val busy = RegInit(Bool(), false.B)
    val dividend = Reg(UInt(33.W))
    val divisor = Reg(UInt(63.W))
    val quotient = Reg(UInt(32.W))

    val divisor_greater = divisor(62, 32) =/= 0.U
    val dividend_msb = dividend(32, 32)

    val start = Reg(Bool())

    when(io.in.start && ~busy) {
        dividend := Cat(0.U(1.W), io.in.op_1)
        divisor := Cat(io.in.op_2, 0.U(31.W))
        quotient := 0.U
        start := false.B
    }.elsewhen(~io.in.start && ~busy) {
        dividend := DontCare
        divisor := DontCare
        quotient := DontCare
        start := DontCare
    }.elsewhen(divisor_greater) {
        dividend := dividend
        divisor := divisor >> 1.U
        quotient := 0.U
        start := false.B
    }.otherwise {
        //from here divisor may be smaller than dividend

        start := true.B
        when(dividend_msb === 1.U) {
            dividend := dividend + divisor(31, 0)
        }.otherwise {
            dividend := dividend - divisor(31, 0)
        }

        quotient := Cat(quotient(30, 0), Mux(start, ~dividend_msb, 0.U))

        divisor := divisor >> 1.U

    }

    when(io.in.start && ~busy) {
        busy := true.B
    }.otherwise {
        busy := busy
    }

    io.out.result := Cat(quotient(30, 0), Mux(start, ~dividend_msb, 0.U))
    io.out.end := true.B
    io.out.busy := busy

}
