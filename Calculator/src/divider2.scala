package Calculator

import chisel3._
import chisel3.util._
import chisel3.experimental.verification

//warning: divide zero is not considered.
class Divider2 extends MultiIOModule {

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
    val quotient = Reg(UInt(32.W))

    val divisor_greater = divisor(62, 32) =/= 0.U

    val sub_result = dividend - divisor(31, 0)
    val sub_result_msb = sub_result(32, 32)

    when(io.in.start && ~busy) {
        dividend := Cat(0.U(1.W), io.in.op_1)
        divisor := Cat(io.in.op_2, 0.U(31.W))
        quotient := 0.U
    }.elsewhen(divisor_greater && busy) {
        dividend := dividend
        divisor := divisor >> 1.U
        quotient := 0.U
    }.elsewhen(busy) {
        //from here divisor may be smaller than dividend

        dividend := Mux(sub_result_msb.asBool(), dividend, sub_result)

        quotient := Cat(quotient, ~sub_result_msb)

        divisor := divisor >> 1.U

    }.otherwise {
        dividend := DontCare
        divisor := DontCare
        quotient := DontCare
    }

    val count = Reg(UInt(5.W))

    when(io.in.start && ~busy) {
        busy := true.B
        count := 0.U
    }.elsewhen(busy && count === 31.U) {
        busy := false.B
        count := 0.U
    }.otherwise {
        busy := busy
        count := count + 1.U
    }

    io.out.result := quotient
    io.out.end := ~busy
    io.out.busy := busy
    io.out.rest := dividend

    debug_io.quotient := quotient
    debug_io.dividend := dividend
    // verification.`package`.assert(rest =/= 2.U)

}
