package Calculator

import chisel3._
import chisel3.util._

class Booth2 extends MultiIOModule {

    val io = IO(
      new Protocol
    )

    val multiplier = RegInit(UInt(33.W), 0.U)
    val multiplicand = Reg(UInt(32.W))
    val result = Reg(UInt(32.W))

    val busy = multiplier =/= 0.U

    val adder = Wire(UInt(32.W))

    val lut = Array(
      "b000".U -> 0.U,
      "b001".U -> multiplicand,
      "b010".U -> multiplicand,
      "b011".U -> (multiplicand << 1.U),
      "b100".U -> -(multiplicand << 1.U),
      "b101".U -> -multiplicand,
      "b110".U -> -multiplicand,
      "b111".U -> 0.U
    )

    adder := MuxLookup(multiplier(2, 0), 0.U, lut)

    when(io.in.start && ~busy) {
        multiplicand := io.in.op_1
        multiplier := Cat(io.in.op_2, 0.U(1.W))
        result := 0.U
    }.elsewhen(multiplier =/= 0.U && busy) {
        //todo
        multiplicand := multiplicand << 2.U
        multiplier := multiplier >> 2.U
        result := result + adder

    }.otherwise {
        multiplicand := multiplicand
        multiplier := multiplier
        result := result
        //don't flip to save power
    }

    io.out.result := result

    io.out.end := ~busy && ~io.in.start
    io.out.busy := busy

}
