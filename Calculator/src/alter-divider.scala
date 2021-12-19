//don't use this
package Calculator

import chisel3._
import chisel3.util._
import chisel3.experimental.verification

class DividerProtocol extends Protocol {

    override val out = new Bundle {
        val result = Output(UInt(32.W))
        val end = Output(Bool())
        val busy = Output(Bool())
        val rest = Output(UInt(32.W))
    }
}

//warning: divide zero is not considered.
class AlterDivider extends MultiIOModule {

    val todo = DontCare

    val io = IO(
      new DividerProtocol
    )

    val debug_io = IO(new Bundle {
        val quotient = Output(UInt(32.W))
        val dividend = Output(UInt(33.W))
    })

    val busy = RegInit(Bool(), false.B)
    val dividend = Reg(UInt(33.W))
    val divisor = Reg(UInt(63.W))
    val quotient = Reg(UInt(31.W))

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

        quotient := Cat(quotient, Mux(start, ~dividend_msb, 0.U))

        divisor := divisor >> 1.U

    }

    val count = Reg(UInt(5.W))
    val rest = Reg(UInt(32.W))

    when(io.in.start && ~busy) {
        busy := true.B
        count := 0.U
    }.elsewhen(busy && count === 30.U) {
        busy := true.B
        count := count + 1.U
    }.elsewhen(busy && count === 31.U) {
        busy := false.B
        count := 0.U
        // verification.`package`.assert(rest =/= 2.U)
        //try to retrive a possible rest
    }.otherwise {
        busy := busy
        count := count + 1.U
    }

    // when(count === 31.U) {
    //     rest := divisor(31, 0)
    // }

    rest := dividend + divisor(32, 1)

    io.out.result := Cat(quotient, Mux(start, ~dividend_msb, 0.U))
    io.out.end := ~busy
    io.out.busy := busy
    io.out.rest := Mux(dividend_msb.asBool(), rest, dividend)

    debug_io.quotient := quotient
    debug_io.dividend := dividend

}
