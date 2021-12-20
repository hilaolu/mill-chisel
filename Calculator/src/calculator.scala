package Calculator

import chisel3._
import chisel3.util._

class Calculator extends MultiIOModule {

    val io = IO(new Bundle {
        val in = new Bundle {
            val start = Input(Bool())
            val op_1 = Input(UInt(8.W))
            val op_2 = Input(UInt(8.W))
            val opcode = Input(UInt(3.W))
        }

        val out = new Bundle {
            val en = Output(UInt(8.W))
            val cx = Output(UInt(8.W))
        }
    })

    val busy :: idle :: Nil = Enum(2)
    val state = Reg(UInt(1.W))

    val started = RegInit(Bool())
    val opcode = io.in.opcode

    val result = RegInit(UInt(32.W), 0.U)

    val subtracter = new Subtracter
    val adder = new Adder
    val multiplier = new Booth2
    val divider = new Divider2

    val op_1 = Wire(UInt(32.W))
    val op_2 = Wire(UInt(32.W))

    op_2 := Mux(io.in.opcode === "b101".U, result, io.in.op_2)
    op_1 := Mux(started, result, io.in.op_1)

    subtracter.io.in.op_1 := op_1
    subtracter.io.in.op_2 := op_2

    divider.io.in.op_1 := op_1
    divider.io.in.op_2 := op_2

    multiplier.io.in.op_1 := op_1
    multiplier.io.in.op_2 := op_2

    adder.io.in.op_1 := op_1
    adder.io.in.op_2 := op_2

    multiplier.io.in.start := Mux(opcode === "b010".U, io.in.start, false.B)
    multiplier.io.in.start := Mux(
      opcode === "b100".U || opcode === "b011".U,
      io.in.start,
      false.B
    )

    val lut = Array(
      "b000".U -> adder.io.out.result,
      "b001".U -> subtracter.io.out.result,
      "b010".U -> multiplier.io.out.result,
      "b011".U -> divider.io.out.result,
      "b100".U -> divider.io.out.rest,
      "b101".U -> multiplier.io.out.result
    )

    val new_result = MuxLookup(opcode, 0.U, lut)

    result := Mux(state === idle, new_result, result)

    val driver = Module(new DisplayDriver)

    //drive display
    for (i <- 0 until 8) {
        driver.io.in.hex_vec(i) <> result(i * 4 + 3, i * 4)
    }

    started := Mux(io.in.start, true.B, started)

    io.out.cx <> driver.io.out.cx
    io.out.en <> driver.io.out.index

}
